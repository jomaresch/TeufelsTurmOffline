package com.dex.teufelsturmoffline.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dex.teufelsturmoffline.R;
import com.dex.teufelsturmoffline.model.Comment;
import com.dex.teufelsturmoffline.model.ModelHelper;

import java.util.List;

public class CommentRecycleAdapter extends RecyclerView.Adapter<CommentRecycleAdapter.CommentRecycleViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Comment item);
    }

    private List<Comment> commentList;
    private OnItemClickListener listener;
    private Context context;

    public CommentRecycleAdapter(Context context, List<Comment> commentList, OnItemClickListener listener) {
        this.commentList = commentList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_comment_list, viewGroup, false);
        return new CommentRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentRecycleViewHolder viewHolder, int i) {
        viewHolder.userName.setText(commentList.get(i).getName());
        viewHolder.date.setText(commentList.get(i).getDate());
        viewHolder.comment.setText(commentList.get(i).getComment());
        viewHolder.imageRating.setImageResource(ModelHelper.convertRatingToImage(commentList.get(i).getRating()));
        viewHolder.bind(commentList.get(i),listener);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentRecycleViewHolder extends RecyclerView.ViewHolder {

        TextView userName, date, comment;
        ImageView imageRating;

        public CommentRecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.text_comment_row_user_name);
            date = itemView.findViewById(R.id.text_comment_row_date);
            comment = itemView.findViewById(R.id.text_comment_row_comment);
            imageRating = itemView.findViewById(R.id.image_comment_row_rating);
        }

        public void bind(final Comment item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

}
