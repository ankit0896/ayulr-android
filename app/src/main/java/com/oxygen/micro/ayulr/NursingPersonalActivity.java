package com.oxygen.micro.ayulr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class NursingPersonalActivity extends AppCompatActivity {
    ImageView drimageView;
    RadioButton rdmale,rdfemale;
    EditText etname,etemail,etcontact,etresidence;
    static EditText etdob,etanniversary;
    Button buttonpersonal;
    String ImageHolder, DrNameHolder,DobHolder,EmailHolder,ContactHolder,AnniversaryHolder,ResidenceHolder,PincodeHolder,GenderHolder;
    Boolean CheckEditText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    String username,useremail,usermob;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://ameygraphics.com/ayulr/ayulr_api/nursing/personal.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String se;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    static final String DATE_DIALOG_ID = "datePicker";
    static final String DATE_DIALOG_ID2 = "datePicker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_personal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        username=intent.getStringExtra("name");
        useremail=intent.getStringExtra("email");
        usermob=intent.getStringExtra("mob");
        drimageView = (ImageView) findViewById(R.id.drimage);
        ImageHolder="Noimage.jpeg";
        etname = (EditText) findViewById(R.id.drName);
        etname.setText(username);
        etdob = (EditText) findViewById(R.id.drdob);
        etemail= (EditText) findViewById(R.id.dremail);
        etemail.setText(useremail);
        etcontact = (EditText) findViewById(R.id.drcontact);
        etcontact.setText(usermob);
        etanniversary= (EditText) findViewById(R.id.dranniversary);
        etresidence = (EditText) findViewById(R.id.drresidential);
        buttonpersonal = (Button) findViewById(R.id.btnpersonal);
        rdmale = (RadioButton) findViewById(R.id.radio);
        rdfemale = (RadioButton) findViewById(R.id.radio1);

        etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
            }
        });
        etanniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment2();
                newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID2);
            }
        });
        buttonpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();
                if (NetworkDetactor.isNetworkAvailable(NursingPersonalActivity.this)) {
                    if (CheckEditText) {

                            DoctorRegistration(DrNameHolder,DobHolder,GenderHolder,EmailHolder,ContactHolder,AnniversaryHolder,ResidenceHolder,ImageHolder);


                    } else {

                    }

                } else {
                    Toast.makeText(NursingPersonalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
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
    public static class DatePickerFragment2 extends DialogFragment
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
            etanniversary.setText(new StringBuilder()

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

        DrNameHolder = etname.getText().toString();
        DobHolder = etdob.getText().toString();
        EmailHolder = etemail.getText().toString();
        ContactHolder = etcontact.getText().toString();
        AnniversaryHolder = etanniversary.getText().toString();
        ResidenceHolder = etresidence.getText().toString();
        GenderHolder = "";
        if (rdmale.isChecked())
            GenderHolder = "male";
        if (rdfemale.isChecked())
            GenderHolder = "female";
        if (DrNameHolder.equals("") || DobHolder.equals("")||ContactHolder.equals("") || EmailHolder.equals("") || (!(ContactHolder.length() == 10)) || (!(EmailHolder.matches(emailPattern)))
                ||(ResidenceHolder.equals(""))){
            if (DrNameHolder.equals("")) {
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
            if (ResidenceHolder.equals("")) {
                etresidence.requestFocus();
                etresidence.setError("field required");

            }

            CheckEditText = false;

        } else {

            CheckEditText = true;
        }

    }
    public void DoctorRegistration(String doctor_n,String dob,String gender,String email,String cont,String doa,String address_resi,String image) {


        class DoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(NursingPersonalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    //  Toast.makeText(PersonalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NursingPersonalActivity.this, NursingProfessionalActivity.class);
                    intent.putExtra("email", EmailHolder);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(NursingPersonalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("doctor_n", params[0]);
                hashMap.put("dob", params[1]);
                hashMap.put("gender", params[2]);
                hashMap.put("email", params[3]);
                hashMap.put("cont", params[4]);
                hashMap.put("doa", params[5]);
                hashMap.put("address_resi", params[6]);
                hashMap.put("image", params[7]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        DoctorRegistrationClass doctorRegistrationClass = new DoctorRegistrationClass();

        doctorRegistrationClass.execute(doctor_n,dob,gender,email,cont,doa,address_resi,image);
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
