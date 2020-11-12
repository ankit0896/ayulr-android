package com.oxygen.micro.ayulr.view.activity;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
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

import com.google.android.material.textfield.TextInputEditText;
import com.oxygen.micro.ayulr.CityAdapter;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.constant.SharedPrefManager;
import com.oxygen.micro.ayulr.model.User;
import com.oxygen.micro.ayulr.view.adapter.StateAdapter;

import org.json.JSONArray;
import org.json.JSONException;
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


public class UpdatePersonalActivity extends AppCompatActivity implements View.OnClickListener {
    String HttpURL1 = Config.BASEURL+"filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    @BindView(R.id.drimage)
    ImageView drimageView;
    @BindView(R.id.drName)
    TextInputEditText etname;
    @BindView(R.id.dremail)
    TextInputEditText etemail;
    @BindView(R.id.drcontact)
    TextInputEditText etcontact;
    @BindView(R.id.drmember)
    TextInputEditText etmember;
    @BindView(R.id.drcollege)
    TextInputEditText etcollege;
    @BindView(R.id.drmedalyear)
    TextInputEditText etmedalyear;
    @BindView(R.id.drmedalfor)
    TextInputEditText etmedalfor;
    @BindView(R.id.drawardas)
    TextInputEditText etawardas;
    @BindView(R.id.drawardfor)
    TextInputEditText etawardfor;
    @BindView(R.id.drresidential)
    TextInputEditText etresidence;
    @BindView(R.id.drlandmark)
    TextInputEditText etlandmark;
    @BindView(R.id.drpincode)
    TextInputEditText etpincode;
    @BindView(R.id.btnpersonal)
    Button buttonpersonal;
    @BindView(R.id.state_spinner)
    Spinner state_spinner;
    @BindView(R.id.city_spinner)
    Spinner city_spinner;
    @BindView(R.id.radio)
    RadioButton rdmale;
    @BindView(R.id.radio1)
    RadioButton rdfemale;
    static EditText etdob,etanniversary;
    String IdHolder,ImageHolder, DrNameHolder,DobHolder,EmailHolder,ContactHolder,AnniversaryHolder,MemberHolder,
            CollegeHolder,MedalyearHolder,MedalforHolder,AwardasHolder,AwardforHolder,ResidenceHolder,StateHolder,CityHolder,CodeHolder,
            LandmarkHolder,PincodeHolder,GenderHolder;
    Boolean CheckEditText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = Config.BASEURL+"update_personal.php";
    String HttpURL2 = Config.BASEURL+"filter_city.php";
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    static final String DATE_DIALOG_ID = "datePicker";
    static final String DATE_DIALOG_ID2 = "datePicker";
    CityAdapter cityAdapter;
    String se;
    public static ArrayList<String> city= new ArrayList<String>();
    StateAdapter stateAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_personal);
        initview();

    }
    private void initview(){
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drimageView = (ImageView) findViewById(R.id.drimage);
        // ImageHolder = String.valueOf(getResources().getDrawable(R.drawable.profileimage));
        etdob = (EditText) findViewById(R.id.drdob);
        etanniversary= (EditText) findViewById(R.id.dranniversary);
        cityAdapter = new CityAdapter(this);
        stateAdapter = new StateAdapter(UpdatePersonalActivity.this, State,Code);
        state_spinner.setAdapter(stateAdapter);
        User user = SharedPrefManager.getInstance(UpdatePersonalActivity.this).getUser();
        IdHolder = String.valueOf(user.getId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(UpdatePersonalActivity.this)) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(UpdatePersonalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
        buttonpersonal.setOnClickListener(this);
        drimageView.setOnClickListener(this);
        etdob.setOnClickListener(this);
        etanniversary.setOnClickListener(this);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //   Select Provider
                TextView state_TV = (TextView) view.findViewById(R.id.state_TV);
                TextView code_TV = (TextView) view.findViewById(R.id.code_TV);
                StateHolder = state_TV.getText().toString();
                CodeHolder = code_TV.getText().toString();
                HttpWeb(CodeHolder);
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
    }

    @Override
    public void onClick(View v) {
        if (v==buttonpersonal){
            GetDataFromImageView();
            CheckEditTextIsEmptyOrNot();
            if (NetworkDetactor.isNetworkAvailable(UpdatePersonalActivity.this)) {
                if (CheckEditText) {
                    UpdateDoctorRegistration(DrNameHolder,DobHolder,GenderHolder,EmailHolder,ContactHolder,CollegeHolder,MemberHolder,
                            AnniversaryHolder, MedalyearHolder,MedalforHolder,AwardasHolder,AwardforHolder,ResidenceHolder,StateHolder,CityHolder,LandmarkHolder,PincodeHolder,ImageHolder,IdHolder);

                } else {

                }
            } else {
                Toast.makeText(UpdatePersonalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }
        if (v==drimageView){
            Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryintent.setType("image/*");
            startActivityForResult(galleryintent, Result_Load_Image);
        }
        if (v==etdob){
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
        }
        if (v==etanniversary){
            DialogFragment newFragment = new DatePickerFragment2();
            newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID2);
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
        MemberHolder = etmember.getText().toString();
        CollegeHolder =etcollege.getText().toString();
        MedalyearHolder = etmedalyear.getText().toString();
        MedalforHolder = etmedalfor.getText().toString();
        AwardasHolder = etawardas.getText().toString();
        AwardforHolder = etawardfor.getText().toString();
        ResidenceHolder = etresidence.getText().toString();
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
    public void UpdateDoctorRegistration(String doctor_n,String dob,String gender,String email,String cont,String studied_place,String member_of,
                                   String doa, String medal_year, String medal_for, String awarded_year,String awarded_for,String address_resi, String state, String city,String res_landmark, String picode,String image,String id) {


        class UpdateDoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(UpdatePersonalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                String code=httpResponseMsg.toString().trim();
                if (code.equals("200")) {
                    Intent intent = new Intent(UpdatePersonalActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(UpdatePersonalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
                hashMap.put("res_landmark", params[15]);
                hashMap.put("pincode", params[16]);
                hashMap.put("image", params[17]);
                hashMap.put("id", params[18]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        UpdateDoctorRegistrationClass updatedoctorRegistrationClass = new UpdateDoctorRegistrationClass();

        updatedoctorRegistrationClass.execute(doctor_n,dob,gender,email,cont,studied_place,member_of,doa,medal_year,medal_for,
                awarded_year,awarded_for,address_resi,state,city,res_landmark,picode,image,id);
    }
    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

              //  progressDialog = ProgressDialog.show(UpdatePersonalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

            //    progressDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                s = httpResponseMsg;
                new GetHttpResponse(UpdatePersonalActivity.this).execute();
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
                            MemberHolder = jobj.getString("member_of");
                            CollegeHolder = jobj.getString("studied_from");
                            MedalyearHolder = jobj.getString("medal_year");
                            MedalforHolder= jobj.getString("medal_for");
                            AwardasHolder = jobj.getString("awarded_as");
                            AwardforHolder= jobj.getString("awarded_for");
                            ResidenceHolder = jobj.getString("res_add");
                            StateHolder = jobj.getString("res_state");
                            CityHolder = jobj.getString("res_city");
                            LandmarkHolder = jobj.getString("landmark");
                            PincodeHolder = jobj.getString("res_pincode");
                            GenderHolder = jobj.getString("gender");
                            ImageHolder = jobj.getString("profile_img");
                            if (!ImageHolder.equals("profile_img")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ameygraphics.com/ayulr/images/"+ImageHolder).openStream();

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
            if (AnniversaryHolder.equals("null")){
                etanniversary.append("");
            }else {
                etanniversary.append(AnniversaryHolder);
            }
            if (MemberHolder.equals("null")){
                etmember.append("");
            }else {
                etmember.append(MemberHolder);
            }
            if (CollegeHolder.equals("null")){
                etcollege.append("");
            }else {
                etcollege.append(CollegeHolder);
            }
            if (MedalforHolder.equals("null")){
                etmedalfor.append("");
            }else {
                etmedalfor.append(MedalforHolder);
            }
            if (MedalyearHolder.equals("null")){
                etmedalyear.append("");
            }else {
                etmedalyear.append(MedalyearHolder);
            }
            if (AwardasHolder.equals("null")){
                etawardas.append("");
            }else {
                etawardas.append(AwardasHolder);
            }
            if (AwardforHolder.equals("null")){
                etawardfor.append("");
            }else {
                etawardfor.append(AwardforHolder);
            }
            if (ResidenceHolder.equals("null")){
                etresidence.append("");
            }else {
                etresidence.append(ResidenceHolder);
            }
            if (LandmarkHolder.equals("null")){
                etlandmark.append("");
            }else {
                etlandmark.append(LandmarkHolder);
            }
            if (PincodeHolder.equals("null")){
                etpincode.append("");
            }else {
                etpincode.append(PincodeHolder);
            }
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
    public void HttpWeb(final String city) {

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

                ParseResult = httpParse.postRequest(ResultHash, HttpURL2);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(city);

    }

    void clearData(){
        city.clear();



    }
    class MyAsync extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(UpdatePersonalActivity.this, "Processing", "please wait moment...");
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
                        // Toast.makeText(UpdatePersonalActivity.this, "No Previews City"+e, Toast.LENGTH_LONG).show();
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
