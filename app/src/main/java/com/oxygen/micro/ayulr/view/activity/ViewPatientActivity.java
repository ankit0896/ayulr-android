package com.oxygen.micro.ayulr.view.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class ViewPatientActivity extends AppCompatActivity {
String PatientHolder,IdHolder,PhoneHolder,AgeHolder,SymptomsHolder,GenderHolder,DateHolder,TimeHolder;
TextView name,age,number,symptoms,gender;
 static EditText date,time;
 Button btnconfirm,btncancel;
    HttpParse httpParse = new HttpParse();
    String HttpURL1 = Config.BASEURL+"filter_patient2.php";
    String HttpU = Config.BASEURL+"cancel_booking.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String se;
    ProgressDialog pDialog;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mMinute;
    static final String DATE_DIALOG_ID = "datePicker";
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    String HttpURL = Config.BASEURL+"update_booking.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);
        Intent intent=getIntent();
        IdHolder=intent.getStringExtra("p_id");
      //  Toast.makeText(this, ""+IdHolder, Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, ""+IdHolder, Toast.LENGTH_SHORT).show();
        name = (TextView)findViewById(R.id.name);
        age = (TextView)findViewById(R.id.age);
        number= (TextView)findViewById(R.id.contact);
        symptoms = (TextView)findViewById(R.id.symptoms);
        gender= (TextView)findViewById(R.id.gender);
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        btnconfirm=(Button)findViewById(R.id.btnconfirm);
        btncancel=(Button)findViewById(R.id.btncancel);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ViewPatientActivity.this, mTimeSetListener, mHour, mMinute, false).show();

            }
        });
        if (NetworkDetactor.isNetworkAvailable(ViewPatientActivity.this)) {
            HttpWebCall(IdHolder);
        } else {
            Toast.makeText(ViewPatientActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if (NetworkDetactor.isNetworkAvailable(ViewPatientActivity.this)) {
                UpdateDoctorRegistration( DateHolder,TimeHolder,IdHolder);
                } else {
                    Toast.makeText(ViewPatientActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkDetactor.isNetworkAvailable(ViewPatientActivity.this)) {
                HttpWeb(IdHolder);
                } else {
                    Toast.makeText(ViewPatientActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
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
            date.setText(new StringBuilder()

                    .append(mDay).append("-")
                    .append(mYear).append("-")
                    .append(mMonth + 1).append(""));
        }
    }
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                    String timeSet = "";
                    if (mHour > 12) {
                        mHour -= 12;
                        timeSet = "PM";
                    } else if (mHour == 0) {
                        mHour += 12;
                        timeSet = "AM";
                    } else if (mHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (mMinute < 10)
                        min = "0" + mMinute ;
                    else
                        min = String.valueOf(mMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(mHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    time.setText(aTime);
                }
            };

    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ViewPatientActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                se = httpResponseMsg;
               // Toast.makeText(ViewPatientActivity.this, ""+se, Toast.LENGTH_SHORT).show();
                new GetHttpResponse(ViewPatientActivity.this).execute();
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
                if (se != null) {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(se);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(i);
                            // Storing Student Name, Phone Number, Class into Variables.
                            PatientHolder = jobj.getString("p_name");
                            SymptomsHolder = jobj.getString("symtoms");
                            PhoneHolder= jobj.getString("phone");
                            AgeHolder = jobj.getString("age");
                            GenderHolder= jobj.getString("gender");
                            DateHolder = jobj.getString("date");
                            TimeHolder = jobj.getString("time");



                        }
                    } catch (JSONException e) {
                        //  Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
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

                name.setText(PatientHolder);
                age.setText(AgeHolder);
                gender.setText(GenderHolder);
                number.setText(PhoneHolder);
                symptoms.setText(SymptomsHolder);
                date.append(DateHolder);
                time.append(TimeHolder);


        }
    }
    public void CheckEditTextIsEmptyOrNot() {
        DateHolder= date.getText().toString();
        TimeHolder = time.getText().toString();
    }
    public void UpdateDoctorRegistration(String date, String time,String id) {


        class UpdateDoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ViewPatientActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    Toast.makeText(ViewPatientActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ViewPatientActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(ViewPatientActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("date", params[0]);
                hashMap.put("time", params[1]);
                hashMap.put("id", params[2]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        UpdateDoctorRegistrationClass updatedoctorRegistrationClass = new UpdateDoctorRegistrationClass();

        updatedoctorRegistrationClass.execute(date,time,id);
    }
    public void HttpWeb(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ViewPatientActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                if (httpResponseMsg.equals("Appointment cancel")) {
                    Toast.makeText(ViewPatientActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ViewPatientActivity.this,Main2Activity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(ViewPatientActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                }



            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("id", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpU);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
        // SendHttpRequestTask myAsync=new SendHttpRequestTask();
        // myAsync.execute(PreviousListViewClickedItem);
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


