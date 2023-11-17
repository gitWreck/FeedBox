package com.example.feedbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LinearLayout linearLayoutBack;
    Spinner ddUTSpinner;
    CardView cardViewRegister;
    String userTypeSelected, SelectAgreement;
    CheckBox cbTPP;
    EditText txtFirstName, txtLastName, txtEmail, txtPassword, txtConfirmPassword;
    TextView tvShowHide, tvConfirmShowHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        SelectAgreement = null;

        ddUTSpinner = findViewById(R.id.spnUserType);
        ddUTSpinner.setOnItemSelectedListener(this);
        userTypeSelected = "Student";

        List<String> categories = new ArrayList<String>();
        categories.add("Student");
        categories.add("Faculty/Administrator");
        categories.add("Parent/Old Student/Visitor");
        categories.add("Other School Personnel");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddUTSpinner.setAdapter(dataAdapter);

        linearLayoutBack = findViewById(R.id.linearLayoutBack);
        cardViewRegister = findViewById(R.id.cardViewRegister);

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        tvShowHide = findViewById(R.id.tvShowHide);
        tvConfirmShowHide = findViewById(R.id.tvConfirmShowHide);

        cbTPP = findViewById(R.id.cbTPP);

        cbTPP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Dialog dialog = new Dialog(RegisterActivity.this);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setContentView(R.layout.terms_and_privacy_policy_layout);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    wlp.gravity = Gravity.BOTTOM;

                    window.setAttributes(wlp);
                    CardView cvDisagree, cvAgree;
                    cvDisagree = dialog.findViewById(R.id.cardViewDisagree);
                    cvAgree = dialog.findViewById(R.id.cardViewAgree);

                    cvAgree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SelectAgreement = "Agree";
                            RegisterButtonWatcher();
                            dialog.dismiss();
                        }
                    });
                    cvDisagree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SelectAgreement = "Disagree";
                            cbTPP.setChecked(false);
                            RegisterButtonWatcher();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                } else {
                    SelectAgreement = "Disagree";
                    RegisterButtonWatcher();
                }
            }
        });

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

        txtFirstName.addTextChangedListener(new TextWatcher() {
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
        txtEmail.addTextChangedListener(new TextWatcher() {
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

        cardViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                    if (userTypeSelected == "Student" || userTypeSelected == "Faculty/Administrator" && !txtEmail.getText().toString().contains("@bulsu.edu.ph")) {
//                        Toast.makeText(RegisterActivity.this, "nice", Toast.LENGTH_SHORT).show();
//                        SendRegistration();
                        if(txtEmail.getText().toString().contains("@bulsu.edu.ph")) {
                            SendRegistration();
                        } else {
                            Toast.makeText(RegisterActivity.this, userTypeSelected + " must use @bulsu.edu.ph emails", Toast.LENGTH_SHORT).show();
                        }
                        } else if (userTypeSelected != "Student" || userTypeSelected != "Faculty/Administrator") {
                        SendRegistration();
                    } else  {
                        SendRegistration();
                    }
//                    else {
//                        Toast.makeText(RegisterActivity.this, "Student and Faculty/Administrator must use @bulsu.edu.ph emails", Toast.LENGTH_SHORT).show();
//                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Password and Confirm Password must be the same.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void SendRegistration() {
        String url = URLDatabase.URL_CHECK_ACCOUNT;

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("user_id").equals("null") || jsonObject.getString("user_id").equals(""))
                    {

                        Intent intent= new Intent(RegisterActivity.this, RegisterAuthenticationActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("VerificationCode", getRandomNumberString());
                        bundle.putString("UserType", userTypeSelected);
                        bundle.putString("FirstName", txtFirstName.getText().toString());
                        bundle.putString("LastName", txtLastName.getText().toString());
                        //                                    bundle.putString("FullName", txtFirstName.getText().toString());

                        bundle.putString("Email", txtEmail.getText().toString());
                        bundle.putString("Password", txtPassword.getText().toString());

                        intent.putExtras(bundle);

                        startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Your email is already existing in the database.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        queue.add(request);
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    void RegisterButtonWatcher() {
        if(txtFirstName.getText().toString().isEmpty() ||
                txtEmail.getText().toString().isEmpty() ||
                txtPassword.getText().toString().isEmpty() ||
                txtConfirmPassword.getText().toString().isEmpty()
                        || SelectAgreement != "Agree")
        {
            cardViewRegister.setAlpha(0.2f);
            cardViewRegister.setFocusable(false);
            cardViewRegister.setClickable(false);
        }
        else
        {
            cardViewRegister.setAlpha(1);
            cardViewRegister.setFocusable(true);
            cardViewRegister.setClickable(true);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        userTypeSelected = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + userTypeSelected, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}