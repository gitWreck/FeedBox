package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    LinearLayout linearLayoutFeedback, linearLayoutSetting;
    ImageView imgView;
    TextView tvGuideline, tvFullName;
    String Slide, Email;
    CardView cardViewClickHere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        imgView = findViewById(R.id.imgView);
        linearLayoutFeedback = findViewById(R.id.linearLayoutFeedback);
        linearLayoutSetting = findViewById(R.id.linearLayoutSetting);
        tvGuideline = findViewById(R.id.tvGuideline);
        tvFullName = findViewById(R.id.tvFullName);
        cardViewClickHere = findViewById(R.id.cardViewClickHere);

        SharedPreferences sh = getSharedPreferences("FeedBox", Context.MODE_PRIVATE);

        Email = sh.getString("email", "");
//        Slide = sh.getString("slide", "");
//
//        if(Slide.isEmpty())
//        {
//            Dialog dialog = new Dialog(HomeActivity.this);
//
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setCancelable(true);
//            dialog.setCanceledOnTouchOutside(true);
//            dialog.setContentView(R.layout.slide_layout);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            Window window = dialog.getWindow();
//            WindowManager.LayoutParams wlp = window.getAttributes();
//
//            wlp.gravity = Gravity.BOTTOM;
//
//            window.setAttributes(wlp);
//
//            RecyclerView recyclerView;
//
//            recyclerView = dialog.findViewById(R.id.recyclerView);
//
//            List<SlideHelper> slideHelpers;
//            SlideAdapter slideAdapter;
//
//            slideHelpers = new ArrayList<>();
//
//            recyclerView.setHasFixedSize(true);
//
//            slideAdapter = new SlideAdapter(slideHelpers, this);
//
//            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//            recyclerView.setLayoutManager(linearLayoutManager2);
//
//            slideHelpers.add(new SlideHelper(R.drawable.slide_one));
//            slideHelpers.add(new SlideHelper(R.drawable.slide_two));
//            slideHelpers.add(new SlideHelper(R.drawable.slide_three));
//            slideHelpers.add(new SlideHelper(R.drawable.slide_four));
//            slideHelpers.add(new SlideHelper(R.drawable.slide_five));
//
//            recyclerView.setAdapter(slideAdapter);
//
//            dialog.show();
//        }

        cardViewClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(HomeActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.feedback_question);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                ImageView imgLike, imgDislike;

                imgLike = dialog.findViewById(R.id.imgLike);
                imgDislike = dialog.findViewById(R.id.imgDislike);

                imgLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent= new Intent(HomeActivity.this, SendFeedbackActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Feedback", "Like");

                        intent.putExtras(bundle);

                        startActivity(intent);

                        dialog.dismiss();
                    }
                });

                imgDislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent= new Intent(HomeActivity.this, SendFeedbackActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Feedback", "Dislike");

                        intent.putExtras(bundle);

                        startActivity(intent);

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        linearLayoutFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        linearLayoutSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        tvGuideline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(HomeActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.guideline_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                CardView cardViewFinish;

                cardViewFinish = dialog.findViewById(R.id.cardViewFinish);

                cardViewFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        LoadAccount();
    }

    void LoadAccount()
    {

        String url = URLDatabase.URL_HOME;

        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);

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
                            JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                            String firstName = jsonObjectData.getString("first_name");
//                            String lastName = jsonObjectData.getString("last_name");
                            //String picture = jsonObjectData.getString("picture");

                            /*if(!picture.equals("null"))
                            {
                                byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                imgViewPicture.setImageBitmap(decodedByte);
                            }*/

                            tvFullName.setText("Hey " + firstName + "!");
                        }
                    }

                } catch (Exception e) {

                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {

    }
}