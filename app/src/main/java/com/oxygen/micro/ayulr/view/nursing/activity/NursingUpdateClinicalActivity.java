package com.oxygen.micro.ayulr.view.nursing.activity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.view.nursing.model.Nur;
import com.oxygen.micro.ayulr.view.nursing.adapter.NursingClinicalCityAdapter;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.constant.SharedPrefManagernur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NursingUpdateClinicalActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.drfee)
    EditText etfee;
    @BindView(R.id.drfeevalidday)
    EditText etfeevalid;
    @BindView(R.id.drvisitday)
    EditText etvisitday;
    @BindView(R.id.drcontactno)
    EditText etcontact;
    @BindView(R.id.drstate)
    EditText etstate;
    @BindView(R.id.drcity)
    EditText etcity;
    @BindView(R.id.drpincode)
    EditText etpincode;
    @BindView(R.id.draddress)
    EditText etaddress;
    @BindView(R.id.btnsub)
    Button buttonclinic;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    static EditText etmrngtime, etmrngtimeto ;
    String FeeHolder,FeeValidHolder,VisitHolder,MrngHolder,MrngToHolder,
            ContactHolder,StateHolder,CityHolder,PincodeHolder,AddressHolder,EmailHolder,IdHolder;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = Config.BASEURL+"nursing/update_clinical.php";
    ArrayList<Integer> muserday = new ArrayList<>();
    String[] listitem;
    boolean[] checkeditem;
    private static int mrngtimeHour;
    private static int mrngtimeMinute;
    private static int mrngtoHour;
    private static int mrngtoMinute;
    String HttpURL1 = Config.BASEURL+"filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    NursingClinicalCityAdapter cityAdapter;
    String CodeHolder;
    static ArrayList<String> city= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_update_clinical);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etmrngtime = (EditText) findViewById(R.id.drmrngtime);
        etmrngtimeto = (EditText) findViewById(R.id.drmrngto);
        listitem = getResources().getStringArray(R.array.days_array);
        checkeditem = new boolean[listitem.length];
        Nur nur = SharedPrefManagernur.getInstance(NursingUpdateClinicalActivity.this).getUserNur();
        IdHolder = String.valueOf(nur.getNurId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(NursingUpdateClinicalActivity.this)) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(NursingUpdateClinicalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }

        etvisitday.setOnClickListener(this);
        etmrngtime.setOnClickListener(this);
        etmrngtimeto.setOnClickListener(this);
        buttonclinic.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view==buttonclinic){
            CheckEditTextIsEmptyOrNot();
            if (NetworkDetactor.isNetworkAvailable(NursingUpdateClinicalActivity.this)) {
                if (CheckEditText) {
                    UpdateDoctorRegistration(FeeHolder, FeeValidHolder, VisitHolder, MrngHolder, MrngToHolder, ContactHolder, StateHolder,
                            CityHolder, PincodeHolder, AddressHolder, IdHolder);

                } else {


                }
            } else {
                Toast.makeText(NursingUpdateClinicalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }
        if(view==etmrngtimeto){
            // TODO Auto-generated method stub
            // show time picker dialog
            // DialogFragment newFragment = new TimePickerFragment1();
            // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID1);
            new TimePickerDialog(NursingUpdateClinicalActivity.this, mTimeSetListener1, mrngtoHour, mrngtoMinute, false).show();
        }
        if(view==etmrngtime){
            // TODO Auto-generated method stub
            // show time picker dialog
            // DialogFragment newFragment = new TimePickerFragment();
            // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID);
            new TimePickerDialog(NursingUpdateClinicalActivity.this, mTimeSetListener, mrngtimeHour, mrngtimeMinute, false).show();
        }
        if(view==etvisitday){
            AlertDialog.Builder MBuilder = new AlertDialog.Builder(NursingUpdateClinicalActivity.this);
            MBuilder.setTitle(R.string.dailog_title);
            MBuilder.setMultiChoiceItems(listitem, checkeditem, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                    if (isChecked) {
                        if (!muserday.contains(position)) {
                            muserday.add(position);
                        }
                    } else if (muserday.contains(position)) {
                        muserday.remove(position);

                    }
                }
            });
            MBuilder.setCancelable(false);
            MBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String item = "";
                    for (int i = 0; i < muserday.size(); i++) {
                        item = item + listitem[muserday.get(i)];
                        if (i != muserday.size() - 1) ;
                        {
                            item = item + ",";
                        }
                    }
                    etvisitday.setText(item);
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
                    for (int i = 0; i < checkeditem.length; i++) {
                        checkeditem[i] = false;
                        muserday.clear();
                        etvisitday.setText("");
                    }
                }
            });
            AlertDialog mdialog = MBuilder.create();
            mdialog.show();
        }
    }


    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mrngtimeHour = hourOfDay;
                    mrngtimeMinute = minute;
                    String timeSet = "";
                    if (mrngtimeHour > 12) {
                        mrngtimeHour -= 12;
                        timeSet = "PM";
                    } else if (mrngtimeHour == 0) {
                        mrngtimeHour += 12;
                        timeSet = "AM";
                    } else if (mrngtimeHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (mrngtimeMinute < 10)
                        min = "0" + mrngtimeMinute ;
                    else
                        min = String.valueOf(mrngtimeMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(mrngtimeHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etmrngtime.setText(aTime);
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener1 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mrngtoHour = hourOfDay;
                    mrngtoMinute = minute;
                    String timeSet = "";
                    if (mrngtoHour > 12) {
                        mrngtoHour -= 12;
                        timeSet = "PM";
                    } else if (mrngtoHour == 0) {
                        mrngtoHour += 12;
                        timeSet = "AM";
                    } else if (mrngtoHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (mrngtoMinute < 10)
                        min = "0" + mrngtoMinute ;
                    else
                        min = String.valueOf(mrngtoMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(mrngtoHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etmrngtimeto.setText(aTime);
                }
            };


    public void CheckEditTextIsEmptyOrNot() {

        FeeHolder = etfee.getText().toString();
        FeeValidHolder= etfeevalid.getText().toString();
        VisitHolder = etvisitday.getText().toString();
        MrngHolder = etmrngtime.getText().toString();
        MrngToHolder = etmrngtimeto.getText().toString();
        ContactHolder = etcontact.getText().toString();
        StateHolder = etstate.getText().toString();
        CityHolder = etcity.getText().toString();
        PincodeHolder = etpincode.getText().toString();
        AddressHolder = etaddress.getText().toString();
        if (FeeHolder.equals("")||FeeValidHolder.equals("") || VisitHolder.equals("")
                ||(ContactHolder.equals(""))||(StateHolder.equals(""))||(CityHolder.equals(""))||(PincodeHolder.equals(""))||(AddressHolder.equals(""))){
            if (FeeHolder.equals("")) {
                etfee.requestFocus();
                etfee.setError("field required");
            }
            if (FeeValidHolder.equals("")) {
                etfeevalid.requestFocus();
                etfeevalid.setError("field required");
            }
            if (VisitHolder.equals("")) {
                etvisitday.requestFocus();
                etvisitday.setError("field required");
            }

            if (ContactHolder.equals("")) {
                etcontact.requestFocus();
                etcontact.setError("field required");
            }
            if (AddressHolder.equals("")) {
                etaddress.requestFocus();
                etaddress.setError("field required");

            }  if (StateHolder.equals("")) {
                etstate.requestFocus();
                etstate.setError("field required");
            }
            if (CityHolder.equals("")) {
                etcity.requestFocus();
                etcity.setError("field required");
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
    public void UpdateDoctorRegistration(String fees,String valid_day, String day,String m_from1,String m_to1,
                                       String contact,String state,String city,String pincode,String address,String id) {


        class UpdateDoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(NursingUpdateClinicalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    Toast.makeText(NursingUpdateClinicalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NursingUpdateClinicalActivity.this, NursingDashboard.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(NursingUpdateClinicalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("fees", params[0]);
                hashMap.put("valid_day", params[1]);
                hashMap.put("day", params[2]);
                hashMap.put("m_from1", params[3]);
                hashMap.put("m_to1", params[4]);
                hashMap.put("contact", params[5]);
                hashMap.put("state", params[6]);
                hashMap.put("city", params[7]);
                hashMap.put("pincode", params[8]);
                hashMap.put("address", params[9]);
                hashMap.put("id", params[10]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        UpdateDoctorRegistrationClass updatedoctorRegistrationClass = new UpdateDoctorRegistrationClass();

        updatedoctorRegistrationClass.execute(fees,valid_day,day,m_from1,m_to1,contact,state,city,pincode,address,id);
    }
    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

               // progressDialog = ProgressDialog.show(NursingUpdateClinicalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

              //  progressDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                s = httpResponseMsg;
                new GetHttpResponse(NursingUpdateClinicalActivity.this).execute();
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
                            FeeHolder= jobj.getString("cl_fees");
                            FeeValidHolder = jobj.getString("cl_valid");
                            VisitHolder= jobj.getString("visit_day");
                            MrngHolder = jobj.getString("m_from1");
                            MrngToHolder = jobj.getString("m_to1");
                            ContactHolder = jobj.getString("cl_contact");
                            StateHolder= jobj.getString("cl_state");
                            CityHolder= jobj.getString("city");
                            PincodeHolder = jobj.getString("cl_pincode");
                            AddressHolder= jobj.getString("cl_address");
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

            if (FeeHolder.equals("null")){
                etfee.append("");
            }else {
                etfee.append(FeeHolder);
            }
            if (FeeValidHolder.equals("null")){
                etfeevalid.append("");
            }else{
                etfeevalid.append(FeeValidHolder);
            }
            if (VisitHolder.equals("null")){
                etvisitday.append("");
            }else {
                etvisitday.append(VisitHolder);
            }
            if (MrngHolder.equals("null")){
                etmrngtime.append("");
            }else {
                etmrngtime.append(MrngHolder);
            }
            if (MrngToHolder.equals("null")){
                etmrngtimeto.append("");
            }else{
                etmrngtimeto.append(MrngToHolder);
            }if(PincodeHolder.equals("null")){
                etpincode.append("");
            }else {
                etpincode.append(PincodeHolder);
            }
            if (AddressHolder.equals("null")){
                etaddress.append("");
            }else {
                etaddress.append(AddressHolder);
            }
            if (ContactHolder.equals("null")){
                etcontact.append("");
            }else {
                etcontact.append(ContactHolder);
            }
            if (StateHolder.equals("null")){
                etstate.append("");
            }else {
                etstate.append(StateHolder);
            }
            if (CityHolder.equals("null")){
                etcity.append("");
            }else {
                etcity.append(CityHolder);
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
