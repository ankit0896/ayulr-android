package com.oxygen.micro.ayulr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;

public class NursingPasswordActivity extends AppCompatActivity {
    EditText editpassword,editnewpassword;
    String userid,password,newpassword;
    Button btnchange;
    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;
    String HttpURL1 = "https://ameygraphics.com/ayulr/ayulr_api/change_password.php";
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    Boolean CheckEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editpassword = (EditText) findViewById(R.id.pass);
        editnewpassword = (EditText)findViewById(R.id.confirmpass);
        btnchange = (Button) findViewById(R.id.btnchange);
        Nur nur = SharedPrefManagernur.getInstance(this).getUserNur();
        userid = String.valueOf(nur.getNurId());
        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDataFromEditText();
                if (NetworkDetactor.isNetworkAvailable(NursingPasswordActivity.this)) {
                    if (CheckEditText) {
                        RecordUpdate(userid,password,newpassword);
                        // Intent intent = new Intent(PasswordActivity.this,MainActivity.class);
                        // startActivity(intent);
                        // finish();
                    }
                } else {
                    Toast.makeText(NursingPasswordActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void GetDataFromEditText() {
        password = editpassword.getText().toString();
        newpassword = editnewpassword.getText().toString();
        if (password.equals("") || newpassword.equals("")) {
            if (password.equals("")) {
                editpassword.requestFocus();
                editpassword.setError("PLease enter the current password");
            }
            if (newpassword.equals("")) {
                editnewpassword.requestFocus();
                editnewpassword.setError("Please enter your new password");
            }
            CheckEditText = false;
        } else {
            CheckEditText = true;
        }

    }

    public void RecordUpdate(final String userid, final String password, final String newpassword){

        class RecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(NursingPasswordActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                if (httpResponseMsg.equals("Update Sucessfully")) {
                    Toast.makeText(NursingPasswordActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NursingPasswordActivity.this,NursingDashboard.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(NursingPasswordActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                if (CheckEditText) {
                    hashMap.put("userid", params[0]);

                    hashMap.put("password", params[1]);

                    hashMap.put("newpassword", params[2]);

                }
                Log.e("some value=", " " +hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL1);


                return finalResult;
            }
        }

        RecordUpdateClass RecordUpdateClass = new RecordUpdateClass();

        RecordUpdateClass.execute(userid,password, newpassword);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

        }
        return super.onOptionsItemSelected(item);
    }
}


