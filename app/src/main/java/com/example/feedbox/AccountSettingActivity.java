package com.example.feedbox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class AccountSettingActivity extends AppCompatActivity {

    LinearLayout linearLayoutBack;
    EditText txtASFirstName, txtASLastName, txtASEmail, txtASPositon, txtPassword, txtConfirmPassword;
    Spinner ddASUTSpinner;
    TextView tvShowHide, tvConfirmShowHide;
    CardView cardViewUpdate;
    String userTypeSelected;
    String Email;
    int User_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settingactivity);

        SharedPreferences sh = getSharedPreferences("FeedBox", Context.MODE_PRIVATE);

        Email = sh.getString("email", "");

        linearLayoutBack = findViewById(R.id.linearLayoutBack);

        txtASFirstName = findViewById(R.id.txtASFirstName);
        txtASLastName = findViewById(R.id.txtASLastName);
        txtPassword = findViewById(R.id.txtPassword);
        txtASEmail = findViewById(R.id.txtASEmail);
        txtASPositon = findViewById(R.id.txtASPosition);

        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        tvShowHide = findViewById(R.id.tvShowHide);
        tvConfirmShowHide = findViewById(R.id.tvConfirmShowHide);
        cardViewUpdate = findViewById(R.id.cardViewUpdate);

        tvShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvShowHide.getText().toString().equals("SHOW"))
                {
                    tvShowHide.setText("HIDE");
                    txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    tvShowHide.setText("SHOW");
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        tvConfirmShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvConfirmShowHide.getText().toString().equals("SHOW"))
                {
                    tvConfirmShowHide.setText("HIDE");
                    txtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    tvConfirmShowHide.setText("SHOW");
                    txtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtASFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RegisterButtonWatcher();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RegisterButtonWatcher();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                RegisterButtonWatcher();
            }
        });

        txtASLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RegisterButtonWatcher();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RegisterButtonWatcher();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                RegisterButtonWatcher();
            }
        });

        txtASEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RegisterButtonWatcher();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RegisterButtonWatcher();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                RegisterButtonWatcher();
            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RegisterButtonWatcher();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RegisterButtonWatcher();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                RegisterButtonWatcher();
            }
        });

        txtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RegisterButtonWatcher();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RegisterButtonWatcher();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                RegisterButtonWatcher();
            }
        });

        cardViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString()))
                {
                    String url = URLDatabase.URL_ACCOUNT_SETTING_UPDATE;

                    RequestQueue queue = Volley.newRequestQueue(AccountSettingActivity.this);

                    StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            if(!txtASEmail.getText().toString().equals(Email)) {
                                Toast.makeText(AccountSettingActivity.this, "Email has been changed", Toast.LENGTH_SHORT).show();
                                SharedPreferences settings = getSharedPreferences("FeedBox", Context.MODE_PRIVATE);
                                settings.edit().clear().commit();

                                Intent intentOut = new Intent(AccountSettingActivity.this, LoginActivity.class);
                                startActivity(intentOut);
                                finishAffinity();
                            } else {
                                Intent intent= new Intent(AccountSettingActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Toast.makeText(AccountSettingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                            params.put("user_id", String.valueOf(User_ID));
                            params.put("first_name", txtASFirstName.getText().toString());
                            params.put("last_name", txtASLastName.getText().toString());
                            params.put("up_email", txtASEmail.getText().toString());
                            params.put("password", txtPassword.getText().toString());
//                            params.put("email", Email);
                            return params;
                        }
                    };
                    queue.add(request);
                }
                else
                {
                    Toast.makeText(AccountSettingActivity.this, "Password and Confirm Password must be the same.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LoadAccount();
    }

    void LoadAccount() {
        String url = URLDatabase.URL_HOME;

        RequestQueue queue = Volley.newRequestQueue(AccountSettingActivity.this);

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
                            int user_id = jsonObjectData.getInt("user_id");
                            String firstName = jsonObjectData.getString("first_name");
                            String lastName = jsonObjectData.getString("last_name");
                            String Email = jsonObjectData.getString("email");
                            String UserType = jsonObjectData.getString("user_type");
                            String password = jsonObjectData.getString("password");
//                            userTypeSelected = UserType;
                            User_ID = user_id;
                            txtASPositon.setText(UserType);
                            txtASEmail.setText(Email);
                            txtASFirstName.setText(firstName);
                            txtASLastName.setText(lastName);
                            txtPassword.setText(password);
                            txtConfirmPassword.setText(password);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(AccountSettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(AccountSettingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    void RegisterButtonWatcher()
    {
        if(txtASFirstName.getText().toString().isEmpty() ||
            txtASLastName.getText().toString().isEmpty() ||
            txtASEmail.getText().toString().isEmpty() ||
            txtPassword.getText().toString().isEmpty() ||
            txtConfirmPassword.getText().toString().isEmpty())
        {
            cardViewUpdate.setAlpha(0.2f);
            cardViewUpdate.setFocusable(false);
            cardViewUpdate.setClickable(false);
        }
        else
        {
            cardViewUpdate.setAlpha(1);
            cardViewUpdate.setFocusable(true);
            cardViewUpdate.setClickable(true);
        }
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        // On selecting a spinner item
//        userTypeSelected = parent.getItemAtPosition(position).toString();
//
//        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + userTypeSelected, Toast.LENGTH_LONG).show();
//
//    }
//
//    public void onNothingSelected(AdapterView<?> arg0) {
//        // TODO Auto-generated method stub
//
//    }
}