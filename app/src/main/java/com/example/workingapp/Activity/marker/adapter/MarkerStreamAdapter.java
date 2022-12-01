package com.example.workingapp.Activity.marker.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workingapp.Activity.marker.ActivityMarkerActivity;
import com.example.workingapp.Activity.marker.data.MarkerItem;
import com.example.workingapp.Activity.marker.database.OpenMarkerHelper;
import com.example.workingapp.R;

import java.util.ArrayList;

public class MarkerStreamAdapter extends RecyclerView.Adapter<MarkerStreamAdapter.MyStreamHolder> {

    private ArrayList<MarkerItem> mMarkerItems;
    private Context mContext;
    private OpenMarkerHelper mMarkerHelper;

    private static OpenMarkerHelper openMarkerHelper;

    public MarkerStreamAdapter(ArrayList<MarkerItem> mMarkerItems, Context mContext) {
        this.mMarkerItems = mMarkerItems;
        this.mContext = mContext;
        mMarkerHelper = new OpenMarkerHelper(mContext);
    }

    @NonNull
    @Override
    public MarkerStreamAdapter.MyStreamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_stream_item, parent, false);
        return new MyStreamHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkerStreamAdapter.MyStreamHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvTitle.setText(mMarkerItems.get(position).getTitle());
        holder.tvLocation.setText(mMarkerItems.get(position).getSnippet());
        holder.tvLatitude.setText(String.valueOf(mMarkerItems.get(position).getLatitude()));
        holder.tvHardness.setText(String.valueOf(mMarkerItems.get(position).getHardness()));

    }

    @Override
    public int getItemCount() {
        return mMarkerItems.size();
    }

    public class MyStreamHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvLocation, tvLatitude, tvHardness;

        CardView cvClickMe;

        public MyStreamHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLocation = itemView.findViewById(R.id.tv_locaiton);
            tvLatitude = itemView.findViewById(R.id.tv_latitude_view);
            tvHardness = itemView.findViewById(R.id.tv_hardness_view);
            cvClickMe = itemView.findViewById(R.id.cv_click_me);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curPos = getAdapterPosition();
                    MarkerItem markerItem = new MarkerItem();

                    String[] strChoiceItems = { "위치보기", "삭제하기" };

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택해주세요.");

                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if (position == 0) {
                                Context context = view.getContext();

                                Intent intent = new Intent(context, ActivityMarkerActivity.class);
                                intent.putExtra("title", tvTitle.getText().toString());
                                intent.putExtra("snippet", tvLocation.getText().toString());
                                intent.putExtra("latitude", Double.parseDouble(tvLatitude.getText().toString()));
                                intent.putExtra("hardness", Double.parseDouble(tvHardness.getText().toString()));

                                context.startActivity(intent);
                            }

                            else if (position == 1) {
                                String title = markerItem.getTitle();

                                mMarkerHelper.onDelete(title);

                                mMarkerItems.remove(curPos);
                                notifyItemRemoved(curPos);

                            }
                        }
                    });

                    builder.show();
                }
            });
        }
    }

    public void addItem(MarkerItem _item) {
        mMarkerItems.add(0, _item);
        notifyItemInserted(0);
    }
}
