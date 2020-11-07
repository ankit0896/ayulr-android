package com.oxygen.micro.ayulr;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
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
import java.util.HashMap;
import java.util.Map;

public class ActivityUserRegister extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private EditText editTextConfirmOtp;
    private AppCompatButton buttonRegister;
    private AppCompatButton buttonConfirm;
    private RequestQueue requestQueue;
    private String email;
    private String username;
    private String password;
    private String phone;
    Boolean CheckEditText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityuser_register);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Main2Activity.class));
            return;
        }
        editTextUsername = (EditText) findViewById(R.id.editname);
        editTextEmail = (EditText) findViewById(R.id.editemail);
        editTextPassword = (EditText) findViewById(R.id.editpass);
        editTextPhone = (EditText) findViewById(R.id.editmob);
        buttonRegister = (AppCompatButton) findViewById(R.id.buttonRegister);
        requestQueue = Volley.newRequestQueue(this);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if (NetworkDetactor.isNetworkAvailable(ActivityUserRegister.this)) {
                if (CheckEditText) {
                    register();
                    } else {
                    Toast.makeText(ActivityUserRegister.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ActivityUserRegister.this, "No Internet Available", Toast.LENGTH_SHORT).show();
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
                final String otp = editTextConfirmOtp.getText().toString().trim();
                if (otp.equals("")) {
                    editTextConfirmOtp.requestFocus();
                    editTextConfirmOtp.setError("please enter otp you received");
                } else {
                    alertDialog.dismiss();

                    //Displaying a progressbar
                    final ProgressDialog loading = ProgressDialog.show(ActivityUserRegister.this, "Authenticating", "Please wait while we check the entered code", false, false);




                    //Creating an string request
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRM_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                   // Toast.makeText(ActivityUserRegister.this, ""+response, Toast.LENGTH_SHORT).show();
                                    if (response.equalsIgnoreCase("success")) {
                                        loading.dismiss();
                                        Intent in=new Intent(ActivityUserRegister.this,ProfessionalActivity.class);
                                        in.putExtra("name",username);
                                        in.putExtra("email",email);
                                        in.putExtra("mob",phone);
                                        startActivity(in);
                                        finish();
                                        Toast.makeText(ActivityUserRegister.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loading.dismiss();
                                        //Displaying a toast if the otp entered is wrong
                                        Toast.makeText(ActivityUserRegister.this, "Wrong OTP Please Try Again", Toast.LENGTH_LONG).show();

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    alertDialog.dismiss();
                                    Toast.makeText(ActivityUserRegister.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(Config.KEY_OTP, otp);
                            params.put(Config.KEY_EMAIL, email);
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                }
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot(){
        username = editTextUsername.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();

        if (username.equals("")||password.equals("")||phone.equals("")||(email.equals(""))||(!(email.matches(emailPattern)))){
            if (username.equals("")){
                editTextUsername.requestFocus();
                editTextUsername.setError("Field Require");
            }
            if (email.equals("")) {
                editTextEmail.requestFocus();
                editTextEmail.setError("Field Require");
            }
            if (!(email.matches(emailPattern))) {
                editTextEmail.requestFocus();
                editTextEmail.setError("Please enter your valid email");
            }
            if (password.equals("")){
                editTextPassword.requestFocus();
                editTextPassword.setError("Field Required");
            }if (phone.equals("")){
                editTextPhone.requestFocus();
                editTextPhone.setError("Field Required");
            }
            CheckEditText = false;
        } else {
            CheckEditText = true;
        }

    }


    private void register() {


        final ProgressDialog loading = ProgressDialog.show(this, "Registering","Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ActivityUserRegister.this, ""+response, Toast.LENGTH_LONG).show();
                        if (response.equalsIgnoreCase("success")) {

                            loading.dismiss();
                            confirmOtp();


                        } else {
                            loading.dismiss();
                            //If not successful user may already have registered
                            Toast.makeText(ActivityUserRegister.this, ""+response, Toast.LENGTH_LONG).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(ActivityUserRegister.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request

                    params.put(Config.KEY_USERNAME, username);
                    params.put(Config.KEY_EMAIL, email);
                    params.put(Config.KEY_PASSWORD, password);
                    params.put(Config.KEY_PHONE, phone);

                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        overridePendingTransition(R.animator.open_main, R.animator.close_next);
    }

}