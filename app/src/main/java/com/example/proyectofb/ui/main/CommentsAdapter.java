package com.example.proyectofb.ui.main;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectofb.Comment;
import com.example.proyectofb.R;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolderComment> {

    ArrayList<Comment> commentList;

    public CommentsAdapter(){
        this.commentList = new ArrayList<>();
    }

    public void addComment(Comment comment){
        this.commentList.add(comment);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderComment onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_layout,viewGroup,false);
        return new ViewHolderComment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComment viewHolderComment, int i) {

        viewHolderComment.textViewUserName.setText(this.commentList.get(i).getUserName());
        viewHolderComment.textViewCommentText.setText(this.commentList.get(i).getCommentText());
    }

    @Override
    public int getItemCount() {
        return this.commentList.size();
    }

    public class ViewHolderComment extends RecyclerView.ViewHolder {

        TextView textViewUserName;
        TextView textViewCommentText;
        public ViewHolderComment(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewCommentUserName);
            textViewCommentText = itemView.findViewById(R.id.textViewCommentLayoutText);
        }
    }


}
