package com.oxygen.micro.ayulr.view.nursing.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.constant.SharedPrefManagernur;
import com.oxygen.micro.ayulr.view.nursing.model.Nur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NursingViewClinicalActivity extends Fragment implements View.OnClickListener {

    @BindView(R.id.drfee)
    TextView etfee;
    @BindView(R.id.drfeevalidday)
    TextView etfeevalid;
    @BindView(R.id.drvisitday)
    TextView etvisitday;
    @BindView(R.id.drcontactno)
    TextView etcontact;
    @BindView(R.id.drstate)
    TextView etstate;
    @BindView(R.id.drcity)
    TextView etcity;
    @BindView(R.id.drpincode)
    TextView etpincode;
    @BindView(R.id.draddress)
    TextView etaddress;
    @BindView(R.id.drmrngtime)
    TextView etmrngtime;
    @BindView(R.id.drmrngto)
    TextView etmrngtimeto;
    @BindView(R.id.btnsub)
    Button buttonclinic;
    String IdHolder = "", FeeHolder = "", FeeValidHolder = "", VisitHolder = "", MrngHolder = "", MrngToHolder = "",
            ContactHolder = "", StateHolder = "", CityHolder = "", PincodeHolder = "", AddressHolder = "";
    ProgressDialog pDialog;
    String HttpURL1 = Config.BASEURL + "filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_nursing_view_clinical, container, false);
        initViews();
        return v;
    }

    private void initViews() {
        ButterKnife.bind(this, v);

        Nur nur = SharedPrefManagernur.getInstance(getContext()).getUserNur();
        IdHolder = String.valueOf(nur.getNurId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(getContext())) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
        }

        buttonclinic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonclinic) {
            Intent intent = new Intent(getContext(), NursingUpdateClinicalActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }


    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //   pDialog = ProgressDialog.show(getContext(), "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                //  pDialog.dismiss();

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
                            FeeHolder = jobj.getString("cl_fees");
                            FeeValidHolder = jobj.getString("cl_valid");
                            VisitHolder = jobj.getString("visit_day");
                            MrngHolder = jobj.getString("m_from1");
                            MrngToHolder = jobj.getString("m_to1");
                            ContactHolder = jobj.getString("cl_contact");
                            StateHolder = jobj.getString("cl_state");
                            CityHolder = jobj.getString("city");
                            PincodeHolder = jobj.getString("cl_pincode");
                            AddressHolder = jobj.getString("cl_address");


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

            if (FeeHolder.equals("null") || (FeeHolder.equals(""))) {
                etfee.setText("`Not Selected`");
            } else {
                etfee.setText(FeeHolder);
            }
            if (FeeValidHolder.equals("null") || (FeeValidHolder.equals(""))) {
                etfeevalid.setText("`Not Selected`");
            } else {
                etfeevalid.setText(FeeValidHolder);
            }
            if (VisitHolder.equals("null") || (VisitHolder.equals(""))) {
                etvisitday.setText("`Not Selected`");
            } else {
                etvisitday.setText(VisitHolder);
            }
            if (MrngHolder.equals("null") || (MrngHolder.equals(""))) {
                etmrngtime.setText("`Not Selected`");
            } else {
                etmrngtime.setText(MrngHolder);
            }
            if (MrngToHolder.equals("null") || (MrngToHolder.equals(""))) {
                etmrngtimeto.setText("`Not Selected`");
            } else {
                etmrngtimeto.setText(MrngToHolder);
            }
            if (ContactHolder.equals("null") || (ContactHolder.equals(""))) {
                etcontact.setText("`Not Selected`");
            } else {
                etcontact.setText(ContactHolder);
            }
            if (StateHolder.equals("null") || (StateHolder.equals(""))) {
                etstate.setText("`Not Selected`");
            } else {
                etstate.setText(StateHolder);
            }
            if (CityHolder.equals("null") || (CityHolder.equals(""))) {
                etcity.setText("`Not Selected`");
            } else {
                etcity.setText(CityHolder);

            }
            if (PincodeHolder.equals("null") || (PincodeHolder.equals(""))) {
                etpincode.setText("`Not Selected`");
            } else {
                etpincode.setText(PincodeHolder);
            }
            if (AddressHolder.equals("null") || (AddressHolder.equals(""))) {
                etaddress.setText("`Not Selected`");
            } else {
                etaddress.setText(AddressHolder);
            }


        }
    }


}
