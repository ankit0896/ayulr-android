package com.oxygen.micro.ayulr.doctor.fragment;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.oxygen.micro.ayulr.HttpParse;
import com.oxygen.micro.ayulr.NetworkDetactor;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.SharedPrefManager;
import com.oxygen.micro.ayulr.UpdateClinicalActivity;
import com.oxygen.micro.ayulr.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by MICRO on 8/1/2018.
 */
public class ViewClinicalActivity extends Fragment implements View.OnClickListener {
    @BindView(R.id.drimage)
    ImageView drimageView;
    @BindView(R.id.drimage2)
    ImageView imageView2;
    @BindView(R.id.drimage3)
    ImageView imageView3;
    @BindView(R.id.drhospital)
    TextView ethospital;
    @BindView(R.id.drfee)
    TextView etfee;
    @BindView(R.id.drfeevalidday)
    TextView etfeevalid;
    @BindView(R.id.drvisitday)
    TextView etvisitday;
    @BindView(R.id.drmrngtime)
    TextView etmrngtime;
    @BindView(R.id.drmrngto)
    TextView etmrngtimeto;
    @BindView(R.id.draftertime)
    TextView etaftertime;
    @BindView(R.id.drafterto)
    TextView etafterto;
    @BindView(R.id.drevngtime)
    TextView etevngtime;
    @BindView(R.id.drevngto)
    TextView etevngto;
    @BindView(R.id.drothervisitday)
    TextView etothervisitday;
    @BindView(R.id.drothermrngtime)
    TextView etothermrngtime;
    @BindView(R.id.drothermrngto)
    TextView etothermrngto;
    @BindView(R.id.drotheraftertime)
    TextView etotheraftertime;
    @BindView(R.id.drotherafterto)
    TextView etotherafterto;
    @BindView(R.id.drotherevngtime)
    TextView etotherevngtime;
    @BindView(R.id.drotherevngto)
    TextView etotherevngto;
    @BindView(R.id.drcontactno)
    TextView etcontact;
    @BindView(R.id.drstate)
    TextView etstate;
    @BindView(R.id.drcity)
    TextView etcity;
    @BindView(R.id.drlandmark)
    TextView etlandmark;
    @BindView(R.id.drpincode)
    TextView etpincode;
    @BindView(R.id.draddress)
    TextView etaddress;
    @BindView(R.id.btnsub)
    Button buttonclinic;
    String IdHolder, HospitalHolder,FeeHolder,FeeValidHolder,VisitHolder,MrngHolder,MrngToHolder,AfterHolder,AfterToHolder,EvngHolder,EvngToHolder,
            OtherVisitHoder,OtherMrngHolder,OtherMrngToHoder,OtherAfterHolder,OtherAfterToHolder,OtherEvngHolder,OtherEvngToHolder,
            ContactHolder,StateHolder,CityHolder,LandmarkHolder,PincodeHolder,AddressHolder,ImageHolder,ImageHolder1,ImageHolder2;
    ProgressDialog pDialog;
    String HttpURL1 = "http://ameygraphics.com/ayulr/api/filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_view_clinical, container, false);
        ButterKnife.bind(this,v);
        buttonclinic.setOnClickListener(this);
        User user = SharedPrefManager.getInstance(getContext()).getUser();
        IdHolder = String.valueOf(user.getId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(getContext())) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
        }
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
        if (v==buttonclinic){
            Intent intent=new Intent(getContext(), UpdateClinicalActivity.class);
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
                            HospitalHolder = jobj.getString("clinic_name");
                            FeeHolder= jobj.getString("cl_fees");
                           FeeValidHolder = jobj.getString("cl_valid");
                            VisitHolder= jobj.getString("visit_day");
                            MrngHolder = jobj.getString("m_from1");
                            MrngToHolder = jobj.getString("m_to1");
                            AfterHolder= jobj.getString("a_from1");
                            AfterToHolder = jobj.getString("a_to1");
                            EvngHolder= jobj.getString("e_from1");
                            EvngToHolder = jobj.getString("e_to1");
                            OtherVisitHoder= jobj.getString("other_day");
                            OtherMrngHolder = jobj.getString("m_from2");
                            OtherMrngToHoder= jobj.getString("m_to2");
                            OtherAfterHolder = jobj.getString("a_from2");
                            OtherAfterToHolder= jobj.getString("a_to2");
                            OtherEvngHolder = jobj.getString("e_from2");
                            OtherEvngToHolder= jobj.getString("e_to2");
                            ContactHolder = jobj.getString("cl_contact");
                            StateHolder= jobj.getString("cl_state");
                            CityHolder= jobj.getString("city");
                            LandmarkHolder= jobj.getString("cl_landmark");
                            PincodeHolder = jobj.getString("cl_pincode");
                            AddressHolder= jobj.getString("cl_address");
                            ImageHolder = jobj.getString("clinic_img");
                            ImageHolder1 = jobj.getString("clinic_img2");
                            ImageHolder2 = jobj.getString("clinic_img3");
                            if (!ImageHolder.equals("clinic_img")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ameygraphics.com/ayulr/clinic_img/"+ImageHolder).openStream();
                                bitmap = BitmapFactory.decodeStream(is);

                            }
                            if (!ImageHolder1.equals("clinic_img2")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ameygraphics.com/ayulr/clinic_img/"+ImageHolder1).openStream();
                                bitmap1 = BitmapFactory.decodeStream(is);

                            }
                            if (!ImageHolder2.equals("clinic_img3")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ameygraphics.com/ayulr/clinic_img/"+ImageHolder2).openStream();
                                bitmap2 = BitmapFactory.decodeStream(is);

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
            if (HospitalHolder.equals("null")||(HospitalHolder.equals(""))){
                ethospital.setText("`Not Selected`");
            }else {
                ethospital.setText(HospitalHolder);
            }if (FeeHolder.equals("null")||(FeeHolder.equals(""))){
                etfee.setText("`Not Selected`");
            }else {
                etfee.setText(FeeHolder);
            }
            if (FeeValidHolder.equals("null")||(FeeValidHolder.equals(""))){
                etfeevalid.setText("`Not Selected`");
            }else{
                etfeevalid.setText(FeeValidHolder);
            }
            if (VisitHolder.equals("null")||(VisitHolder.equals(""))){
                etvisitday.setText("`Not Selected`");
            }else {
                etvisitday.setText(VisitHolder);
            }
            if (MrngHolder.equals("null")||(MrngHolder.equals(""))){
                etmrngtime.setText("`Not Selected`");
            }else {
                etmrngtime.setText(MrngHolder);
            }
            if (MrngToHolder.equals("null")||(MrngToHolder.equals(""))){
                etmrngtimeto.setText("`Not Selected`");
            }else{
                etmrngtimeto.setText(MrngToHolder);
            }
            if (AfterHolder.equals("null")||(AfterHolder.equals(""))){
                etaftertime.setText("`Not Selected`");
            }else {
                etaftertime.setText(AfterHolder);
            }
            if(AfterToHolder.equals("null")||(AfterToHolder.equals(""))){
                etafterto.setText("`Not Selected`");
            }else{
                etafterto.setText(AfterToHolder);
            }
            if (EvngHolder.equals("null")||(EvngHolder.equals(""))){
                etevngtime.setText("`Not Selected`");
            }else {
                etevngtime.setText(EvngHolder);
            }if(EvngToHolder.equals("null")||(EvngToHolder.equals(""))){
                etevngto.setText("`Not Selected`");
            }else {
                etevngto.setText(EvngToHolder);
            }
            if (OtherVisitHoder.equals("null")||(OtherVisitHoder.equals(""))){
                etothervisitday.setText("`Not Selected`");
            }else {
                etothervisitday.setText(OtherVisitHoder);
            }
            if (OtherMrngHolder.equals("null")||(OtherMrngHolder.equals(""))){
                etothermrngtime.setText("`Not Selected`");
            }else {
                etothermrngtime.setText(OtherMrngHolder);
            }
            if (OtherMrngToHoder.equals("null")||(OtherMrngToHoder.equals(""))){
                etothermrngto.setText("`Not Selected`");
            }else {
                etothermrngto.setText(OtherMrngToHoder);
            }if (OtherAfterHolder.equals("null")||(OtherAfterHolder.equals(""))){
                etotheraftertime.setText("`Not Selected`");
            }else {
                etotheraftertime.setText(OtherAfterHolder);
            }if (OtherAfterToHolder.equals("null")||(OtherAfterToHolder.equals(""))){
                etotherafterto.setText("`Not Selected`");
            }else {
                etotherafterto.setText(OtherAfterToHolder);
            }if (OtherEvngHolder.equals("null")||(OtherEvngHolder.equals(""))){
                etotherevngtime.setText("`Not Selected`");
            }else{
                etotherevngtime.setText(OtherEvngHolder);
            }
            if (OtherEvngToHolder.equals("null")||(OtherEvngToHolder.equals(""))){
                etotherevngto.setText("`Not Selected`");
            }else {
                etotherevngto.setText(OtherEvngToHolder);
            }
            if (ContactHolder.equals("null")||(ContactHolder.equals(""))){
                etcontact.setText("`Not Selected`");
            }else {
                etcontact.setText(ContactHolder);
            }if (StateHolder.equals("null")||(StateHolder.equals(""))){
                etstate.setText("`Not Selected`");
            }else{
                etstate.setText(StateHolder);
            }
            if (CityHolder.equals("null")||(CityHolder.equals(""))){
                etcity.setText("`Not Selected`");
            }else {
                etcity.setText(CityHolder);
            }if (LandmarkHolder.equals("null")||(LandmarkHolder.equals(""))){
                etlandmark.setText("`Not Selected`");
            }else {
                etlandmark.setText(LandmarkHolder);
            }if(PincodeHolder.equals("null")||(PincodeHolder.equals(""))){
                etpincode.setText("`Not Selected`");
            }else {
                etpincode.setText(PincodeHolder);
            }
            if (AddressHolder.equals("null")||(AddressHolder.equals(""))){
                etaddress.setText("`Not Selected`");
            }else {
                etaddress.setText(AddressHolder);
            }
            if (bitmap==null||bitmap.equals("")) {
                drimageView.setImageResource(R.drawable.img_upload);
            }else{
                drimageView.setImageBitmap(bitmap);
            }
            if (bitmap1==null||bitmap1.equals("")) {
                imageView2.setImageResource(R.drawable.img_upload);
            }else{
                imageView2.setImageBitmap(bitmap1);
            }
            if (bitmap2==null||bitmap2.equals("")) {
                imageView3.setImageResource(R.drawable.img_upload);
            }else{
                imageView3.setImageBitmap(bitmap2);
            }

        }
    }


}
