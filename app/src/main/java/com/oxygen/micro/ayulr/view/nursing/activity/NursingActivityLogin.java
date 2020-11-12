package com.oxygen.micro.ayulr.view.nursing.activity;

import android.content.Intent;
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

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.connection.RequestHandler;
import com.oxygen.micro.ayulr.constant.SharedPrefManagernur;
import com.oxygen.micro.ayulr.constant.URLs;
import com.oxygen.micro.ayulr.view.activity.ForgotPasswordActivity;
import com.oxygen.micro.ayulr.view.nursing.model.Nur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NursingActivityLogin extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.buttonRegister)
    Button btnreg;
    @BindView(R.id.buttonLogin)
    Button btnlogin;
    @BindView(R.id.forgot_password)
    TextView forgot;
    @BindView(R.id.editemail)
    EditText etuser;
    @BindView(R.id.editMob)
    EditText etmob;
    private static CheckBox show_hide_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_login);
        if (SharedPrefManagernur.getInstance(this).isLoggedInNur()) {
            finish();
            startActivity(new Intent(this, NursingDashboard.class));
            return;
        }
        initViews();

    }

    private void initViews() {
        ButterKnife.bind(this);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);

        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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

        btnreg.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        forgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnreg) {
            Intent in = new Intent(NursingActivityLogin.this, NursingRegisterActivity.class);
            startActivity(in);
        }
        if (v == btnlogin) {
            if (NetworkDetactor.isNetworkAvailable(NursingActivityLogin.this)) {
                userLogin();
            } else {
                Toast.makeText(NursingActivityLogin.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == forgot) {
            Intent in = new Intent(NursingActivityLogin.this, ForgotPasswordActivity.class);
            startActivity(in);
        }
    }


    private void userLogin() {
        //first getting the values
        final String email = etuser.getText().toString();
        final String password = etmob.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(email) && (email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
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
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        Nur nur = new Nur(

                                userJson.getInt("id"),
                                userJson.getString("name"),
                                userJson.getString("email"),
                                userJson.getString("mobile")
                        );

                        //storing the user in shared preferences
                        SharedPrefManagernur.getInstance(getApplicationContext()).userLoginNur(nur);
                        startActivity(new Intent(getApplicationContext(), NursingDashboard.class));
                        finish();
                    } else {
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
                return requestHandler.sendPostRequest(URLs.NURURL_LOGIN, params);
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
