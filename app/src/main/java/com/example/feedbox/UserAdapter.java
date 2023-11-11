package com.example.feedbox;



import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.util.Log;
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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>
{
    String[] statuses = { "Active", "Inactive" };
    Context context;
    int MuteCount;

    ProgressDialog progressDialog;
    private List<UserHelper> mUser;

    public UserAdapter(List<UserHelper> Users, Context context2)
    {
        mUser = Users;
        context = context2;
    }
    private void EndProgLoad(){
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position)
    {
        MuteCount = 0;
        UserHelper UserHelper = mUser.get(position);
        MuteCount = UserHelper.getMuteCounter();
        holder.tvFirstName.setText(UserHelper.getFirstName());
        holder.tvLastName.setText(UserHelper.getLastName());
        holder.tvEmail.setText(UserHelper.getEmail());
        holder.tvPosition.setText(UserHelper.getPosition());
//        holder.tvMuteCounter.setText(String.valueOf(UserHelper.getMuteCounter()));
        holder.tvMuteCounter.setText(String.valueOf(MuteCount));
//        holder.tvMuteCounter.update
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        holder.imgBTNMuteUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Dialog dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.mute_item_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                LinearLayout linearLayoutMuteUser, linearLayoutComplete;
                TextView tvEmail;

                linearLayoutMuteUser = dialog.findViewById(R.id.linearLayoutMuteUser);
//                linearLayoutComplete = dialog.findViewById(R.id.linearLayoutComplete);
                tvEmail = dialog.findViewById(R.id.tvMuteUser);

                tvEmail.setText(UserHelper.getEmail());

                linearLayoutMuteUser.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        progressDialog.setMessage("Sending...");
                        progressDialog.show();
                        if(MuteCount < 3) {
                            holder.tvMuteCounter.setText(String.valueOf(++MuteCount));
                        }
                        Log.d("daw", "onClick: " + MuteCount);
                        String url = URLDatabase.URL_MUTE_USER;

                        RequestQueue queue = Volley.newRequestQueue(context);

                        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
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
                                params.put("email", UserHelper.getEmail());
//                                params.put("feedback_id", FeedbackAdminHelper.getFeedBackID());
                                return params;
                            }
                        };
                        queue.add(request);
                        dialog.dismiss();
                        EndProgLoad();
                    }
                });

//                linearLayoutComplete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String url = URLDatabase.URL_FEEDBACK_COMPLETE;
//
//                        RequestQueue queue = Volley.newRequestQueue(context);
//
//                        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                dialog.dismiss();
//                            }
//                        }, new com.android.volley.Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error)
//                            {
//                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }) {
//                            @Override
//                            public String getBodyContentType() {
//                                return "application/x-www-form-urlencoded; charset=UTF-8";
//                            }
//
//                            @Override
//                            protected Map<String, String> getParams()
//                            {
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("email", UserHelper.getEmail());
//                                return params;
//                            }
//                        };
//                        queue.add(request);
//                    }
//                });

                dialog.show();
            }
        });
//        try
//        {
//
////            holder.tvFullName.setText(UserHelper.getFullName());
//
////            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                @Override
////                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                    Status = statuses[i];
////
////                    String url = URLDatabase.URL_USER_STATUS_EDIT;
////
////                    RequestQueue queue = Volley.newRequestQueue(context);
////
////                    StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
////                        @Override
////                        public void onResponse(String response)
////                        {
////                        }
////                    }, new com.android.volley.Response.ErrorListener() {
////                        @Override
////                        public void onErrorResponse(VolleyError error)
////                        {
////                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
////                        }
////                    }) {
////                        @Override
////                        public String getBodyContentType() {
////                            return "application/x-www-form-urlencoded; charset=UTF-8";
////                        }
////
////                        @Override
////                        protected Map<String, String> getParams()
////                        {
////                            Map<String, String> params = new HashMap<String, String>();
////                            params.put("user_id", UserHelper.getUserID());
////                            params.put("status", Status);
////                            return params;
////                        }
////                    };
////                    queue.add(request);
////                }
////
////                @Override
////                public void onNothingSelected(AdapterView<?> adapterView) {
////
////                }
////            });
////
////            ArrayAdapter ad = new ArrayAdapter(context, android.R.layout.simple_spinner_item, statuses);
////
////            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////
////            holder.spinner.setAdapter(ad);
////            holder.spinner.setSelection(ad.getPosition(UserHelper.getStatus()));
//        }
//        catch (Exception err)
//        {
//            Toast.makeText(context, err.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.user_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvFullName, tvFirstName, tvLastName, tvEmail, tvPosition, tvMuteCounter;
        Spinner spinner;
        ImageView imgBTNMuteUser;

        public ViewHolder(View itemView) {
            super(itemView);

            imgBTNMuteUser = itemView.findViewById(R.id.imgBTNMuteUser);

//            spinner = itemView.findViewById(R.id.spinner);
//            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvFirstName = itemView.findViewById(R.id.tvFirstNameUI);
            tvLastName = itemView.findViewById(R.id.tvLastNameUI);
            tvEmail = itemView.findViewById(R.id.tvEmail_emailUI);
            tvPosition = itemView.findViewById(R.id.tvFullPositionUI);
            tvMuteCounter = itemView.findViewById(R.id.tvMuteCounterUI);
        }
    }
}

