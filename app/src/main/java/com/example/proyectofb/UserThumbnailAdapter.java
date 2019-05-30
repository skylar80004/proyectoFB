package com.example.proyectofb;

import android.content.Intent;
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

    public UserThumbnailAdapter(){
        this.userList = new ArrayList<>();

    }

    public void AddUserThumbnail(UserThumbnail user){
        this.userList.add(user);
        this.notifyDataSetChanged();
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

        final String id = this.userList.get(i).getId();

        viewHolderUserThumbnail.imageViewPhotoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                intent.putExtra("userID",id );
                v.getContext().startActivity(intent);
            }
        });

        viewHolderUserThumbnail.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                intent.putExtra("userID",id );
                v.getContext().startActivity(intent);

            }
        });

        viewHolderUserThumbnail.textViewLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                intent.putExtra("userID",id );
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    public class ViewHolderUserThumbnail extends RecyclerView.ViewHolder  {


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
