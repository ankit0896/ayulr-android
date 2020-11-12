package com.oxygen.micro.ayulr;

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
import butterknife.BindView;
import butterknife.ButterKnife;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewDetailMedicatedEquipmentActivity extends AppCompatActivity {
    @BindView(R.id.etname)
    TextView t1;
    @BindView(R.id.etemail)
    TextView t2;
    @BindView(R.id.etcontact)
    TextView t3;
    @BindView(R.id.etage)
    TextView t4;
    @BindView(R.id.etgender)
    TextView t5;
    @BindView(R.id.ettype)
    TextView t6;
    @BindView(R.id.etgroupe)
    TextView t7;
    @BindView(R.id.etaddress)
    TextView t8;
    @BindView(R.id.etdes)
    TextView t9;
    ProgressDialog pDialog;
    String HttpURL1 = Config.BASEURL+"view_equi.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    String NameHolder,EmailHolder,ContactHolder,AgeHolder,GenderHolder,EquipmentHolder,DescriptionHolder,EquipmentDescriptionHolder,AddressHolder,D_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_medicated_equipment);
        initview();

    }
    private void initview(){
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent iGet = getIntent();
        D_id = (iGet.getStringExtra("b_id"));
        if (NetworkDetactor.isNetworkAvailable(ViewDetailMedicatedEquipmentActivity.this)) {
            HttpWebCall(String.valueOf(D_id));
        } else {
            Toast.makeText(ViewDetailMedicatedEquipmentActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ViewDetailMedicatedEquipmentActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                s = httpResponseMsg;
                new GetHttpResponse(ViewDetailMedicatedEquipmentActivity.this).execute();
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
                            EquipmentHolder = jobj.getString("e_for");
                            DescriptionHolder = jobj.getString("price");
                            EquipmentDescriptionHolder = jobj.getString("e_desc");
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
            t6.setText(EquipmentHolder);
            t7.setText(DescriptionHolder);
            t8.setText(AddressHolder);
            t9.setText(EquipmentDescriptionHolder);


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
