package com.oxygen.micro.ayulr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity {

    Button btnreg, btnlogin;
    TextView forgot;
    EditText etuser, etmob;
    public SharedPreferences settings;
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static CheckBox show_hide_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Main2Activity.class));
            return;

        }
        btnreg = (Button) findViewById(R.id.buttonRegister);
        btnlogin = (Button) findViewById(R.id.buttonLogin);
        forgot = (TextView) findViewById(R.id.forgot_password);
        etuser = (EditText) findViewById(R.id.editemail);
        etmob = (EditText) findViewById(R.id.editMob);
        show_hide_password = (CheckBox)findViewById(R.id.show_hide_password);
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ActivityLogin.this, ActivityUserRegister.class);
                startActivity(in);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkDetactor.isNetworkAvailable(ActivityLogin.this)) {
                userLogin();
                } else {
                    Toast.makeText(ActivityLogin.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }

            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ActivityLogin.this, ForgotPasswordActivity.class);
                startActivity(in);

            }
        });
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            etmob.setInputType(InputType.TYPE_CLASS_TEXT);
                            etmob.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            etmob.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etmob.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });

    }



    private void userLogin() {
        //first getting the values
        final String email = etuser.getText().toString();
        final String password = etmob.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(email)&&(email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
            etuser.setError("Please enter your username");
            etuser.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etmob.setError("Please enter your password");
            etmob.requestFocus();
            return;
        }

        class UserLogin extends AsyncTask<Void, Void, String> {

             ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                 progressBar = (ProgressBar) findViewById(R.id.progressBar);
                  progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                   progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    try {
                        JSONObject obsj = new JSONObject(obj.getString("user"));
                        User user = new User(
                                obsj.getInt("id"),
                                obsj.getString("name"),
                                obsj.getString("email"),
                                obsj.getString("mobile")
                        );
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        overridePendingTransition(R.animator.open_main, R.animator.close_next);
    }
}

