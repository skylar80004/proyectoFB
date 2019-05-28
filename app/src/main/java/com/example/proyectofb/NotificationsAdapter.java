package com.example.proyectofb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_layout_notifications,null,false);
        return new ViewHolderNotifications(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolderNotifications viewHolderNotifications, int i) {

        viewHolderNotifications.textViewUserName.setText(this.notificationsList.get(i).getUserName());
        viewHolderNotifications.textViewAction.setText(this.notificationsList.get(i).getAction());

        String notificationType = this.notificationsList.get(i).getNotificationType();
        if(!notificationType.equals("friendSoli")){

            viewHolderNotifications.buttonAccept.setVisibility(View.INVISIBLE);
            viewHolderNotifications.buttonAccept.setVisibility(View.INVISIBLE);
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
}
