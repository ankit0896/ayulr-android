package com.oxygen.micro.ayulr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class NewPostActivity extends AppCompatActivity {
    public SharedPreferences settings;
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    ImageView imageView;
    EditText editTextdes;
    Button buttonpost;
    int userId;
    String UserName;
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    String Description,ImageHolder;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://ameygraphics.com/ayulr/api/insert_blog.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        User user = SharedPrefManager.getInstance(this).getUser();
        //setting the values to the textviews
        userId=user.getId();
        UserName=user.getName();
        settings = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editTextdes = (EditText) findViewById(R.id.new_post_desc);
        imageView = (ImageView) findViewById(R.id.new_post_image);
        buttonpost=(Button)findViewById(R.id.post_btn) ;
        buttonpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Description=editTextdes.getText().toString();
                if (NetworkDetactor.isNetworkAvailable(NewPostActivity.this)) {
                NewPost(String.valueOf(userId),UserName,ImageHolder,Description);
                } else {
                    Toast.makeText(NewPostActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.new_post_image:
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

        if (requestCode == Result_Load_Image &&resultCode ==RESULT_OK && data !=null) {
            Uri filePath = data.getData();

            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting the Bitmap to ImageView
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

        bmp.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        return ConvertImage;

    }
    public void NewPost(String id,String username,String image,String desc) {


        class NewPostClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(NewPostActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
if (httpResponseMsg.equals("success")) {
    Toast.makeText(NewPostActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
    Intent newPostIntent = new Intent(NewPostActivity.this, ForumActivity.class);
    startActivity(newPostIntent);
    finish();
}else{
    Toast.makeText(NewPostActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
}
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("id", params[0]);
                hashMap.put("username", params[1]);
                hashMap.put("image", params[2]);
                hashMap.put("desc", params[3]);
                Log.e("some value=", " " + hashMap);

                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        NewPostClass newPostClass = new NewPostClass();

        newPostClass.execute(id,username,image,desc);
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
