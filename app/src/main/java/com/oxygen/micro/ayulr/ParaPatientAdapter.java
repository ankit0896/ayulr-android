package com.oxygen.micro.ayulr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParaPatientAdapter extends RecyclerView.Adapter<ParaPatientAdapter.ParaPatientViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<ParaPatient> parapatientList;

    //getting the context and product list with constructor
    public ParaPatientAdapter(Context mCtx, List<ParaPatient> parapatientList) {
        this.mCtx = mCtx;
        this.parapatientList = parapatientList;
    }

    @Override
    public ParaPatientAdapter.ParaPatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.patientlist, null);
        return new ParaPatientAdapter.ParaPatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParaPatientAdapter.ParaPatientViewHolder holder, final int position) {
        //getting the product of the specified position
        final ParaPatient parapatient = parapatientList.get(position);
        holder.textViewOrder.setText(parapatient.getParaOrder());
        holder.textViewName.setText(parapatient.getParaName());
        holder.textViewNumber.setText(parapatient.getParaNumber());
        holder.textViewAge.setText(parapatient.getParaAge());
        holder.textViewStatus.setText(parapatient.getParaStatus());
        holder.textViewDate.setText(parapatient.getParaDate());
        holder.textViewtime.setText(parapatient.getParaTime());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParaPatient parapatient = parapatientList.get(position);
                Intent intent=new Intent(mCtx,ParaViewPatientActivity.class);
                intent.putExtra("p_id",parapatient.getParaId());
                mCtx.startActivity(intent);
                ((Activity)mCtx).finish();


            }
        });

    }


    @Override
    public int getItemCount() {
        return parapatientList.size();
    }



    class ParaPatientViewHolder extends RecyclerView.ViewHolder {

        TextView textViewOrder, textViewDate, textViewName,textViewAge,textViewtime,
                textViewNumber,textViewStatus;
        Button button;


        public ParaPatientViewHolder(View itemView) {
            super(itemView);

            textViewOrder = itemView.findViewById(R.id.order);
            textViewDate= itemView.findViewById(R.id.Date);
            textViewName= itemView.findViewById(R.id.name);
            textViewAge = itemView.findViewById(R.id.age);
            textViewStatus = itemView.findViewById(R.id.status);
            textViewtime= itemView.findViewById(R.id.time);
            textViewNumber= itemView.findViewById(R.id.number);
            button= itemView.findViewById(R.id.info);
        }
    }
}
