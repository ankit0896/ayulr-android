package com.oxygen.micro.ayulr.doctor.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.constant.SharedPrefManager;
import com.oxygen.micro.ayulr.model.User;
import com.oxygen.micro.ayulr.view.activity.UpdatePersonalActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by MICRO on 8/1/2018.
 */

public class ViewPersonalActivity extends Fragment implements View.OnClickListener {
    @BindView(R.id.drName)
    TextView etname;
    @BindView(R.id.drdob)
    TextView etdob;
    @BindView(R.id.dremail)
    TextView etemail;
    @BindView(R.id.drcontact)
    TextView etcontact;
    @BindView(R.id.dranniversary)
    TextView etanniversary;
    @BindView(R.id.drmember)
    TextView etmember;
    @BindView(R.id.drcollege)
    TextView etcollege;
    @BindView(R.id.drmedalyear)
    TextView etmedalyear;
    @BindView(R.id.drmedalfor)
    TextView etmedalfor;
    @BindView(R.id.drawardas)
    TextView etawardas;
    @BindView(R.id.drawardfor)
    TextView etawardfor;
    @BindView(R.id.drresidential)
    TextView etresidence;
    @BindView(R.id.drstate)
    TextView etstate;
    @BindView(R.id.drcity)
    TextView etcity;
    @BindView(R.id.drlandmark)
    TextView etlandmark;
    @BindView(R.id.drpincode)
    TextView etpincode;
    @BindView(R.id.gender)
    TextView etgender;
    @BindView(R.id.btnpersonal)
    Button buttonpersonal;
    @BindView(R.id.drimage)
    ImageView drimageView;
    RadioButton rdmale,rdfemale;
    String DrNameHolder="",DobHolder="",EmailHolder="",ContactHolder="",AnniversaryHolder="",MemberHolder="",
            CollegeHolder="",MedalyearHolder="",MedalforHolder="",AwardasHolder="",AwardforHolder="",ResidenceHolder="",StateHolder="",CityHolder="",
            LandmarkHolder="",PincodeHolder="",GenderHolder="";
    String ImageHolder,IdHolder;
    String HttpURL1 = Config.BASEURL+"filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    private Bitmap bitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_view_personal, container, false);
        ButterKnife.bind(this,v);
        initview();
        return v;
    }
    private void initview(){
        User user = SharedPrefManager.getInstance(getContext()).getUser();
        IdHolder = String.valueOf(user.getId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), ""+IdHolder, Toast.LENGTH_SHORT).show();
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
        }
        buttonpersonal.setOnClickListener(this);
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
                Log.e("response", "response=="+s );
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
        if (v==buttonpersonal){
            Intent intent=new Intent(getContext(), UpdatePersonalActivity.class);
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
                           // Toast.makeText(context, ""+jobj.getInt("name"), Toast.LENGTH_SHORT).show();
                            // Storing Student Name, Phone Number, Class into Variables.
                            DrNameHolder = jobj.getString("name");
                            DobHolder = jobj.getString("dob");
                            EmailHolder = jobj.getString("email");
                            ContactHolder= jobj.getString("cont");
                            AnniversaryHolder = jobj.getString("doa");
                            MemberHolder = jobj.getString("member_of");
                            CollegeHolder = jobj.getString("studied_from");
                            MedalyearHolder = jobj.getString("medal_year");
                            MedalforHolder= jobj.getString("medal_for");
                            AwardasHolder = jobj.getString("awarded_as");
                            AwardforHolder= jobj.getString("awarded_for");
                            ResidenceHolder = jobj.getString("res_add");
                            StateHolder = jobj.getString("res_state");
                            CityHolder = jobj.getString("res_city");
                            LandmarkHolder = jobj.getString("landmark");
                            PincodeHolder = jobj.getString("res_pincode");
                            GenderHolder = jobj.getString("gender");
                            ImageHolder = jobj.getString("profile_img");
                           // if (ImageHolder.equals("null")){
                                if (!ImageHolder.equals("profile_img")) {
                                    // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                    InputStream is = new java.net.URL("http://ameygraphics.com/ayulr/images/"+ImageHolder).openStream();
                                    bitmap = BitmapFactory.decodeStream(is);
                                }
                           // }

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

            // Setting Student Name, Phone Number, Class into TextView after done all process .
            etname.setText(DrNameHolder);
            etdob.setText(DobHolder);
            etemail.setText(EmailHolder);
            etcontact.setText(ContactHolder);
            if (AnniversaryHolder.equals("")||AnniversaryHolder.equals("null")){
                etanniversary.setText("~Not Selected~");
            }else {
                etanniversary.setText(AnniversaryHolder);
            }
            if (MemberHolder.equals("null") ||MemberHolder.equals("")){
                etmember.setText("~Not Selected~");
            }else {
                etmember.setText(MemberHolder);
            }
            if (CollegeHolder.equals("null") ||CollegeHolder.equals("")){
                etcollege.setText("~Not Selected~");
            }else {
                etcollege.setText(CollegeHolder);
            }
            if (MedalforHolder.equals("null") ||MedalforHolder.equals("")){
                etmedalfor.setText("~Not Selected~");
            }else {
                etmedalfor.setText(MedalforHolder);
            }
            if (MedalyearHolder.equals("null") ||MedalyearHolder.equals("")){
                etmedalyear.setText("~Not Selected~");
            }else {
                etmedalyear.setText(MedalyearHolder);
            }
            if (AwardasHolder.equals("null") ||AwardasHolder.equals("")){
                etawardas.setText("~Not Selected~");
            }else {
                etawardas.setText(AwardasHolder);
            }
            if (AwardforHolder.equals("null") ||AwardforHolder.equals("")){
                etawardfor.setText("~Not Selected~");
            }else {
                etawardfor.setText(AwardforHolder);
            }
            if (ResidenceHolder.equals("null")||ResidenceHolder.equals("")){
                etresidence.setText("~Not Selected~");
            }else {
                etresidence.setText(ResidenceHolder);
            }
            if (StateHolder.equals("null") ||StateHolder.equals("")){
                etstate.setText("~Not Selected~");
            }else {
                etstate.setText(StateHolder);
            }if (CityHolder.equals("null") ||CityHolder.equals("")){
                etcity.setText("~Not Selected~");
            }else {
                etcity.setText(CityHolder);
            }
            if (LandmarkHolder.equals("null") ||LandmarkHolder.equals("")){
                etlandmark.setText("~Not Selected~");
            }else {
                etlandmark.setText(LandmarkHolder);
            }if (PincodeHolder.equals("null") ||PincodeHolder.equals("")){
                etpincode.setText("~Not Selected~");
            }else {
                etpincode.setText(PincodeHolder);
            }
            if (GenderHolder.equals("null") ||GenderHolder.equals("")){
                etgender.setText("~Not Selected~");
            }else {
                etgender.setText(GenderHolder);
            }
            if (bitmap==null) {
                drimageView.setImageResource(R.drawable.profileimage);
            }else{
                drimageView.setImageBitmap(bitmap);
            }

        }
    }



}