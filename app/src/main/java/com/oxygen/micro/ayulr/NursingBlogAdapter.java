package com.oxygen.micro.ayulr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NursingBlogAdapter extends RecyclerView.Adapter<NursingBlogAdapter.NursingBlogViewHolder> {



    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<NursingBlog> nursingblogList;

    //getting the context and product list with constructor
    public NursingBlogAdapter(Context mCtx, List<NursingBlog> nursingblogList) {
        this.mCtx = mCtx;
        this.nursingblogList = nursingblogList;
    }

    @Override
    public NursingBlogAdapter.NursingBlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_blog, null);
        return new NursingBlogAdapter.NursingBlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NursingBlogAdapter.NursingBlogViewHolder holder, int position) {
        //getting the product of the specified position
        final NursingBlog nursingblog = nursingblogList.get(position);

        //binding the data with the viewholder views
        holder.textViewid.setText(nursingblog.getNurId());
        holder.textViewUserName.setText(nursingblog.getNurUserName());
        Glide.with(mCtx).load(nursingblogList.get(position).getNurImage()).into(holder.blogimage);
        holder.textViewDescription.setText(nursingblog.getNurDescription());

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
        return nursingblogList.size();
    }


    class NursingBlogViewHolder extends RecyclerView.ViewHolder {

        TextView textViewid,textViewUserName, textViewDescription;
        ImageView blogimage,imagecomment;


        public NursingBlogViewHolder(View itemView) {
            super(itemView);
            textViewid = itemView.findViewById(R.id.textblogid);
            textViewUserName = itemView.findViewById(R.id.textusername);
            blogimage = itemView.findViewById(R.id.blogimageView);
            textViewDescription = itemView.findViewById(R.id.dec);
        }
    }

}