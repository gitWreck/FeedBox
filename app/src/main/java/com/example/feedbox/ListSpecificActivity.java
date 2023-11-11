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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListSpecificActivity extends AppCompatActivity {

    List<ListSpecificHelper> listSpecificHelpers;
    ListSpecificAdapter listSpecificAdapter;

    RecyclerView recyclerView;

    String Sc_Name, Cat_Name;
    TextView tvSubName;

    LinearLayout linearLayoutBack, linearLayoutSubCateg, linearLayoutSpeCom;
    CardView cardViewAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_specific_activty);

        Sc_Name = getIntent().getExtras().getString("sc_name");
        Cat_Name = getIntent().getExtras().getString("cat_name");

        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        cardViewAdd = findViewById(R.id.cardViewAddListSpec);
        recyclerView = findViewById(R.id.recyclerViewLS);
        tvSubName = findViewById(R.id.tvListSpecificName);

        listSpecificHelpers = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        listSpecificAdapter = new ListSpecificAdapter(listSpecificHelpers, this);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager3);
        recyclerView.setAdapter(listSpecificAdapter);

        tvSubName.setText(Sc_Name + " " + Cat_Name);

        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cardViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ListSpecificActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.list_specific_add_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                CardView cardViewSubmit;

                EditText etCompleSpe;

                cardViewSubmit = dialog.findViewById(R.id.cardViewSubmit);

                etCompleSpe = dialog.findViewById(R.id.txtComplaintSpe);

                etCompleSpe.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(etCompleSpe.getText().toString().isEmpty())
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

                        String url = URLDatabase.URL_CATEGORY_ADD;

                        RequestQueue queue = Volley.newRequestQueue(ListSpecificActivity.this);

                        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                dialog.dismiss();

                                LoadListSpecific();
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(ListSpecificActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                params.put("spc_d_name", etCompleSpe.getText().toString());
                                return params;
                            }
                        };
                        queue.add(request);
                    }
                });
                dialog.show();






            }
        });

        LoadListSpecific();
    }

    void LoadListSpecific() {
        listSpecificHelpers.clear();
        recyclerView.setAdapter(listSpecificAdapter);
        String url = URLDatabase.URL_LIST_SPECIFIC;

        RequestQueue queue = Volley.newRequestQueue(ListSpecificActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    if(!response.equals("[]"))
//                    {
                    listSpecificHelpers.clear();
//                        JSONObject jsonObject = new JSONObject(response);
//
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                            int spc_d_id = jsonObjectData.getInt("spc_d_id");
                            String spc_d_cat = jsonObjectData.getString("spc_d_cat");
                            String spc_d_name = jsonObjectData.getString("spc_d_name");
                            int sc_id = jsonObjectData.getInt("sc_id");
                            String sc_name = jsonObjectData.getString("sc_name");
                            int cat_id = jsonObjectData.getInt("cat_id");
                            String cat_name = jsonObjectData.getString("cat_name");
                            Log.d("TAG", "onResponse: " + spc_d_cat);
                            listSpecificHelpers.add(new ListSpecificHelper(spc_d_id,spc_d_cat,spc_d_name,sc_name,cat_name));
                        }
                        catch (Exception err) {
                            Toast.makeText(ListSpecificActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    recyclerView.setAdapter(listSpecificAdapter);
//                    }
                } catch (Exception e) {
                    Toast.makeText(ListSpecificActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(ListSpecificActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("sc_name", Sc_Name);
                params.put("cat_name", Cat_Name);
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