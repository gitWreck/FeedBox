package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class AdminActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    List<AdminHelper> adminHelpers;
    AdminAdapter adminAdapter;

    RecyclerView recyclerView;

    LinearLayout linearLayoutBack;
    CardView cardViewAdd;

    String[] departments = { "COED", "CBA", "CICS", "COE", "CIT" };
    String Department;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        adminHelpers = new ArrayList<>();

        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        cardViewAdd = findViewById(R.id.cardViewAdd);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        adminAdapter = new AdminAdapter(adminHelpers, this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager2);

        recyclerView.setAdapter(adminAdapter);

        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cardViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AdminActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.admin_add_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                TextView tvDialogName;
                LinearLayout linearLayoutBack;
                CardView cardViewSubmit;
                Spinner spinner;

                EditText txtFullName, txtPhoneNumber, txtEmail, txtPassword;

                tvDialogName = dialog.findViewById(R.id.tvDialogName);
                linearLayoutBack = dialog.findViewById(R.id.linearLayoutBack);
                cardViewSubmit = dialog.findViewById(R.id.cardViewSubmit);
                spinner = dialog.findViewById(R.id.spinner);

                txtFullName = dialog.findViewById(R.id.txtFullName);
                txtPhoneNumber = dialog.findViewById(R.id.txtPhoneNumber);
                txtEmail = dialog.findViewById(R.id.txtEmail);
                txtPassword = dialog.findViewById(R.id.txtPassword);

                txtFullName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtFullName.getText().toString().isEmpty() ||
                                txtPhoneNumber.getText().toString().isEmpty() ||
                                txtEmail.getText().toString().isEmpty() ||
                                txtPassword.getText().toString().isEmpty()
                        )
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

                txtPhoneNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtFullName.getText().toString().isEmpty() ||
                                txtPhoneNumber.getText().toString().isEmpty() ||
                                txtEmail.getText().toString().isEmpty() ||
                                txtPassword.getText().toString().isEmpty()
                        )
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

                txtEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtFullName.getText().toString().isEmpty() ||
                                txtPhoneNumber.getText().toString().isEmpty() ||
                                txtEmail.getText().toString().isEmpty() ||
                                txtPassword.getText().toString().isEmpty()
                        )
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

                txtPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtFullName.getText().toString().isEmpty() ||
                                txtPhoneNumber.getText().toString().isEmpty() ||
                                txtEmail.getText().toString().isEmpty() ||
                                txtPassword.getText().toString().isEmpty()
                        )
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

                spinner.setOnItemSelectedListener(AdminActivity.this);

                ArrayAdapter ad = new ArrayAdapter(AdminActivity.this, android.R.layout.simple_spinner_item, departments);

                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(ad);

                tvDialogName.setText("Add Sub-Admin");

                linearLayoutBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                cardViewSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = URLDatabase.URL_ADMIN_ADD;

                        RequestQueue queue = Volley.newRequestQueue(AdminActivity.this);

                        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                dialog.dismiss();

                                LoadAdmin();
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(AdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                params.put("full_name", txtFullName.getText().toString());
                                params.put("department", Department);
                                params.put("phone_number", txtPhoneNumber.getText().toString());
                                params.put("email", txtEmail.getText().toString());
                                params.put("password", txtPassword.getText().toString());
                                return params;
                            }
                        };
                        queue.add(request);
                    }
                });
                dialog.show();
            }
        });

        LoadAdmin();
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
    {
        Department = departments[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
    }

    void LoadAdmin()
    {
        adminHelpers.clear();
        recyclerView.setAdapter(adminAdapter);
        String url = URLDatabase.URL_ADMIN_LIST;

        RequestQueue queue = Volley.newRequestQueue(AdminActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals("[]"))
                    {
                        adminHelpers.clear();

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            try {
                                JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                                String adminID = jsonObjectData.getString("admin_id");
                                String fullName = jsonObjectData.getString("full_name");
                                String department = jsonObjectData.getString("department");
                                String contactNumber = jsonObjectData.getString("contact_number");
                                String email = jsonObjectData.getString("email");
                                String password = jsonObjectData.getString("password");

                                adminHelpers.add(new AdminHelper(adminID,fullName,department,contactNumber, email, password, true));
                            }

                            catch (Exception err)
                            {
                                Toast.makeText(AdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        recyclerView.setAdapter(adminAdapter);
                    }

                } catch (Exception e) {

                    Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(AdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                //params.put("email", Email);
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