package com.oxygen.micro.ayulr.view.adapter;

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
import com.oxygen.micro.ayulr.model.EquipmentDonor;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.view.activity.ViewDetailMedicatedEquipmentActivity;

import java.util.List;

public class EquipmentDonorAdapter extends RecyclerView.Adapter<EquipmentDonorAdapter.EquipmentDonorViewHolder>  {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<EquipmentDonor> donorList;


    //getting the context and product list with constructor
    public EquipmentDonorAdapter(Context mCtx, List<EquipmentDonor> donorList) {
        this.mCtx = mCtx;
        this.donorList = donorList;



    }

    @Override
    public EquipmentDonorAdapter.EquipmentDonorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_donor_list, null);
        return new EquipmentDonorAdapter.EquipmentDonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquipmentDonorAdapter.EquipmentDonorViewHolder holder, final int position) {
        //getting the product of the specified position
        //final Contact contact = contactListFiltered.get(position);
        final EquipmentDonor equipmenDonor = donorList.get(position);
        holder.textViewName.setText(equipmenDonor.getName());
        holder.textViewEmail.setText(equipmenDonor.getEmail());
        holder.textViewContact.setText(equipmenDonor.getContact());
        Glide.with(mCtx).load("http://ameygraphics.com/ayulr/images/"+donorList.get(position).getImage()).into(holder.imageView);
        holder.btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in= new Intent(mCtx, ViewDetailMedicatedEquipmentActivity.class);
                in.putExtra("b_id",equipmenDonor.getId());
                mCtx.startActivity(in);
                // ((Activity)mCtx).finish();
            }
        });


    }


    @Override
    public int getItemCount() {
        return donorList.size();
    }



    class EquipmentDonorViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewEmail, textViewContact;
        ImageView imageView;
        Button btnbook;

        public EquipmentDonorViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail= itemView.findViewById(R.id.textViewEmail);
            textViewContact = itemView.findViewById(R.id.textViewContact);
            imageView = itemView.findViewById(R.id.imageView);
            btnbook=itemView.findViewById(R.id.book);

        }
    }
}