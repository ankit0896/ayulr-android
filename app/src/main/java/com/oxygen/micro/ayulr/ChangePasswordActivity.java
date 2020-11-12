package com.oxygen.micro.ayulr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oxygen.micro.ayulr.doctor.activity.ActivityLogin;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText editpassword,editconfirmpassword;
    String phone,password;
    Button btnchange;
    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;
    String HttpURL1 = "https://ameygraphics.com/ayulr/ayulr_api/reset_password.php";
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    Boolean CheckEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        editpassword = (EditText) findViewById(R.id.pass);
        editconfirmpassword = (EditText)findViewById(R.id.confirmpass);
        Intent intent=getIntent();
        phone=intent.getStringExtra("mobile");
        Log.e("some value="," "+phone);

        btnchange = (Button) findViewById(R.id.btnchange);
        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDataFromEditText();
                if (NetworkDetactor.isNetworkAvailable(ChangePasswordActivity.this)) {
                if (CheckEditText) {
                    RecordUpdate(phone, password);
                    Intent intent = new Intent(ChangePasswordActivity.this, ActivityLogin.class);
                    startActivity(intent);
                }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
    public void GetDataFromEditText() {
        if (editpassword.getText().toString().trim().equals(editconfirmpassword.getText().toString().trim())) {
            password=editpassword.getText().toString();
            CheckEditText=true;
        }else
        {
            editconfirmpassword.requestFocus();
            editconfirmpassword.setError("Do not match password");

            CheckEditText=false;
        }
    }

    public void RecordUpdate(final String email, final String password){

        class RecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ChangePasswordActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                Toast.makeText(ChangePasswordActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {
                if (CheckEditText) {
                    hashMap.put("phone", params[0]);

                    hashMap.put("password", params[1]);

                }
                Log.e("some value=", " " +hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL1);


                return finalResult;
            }
        }

        RecordUpdateClass RecordUpdateClass = new RecordUpdateClass();

        RecordUpdateClass.execute(email, password);
    }

}

