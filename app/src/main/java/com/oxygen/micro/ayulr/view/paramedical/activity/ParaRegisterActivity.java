package com.oxygen.micro.ayulr.view.paramedical.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.constant.SharedPrefManagerpara;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParaRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.editemail)
    EditText editTextEmail;
    @BindView(R.id.editname)
    EditText editTextUsername;
    @BindView(R.id.editpass)
    EditText editTextPassword;
    @BindView(R.id.editmob)
    EditText editTextPhone;

    @BindView(R.id.buttonRegister)
    AppCompatButton buttonRegister;

    AppCompatButton buttonConfirm;
    @BindView(R.id.spin)
    Spinner spinner;
    PinView editTextConfirmOtp;

    private RequestQueue requestQueue;
    private String email;
    private String username;
    private String password;
    private String phone;
    Boolean CheckEditText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String Designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_register);
        if (SharedPrefManagerpara.getInstance(this).isLoggedInPara()) {
            finish();
            startActivity(new Intent(this, ParaDashboard.class));
            return;
        }
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        buttonRegister.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.desig_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Designation = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==buttonRegister){
            CheckEditTextIsEmptyOrNot();
            if (NetworkDetactor.isNetworkAvailable(ParaRegisterActivity.this)) {
                if (CheckEditText) {
                    register();
                } else {
                    Toast.makeText(ParaRegisterActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ParaRegisterActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmOtp(String otp, final String uid) {

        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = confirmDialog.findViewById(R.id.pinView);
        editTextConfirmOtp.setText(otp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Mobile Verification.");

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
                    final ProgressDialog loading = ProgressDialog.show(ParaRegisterActivity.this, "Authenticating", "Please wait while we check the entered code", false, false);


                    //Creating an string request
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PARACONFIRM_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Toast.makeText(ActivityUserRegister.this, ""+response, Toast.LENGTH_SHORT).show();
                                    if (response.equalsIgnoreCase("success")) {
                                        loading.dismiss();
                                        Intent in = new Intent(ParaRegisterActivity.this, ParaProfessionalActivity.class);
                                        in.putExtra("name", username);
                                        in.putExtra("email", email);
                                        in.putExtra("mob", phone);
                                        in.putExtra("uid",""+uid);
                                        startActivity(in);
                                        finish();
                                        Toast.makeText(ParaRegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loading.dismiss();
                                        //Displaying a toast if the otp entered is wrong
                                        Toast.makeText(ParaRegisterActivity.this, "Wrong OTP Please Try Again", Toast.LENGTH_LONG).show();

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    alertDialog.dismiss();
                                    Toast.makeText(ParaRegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
        Log.e("link", "register: " + Config.PARAREGISTER_URL);
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PARAREGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response==", "onResponse==" + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("message").equals("Sucess")) {
                                loading.dismiss();
                                confirmOtp(obj.getString("otp"),obj.getString("uid"));

                            } else {
                                loading.dismiss();
                                //If not successful user may already have registered
                                Toast.makeText(ParaRegisterActivity.this, "" + response, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ParaRegisterActivity.this, "" + response, Toast.LENGTH_LONG).show();
                            loading.dismiss();
                            e.printStackTrace();
                        }
                        //Toast.makeText(ActivityUserRegister.this, ""+response, Toast.LENGTH_LONG).show();
//                        if (response.equalsIgnoreCase("success")) {
//                            loading.dismiss();
//                            confirmOtp();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(ParaRegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_USERNAME, Designation + username);
                params.put(Config.KEY_EMAIL, email);
                params.put(Config.KEY_PASSWORD, password);
                params.put(Config.KEY_PHONE, phone);
                Log.e("link", "register: " + params);

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