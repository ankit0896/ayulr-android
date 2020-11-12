package com.oxygen.micro.ayulr.view.paramedical.activity;

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

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.SharedPrefManagerpara;
import com.oxygen.micro.ayulr.view.paramedical.model.Para;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParaNewPostActivity extends AppCompatActivity implements View.OnClickListener {
    public SharedPreferences settings;
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    @BindView(R.id.new_post_image)
    ImageView imageView;
    @BindView(R.id.new_post_desc)
    EditText editTextdes;
    @BindView(R.id.post_btn)
    Button buttonpost;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    int userId;
    String UserName;
    private static final int Result_Load_Image = 1;
    private Bitmap bitmap;
    String Description, ImageHolder;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://ameygraphics.com/ayulr/api/insert_blog.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_new_post);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Para para = SharedPrefManagerpara.getInstance(this).getUserPara();
        //setting the values to the textviews
        userId = para.getParaId();
        UserName = para.getParaName();
        settings = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        buttonpost.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==buttonpost){
            Description = editTextdes.getText().toString();
            if (NetworkDetactor.isNetworkAvailable(ParaNewPostActivity.this)) {
                NewPost(String.valueOf(userId), UserName, ImageHolder, Description);
            } else {
                Toast.makeText(ParaNewPostActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }
        if(v==imageView){
            switch (v.getId()) {
                case R.id.new_post_image:
                    Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryintent.setType("image/*");
                    startActivityForResult(galleryintent, Result_Load_Image);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Result_Load_Image && resultCode == RESULT_OK && data != null) {
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
        } else {
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

    public void NewPost(String id, String username, String image, String desc) {


        class NewPostClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ParaNewPostActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    Toast.makeText(ParaNewPostActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent newPostIntent = new Intent(ParaNewPostActivity.this, ParaForumActivity.class);
                    startActivity(newPostIntent);
                    finish();
                } else {
                    Toast.makeText(ParaNewPostActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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

        newPostClass.execute(id, username, image, desc);
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
