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

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder>
{
    Context context;
    private List<FeedbackHelper> mFeedback;

    public FeedbackAdapter(List<FeedbackHelper> Feedbacks, Context context2)
    {
        mFeedback = Feedbacks;
        context = context2;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(FeedbackAdapter.ViewHolder holder, int position)
    {
        FeedbackHelper FeedbackHelper = mFeedback.get(position);

        if(FeedbackHelper.getSentiment().equals("Like"))
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

//        holder.tvFullName.setText(FeedbackHelper.getFullName());
        holder.tvDetails.setText(FeedbackHelper.getDetails());
        holder.tvSubDetails.setText(FeedbackHelper.getSubDetails());
        holder.tvReasons.setText(FeedbackHelper.getReasons());
        holder.tvfBID.setText(FeedbackHelper.getFeedBackID());

        holder.tvFirstName.setText(FeedbackHelper.getFirstName());
        holder.tvLastName.setText(FeedbackHelper.getLastName());
        holder.tvStatus.setText(FeedbackHelper.getStatus());
        holder.tvCategoryName.setText(FeedbackHelper.getCategoryName());
        holder.tvSubCategoryName.setText(FeedbackHelper.getSubCategoryName());
        holder.tvDescription.setText(FeedbackHelper.getDescription());
        holder.tvDatePosted.setText(FeedbackHelper.getDatePosted());
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mFeedback.size();
    }

    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.feedback_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvFullName, tvFirstName, tvLastName, tvStatus, tvCategoryName, tvSubCategoryName, tvDescription, tvDatePosted,
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
        }
    }
}

