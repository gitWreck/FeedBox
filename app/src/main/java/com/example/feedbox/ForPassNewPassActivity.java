package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
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

public class ForPassNewPassActivity extends AppCompatActivity {
    String EmailFP, NewPassword;
    CardView cvConfirmNewPass;
    EditText etNP, etCNP;
    TextView tvShowFP, tvShowCFP;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forpass_new_pass_activity);
        EmailFP = getIntent().getExtras().getString("EmailFP");
        cvConfirmNewPass = findViewById(R.id.cvNewPassFP);
        etNP = findViewById(R.id.etPasswordFP);
        etCNP = findViewById(R.id.etConfirmPasswordFP);

        tvShowFP = findViewById(R.id.tvShowHideFP);
        tvShowCFP = findViewById(R.id.tvConfirmShowHideFP);

        cvConfirmNewPass.setAlpha(1);
        cvConfirmNewPass.setFocusable(true);
        cvConfirmNewPass.setClickable(true);

        tvShowFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvShowFP.getText().toString().equals("SHOW"))
                {
                    tvShowFP.setText("HIDE");
                    etNP.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    tvShowFP.setText("SHOW");
                    etNP.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        tvShowCFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvShowCFP.getText().toString().equals("SHOW"))
                {
                    tvShowCFP.setText("HIDE");
                    etCNP.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    tvShowCFP.setText("SHOW");
                    etCNP.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        cvConfirmNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNP.getText().toString().isEmpty() || etCNP.getText().toString().isEmpty()) {
                    Toast.makeText(ForPassNewPassActivity.this, "Field Empty", Toast.LENGTH_SHORT).show();
                } else if (etNP.getText().toString().equals(etCNP.getText().toString())) {
                    NewPassword = etNP.getText().toString();
                    ConfirmNewPass();
                } else {
                    Toast.makeText(ForPassNewPassActivity.this, "Passwords does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void ConfirmNewPass()
    {
        String url = URLDatabase.URL_NEW_PASSWORD;

        RequestQueue queue = Volley.newRequestQueue(ForPassNewPassActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                String getMsg = new String(response);
                Toast.makeText(ForPassNewPassActivity.this, getMsg, Toast.LENGTH_SHORT).show();

                Intent intent= new Intent(ForPassNewPassActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(ForPassNewPassActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("emailFP", EmailFP);
                params.put("new_password", NewPassword);
                return params;
            }
        };
        queue.add(request);
    }

}