package com.oxygen.micro.ayulr;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText txtmob;
    Button btnLogin;
    private EditText editTextConfirmOtp;
    private AppCompatButton buttonConfirm;
    private RequestQueue requestQueue;
    String mobile;
    Boolean CheckEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        txtmob = (EditText)findViewById(R.id.mobile_no_et);
        requestQueue = Volley.newRequestQueue(this);
        btnLogin = (Button)findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if (NetworkDetactor.isNetworkAvailable(ForgotPasswordActivity.this)) {
                if (CheckEditText) {
                    register();
                    confirmOtp();
                }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void confirmOtp()  {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                final ProgressDialog loading = ProgressDialog.show(ForgotPasswordActivity.this, "Authenticating", "Please wait while we check the entered code", false,false);

                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();

                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRMFORGOT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //if the server response is success
                                if(response.equalsIgnoreCase("success")){
                                    //dismissing the progressbar
                                    loading.dismiss();

                                    //Starting a new activity
                                    Intent intent = new Intent(ForgotPasswordActivity.this, ChangePasswordActivity.class);
                                    intent.putExtra("mobile",mobile);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    loading.dismiss();
                                    //Displaying a toast if the otp entered is wrong
                                    Toast.makeText(ForgotPasswordActivity.this,"Wrong OTP Please Try Again",Toast.LENGTH_LONG).show();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                                Toast.makeText(ForgotPasswordActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put(Config.KEY_OTP, otp);
                        params.put(Config.KEY_PHONE, mobile);
                        return params;
                    }
                };

                //Adding the request to the queue
                requestQueue.add(stringRequest);
            }
        });
    }
    public void CheckEditTextIsEmptyOrNot(){
        mobile = txtmob.getText().toString().trim();

        if (mobile.equals("")){
            if (mobile.equals("")){
                txtmob.requestFocus();
                txtmob.setError("Field Require");
            }
            CheckEditText=false;
        }else{
            CheckEditText=true;
        }
    }
    private void register() {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);


        //Getting user data


        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Forgot_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);

                            //If it is success
                            if(jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")){
                                //Asking user to confirm otp
                                // confirmOtp();
                            }else{
                                //If not successful user may already have registered
                                Toast.makeText(ForgotPasswordActivity.this, "Username or Phone number already registered", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                if (CheckEditText) {
                    params.put(Config.KEY_PHONE, mobile);

                }
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

}

