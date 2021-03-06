package com.oxygen.micro.ayulr.view.paramedical.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.oxygen.micro.ayulr.constant.SharedPrefManagerpara;
import com.oxygen.micro.ayulr.view.paramedical.model.Para;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParaUpdateProfessionalActivity extends AppCompatActivity implements View.OnClickListener {

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
    @BindView(R.id.drcategory)
    EditText etcategory;
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
    String HttpURL1 = Config.BASEURL + "filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    String CategoryHolder, DegreeHolder, ExperienceHolder, WorkinHolder, NatureHolder, ExpertHolder, AlsotreatHolder,
            ValidHolder, HomevisitHolder, EmailHolder, HomevisitfeeHolder, IdHolder;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = Config.BASEURL + "paramedical/update_professional.php";
    String[] listitem2;
    boolean[] checkeditem2;
    ArrayList<Integer> degree = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_update_professional);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ethomevisitfee.setVisibility(View.GONE);
        etvalidfor.setVisibility(View.GONE);
        listitem2 = getResources().getStringArray(R.array.para_degree_array);
        checkeditem2 = new boolean[listitem2.length];
        Para para = SharedPrefManagerpara.getInstance(ParaUpdateProfessionalActivity.this).getUserPara();
        IdHolder = String.valueOf(para.getParaId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(ParaUpdateProfessionalActivity.this)) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(ParaUpdateProfessionalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
        rdgroupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.radio) {
                    ethomevisitfee.setVisibility(View.VISIBLE);
                    etvalidfor.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radio1) {
                    ethomevisitfee.setVisibility(View.GONE);
                    etvalidfor.setVisibility(View.GONE);
                    ethomevisitfee.setText("");
                    etvalidfor.setText("");

                }
            }
        });

        etdegree.setOnClickListener(this);
        buttonprofessional.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==etdegree){
            AlertDialog.Builder MBuilder = new AlertDialog.Builder(ParaUpdateProfessionalActivity.this);
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
            if (NetworkDetactor.isNetworkAvailable(ParaUpdateProfessionalActivity.this)) {
                if (CheckEditText) {
                    UpdateDoctorRegistration(DegreeHolder, ExperienceHolder, WorkinHolder, NatureHolder, ExpertHolder, AlsotreatHolder,
                            HomevisitHolder, HomevisitfeeHolder, ValidHolder, IdHolder, CategoryHolder);

                } else {


                }
            } else {
                Toast.makeText(ParaUpdateProfessionalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
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
        if (DegreeHolder.equals("") || ExperienceHolder.equals("") || ExpertHolder.equals("")) {
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
            CheckEditText = false;

        } else {

            CheckEditText = true;
        }

    }

    public void UpdateDoctorRegistration(String degree, String experience, String workin, String nature, String expertise, String alsotreat,
                                         String home_visit, String home_visit_fee, String every_visit_fee, String id, String category) {


        class UpdateDoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ParaUpdateProfessionalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    Toast.makeText(ParaUpdateProfessionalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ParaUpdateProfessionalActivity.this, ParaDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ParaUpdateProfessionalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
                hashMap.put("id", params[9]);
                hashMap.put("category", params[10]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        UpdateDoctorRegistrationClass updatedoctorRegistrationClass = new UpdateDoctorRegistrationClass();

        updatedoctorRegistrationClass.execute(degree, experience, workin, nature, expertise, alsotreat, home_visit, home_visit_fee, every_visit_fee, id, category);
    }

    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //  progressDialog = ProgressDialog.show(ParaUpdateProfessionalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                //  progressDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                s = httpResponseMsg;
                new GetHttpResponse(ParaUpdateProfessionalActivity.this).execute();
                //Parsing the Stored JSOn String to GetHttpResponse Method.


            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("id", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL1);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
        // SendHttpRequestTask myAsync=new SendHttpRequestTask();
        // myAsync.execute(PreviousListViewClickedItem);
    }



    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        public Context context;

        public GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Bitmap bmp;

            try {
                if (s != null) {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(i);
                            // Storing Student Name, Phone Number, Class into Variables.
                            CategoryHolder = jobj.getString("category");
                            DegreeHolder = jobj.getString("degree");
                            ExperienceHolder = jobj.getString("experience");
                            WorkinHolder = jobj.getString("work_in");
                            NatureHolder = jobj.getString("job_nature");
                            ExpertHolder = jobj.getString("expertise");
                            AlsotreatHolder = jobj.getString("also_treat");
                            HomevisitHolder = jobj.getString("home_visit");
                            HomevisitfeeHolder = jobj.getString("home_visit_fee");
                            ValidHolder = jobj.getString("h_visit_for");

                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Check your connection and try again", Toast.LENGTH_SHORT).show();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            // Setting Student Name, Phone Number, Class into TextView after done all process .
            if (CategoryHolder.equals("null")) {
                etcategory.append("");
            } else {
                etcategory.append(CategoryHolder);
            }

            if (DegreeHolder.equals("null")) {
                etdegree.append("");
            } else {
                etdegree.append(DegreeHolder);
            }
            if (ExperienceHolder.equals("null")) {
                etexperience.append("");
            } else {
                etexperience.append(ExperienceHolder);
            }
            if (NatureHolder.equals("null")) {
                etnature.append("");
            } else {
                etnature.append(NatureHolder);
            }
            if (WorkinHolder.equals("null")) {
                etworkin.append("");
            } else {
                etworkin.append(WorkinHolder);
            }
            if (ExpertHolder.equals("null")) {
                etexpertise.append("");
            } else {
                etexpertise.append(ExpertHolder);
            }
            if (AlsotreatHolder.equals("null")) {
                etalsotreat.append("");
            } else {
                etalsotreat.append(AlsotreatHolder);
            }
            // radio button ki value ko get karne ka code
            if (HomevisitHolder.equalsIgnoreCase("yes")) {
                rdyes.setChecked(true);
            } else if (HomevisitHolder.equalsIgnoreCase("no")) {
                rdno.setChecked(true);
            }
            if (HomevisitfeeHolder.equals("null")) {
                ethomevisitfee.append("");
            } else {
                ethomevisitfee.append(HomevisitfeeHolder);
            }
            if (ValidHolder.equals("null")) {
                etvalidfor.append("");
            } else {
                etvalidfor.append(ValidHolder);
            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

}




