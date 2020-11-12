package com.oxygen.micro.ayulr.view.paramedical.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.view.paramedical.model.Para;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.constant.SharedPrefManagerpara;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParaViewPersonalActivity  extends Fragment implements View.OnClickListener {
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
    @BindView(R.id.drimage)
    ImageView drimageView;
    @BindView(R.id.btnpersonal)
    Button buttonpersonal;
    RadioButton rdmale,rdfemale;
    String  DrNameHolder,DobHolder,EmailHolder,ContactHolder,AnniversaryHolder,MemberHolder,
            CollegeHolder,MedalyearHolder,MedalforHolder,AwardasHolder,AwardforHolder,ResidenceHolder,StateHolder,CityHolder,
            LandmarkHolder,PincodeHolder,GenderHolder;
    String ImageHolder,IdHolder;
    ProgressDialog pDialog;
    String HttpURL1 = Config.BASEURL+"filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    private Bitmap bitmap;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         v = inflater.inflate(R.layout.activity_para_view_personal, container, false);

        // ImageHolder = String.valueOf(getResources().getDrawable(R.drawable.profileimage));

        init();
        return v;
    }

    private void init() {
        ButterKnife.bind(this,v);
        Para para = SharedPrefManagerpara.getInstance(getContext()).getUserPara();
        IdHolder = String.valueOf(para.getParaId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(getContext())) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
        }
        buttonpersonal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==buttonpersonal){
            Intent intent=new Intent(getContext(),ParaUpdatePersonalActivity.class);
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

             //   pDialog.dismiss();

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
                            if (!ImageHolder.equals("profile_img")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ameygraphics.com/ayulr/images/"+ImageHolder).openStream();
                                bitmap = BitmapFactory.decodeStream(is);

                            }

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
            if (MemberHolder.equals("")||MemberHolder.equals("null")){
                etmember.setText("~Not Selected~");
            }else {
                etmember.setText(MemberHolder);
            }
            if (CollegeHolder.equals("")||CollegeHolder.equals("null")){
                etcollege.setText("~Not Selected~");
            }else {
                etcollege.setText(CollegeHolder);
            }
            if (MedalforHolder.equals("")||MedalforHolder.equals("null")){
                etmedalfor.setText("~Not Selected~");
            }else {
                etmedalfor.setText(MedalforHolder);
            }
            if (MedalyearHolder.equals("")||MedalyearHolder.equals("null")){
                etmedalyear.setText("~Not Selected~");
            }else {
                etmedalyear.setText(MedalyearHolder);
            }
            if (AwardasHolder.equals("")||AwardasHolder.equals("null")){
                etawardas.setText("~Not Selected~");
            }else {
                etawardas.setText(AwardasHolder);
            }
            if (AwardforHolder.equals("")||AwardforHolder.equals("null")){
                etawardfor.setText("~Not Selected~");
            }else {
                etawardfor.setText(AwardforHolder);
            }
            if (ResidenceHolder.equals("")||ResidenceHolder.equals("null")){
                etresidence.setText("~Not Selected~");
            }else {
                etresidence.setText(ResidenceHolder);
            }
            if (StateHolder.equals("")||StateHolder.equals("null")){
                etstate.setText("~Not Selected~");
            }else {
                etstate.setText(StateHolder);
            }
            if (CityHolder.equals("")||CityHolder.equals("null")){
                etcity.setText("~Not Selected~");
            }else {
                etcity.setText(CityHolder);
            }
            if (LandmarkHolder.equals("")||(LandmarkHolder.equals("null"))){
                etlandmark.setText("~Not Selected~");
            }else {
                etlandmark.setText(LandmarkHolder);
            }
            if (PincodeHolder.equals("")||PincodeHolder.equals("null")){
                etpincode.setText("~Not Selected~");
            }else {
                etpincode.setText(PincodeHolder);
            }
            if (GenderHolder.equals("")||GenderHolder.equals("null")){
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