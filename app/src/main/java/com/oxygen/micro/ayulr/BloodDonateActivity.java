package com.oxygen.micro.ayulr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BloodDonateActivity extends AppCompatActivity {
    ImageView imageView;
    Spinner group_spinner;
    RadioButton rdmale,rdfemale,rdnew,rdregular;
    EditText etname,etemail,etcontact,etresidence,etage;
    Button buttonpersonal;
    String ImageHolder, NameHolder,EmailHolder,ContactHolder,ResidenceHolder,GroupHolder,AgeHolder,
            TypeHolder,GenderHolder;
    Boolean CheckEditText;
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        imageView = (ImageView) findViewById(R.id.image);
        ImageHolder="Noimage.jpeg";
        etname = (EditText) findViewById(R.id.name);
        etemail= (EditText) findViewById(R.id.email);
        etcontact = (EditText) findViewById(R.id.contact);
        etage = (EditText) findViewById(R.id.age);
        etresidence = (EditText) findViewById(R.id.address);
        group_spinner = (Spinner) findViewById(R.id.group_spinner);
        buttonpersonal = (Button) findViewById(R.id.btnpersonal);
        rdmale = (RadioButton) findViewById(R.id.radio1);
        rdfemale = (RadioButton) findViewById(R.id.radio2);
        rdnew = (RadioButton) findViewById(R.id.radio3);
        rdregular = (RadioButton) findViewById(R.id.radio4);
        GroupeAdapter groupeAdapter = new GroupeAdapter(BloodDonateActivity.this, Groupe);
        group_spinner.setAdapter(groupeAdapter);
        group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView group_TV = (TextView) view.findViewById(R.id.groupe_TV);
                GroupHolder = group_TV.getText().toString();
                }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.image:
                        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryintent.setType("image/*");
                        startActivityForResult(galleryintent, Result_Load_Image);
                        break;
                }

            }
        });
        buttonpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmptyOrNot();
                if (NetworkDetactor.isNetworkAvailable(BloodDonateActivity.this)) {
                    if (CheckEditText) {
                        if (!(GroupHolder.equals("Select Blood Group"))){
                             register();
                        }else{
                            Toast.makeText(BloodDonateActivity.this, "Select Blood Groupe", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Toast.makeText(PatientActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(BloodDonateActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Result_Load_Image &&resultCode ==RESULT_OK && data !=null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                ImageHolder = getStringImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStreamObject;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        return ConvertImage;

    }


    public void CheckEditTextIsEmptyOrNot() {

        NameHolder = etname.getText().toString();
        EmailHolder = etemail.getText().toString();
        ContactHolder = etcontact.getText().toString();
        AgeHolder = etage.getText().toString();
        ResidenceHolder = etresidence.getText().toString();
        GenderHolder = "";
        if (rdmale.isChecked())
            GenderHolder = "male";
        if (rdfemale.isChecked())
            GenderHolder = "female";
        TypeHolder = "";
        if (rdnew.isChecked())
            TypeHolder = "new";
        if (rdregular.isChecked())
            TypeHolder = "regular";
        if (NameHolder.equals("") ||ContactHolder.equals("") || (!(ContactHolder.length() == 10))
                ||(ResidenceHolder.equals(""))||(AgeHolder.equals(""))) {
            if (NameHolder.equals("")) {
                etname.requestFocus();
                etname.setError("field required");
            }
            if (ContactHolder.equals("")) {
                etcontact.requestFocus();
                etcontact.setError("field required");
            }
            if(!(ContactHolder.length() == 10)){
                etcontact.requestFocus();
                etcontact.setError("Mobile Number Must be 10 Digit");
            }
            if (ResidenceHolder.equals("")) {
                etresidence.requestFocus();
                etresidence.setError("field required");

            }
            if (AgeHolder.equals("")) {
                etage.requestFocus();
                etage.setError("field required");
            }

            CheckEditText = false;

        } else {

            CheckEditText = true;
        }

    }
    String[] Groupe = {
            "Select Blood Group",
            "A+",
            "A-",
            "B+",
            "B-",
            "AB+",
            "AB-",
            "O+",
            "O-",
    };
    private void register() {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);


        //Getting user data


        //Again creating the string request
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, Config.BLOODDONOR_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        String res=response.toString().trim();
                        Log.e("res",""+res);
                        if (res.equalsIgnoreCase("success")) {
                            loading.dismiss();
                            Intent intent=new Intent(BloodDonateActivity.this,ViewBloodDonorActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Toast.makeText(BloodDonateActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            // finish();
                        } else {
                            loading.dismiss();
                            //If not successful user may already have registered
                            Toast.makeText(BloodDonateActivity.this, ""+response, Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(BloodDonateActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                if (CheckEditText) {
                    params.put("name", NameHolder);
                    params.put("email", EmailHolder);
                    params.put("age", AgeHolder);
                    params.put("gender", GenderHolder);
                    params.put("type", TypeHolder);
                    params.put("contact", ContactHolder);
                    params.put("address", ResidenceHolder);
                    params.put("b_grp", GroupHolder);
                    params.put("photo", ImageHolder);

                }
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);
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
