package com.oxygen.micro.ayulr.view.nursing.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.constant.SharedPrefManagernur;
import com.oxygen.micro.ayulr.view.nursing.model.Nur;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NursingStandardPlanActivity extends AppCompatActivity implements PaymentResultListener {
    TextView textView;
    ProgressDialog pDialog;
    String HttpURL1 = Config.BASEURL+"nursing/insert_standard.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    String PaymentHolder;
    RadioButton rdcash,rdonline;
    private AppCompatButton buttonConfirm;
    Button button;
    String EmailHolder,IdHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_standard_plan);
        Nur nur = SharedPrefManagernur.getInstance(this).getUserNur();
        IdHolder = String.valueOf(nur.getNurId());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        EmailHolder=intent.getStringExtra("email");
        button=(Button)findViewById(R.id.btnstandardplan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkDetactor.isNetworkAvailable(NursingStandardPlanActivity.this)) {
                    confirmOtp();

                }else {
                    Toast.makeText(NursingStandardPlanActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void confirmOtp()  {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dailog_nursingstandard_plan, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        rdcash=(RadioButton)confirmDialog.findViewById(R.id.radio);
        rdonline=(RadioButton)confirmDialog.findViewById(R.id.radio1);
        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);


        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentHolder = "";
                if (rdcash.isChecked())
                    PaymentHolder = "cash payment";
                if (rdonline.isChecked())
                    PaymentHolder = "Online payment";
                if (PaymentHolder.equals("cash payment")) {
                    alertDialog.dismiss();
                    Http(EmailHolder,PaymentHolder,IdHolder);
                }else if (PaymentHolder.equals("Online payment")){
                    alertDialog.dismiss();
                    startPayment();
                }
            }

        });
    }

    private void startPayment() {
        Checkout checkout=new Checkout();
        checkout.setImage(R.drawable.ayulrlogo);
        final Activity activity=this;
        try {
            JSONObject options=new JSONObject();
            options.put("name", "Ayu-LR");
            options.put("description", "Standard Membership");
            options.put("currency", "INR");
            options.put("amount", 708*100);
            JSONObject preFill = new JSONObject();
            preFill.put("email",EmailHolder);
            preFill.put("contact","");
            options.put("prefill", preFill);

            checkout.open(activity,options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        Http(EmailHolder,PaymentHolder,IdHolder);
        // Toast.makeText(getContext(), "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(NursingStandardPlanActivity.this, "Payment failed: " + i + " " + s,  Toast.LENGTH_SHORT).show();
    }
    public void Http(String email,String paymentmode,String Id) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(NursingStandardPlanActivity.this, "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                pDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    Toast.makeText(NursingStandardPlanActivity.this, "You are registered Successfully\nPlease login to access your account", Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(NursingStandardPlanActivity.this, NursingActivityLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(NursingStandardPlanActivity.this,httpResponseMsg.toString() , Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("email", params[0]);
                ResultHash.put("paymentmode", params[1]);
                ResultHash.put("id",params[2]);
                Log.e("value",""+ResultHash);
                ParseResult = httpParse.postRequest(ResultHash, HttpURL1);


                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(email,paymentmode,Id);

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

