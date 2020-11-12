package com.oxygen.micro.ayulr.view.nursing.activity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.view.adapter.StateAdapter;
import com.oxygen.micro.ayulr.view.nursing.adapter.NursingClinicalCityAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NursingClinicalActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.drfee)
    EditText etfee;
    @BindView(R.id.drfeevalidday)
    EditText etfeevalid;
    @BindView(R.id.drvisitday)
    EditText etvisitday;
    @BindView(R.id.drcontactno)
    EditText etcontact;
    EditText etstate;
    EditText etcity;
    @BindView(R.id.drpincode)
    EditText etpincode;
    @BindView(R.id.draddress)
    EditText etaddress;
    @BindView(R.id.btnsub)
    Button buttonclinic;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    static EditText etmrngtime, etmrngtimeto;
    String FeeHolder, FeeValidHolder, VisitHolder, MrngHolder, MrngToHolder,
            ContactHolder, StateHolder, CityHolder, PincodeHolder, AddressHolder, EmailHolder;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = Config.BASEURL + "nursing/clinical.php";
    ArrayList<Integer> muserday = new ArrayList<>();
    String[] listitem;
    boolean[] checkeditem;
    private static int mrngtimeHour;
    private static int mrngtimeMinute;
    private static int mrngtoHour;
    private static int mrngtoMinute;
    Spinner state_spinner, city_spinner;
    String HttpURL1 = Config.BASEURL + "filter_city.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String se;
    NursingClinicalCityAdapter cityAdapter;
    String CodeHolder;
    public static ArrayList<String> city = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_clinical);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("email");

        etmrngtime = (EditText) findViewById(R.id.drmrngtime);
        etmrngtimeto = (EditText) findViewById(R.id.drmrngto);

        state_spinner = (Spinner) findViewById(R.id.state_spinner);
        city_spinner = (Spinner) findViewById(R.id.city_spinner);
        listitem = getResources().getStringArray(R.array.days_array);
        checkeditem = new boolean[listitem.length];
        cityAdapter = new NursingClinicalCityAdapter(NursingClinicalActivity.this);
        StateAdapter stateAdapter = new StateAdapter(NursingClinicalActivity.this, State, Code);
        state_spinner.setAdapter(stateAdapter);
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


        etvisitday.setOnClickListener(this);
        etmrngtime.setOnClickListener(this);
        etmrngtimeto.setOnClickListener(this);
        buttonclinic.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==etvisitday){
            AlertDialog.Builder MBuilder = new AlertDialog.Builder(NursingClinicalActivity.this);
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

        if(v==etmrngtime){
            // TODO Auto-generated method stub
            // show time picker dialog
            // DialogFragment newFragment = new TimePickerFragment();
            //newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID);
            new TimePickerDialog(NursingClinicalActivity.this, mTimeSetListener, mrngtimeHour, mrngtimeMinute, false).show();
        }

        if(v==etmrngtimeto){
            // TODO Auto-generated method stub
            // show time picker dialog
            //  DialogFragment newFragment = new TimePickerFragment1();
            // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID1);
            new TimePickerDialog(NursingClinicalActivity.this, mTimeSetListener1, mrngtoHour, mrngtoMinute, false).show();
        }

        if(v==buttonclinic){
            CheckEditTextIsEmptyOrNot();
            if (NetworkDetactor.isNetworkAvailable(NursingClinicalActivity.this)) {
                if (CheckEditText) {
                    if (!(StateHolder.equals("Select State"))) {
                        DoctorRegistration(FeeHolder, FeeValidHolder, VisitHolder, MrngHolder, MrngToHolder, ContactHolder, StateHolder,
                                CityHolder, PincodeHolder, AddressHolder, EmailHolder);

                    } else {
                        Toast.makeText(NursingClinicalActivity.this, "Select State", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            } else {
                Toast.makeText(NursingClinicalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
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
                    } else if (mrngtimeHour == 12) {
                        timeSet = "PM";
                    } else {
                        timeSet = "AM";
                    }

                    String min = "";
                    if (mrngtimeMinute < 10)
                        min = "0" + mrngtimeMinute;
                    else
                        min = String.valueOf(mrngtimeMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(mrngtimeHour).append(':')
                            .append(min).append(" ").append(timeSet).toString();
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
                    } else if (mrngtoHour == 12) {
                        timeSet = "PM";
                    } else {
                        timeSet = "AM";
                    }

                    String min = "";
                    if (mrngtoMinute < 10)
                        min = "0" + mrngtoMinute;
                    else
                        min = String.valueOf(mrngtoMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(mrngtoHour).append(':')
                            .append(min).append(" ").append(timeSet).toString();
                    etmrngtimeto.setText(aTime);
                }
            };


    public void CheckEditTextIsEmptyOrNot() {


        FeeHolder = etfee.getText().toString();
        FeeValidHolder = etfeevalid.getText().toString();
        VisitHolder = etvisitday.getText().toString();
        MrngHolder = etmrngtime.getText().toString();
        MrngToHolder = etmrngtimeto.getText().toString();
        ContactHolder = etcontact.getText().toString();
        PincodeHolder = etpincode.getText().toString();
        AddressHolder = etaddress.getText().toString();
        if (FeeHolder.equals("") || FeeValidHolder.equals("") || VisitHolder.equals("")
                || (ContactHolder.equals("")) || (PincodeHolder.equals("")) || (AddressHolder.equals(""))) {
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

    public void DoctorRegistration(String fees, String valid_day, String day, String m_from1, String m_to1,
                                   String contact, String state, String city, String pincode, String address, String email) {


        class DoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(NursingClinicalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    Toast.makeText(NursingClinicalActivity.this, "You are registered Successfully\nPlease login to access your account", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NursingClinicalActivity.this, NursingActivityLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    // Toast.makeText(NursingClinicalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    // Intent intent = new Intent(NursingClinicalActivity.this, NursingPlanActivity.class);
                    //intent.putExtra("email", EmailHolder);
                    // startActivity(intent);
                    // finish();
                } else {
                    Toast.makeText(NursingClinicalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
                hashMap.put("email", params[10]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        DoctorRegistrationClass doctorRegistrationClass = new DoctorRegistrationClass();

        doctorRegistrationClass.execute(fees, valid_day, day, m_from1, m_to1, contact, state, city, pincode, address, email);
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

    void clearData() {
        city.clear();


    }


    class MyAsync extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(NursingClinicalActivity.this, "Processing", "please wait moment...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String server_url = Config.BASEURL + "fetch_city.php";

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
                        // Toast.makeText(ClinicActivity.this, "No Previews City"+e, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // Toast.makeText(ClinicActivity.this, "No Previews City"+e, Toast.LENGTH_SHORT).show();
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


}
