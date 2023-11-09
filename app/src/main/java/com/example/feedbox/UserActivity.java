package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class UserActivity extends AppCompatActivity {

    List<UserHelper> userHelpers;
    UserAdapter userAdapter;

    RecyclerView recyclerView;
    LinearLayout linearLayoutBack, linearLayoutFilter;
    public String UserType = "All";
    CardView cardViewAdd;

    Dialog dialog;
    public static UserActivity userActivity;
    public static UserActivity getInstance() {
        return userActivity;
    }
    public Dialog getDialog() {
        return dialog;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        userActivity = this;

        userHelpers = new ArrayList<>();

        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        linearLayoutFilter = findViewById(R.id.linearLayoutFilterU);
//        cardViewAdd = findViewById(R.id.cardViewAdd);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        userAdapter = new UserAdapter(userHelpers, this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager2);

        recyclerView.setAdapter(userAdapter);

        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        linearLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(UserActivity.this);

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

                List<FilterUserTypeHelper> filterUserTypeHelpers;
                FilterUserTypeAdapter filterUserTypeAdapter;
                RecyclerView recyclerView;

                recyclerView = dialog.findViewById(R.id.recyclerView);

                filterUserTypeHelpers = new ArrayList<>();

                recyclerView.setHasFixedSize(true);

                filterUserTypeAdapter = new FilterUserTypeAdapter(filterUserTypeHelpers, UserActivity.this);

                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager2);

                filterUserTypeHelpers.add(new FilterUserTypeHelper("All"));
                recyclerView.setAdapter(filterUserTypeAdapter);

                //CATEGORY
                {
                    String url = URLDatabase.URL_USER_TYPE_LIST;

                    RequestQueue queue = Volley.newRequestQueue(UserActivity.this);

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

                                            String userType = jsonObjectData.getString("user_type");

                                            filterUserTypeHelpers.add(new FilterUserTypeHelper(userType));
                                        }

                                        catch (Exception err)
                                        {
                                            Toast.makeText(UserActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    recyclerView.setAdapter(filterUserTypeAdapter);
                                }

                            } catch (Exception e) {

                                Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Toast.makeText(UserActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                            params.put("user_type", UserType);
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


        LoadUser();
    }
    void LoadUser()
    {
        userHelpers.clear();
        recyclerView.setAdapter(userAdapter);

        Log.d("TAG", "LoadUser: " + UserType);

        String url = URLDatabase.URL_USER_LIST;
        RequestQueue queue = Volley.newRequestQueue(UserActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals("[]"))
                    {
                        userHelpers.clear();

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            try {
                                JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                                String userID = jsonObjectData.getString("user_id");
                                String firstName = jsonObjectData.getString("first_name");
                                String lastName = jsonObjectData.getString("last_name");
                                String email = jsonObjectData.getString("email");
                                String position = jsonObjectData.getString("position");
                                String muteCounter = jsonObjectData.getString("mute_counter");

//                                String fullName = jsonObjectData.getString("full_name");
//                                String status = jsonObjectData.getString("status");
//                                fullName,
                                userHelpers.add(new UserHelper(userID, firstName, lastName, Integer.valueOf(muteCounter), email, position));
                            }

                            catch (Exception err)
                            {
                                Toast.makeText(UserActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        recyclerView.setAdapter(userAdapter);
                    } else {
                        Toast.makeText(UserActivity.this, "Empty", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {

                    Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(UserActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("user_type", UserType);
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