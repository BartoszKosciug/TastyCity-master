package com.example.bartosz.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.MyViewHolder> {

    RequestOptions options;
    private Context mContext;
    private List<Review> mData;

    public ReviewRecyclerViewAdapter(Context mContext, List<Review> mData){
        this.mContext = mContext;
        this.mData = mData;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_error_black_24dp)
                .error(R.drawable.ic_error_black_24dp);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.review_row_item,viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.author.setText(mData.get(i).getAuthor_name());
        myViewHolder.text.setText(mData.get(i).getText());
        myViewHolder.howlongago.setText(mData.get(i).getRelative_time_description());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView author;
        TextView text;
        TextView howlongago;

        public MyViewHolder(View itemView){
            super(itemView);

            author = itemView.findViewById(R.id.author);
            text = itemView.findViewById(R.id.text_field);
            howlongago = itemView.findViewById(R.id.howlongago);
        }
    }
}
