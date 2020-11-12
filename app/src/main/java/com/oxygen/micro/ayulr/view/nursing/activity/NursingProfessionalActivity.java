package com.oxygen.micro.ayulr.view.nursing.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NursingProfessionalActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.drdegree)
    EditText etdegree;
    @BindView(R.id.drexperience)
    EditText etexperience;
    @BindView(R.id.drworkin)
    EditText etworkin;
    @BindView(R.id.drnature)
    EditText etnature;
    @BindView(R.id.drexpert)
    EditText etexpertise;
    @BindView(R.id.dralsotreat)
    EditText etalsotreat;
    @BindView(R.id.drhomevisitfee)
    EditText ethomevisitfee;
    @BindView(R.id.drvalidfor)
    EditText etvalidfor;
    @BindView(R.id.btnprofessional)
    Button buttonprofessional;
    @BindView(R.id.radiogrp)
    RadioGroup rdgroupe;
    @BindView(R.id.radio)
    RadioButton rdyes;
    @BindView(R.id.radio1)
    RadioButton rdno;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String DegreeHolder, ExperienceHolder, WorkinHolder, NatureHolder, ExpertHolder, AlsotreatHolder,
            ValidHolder, HomevisitHolder, EmailHolder, HomevisitfeeHolder;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = Config.BASEURL + "nursing/professional.php";
    String[] listitem2;
    boolean[] checkeditem2;
    ArrayList<Integer> degree = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_professional);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("email");
        ethomevisitfee.setVisibility(View.GONE);
        etvalidfor.setVisibility(View.GONE);
        listitem2 = getResources().getStringArray(R.array.para_degree_array);
        checkeditem2 = new boolean[listitem2.length];
        rdgroupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.radio) {
                    ethomevisitfee.setVisibility(View.VISIBLE);
                    etvalidfor.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radio1) {
                    ethomevisitfee.setVisibility(View.GONE);
                    etvalidfor.setVisibility(View.GONE);

                }
            }
        });

        etdegree.setOnClickListener(this);
        buttonprofessional.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==etdegree){
            AlertDialog.Builder MBuilder = new AlertDialog.Builder(NursingProfessionalActivity.this);
            MBuilder.setTitle(R.string.dailog_title2);
            MBuilder.setMultiChoiceItems(listitem2, checkeditem2, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                    if (isChecked) {
                        if (!degree.contains(position)) {
                            degree.add(position);
                        }
                    } else if (degree.contains(position)) {
                        degree.remove(position);

                    }
                }
            });
            MBuilder.setCancelable(false);
            MBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String item = "";
                    for (int i = 0; i < degree.size(); i++) {
                        item = item + listitem2[degree.get(i)];
                        if (i != degree.size() - 1) ;
                        {
                            item = item + ",";
                        }
                    }
                    etdegree.setText(item);
                }
            });
            MBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            MBuilder.setNeutralButton(R.string.clearall_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < checkeditem2.length; i++) {
                        checkeditem2[i] = false;
                        degree.clear();
                        etdegree.setText("");
                    }
                }
            });
            AlertDialog mdialog = MBuilder.create();
            mdialog.show();
        }

        if(v==buttonprofessional){
            CheckEditTextIsEmptyOrNot();
            if (NetworkDetactor.isNetworkAvailable(NursingProfessionalActivity.this)) {
                if (CheckEditText) {
                    DoctorRegistration(DegreeHolder, ExperienceHolder, WorkinHolder, NatureHolder, ExpertHolder, AlsotreatHolder,
                            HomevisitHolder, HomevisitfeeHolder, ValidHolder, EmailHolder);
                } else {
                }
            } else {
                Toast.makeText(NursingProfessionalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void CheckEditTextIsEmptyOrNot() {
        DegreeHolder = etdegree.getText().toString();
        ExperienceHolder = etexperience.getText().toString();
        WorkinHolder = etworkin.getText().toString();
        NatureHolder = etnature.getText().toString();
        ExpertHolder = etexpertise.getText().toString();
        AlsotreatHolder = etalsotreat.getText().toString();
        HomevisitfeeHolder = ethomevisitfee.getText().toString();
        ValidHolder = etvalidfor.getText().toString();
        HomevisitHolder = "";
        if (rdyes.isChecked())
            HomevisitHolder = "yes";
        if (rdno.isChecked())
            HomevisitHolder = "no";
        if (DegreeHolder.equals("") || ExperienceHolder.equals("") || ExpertHolder.equals("") || WorkinHolder.equals("")) {
            if (DegreeHolder.equals("")) {
                etdegree.requestFocus();
                etdegree.setError("field required");
            }
            if (ExperienceHolder.equals("")) {
                etexperience.requestFocus();
                etexperience.setError("field required");
            }
            if (ExpertHolder.equals("")) {
                etexpertise.requestFocus();
                etexpertise.setError("field required");
            }
            if (WorkinHolder.equals("")) {
                etworkin.requestFocus();
                etworkin.setError("field required");
            }
            CheckEditText = false;

        } else {

            CheckEditText = true;
        }

    }

    public void DoctorRegistration(String degree, String experience, String workin, String nature, String expertise, String alsotreat,
                                   String home_visit, String home_visit_fee, String every_visit_fee, String email) {


        class DoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(NursingProfessionalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    //Toast.makeText(ParaProfessionalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NursingProfessionalActivity.this, NursingClinicalActivity.class);
                    intent.putExtra("email", EmailHolder);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(NursingProfessionalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("degree", params[0]);
                hashMap.put("experience", params[1]);
                hashMap.put("work_in", params[2]);
                hashMap.put("nature", params[3]);
                hashMap.put("expertise", params[4]);
                hashMap.put("also_treat", params[5]);
                hashMap.put("home_visit", params[6]);
                hashMap.put("home_visit_fee", params[7]);
                hashMap.put("every_visit_fee", params[8]);
                hashMap.put("email", params[9]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        DoctorRegistrationClass doctorRegistrationClass = new DoctorRegistrationClass();

        doctorRegistrationClass.execute(degree, experience, workin, nature, expertise, alsotreat, home_visit, home_visit_fee, every_visit_fee, email);
    }


}




