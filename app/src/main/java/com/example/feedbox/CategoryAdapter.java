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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    String Department;
    private List<CategoryHelper> mCategory;

    public CategoryAdapter(List<CategoryHelper> Categorys, Context context2)
    {
        mCategory = Categorys;
        context = context2;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position)
    {
        CategoryHelper CategoryHelper = mCategory.get(position);

        holder.tvCategoryName.setText(CategoryHelper.getCategoryName());
        holder.tvDatePosted.setText(CategoryHelper.getDatePosted());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Dialog dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.setting_item_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;

                window.setAttributes(wlp);

                LinearLayout linearLayoutView, linearLayoutEdit, linearLayoutDelete;

                linearLayoutView = dialog.findViewById(R.id.linearLayoutView);
                linearLayoutEdit = dialog.findViewById(R.id.linearLayoutEdit);
                linearLayoutDelete = dialog.findViewById(R.id.linearLayoutDelete);

                linearLayoutView.setVisibility(View.VISIBLE);
                linearLayoutEdit.setVisibility(View.VISIBLE);
                linearLayoutDelete.setVisibility(View.VISIBLE);

                linearLayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent= new Intent(context, SubCategoryActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("CategoryID", CategoryHelper.getCategoryID());
                        bundle.putString("CategoryName", CategoryHelper.getCategoryName());

                        intent.putExtras(bundle);

                        ((Activity)context).startActivity(intent);
                    }
                });

                linearLayoutEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(context);

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setContentView(R.layout.category_add_layout);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();

                        wlp.gravity = Gravity.BOTTOM;

                        window.setAttributes(wlp);

                        CardView cardViewSubmit;

                        EditText txtCategoryName;

                        cardViewSubmit = dialog.findViewById(R.id.cardViewSubmit);

                        txtCategoryName = dialog.findViewById(R.id.txtCategoryName);

                        txtCategoryName.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if(txtCategoryName.getText().toString().isEmpty())
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

                        txtCategoryName.setText(CategoryHelper.getCategoryName());

                        cardViewSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String url = URLDatabase.URL_CATEGORY_EDIT;

                                RequestQueue queue = Volley.newRequestQueue(context);

                                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        dialog.dismiss();
                                        ((Activity)context).finish();
                                        Intent intent= new Intent(context, CategoryActivity.class);
                                        ((Activity)context).startActivity(intent);
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
                                        params.put("category_id", CategoryHelper.getCategoryID());
                                        params.put("category_name", txtCategoryName.getText().toString());
                                        return params;
                                    }
                                };
                                queue.add(request);
                            }
                        });
                        dialog.show();
                    }
                });

                linearLayoutDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(context);

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setContentView(R.layout.delete_confirmation_layout);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();

                        wlp.gravity = Gravity.BOTTOM;

                        window.setAttributes(wlp);

                        CardView cardViewNo, cardViewYes;

                        cardViewNo = dialog.findViewById(R.id.cardViewNo);
                        cardViewYes = dialog.findViewById(R.id.cardViewYes);

                        cardViewNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        cardViewYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String url = URLDatabase.URL_CATEGORY_DELETE;

                                RequestQueue queue = Volley.newRequestQueue(context);

                                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        dialog.dismiss();

                                        mCategory.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeRemoved(position, mCategory.size());
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
                                        params.put("category_id", CategoryHelper.getCategoryID());
                                        return params;
                                    }
                                };
                                queue.add(request);
                            }
                        });
                        dialog.show();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.category_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvDatePosted;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvDatePosted = itemView.findViewById(R.id.tvDatePosted);
        }
    }
}
