package com.example.workingapp.Activity.board.introduce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workingapp.Activity.board.introduce.data.UserDTO;
import com.example.workingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.Arrays;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private ArrayList<UserDTO> userList;
    Context context;

    public UserAdapter(ArrayList<UserDTO> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_introduce_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        holder.tvNameText.setText(userList.get(position).getTitle());
        holder.tvHobbyText.setText(userList.get(position).getContent());
        holder.tvNameHelloView.setText(userList.get(position).getTitle() + "님");

        final int[] totalCount = {0};

        holder.ivHeartClick.setClickable(true);

        holder.ivHeartClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.tvTotalCount.setText(String.valueOf(totalCount[0]));
                totalCount[0]++;

                holder.ivHeartClick.setClickable(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNameText;
        private TextView tvHobbyText;
        private TextView tvNameHelloView;
        private TextView tvTotalCount;
        private ImageView ivHeartClick;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameText = itemView.findViewById(R.id.tvNameText);
            tvHobbyText = itemView.findViewById(R.id.tvHobbyText);
            tvNameHelloView = itemView.findViewById(R.id.tvNameHelloView);
            tvTotalCount = itemView.findViewById(R.id.tvTotalCount);
            ivHeartClick = itemView.findViewById(R.id.iv_heart_click);
        }
    }


}
