package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    CardView cardViewLogin;
    TextView tvRegister, tvForPass;
    TextView tvShowHide, tvLoginAdmin;
    String UserLevel;
    String Email;
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
        setContentView(R.layout.login_activity);

        SharedPreferences sh = getSharedPreferences("FeedBox", Context.MODE_PRIVATE);

        UserLevel = sh.getString("user_level", "");
        Email = sh.getString("email", "");

        if(!UserLevel.isEmpty())
        {
            if(UserLevel.equals("1"))
            {
                Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
            else
            {
                Intent intent= new Intent(LoginActivity.this, HomeAdminActivity.class);
                startActivity(intent);
            }
        }
        else
        {
            if(!Email.isEmpty())
            {
                Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }

        }
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        cardViewLogin = findViewById(R.id.cardViewLogin);

        tvRegister = findViewById(R.id.tvRegister);
        tvForPass = findViewById(R.id.tvForgotPassword);

        tvShowHide = findViewById(R.id.tvShowHide);
        tvLoginAdmin = findViewById(R.id.tvLoginAdmin);

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
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoginChecker();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoginChecker();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                LoginChecker();
            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoginChecker();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoginChecker();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                LoginChecker();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        tvForPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);

        tvLoginAdmin.setVisibility(View.GONE);

        cardViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                String url = URLDatabase.URL_LOGIN;

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.contains("Admin")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("FeedBox",MODE_PRIVATE);

                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            myEdit.putString("email", txtEmail.getText().toString());
                            myEdit.putString("user_level", "0");

                            myEdit.commit();

                            Intent intent= new Intent(LoginActivity.this, HomeAdminActivity.class);
                            startActivity(intent);
                        } else {
                            if(response.contains("BlockedMessage")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    ToastMessage(jsonObject.getString("BlockedMessage"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (response.contains("InvalidMessage")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    ToastMessage(jsonObject.getString("InvalidMessage"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (response.contains("IncorrectPass")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    ToastMessage(jsonObject.getString("IncorrectPass"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for(int i = 0; i < jsonArray.length(); i++) {
                                        try {
                                            JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                                            //String floodJanMarch = jsonObjectData.getString("flood_jan_march");

                                            SharedPreferences sharedPreferences = getSharedPreferences("FeedBox", MODE_PRIVATE);

                                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                            myEdit.putString("email", txtEmail.getText().toString());
                                            myEdit.putString("user_level", "1");

                                            myEdit.commit();

                                            Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        } catch (Exception err) {
                                            Toast.makeText(LoginActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        EndProgLoad();
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        EndProgLoad();
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                        params.put("email", txtEmail.getText().toString());
                        params.put("password", txtPassword.getText().toString());
                        return params;
                    }
                };
                queue.add(request);
            }
        });

        tvLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this, LoginAdminActivity.class);
                startActivity(intent);
            }
        });
    }
    void ToastMessage(String loadMsg) {
        Toast.makeText(LoginActivity.this, loadMsg, Toast.LENGTH_SHORT).show();
    }

    void LoginChecker()
    {
        if(txtPassword.getText().toString().isEmpty() || txtEmail.getText().toString().isEmpty())
        {
            cardViewLogin.setAlpha(0.2f);
            cardViewLogin.setClickable(false);
            cardViewLogin.setFocusable(false);
        }
        else
        {
            cardViewLogin.setAlpha(1);
            cardViewLogin.setClickable(true);
            cardViewLogin.setFocusable(true);
        }
    }
}