package com.example.feedbox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListSpecificAdapter extends RecyclerView.Adapter<ListSpecificAdapter.ViewHolder> {

    String Spc_d_ID, Spc_d_Name, Sc_Name, Cat_Name;
    Context context;
    String Department;
    private List<ListSpecificHelper> lShelper;

    public ListSpecificAdapter(List<ListSpecificHelper> LShelper, Context context2)
    {
        lShelper = LShelper;
        context = context2;
    }
    @Override
    public void onViewAttachedToWindow(@NonNull ListSpecificAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSpecificAdapter.ViewHolder holder, int position) {
        ListSpecificHelper ListSpecificHelper = lShelper.get(position);

        holder.tvSpc_d_Cat.setText(String.valueOf(ListSpecificHelper.getSpc_d_Cat()).replace("_"," "));
        holder.tvSpd_d_Name.setText(ListSpecificHelper.getSpc_d_Name());

        holder.linearLayoutLS.setOnClickListener(new View.OnClickListener() {
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

                linearLayoutView.setVisibility(View.GONE);
                linearLayoutEdit.setVisibility(View.VISIBLE);
                linearLayoutDelete.setVisibility(View.VISIBLE);

//                linearLayoutView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent intent= new Intent(context, SubCategoryActivity.class);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("CategoryID", CategoryHelper.getCategoryID());
//                        bundle.putString("CategoryName", CategoryHelper.getCategoryName());
//
//                        intent.putExtras(bundle);
//
//                        ((Activity)context).startActivity(intent);
//                    }
//                });

                linearLayoutEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(context);

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setContentView(R.layout.list_specific_add_layout);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();

                        wlp.gravity = Gravity.BOTTOM;

                        window.setAttributes(wlp);

                        CardView cardViewSubmit;

                        Spinner spinner = dialog.findViewById(R.id.spnListSpe);
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                                context,
                                R.array.list_specific,
                                android.R.layout.simple_spinner_item
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        setSpinText(spinner, ListSpecificHelper.getSpc_d_Cat());

                        EditText etComSpe;

                        cardViewSubmit = dialog.findViewById(R.id.cardViewSubmit);

                        etComSpe = dialog.findViewById(R.id.txtComplaintSpe);

                        etComSpe.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if(etComSpe.getText().toString().isEmpty())
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

                        etComSpe.setText(ListSpecificHelper.getSpc_d_Name());

                        cardViewSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String url = URLDatabase.URL_LIST_SPECIFIC_EDIT;

                                RequestQueue queue = Volley.newRequestQueue(context);

                                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        dialog.dismiss();
                                        ((Activity)context).finish();
                                        Intent intent= new Intent(context, ListSpecificActivity.class);
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
                                        params.put("spc_d_id", String.valueOf(ListSpecificHelper.getSpc_d_ID()));
                                        params.put("spc_d_cat", String.valueOf(ListSpecificHelper.getSpc_d_Cat()));
                                        params.put("spc_d_name", etComSpe.getText().toString());
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
                                String url = URLDatabase.URL_LIST_SPECIFIC_DELETE;
                                RequestQueue queue = Volley.newRequestQueue(context);
                                StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        dialog.dismiss();

                                        lShelper.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeRemoved(position, lShelper.size());
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
                                        params.put("spc_d_cat", String.valueOf(ListSpecificHelper.getSpc_d_Cat()));
                                        params.put("spc_d_name", ListSpecificHelper.getSpc_d_Name());
                                        params.put("sc_name", ListSpecificHelper.getSc_Name());
                                        params.put("cat_name", ListSpecificHelper.getCat_Name());
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
    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }
    }


    @Override
    public int getItemCount() {
        return lShelper.size();
    }

    @Override
    public ListSpecificAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.list_specific_item_layout, parent, false);

        ListSpecificAdapter.ViewHolder viewHolder = new ListSpecificAdapter.ViewHolder(contactView);

        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSpd_d_Name, tvSpc_d_Cat;
        LinearLayout linearLayoutLS;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutLS = itemView.findViewById(R.id.linearLayoutLS);
            tvSpd_d_Name = itemView.findViewById(R.id.tvComplaintNameItem);
            tvSpc_d_Cat = itemView.findViewById(R.id.tvSpc_d_Cat);


        }
    }
}
