package com.example.feedbox;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SentimentCommentAdapter extends RecyclerView.Adapter<SentimentCommentAdapter.ViewHolder>
{
    Context context;
    private List<SentimentCommentHelper> mSentimentComment;

    public SentimentCommentAdapter(List<SentimentCommentHelper> SentimentComments, Context context2)
    {
        mSentimentComment = SentimentComments;
        context = context2;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(SentimentCommentAdapter.ViewHolder holder, int position)
    {
        SentimentCommentHelper SentimentCommentHelper = mSentimentComment.get(position);

        holder.tvDescription.setText(SentimentCommentHelper.getDescription());

    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mSentimentComment.size();
    }

    @Override
    public SentimentCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.sentiment_comment_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}

