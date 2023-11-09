package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class ForPassAuthActivity extends AppCompatActivity {
    LinearLayout linearLayoutBack;
    TextView tvEmailFP;
    String VerificationCode, UserType, FirstName, LastName, EmailFP, Password;

    CardView cardViewNumPad1, cardViewNumPad2, cardViewNumPad3,cardViewNumPad4,cardViewNumPad5,cardViewNumPad6,cardViewNumPad7,cardViewNumPad8,cardViewNumPad9, cardViewNumPad0;

    EditText txtCode1, txtCode2, txtCode3, txtCode4, txtCode5, txtCode6;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forpass_auth_activity);

        VerificationCode = getIntent().getExtras().getString("VerificationCodeFP");
        EmailFP = getIntent().getExtras().getString("EmailFP");

        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        tvEmailFP = findViewById(R.id.tvEmailFP);

        cardViewNumPad1 = findViewById(R.id.cardViewNumPad1);
        cardViewNumPad2 = findViewById(R.id.cardViewNumPad2);
        cardViewNumPad3 = findViewById(R.id.cardViewNumPad3);
        cardViewNumPad4 = findViewById(R.id.cardViewNumPad4);
        cardViewNumPad5 = findViewById(R.id.cardViewNumPad5);
        cardViewNumPad6 = findViewById(R.id.cardViewNumPad6);
        cardViewNumPad7 = findViewById(R.id.cardViewNumPad7);
        cardViewNumPad8 = findViewById(R.id.cardViewNumPad8);
        cardViewNumPad9 = findViewById(R.id.cardViewNumPad9);
        cardViewNumPad0 = findViewById(R.id.cardViewNumPad0);

        txtCode1 = findViewById(R.id.txtCodeFP1);
        txtCode2 = findViewById(R.id.txtCodeFP2);
        txtCode3 = findViewById(R.id.txtCodeFP3);
        txtCode4 = findViewById(R.id.txtCodeFP4);
        txtCode5 = findViewById(R.id.txtCodeFP5);
        txtCode6 = findViewById(R.id.txtCodeFP6);

        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvEmailFP.setText(EmailFP);

        SendCode();

        cardViewNumPad1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtCode1.getText().toString().isEmpty())
                {
                    txtCode1.setText("1");
                }
                else if(txtCode2.getText().toString().isEmpty())
                {
                    txtCode2.setText("1");
                }
                else if(txtCode3.getText().toString().isEmpty())
                {
                    txtCode3.setText("1");
                }
                else if(txtCode4.getText().toString().isEmpty())
                {
                    txtCode4.setText("1");
                }
                else if(txtCode5.getText().toString().isEmpty())
                {
                    txtCode5.setText("1");
                }
                else if(txtCode6.getText().toString().isEmpty())
                {
                    txtCode6.setText("1");
                }

                if(!txtCode1.getText().toString().isEmpty() &
                        !txtCode2.getText().toString().isEmpty() &
                        !txtCode3.getText().toString().isEmpty() &
                        !txtCode4.getText().toString().isEmpty() &
                        !txtCode5.getText().toString().isEmpty() &
                        !txtCode6.getText().toString().isEmpty()
                )
                {
                    String myVerificationCode = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString() + txtCode5.getText().toString() + txtCode6.getText().toString();

                    if(myVerificationCode.equals(VerificationCode))
                    {
                        openNewPassActivity();
                    }
                    else
                    {
                        txtCode1.setText("");
                        txtCode2.setText("");
                        txtCode3.setText("");
                        txtCode4.setText("");
                        txtCode5.setText("");
                        txtCode6.setText("");

                        Toast.makeText(ForPassAuthActivity.this, "Verification is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardViewNumPad2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(txtCode1.getText().toString().isEmpty())
                {
                    txtCode1.setText("2");
                }
                else if(txtCode2.getText().toString().isEmpty())
                {
                    txtCode2.setText("2");
                }
                else if(txtCode3.getText().toString().isEmpty())
                {
                    txtCode3.setText("2");
                }
                else if(txtCode4.getText().toString().isEmpty())
                {
                    txtCode4.setText("2");
                }
                else if(txtCode5.getText().toString().isEmpty())
                {
                    txtCode5.setText("2");
                }
                else if(txtCode6.getText().toString().isEmpty())
                {
                    txtCode6.setText("2");
                }

                if(!txtCode1.getText().toString().isEmpty() &
                        !txtCode2.getText().toString().isEmpty() &
                        !txtCode3.getText().toString().isEmpty() &
                        !txtCode4.getText().toString().isEmpty() &
                        !txtCode5.getText().toString().isEmpty() &
                        !txtCode6.getText().toString().isEmpty()
                )
                {
                    String myVerificationCode = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString() + txtCode5.getText().toString() + txtCode6.getText().toString();

                    if(myVerificationCode.equals(VerificationCode))
                    {
                        openNewPassActivity();
                    }
                    else
                    {
                        txtCode1.setText("");
                        txtCode2.setText("");
                        txtCode3.setText("");
                        txtCode4.setText("");
                        txtCode5.setText("");
                        txtCode6.setText("");

                        Toast.makeText(ForPassAuthActivity.this, "Verification is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardViewNumPad3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtCode1.getText().toString().isEmpty())
                {
                    txtCode1.setText("3");
                }
                else if(txtCode2.getText().toString().isEmpty())
                {
                    txtCode2.setText("3");
                }
                else if(txtCode3.getText().toString().isEmpty())
                {
                    txtCode3.setText("3");
                }
                else if(txtCode4.getText().toString().isEmpty())
                {
                    txtCode4.setText("3");
                }
                else if(txtCode5.getText().toString().isEmpty())
                {
                    txtCode5.setText("3");
                }
                else if(txtCode6.getText().toString().isEmpty())
                {
                    txtCode6.setText("3");
                }

                if(!txtCode1.getText().toString().isEmpty() &
                        !txtCode2.getText().toString().isEmpty() &
                        !txtCode3.getText().toString().isEmpty() &
                        !txtCode4.getText().toString().isEmpty() &
                        !txtCode5.getText().toString().isEmpty() &
                        !txtCode6.getText().toString().isEmpty()
                )
                {
                    String myVerificationCode = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString() + txtCode5.getText().toString() + txtCode6.getText().toString();

                    if(myVerificationCode.equals(VerificationCode))
                    {
                        openNewPassActivity();
                    }
                    else
                    {
                        txtCode1.setText("");
                        txtCode2.setText("");
                        txtCode3.setText("");
                        txtCode4.setText("");
                        txtCode5.setText("");
                        txtCode6.setText("");

                        Toast.makeText(ForPassAuthActivity.this, "Verification is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardViewNumPad4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtCode1.getText().toString().isEmpty())
                {
                    txtCode1.setText("4");
                }
                else if(txtCode2.getText().toString().isEmpty())
                {
                    txtCode2.setText("4");
                }
                else if(txtCode3.getText().toString().isEmpty())
                {
                    txtCode3.setText("4");
                }
                else if(txtCode4.getText().toString().isEmpty())
                {
                    txtCode4.setText("4");
                }
                else if(txtCode5.getText().toString().isEmpty())
                {
                    txtCode5.setText("4");
                }
                else if(txtCode6.getText().toString().isEmpty())
                {
                    txtCode6.setText("4");
                }

                if(!txtCode1.getText().toString().isEmpty() &
                        !txtCode2.getText().toString().isEmpty() &
                        !txtCode3.getText().toString().isEmpty() &
                        !txtCode4.getText().toString().isEmpty() &
                        !txtCode5.getText().toString().isEmpty() &
                        !txtCode6.getText().toString().isEmpty()
                )
                {
                    String myVerificationCode = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString() + txtCode5.getText().toString() + txtCode6.getText().toString();

                    if(myVerificationCode.equals(VerificationCode))
                    {
                        openNewPassActivity();
                    }
                    else
                    {
                        txtCode1.setText("");
                        txtCode2.setText("");
                        txtCode3.setText("");
                        txtCode4.setText("");
                        txtCode5.setText("");
                        txtCode6.setText("");

                        Toast.makeText(ForPassAuthActivity.this, "Verification is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardViewNumPad5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtCode1.getText().toString().isEmpty())
                {
                    txtCode1.setText("5");
                }
                else if(txtCode2.getText().toString().isEmpty())
                {
                    txtCode2.setText("5");
                }
                else if(txtCode3.getText().toString().isEmpty())
                {
                    txtCode3.setText("5");
                }
                else if(txtCode4.getText().toString().isEmpty())
                {
                    txtCode4.setText("5");
                }
                else if(txtCode5.getText().toString().isEmpty())
                {
                    txtCode5.setText("5");
                }
                else if(txtCode6.getText().toString().isEmpty())
                {
                    txtCode6.setText("5");
                }

                if(!txtCode1.getText().toString().isEmpty() &
                        !txtCode2.getText().toString().isEmpty() &
                        !txtCode3.getText().toString().isEmpty() &
                        !txtCode4.getText().toString().isEmpty() &
                        !txtCode5.getText().toString().isEmpty() &
                        !txtCode6.getText().toString().isEmpty()
                )
                {
                    String myVerificationCode = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString() + txtCode5.getText().toString() + txtCode6.getText().toString();

                    if(myVerificationCode.equals(VerificationCode))
                    {
                        openNewPassActivity();
                    }
                    else
                    {
                        txtCode1.setText("");
                        txtCode2.setText("");
                        txtCode3.setText("");
                        txtCode4.setText("");
                        txtCode5.setText("");
                        txtCode6.setText("");

                        Toast.makeText(ForPassAuthActivity.this, "Verification is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardViewNumPad6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtCode1.getText().toString().isEmpty())
                {
                    txtCode1.setText("6");
                }
                else if(txtCode2.getText().toString().isEmpty())
                {
                    txtCode2.setText("6");
                }
                else if(txtCode3.getText().toString().isEmpty())
                {
                    txtCode3.setText("6");
                }
                else if(txtCode4.getText().toString().isEmpty())
                {
                    txtCode4.setText("6");
                }
                else if(txtCode5.getText().toString().isEmpty())
                {
                    txtCode5.setText("6");
                }
                else if(txtCode6.getText().toString().isEmpty())
                {
                    txtCode6.setText("6");
                }

                if(!txtCode1.getText().toString().isEmpty() &
                        !txtCode2.getText().toString().isEmpty() &
                        !txtCode3.getText().toString().isEmpty() &
                        !txtCode4.getText().toString().isEmpty() &
                        !txtCode5.getText().toString().isEmpty() &
                        !txtCode6.getText().toString().isEmpty()
                )
                {
                    String myVerificationCode = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString() + txtCode5.getText().toString() + txtCode6.getText().toString();

                    if(myVerificationCode.equals(VerificationCode))
                    {
                        openNewPassActivity();
                    }
                    else
                    {
                        txtCode1.setText("");
                        txtCode2.setText("");
                        txtCode3.setText("");
                        txtCode4.setText("");
                        txtCode5.setText("");
                        txtCode6.setText("");

                        Toast.makeText(ForPassAuthActivity.this, "Verification is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardViewNumPad7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtCode1.getText().toString().isEmpty())
                {
                    txtCode1.setText("7");
                }
                else if(txtCode2.getText().toString().isEmpty())
                {
                    txtCode2.setText("7");
                }
                else if(txtCode3.getText().toString().isEmpty())
                {
                    txtCode3.setText("7");
                }
                else if(txtCode4.getText().toString().isEmpty())
                {
                    txtCode4.setText("7");
                }
                else if(txtCode5.getText().toString().isEmpty())
                {
                    txtCode5.setText("7");
                }
                else if(txtCode6.getText().toString().isEmpty())
                {
                    txtCode6.setText("7");
                }

                if(!txtCode1.getText().toString().isEmpty() &
                        !txtCode2.getText().toString().isEmpty() &
                        !txtCode3.getText().toString().isEmpty() &
                        !txtCode4.getText().toString().isEmpty() &
                        !txtCode5.getText().toString().isEmpty() &
                        !txtCode6.getText().toString().isEmpty()
                )
                {
                    String myVerificationCode = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString() + txtCode5.getText().toString() + txtCode6.getText().toString();

                    if(myVerificationCode.equals(VerificationCode))
                    {
                        openNewPassActivity();
                    }
                    else
                    {
                        txtCode1.setText("");
                        txtCode2.setText("");
                        txtCode3.setText("");
                        txtCode4.setText("");
                        txtCode5.setText("");
                        txtCode6.setText("");

                        Toast.makeText(ForPassAuthActivity.this, "Verification is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardViewNumPad8.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtCode1.getText().toString().isEmpty())
                {
                    txtCode1.setText("8");
                }
                else if(txtCode2.getText().toString().isEmpty())
                {
                    txtCode2.setText("8");
                }
                else if(txtCode3.getText().toString().isEmpty())
                {
                    txtCode3.setText("8");
                }
                else if(txtCode4.getText().toString().isEmpty())
                {
                    txtCode4.setText("8");
                }
                else if(txtCode5.getText().toString().isEmpty())
                {
                    txtCode5.setText("8");
                }
                else if(txtCode6.getText().toString().isEmpty())
                {
                    txtCode6.setText("8");
                }

                if(!txtCode1.getText().toString().isEmpty() &
                        !txtCode2.getText().toString().isEmpty() &
                        !txtCode3.getText().toString().isEmpty() &
                        !txtCode4.getText().toString().isEmpty() &
                        !txtCode5.getText().toString().isEmpty() &
                        !txtCode6.getText().toString().isEmpty()
                )
                {
                    String myVerificationCode = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString() + txtCode5.getText().toString() + txtCode6.getText().toString();

                    if(myVerificationCode.equals(VerificationCode))
                    {
                        openNewPassActivity();
                    }
                    else
                    {
                        txtCode1.setText("");
                        txtCode2.setText("");
                        txtCode3.setText("");
                        txtCode4.setText("");
                        txtCode5.setText("");
                        txtCode6.setText("");

                        Toast.makeText(ForPassAuthActivity.this, "Verification is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardViewNumPad9.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtCode1.getText().toString().isEmpty())
                {
                    txtCode1.setText("9");
                }
                else if(txtCode2.getText().toString().isEmpty())
                {
                    txtCode2.setText("9");
                }
                else if(txtCode3.getText().toString().isEmpty())
                {
                    txtCode3.setText("9");
                }
                else if(txtCode4.getText().toString().isEmpty())
                {
                    txtCode4.setText("9");
                }
                else if(txtCode5.getText().toString().isEmpty())
                {
                    txtCode5.setText("9");
                }
                else if(txtCode6.getText().toString().isEmpty())
                {
                    txtCode6.setText("9");
                }

                if(!txtCode1.getText().toString().isEmpty() &
                        !txtCode2.getText().toString().isEmpty() &
                        !txtCode3.getText().toString().isEmpty() &
                        !txtCode4.getText().toString().isEmpty() &
                        !txtCode5.getText().toString().isEmpty() &
                        !txtCode6.getText().toString().isEmpty()
                )
                {
                    String myVerificationCode = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString() + txtCode5.getText().toString() + txtCode6.getText().toString();

                    if(myVerificationCode.equals(VerificationCode))
                    {
                        openNewPassActivity();
                    }
                    else
                    {
                        txtCode1.setText("");
                        txtCode2.setText("");
                        txtCode3.setText("");
                        txtCode4.setText("");
                        txtCode5.setText("");
                        txtCode6.setText("");

                        Toast.makeText(ForPassAuthActivity.this, "Verification is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardViewNumPad0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtCode1.getText().toString().isEmpty())
                {
                    txtCode1.setText("0");
                }
                else if(txtCode2.getText().toString().isEmpty())
                {
                    txtCode2.setText("0");
                }
                else if(txtCode3.getText().toString().isEmpty())
                {
                    txtCode3.setText("0");
                }
                else if(txtCode4.getText().toString().isEmpty())
                {
                    txtCode4.setText("0");
                }
                else if(txtCode5.getText().toString().isEmpty())
                {
                    txtCode5.setText("0");
                }
                else if(txtCode6.getText().toString().isEmpty())
                {
                    txtCode6.setText("0");
                }


                if(!txtCode1.getText().toString().isEmpty() &
                        !txtCode2.getText().toString().isEmpty() &
                        !txtCode3.getText().toString().isEmpty() &
                        !txtCode4.getText().toString().isEmpty() &
                        !txtCode5.getText().toString().isEmpty() &
                        !txtCode6.getText().toString().isEmpty()
                )
                {
                    String myVerificationCode = txtCode1.getText().toString() + txtCode2.getText().toString() + txtCode3.getText().toString() + txtCode4.getText().toString() + txtCode5.getText().toString() + txtCode6.getText().toString();

                    if(myVerificationCode.equals(VerificationCode))
                    {
                        openNewPassActivity();
                    }
                    else
                    {
                        txtCode1.setText("");
                        txtCode2.setText("");
                        txtCode3.setText("");
                        txtCode4.setText("");
                        txtCode5.setText("");
                        txtCode6.setText("");

                        Toast.makeText(ForPassAuthActivity.this, "Verification is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    void SendCode()
    {
        String url = URLDatabase.URL_SEND_CODE;

        RequestQueue queue = Volley.newRequestQueue(ForPassAuthActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(ForPassAuthActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("verification_code", VerificationCode);
                params.put("email", EmailFP);
                return params;
            }
        };
        queue.add(request);
    }

    void openNewPassActivity()
    {
        String url = URLDatabase.URL_REGISTER;

        RequestQueue queue = Volley.newRequestQueue(ForPassAuthActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Intent intent= new Intent(ForPassAuthActivity.this, ForPassNewPassActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("EmailFP", EmailFP);

                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(ForPassAuthActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("email", EmailFP);
                return params;
            }
        };
        queue.add(request);
    }
}