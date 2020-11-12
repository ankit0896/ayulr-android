package com.oxygen.micro.ayulr;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.chaos.view.PinView;
import com.oxygen.micro.ayulr.doctor.activity.ActivityUserRegister;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NursingRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.editemail)
    EditText editTextEmail;
    @BindView(R.id.editname)
    EditText editTextUsername;
    @BindView(R.id.editpass)
    EditText editTextPassword;
    @BindView(R.id.editmob)
    EditText editTextPhone;
    PinView editTextConfirmOtp;
    @BindView(R.id.buttonRegister)
    AppCompatButton buttonRegister;
    RequestQueue requestQueue;

    private String email;
    private String username;
    private String password;
    private String phone;
    Boolean CheckEditText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    TextView textU;
    TextView txtmobile;
    CardView buttonConfirm;
    PinView pinView;
    String OtpHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_register);
        if (SharedPrefManagernur.getInstance(this).isLoggedInNur()) {
            finish();
            startActivity(new Intent(this, NursingDashboard.class));
            return;
        }
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
        buttonRegister.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        if(v==buttonRegister){
            CheckEditTextIsEmptyOrNot();
            if (NetworkDetactor.isNetworkAvailable(NursingRegisterActivity.this)) {
                if (CheckEditText) {
                    register();
                } else {
                    Toast.makeText(NursingRegisterActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(NursingRegisterActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmOtp(final String otphold) {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = confirmDialog.findViewById(R.id.buttonConfirm);
        pinView = confirmDialog.findViewById(R.id.pinView);
        textU = confirmDialog.findViewById(R.id.textView_noti);
        txtmobile = confirmDialog.findViewById(R.id.txtmobile);
        txtmobile.setText("Sent via SMS to "+phone);
        pinView.setText(otphold);

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
                String OTP = pinView.getText().toString();
                if (!(OTP.equals(otphold))) {
                    pinView.setLineColor(Color.RED);
                    textU.setText("Incorrect OTP");
                    textU.setTextColor(Color.RED);
                } else {
                    alertDialog.dismiss();

                    //Displaying a progressbar
                    final ProgressDialog loading = ProgressDialog.show(NursingRegisterActivity.this, "Authenticating", "Please wait while we check the entered code", false, false);

                    //Creating an string request
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.NURCONFIRM_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Toast.makeText(ActivityUserRegister.this, ""+response, Toast.LENGTH_SHORT).show();
                                    if (response.equalsIgnoreCase("success")) {
                                        loading.dismiss();
                                        Intent in = new Intent(NursingRegisterActivity.this, NursingPersonalActivity.class);
                                        in.putExtra("name", username);
                                        in.putExtra("email", email);
                                        in.putExtra("mob", phone);
                                        startActivity(in);
                                        finish();
                                        Toast.makeText(NursingRegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loading.dismiss();
                                        //Displaying a toast if the otp entered is wrong
                                        Toast.makeText(NursingRegisterActivity.this, "Wrong OTP Please Try Again", Toast.LENGTH_LONG).show();

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    alertDialog.dismiss();
                                    Toast.makeText(NursingRegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(Config.KEY_OTP, otphold);
                            params.put(Config.KEY_EMAIL, email);
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                }
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot() {
        username = editTextUsername.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();

        if (username.equals("") || password.equals("") || phone.equals("") || (email.equals("")) || (!(email.matches(emailPattern)))) {
            if (username.equals("")) {
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
            if (password.equals("")) {
                editTextPassword.requestFocus();
                editTextPassword.setError("Field Required");
            }
            if (phone.equals("")) {
                editTextPhone.requestFocus();
                editTextPhone.setError("Field Required");
            }
            CheckEditText = false;
        } else {
            CheckEditText = true;
        }

    }


    private void register() {


        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.NURREGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String code=jsonObject.getString("CODE");
                            if (code.equals("200")) {
                                OtpHolder=jsonObject.getString("otp");
                                String message=jsonObject.getString("message");
                                loading.dismiss();
                                confirmOtp(OtpHolder);

                            } else {
                                loading.dismiss();
                                //If not successful user may already have registered
                                Toast.makeText(NursingRegisterActivity.this, ""+response, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(NursingRegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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