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

public class NursingPatientAdapter extends RecyclerView.Adapter<NursingPatientAdapter.NursingPatientViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<NursingPatient> nursingpatientList;

    //getting the context and product list with constructor
    public NursingPatientAdapter(Context mCtx, List<NursingPatient> nursingpatientList) {
        this.mCtx = mCtx;
        this.nursingpatientList = nursingpatientList;
    }

    @Override
    public NursingPatientAdapter.NursingPatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.patientlist, null);
        return new NursingPatientAdapter.NursingPatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NursingPatientAdapter.NursingPatientViewHolder holder, final int position) {
        //getting the product of the specified position
        final NursingPatient nursingpatient = nursingpatientList.get(position);
        holder.textViewOrder.setText(nursingpatient.getNurOrder());
        holder.textViewName.setText(nursingpatient.getNurName());
        holder.textViewNumber.setText(nursingpatient.getNurNumber());
        holder.textViewAge.setText(nursingpatient.getNurAge());
        holder.textViewStatus.setText(nursingpatient.getNurStatus());
        holder.textViewDate.setText(nursingpatient.getNurDate());
        holder.textViewtime.setText(nursingpatient.getNurTime());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NursingPatient nursingpatient = nursingpatientList.get(position);
                Intent intent=new Intent(mCtx,NursingViewPatientActivity.class);
                intent.putExtra("p_id",nursingpatient.getNurId());
                mCtx.startActivity(intent);
                ((Activity)mCtx).finish();


            }
        });

    }


    @Override
    public int getItemCount() {
        return nursingpatientList.size();
    }



    class NursingPatientViewHolder extends RecyclerView.ViewHolder {

        TextView textViewOrder, textViewDate, textViewName,textViewAge,textViewtime,
                textViewNumber,textViewStatus;
        Button button;


        public NursingPatientViewHolder(View itemView) {
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
