package com.oxygen.micro.ayulr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PersonalActivity extends AppCompatActivity {
ImageView drimageView;
    Spinner state_spinner,city_spinner;
    RadioButton rdmale,rdfemale;
    EditText etname,etemail,etcontact,etmember,etcollege,etmedalyear,etmedalfor,
    etawardas,etawardfor,etresidence,etlandmark,etpincode;
    static EditText etdob,etanniversary;
    Button buttonpersonal;
    String ImageHolder, DrNameHolder,DobHolder,EmailHolder,ContactHolder,AnniversaryHolder,MemberHolder,
    CollegeHolder,MedalyearHolder,MedalforHolder,AwardasHolder,AwardforHolder,ResidenceHolder,StateHolder,CityHolder,
    LandmarkHolder,PincodeHolder,GenderHolder;
    Boolean CheckEditText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    String username,useremail,usermob;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL =Config.BASEURL+"personal.php";
    String HttpURL1 = Config.BASEURL+"filter_city.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String se;
    CityAdapter cityAdapter;
    String CodeHolder;
    static ArrayList<String> city= new ArrayList<String>();
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    static final String DATE_DIALOG_ID = "datePicker";
    static final String DATE_DIALOG_ID2 = "datePicker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        username=intent.getStringExtra("name");
        useremail=intent.getStringExtra("email");
        usermob=intent.getStringExtra("mob");
        drimageView = (ImageView) findViewById(R.id.drimage);
        ImageHolder="Noimage.jpeg";
        //ImageHolder = String.valueOf(getResources().getDrawable(R.drawable.profileimage));
       // Toast.makeText(this, ""+ImageHolder, Toast.LENGTH_LONG).show();
        Log.e("imagepath",""+ImageHolder);
        etname = (EditText) findViewById(R.id.drName);
        etname.setText("Dr."+username);
        etdob = (EditText) findViewById(R.id.drdob);
        etemail= (EditText) findViewById(R.id.dremail);
        etemail.setText(useremail);
        etcontact = (EditText) findViewById(R.id.drcontact);
        etcontact.setText(usermob);
        etanniversary= (EditText) findViewById(R.id.dranniversary);
        etmember = (EditText) findViewById(R.id.drmember);
        etcollege = (EditText) findViewById(R.id.drcollege);
        etmedalyear = (EditText) findViewById(R.id.drmedalyear);
        etmedalfor= (EditText) findViewById(R.id.drmedalfor);
        etawardas = (EditText) findViewById(R.id.drawardas);
        etawardfor= (EditText) findViewById(R.id.drawardfor);
        etresidence = (EditText) findViewById(R.id.drresidential);
        state_spinner = (Spinner) findViewById(R.id.state_spinner);
        city_spinner = (Spinner) findViewById(R.id.city_spinner);
        etlandmark = (EditText) findViewById(R.id.drlandmark);
        etpincode = (EditText) findViewById(R.id.drpincode);
        buttonpersonal = (Button) findViewById(R.id.btnpersonal);
        rdmale = (RadioButton) findViewById(R.id.radio);
        rdfemale = (RadioButton) findViewById(R.id.radio1);
        cityAdapter = new CityAdapter(this);
        StateAdapter stateAdapter = new StateAdapter(PersonalActivity.this, State,Code);
        state_spinner.setAdapter(stateAdapter);
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
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //   Select Provider
                TextView state_TV = (TextView) view.findViewById(R.id.state_TV);
                TextView code_TV = (TextView) view.findViewById(R.id.code_TV);
                StateHolder = state_TV.getText().toString();
                CodeHolder = code_TV.getText().toString();
                HttpWebCall(CodeHolder);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //   Select Provider
                TextView city_TV = (TextView) view.findViewById(R.id.city_TV);
                CityHolder = city_TV.getText().toString();
                }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();
                if (NetworkDetactor.isNetworkAvailable(PersonalActivity.this)) {
                    if (CheckEditText) {
                        if (!(StateHolder.equals("Select State"))) {
                            DoctorRegistration(DrNameHolder, DobHolder, GenderHolder, EmailHolder, ContactHolder, AnniversaryHolder, MemberHolder,
                                    CollegeHolder, MedalyearHolder, MedalforHolder, AwardasHolder, AwardforHolder, ResidenceHolder, StateHolder, CityHolder, LandmarkHolder, PincodeHolder, ImageHolder);
                        } else {
                            Toast.makeText(PersonalActivity.this, "Select State", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                    }

                } else {
                    Toast.makeText(PersonalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
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
        MemberHolder = etmember.getText().toString();
        CollegeHolder =etcollege.getText().toString();
        MedalyearHolder = etmedalyear.getText().toString();
        MedalforHolder = etmedalfor.getText().toString();
        AwardasHolder = etawardas.getText().toString();
        AwardforHolder = etawardfor.getText().toString();
        ResidenceHolder = etresidence.getText().toString();
      //  StateHolder = etstate.getText().toString();
       // CityHolder = etcity.getText().toString();
        LandmarkHolder = etlandmark.getText().toString();
        PincodeHolder = etpincode.getText().toString();
        GenderHolder = "";
        if (rdmale.isChecked())
            GenderHolder = "male";
        if (rdfemale.isChecked())
            GenderHolder = "female";
        if (DrNameHolder.equals("") || DobHolder.equals("")||ContactHolder.equals("") || EmailHolder.equals("") || (!(ContactHolder.length() == 10)) || (!(EmailHolder.matches(emailPattern)))
                ||(ResidenceHolder.equals(""))||(PincodeHolder.equals(""))) {
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
            if (PincodeHolder.equals("")) {
                etpincode.requestFocus();
                etpincode.setError("field required");
            }

            CheckEditText = false;

        } else {

            CheckEditText = true;
        }

    }
    public void DoctorRegistration(String doctor_n, String dob,String gender, String email,String cont,String doa,String member_of,
                                   String studied_place,String medal_year, String medal_for, String awarded_year,String awarded_for,String address_resi, String state, String city,String landmark, String picode,String image) {


        class DoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(PersonalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
if (httpResponseMsg.equals("success")) {
  //  Toast.makeText(PersonalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
    Intent intent = new Intent(PersonalActivity.this, ProfessionalActivity.class);
    intent.putExtra("email", EmailHolder);
    startActivity(intent);
    finish();
}else{
    Toast.makeText(PersonalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
}
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("doctor_n", params[0]);
                hashMap.put("dob", params[1]);
                hashMap.put("gender", params[2]);
                hashMap.put("email", params[3]);
                hashMap.put("cont", params[4]);
                hashMap.put("studied_place", params[5]);
                hashMap.put("member_of", params[6]);
                hashMap.put("doa", params[7]);
                hashMap.put("medal_year", params[8]);
                hashMap.put("medal_for", params[9]);
                hashMap.put("awarded_year", params[10]);
                hashMap.put("awarded_for", params[11]);
                hashMap.put("address_resi", params[12]);
                hashMap.put("state", params[13]);
                hashMap.put("city", params[14]);
                hashMap.put("landmark", params[15]);
                hashMap.put("pincode", params[16]);
                hashMap.put("image", params[17]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        DoctorRegistrationClass doctorRegistrationClass = new DoctorRegistrationClass();

        doctorRegistrationClass.execute(doctor_n,dob,gender,email,cont,doa,member_of,studied_place,medal_year,medal_for,
                awarded_year,awarded_for,address_resi,state,city,landmark,picode,image);
    }
    String[] State = {
            "Select State",
            "Andaman And Nicobar Island",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chandigarh",
            "Chhattisgarh",
            "Dadra & Nagar Haveli",
            "Daman & Diu",
            "Delhi",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jammu & Kashmir",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Lakshadweep",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Pondicherry",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal",
    };
    String[] Code = {
            "",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "19",
            "20",
            "21",
            "22",
            "23",
            "24",
            "25",
            "26",
            "29",
            "31",
            "32",
            "33",
            "34",
            "35",
            "36",
            "37",
            "38",
            "39",
            "41",



    };
    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);


                se = httpResponseMsg;
                MyAsync myAsync = new MyAsync();
                myAsync.execute();



            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("state", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL1);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);

    }

    void clearData(){
        city.clear();



    }
    class MyAsync extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(PersonalActivity.this, "Processing", "please wait moment...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String server_url = Config.BASEURL+"fetch_city.php";

            // String server_url = "http://www.w3schools.com/xml/guestbook.asp";
            try {
                URL url = new URL(server_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                String result = getStringFromInputStream(inputStream);

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            clearData();
            try {
                if (se != null) {
                    JSONArray jsonArray = null;
                    try {
                         jsonArray = new JSONArray(se);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(i);
                            city.add(jobj.getString("name"));
                            city_spinner.setAdapter(cityAdapter);

                        }
                    } catch (Exception e) {
                       // Toast.makeText(PersonalActivity.this, "No Previews City"+e, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
               // Toast.makeText(PersonalActivity.this, "No Previews City"+e, Toast.LENGTH_SHORT).show();
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

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
