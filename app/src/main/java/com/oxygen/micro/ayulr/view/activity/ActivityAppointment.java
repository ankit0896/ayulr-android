package com.oxygen.micro.ayulr.view.activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.model.Patient;
import com.oxygen.micro.ayulr.view.adapter.PatientAdapter;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.constant.SharedPrefManager;
import com.oxygen.micro.ayulr.model.User;

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
import java.util.List;

public class ActivityAppointment extends AppCompatActivity {
    RecyclerView recyclerView;
    private PatientAdapter adapter;
    private List<Patient> patientList;
    HttpParse httpParse = new HttpParse();
    String HttpUR = "https://ameygraphics.com/ayulr/api/filter_patient.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String se;
    ProgressDialog pDialog;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientList = new ArrayList<>();
        User user = SharedPrefManager.getInstance(this).getUser();
        userid = String.valueOf(user.getId());
        //Toast.makeText(this, ""+userid, Toast.LENGTH_SHORT).show();
        if (NetworkDetactor.isNetworkAvailable(ActivityAppointment.this)) {
            HttpWeb(userid);
        } else {
            Toast.makeText(ActivityAppointment.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
    }
    public void HttpWeb(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

              //  pDialog = ProgressDialog.show(ActivityAppointment.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

             //   pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                se = httpResponseMsg;
                //Toast.makeText(ActivityAppointment.this, ""+se, Toast.LENGTH_SHORT).show();

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                MyAsync myAsync = new MyAsync();
                myAsync.execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("id", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpUR);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);

    }
    class MyAsync extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ActivityAppointment.this, "loading", "please wait");
        }

        @Override
        protected String doInBackground(Void... params) {
            String server_url = "https://ameygraphics.com/ayulr/api/fetch_patient.php";

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
           // histroyList.clear();
           // mSwipeRefreshLayout.setRefreshing(false);
            try {
                if (se != null) {
                    JSONArray jsonArray = null;
                    try {
                        JSONArray jsonarray = new JSONArray(se);

                        for(int i=0;i<jsonarray.length();i++){
                            JSONObject jobj = jsonarray.getJSONObject(i);
                               Patient data = new Patient(
                                    jobj.getString("id"),
                                    jobj.getString("order_id"),
                                    jobj.getString("p_name"),
                                    jobj.getString("phone"),
                                    jobj.getString("age"),
                                       jobj.getString("status"),
                                    jobj.getString("date"),
                                    jobj.getString("time")
                            );
                               patientList.add(data);
                               adapter = new PatientAdapter(ActivityAppointment.this,patientList);
                               recyclerView.setAdapter(adapter);


                        }
                    } catch (Exception e) {
                        Toast.makeText(ActivityAppointment.this, "No Appointmnet", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
               // Toast.makeText(ActivityAppointment.this, "No Appointment", Toast.LENGTH_SHORT).show();
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
