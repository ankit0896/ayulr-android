package com.oxygen.micro.ayulr.view.paramedical.activity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oxygen.micro.ayulr.constant.Config;
import com.oxygen.micro.ayulr.connection.HttpParse;
import com.oxygen.micro.ayulr.connection.NetworkDetactor;
import com.oxygen.micro.ayulr.view.paramedical.model.Para;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.constant.SharedPrefManagerpara;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParaUpdateClinicalActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.drhospital)
    EditText ethospital;
    @BindView(R.id.drfee)
    EditText etfee;
    @BindView(R.id.drfeevalidday)
    EditText etfeevalid;
    @BindView(R.id.drvisitday)
    EditText etvisitday;
    @BindView(R.id.drothervisitday)
    EditText etothervisitday;
    @BindView(R.id.drcontactno)
    EditText etcontact;
    @BindView(R.id.drstate)
    EditText etstate;
    @BindView(R.id.drcity)
    EditText etcity;
    @BindView(R.id.drlandmark)
    EditText etlandmark;
    @BindView(R.id.drpincode)
    EditText etpincode;
    @BindView(R.id.draddress)
    EditText etaddress;
    @BindView(R.id.drimage)
    ImageView drimageView;
    @BindView(R.id.drimage2)
    ImageView imageView2;
    @BindView(R.id.drimage3)
    ImageView imageView3;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    static EditText etmrngtime, etmrngtimeto,etaftertime,etafterto, etevngtime,etevngto,
            etothermrngtime,etothermrngto,etotheraftertime,etotherafterto,etotherevngtime,etotherevngto;
    Button buttonclinic;
    String HttpURL1 = Config.BASEURL+"filter_view.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    String IdHolder,HospitalHolder,FeeHolder,FeeValidHolder,VisitHolder,MrngHolder,MrngToHolder,AfterHolder,AfterToHolder,EvngHolder,EvngToHolder,
            OtherVisitHoder,OtherMrngHolder,OtherMrngToHoder,OtherAfterHolder,OtherAfterToHolder,OtherEvngHolder,OtherEvngToHolder,
            ContactHolder,StateHolder,CityHolder,LandmarkHolder,PincodeHolder,AddressHolder,ImageHolder,ImageHolder2,ImageHolder3;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = Config.BASEURL+"update_clinical.php";
    ArrayList<Integer> muserday = new ArrayList<>();
    String[] listitem;
    boolean[] checkeditem;
    ArrayList<Integer> muserdayoff = new ArrayList<>();
    String[] listitem1;
    boolean[] checkeditem1;
    static final String TIME_DIALOG_ID = "timePicker";
    static final String TIME_DIALOG_ID1 = "timePicker1";
    static final String TIME_DIALOG_ID2 = "timePicker2";
    static final String TIME_DIALOG_ID3 = "timePicker3";
    static final String TIME_DIALOG_ID4 = "timePicker4";
    static final String TIME_DIALOG_ID5 = "timePicker5";
    private static int mrngtimeHour;
    private static int mrngtimeMinute;
    private static int mrngtoHour;
    private static int mrngtoMinute;
    private static int aftertimeHour;
    private static int aftertimeMinute;
    private static int aftertoHour;
    private static int aftertoMinute;
    private static int evngtimeHour;
    private static int evngtimeMinute;
    private static int evngtoHour;
    private static int evngtoMinute;
    static final String TIME_DIALOG_ID6 = "timePicker6";
    static final String TIME_DIALOG_ID7 = "timePicker7";
    static final String TIME_DIALOG_ID8 = "timePicker8";
    static final String TIME_DIALOG_ID9 = "timePicker9";
    static final String TIME_DIALOG_ID10 = "timePicker10";
    static final String TIME_DIALOG_ID11 = "timePicker11";
    private static int othermrngtimeHour;
    private static int othermrngtimeMinute;
    private static int othermrngtoHour;
    private static int othermrngtoMinute;
    private static int otheraftertimeHour;
    private static int otheraftertimeMinute;
    private static int otheraftertoHour;
    private static int otheraftertoMinute;
    private static int otherevngtimeHour;
    private static int otherevngtimeMinute;
    private static int otherevngtoHour;
    private static int otherevngtoMinute;
    private static final int Result_Load_Image = 1;
    private static final int Result_Load_Image2 = 2;
    private static final int Result_Load_Image3 = 3;
    private Bitmap bitmap;
    private Bitmap bitmap2;
    private Bitmap bitmap3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_clinical);
        init();



    }

    private void init() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etmrngtime= (EditText) findViewById(R.id.drmrngtime);
        etmrngtimeto = (EditText) findViewById(R.id.drmrngto);
        etaftertime = (EditText) findViewById(R.id.draftertime);
        etafterto = (EditText) findViewById(R.id.drafterto);
        etevngtime= (EditText) findViewById(R.id.drevngtime);
        etevngto= (EditText) findViewById(R.id.drevngto);

        etothermrngtime = (EditText) findViewById(R.id.drothermrngtime);
        etothermrngto = (EditText) findViewById(R.id.drothermrngto);
        etotheraftertime = (EditText) findViewById(R.id.drotheraftertime);
        etotherafterto = (EditText) findViewById(R.id.drotherafterto);
        etotherevngtime= (EditText) findViewById(R.id.drotherevngtime);
        etotherevngto = (EditText) findViewById(R.id.drotherevngto);
        buttonclinic = (Button) findViewById(R.id.btnsub);
        listitem = getResources().getStringArray(R.array.days_array);
        checkeditem = new boolean[listitem.length];
        listitem1 = getResources().getStringArray(R.array.days_array);
        checkeditem1 = new boolean[listitem1.length];
        Para para = SharedPrefManagerpara.getInstance(ParaUpdateClinicalActivity.this).getUserPara();
        IdHolder = String.valueOf(para.getParaId());
        Log.e("value=", " " + IdHolder);
        if (NetworkDetactor.isNetworkAvailable(ParaUpdateClinicalActivity.this)) {
            HttpWebCall(String.valueOf(IdHolder));
        } else {
            Toast.makeText(ParaUpdateClinicalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }


        drimageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        etvisitday.setOnClickListener(this);
        etothervisitday.setOnClickListener(this);
        etmrngtime.setOnClickListener(this);
        etmrngtimeto.setOnClickListener(this);
        etevngtime.setOnClickListener(this);
        etevngto.setOnClickListener(this);
        etaftertime.setOnClickListener(this);
        etafterto.setOnClickListener(this);
        buttonclinic.setOnClickListener(this);
        etothermrngtime.setOnClickListener(this);
        etothermrngto.setOnClickListener(this);
        etotherevngtime.setOnClickListener(this);
        etotherevngto.setOnClickListener(this);
        etotheraftertime.setOnClickListener(this);
        etotherafterto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==drimageView){
            switch (v.getId()) {
                case R.id.drimage:
                    Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryintent.setType("image/*");
                    startActivityForResult(galleryintent, Result_Load_Image);
                    break;
            }
        }
        if(v==imageView2){
            switch (v.getId()) {
                case R.id.drimage2:
                    Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryintent.setType("image/*");
                    startActivityForResult(galleryintent, Result_Load_Image2);
                    break;
            }
        }
        if(v==imageView3){
            switch (v.getId()) {
                case R.id.drimage3:
                    Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryintent.setType("image/*");
                    startActivityForResult(galleryintent, Result_Load_Image3);
                    break;
            }
        }
        if(v==etvisitday){
            AlertDialog.Builder MBuilder = new AlertDialog.Builder(ParaUpdateClinicalActivity.this);
            MBuilder.setTitle(R.string.dailog_title);
            MBuilder.setMultiChoiceItems(listitem, checkeditem, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                    if (isChecked) {
                        if (!muserday.contains(position)) {
                            muserday.add(position);
                        }
                    } else if (muserday.contains(position)) {
                        muserday.remove(position);

                    }
                }
            });
            MBuilder.setCancelable(false);
            MBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String item = "";
                    for (int i = 0; i < muserday.size(); i++) {
                        item = item + listitem[muserday.get(i)];
                        if (i != muserday.size() - 1) ;
                        {
                            item = item + ",";
                        }
                    }
                    etvisitday.setText(item);
                }
            });
            MBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            MBuilder.setNeutralButton(R.string.clearall_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < checkeditem.length; i++) {
                        checkeditem[i] = false;
                        muserday.clear();
                        etvisitday.setText("");
                    }
                }
            });
            AlertDialog mdialog = MBuilder.create();
            mdialog.show();
        }
        if(v==etothervisitday){
            AlertDialog.Builder MBuilder = new AlertDialog.Builder(ParaUpdateClinicalActivity.this);
            MBuilder.setTitle(R.string.dailog_title1);
            MBuilder.setMultiChoiceItems(listitem1, checkeditem1, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                    if (isChecked) {
                        if (!muserdayoff.contains(position)) {
                            muserdayoff.add(position);
                        }
                    } else if (muserdayoff.contains(position)) {
                        muserdayoff.remove(position);

                    }
                }
            });
            MBuilder.setCancelable(false);
            MBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String item = "";
                    for (int i = 0; i < muserdayoff.size(); i++) {
                        item = item + listitem1[muserdayoff.get(i)];
                        if (i != muserdayoff.size() - 1) ;
                        {
                            item = item + ",";
                        }
                    }
                    etothervisitday.setText(item);
                }
            });
            MBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            MBuilder.setNeutralButton(R.string.clearall_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < checkeditem1.length; i++) {
                        checkeditem1[i] = false;
                        muserdayoff.clear();
                        etothervisitday.setText("");
                    }
                }
            });
            AlertDialog mdialog = MBuilder.create();
            mdialog.show();
        }
        if(v==etmrngtime){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener, mrngtimeHour, mrngtimeMinute, false).show();
        }
        if(v==etmrngtimeto){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener1, mrngtoHour, mrngtoMinute, false).show();
        }
        if(v==etevngtime){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener2, evngtimeHour, evngtimeMinute, false).show();
        }
        if(v==etevngto){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener3, evngtoHour, evngtoMinute, false).show();
        }
        if(v==etaftertime){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener4, aftertimeHour, aftertimeMinute, false).show();
        }
        if(v==etafterto){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener5, aftertoHour, aftertoMinute, false).show();
        }
        if(v==buttonclinic){
            GetDataFromImageView();
            GetDataFromImageView2();
            GetDataFromImageView3();
            CheckEditTextIsEmptyOrNot();
            if (NetworkDetactor.isNetworkAvailable(ParaUpdateClinicalActivity.this)) {
                if (CheckEditText) {
                    UpdateDoctorRegistration( HospitalHolder,FeeHolder,FeeValidHolder,VisitHolder,MrngHolder,MrngToHolder,AfterHolder,
                            AfterToHolder,EvngHolder,EvngToHolder,OtherVisitHoder,OtherMrngHolder,OtherMrngToHoder,
                            OtherAfterHolder,OtherAfterToHolder,OtherEvngHolder,OtherEvngToHolder,ContactHolder,StateHolder,
                            CityHolder,LandmarkHolder,PincodeHolder,AddressHolder,IdHolder,ImageHolder,ImageHolder2,ImageHolder3);

                } else {


                }
            } else {
                Toast.makeText(ParaUpdateClinicalActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }

        if(v==etothermrngtime){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener6, othermrngtimeHour, othermrngtimeMinute, false).show();
        }
        if(v==etothermrngto){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener7, othermrngtoHour, othermrngtoMinute, false).show();
        }
        if(v==etotherevngtime){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener8, otherevngtimeHour, otherevngtimeMinute, false).show();
        }
        if(v==etotherevngto){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener9, otherevngtoHour, otherevngtoMinute, false).show();
        }
        if(v==etotheraftertime){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener10,otheraftertimeHour ,otheraftertimeMinute, false).show();
        }
        if(v==etotherafterto){
            new TimePickerDialog(ParaUpdateClinicalActivity.this, mTimeSetListener11, otheraftertoHour, otheraftertoMinute, false).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==RESULT_OK ) {
            switch (requestCode) {
                case Result_Load_Image:
                    if (data != null) {
                        Uri filePath = data.getData();

                        try {
                            //Getting the Bitmap from Gallery
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                            drimageView.setImageBitmap(bitmap);
                            ImageHolder = getStringImage(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }break;
                case Result_Load_Image2:
                    if (data != null) {
                        Uri filePath = data.getData();

                        try {
                            //Getting the Bitmap from Gallery
                            bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                            imageView2.setImageBitmap(bitmap2);
                            ImageHolder2 = getStringImage2(bitmap2);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }break;
                case Result_Load_Image3:
                    if (data != null) {
                        Uri filePath = data.getData();

                        try {
                            //Getting the Bitmap from Gallery
                            bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                            imageView3.setImageBitmap(bitmap3);
                            ImageHolder3 = getStringImage3(bitmap3);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }break;
            }
        }else{
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }


    public String getStringImage(Bitmap bmp) {
        if(bmp!=null){
            ByteArrayOutputStream byteArrayOutputStreamObject;

            byteArrayOutputStreamObject = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.JPEG,10 , byteArrayOutputStreamObject);

            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

            return ConvertImage;
        }
        return "NoImage.jpeg";

    }
    public String getStringImage2(Bitmap bmp) {
        if(bmp!=null){
            ByteArrayOutputStream byteArrayOutputStreamObject;

            byteArrayOutputStreamObject = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.JPEG,10 , byteArrayOutputStreamObject);

            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
            return ConvertImage;
        }
        return "NoImage.jpeg";



    }
    public String getStringImage3(Bitmap bmp) {
        if(bmp!=null){

            ByteArrayOutputStream byteArrayOutputStreamObject;

            byteArrayOutputStreamObject = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.JPEG,10 , byteArrayOutputStreamObject);

            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

            return ConvertImage;
        }
        return "NoImage.jpeg";
    }




    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mrngtimeHour = hourOfDay;
                    mrngtimeMinute = minute;
                    String timeSet = "";
                    if (mrngtimeHour > 12) {
                        mrngtimeHour -= 12;
                        timeSet = "PM";
                    } else if (mrngtimeHour == 0) {
                        mrngtimeHour += 12;
                        timeSet = "AM";
                    } else if (mrngtimeHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (mrngtimeMinute < 10)
                        min = "0" + mrngtimeMinute ;
                    else
                        min = String.valueOf(mrngtimeMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(mrngtimeHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etmrngtime.setText(aTime);
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener1 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mrngtoHour = hourOfDay;
                    mrngtoMinute = minute;
                    String timeSet = "";
                    if (mrngtoHour > 12) {
                        mrngtoHour -= 12;
                        timeSet = "PM";
                    } else if (mrngtoHour == 0) {
                        mrngtoHour += 12;
                        timeSet = "AM";
                    } else if (mrngtoHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (mrngtoMinute < 10)
                        min = "0" + mrngtoMinute ;
                    else
                        min = String.valueOf(mrngtoMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(mrngtoHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etmrngtimeto.setText(aTime);
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener2 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    evngtimeHour = hourOfDay;
                    evngtimeMinute = minute;
                    String timeSet = "";
                    if (evngtimeHour > 12) {
                        evngtimeHour -= 12;
                        timeSet = "PM";
                    } else if (evngtimeHour == 0) {
                        evngtimeHour += 12;
                        timeSet = "AM";
                    } else if (evngtimeHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (evngtimeMinute < 10)
                        min = "0" + evngtimeMinute ;
                    else
                        min = String.valueOf(evngtimeMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(evngtimeHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etevngtime.setText(aTime);
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener3 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    evngtoHour = hourOfDay;
                    evngtoMinute = minute;
                    String timeSet = "";
                    if (evngtoHour > 12) {
                        evngtoHour -= 12;
                        timeSet = "PM";
                    } else if (evngtoHour == 0) {
                        evngtoHour += 12;
                        timeSet = "AM";
                    } else if (evngtoHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (evngtoMinute < 10)
                        min = "0" + evngtoMinute ;
                    else
                        min = String.valueOf(evngtoMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(evngtoHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etevngto.setText(aTime);
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener4 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    aftertimeHour = hourOfDay;
                    aftertimeMinute = minute;
                    String timeSet = "";
                    if (aftertimeHour > 12) {
                        aftertimeHour -= 12;
                        timeSet = "PM";
                    } else if (aftertimeHour == 0) {
                        aftertimeHour += 12;
                        timeSet = "AM";
                    } else if (aftertimeHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (aftertimeMinute < 10)
                        min = "0" + aftertimeMinute ;
                    else
                        min = String.valueOf(aftertimeMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(aftertimeHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etaftertime.setText(aTime);
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener5 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    aftertoHour = hourOfDay;
                    aftertoMinute = minute;
                    String timeSet = "";
                    if (aftertoHour > 12) {
                        aftertoHour -= 12;
                        timeSet = "PM";
                    } else if (aftertoHour == 0) {
                        aftertoHour += 12;
                        timeSet = "AM";
                    } else if (aftertoHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (aftertoMinute < 10)
                        min = "0" + aftertoMinute ;
                    else
                        min = String.valueOf(aftertoMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(aftertoHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etafterto.setText(aTime);
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener6 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    othermrngtimeHour = hourOfDay;
                    othermrngtimeMinute = minute;
                    String timeSet = "";
                    if (othermrngtimeHour> 12) {
                        othermrngtimeHour -= 12;
                        timeSet = "PM";
                    } else if (othermrngtimeHour == 0) {
                        othermrngtimeHour += 12;
                        timeSet = "AM";
                    } else if (othermrngtimeHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (othermrngtimeMinute < 10)
                        min = "0" + othermrngtimeMinute;
                    else
                        min = String.valueOf(othermrngtimeMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(othermrngtimeHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etothermrngtime.setText(aTime);
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener7 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    othermrngtoHour = hourOfDay;
                    othermrngtoMinute = minute;
                    String timeSet = "";
                    if (othermrngtoHour> 12) {
                        othermrngtoHour -= 12;
                        timeSet = "PM";
                    } else if (othermrngtoHour == 0) {
                        othermrngtoHour += 12;
                        timeSet = "AM";
                    } else if (othermrngtoHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (othermrngtoMinute < 10)
                        min = "0" + othermrngtoMinute;
                    else
                        min = String.valueOf(othermrngtoMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(othermrngtoHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etothermrngto.setText(aTime);
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener8 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    otherevngtimeHour = hourOfDay;
                    otherevngtimeMinute= minute;
                    String timeSet = "";
                    if (otherevngtimeHour> 12) {
                        otherevngtimeHour -= 12;
                        timeSet = "PM";
                    } else if (otherevngtimeHour == 0) {
                        otherevngtimeHour += 12;
                        timeSet = "AM";
                    } else if (otherevngtimeHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (otherevngtimeMinute < 10)
                        min = "0" + otherevngtimeMinute;
                    else
                        min = String.valueOf( otherevngtimeMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(otherevngtimeHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etotherevngtime.setText(aTime);
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener9 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    otherevngtoHour = hourOfDay;
                    otherevngtoMinute= minute;
                    String timeSet = "";
                    if ( otherevngtoHour> 12) {
                        otherevngtoHour -= 12;
                        timeSet = "PM";
                    } else if ( otherevngtoHour == 0) {
                        otherevngtoHour+= 12;
                        timeSet = "AM";
                    } else if ( otherevngtoHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (otherevngtoMinute< 10)
                        min = "0" + otherevngtoMinute;
                    else
                        min = String.valueOf( otherevngtoMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(otherevngtoHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etotherevngto.setText(aTime);
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener10 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    otheraftertimeHour = hourOfDay;
                    otheraftertimeMinute= minute;
                    String timeSet = "";
                    if (otheraftertimeHour> 12) {
                        otheraftertimeHour -= 12;
                        timeSet = "PM";
                    } else if ( otheraftertimeHour == 0) {
                        otheraftertimeHour+= 12;
                        timeSet = "AM";
                    } else if ( otheraftertimeHour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (  otheraftertimeMinute< 10)
                        min = "0" +   otheraftertimeMinute;
                    else
                        min = String.valueOf(  otheraftertimeMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append( otheraftertimeHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etotheraftertime.setText(aTime);
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener11 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    otheraftertoHour = hourOfDay;
                    otheraftertoMinute= minute;
                    String timeSet = "";
                    if (otheraftertoHour > 12) {
                        otheraftertoHour  -= 12;
                        timeSet = "PM";
                    } else if (otheraftertoHour  == 0) {
                        otheraftertoHour += 12;
                        timeSet = "AM";
                    } else if (otheraftertoHour  == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (otheraftertoMinute< 10)
                        min = "0" +otheraftertoMinute;
                    else
                        min = String.valueOf(otheraftertoMinute);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(otheraftertoHour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    etotherafterto.setText(aTime);
                }
            };
    public void GetDataFromImageView() {
        drimageView.setImageBitmap(bitmap);
        ImageHolder=getStringImage(bitmap);


    }
    public void GetDataFromImageView2() {
        imageView2.setImageBitmap(bitmap2);
        ImageHolder2=getStringImage2(bitmap2);


    }

    public void GetDataFromImageView3() {
        imageView3.setImageBitmap(bitmap3);
        ImageHolder3=getStringImage3(bitmap3);


    }


    public void CheckEditTextIsEmptyOrNot() {

        HospitalHolder = ethospital.getText().toString();
        FeeHolder = etfee.getText().toString();
        FeeValidHolder= etfeevalid.getText().toString();
        VisitHolder = etvisitday.getText().toString();
        MrngHolder = etmrngtime.getText().toString();
        MrngToHolder = etmrngtimeto.getText().toString();
        AfterHolder =etaftertime.getText().toString();
        AfterToHolder = etafterto.getText().toString();
        EvngHolder = etevngtime.getText().toString();
        EvngToHolder = etevngto.getText().toString();
        OtherVisitHoder = etothervisitday.getText().toString();
        OtherMrngHolder = etothermrngtime.getText().toString();
        OtherMrngToHoder = etothermrngto.getText().toString();
        OtherAfterHolder= etotheraftertime.getText().toString();
        OtherAfterToHolder= etotherafterto.getText().toString();
        OtherEvngHolder = etotherevngtime.getText().toString();
        OtherEvngToHolder= etotherevngto.getText().toString();
        ContactHolder = etcontact.getText().toString();
        StateHolder = etstate.getText().toString();
        CityHolder = etcity.getText().toString();
        LandmarkHolder = etlandmark.getText().toString();
        PincodeHolder = etpincode.getText().toString();
        AddressHolder = etaddress.getText().toString();
        if (HospitalHolder.equals("") || FeeHolder.equals("")||FeeValidHolder.equals("") || VisitHolder.equals("")
                ||(ContactHolder.equals(""))||(StateHolder.equals(""))||(CityHolder.equals(""))||(PincodeHolder.equals(""))||(AddressHolder.equals(""))){
            if (HospitalHolder.equals("")) {
                ethospital.requestFocus();
                ethospital.setError("field required");
            }
            if (FeeHolder.equals("")) {
                etfee.requestFocus();
                etfee.setError("field required");
            }
            if (FeeValidHolder.equals("")) {
                etfeevalid.requestFocus();
                etfeevalid.setError("field required");
            }
            if (VisitHolder.equals("")) {
                etvisitday.requestFocus();
                etvisitday.setError("field required");
            }

            if (ContactHolder.equals("")) {
                etcontact.requestFocus();
                etcontact.setError("field required");
            }
            if (AddressHolder.equals("")) {
                etaddress.requestFocus();
                etaddress.setError("field required");

            }  if (StateHolder.equals("")) {
                etstate.requestFocus();
                etstate.setError("field required");
            }
            if (CityHolder.equals("")) {
                etcity.requestFocus();
                etcity.setError("field required");
            }
            if (PincodeHolder.equals("")) {
                etpincode.requestFocus();
                etpincode.setError("field required");
            }
            CheckEditText = false;

        } else {

            CheckEditText = true;
        }

    }
    public void UpdateDoctorRegistration(String cl_name, String fees,String valid_day, String day,String m_from1,String m_to1,String a_from1,
                                         String a_to1, String e_from1, String e_to1, String other_day,String m_from2,String m_to2, String a_from2, String a_to2, String e_from2,String e_to2,String contact,String state,String city,String landmark,String pincode,String address,String id,String image,String image2,String image3) {


        class UpdateDoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ParaUpdateClinicalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                if (httpResponseMsg.equals("success")) {
                    Toast.makeText(ParaUpdateClinicalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ParaUpdateClinicalActivity.this, ParaDashboard.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(ParaUpdateClinicalActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("cl_name", params[0]);
                hashMap.put("fees", params[1]);
                hashMap.put("valid_day", params[2]);
                hashMap.put("day", params[3]);
                hashMap.put("m_from1", params[4]);
                hashMap.put("m_to1", params[5]);
                hashMap.put("a_from1", params[6]);
                hashMap.put("a_to1", params[7]);
                hashMap.put("e_from1", params[8]);
                hashMap.put("e_to1", params[9]);
                hashMap.put("other_day", params[10]);
                hashMap.put("m_from2", params[11]);
                hashMap.put("m_to2", params[12]);
                hashMap.put("a_from2", params[13]);
                hashMap.put("a_to2", params[14]);
                hashMap.put("e_from2", params[15]);
                hashMap.put("e_to2", params[16]);
                hashMap.put("contact", params[17]);
                hashMap.put("state", params[18]);
                hashMap.put("city", params[19]);
                hashMap.put("landmark", params[20]);
                hashMap.put("pincode", params[21]);
                hashMap.put("address", params[22]);
                hashMap.put("id", params[23]);
                hashMap.put("image", params[24]);
                hashMap.put("image2", params[25]);
                hashMap.put("image3", params[26]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        UpdateDoctorRegistrationClass updatedoctorRegistrationClass = new UpdateDoctorRegistrationClass();

        updatedoctorRegistrationClass.execute(cl_name,fees,valid_day,day,m_from1,m_to1,a_from1,a_to1,e_from1,e_to1,
                other_day,m_from2,m_to2,a_from2,a_to2,e_from2,e_to2,contact,state,city,landmark,pincode,address,id,image,image2,image3);
    }
    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

              //  progressDialog = ProgressDialog.show(ParaUpdateClinicalActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

              //  progressDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                s = httpResponseMsg;
                new GetHttpResponse(ParaUpdateClinicalActivity.this).execute();
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
                            ImageHolder2 = jobj.getString("clinic_img2");
                            ImageHolder3 = jobj.getString("clinic_img3");
                            if (!ImageHolder.equals("clinic_img")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ayulr.com/clinic_img/"+ImageHolder).openStream();

                                bitmap = BitmapFactory.decodeStream(is);

                            }
                            if (!ImageHolder2.equals("clinic_img2")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ayulr.com/clinic_img/"+ImageHolder2).openStream();
                                bitmap2 = BitmapFactory.decodeStream(is);

                            }
                            if (!ImageHolder3.equals("clinic_img3")) {
                                // byte[] b = Base64.decode(ImageHolder, Base64.DEFAULT);
                                InputStream is = new java.net.URL("http://ayulr.com/clinic_img/"+ImageHolder3).openStream();
                                bitmap3 = BitmapFactory.decodeStream(is);

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
            if (HospitalHolder.equals("null")){
                ethospital.append("");
            }else {
                ethospital.append(HospitalHolder);
            }if (FeeHolder.equals("null")){
                etfee.append("");
            }else {
                etfee.append(FeeHolder);
            }
            if (FeeValidHolder.equals("null")){
                etfeevalid.append("");
            }else{
                etfeevalid.append(FeeValidHolder);
            }
            if (VisitHolder.equals("null")){
                etvisitday.append("");
            }else {
                etvisitday.append(VisitHolder);
            }
            if (MrngHolder.equals("null")){
                etmrngtime.append("");
            }else {
                etmrngtime.append(MrngHolder);
            }
            if (MrngToHolder.equals("null")){
                etmrngtimeto.append("");
            }else{
                etmrngtimeto.append(MrngToHolder);
            }
            if (AfterHolder.equals("null")){
                etaftertime.append("");
            }else {
                etaftertime.append(AfterHolder);
            }
            if(AfterToHolder.equals("null")){
                etafterto.append("");
            }else{
                etafterto.append(AfterToHolder);
            }
            if (EvngHolder.equals("null")){
                etevngtime.append("");
            }else {
                etevngtime.append(EvngHolder);
            }if(EvngToHolder.equals("null")){
                etevngto.append("");
            }else {
                etevngto.append(EvngToHolder);
            }
            if (OtherVisitHoder.equals("null")){
                etothervisitday.append("");
            }else {
                etothervisitday.append(OtherVisitHoder);
            }
            if (OtherMrngHolder.equals("null")){
                etothermrngtime.append("");
            }else {
                etothermrngtime.append(OtherMrngHolder);
            }
            if (OtherMrngToHoder.equals("null")){
                etothermrngto.append("");
            }else {
                etothermrngto.append(OtherMrngToHoder);
            }if (OtherAfterHolder.equals("null")){
                etotheraftertime.append("");
            }else {
                etotheraftertime.append(OtherAfterHolder);
            }if (OtherAfterToHolder.equals("null")){
                etotherafterto.append("");
            }else {
                etotherafterto.append(OtherAfterToHolder);
            }if (OtherEvngHolder.equals("null")){
                etotherevngtime.append("");
            }else{
                etotherevngtime.append(OtherEvngHolder);
            }
            if (OtherEvngToHolder.equals("null")){
                etotherevngto.append("");
            }else {
                etotherevngto.append(OtherEvngToHolder);
            }
            if (ContactHolder.equals("null")){
                etcontact.append("");
            }else {
                etcontact.append(ContactHolder);
            }if (StateHolder.equals("null")){
                etstate.append("");
            }else{
                etstate.append(StateHolder);
            }
            if (CityHolder.equals("null")){
                etcity.append("");
            }else {
                etcity.append(CityHolder);
            }if (LandmarkHolder.equals("null")){
                etlandmark.append("");
            }else {
                etlandmark.append(LandmarkHolder);
            }if(PincodeHolder.equals("null")){
                etpincode.append("");
            }else {
                etpincode.append(PincodeHolder);
            }
            if (AddressHolder.equals("null")){
                etaddress.append("");
            }else {
                etaddress.append(AddressHolder);
            }
            if (bitmap==null) {
                drimageView.setImageResource(R.drawable.img_upload);
            }else{
                drimageView.setImageBitmap(bitmap);
            }
            if (bitmap2==null||bitmap2.equals("")) {
                imageView2.setImageResource(R.drawable.img_upload);
            }else{
                imageView2.setImageBitmap(bitmap2);
            }
            if (bitmap3==null||bitmap3.equals("")) {
                imageView3.setImageResource(R.drawable.img_upload);
            }else{
                imageView3.setImageBitmap(bitmap3);
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }


}
