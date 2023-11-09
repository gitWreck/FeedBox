package com.example.feedbox;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> implements AdapterView.OnItemSelectedListener
{
    String[] departments = { "COED", "CBA", "CICS", "COE", "CIT" };
    Context context;
    String Department;
    private List<AdminHelper> mAdmin;

    public AdminAdapter(List<AdminHelper> Admins, Context context2)
    {
        mAdmin = Admins;
        context = context2;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(AdminAdapter.ViewHolder holder, int position)
    {
        AdminHelper AdminHelper = mAdmin.get(position);

        holder.tvFullName.setText(AdminHelper.getFullName());
        holder.tvDepartment.setText(AdminHelper.getDepartment());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.admin_add_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                TextView tvDialogName;
                LinearLayout linearLayoutBack;
                CardView cardViewSubmit;
                Spinner spinner;

                EditText txtFullName, txtPhoneNumber, txtEmail, txtPassword;

                tvDialogName = dialog.findViewById(R.id.tvDialogName);
                linearLayoutBack = dialog.findViewById(R.id.linearLayoutBack);
                cardViewSubmit = dialog.findViewById(R.id.cardViewSubmit);
                spinner = dialog.findViewById(R.id.spinner);

                txtFullName = dialog.findViewById(R.id.txtFullName);
                txtPhoneNumber = dialog.findViewById(R.id.txtPhoneNumber);
                txtEmail = dialog.findViewById(R.id.txtEmail);
                txtPassword = dialog.findViewById(R.id.txtPassword);

                txtFullName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtFullName.getText().toString().isEmpty() ||
                                txtPhoneNumber.getText().toString().isEmpty() ||
                                txtEmail.getText().toString().isEmpty() ||
                                txtPassword.getText().toString().isEmpty()
                        )
                        {
                            cardViewSubmit.setEnabled(false);
                            cardViewSubmit.setClickable(false);
                            cardViewSubmit.setFocusable(false);
                            cardViewSubmit.setAlpha(0.2f);
                        }
                        else
                        {
                            cardViewSubmit.setEnabled(true);
                            cardViewSubmit.setClickable(true);
                            cardViewSubmit.setFocusable(true);
                            cardViewSubmit.setAlpha(1.0f);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                txtPhoneNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtFullName.getText().toString().isEmpty() ||
                                txtPhoneNumber.getText().toString().isEmpty() ||
                                txtEmail.getText().toString().isEmpty() ||
                                txtPassword.getText().toString().isEmpty()
                        )
                        {
                            cardViewSubmit.setEnabled(false);
                            cardViewSubmit.setClickable(false);
                            cardViewSubmit.setFocusable(false);
                            cardViewSubmit.setAlpha(0.2f);
                        }
                        else
                        {
                            cardViewSubmit.setEnabled(true);
                            cardViewSubmit.setClickable(true);
                            cardViewSubmit.setFocusable(true);
                            cardViewSubmit.setAlpha(1.0f);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                txtEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtFullName.getText().toString().isEmpty() ||
                                txtPhoneNumber.getText().toString().isEmpty() ||
                                txtEmail.getText().toString().isEmpty() ||
                                txtPassword.getText().toString().isEmpty()
                        )
                        {
                            cardViewSubmit.setEnabled(false);
                            cardViewSubmit.setClickable(false);
                            cardViewSubmit.setFocusable(false);
                            cardViewSubmit.setAlpha(0.2f);
                        }
                        else
                        {
                            cardViewSubmit.setEnabled(true);
                            cardViewSubmit.setClickable(true);
                            cardViewSubmit.setFocusable(true);
                            cardViewSubmit.setAlpha(1.0f);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                txtPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(txtFullName.getText().toString().isEmpty() ||
                                txtPhoneNumber.getText().toString().isEmpty() ||
                                txtEmail.getText().toString().isEmpty() ||
                                txtPassword.getText().toString().isEmpty()
                        )
                        {
                            cardViewSubmit.setEnabled(false);
                            cardViewSubmit.setClickable(false);
                            cardViewSubmit.setFocusable(false);
                            cardViewSubmit.setAlpha(0.2f);
                        }
                        else
                        {
                            cardViewSubmit.setEnabled(true);
                            cardViewSubmit.setClickable(true);
                            cardViewSubmit.setFocusable(true);
                            cardViewSubmit.setAlpha(1.0f);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                txtFullName.setText(AdminHelper.getFullName());
                txtPhoneNumber.setText(AdminHelper.getContactNumber());
                txtEmail.setText(AdminHelper.getEmail());
                txtPassword.setText(AdminHelper.getPassword());

                spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) context);

                ArrayAdapter ad = new ArrayAdapter(context, android.R.layout.simple_spinner_item, departments);

                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(ad);
                spinner.setSelection(ad.getPosition(AdminHelper.getDepartment()));

                tvDialogName.setText("Edit Sub-Admin");

                linearLayoutBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                cardViewSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = URLDatabase.URL_ADMIN_EDIT;

                        RequestQueue queue = Volley.newRequestQueue(context);

                        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                dialog.dismiss();

                                AdminHelper.setFullName(txtFullName.getText().toString());
                                AdminHelper.setDepartment(spinner.getSelectedItem().toString());
                                AdminHelper.setEmail(txtEmail.getText().toString());
                                AdminHelper.setPassword(txtPassword.getText().toString());
                                AdminHelper.setContactNumber(txtPhoneNumber.getText().toString());

                                notifyItemChanged(position);
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
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
                                params.put("admin_id", AdminHelper.getAdminID());
                                params.put("full_name", txtFullName.getText().toString());
                                params.put("department", spinner.getSelectedItem().toString());
                                params.put("phone_number", txtPhoneNumber.getText().toString());
                                params.put("email", txtEmail.getText().toString());
                                params.put("password", txtPassword.getText().toString());
                                return params;
                            }
                        };
                        queue.add(request);
                    }
                });
                dialog.show();
            }
        });
    }
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
    {
        Department = departments[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mAdmin.size();
    }

    @Override
    public AdminAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.admin_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvFullName, tvDepartment;

        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvDepartment = itemView.findViewById(R.id.tvDepartment);
        }
    }
}
