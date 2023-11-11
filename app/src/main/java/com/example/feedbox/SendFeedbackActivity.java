package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

public class SendFeedbackActivity extends AppCompatActivity{

    LinearLayout linearLayoutBack;
    Spinner spinnerCategory, spinnerSubCategory, spinnerDetails, spinnerSubDetails, spinnerSelectWhatHappened;

    List<String> categories = new ArrayList<String>();
    String Category;

    List<String> subCategories = new ArrayList<String>();
    String SubCategory;

    List<String> details = new ArrayList<String>();
    String Details;

    List<String> sub_details = new ArrayList<String>();
    String SubDetails;

    List<String> what_happened = new ArrayList<String>();
    String WhatHappened = " ";

    EditText txtDescription;
    CardView cardViewSubmit;
    String Email;

    String Feedback;

    TextView tvDescription1, tvDescription2, tvDescription3;

    ImageView imgFeedback;

    private long mLastClickTime = 0;

    CardView cardViewWhatHappened;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_feedback_activity);

        SharedPreferences sh = getSharedPreferences("FeedBox", Context.MODE_PRIVATE);

        Email = sh.getString("email", "");

        Feedback = getIntent().getExtras().getString("Feedback");

        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubCategory = findViewById(R.id.spinnerSubCategory);
        txtDescription = findViewById(R.id.txtDescription);
        cardViewSubmit = findViewById(R.id.cardViewSubmit);
        imgFeedback = findViewById(R.id.imgFeedback);
        cardViewWhatHappened = findViewById(R.id.cardViewWhatHappened);

        tvDescription1 = findViewById(R.id.tvDescription1);
        tvDescription2 = findViewById(R.id.tvDescription2);
        tvDescription3 = findViewById(R.id.tvDescription3);

        spinnerDetails = findViewById(R.id.spinnerDetails);
        spinnerSubDetails = findViewById(R.id.spinnerSubDetails);
        spinnerSelectWhatHappened = findViewById(R.id.spinnerSelectWhatHappened);

        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(txtDescription.getText().toString().isEmpty())
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

        if(Feedback.equals("Like"))
        {
            tvDescription1.setText("We're glad for your great experience!");
            tvDescription2.setText("In which category do you have good experience?");
            tvDescription3.setText("Share your great experience with us:");
            imgFeedback.setImageTintList(getColorStateList(R.color.emerald));
            cardViewWhatHappened.setVisibility(View.GONE);
        }
        else
        {
            tvDescription1.setText("We're really sorry for your bad experience!");
            tvDescription2.setText("In which category do you have bad experience?");
            tvDescription3.setText("Specify your reason in one sentence");
            imgFeedback.setImageTintList(getColorStateList(R.color.alizarin));
            imgFeedback.setRotation(180.0f);
        }

        cardViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                String url = URLDatabase.URL_SEND_FEEDBACK;

                RequestQueue queue = Volley.newRequestQueue(SendFeedbackActivity.this);

                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        finish();
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(SendFeedbackActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                        params.put("category_name", Category);
                        params.put("sub_category_name", SubCategory);
                        params.put("details", Details);
                        params.put("sub_details", SubDetails);
                        params.put("what_happened", WhatHappened);
                        params.put("sentiment", Feedback);
                        params.put("description", txtDescription.getText().toString());
                        return params;
                    }
                };
                queue.add(request);
            }
        });

        LoadCategory();
    }

    void LoadCategory()
    {
        String url = URLDatabase.URL_CATEGORY_LIST;

        RequestQueue queue = Volley.newRequestQueue(SendFeedbackActivity.this);

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

                                categories.add(categoryName);
                            }

                            catch (Exception err)
                            {
                                Toast.makeText(SendFeedbackActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        ArrayAdapter ad = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, categories);

                        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerCategory.setAdapter(ad);

                        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                            {
                                String[] simpleArray = new String[ categories.size() ];
                                categories.toArray(simpleArray);

                                Category = simpleArray[i];

                                if(Category.equals("Facilities")) {
                                    subCategories.clear();

                                    String url = URLDatabase.URL_SUB_CATEGORY_NAME_LIST;

                                    RequestQueue queue = Volley.newRequestQueue(SendFeedbackActivity.this);

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

                                                            String subCategoryID = jsonObjectData.getString("sub_category_id");
                                                            String categoryID = jsonObjectData.getString("category_id");
                                                            String subCategoryName = jsonObjectData.getString("sub_category_name");
                                                            String datePosted = jsonObjectData.getString("date_posted");

                                                            subCategories.add(subCategoryName);
                                                        }

                                                        catch (Exception err)
                                                        {
                                                            Toast.makeText(SendFeedbackActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    ArrayAdapter ad = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, subCategories);

                                                    ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                    spinnerSubCategory.setAdapter(ad);

                                                    spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                            String[] simpleArray = new String[ subCategories.size() ];
                                                            subCategories.toArray(simpleArray);

                                                            SubCategory = simpleArray[i];

                                                            details.clear();
                                                            sub_details.clear();
                                                            what_happened.clear();


                                                            //DETAILS
                                                            {
                                                                ArrayAdapter ad2 = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, details);

                                                                ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                spinnerDetails.setAdapter(ad2);

                                                                spinnerDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                        String[] simpleArray = new String[ details.size() ];
                                                                        details.toArray(simpleArray);

                                                                        Details = simpleArray[i];
                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                                    }
                                                                });
                                                            }

                                                            //SUB DETAILS
                                                            {
                                                                ArrayAdapter ad = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, sub_details);

                                                                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                spinnerSubDetails.setAdapter(ad);

                                                                spinnerSubDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                        String[] simpleArray = new String[ sub_details.size() ];
                                                                        sub_details.toArray(simpleArray);

                                                                        SubDetails = simpleArray[i];
                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                                    }
                                                                });
                                                            }

                                                            //WHAT HAPPENED
                                                            {
                                                                ArrayAdapter ad = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, what_happened);

                                                                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                spinnerSelectWhatHappened.setAdapter(ad);

                                                                spinnerSelectWhatHappened.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                        String[] simpleArray = new String[ what_happened.size() ];
                                                                        what_happened.toArray(simpleArray);

                                                                        WhatHappened = simpleArray[i];
                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                                        }
                                                    });

                                                }

                                            } catch (Exception e) {

                                                Toast.makeText(SendFeedbackActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error)
                                        {
                                            Toast.makeText(SendFeedbackActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            params.put("category_name", Category);
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
                                else if (Category.equals("Conduct")) {
                                    subCategories.clear();

                                    String url = URLDatabase.URL_SUB_CATEGORY_NAME_LIST;

                                    RequestQueue queue = Volley.newRequestQueue(SendFeedbackActivity.this);

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

                                                            String subCategoryID = jsonObjectData.getString("sub_category_id");
                                                            String categoryID = jsonObjectData.getString("category_id");
                                                            String subCategoryName = jsonObjectData.getString("sub_category_name");
                                                            String datePosted = jsonObjectData.getString("date_posted");

                                                            subCategories.add(subCategoryName);
                                                        }

                                                        catch (Exception err)
                                                        {
                                                            Toast.makeText(SendFeedbackActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    ArrayAdapter ad = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, subCategories);

                                                    ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                    spinnerSubCategory.setAdapter(ad);

                                                    spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                                    {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                                                        {
                                                            String[] simpleArray = new String[ subCategories.size() ];
                                                            subCategories.toArray(simpleArray);

                                                            SubCategory = simpleArray[i];

                                                            details.clear();
                                                            sub_details.clear();
                                                            what_happened.clear();



                                                            //DETAILS
                                                            {
                                                                ArrayAdapter ad2 = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, details);

                                                                ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                spinnerDetails.setAdapter(ad2);

                                                                spinnerDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                        String[] simpleArray = new String[ details.size() ];
                                                                        details.toArray(simpleArray);

                                                                        Details = simpleArray[i];
                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                                    }
                                                                });
                                                            }

                                                            //SUB DETAILS
                                                            {
                                                                ArrayAdapter ad = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, sub_details);

                                                                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                spinnerSubDetails.setAdapter(ad);

                                                                spinnerSubDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                        String[] simpleArray = new String[ sub_details.size() ];
                                                                        sub_details.toArray(simpleArray);

                                                                        SubDetails = simpleArray[i];
                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                                    }
                                                                });
                                                            }

                                                            //WHAT HAPPENED
                                                            {
                                                                ArrayAdapter ad = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, what_happened);

                                                                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                spinnerSelectWhatHappened.setAdapter(ad);

                                                                spinnerSelectWhatHappened.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                        String[] simpleArray = new String[ what_happened.size() ];
                                                                        what_happened.toArray(simpleArray);

                                                                        WhatHappened = simpleArray[i];
                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                                        }
                                                    });

                                                }

                                            } catch (Exception e) {

                                                Toast.makeText(SendFeedbackActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error)
                                        {
                                            Toast.makeText(SendFeedbackActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                            params.put("category_name", Category);
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

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                } catch (Exception e) {

                    Toast.makeText(SendFeedbackActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(SendFeedbackActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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