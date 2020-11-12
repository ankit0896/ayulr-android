package com.oxygen.micro.ayulr.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.model.User;
import com.oxygen.micro.ayulr.constant.SharedPrefManager;
import com.oxygen.micro.ayulr.view.adapter.BlogAdapter;
import com.oxygen.micro.ayulr.model.Blog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private BlogAdapter adapter;
    private List<Blog> blogList;
    HttpParse httpParse = new HttpParse();
    String HttpUR = "https://ameygraphics.com/ayulr/api/filter_testimonial.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String se;
    ProgressDialog pDialog;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView=(TextView)findViewById(R.id.addblog);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForumActivity.this, NewPostActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        User user = SharedPrefManager.getInstance(this).getUser();
        userid = String.valueOf(user.getId());

        blogList = new ArrayList<>();
        //  cd=new ConnectionDetector(this);
        if (NetworkDetactor.isNetworkAvailable(ForumActivity.this)) {
            HttpWeb(userid);
        } else {
            Toast.makeText(ForumActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }


    }




    public void HttpWeb(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ForumActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                se = httpResponseMsg;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                MyAsync myAsync = new MyAsync();
                myAsync.execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("id", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpUR);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);

    }
    class MyAsync extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ForumActivity.this, "loading", "please wait");
        }

        @Override
        protected String doInBackground(Void... params) {
            String server_url ="https://ameygraphics.com/ayulr/api/fetchforum.php";

            // String server_url = "http://www.w3schools.com/xml/guestbook.asp";
            try {
                URL url = new URL(server_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                String result = getStringFromInputStream(inputStream);

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            try {
                if (se != null) {
                    JSONArray jsonArray = null;
                    try {
                        JSONArray jsonarray = new JSONArray(se);

                        for(int i=0;i<jsonarray.length();i++){
                            JSONObject jobj = jsonarray.getJSONObject(i);

                            Blog data = new Blog(
                                    jobj.getString("b_id"),
                                    jobj.getString("username"),
                                    jobj.getString("blog_img"),
                                    jobj.getString("description")


                            );
                            blogList.add(data);
                            Log.e("some value=", " " + blogList);
                            adapter = new BlogAdapter(ForumActivity.this, blogList);
                            recyclerView.setAdapter(adapter);

                        }
                    } catch (Exception e) {
                        Toast.makeText(ForumActivity.this, "No Testimonial", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(ForumActivity.this, "No Testimonial", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id== android.R.id.home){
            onBackPressed();
            return true;
        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}










