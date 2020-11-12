package com.oxygen.micro.ayulr.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.view.adapter.BloodDonorAdapter;
import com.oxygen.micro.ayulr.model.BloodDonor;

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

public class ViewBloodDonorActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView txtAlert;
    FloatingActionButton floatingActionButton;
    private BloodDonorAdapter adapter;
    private List<BloodDonor> donorList;
    String[] Cat = {"A+","A-","B+","B-","AB+","AB-","O+","O-"};
    String category;
    String HttpURL1 = Config.BASEURL+"donor/filter_blood_donor.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String se;
    HttpParse httpParse = new HttpParse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blood_donor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        donorList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        txtAlert = (TextView)findViewById(R.id.txtAler);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.funnel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewBloodDonorActivity.this));
        if (NetworkDetactor.isNetworkAvailable(ViewBloodDonorActivity.this)) {
            MyAsync myAsync = new MyAsync();
            myAsync.execute();
        } else {
            Toast.makeText(ViewBloodDonorActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 &&  floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 &&  floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.popmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.blood) {
            Intent intent=new Intent(ViewBloodDonorActivity.this, BloodDonateActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == android.R.id.home) {
           onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Category() {
        final LayoutInflater li = LayoutInflater.from(ViewBloodDonorActivity.this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.activity_filter, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box

        final ListView listView = (ListView) confirmDialog.findViewById(R.id.list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewBloodDonorActivity.this, android.R.layout.simple_list_item_1, Cat);
        listView.setAdapter(arrayAdapter);
        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewBloodDonorActivity.this);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();
                category = ((TextView) view).getText().toString();
                if (NetworkDetactor.isNetworkAvailable(ViewBloodDonorActivity.this)) {
                    HttpWebCall(category);
                } else {
                    Toast.makeText(ViewBloodDonorActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    void clearData(){
        donorList.clear();


    }
    class MyAsync extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ViewBloodDonorActivity.this, "loading", "please wait");
        }

        @Override
        protected String doInBackground(Void... params) {
            String server_url = Config.BASEURL+"donor/fetch_donor.php";

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

            try {
                JSONArray jsonarray = new JSONArray(s);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jobj = jsonarray.getJSONObject(i);

                    BloodDonor data = new BloodDonor(
                            jobj.getString("id"),
                            jobj.getString("name"),
                            jobj.getString("email"),
                            jobj.getString("contact"),
                            jobj.getString("photo")
                    );

                    donorList.add(data);
                    adapter = new BloodDonorAdapter(ViewBloodDonorActivity.this, donorList);
                    recyclerView.setAdapter(adapter);

                }
            } catch (Exception e) {
                Toast.makeText(ViewBloodDonorActivity.this, " No Donor Found" , Toast.LENGTH_LONG).show();
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
    public void HttpWebCall(final String category) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //  pDialog = ProgressDialog.show(getContext(), "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                // pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                se = httpResponseMsg;

                if (se.equals("fail")){
                    txtAlert.setVisibility(View.VISIBLE);
                    clearData();
                }else {
                    txtAlert.setVisibility(View.GONE);
                    MyAsyn myAsyn = new MyAsyn();
                    myAsyn.execute();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("category", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL1);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(category);

    }
    class MyAsyn extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ViewBloodDonorActivity.this, "loading", "please wait");
        }

        @Override
        protected String doInBackground(Void... params) {
            String server_url = Config.BASEURL+"donor/filter_blood_donor.php";

            try {
                URL url = new URL(server_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                String result = getStringFromInputStrea(inputStream);

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
                JSONArray jsonarray = new JSONArray(se);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jobj = jsonarray.getJSONObject(i);

                    BloodDonor data = new BloodDonor(
                            jobj.getString("id"),
                            jobj.getString("name"),
                            jobj.getString("email"),
                            jobj.getString("contact"),
                            jobj.getString("photo")
                    );

                    donorList.add(data);
                    adapter = new BloodDonorAdapter(ViewBloodDonorActivity.this, donorList);
                    recyclerView.setAdapter(adapter);

                }
            } catch (Exception e) {
              //  Toast.makeText(ViewBloodDonorActivity.this, " No Donor Found" , Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
    }

    private static String getStringFromInputStrea(InputStream is) {

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
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),ServiceActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}


