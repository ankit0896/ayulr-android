package com.oxygen.micro.ayulr.view.nursing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.constant.Config;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NursingBasicPlanActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog pDialog;
    String HttpURL1 = Config.BASEURL+"insert_basic.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    String StatusHolder,userid;
    String EmailHolder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnplan)
    Button plan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_basic_plan);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        EmailHolder=intent.getStringExtra("email");
        Log.e("value",""+EmailHolder);
        plan.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view==plan){
            if (NetworkDetactor.isNetworkAvailable(NursingBasicPlanActivity.this)) {
                Http(EmailHolder);

            } else {
                Toast.makeText(NursingBasicPlanActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Http(String email) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(NursingBasicPlanActivity.this, "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                pDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    Toast.makeText(NursingBasicPlanActivity.this, "You are registered Successfully\nPlease login to access your account", Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(NursingBasicPlanActivity.this, NursingActivityLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(NursingBasicPlanActivity.this,httpResponseMsg.toString() , Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("email", params[0]);
                ;
                Log.e("value",""+ResultHash);
                ParseResult = httpParse.postRequest(ResultHash, HttpURL1);


                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(email);

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
