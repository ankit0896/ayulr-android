package com.oxygen.micro.ayulr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class AddVideoActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.edtname)
    TextInputEditText etname;
    @BindView(R.id.edtlink)
    TextInputEditText etlink;
    @BindView(R.id.edtdesig)
    TextInputEditText etdesig;
    @BindView(R.id.imageView)
    ImageView imageView;
    String userid,name,designation,image,username,userdisg,userlink,userimage;
    String se;
    ProgressDialog pDialog;
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://ameygraphics.com/ayulr/api/add_videos.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String ImageHolder;
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    Boolean CheckEditText;
    @BindView(R.id.btnadd)
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageHolder="Noimage.jpeg";
        User user = SharedPrefManager.getInstance(this).getUser();
        userid = String.valueOf(user.getId());
        button.setOnClickListener(this);
        imageView.setOnClickListener(this);
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




    public void GetDataFromEditText() {
        username=etname.getText().toString();
        userdisg=etdesig.getText().toString();
        userlink=etlink.getText().toString();
        if (username.equals("")||userdisg.equals("")||userlink.equals("")){
            if (username.equals("")){
                etname.requestFocus();
                etname.setError("field required");
            }if (userdisg.equals("")){
                etdesig.requestFocus();
                etdesig.setError("field required");
            }
            if (userlink.equals("")){
                etlink.requestFocus();
                etlink.setError("field required");
            }
            CheckEditText = false;
        } else {
            CheckEditText = true;
        }

    }

    public void RecordUpdate(final String username,final String userdesig, final String userlink, final String userimage,final String userid){

        class RecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(AddVideoActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();
                if (httpResponseMsg.equals("your video successfully added")) {
                    Toast.makeText(AddVideoActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddVideoActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                }


            }
            @Override
            protected String doInBackground(String... params) {
                if (CheckEditText) {


                    hashMap.put("username", params[0]);

                    hashMap.put("userdes", params[1]);

                    hashMap.put("userlink", params[2]);

                    hashMap.put("userimage", params[3]);

                    hashMap.put("userid", params[4]);


                }
                Log.e("some value=", " " +hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);


                return finalResult;
            }
        }

        RecordUpdateClass RecordUpdateClass = new RecordUpdateClass();

        RecordUpdateClass.execute(username,userdesig,userlink,userimage,userid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v==button){
            GetDataFromEditText();
            if (CheckEditText) {
                if (NetworkDetactor.isNetworkAvailable(AddVideoActivity.this)) {
                    RecordUpdate(username,userdisg,userlink,ImageHolder,userid);

                } else {
                    Toast.makeText(AddVideoActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }

            }
        }
        if (v==imageView){
            Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryintent.setType("image/*");
            startActivityForResult(galleryintent, Result_Load_Image);
        }
    }
}



