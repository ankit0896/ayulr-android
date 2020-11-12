package com.oxygen.micro.ayulr.view.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class ProfessionalActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.category)
    Spinner spinner;
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
    @BindView(R.id.drName)
    TextInputEditText etname;
    @BindView(R.id.dremail)
    TextInputEditText etemail;
    @BindView(R.id.drcontact)
    TextInputEditText etcontact;
    static EditText etdob;
    @BindView(R.id.btnprofessional)
    Button buttonprofessional;
    @BindView(R.id.radiogrp)
    RadioGroup rdgroupe;
    @BindView(R.id.radio)
    RadioButton rdyes;
    @BindView(R.id.radio1)
    RadioButton rdno;
    @BindView(R.id.drimage)
    ImageView drimageView;
    @BindView(R.id.radio3)
    RadioButton rdmale;
    @BindView(R.id.radio4)
    RadioButton rdfemale;
    String DocidHolder,CategoryHolder,DegreeHolder,RgnumberHolder,SpecialityHolder,OthereSpecialityHolder,DiseaseHolder,OthereDiseasesHolder,
    WorkHospitalHolder,ExperienceHolder,SpecialfeeHolder,SpecialValidforHolder,SpecialhealthpkgHolder,SpecialhealthpkgdayHolder,
    EveryvisitHolder,HomevisitHolder,EmailHolder,HomevisitfeeHolder,NameHolder,MobileHolder,ImageHolder,DobHolder,GenderHolder;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = Config.BASEURL+"new_professional.php";
    String[] listitem2;
    boolean[] checkeditem2;
    ArrayList<Integer> degree = new ArrayList<>();
    String[] listitem3;
    boolean[] checkeditem3;
    ArrayList<Integer> speciality = new ArrayList<>();
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    static final String DATE_DIALOG_ID = "datePicker";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional);
        initview();
    }
    private void initview(){
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        NameHolder=intent.getStringExtra("name");
        EmailHolder=intent.getStringExtra("email");
        MobileHolder=intent.getStringExtra("mob");
        DocidHolder=intent.getStringExtra("docid");
        Toast.makeText(this, ""+DocidHolder, Toast.LENGTH_SHORT).show();
        ImageHolder="Noimage.jpeg";
        etname.setText("Dr."+NameHolder);
        etemail.setText(EmailHolder);
        etcontact.setText(MobileHolder);
        String[] Category = {"Select Category","Allopathy","Ayurvedic","Unani","Homeopathy","Siddha","Integrated Medicine"};
        ArrayAdapter adapter = new ArrayAdapter(ProfessionalActivity.this, R.layout.support_simple_spinner_dropdown_item, Category);
        spinner.setAdapter(adapter);
        etdob=findViewById(R.id.drdob);
        ethomevisitfee.setVisibility(View.GONE);
        eteveryvisit.setVisibility(View.GONE);
        etdob.setOnClickListener(this);
        drimageView.setOnClickListener(this);
        buttonprofessional.setOnClickListener(this);
        etspeciality.setOnClickListener(this);
        etdegree.setOnClickListener(this);
        listitem2 = getResources().getStringArray(R.array.degree_array);
        checkeditem2 = new boolean[listitem2.length];
        listitem3 = getResources().getStringArray(R.array.speciality_array);
        checkeditem3 = new boolean[listitem3.length];
        rdgroupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId==R.id.radio){
                    ethomevisitfee.setVisibility(View.VISIBLE);
                    eteveryvisit.setVisibility(View.VISIBLE);
                }else if (checkedId==R.id.radio1){
                    ethomevisitfee.setVisibility(View.GONE);
                    eteveryvisit.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v==etdob){
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
        }
        if (v==drimageView){
            Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryintent.setType("image/*");
            startActivityForResult(galleryintent, Result_Load_Image);
        }
        if (v==buttonprofessional){
            CheckEditTextIsEmptyOrNot();
            if (NetworkDetactor.isNetworkAvailable(ProfessionalActivity.this)) {
                if (CheckEditText) {
                    if (!spinner.getSelectedItem().equals("Select Category")) {
                        DoctorRegistration(NameHolder, DobHolder, GenderHolder, MobileHolder,DegreeHolder, RgnumberHolder, SpecialityHolder, OthereSpecialityHolder, DiseaseHolder, OthereDiseasesHolder, WorkHospitalHolder,
                                ExperienceHolder, SpecialfeeHolder, SpecialValidforHolder, SpecialhealthpkgHolder, SpecialhealthpkgdayHolder, HomevisitHolder, HomevisitfeeHolder, EveryvisitHolder, EmailHolder,CategoryHolder,ImageHolder,DocidHolder);
                    }else{
                        Toast.makeText(ProfessionalActivity.this, "Select Category First", Toast.LENGTH_SHORT).show();
                    }
                } else {


                }
            } else {
                Toast.makeText(ProfessionalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }
        if (v==etspeciality){
            AlertDialog.Builder MBuilder = new AlertDialog.Builder(ProfessionalActivity.this);
            MBuilder.setTitle(R.string.dailog_title3);
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
        if (v==etdegree){
            AlertDialog.Builder MBuilder = new AlertDialog.Builder(ProfessionalActivity.this);
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

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // get selected date
            mYear = year;
            mMonth = month;
            mDay = day;

            // show selected date to date button
            etdob.setText(new StringBuilder()

                    .append(mDay).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mYear).append(""));

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Result_Load_Image &&resultCode ==RESULT_OK && data !=null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                drimageView.setImageBitmap(bitmap);
                ImageHolder = getStringImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStreamObject;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        return ConvertImage;

    }
    public void CheckEditTextIsEmptyOrNot() {
        NameHolder = etname.getText().toString();
        DobHolder = etdob.getText().toString();
        EmailHolder = etemail.getText().toString();
        MobileHolder = etcontact.getText().toString();
        CategoryHolder=spinner.getSelectedItem().toString();
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
        GenderHolder = "";
        if (rdmale.isChecked())
            GenderHolder = "male";
        if (rdfemale.isChecked())
            GenderHolder = "female";
        HomevisitHolder = "";
        if (rdyes.isChecked())
            HomevisitHolder = "yes";
        if (rdno.isChecked())
            HomevisitHolder = "no";
        if(NameHolder.equals("") || DobHolder.equals("")||MobileHolder.equals("") || EmailHolder.equals("") || (!(MobileHolder.length() == 10)) || (!(EmailHolder.matches(emailPattern))) || DegreeHolder.equals("")||SpecialityHolder.equals("")||RgnumberHolder.equals("")||ExperienceHolder.equals("")) {
            if (NameHolder.equals("")) {
                etname.requestFocus();
                etname.setError("field required");
            }
            if (DobHolder.equals("")) {
                etdob.requestFocus();
                etdob.setError("field required");
            }
            if (EmailHolder.equals("")) {
                etemail.requestFocus();
                etemail.setError("field required");
            }
            if (!(EmailHolder.matches(emailPattern))) {
                etemail.requestFocus();
                etemail.setError("invalid email");
            }
            if (MobileHolder.equals("")) {
                etcontact.requestFocus();
                etcontact.setError("field required");
            }
            if(!(MobileHolder.length() == 10)){
                etcontact.requestFocus();
                etcontact.setError("Mobile Number Must be 10 Digit");
            }
            if (DegreeHolder.equals("")) {
                etdegree.requestFocus();
                etdegree.setError("field required");
            }
            if (RgnumberHolder.equals("")) {
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
            CheckEditText = false;

        } else {

            CheckEditText = true;
        }

    }
    public void DoctorRegistration(String doctor_n, String dob,String gender,String cont,String degree, String reg_no,String speciality, String other_speciality,String disease,String other_disease,String work_in,
                                   String c_exp,String spl_fee,String spl_days,String spl_package, String package_valid_days, String home_visit, String home_visit_fee,String every_visit_fee,String email,String category,String image,String docid) {


        class DoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ProfessionalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                Log.e("httpResponseMsg=", "httpResponseMsg==" + httpResponseMsg);
                progressDialog.dismiss();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(httpResponseMsg);
                    String code=jsonObject.getString("code");
                    if (code.equals("200")) {
                        Intent intent = new Intent(ProfessionalActivity.this, ClinicActivity.class);
                        intent.putExtra("email", EmailHolder);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("doctor_n", params[0]);
                hashMap.put("dob", params[1]);
                hashMap.put("gender", params[2]);
                hashMap.put("cont", params[3]);
                hashMap.put("degree", params[4]);
                hashMap.put("reg_no", params[5]);
                hashMap.put("speciality", params[6]);
                hashMap.put("other_speciality", params[7]);
                hashMap.put("disease", params[8]);
                hashMap.put("other_disease", params[9]);
                hashMap.put("work_in", params[10]);
                hashMap.put("c_exp", params[11]);
                hashMap.put("spl_fee", params[12]);
                hashMap.put("spl_days", params[13]);
                hashMap.put("spl_package", params[14]);
                hashMap.put("package_valid_days", params[15]);
                hashMap.put("home_visit", params[16]);
                hashMap.put("home_visit_fee", params[17]);
                hashMap.put("every_visit_fee", params[18]);
                hashMap.put("email", params[19]);
                hashMap.put("category", params[20]);
                hashMap.put("image", params[21]);
                hashMap.put("id", params[22]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        DoctorRegistrationClass doctorRegistrationClass = new DoctorRegistrationClass();

        doctorRegistrationClass.execute(doctor_n,dob,gender,cont,degree,reg_no,speciality,other_speciality,disease,other_disease,work_in,c_exp,
                spl_fee,spl_days,spl_package,package_valid_days,home_visit,home_visit_fee,every_visit_fee,email,category,image,docid);
    }

}




