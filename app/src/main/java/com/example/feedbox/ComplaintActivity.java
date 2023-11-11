package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintActivity extends AppCompatActivity {

    List<FeedbackAdminHelper> feedbackAdminHelpers;
    FeedbackAdminAdapter feedbackAdminAdapter;

    RecyclerView recyclerView;

    LinearLayout linearLayoutBack;
    public String Status = "Pending", Sentiment = "Like", Category = "All";

    public void setCategory(String category) {
        Category = category;
    }

    Chip chipAll, chipPending, chipInProgress, chipCompleted;
    Chip chipLike, chipDislike;
    LinearLayout linearLayoutFilter;

    public static ComplaintActivity complaintActivity;

    public static ComplaintActivity getInstance() {
        return complaintActivity;
    }

    Dialog dialog;

    public Dialog getDialog() {
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_activity);

        complaintActivity = this;

        feedbackAdminHelpers = new ArrayList<>();

        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        recyclerView = findViewById(R.id.recyclerView);
        chipAll = findViewById(R.id.chipAll);
        chipPending = findViewById(R.id.chipPending);
        chipInProgress = findViewById(R.id.chipInProgress);
        chipCompleted = findViewById(R.id.chipCompleted);
        chipLike = findViewById(R.id.chipLike);
        chipDislike = findViewById(R.id.chipDislike);
        linearLayoutFilter = findViewById(R.id.linearLayoutFilter);

        recyclerView.setHasFixedSize(true);

        feedbackAdminAdapter = new FeedbackAdminAdapter(feedbackAdminHelpers, this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager2);

        recyclerView.setAdapter(feedbackAdminAdapter);

        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chipPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Status = "Pending";
                LoadFeedback();
            }
        });

        chipAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Status = "All";
                LoadFeedback();
            }
        });

        chipInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Status = "In Progress";
                LoadFeedback();
            }
        });

        chipCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Status = "Completed";
                LoadFeedback();
            }
        });

        chipLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sentiment = "Like";
                LoadFeedback();
            }
        });

        chipDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sentiment = "Dislike";
                LoadFeedback();
            }
        });

        linearLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ComplaintActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.filter_category_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                List<FilterCategoryHelper> filterCategoryHelpers;
                FilterCategoryAdapter filterCategoryAdapter;
                RecyclerView recyclerView;

                recyclerView = dialog.findViewById(R.id.recyclerView);

                filterCategoryHelpers = new ArrayList<>();

                recyclerView.setHasFixedSize(true);

                filterCategoryAdapter = new FilterCategoryAdapter(filterCategoryHelpers, ComplaintActivity.this);

                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager2);

                filterCategoryHelpers.add(new FilterCategoryHelper("All"));
                recyclerView.setAdapter(filterCategoryAdapter);

                //CATEGORY
                {
                    String url = URLDatabase.URL_CATEGORY_LIST;

                    RequestQueue queue = Volley.newRequestQueue(ComplaintActivity.this);

                    StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if(!response.equals("[]"))
                                {

                                    JSONObject jsonObject = new JSONObject(response);

                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for(int i = 0; i < jsonArray.length(); i++)
                                    {
                                        try {
                                            JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                                            String categoryID = jsonObjectData.getString("category_id");
                                            String categoryName = jsonObjectData.getString("category_name");
                                            String datePosted = jsonObjectData.getString("date_posted");

                                            filterCategoryHelpers.add(new FilterCategoryHelper(categoryName));
                                        }

                                        catch (Exception err)
                                        {
                                            Toast.makeText(ComplaintActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    recyclerView.setAdapter(filterCategoryAdapter);
                                }

                            } catch (Exception e) {

                                Toast.makeText(ComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Toast.makeText(ComplaintActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                dialog.show();
            }
        });

        LoadFeedback();
    }

    public void LoadFeedback()
    {
        feedbackAdminHelpers.clear();
        recyclerView.setAdapter(feedbackAdminAdapter);
        String url = URLDatabase.URL_FEEDBACK_LIST_STATUS;

        RequestQueue queue = Volley.newRequestQueue(ComplaintActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals("[]"))
                    {
                        feedbackAdminHelpers.clear();

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            try {
                                JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                                String feedbackID = jsonObjectData.getString("feedback_id");
                                String email = jsonObjectData.getString("email");
                                String firstName = jsonObjectData.getString("first_name");
                                String lastName = jsonObjectData.getString("last_name");
//                                String fullName = jsonObjectData.getString("full_name");
                                String categoryName = jsonObjectData.getString("category_name");
                                String subCategoryName = jsonObjectData.getString("sub_category_name");
                                String sentiment = jsonObjectData.getString("sentiment");
                                String description = jsonObjectData.getString("description");
                                String status = jsonObjectData.getString("status");
                                String subStatus = jsonObjectData.getString("sub_status");
                                String datePosted = jsonObjectData.getString("date_posted");
//                                fullName,
                                feedbackAdminHelpers.add(new FeedbackAdminHelper(feedbackID, firstName, lastName,
                                        email, categoryName, subCategoryName, sentiment, description, status, subStatus, datePosted));
                            }

                            catch (Exception err)
                            {
                                Toast.makeText(ComplaintActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        recyclerView.setAdapter(feedbackAdminAdapter);
                    } else {
                        Toast.makeText(ComplaintActivity.this, "Empty", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Toast.makeText(ComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(ComplaintActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("status", Status);
                params.put("sentiment", Sentiment);
                params.put("category", Category);
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