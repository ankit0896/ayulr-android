package com.oxygen.micro.ayulr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import androidx.annotation.IdRes;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ParaProfessionalActivity extends AppCompatActivity {
    Spinner spinner;
    EditText etdegree,etexperience,etworkin,etnature,etexpertise,etalsotreat,ethomevisitfee,etvalidfor,etname,etemail,etcontact;
    Button buttonprofessional;
    RadioGroup rdgroupe;
    RadioButton rdyes,rdno;
    String CategoryHolder,DegreeHolder,ExperienceHolder,WorkinHolder,NatureHolder,ExpertHolder,AlsotreatHolder,
    ValidHolder,HomevisitHolder,EmailHolder,HomevisitfeeHolder,NameHolder,ContactHolder,ImageHolder,StateHolder,CityHolder,CodeHolder,DobHolder,GenderHolder;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = Config.BASEURL+"paramedical/new_professional.php";
    String[] listitem2;
    boolean[] checkeditem2;
    ArrayList<Integer> degree = new ArrayList<>();
    ImageView drimageView;
    RadioButton rdmale,rdfemale;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    static final String DATE_DIALOG_ID = "datePicker";
    static EditText etdob;
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_professional);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        NameHolder=intent.getStringExtra("name");
        EmailHolder=intent.getStringExtra("email");
        ContactHolder=intent.getStringExtra("mob");
        drimageView = (ImageView) findViewById(R.id.drimage);
        ImageHolder="Noimage.jpeg";
        Log.e("imagepath",""+ImageHolder);
        etname = (EditText) findViewById(R.id.drName);
        etname.setText("Dr."+NameHolder);
        etdob = (EditText) findViewById(R.id.drdob);
        etemail= (EditText) findViewById(R.id.dremail);
        etemail.setText(EmailHolder);
        etcontact = (EditText) findViewById(R.id.drcontact);
        etcontact.setText(ContactHolder);
        spinner = (Spinner) findViewById(R.id.category);
        String[] Category = {"Select Category","Physiotherapy","Acupunture","Acupressure","Yoga","Audiometrist","Optometrist"};
        ArrayAdapter adapter = new ArrayAdapter(ParaProfessionalActivity.this, R.layout.support_simple_spinner_dropdown_item, Category);
        spinner.setAdapter(adapter);
        etdegree = (EditText) findViewById(R.id.drdegree);
        etexperience = (EditText) findViewById(R.id.drexperience);
        etworkin= (EditText) findViewById(R.id.drworkin);
        etnature = (EditText) findViewById(R.id.drnature);
        etexpertise= (EditText) findViewById(R.id.drexpert);
        etalsotreat = (EditText) findViewById(R.id.dralsotreat);
        ethomevisitfee = (EditText) findViewById(R.id.drhomevisitfee);
        etvalidfor= (EditText) findViewById(R.id.drvalidfor);
        ethomevisitfee.setVisibility(View.GONE);
        etvalidfor.setVisibility(View.GONE);
        buttonprofessional = (Button) findViewById(R.id.btnprofessional);
        rdgroupe=(RadioGroup)findViewById(R.id.radiogrp);
        rdyes = (RadioButton) findViewById(R.id.radio);
        rdno = (RadioButton) findViewById(R.id.radio1);
        rdmale = (RadioButton) findViewById(R.id.radio3);
        rdfemale = (RadioButton) findViewById(R.id.radio4);
        listitem2 = getResources().getStringArray(R.array.para_degree_array);
        checkeditem2 = new boolean[listitem2.length];
        etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
            }
        });
        rdgroupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId==R.id.radio){
                    ethomevisitfee.setVisibility(View.VISIBLE);
                    etvalidfor.setVisibility(View.VISIBLE);
                }else if (checkedId==R.id.radio1){
                    ethomevisitfee.setVisibility(View.GONE);
                    etvalidfor.setVisibility(View.GONE);

                }
            }
        });

        etdegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder MBuilder = new AlertDialog.Builder(ParaProfessionalActivity.this);
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
        buttonprofessional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();
                if (NetworkDetactor.isNetworkAvailable(ParaProfessionalActivity.this)) {
                    if (CheckEditText) {
                        if (!spinner.getSelectedItem().equals("Select Category")) {
                            DoctorRegistration(NameHolder, DobHolder, GenderHolder, ContactHolder,CategoryHolder,DegreeHolder,ExperienceHolder,WorkinHolder, NatureHolder,ExpertHolder,AlsotreatHolder,
                                    HomevisitHolder, HomevisitfeeHolder, ValidHolder, EmailHolder,ImageHolder);
                        }else{
                            Toast.makeText(ParaProfessionalActivity.this, "Select Category First", Toast.LENGTH_SHORT).show();
                        }
                    } else {


                    }
                } else {
                    Toast.makeText(ParaProfessionalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }

            }
        });
        drimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.drimage:
                        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryintent.setType("image/*");
                        startActivityForResult(galleryintent, Result_Load_Image);
                        break;
                }

            }
        });

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
        ContactHolder = etcontact.getText().toString();
        CategoryHolder=spinner.getSelectedItem().toString();
        DegreeHolder = etdegree.getText().toString();
        ExperienceHolder= etexperience.getText().toString();
        WorkinHolder = etworkin.getText().toString();
        NatureHolder = etnature.getText().toString();
        ExpertHolder = etexpertise.getText().toString();
        AlsotreatHolder = etalsotreat.getText().toString();
        HomevisitfeeHolder = ethomevisitfee.getText().toString();
        ValidHolder = etvalidfor.getText().toString();
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
        if (NameHolder.equals("") || DobHolder.equals("")||ContactHolder.equals("") || EmailHolder.equals("") || (!(ContactHolder.length() == 10)) || (!(EmailHolder.matches(emailPattern)))||DegreeHolder.equals("")||ExperienceHolder.equals("")||ExpertHolder.equals("")) {
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
            if (ContactHolder.equals("")) {
                etcontact.requestFocus();
                etcontact.setError("field required");
            }
            if(!(ContactHolder.length() == 10)){
                etcontact.requestFocus();
                etcontact.setError("Mobile Number Must be 10 Digit");
            }
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
    public void DoctorRegistration(String doctor_n, String dob,String gender,String cont,String category,String degree, String experience,String workin, String nature,String expertise,String alsotreat,
                                String home_visit,String home_visit_fee,String every_visit_fee,String email,String image) {


        class DoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ParaProfessionalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    //Toast.makeText(ParaProfessionalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ParaProfessionalActivity.this, ParaClinicActivity.class);
                    intent.putExtra("email", EmailHolder);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ParaProfessionalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("p_name", params[0]);
                hashMap.put("dob", params[1]);
                hashMap.put("gender", params[2]);
                hashMap.put("cont", params[3]);
                hashMap.put("category", params[4]);
                hashMap.put("degree", params[5]);
                hashMap.put("experience", params[6]);
                hashMap.put("work_in", params[7]);
                hashMap.put("nature", params[8]);
                hashMap.put("expertise", params[9]);
                hashMap.put("also_treat", params[10]);
                hashMap.put("home_visit", params[11]);
                hashMap.put("home_visit_fee", params[12]);
                hashMap.put("every_visit_fee", params[13]);
                hashMap.put("email", params[14]);
                hashMap.put("image", params[15]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        DoctorRegistrationClass doctorRegistrationClass = new DoctorRegistrationClass();

        doctorRegistrationClass.execute(doctor_n,dob,gender,cont,category,degree,experience,workin,nature,expertise,alsotreat,home_visit,home_visit_fee,every_visit_fee,email,image);
    }

}




