package com.oxygen.micro.ayulr.view.paramedical.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParaViewClinicalActivity extends Fragment implements View.OnClickListener {


    @BindView(R.id.drhospital)
    TextView ethospital;
    @BindView(R.id.drfee)
    TextView etfee;
    @BindView(R.id.drfeevalidday)
    TextView etfeevalid;
    @BindView(R.id.drvisitday)
    TextView etvisitday;
    @BindView(R.id.drothervisitday)
    TextView etothervisitday;
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
    @BindView(R.id.btnsub)
    Button buttonclinic;
    @BindView(R.id.drimage)
    ImageView drimageView;
    @BindView(R.id.drimage2)
    ImageView imageView2;
    @BindView(R.id.drimage3)
    ImageView imageView3;

    String IdHolder, HospitalHolder, FeeHolder, FeeValidHolder, VisitHolder, MrngHolder, MrngToHolder, AfterHolder, AfterToHolder, EvngHolder, EvngToHolder,
            OtherVisitHoder, OtherMrngHolder, OtherMrngToHoder, OtherAfterHolder, OtherAfterToHolder, OtherEvngHolder, OtherEvngToHolder,
            ContactHolder, StateHolder, CityHolder, LandmarkHolder, PincodeHolder, AddressHolder, ImageHolder, ImageHolder1, ImageHolder2;
    ProgressDialog pDialog;
    String HttpURL1 = Config.BASEURL + "filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_view_clinical, container, false);
        init();
        return v;
    }

    private void init() {
        ButterKnife.bind(this,v);
        buttonclinic.setOnClickListener(this);
        Para para = SharedPrefManagerpara.getInstance(getContext()).getUserPara();
        IdHolder = String.valueOf(para.getParaId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(getContext())) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==buttonclinic){
            Intent intent = new Intent(getContext(), ParaUpdateClinicalActivity.class);
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

                // pDialog.dismiss();

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
                            HospitalHolder = jobj.getString("clinic_name");
                            FeeHolder = jobj.getString("cl_fees");
                            FeeValidHolder = jobj.getString("cl_valid");
                            VisitHolder = jobj.getString("visit_day");
                            MrngHolder = jobj.getString("m_from1");
                            MrngToHolder = jobj.getString("m_to1");
                            AfterHolder = jobj.getString("a_from1");
                            AfterToHolder = jobj.getString("a_to1");
                            EvngHolder = jobj.getString("e_from1");
                            EvngToHolder = jobj.getString("e_to1");
                            OtherVisitHoder = jobj.getString("other_day");
                            OtherMrngHolder = jobj.getString("m_from2");
                            OtherMrngToHoder = jobj.getString("m_to2");
                            OtherAfterHolder = jobj.getString("a_from2");
                            OtherAfterToHolder = jobj.getString("a_to2");
                            OtherEvngHolder = jobj.getString("e_from2");
                            OtherEvngToHolder = jobj.getString("e_to2");
                            ContactHolder = jobj.getString("cl_contact");
                            StateHolder = jobj.getString("cl_state");
                            CityHolder = jobj.getString("city");
                            LandmarkHolder = jobj.getString("cl_landmark");
                            PincodeHolder = jobj.getString("cl_pincode");
                            AddressHolder = jobj.getString("cl_address");
                            ImageHolder = jobj.getString("clinic_img");
                            ImageHolder1 = jobj.getString("clinic_img2");
                            ImageHolder2 = jobj.getString("clinic_img3");
                            if (!ImageHolder.equals("clinic_img")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ameygraphics.com/ayulr/clinic_img/"+ ImageHolder).openStream();
                                bitmap = BitmapFactory.decodeStream(is);

                            }
                            if (!ImageHolder1.equals("clinic_img2")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ameygraphics.com/ayulr/clinic_img/" + ImageHolder1).openStream();
                                bitmap1 = BitmapFactory.decodeStream(is);

                            }
                            if (!ImageHolder2.equals("clinic_img3")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ameygraphics.com/ayulr/clinic_img/" + ImageHolder2).openStream();
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
            if (HospitalHolder.equals("") || HospitalHolder.equals("null")) {
                ethospital.setText("`Not Selected`");
            } else {
                ethospital.setText(HospitalHolder);
            }
            if ((FeeHolder.equals("") || FeeHolder.equals("null"))) {
                etfee.setText("`Not Selected`");
            } else {
                etfee.setText(FeeHolder);
            }
            if (FeeValidHolder.equals("") || FeeValidHolder.equals("null")) {
                etfeevalid.setText("`Not Selected`");
            } else {
                etfeevalid.setText(FeeValidHolder);
            }
            if ((VisitHolder.equals("")) || VisitHolder.equals("null")) {
                etvisitday.setText("`Not Selected`");
            } else {
                etvisitday.setText(VisitHolder);
            }
            if ((MrngHolder.equals("")) || MrngHolder.equals("null")) {
                etmrngtime.setText("`Not Selected`");
            } else {
                etmrngtime.setText(MrngHolder);
            }
            if ((MrngToHolder.equals("")) || MrngToHolder.equals("null")) {
                etmrngtimeto.setText("`Not Selected`");
            } else {
                etmrngtimeto.setText(MrngToHolder);
            }
            if ((AfterHolder.equals("")) || AfterHolder.equals("null")) {
                etaftertime.setText("`Not Selected`");
            } else {
                etaftertime.setText(AfterHolder);
            }
            if ((AfterToHolder.equals("")) || AfterToHolder.equals("null")) {
                etafterto.setText("`Not Selected`");
            } else {
                etafterto.setText(AfterToHolder);
            }
            if ((EvngHolder.equals("")) || EvngHolder.equals("null")) {
                etevngtime.setText("`Not Selected`");
            } else {
                etevngtime.setText(EvngHolder);
            }
            if ((EvngToHolder.equals("")) || EvngToHolder.equals("null")) {
                etevngto.setText("`Not Selected`");
            } else {
                etevngto.setText(EvngToHolder);
            }
            if ((OtherVisitHoder.equals("")) || OtherVisitHoder.equals("null")) {
                etothervisitday.setText("`Not Selected`");
            } else {
                etothervisitday.setText(OtherVisitHoder);
            }
            if ((OtherMrngHolder.equals("")) || OtherMrngHolder.equals("null")) {
                etothermrngtime.setText("`Not Selected`");
            } else {
                etothermrngtime.setText(OtherMrngHolder);
            }
            if ((OtherMrngToHoder.equals("")) || OtherMrngToHoder.equals("null")) {
                etothermrngto.setText("`Not Selected`");
            } else {
                etothermrngto.setText(OtherMrngToHoder);
            }
            if ((OtherAfterHolder.equals("")) || OtherAfterHolder.equals("null")) {
                etotheraftertime.setText("`Not Selected`");
            } else {
                etotheraftertime.setText(OtherAfterHolder);
            }
            if ((OtherAfterToHolder.equals("")) || OtherAfterToHolder.equals("null")) {
                etotherafterto.setText("`Not Selected`");
            } else {
                etotherafterto.setText(OtherAfterToHolder);
            }
            if ((OtherEvngHolder.equals("")) || OtherEvngHolder.equals("null")) {
                etotherevngtime.setText("`Not Selected`");
            } else {
                etotherevngtime.setText(OtherEvngHolder);
            }
            if ((OtherEvngToHolder.equals("")) || OtherEvngToHolder.equals("null")) {
                etotherevngto.setText("`Not Selected`");
            } else {
                etotherevngto.setText(OtherEvngToHolder);
            }
            if ((ContactHolder.equals("")) || ContactHolder.equals("null")) {
                etcontact.setText("`Not Selected`");
            } else {
                etcontact.setText(ContactHolder);
            }
            if ((StateHolder.equals("")) || StateHolder.equals("null")) {
                etstate.setText("`Not Selected`");
            } else {
                etstate.setText(StateHolder);
            }
            if ((CityHolder.equals("")) || CityHolder.equals("null")) {
                etcity.setText("`Not Selected`");
            } else {
                etcity.setText(CityHolder);
            }
            if ((LandmarkHolder.equals("")) || LandmarkHolder.equals("null")) {
                etlandmark.setText("`Not Selected`");
            } else {
                etlandmark.setText(LandmarkHolder);
            }
            if ((PincodeHolder.equals("")) || PincodeHolder.equals("null")) {
                etpincode.setText("`Not Selected`");
            } else {
                etpincode.setText(PincodeHolder);
            }
            if ((AddressHolder.equals("")) || AddressHolder.equals("null")) {
                etaddress.setText("`Not Selected`");
            } else {
                etaddress.setText(AddressHolder);
            }
            if (bitmap == null || bitmap.equals("")) {
                drimageView.setImageResource(R.drawable.img_upload);
            } else {
                drimageView.setImageBitmap(bitmap);
            }
            if (bitmap1 == null || bitmap1.equals("")) {
                imageView2.setImageResource(R.drawable.img_upload);
            } else {
                imageView2.setImageBitmap(bitmap1);
            }
            if (bitmap2 == null || bitmap2.equals("")) {
                imageView3.setImageResource(R.drawable.img_upload);
            } else {
                imageView3.setImageBitmap(bitmap2);
            }

        }
    }


}
