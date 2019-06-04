package com.example.proyectofb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderPost> {


    ArrayList<Post> postList;

    public PostAdapter(){
        this.postList = new ArrayList<>();

    }

    public void AddPost(Post post){
        this.postList.add(post);
        this.notifyDataSetChanged();
    }

    public void clearItems(){
        this.postList.clear();
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolderPost onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_thumbnail_layout,null,false);
        return new ViewHolderPost(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPost viewHolderUserPost, int i) {

        String name = this.postList.get(i).getUserName() + this.postList.get(i).getLastName();
        viewHolderUserPost.textViewUserName.setText(name);
        viewHolderUserPost.textViewPostText.setText(this.postList.get(i).getPostText());

        viewHolderUserPost.imageButtonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        viewHolderUserPost.imageButtonDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.postList.size();
    }

    public class ViewHolderPost extends RecyclerView.ViewHolder  {


        TextView textViewUserName;
        TextView textViewPostText;
        ImageButton imageButtonLike;
        ImageButton imageButtonDislike;
        Button buttonComments;
        public ViewHolderPost(@NonNull View itemView) {
            super(itemView);

            textViewUserName = itemView.findViewById(R.id.textViewPostUserName);
            textViewPostText = itemView.findViewById(R.id.textViewPostText);

            imageButtonLike = itemView.findViewById(R.id.imageButtonLike);
            imageButtonDislike =  itemView.findViewById(R.id.imageButtonDislike);





        }



    }
}
