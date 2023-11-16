package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
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
import org.json.JSONException;
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

    String Feedback, AY_Range;

    TextView tvDescription1, tvDescription2, tvDescription3, tvActiveAY;

    ImageView imgFeedback;

    private long mLastClickTime = 0;

    CardView cardViewWhatHappened;

    ProgressDialog progressDialog;

    private void EndProgLoad(){
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }
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
        tvActiveAY = findViewById(R.id.tvActiveAYSFB);

        GetActiveAY();

        spinnerDetails = findViewById(R.id.spinnerDetails);
        spinnerSubDetails = findViewById(R.id.spinnerSubDetails);
        spinnerSelectWhatHappened = findViewById(R.id.spinnerSelectWhatHappened);

        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cardViewSubmit.setEnabled(true);
        cardViewSubmit.setClickable(true);
        cardViewSubmit.setFocusable(true);
        cardViewSubmit.setAlpha(1.0f);

        progressDialog = new ProgressDialog(SendFeedbackActivity.this);
        progressDialog.setCancelable(false);

        txtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(txtDescription.getText().toString().isEmpty())
//                {
//                    cardViewSubmit.setEnabled(false);
//                    cardViewSubmit.setClickable(false);
//                    cardViewSubmit.setFocusable(false);
//                    cardViewSubmit.setAlpha(0.2f);
//                }
//                else
//                {
//                    cardViewSubmit.setEnabled(true);
//                    cardViewSubmit.setClickable(true);
//                    cardViewSubmit.setFocusable(true);
//                    cardViewSubmit.setAlpha(1.0f);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(Feedback.equals("Like")) {
            tvDescription1.setText("We're glad for your great experience!");
            tvDescription2.setText("In which category do you have good experience?");
            tvDescription3.setText("Share your great experience with us:");
            imgFeedback.setImageTintList(getColorStateList(R.color.emerald));
            cardViewWhatHappened.setVisibility(View.GONE);
        } else {
            tvDescription1.setText("We're really sorry for your bad experience!");
            tvDescription2.setText("In which category do you have bad experience?");
            tvDescription3.setText("Specify your reason in one sentence");
            imgFeedback.setImageTintList(getColorStateList(R.color.alizarin));
            imgFeedback.setRotation(180.0f);
        }

        cardViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(SendFeedbackActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.delete_confirmation_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                TextView tvMsgConfirm, tvcbcancel, tvcbconfirm;
                CardView cvNo, cvYes;

                tvMsgConfirm = dialog.findViewById(R.id.tvMessageConfirm);
                tvcbcancel = dialog.findViewById(R.id.cbcancel);
                tvcbconfirm = dialog.findViewById(R.id.cbconfirm);

                cvNo = dialog.findViewById(R.id.cardViewNo);
                cvYes = dialog.findViewById(R.id.cardViewYes);

                tvcbconfirm.setText("Confirm");
                tvcbcancel.setText("Cancel");
                tvMsgConfirm.setText("Confirm send feedback");


                cvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.setMessage("Sending...");
                        progressDialog.show();

                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        String url = URLDatabase.URL_SEND_FEEDBACK;

                        RequestQueue queue = Volley.newRequestQueue(SendFeedbackActivity.this);

                        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                EndProgLoad();
                                Toast.makeText(SendFeedbackActivity.this, "Feedback sent successfully...", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
//                                finish();
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                EndProgLoad();
                                Toast.makeText(SendFeedbackActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
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
                                params.put("ay_range", tvActiveAY.getText().toString());
                                return params;
                            }
                        };
                        queue.add(request);
                    }
                });

                cvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        LoadCategory();
    }

    void LoadCategory() {
        String url = URLDatabase.URL_CATEGORY_LIST;
        RequestQueue queue = Volley.newRequestQueue(SendFeedbackActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals("[]")) {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                                String categoryID = jsonObjectData.getString("category_id");
                                String categoryName = jsonObjectData.getString("category_name");
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
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String[] simpleArray = new String[ categories.size() ];
                                categories.toArray(simpleArray);
                                Category = simpleArray[i];
//                                if(Category.equals("Facilities")) {
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
                                                for(int i = 0; i < jsonArray.length(); i++) {
                                                    try {
                                                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                                                        String subCategoryID = jsonObjectData.getString("sub_category_id");
//                                                            String categoryID = jsonObjectData.getString("category_id");
                                                        String subCategoryName = jsonObjectData.getString("sub_category_name");
//                                                            String datePosted = jsonObjectData.getString("date_posted");

                                                        subCategories.add(subCategoryName);
                                                    } catch (Exception err) {
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

                                                        String url = URLDatabase.URL_LIST_SPECIFIC;
                                                        RequestQueue queue = Volley.newRequestQueue(SendFeedbackActivity.this);
                                                        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
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

//                                                                            Toast.makeText(SendFeedbackActivity.this, spc_d_name, Toast.LENGTH_SHORT).show();
                                                                            Log.d("TAG", "onResponse: " + spc_d_name);

                                                                            if (spc_d_cat.equals("details")) {
                                                                                details.add(spc_d_name);
                                                                            } else if (spc_d_cat.equals("sub_details")) {
                                                                                sub_details.add(spc_d_name);
                                                                            } else if (spc_d_cat.equals("what_happened")) {
                                                                                what_happened.add(spc_d_name);
                                                                            }

//                                                                            loadListSpecific();
                                                                        } catch (Exception err) {
//                                                                            details.clear();
//                                                                            sub_details.clear();
//                                                                            what_happened.clear();

//                                                                            loadListSpecific();
                                                                            Toast.makeText(SendFeedbackActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }

                                                                    loadListSpecific();


                                                                } catch (Exception err) {
                                                                    Toast.makeText(SendFeedbackActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                                if (response.contains("[]")) {
                                                                    Toast.makeText(SendFeedbackActivity.this, "List Empty. Choose Another Category", Toast.LENGTH_SHORT).show();
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
                                                                params.put("sc_name", SubCategory);
                                                                params.put("cat_name", Category);
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
//                                }
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
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SendFeedbackActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
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

    void loadListSpecific () {
        // Details
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

    void GetActiveAY(){
        String url = URLDatabase.URL_AY_ACTIVE;
        RequestQueue queue = Volley.newRequestQueue(SendFeedbackActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
//                JSONObject jsonObject = new JSONObject(response)
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                        int ret_ay_id = jsonObjectData.getInt("ay_id");
                        String ret_ay_active = jsonObjectData.getString("ay_range");
                        String ret_ay_status = jsonObjectData.getString("ay_status");
                        AY_Range = ret_ay_active;
//                        Toast.makeText(HomeAdminActivity.this, String.valueOf(jsonArray.length()), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception err) {
                    Toast.makeText(SendFeedbackActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                }
                tvActiveAY.setText(AY_Range);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                tvActiveAY.setText(error.toString());
                Toast.makeText(SendFeedbackActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("active", "dwa");
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