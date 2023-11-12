package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateReportActivity extends AppCompatActivity {

    List<GenerateReportHelper> generateReportHelpers;
    GenerateReportAdapter generateReportAdapter;

    RecyclerView recyclerView;

    LinearLayout linearLayoutBack;
    CardView cardViewAdd;

    String PickedStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_report_activity);

        linearLayoutBack = findViewById(R.id.linearLayoutBackAY);
        cardViewAdd = findViewById(R.id.cardViewAddAY);
        recyclerView = findViewById(R.id.recyclerViewAY);
        generateReportHelpers = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        generateReportAdapter = new GenerateReportAdapter(generateReportHelpers, this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager2);

        recyclerView.setAdapter(generateReportAdapter);


        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cardViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(GenerateReportActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.generate_report_add_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                CardView cardViewSubmit;
                Spinner spinner = dialog.findViewById(R.id.spnActAY);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        GenerateReportActivity.this,
                        R.array.listAYActive,
                        android.R.layout.simple_spinner_item
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        PickedStatus = adapterView.getItemAtPosition(i).toString();
                        Toast.makeText(adapterView.getContext(), "Selected: " + PickedStatus, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                EditText txtAY_add;

                cardViewSubmit = dialog.findViewById(R.id.cardViewSubmitAY);

                txtAY_add = dialog.findViewById(R.id.txtAY_Add);

                txtAY_add.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtAY_add.getText().toString().isEmpty())
                        {
                            cardViewSubmit.setEnabled(false);
                            cardViewSubmit.setClickable(false);
                            cardViewSubmit.setFocusable(false);
                            cardViewSubmit.setAlpha(0.2f);
                        }
                        else
                        {
                            cardViewSubmit.setEnabled(true);
                            cardViewSubmit.setClickable(true);
                            cardViewSubmit.setFocusable(true);
                            cardViewSubmit.setAlpha(1.0f);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                linearLayoutBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                cardViewSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = URLDatabase.URL_AY_ADD;

                        RequestQueue queue = Volley.newRequestQueue(GenerateReportActivity.this);

                        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                dialog.dismiss();

                                LoadAY();
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(GenerateReportActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/x-www-form-urlencoded; charset=UTF-8";
                            }

                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("ay_range", txtAY_add.getText().toString());
                                params.put("ay_status", PickedStatus);
                                return params;
                            }
                        };
                        queue.add(request);
                    }
                });
                dialog.show();
            }
        });

        LoadAY();
    }

    void LoadAY() {
        generateReportHelpers.clear();
        recyclerView.setAdapter(generateReportAdapter);
        String url = URLDatabase.URL_AY_LIST;

        RequestQueue queue = Volley.newRequestQueue(GenerateReportActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    if(!response.equals("[]")) {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                        for(int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObjectData = jsonArray.getJSONObject(i);
//                            String ay_range = jsonObjectData.getString("ay_range");
//                            String ay_status = jsonObjectData.getString("ay_status");
//                            int ay_id = jsonObjectData.getInt("ay_id");
//
//                            generateReportHelpers.add(new GenerateReportHelper(ay_range, ay_status, ay_id));
//                            Log.d("TAG", "onResponse: " + ay_range);
//                        }
//                    }
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                        String ay_range = jsonObjectData.getString("ay_range");
                        String ay_status = jsonObjectData.getString("ay_status");
                        int ay_id = jsonObjectData.getInt("ay_id");

                        generateReportHelpers.add(new GenerateReportHelper(ay_range, ay_status, ay_id));
                        Log.d("TAG", "onResponse: " + ay_range);
                    }

                    Log.d("TAG", "onResponse: " + response);
                    recyclerView.setAdapter((generateReportAdapter));
                } catch (Exception e) {
                    Toast.makeText(GenerateReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(GenerateReportActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("getListAY", "PickedStatus");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        queue.add(request);
    }
}