package com.example.proyectofb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolderNotifications> {



    private ArrayList<Notifications> notificationsList;

    public NotificationsAdapter() {
        this.notificationsList = new ArrayList<>();

    }

    public void AddNotification(Notifications noti){
        this.notificationsList.add(noti);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolderNotifications onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_layout_notifications,viewGroup,false);
        return new ViewHolderNotifications(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolderNotifications viewHolderNotifications, final int i) {

        viewHolderNotifications.textViewUserName.setText(this.notificationsList.get(i).getUserName());
        viewHolderNotifications.textViewAction.setText(this.notificationsList.get(i).getAction());

        final String notificationType = this.notificationsList.get(i).getNotificationType();
        if(notificationType.equals("friendSoli")){

            final int  position = i;
            viewHolderNotifications.buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // Accept friendship
                    Toast.makeText(v.getContext(), "Hola bb", Toast.LENGTH_LONG).show();
                    String userIdOwner = notificationsList.get(position).getUserIdOwner();
                    String userIdFrom = notificationsList.get(position).getUserIdFrom();
                    FireBaseAcceptFriendship(userIdOwner, userIdFrom, v.getContext());
                    notificationsList.remove(position);
                    notifyItemRemoved(position);

                }
            });

            viewHolderNotifications.buttonDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // Decline Friendship

                    String userIdOwner = notificationsList.get(position).getUserIdOwner();
                    String userIdFrom = notificationsList.get(position).getUserIdFrom();
                    FireBaseDeclineFriendship(userIdOwner, userIdFrom, v.getContext());
                    notificationsList.remove(position);
                    notifyItemRemoved(position);

                }
            });
        }
        else{
            viewHolderNotifications.buttonAccept.setVisibility(View.INVISIBLE);
            viewHolderNotifications.buttonDecline.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.notificationsList.size();
    }

    public class ViewHolderNotifications extends RecyclerView.ViewHolder{


        TextView textViewUserName;
        TextView textViewAction;
        Button buttonAccept;
        Button buttonDecline;
        public ViewHolderNotifications(@NonNull View itemView) {
            super(itemView);

            textViewUserName = itemView.findViewById(R.id.textViewNotificationUserName);
            textViewAction = itemView.findViewById(R.id.textViewNotificationAction);
            buttonAccept = itemView.findViewById(R.id.buttonNotificationAccept);
            buttonDecline = itemView.findViewById(R.id.buttonNotificationsDecline);

        }
    }


    public void FireBaseAcceptFriendship(final String userIdOwner, final String userIdFrom, final Context context){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();

        databaseReference.child("notifications")
                .child(userIdOwner).child("friendSoli").child(userIdFrom).removeValue();

        final DatabaseReference databaseReferenceFriends = database.getReference();
        databaseReferenceFriends.child("friends").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String currentUserKey = (String)dataSnapshot.getKey();
                if(currentUserKey.equals(userIdOwner)){

                    Map<String,Object> friendMap = (Map<String, Object>) dataSnapshot.getValue();
                    friendMap.put(userIdFrom, userIdFrom);

                    databaseReferenceFriends.child("friends").child(userIdOwner).setValue(friendMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                        }
                    });

                    Toast.makeText(context, " Se ha aceptado la solicitud de amistad", Toast.LENGTH_LONG).show();


                }
                else if(currentUserKey.equals(userIdFrom)){

                    Map<String,Object> friendMap = (Map<String, Object>) dataSnapshot.getValue();

                    friendMap.put(userIdOwner, userIdOwner);

                    databaseReferenceFriends.child("friends").child(userIdFrom).setValue(friendMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                        }
                    });

                    Toast.makeText(context, " Se ha aceptado la solicitud de amistad", Toast.LENGTH_LONG).show();


                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    public void FireBaseDeclineFriendship(String userIdOwner, String userIdFrom, final Context context){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("notifications")
                .child(userIdOwner).child("friendSoli").child(userIdFrom)
                .setValue("declined").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Se ha denegado la solicitud de amistad", Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
