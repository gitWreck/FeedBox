package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class FeedbackActivity extends AppCompatActivity {

    List<FeedbackHelper> feedbackHelpers;
    FeedbackAdapter feedbackAdapter;

    RecyclerView recyclerView;

    LinearLayout linearLayoutBack;

    String Email;

    String Status = "Pending", Sentiment = "Like";

    Chip chipPending, chipInProgress, chipCompleted;
    Chip chipLike, chipDislike;

    TextView tvEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);

        SharedPreferences sh = getSharedPreferences("FeedBox", Context.MODE_PRIVATE);

        Email = sh.getString("email", "");

        feedbackHelpers = new ArrayList<>();

        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        recyclerView = findViewById(R.id.recyclerView);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvEmpty.setVisibility(View.GONE);

        chipPending = findViewById(R.id.chipPending);
        chipInProgress = findViewById(R.id.chipInProgress);
        chipCompleted = findViewById(R.id.chipCompleted);

        chipLike = findViewById(R.id.chipLike);
        chipDislike = findViewById(R.id.chipDislike);

        recyclerView.setHasFixedSize(true);

        feedbackAdapter = new FeedbackAdapter(feedbackHelpers, this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager2);

        recyclerView.setAdapter(feedbackAdapter);

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

        LoadFeedback();
    }

    void LoadFeedback()
    {
        feedbackHelpers.clear();
        recyclerView.setAdapter(feedbackAdapter);
        String url = URLDatabase.URL_FEEDBACK_LIST;

        RequestQueue queue = Volley.newRequestQueue(FeedbackActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals("[]")) {
                        feedbackHelpers.clear();

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++) {
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

                                String details = jsonObjectData.getString("details");
                                String subdetails = jsonObjectData.getString("sub_details");
                                String reasons = jsonObjectData.getString("what_happened");
//                                fullName,
                                feedbackHelpers.add(new FeedbackHelper(feedbackID, firstName, lastName,
                                        email,categoryName,subCategoryName,sentiment,description,status,subStatus,datePosted,
                                        details, subdetails, reasons));
                            } catch (Exception err) {
//                                Toast.makeText(FeedbackActivity.this, "dAWDW", Toast.LENGTH_SHORT).show();
                                Toast.makeText(FeedbackActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        tvEmpty.setVisibility(View.GONE);
                        recyclerView.setAdapter(feedbackAdapter);
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    Toast.makeText(FeedbackActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(FeedbackActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("email", Email);
                params.put("status", Status);
                params.put("sentiment", Sentiment);
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