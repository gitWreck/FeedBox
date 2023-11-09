package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class SubCategoryActivity extends AppCompatActivity {

    List<SubCategoryHelper> subCategoryHelpers;
    SubCategoryAdapter subCategoryAdapter;

    RecyclerView recyclerView;

    LinearLayout linearLayoutBack;
    String CategoryID, CategoryName;
    TextView tvCategoryName;

    CardView cardViewAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category_activity);

        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        tvCategoryName = findViewById(R.id.tvCategoryName);
        cardViewAdd = findViewById(R.id.cardViewAdd);

        SharedPreferences sh = getSharedPreferences("FeedBox", Context.MODE_PRIVATE);

        CategoryID = getIntent().getExtras().getString("CategoryID");
        CategoryName = getIntent().getExtras().getString("CategoryName");

        tvCategoryName.setText(CategoryName);

        linearLayoutBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        subCategoryHelpers = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        subCategoryAdapter = new SubCategoryAdapter(subCategoryHelpers, this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager2);

        recyclerView.setAdapter(subCategoryAdapter);

        cardViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(SubCategoryActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.sub_category_add_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                CardView cardViewSubmit;

                EditText txtSubCategoryName;

                cardViewSubmit = dialog.findViewById(R.id.cardViewSubmit);

                txtSubCategoryName = dialog.findViewById(R.id.txtSubCategoryName);

                txtSubCategoryName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtSubCategoryName.getText().toString().isEmpty())
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

                cardViewSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = URLDatabase.URL_SUB_CATEGORY_ADD;

                        RequestQueue queue = Volley.newRequestQueue(SubCategoryActivity.this);

                        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                dialog.dismiss();

                                LoadSubCategory();
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(SubCategoryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                params.put("category_id", CategoryID);
                                params.put("sub_category_name", txtSubCategoryName.getText().toString());
                                return params;
                            }
                        };
                        queue.add(request);
                    }
                });
                dialog.show();
            }
        });

        LoadSubCategory();
    }

    void LoadSubCategory()
    {
        subCategoryHelpers.clear();
        recyclerView.setAdapter(subCategoryAdapter);
        String url = URLDatabase.URL_SUB_CATEGORY_LIST;

        RequestQueue queue = Volley.newRequestQueue(SubCategoryActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals("[]"))
                    {
                        subCategoryHelpers.clear();

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            try {
                                JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                                String subCategoryID = jsonObjectData.getString("sub_category_id");
                                String categoryID = jsonObjectData.getString("category_id");
                                String subCategoryName = jsonObjectData.getString("sub_category_name");
                                String datePosted = jsonObjectData.getString("date_posted");

                                subCategoryHelpers.add(new SubCategoryHelper(subCategoryID,categoryID,subCategoryName,datePosted));
                            }

                            catch (Exception err)
                            {
                                Toast.makeText(SubCategoryActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        recyclerView.setAdapter(subCategoryAdapter);
                    }

                } catch (Exception e) {

                    Toast.makeText(SubCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(SubCategoryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("category_id", CategoryID);
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