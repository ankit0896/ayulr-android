package com.oxygen.micro.ayulr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.BloodDonorViewHolder>  {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<BloodDonor> donorList;


    //getting the context and product list with constructor
    public BloodDonorAdapter(Context mCtx, List<BloodDonor> donorList) {
        this.mCtx = mCtx;
        this.donorList = donorList;



    }

    @Override
    public BloodDonorAdapter.BloodDonorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_donor_list, null);
        return new BloodDonorAdapter.BloodDonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BloodDonorAdapter.BloodDonorViewHolder holder, final int position) {
        //getting the product of the specified position
        //final Contact contact = contactListFiltered.get(position);
        final BloodDonor bloodDonor = donorList.get(position);
            holder.textViewName.setText(bloodDonor.getName());
            holder.textViewEmail.setText(bloodDonor.getEmail());
            holder.textViewContact.setText(bloodDonor.getContact());
            Glide.with(mCtx).load("http://ayulr.com/donor_image/" + donorList.get(position).getImage()).into(holder.imageView);
            holder.btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in= new Intent(mCtx,ViewDetailBloodDonorActivity.class);
                in.putExtra("b_id",bloodDonor.getId());
                mCtx.startActivity(in);
                // ((Activity)mCtx).finish();
            }
        });


    }


    @Override
    public int getItemCount() {
        return donorList.size();
    }



    class BloodDonorViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewEmail, textViewContact;
        ImageView imageView;
        Button btnbook;

        public BloodDonorViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail= itemView.findViewById(R.id.textViewEmail);
            textViewContact = itemView.findViewById(R.id.textViewContact);
            imageView = itemView.findViewById(R.id.imageView);
            btnbook=itemView.findViewById(R.id.book);

        }
    }
}