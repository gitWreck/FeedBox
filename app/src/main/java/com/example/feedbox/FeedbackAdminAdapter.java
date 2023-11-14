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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class FeedbackAdminAdapter extends RecyclerView.Adapter<FeedbackAdminAdapter.ViewHolder>
{
    Context context;
    ProgressDialog progressDialog;
    private List<FeedbackAdminHelper> mFeedbackAdmin;

    public FeedbackAdminAdapter(List<FeedbackAdminHelper> FeedbackAdmins, Context context2)
    {
        mFeedbackAdmin = FeedbackAdmins;
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
    public void onBindViewHolder(FeedbackAdminAdapter.ViewHolder holder, int position)
    {
        FeedbackAdminHelper FeedbackAdminHelper = mFeedbackAdmin.get(position);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        if(FeedbackAdminHelper.getSentiment().equals("Like"))
        {
            holder.imgSentiment.setImageTintList(context.getColorStateList(R.color.emerald));
            holder.tvReasons.setVisibility(View.GONE);
            holder.tvReasonsLbl.setVisibility(View.GONE);
        }
        else
        {
            holder.imgSentiment.setImageTintList(context.getColorStateList(R.color.alizarin));
            holder.imgSentiment.setRotation(180.0f);
        }

//        holder.tvFullName.setText(FeedbackAdminHelper.getFullName());
        holder.tvFirstName.setText(FeedbackAdminHelper.getFirstName());
        holder.tvLastName.setText(FeedbackAdminHelper.getLastName());
        holder.tvStatus.setText(FeedbackAdminHelper.getStatus());
        holder.tvCategoryName.setText(FeedbackAdminHelper.getCategoryName());
        holder.tvSubCategoryName.setText(FeedbackAdminHelper.getSubCategoryName());
        holder.tvDescription.setText(FeedbackAdminHelper.getDescription());
        holder.tvDatePosted.setText(FeedbackAdminHelper.getDatePosted());

        holder.tvDetails.setText(FeedbackAdminHelper.getDetails());
        holder.tvSubDetails.setText(FeedbackAdminHelper.getSubDetails());
        holder.tvReasons.setText(FeedbackAdminHelper.getReasons());
        holder.tvfBID.setText(FeedbackAdminHelper.getFeedBackID());

        if(FeedbackAdminHelper.getStatus().equals("Pending"))
        {
            holder.cardViewUpdate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Dialog dialog = new Dialog(context);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setContentView(R.layout.select_action_layout);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    wlp.gravity = Gravity.BOTTOM;

                    window.setAttributes(wlp);

                    LinearLayout linearLayoutEmail, linearLayoutComplete;
                    TextView tvEmail;

                    linearLayoutEmail = dialog.findViewById(R.id.linearLayoutEmail);
                    linearLayoutComplete = dialog.findViewById(R.id.linearLayoutComplete);
                    tvEmail = dialog.findViewById(R.id.tvEmail);

                    tvEmail.setText(FeedbackAdminHelper.getEmail());

                    linearLayoutEmail.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            progressDialog.setMessage("Sending...");
                            progressDialog.show();
                            String url = URLDatabase.URL_SEND_FEEDBACK_EMAIL;

                            RequestQueue queue = Volley.newRequestQueue(context);

                            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(context, "Email sent successfully to : " + FeedbackAdminHelper.getEmail(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
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
                                    params.put("email", FeedbackAdminHelper.getEmail());
                                    params.put("feedback_id", FeedbackAdminHelper.getFeedBackID());
                                    return params;
                                }
                            };
                            queue.add(request);
                            EndProgLoad();
                        }
                    });

                    linearLayoutComplete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = URLDatabase.URL_FEEDBACK_COMPLETE;

                            RequestQueue queue = Volley.newRequestQueue(context);

                            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(context, FeedbackAdminHelper.getEmail() + " Complaint completed", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
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
                                    params.put("feedback_id", FeedbackAdminHelper.getFeedBackID());
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
        else if(FeedbackAdminHelper.getStatus().equals("In Progress"))
        {
            holder.cardViewUpdate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Dialog dialog = new Dialog(context);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setContentView(R.layout.select_action_layout);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    wlp.gravity = Gravity.BOTTOM;

                    window.setAttributes(wlp);

                    LinearLayout linearLayoutEmail, linearLayoutComplete;
                    TextView tvEmail;

                    linearLayoutEmail = dialog.findViewById(R.id.linearLayoutEmail);
                    linearLayoutComplete = dialog.findViewById(R.id.linearLayoutComplete);
                    tvEmail = dialog.findViewById(R.id.tvEmail);

                    tvEmail.setText("Email: " + FeedbackAdminHelper.getEmail());

//                    linearLayoutEmail.setVisibility(View.GONE);

                    linearLayoutEmail.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            Dialog dialog = new Dialog(context);

                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setContentView(R.layout.send_gmail);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            Window window = dialog.getWindow();
                            WindowManager.LayoutParams wlp = window.getAttributes();

                            wlp.gravity = Gravity.BOTTOM;

                            window.setAttributes(wlp);

                            TextView tvEmail;
                            EditText etBod, etSub;
                            CardView btnSendGmail;

                            etBod = dialog.findViewById(R.id.etEMBod);
                            etSub = dialog.findViewById(R.id.etEMSub);
                            tvEmail = dialog.findViewById(R.id.UserEmail);
                            btnSendGmail = dialog.findViewById(R.id.cvSendGmail);

                            tvEmail.setText(FeedbackAdminHelper.getEmail());
                            btnSendGmail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String url = URLDatabase.URL_SEND_FEEDBACK_EMAIL;

                                    RequestQueue queue = Volley.newRequestQueue(context);

                                    StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            dialog.dismiss();
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
                                            params.put("email", FeedbackAdminHelper.getEmail());
                                            params.put("etSub", etSub.getText().toString());
                                            params.put("etBod", etBod.getText().toString());
//                                            params.put("feedback_id", FeedbackAdminHelper.getFeedBackID());
                                            return params;
                                        }
                                    };
                                    queue.add(request);
                                }
                            });
                            dialog.show();

                        }
                    });

                    linearLayoutComplete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = URLDatabase.URL_FEEDBACK_COMPLETE;

                            RequestQueue queue = Volley.newRequestQueue(context);

                            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
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
                                    params.put("feedback_id", FeedbackAdminHelper.getFeedBackID());
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
        else if(FeedbackAdminHelper.getStatus().equals("Completed"))
        {
            holder.cardViewUpdate.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mFeedbackAdmin.size();
    }

    @Override
    public FeedbackAdminAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.complaint_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardViewUpdate;
        TextView tvFullName, tvFirstName, tvLastName, tvStatus, tvCategoryName, tvSubCategoryName, tvDescription, tvDatePosted, tvUpdate,
            tvDetails, tvSubDetails, tvReasons, tvReasonsLbl, tvfBID;
        ImageView imgSentiment;
        public ViewHolder(View itemView) {
            super(itemView);

//            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvfBID = itemView.findViewById(R.id.tvUID);

            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvSubDetails = itemView.findViewById(R.id.tvSubDetails);
            tvReasons = itemView.findViewById(R.id.tvReasons);
            tvReasonsLbl = itemView.findViewById(R.id.tvReasonsLbl);

            tvFirstName = itemView.findViewById(R.id.tvFirstName);
            tvLastName = itemView.findViewById(R.id.tvLastName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvSubCategoryName = itemView.findViewById(R.id.tvSubCategoryName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDatePosted = itemView.findViewById(R.id.tvDatePosted);
            imgSentiment = itemView.findViewById(R.id.imgSentiment);
            cardViewUpdate = itemView.findViewById(R.id.cardViewUpdate);
            tvUpdate = itemView.findViewById(R.id.tvUpdate);
        }
    }
}