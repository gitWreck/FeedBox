package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {


    CardView cvSendCode;
    TextView txtEmailFP;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);

        cvSendCode = findViewById(R.id.cvSendCode);
        txtEmailFP = findViewById(R.id.txtEmailFP);

        txtEmailFP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SendCodeButtonWatcher();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SendCodeButtonWatcher();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                SendCodeButtonWatcher();
            }
        });


        cvSendCode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!txtEmailFP.getText().toString().isEmpty()) {

                    String url = URLDatabase.URL_CHECK_ACCOUNT;

                    RequestQueue queue = Volley.newRequestQueue(ForgotPasswordActivity.this);

                    StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("user_id").equals("null") || jsonObject.getString("user_id").equals("")) {
                                    Toast.makeText(ForgotPasswordActivity.this, "This email does not exist in the database.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Intent intent= new Intent(ForgotPasswordActivity.this, ForPassAuthActivity.class);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("VerificationCodeFP", getRandomNumberString());
                                    bundle.putString("EmailFP", txtEmailFP.getText().toString());

                                    intent.putExtras(bundle);

                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Toast.makeText(ForgotPasswordActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

                            params.put("email", txtEmailFP.getText().toString());
                            return params;
                        }
                    };
                    queue.add(request);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Student and Faculty/Administrator must use @bulsu.edu.ph emails", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void SendCodeButtonWatcher()
    {
        if(txtEmailFP.getText().toString().isEmpty()) {
            cvSendCode.setAlpha(0.2f);
            cvSendCode.setFocusable(false);
            cvSendCode.setClickable(false);
        } else {
            cvSendCode.setAlpha(1);
            cvSendCode.setFocusable(true);
            cvSendCode.setClickable(true);
        }
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}