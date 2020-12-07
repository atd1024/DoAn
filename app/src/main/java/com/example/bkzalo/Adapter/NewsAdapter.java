package com.example.bkzalo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bkzalo.Model.News;
import com.example.bkzalo.R;
import com.example.bkzalo.WebActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>  {
    private String title;
    private String link;
    private String image;
    private Context context;
    private List<News> newsList;
    //constructor
    public NewsAdapter(Context context, String title, String link,String image,List<News> newsList) {
        this.context = context;
        this.title = title;
        this.link = link;
        this.image = image;
        this.newsList = newsList;
    }
    @NonNull
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =  inflater.inflate(R.layout.news_item,parent,false);


        return new  NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsAdapter.ViewHolder holder, final int position) {
        News p = newsList.get(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            holder.textViewTitle.setText(p.title);
            Picasso.with(context).load(p.image).into(holder.imageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("link",newsList.get(position).link);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textviewtitle);
            imageView = itemView.findViewById(R.id.imageView);
        }

    }
}
