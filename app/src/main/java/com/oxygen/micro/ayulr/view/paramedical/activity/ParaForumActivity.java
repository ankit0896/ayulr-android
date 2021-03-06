package com.oxygen.micro.ayulr.view.paramedical.activity;

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

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.constant.SharedPrefManagerpara;
import com.oxygen.micro.ayulr.view.paramedical.adapter.ParaBlogAdapter;
import com.oxygen.micro.ayulr.view.paramedical.model.Para;
import com.oxygen.micro.ayulr.view.paramedical.model.ParaBlog;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParaForumActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.addblog)
    TextView textView;
    private ParaBlogAdapter adapter;
    private List<ParaBlog> parablogList;
    HttpParse httpParse = new HttpParse();
    String HttpUR = Config.BASEURL + "filter_testimonial.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String se;
    ProgressDialog pDialog;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Para para = SharedPrefManagerpara.getInstance(this).getUserPara();
        userid = String.valueOf(para.getParaId());

        parablogList = new ArrayList<>();
        //  cd=new ConnectionDetector(this);
        if (NetworkDetactor.isNetworkAvailable(ParaForumActivity.this)) {
            HttpWeb(userid);
        } else {
            Toast.makeText(ParaForumActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }

        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == textView) {
            Intent intent = new Intent(ParaForumActivity.this, ParaNewPostActivity.class);
            startActivity(intent);
        }
    }


    public void HttpWeb(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ParaForumActivity.this, "Loading Data", null, true, true);
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

            progressDialog = ProgressDialog.show(ParaForumActivity.this, "loading", "please wait");
        }

        @Override
        protected String doInBackground(Void... params) {
            String server_url = Config.BASEURL + "fetchforum.php";

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

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jobj = jsonarray.getJSONObject(i);

                            ParaBlog data = new ParaBlog(
                                    jobj.getString("b_id"),
                                    jobj.getString("username"),
                                    jobj.getString("blog_img"),
                                    jobj.getString("description")


                            );
                            parablogList.add(data);
                            Log.e("some value=", " " + parablogList);
                            adapter = new ParaBlogAdapter(ParaForumActivity.this, parablogList);
                            recyclerView.setAdapter(adapter);

                        }
                    } catch (Exception e) {
                        Toast.makeText(ParaForumActivity.this, "No Testimonial", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(ParaForumActivity.this, "No Testimonial", Toast.LENGTH_SHORT).show();
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
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}











