package com.oxygen.micro.ayulr.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.IdRes;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.view.activity.ViewMedicatedEquipmentActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MedicateEquipmentActivity extends AppCompatActivity {
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.radio1)
    RadioButton rdmale;
    @BindView(R.id.radio2)
    RadioButton rdfemale;
    @BindView(R.id.radiogroup1)
    RadioGroup rdgroupe;
    @BindView(R.id.radio3)
    RadioButton rddonate;
    @BindView(R.id.radio4)
    RadioButton rdrent;
    @BindView(R.id.radio5)
    RadioButton rdsale;
    @BindView(R.id.radio6)
    RadioButton rdexchange;
    @BindView(R.id.name)
    TextInputEditText etname;
    @BindView(R.id.email)
    TextInputEditText etemail;
    @BindView(R.id.contact)
    TextInputEditText etcontact;
    @BindView(R.id.age)
    TextInputEditText etage;
    @BindView(R.id.des)
    TextInputEditText etdes;
    @BindView(R.id.address)
    TextInputEditText etresidence;
    @BindView(R.id.price)
    TextInputEditText etprice;
    @BindView(R.id.btnpersonal)
    Button buttonpersonal;
    String ImageHolder, NameHolder,EmailHolder,ContactHolder,ResidenceHolder,AgeHolder,GenderHolder,EquipmentHolder,PriceHolder,DescriptionHolder;
    Boolean CheckEditText;
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicate_equipment);
        initview();
    }
    private void initview(){
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        ImageHolder="Noimage.jpeg";
        rdgroupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId==R.id.radio3){
                    etprice.setVisibility(View.GONE);
                }else if (checkedId==R.id.radio4){
                    etprice.setVisibility(View.VISIBLE);
                }else if (checkedId==R.id.radio5){
                    etprice.setVisibility(View.VISIBLE);
                }else if (checkedId==R.id.radio6){
                    etprice.setVisibility(View.GONE);
                }
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
                if (NetworkDetactor.isNetworkAvailable(MedicateEquipmentActivity.this)) {
                    if (CheckEditText) {
                        register();
                    } else {
                        // Toast.makeText(PatientActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MedicateEquipmentActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
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
        PriceHolder = etprice.getText().toString();
        DescriptionHolder = etdes.getText().toString();
        ResidenceHolder = etresidence.getText().toString();
        GenderHolder = "";
        if (rdmale.isChecked())
            GenderHolder = "male";
        if (rdfemale.isChecked())
            GenderHolder = "female";
        EquipmentHolder = "";
        if (rddonate.isChecked())
            EquipmentHolder = "Donate";
        if (rdrent.isChecked())
            EquipmentHolder = "Rent";
        if (rdsale.isChecked())
            EquipmentHolder = "Sale";
        if (rdexchange.isChecked())
            EquipmentHolder = "Exchange";
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
    private void register() {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);


        //Getting user data


        //Again creating the string request
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, Config.EQUIPMENTDONOR_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        String res=response.toString().trim();
                        Log.e("res",""+res);
                        if (res.equalsIgnoreCase("success")) {
                            loading.dismiss();
                            Intent intent=new Intent(MedicateEquipmentActivity.this, ViewMedicatedEquipmentActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Toast.makeText(MedicateEquipmentActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            // finish();
                        } else {
                            loading.dismiss();
                            //If not successful user may already have registered
                            Toast.makeText(MedicateEquipmentActivity.this, ""+response, Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(MedicateEquipmentActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
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
                    params.put("e_for", EquipmentHolder);
                    params.put("contact", ContactHolder);
                    params.put("address", ResidenceHolder);
                    params.put("price", PriceHolder);
                    params.put("e_desc", DescriptionHolder);
                    params.put("e_image", ImageHolder);

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
