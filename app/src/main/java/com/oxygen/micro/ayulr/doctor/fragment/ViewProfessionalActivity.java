package com.oxygen.micro.ayulr.doctor.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.constant.SharedPrefManager;
import com.oxygen.micro.ayulr.model.User;
import com.oxygen.micro.ayulr.view.activity.UpdateProfessionalActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by MICRO on 8/1/2018.
 */

public class ViewProfessionalActivity  extends Fragment implements View.OnClickListener {
    @BindView(R.id.drcategory)
    TextView etcategory;
    @BindView(R.id.drdegree)
    TextView etdegree;
    @BindView(R.id.drrgnumber)
    TextView etrgnumber;
    @BindView(R.id.drspeciality)
    TextView etspeciality;
    @BindView(R.id.drotherespeciality)
    TextView etotherespeciality;
    @BindView(R.id.drspecialtreat)
    TextView etdisease;
    @BindView(R.id.drotherediseases)
    TextView etotherediseases;
    @BindView(R.id.drwrkhosptl)
    TextView etworkhospital;
    @BindView(R.id.drexperience)
    TextView etexperience;
    @BindView(R.id.drspecialfee)
    TextView etspecialfee;
    @BindView(R.id.drspecialvalidfor)
    TextView etspecialvalidfor;
    @BindView(R.id.drsplhealthpkg)
    TextView etspecialhealthpkg;
    @BindView(R.id.drsplhealthpkgrs)
    TextView etspecialhealthday;
    @BindView(R.id.homevisit)
    TextView ethomevisit;
    @BindView(R.id.drhomevisitfee)
    TextView ethomevisitfee;
    @BindView(R.id.dreveryvisit)
    TextView eteveryvisit;
    @BindView(R.id.btnprofessional)
    Button buttonprofessional;
    String IdHolder,CategoryHolder, DegreeHolder,RgnumberHolder,SpecialityHolder,OthereSpecialityHolder,DiseaseHolder,OthereDiseasesHolder,
            WorkHospitalHolder,ExperienceHolder,SpecialfeeHolder,SpecialValidforHolder,SpecialhealthpkgHolder,SpecialhealthpkgdayHolder,
            EveryvisitHolder,HomevisitHolder,EmailHolder,HomevisitfeeHolder;
    String HttpURL1 = Config.BASEURL+"filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_view_professional, container, false);
        ButterKnife.bind(this,v);
        initview();
        return v;
    }
    private void initview(){
        buttonprofessional.setOnClickListener(this);
        User user = SharedPrefManager.getInstance(getContext()).getUser();
        IdHolder = String.valueOf(user.getId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(getContext())) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
        }
    }
    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

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
    }

    @Override
    public void onClick(View v) {
        if (v==buttonprofessional){
            Intent intent=new Intent(getContext(), UpdateProfessionalActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
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
                            RgnumberHolder= jobj.getString("reg_no");
                            SpecialityHolder = jobj.getString("speciality");
                            OthereSpecialityHolder= jobj.getString("other_speciality");
                            DiseaseHolder = jobj.getString("diseases");
                            OthereDiseasesHolder = jobj.getString("other_dise");
                            WorkHospitalHolder= jobj.getString("work_in");
                            ExperienceHolder = jobj.getString("clinic_exper");
                            SpecialfeeHolder= jobj.getString("special_fee");
                            SpecialValidforHolder = jobj.getString("svalid_for");
                            SpecialhealthpkgHolder= jobj.getString("special_package");
                            SpecialhealthpkgdayHolder = jobj.getString("valid_for");
                            HomevisitHolder= jobj.getString("home_visit");
                            HomevisitfeeHolder = jobj.getString("home_visit_fee");
                            EveryvisitHolder = jobj.getString("h_visit_for");

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
            if (CategoryHolder==null ||(CategoryHolder.equals(""))){
                etcategory.setText("`Not Selected`");
            }else {
                etcategory.setText(CategoryHolder);
            }
            if (DegreeHolder==null ||(DegreeHolder.equals(""))){
                etdegree.setText("`Not Selected`");
            }else {
                etdegree.setText(DegreeHolder);
            }
            if (RgnumberHolder==null ||(RgnumberHolder.equals(""))){
                etrgnumber.setText("`Not Selected`");
            }else {
                etrgnumber.setText(RgnumberHolder);
            }
            if (SpecialityHolder==null ||(SpecialityHolder.equals(""))){
                etspeciality.setText("`Not Selected`");
            }else{
                etspeciality.setText(SpecialityHolder);
            }
            if (OthereSpecialityHolder==null ||(OthereSpecialityHolder.equals(""))){
                etotherespeciality.setText("`Not Selected`");
            }else {
                etotherespeciality.setText(OthereSpecialityHolder);
            }if (DiseaseHolder==null ||(DiseaseHolder.equals(""))) {
                etdisease.setText("`Not Selected`");
            }else{
                etdisease.setText(DiseaseHolder);
            }
            if (OthereDiseasesHolder==null ||(OthereDiseasesHolder.equals(""))){
                etotherediseases.setText("`Not Selected`");
            }else {
                etotherediseases.setText(OthereDiseasesHolder);
            }
            if (WorkHospitalHolder==null ||(WorkHospitalHolder.equals(""))){
                etworkhospital.setText("`Not Selected`");
            }else {
                etworkhospital.setText(WorkHospitalHolder);
            }
            if (ExperienceHolder==null ||(ExperienceHolder.equals(""))){
                etexperience.setText("`Not Selected`");
            }else {
                etexperience.setText(ExperienceHolder);
            }
            if (SpecialfeeHolder==null ||(SpecialfeeHolder.equals(""))){
                etspecialfee.setText("`Not Selected`");
            }else {
                etspecialfee.setText(SpecialfeeHolder);
            }
            if (SpecialValidforHolder==null ||(SpecialValidforHolder.equals(""))){
                etspecialvalidfor.setText("`Not Selected`");
            }else {
                etspecialvalidfor.setText(SpecialValidforHolder);
            }
            if (SpecialhealthpkgHolder==null ||(SpecialhealthpkgHolder.equals(""))){
                etspecialhealthpkg.setText("`Not Selected`");
            }else {
                etspecialhealthpkg.setText(SpecialhealthpkgHolder);

            }
            if (SpecialhealthpkgdayHolder==null ||(SpecialhealthpkgdayHolder.equals(""))){
                etspecialhealthday.setText("`Not Selected`");
            }else {
                etspecialhealthday.setText(SpecialhealthpkgdayHolder);
            }
            // radio button ki value ko get karne ka code
            if (HomevisitHolder==null ||(HomevisitHolder.equals(""))) {
              ethomevisit.setText("`Not Selected`");
            } else {
                ethomevisit.setText(HomevisitHolder);
            }
            if (HomevisitfeeHolder==null ||(HomevisitfeeHolder.equals(""))){
                ethomevisitfee.setText("`Not Selected`");
            }else {
                ethomevisitfee.setText(HomevisitfeeHolder);
            }
            if (EveryvisitHolder==null ||(EveryvisitHolder.equals(""))){
                eteveryvisit.setText("`Not Selected`");
            }else {
                eteveryvisit.setText(EveryvisitHolder);
            }


        }
    }



}