package com.oxygen.micro.ayulr.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oxygen.micro.ayulr.model.Blog;
import com.oxygen.micro.ayulr.R;

import java.util.List;

/**
 * Created by MICRO on 2/22/2018.
 */


public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {



    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Blog> blogList;

    //getting the context and product list with constructor
    public BlogAdapter(Context mCtx, List<Blog> blogList) {
        this.mCtx = mCtx;
        this.blogList = blogList;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_blog, null);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BlogViewHolder holder, int position) {
        //getting the product of the specified position
        final Blog blog = blogList.get(position);

        //binding the data with the viewholder views
        holder.textViewid.setText(blog.getId());
        holder.textViewUserName.setText(blog.getUserName());
        Glide.with(mCtx).load(blogList.get(position).getImage()).into(holder.blogimage);
        holder.textViewDescription.setText(blog.getDescription());

       /* holder.imagecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iDetail = new Intent(mCtx, CommentActivity.class);
                iDetail.putExtra("blog_id",blog.getId());
                mCtx.startActivity(iDetail);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }


    class BlogViewHolder extends RecyclerView.ViewHolder {

        TextView textViewid,textViewUserName, textViewDescription;
        ImageView blogimage,imagecomment;


        public BlogViewHolder(View itemView) {
            super(itemView);
            textViewid = itemView.findViewById(R.id.textblogid);
            textViewUserName = itemView.findViewById(R.id.textusername);
            blogimage = itemView.findViewById(R.id.blogimageView);
            textViewDescription = itemView.findViewById(R.id.dec);
           // imagecomment = itemView.findViewById(R.id.comment);
        }
    }

}