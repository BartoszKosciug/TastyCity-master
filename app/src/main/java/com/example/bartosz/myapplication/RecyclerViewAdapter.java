package com.example.bartosz.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bartosz.myapplication.R;

import java.lang.reflect.Constructor;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    RequestOptions options;
    private Context mContext;
    private List<Place> mData;

    public RecyclerViewAdapter(Context mContext, List<Place> mData){
        this.mContext = mContext;
        this.mData = mData;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_error_black_24dp)
                .error(R.drawable.ic_error_black_24dp);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.place_row_item,viewGroup, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_containter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(mContext, PlaceActivity.class);
                j.putExtra("place_id",mData.get(viewHolder.getAdapterPosition()).getPlace_id());
                j.putExtra("name", mData.get(viewHolder.getAdapterPosition()).getName());
                j.putExtra("distance", mData.get(viewHolder.getAdapterPosition()).getDistane());
                j.putExtra("open", mData.get(viewHolder.getAdapterPosition()).getOpen());
                j.putExtra("rating", mData.get(viewHolder.getAdapterPosition()).getRating());
                j.putExtra("address", mData.get(viewHolder.getAdapterPosition()).getAddress());
                j.putExtra("uri", mData.get(viewHolder.getAdapterPosition()).getUri());
                mContext.startActivity(j);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.place_name.setText(mData.get(i).getName());
        myViewHolder.place_address.setText(mData.get(i).getAddress());
        myViewHolder.place_rating.setText(mData.get(i).getRating());
        myViewHolder.place_open.setText(mData.get(i).getOpen());
        myViewHolder.place_distance.setText(mData.get(i).getDistane());

        Glide.with(mContext).load(mData.get(i).getUri()).apply(options).into(myViewHolder.place_image);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView place_name;
        TextView place_rating;
        TextView place_address;
        TextView place_open;
        TextView place_distance;
        ImageView place_image;
        LinearLayout view_containter;

        public MyViewHolder(View itemView){
            super(itemView);

            view_containter = itemView.findViewById(R.id.container);
            place_name = itemView.findViewById(R.id.name);
            place_rating = itemView.findViewById(R.id.rating);
            place_address = itemView.findViewById(R.id.address);
            place_open = itemView.findViewById(R.id.open);
            place_distance = itemView.findViewById(R.id.distance);
            place_image = itemView.findViewById(R.id.image);
        }
    }
}
