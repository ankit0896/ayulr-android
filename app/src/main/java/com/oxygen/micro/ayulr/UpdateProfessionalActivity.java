package com.oxygen.micro.ayulr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.IdRes;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdateProfessionalActivity extends AppCompatActivity implements View.OnClickListener {
    String HttpURL1 = Config.BASEURL+"filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    @BindView(R.id.drcategory)
    TextInputEditText etcategory;
    @BindView(R.id.drdegree)
    TextInputEditText etdegree;
    @BindView(R.id.drrgnumber)
    TextInputEditText etrgnumber;
    @BindView(R.id.drspeciality)
    TextInputEditText etspeciality;
    @BindView(R.id.drotherespeciality)
    TextInputEditText etotherespeciality;
    @BindView(R.id.drspecialtreat)
    TextInputEditText etdisease;
    @BindView(R.id.drotherediseases)
    TextInputEditText etotherediseases;
    @BindView(R.id.drwrkhosptl)
    TextInputEditText etworkhospital;
    @BindView(R.id.drexperience)
    TextInputEditText etexperience;
    @BindView(R.id.drspecialfee)
    TextInputEditText etspecialfee;
    @BindView(R.id.drspecialvalidfor)
    TextInputEditText etspecialvalidfor;
    @BindView(R.id.drsplhealthpkg)
    TextInputEditText etspecialhealthpkg;
    @BindView(R.id.drsplhealthpkgrs)
    TextInputEditText etspecialhealthday;
    @BindView(R.id.drhomevisitfee)
    TextInputEditText ethomevisitfee;
    @BindView(R.id.dreveryvisit)
    TextInputEditText eteveryvisit;
    @BindView(R.id.btnprofessional)
    Button buttonprofessional;
    @BindView(R.id.radiogrp)
    RadioGroup rdgroupe;
    @BindView(R.id.radio)
    RadioButton rdyes;
    @BindView(R.id.radio1)
    RadioButton rdno;
    String IdHolder,CategoryHolder, DegreeHolder,RgnumberHolder,SpecialityHolder,OthereSpecialityHolder,DiseaseHolder,OthereDiseasesHolder,
            WorkHospitalHolder,ExperienceHolder,SpecialfeeHolder,SpecialValidforHolder,SpecialhealthpkgHolder,SpecialhealthpkgdayHolder,
            EveryvisitHolder,HomevisitHolder,HomevisitfeeHolder;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = Config.BASEURL+"update_professional.php";
    String[] listitem2;
    boolean[] checkeditem2;
    ArrayList<Integer> degree = new ArrayList<>();
    String[] listitem3;
    boolean[] checkeditem3;
    ArrayList<Integer> speciality = new ArrayList<>();
    String local= String.valueOf(-1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_professional);
        initview();
    }
    private void initview(){
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonprofessional.setOnClickListener(this);
        ethomevisitfee.setVisibility(View.GONE);
        eteveryvisit.setVisibility(View.GONE);
        listitem2 = getResources().getStringArray(R.array.degree_array);
        checkeditem2 = new boolean[listitem2.length];
        listitem3 = getResources().getStringArray(R.array.speciality_array);
        checkeditem3 = new boolean[listitem3.length];
        User user = SharedPrefManager.getInstance(UpdateProfessionalActivity.this).getUser();
        IdHolder = String.valueOf(user.getId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(UpdateProfessionalActivity.this)) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(UpdateProfessionalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
        rdgroupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId==R.id.radio){
                    ethomevisitfee.setVisibility(View.VISIBLE);
                    eteveryvisit.setVisibility(View.VISIBLE);
                }else if (checkedId==R.id.radio1){
                    ethomevisitfee.setVisibility(View.GONE);
                    eteveryvisit.setVisibility(View.GONE);
                    ethomevisitfee.setText("");
                    eteveryvisit.setText("");

                }
            }
        });
        etspeciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder MBuilder = new AlertDialog.Builder(UpdateProfessionalActivity.this);
                MBuilder.setTitle(R.string.dailog_title3);
                // String local=  SpecialityHolder.replace(",","");
                // if (speciality.containsAll(Collections.singleton(local))) {
                // Toast.makeText(UpdateProfessionalActivity.this, "" + speciality.indexOf(local), Toast.LENGTH_SHORT).show();
                //  }
                MBuilder.setMultiChoiceItems(listitem3, checkeditem3, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!speciality.contains(position)) {
                                speciality.add(position);
                            }
                        } else if (speciality.contains(position)) {
                            speciality.remove(position);

                        }
                    }
                });
                MBuilder.setCancelable(false);
                MBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < speciality.size(); i++) {
                            item = item + listitem3[speciality.get(i)];
                            if (i != speciality.size() - 1) ;
                            {
                                item = item + ",";
                            }
                        }
                        etspeciality.setText(item);
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
                        for (int i = 0; i < checkeditem3.length; i++) {
                            checkeditem3[i] = false;
                            speciality.clear();
                            etspeciality.setText("");
                        }
                    }
                });
                AlertDialog mdialog = MBuilder.create();
                mdialog.show();
            }
        });

        etdegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder MBuilder = new AlertDialog.Builder(UpdateProfessionalActivity.this);
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
        });

    }
    public void CheckEditTextIsEmptyOrNot() {
        DegreeHolder = etdegree.getText().toString();
        RgnumberHolder = etrgnumber.getText().toString();
        SpecialityHolder = etspeciality.getText().toString();
        OthereSpecialityHolder = etotherespeciality.getText().toString();
        DiseaseHolder = etdisease.getText().toString();
        OthereDiseasesHolder = etotherediseases.getText().toString();
        WorkHospitalHolder =etworkhospital.getText().toString();
        ExperienceHolder = etexperience.getText().toString();
        SpecialfeeHolder = etspecialfee.getText().toString();
        SpecialValidforHolder = etspecialvalidfor.getText().toString();
        SpecialhealthpkgHolder = etspecialhealthpkg.getText().toString();
        SpecialhealthpkgdayHolder = etspecialhealthday.getText().toString();
        HomevisitfeeHolder = ethomevisitfee.getText().toString();
        EveryvisitHolder = eteveryvisit.getText().toString();
        HomevisitHolder = "";
        if (rdyes.isChecked())
            HomevisitHolder = "yes";
        if (rdno.isChecked())
            HomevisitHolder = "no";
        if (DegreeHolder.equals("")||SpecialityHolder.equals("")||ExperienceHolder.equals("")||RgnumberHolder.equals("")) {
            if (DegreeHolder.equals("")) {
                etdegree.requestFocus();
                etdegree.setError("field required");
            }
            if (SpecialityHolder.equals("")) {
                etspeciality.requestFocus();
                etspeciality.setError("field required");
            }
            if (ExperienceHolder.equals("")) {
                etexperience.requestFocus();
                etexperience.setError("field required");
            }
            if (RgnumberHolder.equals("")) {
                etdegree.requestFocus();
                etdegree.setError("field required");
            }
            CheckEditText = false;

        } else {

            CheckEditText = true;
        }

    }
    public void UpdateDoctorRegistration(String degree, String reg_no,String speciality, String other_speciality,String disease,String other_disease,String work_in,
                                   String c_exp,String spl_fee,String spl_days,String spl_package, String package_valid_days, String home_visit, String home_visit_fee,String every_visit_fee,String id) {


        class UpdateDoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdateProfessionalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    Toast.makeText(UpdateProfessionalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UpdateProfessionalActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(UpdateProfessionalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("degree", params[0]);
                hashMap.put("reg_no", params[1]);
                hashMap.put("speciality", params[2]);
                hashMap.put("other_speciality", params[3]);
                hashMap.put("disease", params[4]);
                hashMap.put("other_disease", params[5]);
                hashMap.put("work_in", params[6]);
                hashMap.put("c_exp", params[7]);
                hashMap.put("spl_fee", params[8]);
                hashMap.put("spl_days", params[9]);
                hashMap.put("spl_package", params[10]);
                hashMap.put("package_valid_days", params[11]);
                hashMap.put("home_visit", params[12]);
                hashMap.put("home_visit_fee", params[13]);
                hashMap.put("every_visit_fee", params[14]);
                hashMap.put("id", params[15]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        UpdateDoctorRegistrationClass updatedoctorRegistrationClass = new UpdateDoctorRegistrationClass();

        updatedoctorRegistrationClass.execute(degree,reg_no,speciality,other_speciality,disease,other_disease,work_in,c_exp,
                spl_fee,spl_days,spl_package,package_valid_days,home_visit,home_visit_fee,every_visit_fee,id);
    }
    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

               // progressDialog = ProgressDialog.show(UpdateProfessionalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                s = httpResponseMsg;
                new GetHttpResponse(UpdateProfessionalActivity.this).execute();
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

    }

    @Override
    public void onClick(View v) {
        if (v==buttonprofessional){
            CheckEditTextIsEmptyOrNot();
            if (NetworkDetactor.isNetworkAvailable(UpdateProfessionalActivity.this)) {
                if (CheckEditText) {
                    UpdateDoctorRegistration(DegreeHolder,RgnumberHolder,SpecialityHolder,OthereSpecialityHolder,DiseaseHolder,OthereDiseasesHolder,WorkHospitalHolder,
                            ExperienceHolder,SpecialfeeHolder,SpecialValidforHolder,SpecialhealthpkgHolder,SpecialhealthpkgdayHolder,HomevisitHolder,HomevisitfeeHolder,EveryvisitHolder,IdHolder);

                } else {

                }
            } else {
                Toast.makeText(UpdateProfessionalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }

        }

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
                            RgnumberHolder= jobj.getString("reg_no");
                            SpecialityHolder = jobj.getString("speciality");
                            OthereSpecialityHolder= jobj.getString("other_speciality");
                            DiseaseHolder = jobj.getString("diseases");
                            OthereDiseasesHolder = jobj.getString("other_dise");
                            WorkHospitalHolder= jobj.getString("work_in");
                            ExperienceHolder = jobj.getString("clinic_exper");
                            SpecialfeeHolder= jobj.getString("special_fee");
                            SpecialValidforHolder = jobj.getString("svalid_for");
                            SpecialhealthpkgHolder= jobj.getString("special_package");
                            SpecialhealthpkgdayHolder = jobj.getString("valid_for");
                            HomevisitHolder= jobj.getString("home_visit");
                            HomevisitfeeHolder = jobj.getString("home_visit_fee");
                            EveryvisitHolder = jobj.getString("h_visit_for");

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
            //boolean isStringArrayC = Arrays.asList(speciality).contains(SpecialityHolder);
           // Toast.makeText(context, ""+isStringArrayC, Toast.LENGTH_SHORT).show();
           // String item="";
           // int pos = -1;
           // pos= speciality.indexOf(SpecialityHolder);
           // Toast.makeText(context, ""+pos, Toast.LENGTH_SHORT).show();
            String arr[]=  SpecialityHolder.split(",");
           // List acct_Rte_Cdes_A = Arrays.asList(arr) ;
           // ArrayList<String> listofstring=new ArrayList<String>(acct_Rte_Cdes_A);
           // for (String blist:speciality){
           // if (listofstring.contains(blist)) {
               // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
           // }else{
               // Toast.makeText(context, "not exist", Toast.LENGTH_SHORT).show();
          //  }
           // for ( int j = 0; j < arr.length; j++) {
                // item = item + arr[j];
                //checkeditem3[j] = true;
         //   }
           //

           // if (speciality.contains(acct_Rte_Cdes_A)){
               // Toast.makeText(context, ""+acct_Rte_Cdes_A, Toast.LENGTH_SHORT).show();
            // checkeditem3[]=true;
           // }

           // for ( int j = 0; j < speciality.size(); j++) {
                // item = item + arr[j];
               // checkeditem3[j] = true;
          //  }
                  //  }
                   // etspeciality.setText(item);
            // Setting Student Name, Phone Number, Class into TextView after done all process .
            if (CategoryHolder.equals("null")){
                etcategory.append("");
            }else {
                etcategory.append(CategoryHolder);
            }

            if (DegreeHolder.equals("null")){
                etdegree.append("");
            }else {
                etdegree.append(DegreeHolder);
            }
            if (RgnumberHolder.equals("null")){
                etrgnumber.append("");
            }else {
                etrgnumber.append(RgnumberHolder);
            }
            if (SpecialityHolder.equals("null")){
                etspeciality.append("");
            }else{
                etspeciality.append(SpecialityHolder);
            }
        if (OthereSpecialityHolder.equals("null")){
            etotherespeciality.append("");
        }else {
            etotherespeciality.append(OthereSpecialityHolder);
        }if (DiseaseHolder.equals("null"))
            {
               etdisease.append("");
            }else{
                etdisease.append(DiseaseHolder);
            }if (OthereDiseasesHolder.equals("null")){
                etotherediseases.append("");
            }else {
                etotherediseases.append(OthereDiseasesHolder);
            }if (WorkHospitalHolder.equals("null")){
                etworkhospital.append("");
            }else {
                etworkhospital.append(WorkHospitalHolder);
            }if (ExperienceHolder.equals("null")){
                etexperience.append("");
            }else {
                etexperience.append(ExperienceHolder);
            }
            if (SpecialfeeHolder.equals("null")){
                etspecialfee.append("");
            }else {
                etspecialfee.append(SpecialfeeHolder);
            }if (SpecialValidforHolder.equals("null")){
                etspecialvalidfor.append("");
            }else {
                etspecialvalidfor.append(SpecialValidforHolder);
            }
            if (SpecialhealthpkgHolder.equals("null")){
                etspecialhealthpkg.append("");
            }else {
                etspecialhealthpkg.append(SpecialhealthpkgHolder);

            }
            if (SpecialhealthpkgdayHolder.equals("null")){
                etspecialhealthday.append("");
            }else {
                etspecialhealthday.append(SpecialhealthpkgdayHolder);
            }
            // radio button ki value ko get karne ka code
            if (HomevisitHolder.equalsIgnoreCase("yes")) {
                rdyes.setChecked(true);
            } else if (HomevisitHolder.equalsIgnoreCase("no")) {
                rdno.setChecked(true);
            }
            if (HomevisitfeeHolder.equals("null")){
                ethomevisitfee.append("");
            }else {
                ethomevisitfee.append(HomevisitfeeHolder);
            }
            if (EveryvisitHolder.equals("null")){
                eteveryvisit.append("");
            }else {
                eteveryvisit.append(EveryvisitHolder);
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




