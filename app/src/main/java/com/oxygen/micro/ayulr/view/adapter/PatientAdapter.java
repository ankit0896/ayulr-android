package com.oxygen.micro.ayulr.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.oxygen.micro.ayulr.model.Patient;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.view.activity.ViewPatientActivity;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Patient> patientList;

    //getting the context and product list with constructor
    public PatientAdapter(Context mCtx, List<Patient> patientList) {
        this.mCtx = mCtx;
        this.patientList = patientList;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.patientlist, null);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, final int position) {
        //getting the product of the specified position
        final Patient patient = patientList.get(position);
        holder.textViewOrder.setText(patient.getOrder());
        holder.textViewName.setText(patient.getName());
        holder.textViewNumber.setText(patient.getNumber());
        holder.textViewAge.setText(patient.getAge());
        holder.textViewStatus.setText(patient.getStatus());
        holder.textViewDate.setText(patient.getDate());
        holder.textViewtime.setText(patient.getTime());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient patient = patientList.get(position);
                Intent intent=new Intent(mCtx, ViewPatientActivity.class);
                intent.putExtra("p_id",patient.getId());
                mCtx.startActivity(intent);
                ((Activity)mCtx).finish();


            }
        });

    }


    @Override
    public int getItemCount() {
        return patientList.size();
    }



    class PatientViewHolder extends RecyclerView.ViewHolder {

        TextView textViewOrder, textViewDate, textViewName,textViewAge,textViewtime,
                textViewNumber,textViewStatus;
Button button;


        public PatientViewHolder(View itemView) {
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
