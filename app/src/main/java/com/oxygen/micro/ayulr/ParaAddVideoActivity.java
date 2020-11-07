package com.oxygen.micro.ayulr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ParaAddVideoActivity extends AppCompatActivity {
    EditText etname,etlink,etdesig;
    ImageView imageView;
    String userid,name,designation,image,username,userdisg,userlink,userimage;
    String se;
    ProgressDialog pDialog;
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://ameygraphics.com/ayulr/ayulr_api/add_videos.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String ImageHolder;
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    Boolean CheckEditText;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_add_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etname=(EditText) findViewById(R.id.edtname);
        etdesig=(EditText) findViewById(R.id.edtdesig);
        etlink=(EditText) findViewById(R.id.edtlink);
        imageView=(ImageView)findViewById(R.id.imageView);
        ImageHolder="Noimage.jpeg";
        Para para = SharedPrefManagerpara.getInstance(this).getUserPara();
        userid = String.valueOf(para.getParaId());
        button=(Button)findViewById(R.id.btnadd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDataFromEditText();
                if (CheckEditText) {
                    if (NetworkDetactor.isNetworkAvailable(ParaAddVideoActivity.this)) {
                        RecordUpdate(username,userdisg,userlink,ImageHolder,userid);
                    } else {
                        Toast.makeText(ParaAddVideoActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.imageView:
                        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryintent.setType("image/*");
                        startActivityForResult(galleryintent, Result_Load_Image);
                        break;
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

                pDialog = ProgressDialog.show(ParaAddVideoActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                if (httpResponseMsg.equals("your video successfully added")) {
                    Toast.makeText(ParaAddVideoActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ParaAddVideoActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
}



