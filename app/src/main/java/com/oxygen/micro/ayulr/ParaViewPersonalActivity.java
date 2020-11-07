package com.oxygen.micro.ayulr;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.HashMap;

public class ParaViewPersonalActivity  extends Fragment {
    TextView etname,etdob,etemail,etcontact,etanniversary,etmember,etcollege,etmedalyear,etmedalfor,
            etawardas,etawardfor,etresidence,etstate,etcity,etlandmark,etpincode,etgender;
    ImageView drimageView;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_para_view_personal, container, false);
        drimageView = (ImageView)v.findViewById(R.id.drimage);
        // ImageHolder = String.valueOf(getResources().getDrawable(R.drawable.profileimage));
        etname = (TextView)v. findViewById(R.id.drName);
        etdob = (TextView)v. findViewById(R.id.drdob);
        etemail= (TextView)v. findViewById(R.id.dremail);
        etcontact = (TextView)v. findViewById(R.id.drcontact);
        etanniversary= (TextView)v. findViewById(R.id.dranniversary);
        etmember = (TextView)v. findViewById(R.id.drmember);
        etcollege = (TextView)v. findViewById(R.id.drcollege);
        etmedalyear = (TextView)v. findViewById(R.id.drmedalyear);
        etmedalfor= (TextView)v. findViewById(R.id.drmedalfor);
        etawardas = (TextView)v. findViewById(R.id.drawardas);
        etawardfor= (TextView)v. findViewById(R.id.drawardfor);
        etresidence = (TextView)v. findViewById(R.id.drresidential);
        etstate = (TextView)v. findViewById(R.id.drstate);
        etcity = (TextView)v. findViewById(R.id.drcity);
        etlandmark = (TextView)v. findViewById(R.id.drlandmark);
        etpincode = (TextView)v. findViewById(R.id.drpincode);
        buttonpersonal = (Button)v. findViewById(R.id.btnpersonal);
        etgender=(TextView)v.findViewById(R.id.gender);
        Para para = SharedPrefManagerpara.getInstance(getContext()).getUserPara();
        IdHolder = String.valueOf(para.getParaId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(getContext())) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
        }
        buttonpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),ParaUpdatePersonalActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        return v;
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
                                InputStream is = new java.net.URL("http://ayulr.com/images/"+ImageHolder).openStream();
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