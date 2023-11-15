package com.example.feedbox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateReportAdapter extends RecyclerView.Adapter<GenerateReportAdapter.ViewHolder> {

    Context context;
    private List<GenerateReportHelper> genRepList;
    String PickedStatus;

    public GenerateReportAdapter(List<GenerateReportHelper>  GenRepList, Context context2){
        genRepList = GenRepList;
        context = context2;
    }
    @Override
    public int getItemCount() {
        return genRepList.size();
    }

    @Override
    public GenerateReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.generate_report_item_layout, parent, false);

        GenerateReportAdapter.ViewHolder viewHolder = new GenerateReportAdapter.ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GenerateReportAdapter.ViewHolder holder, int position) {
        GenerateReportHelper generateReportHelper = genRepList.get(position);

        holder.tvAY_Range.setText(generateReportHelper.getAY_Range());
        holder.tvAY_Status.setText(generateReportHelper.getAY_Status());

        holder.linearLayoutAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        ((Activity)context).finish();
                        String uri = Uri.parse(URLDatabase.URL_REPORT)
                                .buildUpon()
                                .appendQueryParameter("ay_range", generateReportHelper.getAY_Range())
                                .build().toString();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                        Intent intent= new Intent(context, GenerateReportActivity.class);
                        ((Activity)context).startActivity(browserIntent);
//                        Uri authUri = Uri.parse(URLDatabase.LINK_PHP)
//                            .buildUpon()
//                            .appendQueryParameter("ay_range", generateReportHelper.getAY_Range())
//                            .build();
                    }
                });
                linearLayoutEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(context);

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setContentView(R.layout.generate_report_add_layout);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();

                        wlp.gravity = Gravity.BOTTOM;

                        window.setAttributes(wlp);

                        CardView cardViewSubmit;

                        Spinner spinner = dialog.findViewById(R.id.spnActAY);
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                                context,
                                R.array.listAYActive,
                                android.R.layout.simple_spinner_item
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // On selecting a spinner item
                                PickedStatus = parent.getItemAtPosition(position).toString();

                                // Showing selected spinner item
                                Toast.makeText(parent.getContext(), "Selected: " + PickedStatus, Toast.LENGTH_LONG).show();
                            }

                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });

                        setSpinText(spinner, generateReportHelper.getAY_Status());

//                        spinner.setSelection(position);

                        EditText etAY_range;

                        cardViewSubmit = dialog.findViewById(R.id.cardViewSubmitAY);

                        etAY_range = dialog.findViewById(R.id.txtAY_Add);

                        etAY_range.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if(etAY_range.getText().toString().isEmpty())
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

                        etAY_range.setText(generateReportHelper.getAY_Range());

                        cardViewSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String url = URLDatabase.URL_AY_EDIT;

                                RequestQueue queue = Volley.newRequestQueue(context);

                                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        dialog.dismiss();
                                        ((Activity)context).finish();
                                        Intent intent= new Intent(context, GenerateReportActivity.class);
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
                                        params.put("ay_id", String.valueOf(generateReportHelper.getAY_ID()));
                                        params.put("ay_status", PickedStatus);
                                        params.put("ay_range", etAY_range.getText().toString());
//                                        params.put("sc_name", ListSpecificHelper.getSc_Name());
//                                        params.put("cat_name", ListSpecificHelper.getCat_Name());
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
                                String url = URLDatabase.URL_AY_DELETE;
                                RequestQueue queue = Volley.newRequestQueue(context);
                                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        dialog.dismiss();

                                        genRepList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeRemoved(position, genRepList.size());
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
                                        params.put("ay_id", String.valueOf(generateReportHelper.getAY_ID()));
                                        params.put("ay_range", generateReportHelper.getAY_Range());
                                        params.put("ay_status", generateReportHelper.getAY_Status());
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
    public void setSpinText(Spinner spin, String text) {
        for(int i = 0; i < spin.getAdapter().getCount(); i++) {
            if(spin.getAdapter().getItem(i).toString().equals(text)) {
                spin.setSelection(i);
//                position = i;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAY_Range, tvAY_Status;
        LinearLayout linearLayoutAY;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayoutAY = itemView.findViewById(R.id.linearLayoutAY);
            tvAY_Range = itemView.findViewById(R.id.tvAY_Range);
            tvAY_Status = itemView.findViewById(R.id.tvAY_Status);
        }
    }
}
