package com.oxygen.micro.ayulr.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewDetailBloodDonorActivity extends AppCompatActivity {
    TextView t1, t2, t3, t4, t5, t6, t7, t8;
    ProgressDialog pDialog;
    String HttpURL1 = Config.BASEURL+"donor/view_blood_donor.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    String NameHolder,EmailHolder,ContactHolder,AgeHolder,GenderHolder,TypeHolder,GroupeHolder,AddressHolder,D_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_blood_donor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        t1 = (TextView) findViewById(R.id.etname);
        t2 = (TextView) findViewById(R.id.etemail);
        t3 = (TextView) findViewById(R.id.etcontact);
        t4 = (TextView) findViewById(R.id.etage);
        t5 = (TextView) findViewById(R.id.etgender);
        t6 = (TextView) findViewById(R.id.ettype);
        t7 = (TextView) findViewById(R.id.etgroupe);
        t8 = (TextView) findViewById(R.id.etaddress);
        Intent iGet = getIntent();
        D_id = (iGet.getStringExtra("b_id"));
        if (NetworkDetactor.isNetworkAvailable(ViewDetailBloodDonorActivity.this)) {
            HttpWebCall(String.valueOf(D_id));
        } else {
            Toast.makeText(ViewDetailBloodDonorActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ViewDetailBloodDonorActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                s = httpResponseMsg;
                new GetHttpResponse(ViewDetailBloodDonorActivity.this).execute();
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
                            NameHolder = jobj.getString("name");
                            EmailHolder = jobj.getString("email");
                            ContactHolder = jobj.getString("contact");
                            AgeHolder = jobj.getString("age");
                            GenderHolder = jobj.getString("gender");
                            TypeHolder = jobj.getString("type");
                            GroupeHolder = jobj.getString("b_grp");
                            AddressHolder = jobj.getString("address");


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

            // Setting Student Name, Phone Number, Class into TextView after done all process .
            t1.setText(NameHolder);
            t2.setText(EmailHolder);
            t3.setText(ContactHolder);
            t4.setText(AgeHolder);
            t5.setText(GenderHolder);
            t6.setText(TypeHolder);
            t7.setText(GroupeHolder);
            t8.setText(AddressHolder);


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
