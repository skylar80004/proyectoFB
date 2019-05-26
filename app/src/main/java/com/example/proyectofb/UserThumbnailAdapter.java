package com.example.proyectofb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectofb.ui.main.UserThumbnail;

import java.util.ArrayList;

public class UserThumbnailAdapter extends RecyclerView.Adapter<UserThumbnailAdapter.ViewHolderUserThumbnail> {


    ArrayList<UserThumbnail> userList;

    public UserThumbnailAdapter(ArrayList<UserThumbnail> userList){
        this.userList = userList;

    }

    @NonNull
    @Override
    public ViewHolderUserThumbnail onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_thumbnail_layout,null,false);
        return new ViewHolderUserThumbnail(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUserThumbnail viewHolderUserThumbnail, int i) {

        viewHolderUserThumbnail.textViewName.setText(this.userList.get(i).getName());
        viewHolderUserThumbnail.textViewLastName.setText(this.userList.get(i).getLastName());
        viewHolderUserThumbnail.imageViewPhotoThumbnail.setImageBitmap(this.userList.get(i).getBitmap());

    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    public class ViewHolderUserThumbnail extends RecyclerView.ViewHolder{


         TextView textViewName, textViewLastName;
         ImageView imageViewPhotoThumbnail;

        public ViewHolderUserThumbnail(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewUserThumbName);
            textViewLastName = itemView.findViewById(R.id.textViewUserThumbLastName);
            imageViewPhotoThumbnail = itemView.findViewById(R.id.imageViewUserThumbnailPhoto);

        }
    }
}
