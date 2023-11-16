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

public class CategoryActivity extends AppCompatActivity {

    List<CategoryHelper> categoryHelpers;
    CategoryAdapter categoryAdapter;

    RecyclerView recyclerView;

    LinearLayout linearLayoutBack, linearLayoutSubCateg, linearLayoutSpeCom;
    CardView cardViewAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

//        linearLayoutSubCateg = findViewById(R.id.linearLayoutSubCategory);
//        linearLayoutSpeCom = findViewById(R.id.linearLayoutSpecificComplaint);
        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        cardViewAdd = findViewById(R.id.cardViewAdd);
        recyclerView = findViewById(R.id.recyclerView);

        categoryHelpers = new ArrayList<>();

        recyclerView.setHasFixedSize(true);

        categoryAdapter = new CategoryAdapter(categoryHelpers, this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager2);

        recyclerView.setAdapter(categoryAdapter);

        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        linearLayoutSubCateg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(CategoryActivity.this, SubCategoryActivity.class);
//                startActivity(intent);
//            }
//        });

        cardViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(CategoryActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.category_add_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                CardView cardViewSubmit;

                EditText txtCategoryName;

                cardViewSubmit = dialog.findViewById(R.id.cardViewSubmit);

                txtCategoryName = dialog.findViewById(R.id.txtCategoryName);

                txtCategoryName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtCategoryName.getText().toString().isEmpty())
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

//                linearLayoutBack.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });

                cardViewSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = URLDatabase.URL_CATEGORY_ADD;

                        RequestQueue queue = Volley.newRequestQueue(CategoryActivity.this);

                        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                dialog.dismiss();

                                LoadCategory();
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(CategoryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                params.put("category_name", txtCategoryName.getText().toString());
                                return params;
                            }
                        };
                        queue.add(request);
                    }
                });
                dialog.show();
            }
        });

        LoadCategory();
    }

    void LoadCategory() {
        categoryHelpers.clear();
        recyclerView.setAdapter(categoryAdapter);
        String url = URLDatabase.URL_CATEGORY_LIST;

        RequestQueue queue = Volley.newRequestQueue(CategoryActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals("[]"))
                    {
                        categoryHelpers.clear();

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            try {
                                JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                                String categoryID = jsonObjectData.getString("category_id");
                                String categoryName = jsonObjectData.getString("category_name");
                                String datePosted = jsonObjectData.getString("date_posted");

                                categoryHelpers.add(new CategoryHelper(categoryID,categoryName,datePosted, true));
                            }

                            catch (Exception err)
                            {
                                Toast.makeText(CategoryActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        recyclerView.setAdapter(categoryAdapter);
                    }

                } catch (Exception e) {

                    Toast.makeText(CategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(CategoryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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