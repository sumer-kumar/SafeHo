package com.sumer.safeho.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sumer.safeho.R;
import com.sumer.safeho.modelclasses.Notification;
import com.sumer.safeho.modelclasses.User;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewsHolder>  {
    ArrayList<Notification> notiList;
    Context context;

    public NotificationAdapter(ArrayList<Notification> notiList, Context context) {
        this.notiList = notiList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_block,parent,false);
        return new ViewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewsHolder holder, int position) {
        Toast.makeText(context, "hola "+notiList.size(), Toast.LENGTH_SHORT).show();
        Notification noti = notiList.get(position);
        Log.e("bang",""+(noti==null));
        String details = "Name : " + noti.getName()+"\nAge : "+noti.getAge()+"\nGender : "+noti.getGender();
        holder.tvDetails.setText(details);

        String note = noti.getName()+" needs your help ";
        holder.tvNote.setText(note);
        Picasso.get().load(noti.getProfilepic()).placeholder(R.drawable.ic_user).into(holder.imProfile);

        //further to add clicl listners on the buttons maps and call
    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder{
        ImageView imProfile;
        TextView tvDetails,tvNote;
        Button btOnmaps,btCall;
        public ViewsHolder(@NonNull View itemView) {
            super(itemView);
            imProfile = itemView.findViewById(R.id.ivProfile);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvNote = itemView.findViewById(R.id.tvNote);
            btOnmaps = itemView.findViewById(R.id.btOnMaps);
            btCall = itemView.findViewById(R.id.btCall);
        }
    }

}
