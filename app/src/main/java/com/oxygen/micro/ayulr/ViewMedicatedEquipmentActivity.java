package com.oxygen.micro.ayulr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

public class ViewMedicatedEquipmentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView txtAlert;
    FloatingActionButton floatingActionButton;
    private EquipmentDonorAdapter adapter;
    private List<EquipmentDonor> donorList;
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicated_equipment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        donorList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        txtAlert = (TextView)findViewById(R.id.txtAler);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.funnel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewMedicatedEquipmentActivity.this));
        if (NetworkDetactor.isNetworkAvailable(ViewMedicatedEquipmentActivity.this)) {
            MyAsync myAsync = new MyAsync();
            myAsync.execute();
        } else {
            Toast.makeText(ViewMedicatedEquipmentActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.popmenu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.equipment) {
            Intent intent=new Intent(ViewMedicatedEquipmentActivity.this,MedicateEquipmentActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyAsync extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ViewMedicatedEquipmentActivity.this, "loading", "please wait");
        }

        @Override
        protected String doInBackground(Void... params) {
            String server_url = Config.BASEURL+"fetch_equi.php";

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

                    EquipmentDonor data = new EquipmentDonor(
                            jobj.getString("id"),
                            jobj.getString("name"),
                            jobj.getString("email"),
                            jobj.getString("contact"),
                            jobj.getString("e_image")
                    );

                    donorList.add(data);
                    adapter = new EquipmentDonorAdapter(ViewMedicatedEquipmentActivity.this, donorList);
                    recyclerView.setAdapter(adapter);

                }
            } catch (Exception e) {
                Toast.makeText(ViewMedicatedEquipmentActivity.this, " No Donor Found" , Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),ServiceActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}


