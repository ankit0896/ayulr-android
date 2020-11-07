package com.oxygen.micro.ayulr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

public class NursingUpdatePersonalActivity extends AppCompatActivity {
    ImageView drimageView;
    RadioButton rdmale,rdfemale;
    EditText etname,etemail,etcontact,etresidence;
    static EditText etdob,etanniversary;
    Button buttonpersonal;
    String  IdHolder,ImageHolder, DrNameHolder,DobHolder,EmailHolder,ContactHolder,AnniversaryHolder,ResidenceHolder,PincodeHolder,GenderHolder;
    Boolean CheckEditText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    String username;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://ameygraphics.com/ayulr/ayulr_api/nursing/update_personal.php";
    String HttpURL1 = "https://ameygraphics.com/ayulr/ayulr_api/filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    static final String DATE_DIALOG_ID = "datePicker";
    static final String DATE_DIALOG_ID2 = "datePicker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_update_personal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drimageView = (ImageView) findViewById(R.id.drimage);
        etname = (EditText) findViewById(R.id.drName);
        etdob = (EditText) findViewById(R.id.drdob);
        etemail= (EditText) findViewById(R.id.dremail);
        etcontact = (EditText) findViewById(R.id.drcontact);
        etanniversary= (EditText) findViewById(R.id.dranniversary);
        etresidence = (EditText) findViewById(R.id.drresidential);
        buttonpersonal = (Button) findViewById(R.id.btnpersonal);
        rdmale = (RadioButton) findViewById(R.id.radio);
        rdfemale = (RadioButton) findViewById(R.id.radio1);
        Nur nur = SharedPrefManagernur.getInstance(NursingUpdatePersonalActivity.this).getUserNur();
        IdHolder = String.valueOf(nur.getNurId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(NursingUpdatePersonalActivity.this)) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(NursingUpdatePersonalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
        buttonpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDataFromImageView();
                CheckEditTextIsEmptyOrNot();
                if (NetworkDetactor.isNetworkAvailable(NursingUpdatePersonalActivity.this)) {
                    if (CheckEditText) {
                        UpdateDoctorRegistration(DrNameHolder,DobHolder,GenderHolder,EmailHolder,ContactHolder,AnniversaryHolder,
                             ResidenceHolder,ImageHolder,IdHolder);

                    } else {

                    }
                } else {
                    Toast.makeText(NursingUpdatePersonalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
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

        if (requestCode == Result_Load_Image &&resultCode ==RESULT_OK && data !=null) {
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

        bmp.compress(Bitmap.CompressFormat.JPEG,10 , byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        return ConvertImage;

    }
    public void GetDataFromImageView() {
        drimageView.setImageBitmap(bitmap);
        ImageHolder=getStringImage(bitmap);


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
                ||(ResidenceHolder.equals(""))) {
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
    public void UpdateDoctorRegistration(String doctor_n,String dob,String gender,String email,String cont, String doa,String address_resi,String image,String id) {


        class UpdateDoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(NursingUpdatePersonalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    Toast.makeText(NursingUpdatePersonalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NursingUpdatePersonalActivity.this,NursingDashboard.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(NursingUpdatePersonalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
                hashMap.put("id", params[8]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        UpdateDoctorRegistrationClass updatedoctorRegistrationClass = new UpdateDoctorRegistrationClass();

        updatedoctorRegistrationClass.execute(doctor_n,dob,gender,email,cont,doa,address_resi,image,id);
    }
    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

             //  progressDialog = ProgressDialog.show(NursingUpdatePersonalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

              //  progressDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                s = httpResponseMsg;
                new GetHttpResponse(NursingUpdatePersonalActivity.this).execute();
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
                            DrNameHolder = jobj.getString("name");
                            DobHolder = jobj.getString("dob");
                            EmailHolder = jobj.getString("email");
                            ContactHolder= jobj.getString("cont");
                            AnniversaryHolder = jobj.getString("doa");
                            ResidenceHolder = jobj.getString("res_add");
                            GenderHolder = jobj.getString("gender");
                            ImageHolder = jobj.getString("profile_img");
                            if (!ImageHolder.equals("profile_img")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ayulr.com/images/"+ImageHolder).openStream();

                                bitmap = BitmapFactory.decodeStream(is);

                            }

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
            etname.append(DrNameHolder);
            etdob.append(DobHolder);
            etemail.append(EmailHolder);
            etcontact.append(ContactHolder);
            etanniversary.append(AnniversaryHolder);
            etresidence.append(ResidenceHolder);
            // radio button ki value ko get karne ka code
            if (GenderHolder.equalsIgnoreCase("male")) {
                rdmale.setChecked(true);
            } else if (GenderHolder.equalsIgnoreCase("female")) {
                rdfemale.setChecked(true);
            }
            if (bitmap==null) {
                drimageView.setImageResource(R.drawable.profileimage);
            }else{
                drimageView.setImageBitmap(bitmap);
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
