package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeAdminActivity extends AppCompatActivity {

    LinearLayout linearLayoutAdmin, linearLayoutComplaint, linearLayoutUser, linearLayoutCategory, linearLayoutReport;
    ImageView imgViewLogout;
    TextView tvActiveAY, tvPendingCount, tvInProgressCount, tvCompletedCount, tvTotalFeedbacks, tvTotalUsers;

    List<String> xValues = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    ArrayList<BarEntry> entries = new ArrayList<>();
    int count = 0;
    int PendingCount = 0, InProgressCount = 0, CompletedCount = 0, TotalUsers = 0;


    List<String> categories = new ArrayList<String>();
    List<String> subcategories = new ArrayList<String>();
    List<String> ay_range_list = new ArrayList<String>();

    String Category, SubCategory, Month, AY_Range;
    Spinner spinnerCat, spinnerSubCat, spinnerMonth, spinnerYear;

    PieChart pieChart, pieSubChart;
    BarChart barChart;

    RecyclerView recyclerView;
    DateFormat dateFormat;
    Date date;

    List<SentimentCommentHelper> sentimentCommentHelpers;
    SentimentCommentAdapter sentimentCommentAdapter;
    TextView tvSentimentCount;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_admin_activity);
        dateFormat = new SimpleDateFormat("MM");
        date = new Date();
        Log.d("Month ", dateFormat.format(date));
        categories.add("All");
        Category = "All";
//        SubCategory = "Student";

        linearLayoutAdmin = findViewById(R.id.linearLayoutAdmin);
        linearLayoutComplaint = findViewById(R.id.linearLayoutComplaint);
        linearLayoutUser = findViewById(R.id.linearLayoutUser);
        linearLayoutCategory = findViewById(R.id.linearLayoutCategory);
        linearLayoutReport = findViewById(R.id.linearLayoutReport);
        imgViewLogout = findViewById(R.id.imgViewLogout);

        tvTotalFeedbacks = findViewById(R.id.tvTotalFeedbacks);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);

        tvActiveAY = findViewById(R.id.tvActiveAY);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvInProgressCount = findViewById(R.id.tvInProgressCount);
        tvCompletedCount = findViewById(R.id.tvCompletedCount);

        spinnerCat = findViewById(R.id.spinnerSelCat);
        spinnerSubCat = findViewById(R.id.spinnerSelSubCat);
//        spinnerMonth = findViewById(R.id.spinnerSelMon);
//        spinnerYear = findViewById(R.id.spinnerSelAY);

//        spinnerYear.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerView);
        tvSentimentCount = findViewById(R.id.tvSentimentCount);

        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
        pieSubChart = findViewById(R.id.pieSubChart);


        sentimentCommentHelpers = new ArrayList<>();

        recyclerView.setHasFixedSize(true);

        sentimentCommentAdapter = new SentimentCommentAdapter(sentimentCommentHelpers, this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager2);

        recyclerView.setAdapter(sentimentCommentAdapter);

        showPieChart();

        linearLayoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeAdminActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        linearLayoutReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeAdminActivity.this, GenerateReportActivity.class);
                startActivity(intent);
            }
        });

        linearLayoutCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeAdminActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        linearLayoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeAdminActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        linearLayoutComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeAdminActivity.this, ComplaintActivity.class);
                startActivity(intent);
            }
        });

        imgViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(HomeAdminActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.logout_confirmation_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                CardView cardViewNo, cardViewYes;

                cardViewNo = dialog.findViewById(R.id.cardViewNo);
                cardViewYes = dialog.findViewById(R.id.cardViewYes);

                cardViewNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                cardViewYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        SharedPreferences settings = getSharedPreferences("FeedBox", Context.MODE_PRIVATE);
                        settings.edit().clear().commit();

                        Intent intent= new Intent(HomeAdminActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });
                dialog.show();
            }
        });

        barChart.getAxisRight().setDrawLabels(false);

        GetActiveAY();
        LoadFeedback();
//        LoadSpin();

        LoadTotalUsers();


        LoadMonthly();
    }

    private void showPieChart(){


    }
//    public void setSpinText(Spinner spin, int pos) {
//        for(int i = 0; i < spin.getAdapter().getCount(); i++) {
////            if(spin.getAdapter().getItem(i).toString().equals(text)) {
////                spin.setSelection(i);
//////                position = i;
////            }
//            if(i == pos) {
//                spin.setSelection(i);
////                position = i;
//            }
//        }
//    }

    void GetActiveAY(){
        String url = URLDatabase.URL_AY_ACTIVE;
        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);
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
                    Toast.makeText(HomeAdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                }
                tvActiveAY.setText(AY_Range);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                tvActiveAY.setText(error.toString());
                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    double percentageLike, percentageDislike;
    double SubpercentageLike, SubpercentageDislike;
    void LoadPieChart() {

        percentageLike = 0;
        percentageDislike = 0;
        String url = URLDatabase.URL_PIE_CHART_FEEDBACK;

        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);

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

                                int like = Integer.parseInt(jsonObjectData.getString("like"));
                                int dislike = Integer.parseInt(jsonObjectData.getString("dislike"));

                                double sum = like + dislike;

                                percentageLike = (like / sum) * 100 ;
                                percentageLike = Math.round(percentageLike);

                                percentageDislike = (dislike / sum) * 100 ;
                                percentageDislike = Math.round(percentageDislike);

                                pieChart.setUsePercentValues(true);

                                //remove the description label on the lower left corner, default true if not set
                                pieChart.getDescription().setEnabled(false);

                                //enabling the user to rotate the chart, default true
                                pieChart.setRotationEnabled(true);
                                //adding friction when rotating the pie chart
                                pieChart.setDragDecelerationFrictionCoef(0.9f);
                                //setting the first entry start from right hand side, default starting from top
                                pieChart.setRotationAngle(0);

                                //highlight the entry when it is tapped, default true if not set
                                pieChart.setHighlightPerTapEnabled(true);
                                //adding animation so the entries pop up from 0 degree
                                //setting the color of the hole in the middle, default white
                                pieChart.setHoleColor(Color.parseColor("#000000"));

                                pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                    @Override
                                    public void onValueSelected(Entry e, Highlight h) {
                                        //Toast.makeText(HomeAdminActivity.this, String.valueOf(e.getY()), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected() {

                                    }
                                });

                                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                                String label = "type";

                                //initializing data
                                Map<String, Integer> typeAmountMap = new HashMap<>();

                                typeAmountMap.put("Like",(int)percentageLike);
                                typeAmountMap.put("Dislike",(int)percentageDislike);

                                //initializing colors for the entries
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#304567"));
                                colors.add(Color.parseColor("#309967"));

                                //input data and fit data into pie chart entry
                                for(String type: typeAmountMap.keySet()){
                                    pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
                                }

                                //collecting the entries with label name
                                PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
                                //setting text size of the value
                                pieDataSet.setValueTextSize(12f);
                                //providing color list for coloring different entries
                                pieDataSet.setColors(colors);
                                //grouping the data set from entry to chart
                                PieData pieData = new PieData(pieDataSet);
                                pieData.setValueFormatter(new PercentFormatter());
                                //showing the value of the entries, default true if not set
                                pieData.setDrawValues(true);

                                pieChart.setData(pieData);
                                pieChart.invalidate();
                            } catch (Exception err) {
                                Toast.makeText(HomeAdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(HomeAdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category", Category);
                params.put("ay_range", AY_Range);
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
    void LoadPieSubChart() {

        SubpercentageLike = 0;
        SubpercentageDislike = 0;
        String url = URLDatabase.URL_PIE_SUB_CHART_FEEDBACK;

        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);

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

                                int like = Integer.parseInt(jsonObjectData.getString("like"));
                                int dislike = Integer.parseInt(jsonObjectData.getString("dislike"));

                                double sum = like + dislike;

                                SubpercentageLike = (like / sum) * 100 ;
                                SubpercentageLike = Math.round(SubpercentageLike);

                                SubpercentageDislike = (dislike / sum) * 100 ;
                                SubpercentageDislike = Math.round(SubpercentageDislike);

                                pieSubChart.setUsePercentValues(true);

                                //remove the description label on the lower left corner, default true if not set
                                pieSubChart.getDescription().setEnabled(false);

                                //enabling the user to rotate the chart, default true
                                pieSubChart.setRotationEnabled(true);
                                //adding friction when rotating the pie chart
                                pieSubChart.setDragDecelerationFrictionCoef(0.9f);
                                //setting the first entry start from right hand side, default starting from top
                                pieSubChart.setRotationAngle(0);

                                //highlight the entry when it is tapped, default true if not set
                                pieSubChart.setHighlightPerTapEnabled(true);
                                //adding animation so the entries pop up from 0 degree
                                //setting the color of the hole in the middle, default white
                                pieSubChart.setHoleColor(Color.parseColor("#000000"));

                                pieSubChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                    @Override
                                    public void onValueSelected(Entry e, Highlight h) {
                                        //Toast.makeText(HomeAdminActivity.this, String.valueOf(e.getY()), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected() {

                                    }
                                });

                                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                                String label = "type";

                                //initializing data
                                Map<String, Integer> typeAmountMap = new HashMap<>();

                                typeAmountMap.put("Like",(int)SubpercentageLike);
                                typeAmountMap.put("Dislike",(int)SubpercentageDislike);

                                //initializing colors for the entries
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#304567"));
                                colors.add(Color.parseColor("#309967"));

                                //input data and fit data into pie chart entry
                                for(String type: typeAmountMap.keySet()){
                                    pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
                                }

                                //collecting the entries with label name
                                PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
                                //setting text size of the value
                                pieDataSet.setValueTextSize(12f);
                                //providing color list for coloring different entries
                                pieDataSet.setColors(colors);
                                //grouping the data set from entry to chart
                                PieData pieData = new PieData(pieDataSet);
                                pieData.setValueFormatter(new PercentFormatter());
                                //showing the value of the entries, default true if not set
                                pieData.setDrawValues(true);

                                pieSubChart.setData(pieData);
                                pieSubChart.invalidate();
                            } catch (Exception err) {
                                tvActiveAY.setText(err.getMessage());
                                Toast.makeText(HomeAdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(HomeAdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                tvActiveAY.setText(error.toString());
                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sub_category", SubCategory);
                params.put("category", Category);
                params.put("ay_range", AY_Range);
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
    void LoadSpinCat() {
        {
            ArrayAdapter ad = new ArrayAdapter(HomeAdminActivity.this, android.R.layout.simple_spinner_item, categories);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCat.setAdapter(ad);
            spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String[] simpleArray = new String[categories.size()];
                    categories.toArray(simpleArray);
                    Category = simpleArray[i];
                    LoadSubCategory();
                    LoadPieChart();
                    LoadPieSubChart();
                    LoadSentiment();
                    LoadMonthly();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    void LoadSpinSubCat () {
        {
            ArrayAdapter adS = new ArrayAdapter(HomeAdminActivity.this, android.R.layout.simple_spinner_item, subcategories);
            adS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubCat.setAdapter(adS);
            spinnerSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String[] simpleArray = new String[subcategories.size()];
                    subcategories.toArray(simpleArray);
                    SubCategory = simpleArray[i];
                    LoadPieSubChart();
                    LoadPieChart();
                    LoadSentiment();
                    LoadMonthly();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    void LoadFeedback() {
        String url = URLDatabase.URL_FEEDBACK_ADMIN_LIST;

        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);

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

                                String feedbackID = jsonObjectData.getString("feedback_id");
                                String email = jsonObjectData.getString("email");
                                String categoryName = jsonObjectData.getString("category_name");
                                String subCategoryName = jsonObjectData.getString("sub_category_name");
                                String sentiment = jsonObjectData.getString("sentiment");
                                String description = jsonObjectData.getString("description");
                                String status = jsonObjectData.getString("status");
                                String subStatus = jsonObjectData.getString("sub_status");
                                String ay_range = jsonObjectData.getString("ay_range");
                                String datePosted = jsonObjectData.getString("date_posted");

                                if (status.equals("Pending")) {
                                    PendingCount++;
                                } else if (status.equals("In Progress")) {
                                    InProgressCount++;
                                } else if (status.equals("Completed")) {
                                    CompletedCount++;
                                }
                            }
                            catch (Exception err) {
                                Toast.makeText(HomeAdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        tvPendingCount.setText(String.valueOf(PendingCount));
                        tvInProgressCount.setText(String.valueOf(InProgressCount));
                        tvCompletedCount.setText(String.valueOf(CompletedCount));

                        tvTotalFeedbacks.setText(String.valueOf(PendingCount + InProgressCount + CompletedCount));

                    }
                } catch (Exception e) {
                    Toast.makeText(HomeAdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                LoadCategory();
//                LoadAY();
                LoadSubCategory();
//                LoadSpinCat();
//                LoadSpinSubCat();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    void LoadTotalUsers() {
        String url = URLDatabase.URL_USER_LIST;

        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);

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
                            TotalUsers++;
                        }

                        tvTotalUsers.setText(String.valueOf(TotalUsers));
                    }

                } catch (Exception e) {

                    Toast.makeText(HomeAdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("user_type", "All");
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

    void LoadSentiment() {
        String url = URLDatabase.URL_SENTIMENT_LIST;

        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);

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

                                String description = jsonObjectData.getString("description");

                                sentimentCommentHelpers.add(new SentimentCommentHelper(description));

                                recyclerView.setAdapter(sentimentCommentAdapter);
                            } catch (Exception err) {
                                Toast.makeText(HomeAdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        tvSentimentCount.setText(jsonArray.length() + " response");
                    }
                } catch (Exception e) {
                    Toast.makeText(HomeAdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    void LoadMonthly() {
        count = 0;
        entries.clear();

        String url = URLDatabase.URL_BAR_GRAPH;

        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
    //                    try {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                        int fbCount = jsonObjectData.getInt("fbCount");
                        String month = jsonObjectData.getString("Month");
                        entries.add(new BarEntry(i, fbCount));
//                            Log.d("TAG", "onResponse: " + month + " FB_Count: " + fbCount);

                    }
                    YAxis yAxis = barChart.getAxisLeft();
                    yAxis.setAxisMaximum(0f);
                    yAxis.setAxisMaximum(100f);
                    yAxis.setAxisLineWidth(2f);
                    yAxis.setAxisLineColor(Color.BLACK);
                    yAxis.setLabelCount(10);

                    BarDataSet dataSet = new BarDataSet(entries, "Months");
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    BarData barData = new BarData(dataSet);

                    barChart.setData(barData);
                    barChart.getDescription().setEnabled(false);
                    barChart.invalidate();
                    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart.getXAxis().setGranularity(1f);
                    barChart.getXAxis().setGranularityEnabled(true);

                } catch (Exception err) {
                    Toast.makeText(HomeAdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("category", Category);
                params.put("ay_range", tvActiveAY.getText().toString());
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

    void LoadCategory() {
        String url = URLDatabase.URL_CATEGORY_LIST;

        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);

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
                                String datePosted = jsonObjectData.getString("date_posted");

                                categories.add(categoryName);
                            } catch (Exception err) {
//                                tvActiveAY.setText(err.getMessage());
                                Toast.makeText(HomeAdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
//                    tvPendingCount.setText(e.getMessage());
                    Toast.makeText(HomeAdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                ArrayAdapter ad = new ArrayAdapter(HomeAdminActivity.this, android.R.layout.simple_spinner_item, categories);
                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCat.setAdapter(ad);
                spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String[] simpleArray = new String[categories.size()];
                        categories.toArray(simpleArray);
                        Category = simpleArray[i];

                        subcategories.clear();
                        String url = URLDatabase.URL_SUB_CATEGORY_NAME_LIST;
                        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);

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

                                                String subCategoryID = jsonObjectData.getString("sub_category_id");
                                                String subCategoryName = jsonObjectData.getString("sub_category_name");
                                                String datePosted = jsonObjectData.getString("date_posted");

                                                subcategories.add(subCategoryName);
                                            } catch (Exception err) {

                                                Toast.makeText(HomeAdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        ArrayAdapter adS = new ArrayAdapter(HomeAdminActivity.this, android.R.layout.simple_spinner_item, subcategories);
                                        adS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                        spinnerSubCat.setAdapter(adS);
                                        spinnerSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                String[] simpleArray = new String[subcategories.size()];
                                                subcategories.toArray(simpleArray);
                                                SubCategory = simpleArray[i];
                                                LoadPieChart();
                                                LoadPieSubChart();

                                                LoadSentiment();
                                                LoadMonthly();
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                } catch (Exception e) {
//                                    tvPendingCount.setText(response);
                                    Toast.makeText(HomeAdminActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/x-www-form-urlencoded; charset=UTF-8";
                            }
                            @Override
                            protected Map<String, String> getParams() {
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
//                        LoadSubCategory();
//                        LoadPieChart();
//                        LoadPieSubChart();
//                        LoadSentiment();
//                        LoadMonthly();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    void LoadSubCategory() {

        subcategories.clear();
        String url = URLDatabase.URL_CATEGORY_SUB_LIST;

        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);

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

                                String subCategoryID = jsonObjectData.getString("sub_category_id");
                                String subCategoryName = jsonObjectData.getString("sub_category_name");
                                String datePosted = jsonObjectData.getString("date_posted");

                                subcategories.add(subCategoryName);
                            } catch (Exception err) {

                                Toast.makeText(HomeAdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
//                    tvPendingCount.setText(response);
                    Toast.makeText(HomeAdminActivity.this, response, Toast.LENGTH_SHORT).show();
                }
                spinnerSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String[] simpleArray = new String[subcategories.size()];
                        subcategories.toArray(simpleArray);
                        SubCategory = simpleArray[i];
                        LoadPieSubChart();
//                        LoadPieChart();
//                        LoadSentiment();
//                        LoadMonthly();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
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
//    void LoadAY() {
//        String url = URLDatabase.URL_AY_LIST;
//        RequestQueue queue = Volley.newRequestQueue(HomeAdminActivity.this);
//
//        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    for(int i = 0; i < jsonArray.length(); i++) {
//                        try {
//                            JSONObject jsonObjectData = jsonArray.getJSONObject(i);
//
//                            int ay_id = jsonObjectData.getInt("ay_id");
//                            String ay_range = jsonObjectData.getString("ay_range");
//                            String ay_status = jsonObjectData.getString("ay_status");
//
//                            ay_range_list.add(ay_range);
//                        } catch (Exception err) {
//                            Toast.makeText(HomeAdminActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
////                    }
//                } catch (Exception e) {
////                    tvPendingCount.setText(e.getMessage());
//                    Toast.makeText(HomeAdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
////                Log.d("TAG", "onResponse: " + response);
//                LoadSpin();
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(HomeAdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                //params.put("email", Email);
//                return params;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
//        );
//        queue.add(request);
//    }

    @Override
    public void onBackPressed() {

    }
}