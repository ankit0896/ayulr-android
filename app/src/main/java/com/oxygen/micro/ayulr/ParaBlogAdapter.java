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

public class ParaBlogAdapter extends RecyclerView.Adapter<ParaBlogAdapter.ParaBlogViewHolder> {



    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<ParaBlog> parablogList;

    //getting the context and product list with constructor
    public ParaBlogAdapter(Context mCtx, List<ParaBlog> parablogList) {
        this.mCtx = mCtx;
        this.parablogList = parablogList;
    }

    @Override
    public ParaBlogAdapter.ParaBlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_blog, null);
        return new ParaBlogAdapter.ParaBlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ParaBlogAdapter.ParaBlogViewHolder holder, int position) {
        //getting the product of the specified position
        final ParaBlog parablog = parablogList.get(position);

        //binding the data with the viewholder views
        holder.textViewid.setText(parablog.getParaId());
        holder.textViewUserName.setText(parablog.getParaUserName());
        Glide.with(mCtx).load(parablogList.get(position).getParaImage()).into(holder.blogimage);
        holder.textViewDescription.setText(parablog.getParaDescription());

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
        return parablogList.size();
    }


    class ParaBlogViewHolder extends RecyclerView.ViewHolder {

        TextView textViewid,textViewUserName, textViewDescription;
        ImageView blogimage,imagecomment;


        public ParaBlogViewHolder(View itemView) {
            super(itemView);
            textViewid = itemView.findViewById(R.id.textblogid);
            textViewUserName = itemView.findViewById(R.id.textusername);
            blogimage = itemView.findViewById(R.id.blogimageView);
            textViewDescription = itemView.findViewById(R.id.dec);
            // imagecomment = itemView.findViewById(R.id.comment);
        }
    }

}