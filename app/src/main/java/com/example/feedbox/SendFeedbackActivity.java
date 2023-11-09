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
import android.widget.AutoCompleteTextView;
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
import com.android.volley.Response;
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
    Spinner spinnerCategory, spinnerSubCategory;
//            spinnerDetails, spinnerSubDetails, spinnerSelectWhatHappened;
    AutoCompleteTextView ACDetails, ACSubDetails, ACSelectWhatHapp;

    List<String> categories = new ArrayList<String>();

    String Category;

    List<String> subCategories = new ArrayList<String>();
    String SubCategory;

    List<String> details = new ArrayList<String>();

//    String[] detailsN = {null};
//    ArrayList<String> detailsN = new ArrayList<String>();
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

        ACDetails = findViewById(R.id.ACDetails);
        ACSubDetails = findViewById(R.id.ACSubDetails);
        ACSelectWhatHapp = findViewById(R.id.ACSelectWhatHapp);

        cardViewSubmit.setEnabled(true);
        cardViewSubmit.setClickable(true);
        cardViewSubmit.setFocusable(true);
        cardViewSubmit.setAlpha(1.0f);

        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        txtDescription.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

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

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        finish();
                    }
                }, new Response.ErrorListener() {
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

                                if(Category.equals("Facilities"))
                                {
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

                                                            if(SubCategory.equals("Restroom"))
                                                            {
                                                                details.add("Pancho Hall 1st floor leftside - FEMALE");
                                                                details.add("Pancho Hall 1st floor leftside - MALE");
                                                                details.add("Pancho Hall 1st floor rightside - FEMALE");
                                                                details.add("Pancho Hall 1st floor rightside - MALE");
                                                                details.add("Pancho Hall 1st floorcenter - FEMALE");
                                                                details.add("Pancho Hall 1st floorcenter - MALE");
                                                                details.add("Pancho Hall 1st floorcenter - MALE");
                                                                details.add("Pancho Hall 2nd floor leftside – MALE");
                                                                details.add("Pancho Hall 2nd floor rightside – FEMALE");
                                                                details.add("Pancho Hall 2nd floor rightside – MALE");
                                                                details.add("Pancho Hall 2nd floorcenter - FEMALE");
                                                                details.add("Pancho Hall 2nd floorcenter - MALE");
                                                                details.add("CBA Building 1st floor FEMALE");
                                                                details.add("CBA Building 1st floor MALE");
                                                                details.add("Activity Center – FEMALE");
                                                                details.add("Activity Center - MALE");
                                                                details.add("Hanger left side - FEMALE");
                                                                details.add("Hanger left side - MALE");
                                                                details.add("Hanger right side FEMALE");
                                                                details.add("Hanger right side - MALE");

                                                                sub_details.add("Door");
                                                                sub_details.add("Doorknob");
                                                                sub_details.add("Window");
                                                                sub_details.add("Faucet");
                                                                sub_details.add("Roof");
                                                                sub_details.add("Toilet bowl");
                                                                sub_details.add("Sink");
                                                                sub_details.add("Floor");
                                                                sub_details.add("Light Bulb");

                                                                what_happened.add("Broken");
                                                                what_happened.add("No water");
                                                                what_happened.add("Bad Odor");
                                                                what_happened.add("Roof is Leaking");
                                                                what_happened.add("No flash");
                                                                what_happened.add("Dirty");
                                                                what_happened.add("Floor is slippery");
                                                                what_happened.add("No Electricity");
                                                            }
                                                            else if(SubCategory.equals("Classroom"))
                                                            {
                                                                details.add("Room 100A");
                                                                details.add("Room 100B");
                                                                details.add("Room 101");
                                                                details.add("Room 102A");
                                                                details.add("Room 102B");
                                                                details.add("Room 103");
                                                                details.add("Room 104");
                                                                details.add("Room 105");
                                                                details.add("Room 106");
                                                                details.add("Room 107");
                                                                details.add("Room 108");
                                                                details.add("Room 109");
                                                                details.add("Room 110");
                                                                details.add("Room 111");
                                                                details.add("Room 112A");
                                                                details.add("Room 112B");
                                                                details.add("Room 113A");
                                                                details.add("Room 113B");
                                                                details.add("Room 114A");
                                                                details.add("Room 114B");
                                                                details.add("Room 115A");
                                                                details.add("Room 115B");

                                                                sub_details.add("Electric Fan");
                                                                sub_details.add("TV");
                                                                sub_details.add("White Board");
                                                                sub_details.add("Black Board");
                                                                sub_details.add("Window");
                                                                sub_details.add("Armchair");
                                                                sub_details.add("Teacher Table");
                                                                sub_details.add("Aircon");
                                                                sub_details.add("Light Bulb");
                                                                sub_details.add("Floor");
                                                                sub_details.add("Projector");
                                                                sub_details.add("Door");
                                                                sub_details.add("Doorknob");
                                                                sub_details.add("Roof");

                                                                what_happened.add("Broken");
                                                                what_happened.add("Dirty");
                                                                what_happened.add("No Electricity");
                                                                what_happened.add("Roof is leaking");
                                                                what_happened.add("Slippery");
                                                                what_happened.add("Smelly");
                                                            }
                                                            else if(SubCategory.equals("Parking"))
                                                            {
                                                                details.add("CIT Parking");
                                                                details.add("COED Parking ");
                                                                details.add("CBA Parking");
                                                                details.add("COE Parking");
                                                                details.add("Visitor Parking");
                                                                details.add("Car Parking");

                                                                sub_details.add("place");

                                                                what_happened.add("Dirty");
                                                                what_happened.add("No space");
                                                            }
                                                            else if(SubCategory.equals("Building"))
                                                            {
                                                                details.add("Pancho Building 1st floor");
                                                                details.add("Pancho Building 2ns floor");
                                                                details.add("Pancho Building 3rd floor");
                                                                details.add("CBA Building 1st floor");
                                                                details.add("CBA Building 2nd floor");
                                                                details.add("CBA Building 3rd floor");
                                                                details.add("CBA Building 4th floor");
                                                                details.add("Admin Building 1st floor");
                                                                details.add("Admin Building 2nd floor");
                                                                details.add("Admin Building 3rd floor");

                                                                sub_details.add("Hallway");

                                                                what_happened.add("Noisy");
                                                                what_happened.add("Dirty");
                                                                what_happened.add("Smelly");
                                                                what_happened.add("Slippery");
                                                                what_happened.add("Roof is leaking");
                                                            }
                                                            else if(SubCategory.equals("Library"))
                                                            {
                                                                details.add("1st floor Pancho Hall");

                                                                sub_details.add("Books");
                                                                sub_details.add("Table");
                                                                sub_details.add("Chair");
                                                                sub_details.add("Bookshelf");
                                                                sub_details.add("Librarian");
                                                                sub_details.add("Assistant Librarian");
                                                                sub_details.add("Door");
                                                                sub_details.add("Doorknob");

                                                                what_happened.add("Not enough books");
                                                                what_happened.add("Noisy");
                                                                what_happened.add("Broken");
                                                                what_happened.add("Not enough chair");
                                                                what_happened.add("Not enough table");
                                                                what_happened.add("Dirty");
                                                                what_happened.add("No electricity");
                                                                what_happened.add("Attitude");
                                                                what_happened.add("Can’t borrow books");
                                                                what_happened.add("Broken");
                                                            }
                                                            else if(SubCategory.equals("Laboratory"))
                                                            {
                                                                details.add("Laboratory 1");
                                                                details.add("Laboratory 2");

                                                                sub_details.add("Monitor");
                                                                sub_details.add("Keyboard");
                                                                sub_details.add("TV");
                                                                sub_details.add("Chair");
                                                                sub_details.add("Table");
                                                                sub_details.add("Floor");
                                                                sub_details.add("Roof");
                                                                sub_details.add("Window");
                                                                sub_details.add("Aircon");
                                                                sub_details.add("Electric fan");
                                                                sub_details.add("Door");
                                                                sub_details.add("Doorknob");

                                                                what_happened.add("Broken");
                                                                what_happened.add("Not enough");
                                                                what_happened.add("Dirty");
                                                                what_happened.add("Leaking");
                                                                what_happened.add("Not working");
                                                                what_happened.add("No electricity");
                                                            }
                                                            else if(SubCategory.equals("Activity Center"))
                                                            {
                                                                details.add("Infront of Hanger room");

                                                                sub_details.add("Chair");
                                                                sub_details.add("Stage");
                                                                sub_details.add("Equipment");
                                                                sub_details.add("Electric Fan ");
                                                                sub_details.add("Events");
                                                                sub_details.add("Roof");
                                                                sub_details.add("Floor");
                                                                sub_details.add("Lights");

                                                                what_happened.add("Dirty");
                                                                what_happened.add("No electricity");
                                                                what_happened.add("Slippery");
                                                                what_happened.add("leaking");
                                                                what_happened.add("broken");
                                                                what_happened.add("Smelly");
                                                                what_happened.add("Not enough");
                                                            }
                                                            else if(SubCategory.equals("Study Area"))
                                                            {
                                                                details.add("Beside Pancho Hall");

                                                                sub_details.add("Chair");
                                                                sub_details.add("Table");
                                                                sub_details.add("Plants");
                                                                sub_details.add("Electric fan");
                                                                sub_details.add("Roof");
                                                                sub_details.add("Floor");

                                                                what_happened.add("Not enough");
                                                                what_happened.add("No electricity");
                                                                what_happened.add("Dirty");
                                                                what_happened.add("Smelly");
                                                                what_happened.add("Leaking");
                                                                what_happened.add("rotten");
                                                            }


                                                            //DETAILS
                                                            {
                                                                ArrayAdapter ad2 = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_dropdown_item_1line, details);

//                                                                ad2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                                                ACDetails.setThreshold(1);
                                                                ACDetails.setAdapter(ad2);

//                                                                ACDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                                                    @Override
//                                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                                                        String[] simpleArray = new String[ details.size() ];
//                                                                        details.toArray(simpleArray);
//
//                                                                        Details = simpleArray[i];
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                                                    }
//                                                                });
                                                            }

                                                            //SUB DETAILS
                                                            {
                                                                ArrayAdapter ad = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, sub_details);

                                                                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                ACSubDetails.setAdapter(ad);

                                                                ACSubDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                                                                ACSelectWhatHapp.setAdapter(ad);

                                                                ACSelectWhatHapp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                                else if (Category.equals("Conduct"))
                                {
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

                                                            if(SubCategory.equals("Student"))
                                                            {
//                                                                detailsN = new String[] {"CBA","COED","COE","CICS","CIT"};
                                                                details.add("CBA");
                                                                details.add("COED");
                                                                details.add("COE");
                                                                details.add("CICS");
                                                                details.add("CIT");

                                                                sub_details.add("Female");
                                                                sub_details.add("Male");

                                                                what_happened.add("Bad Attitude");
                                                                what_happened.add("No manner");
                                                                what_happened.add("Noisy");
                                                                what_happened.add("School Law breaker");
                                                                what_happened.add("Always late");
                                                                what_happened.add("Always Absent");
                                                                what_happened.add("Bullying");
                                                            }
                                                            else if(SubCategory.equals("Faculty Member"))
                                                            {
                                                                details.add("CBA Faculty");
                                                                details.add("COE Faculty");
                                                                details.add("COED Faculty");
                                                                details.add("CICS Faculty ");

                                                                sub_details.add("Female");
                                                                sub_details.add("Male");

                                                                what_happened.add("Bad Attitude");
                                                                what_happened.add("Physical Abuse");
                                                                what_happened.add("Emotional Abuse");
                                                                what_happened.add("Not Teaching");
                                                                what_happened.add("Snobber");
                                                            }
                                                            else if(SubCategory.equals("School Personnel"))
                                                            {
                                                                details.add("Admin staff");
                                                                details.add("Gate");
                                                                details.add("Canteen");
                                                                details.add("Caretaker");
                                                                details.add("Visitor Parking");
                                                                details.add("Car Parking");

                                                                sub_details.add("Cashier");
                                                                sub_details.add("Guard");
                                                                sub_details.add("Guidance");
                                                                sub_details.add("Tindera");
                                                                sub_details.add("Tindero");
                                                                sub_details.add("Janitor");
                                                                sub_details.add("Janitress");

                                                                what_happened.add("Bad attitude");
                                                                what_happened.add("No respect");
                                                                what_happened.add("Physical Abuse");
                                                                what_happened.add("Emotional Abuse");
                                                                what_happened.add("Not working");
                                                                what_happened.add("Not accommodating");
                                                                what_happened.add("Snobber");
                                                            }

                                                            //DETAILS
                                                            {
                                                                ArrayAdapter ad2 = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_dropdown_item_1line, details);
//                                                                ad2.setNotifyOnChange(true);
//                                                                ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                                                if (!details.isEmpty() && details.size() > 0) {
//                                                                    ACDetails.setText(details.get(0));
//                                                                }

                                                                ACDetails.setThreshold(1);
                                                                ACDetails.setAdapter(ad2);

//                                                                ACDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                                                    @Override
//                                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                                                        String[] simpleArray = new String[ details.size() ];
//                                                                        details.toArray(simpleArray);
//
//                                                                        Details = simpleArray[i];
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                                                    }
//                                                                });
                                                            }

                                                            //SUB DETAILS
                                                            {
                                                                ArrayAdapter ad = new ArrayAdapter(SendFeedbackActivity.this, android.R.layout.simple_spinner_item, sub_details);

                                                                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                ACSubDetails.setAdapter(ad);

                                                                ACSubDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                                                                ACSelectWhatHapp.setAdapter(ad);

                                                                ACSelectWhatHapp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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