package com.oxygen.micro.ayulr.view.paramedical.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.constant.SharedPrefManagerpara;
import com.oxygen.micro.ayulr.view.paramedical.model.Para;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParaViewProfessionalActivity extends Fragment implements View.OnClickListener {
    @BindView(R.id.drcategory)
    TextView etcategory;
    @BindView(R.id.drdegree)
    TextView etdegree;
    @BindView(R.id.drexp)
    TextView etexperience;
    @BindView(R.id.drwork)
    TextView etwork;
    @BindView(R.id.drnat)
    TextView etnature;
    @BindView(R.id.drexpe)
    TextView etexpert;
    @BindView(R.id.dralso)
    TextView etalsotreat;
    @BindView(R.id.drhomevisitfee)
    TextView ethomevisitfee;
    @BindView(R.id.drvalid)
    TextView etvalid;
    @BindView(R.id.homevisit)
    TextView ethomevisit;
//    @BindView(R.id.radio)
//    RadioButton rdyes;
//    @BindView(R.id.radio1)
//    RadioButton rdno;
    @BindView(R.id.btnprofessional)
    Button buttonprofessional;
    String IdHolder, CategoryHolder, NatureHolder, WorkHolder, ExpertHolder, AlsotreatHolder,
            ExperienceHolder, ValidHolder, HomevisitHolder, EmailHolder, HomevisitfeeHolder, DegreeHolder;
    ProgressDialog pDialog;
    String HttpURL1 = Config.BASEURL + "filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_para_view_professional, container, false);
        init();
        return v;
    }

    private void init() {
        ButterKnife.bind(this, v);
        Para para = SharedPrefManagerpara.getInstance(getContext()).getUserPara();
        IdHolder = String.valueOf(para.getParaId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(getContext())) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
        }

        buttonprofessional.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == buttonprofessional) {
            Intent intent = new Intent(getContext(), ParaUpdateProfessionalActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //  pDialog = ProgressDialog.show(getContext(), "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                //    pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                s = httpResponseMsg;
                new GetHttpResponse(getContext()).execute();
                //Parsing the Stored JSOn String to GetHttpResponse Method.
            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("id", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL1);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
        // SendHttpRequestTask myAsync=new SendHttpRequestTask();
        // myAsync.execute(PreviousListViewClickedItem);
    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        public Context context;

        public GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Bitmap bmp;

            try {
                if (s != null) {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(i);
                            // Storing Student Name, Phone Number, Class into Variables.
                            CategoryHolder = jobj.getString("category");
                            DegreeHolder = jobj.getString("degree");
                            ExperienceHolder = jobj.getString("experience");
                            WorkHolder = jobj.getString("work_in");
                            NatureHolder = jobj.getString("job_nature");
                            ExpertHolder = jobj.getString("expertise");
                            AlsotreatHolder = jobj.getString("also_treat");
                            HomevisitHolder = jobj.getString("home_visit");
                            HomevisitfeeHolder = jobj.getString("home_visit_fee");
                            ValidHolder = jobj.getString("h_visit_for");

                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Check your connection and try again", Toast.LENGTH_SHORT).show();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (CategoryHolder.equals("null") || (CategoryHolder.equals(""))) {
                etcategory.setText("`Not Selected`");
            } else {
                etcategory.setText(CategoryHolder);
            }
            if (DegreeHolder.equals("null") || (DegreeHolder.equals(""))) {
                etdegree.setText("`Not Selected`");
            } else {
                etdegree.setText(DegreeHolder);
            }
            if (ExperienceHolder.equals("null") || (ExperienceHolder.equals(""))) {
                etexperience.setText("`Not Selected`");
            } else {
                etexperience.setText(ExperienceHolder);
            }
            if (WorkHolder.equals("null") || (WorkHolder.equals(""))) {
                etwork.setText("`Not Selected`");
            } else {
                etwork.setText(WorkHolder);
            }
            if (NatureHolder.equals("null") || (NatureHolder.equals(""))) {
                etnature.setText("`Not Selected`");
            } else {
                etnature.setText(NatureHolder);
            }
            if (ExpertHolder.equals("null") || (ExpertHolder.equals(""))) {
                etexpert.setText("`Not Selected`");
            } else {
                etexpert.setText(ExpertHolder);
            }
            if (AlsotreatHolder.equals("null") || (AlsotreatHolder.equals(""))) {
                etalsotreat.setText("`Not Selected`");
            } else {
                etalsotreat.setText(AlsotreatHolder);
            }

            // radio button ki value ko get karne ka code
            if (HomevisitHolder.equals("null") || (HomevisitHolder.equals(""))) {
                ethomevisit.setText("`Not Selected`");
            } else {
                ethomevisit.setText(HomevisitHolder);
            }
            if (HomevisitfeeHolder.equals("null") || (HomevisitfeeHolder.equals(""))) {
                ethomevisitfee.setText("`Not Selected`");
            } else {
                ethomevisitfee.setText(HomevisitfeeHolder);
            }
            if (ValidHolder.equals("null") || (ValidHolder.equals(""))) {
                etvalid.setText("`Not Selected`");
            } else {
                etvalid.setText(ValidHolder);
            }
            // Setting Student Name, Phone Number, Class into TextView after done all process .
            // radio button ki value ko get karne ka code
        }
    }
}