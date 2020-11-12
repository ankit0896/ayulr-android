package com.oxygen.micro.ayulr;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.oxygen.micro.ayulr.doctor.activity.ActivityLogin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ClinicActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.drhospital)
    TextInputEditText ethospital;
    @BindView(R.id.drfee)
    TextInputEditText etfee;
    @BindView(R.id.drfeevalidday)
    TextInputEditText etfeevalid;
    @BindView(R.id.drvisitday)
    EditText etvisitday;
    @BindView(R.id.drpincode)
    TextInputEditText etpincode;
    @BindView(R.id.drlandmark)
    TextInputEditText etlandmark;
    @BindView(R.id.draddress)
    TextInputEditText etaddress;
    @BindView(R.id.drcontactno)
    TextInputEditText etcontact;
    @BindView(R.id.drothervisitday)
    EditText etothervisitday;
    @BindView(R.id.state_spinner)
    Spinner state_spinner;
    @BindView(R.id.city_spinner)
    Spinner city_spinner;
    @BindView(R.id.btnsub)
    Button buttonclinic;
    @BindView(R.id.drimage)
    ImageView drimageView;
    static EditText etmrngtime, etmrngtimeto,etaftertime,etafterto, etevngtime,etevngto,
    etothermrngtime,etothermrngto,etotheraftertime,etotherafterto,etotherevngtime,etotherevngto;
    String HospitalHolder,FeeHolder,FeeValidHolder,VisitHolder,MrngHolder,MrngToHolder,AfterHolder,AfterToHolder,EvngHolder,EvngToHolder,
    OtherVisitHoder,OtherMrngHolder,OtherMrngToHoder,OtherAfterHolder,OtherAfterToHolder,OtherEvngHolder,OtherEvngToHolder,
    ContactHolder,StateHolder,CityHolder,LandmarkHolder,PincodeHolder,AddressHolder,EmailHolder;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://ameygraphics.com/ayulr/api/clinical.php";
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
    String ImageHolder;
    Bitmap bitmap;
    String HttpURL1 = "https://ameygraphics.com/ayulr/api/filter_city.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String se;
    ClinicalCityAdapter cityAdapter;
    String CodeHolder;
    static ArrayList<String> cityList= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageHolder="Noimage.jpeg";
        Intent intent=getIntent();
        EmailHolder=intent.getStringExtra("email");
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
        buttonclinic.setOnClickListener(this);
        listitem = getResources().getStringArray(R.array.days_array);
        checkeditem = new boolean[listitem.length];
        listitem1 = getResources().getStringArray(R.array.days_array);
        checkeditem1 = new boolean[listitem1.length];
        cityAdapter = new ClinicalCityAdapter(this);
        StateAdapter stateAdapter = new StateAdapter(ClinicActivity.this, State,Code);
        state_spinner.setAdapter(stateAdapter);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //   Select Provider
                TextView state_TV = (TextView) view.findViewById(R.id.state_TV);
                TextView code_TV = (TextView) view.findViewById(R.id.code_TV);
                StateHolder = state_TV.getText().toString();
                CodeHolder = code_TV.getText().toString();
                HttpWebCall(CodeHolder);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //   Select Provider
                TextView city_TV = (TextView) view.findViewById(R.id.city_TV);
                CityHolder = city_TV.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        drimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.drimage:
                        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryintent.setType("image/*");
                        startActivityForResult(galleryintent, Result_Load_Image);
                        break;
                }

            }
        });
        etvisitday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder MBuilder = new AlertDialog.Builder(ClinicActivity.this);
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
        });
        etothervisitday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder MBuilder = new AlertDialog.Builder(ClinicActivity.this);
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
        });
        etmrngtime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
               // DialogFragment newFragment = new TimePickerFragment();
                //newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener, mrngtimeHour, mrngtimeMinute, false).show();
            }
        });
        etmrngtimeto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
              //  DialogFragment newFragment = new TimePickerFragment1();
               // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID1);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener1, mrngtoHour, mrngtoMinute, false).show();
            }
        });
        etevngtime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
               /// DialogFragment newFragment = new TimePickerFragment2();
               // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID2);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener2, evngtimeHour, evngtimeMinute, false).show();
            }
        });
        etevngto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
               // DialogFragment newFragment = new TimePickerFragment3();
               // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID3);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener3, evngtoHour, evngtoMinute, false).show();
            }
        });
        etaftertime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
                //DialogFragment newFragment = new TimePickerFragment4();
              //  newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID4);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener4, aftertimeHour, aftertimeMinute, false).show();
            }
        });
        etafterto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
               // DialogFragment newFragment = new TimePickerFragment5();
               // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID5);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener5, aftertoHour, aftertoMinute, false).show();
            }
        });


        buttonclinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();
                if (NetworkDetactor.isNetworkAvailable(ClinicActivity.this)) {
                    if (CheckEditText) {
                        if (!(StateHolder.equals("Select State"))) {
                            DoctorRegistration(HospitalHolder, FeeHolder, FeeValidHolder, VisitHolder, MrngHolder, MrngToHolder, AfterHolder,
                                    AfterToHolder, EvngHolder, EvngToHolder, OtherVisitHoder, OtherMrngHolder, OtherMrngToHoder,
                                    OtherAfterHolder, OtherAfterToHolder, OtherEvngHolder, OtherEvngToHolder, ContactHolder, StateHolder,
                                    CityHolder, LandmarkHolder, PincodeHolder, AddressHolder, EmailHolder, ImageHolder);

                        } else {
                            Toast.makeText(ClinicActivity.this, "Select State", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                }
             else{
                Toast.makeText(ClinicActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        }
        });
        etothermrngtime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
                //DialogFragment newFragment = new TimePickerFragment6();
                //newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID6);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener6, othermrngtimeHour, othermrngtimeMinute, false).show();
            }
        });
        etothermrngto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
              //  DialogFragment newFragment = new TimePickerFragment7();
               // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID7);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener7, othermrngtoHour, othermrngtoMinute, false).show();
            }
        });
        etotherevngtime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
               // DialogFragment newFragment = new TimePickerFragment8();
               // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID8);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener8, otherevngtimeHour, otherevngtimeMinute, false).show();
            }
        });
        etotherevngto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
               // DialogFragment newFragment = new TimePickerFragment9();
                //newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID9);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener9, otherevngtoHour, otherevngtoMinute, false).show();
            }
        });
        etotheraftertime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
                //DialogFragment newFragment = new TimePickerFragment10();
               // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID10);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener10, otheraftertimeHour, otheraftertimeMinute, false).show();
            }
        });
        etotherafterto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
               // DialogFragment newFragment = new TimePickerFragment11();
               // newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID11);
                new TimePickerDialog(ClinicActivity.this, mTimeSetListener11, otheraftertoHour, otheraftertoMinute, false).show();
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Result_Load_Image &&resultCode ==RESULT_OK && data !=null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                drimageView.setImageBitmap(bitmap);
                ImageHolder = getStringImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStreamObject;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        return ConvertImage;

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

 /*   public static class TimePickerFragment11 extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default time
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of DatePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // get selected time
            AtomicReference<String> AM_PM = new AtomicReference<String>();
            if (hourOfDay < 12) {
                AM_PM.set("AM");
            } else {
                AM_PM.set("PM");
            }
            otheraftertoHour = hourOfDay;
            otheraftertoMinute = minute;

            // show selected time to time button
            etotherafterto.setText(new StringBuilder()
                    .append((otheraftertoHour)).append(":")
                    .append((otheraftertoMinute)).append(":")
                    .append(AM_PM));
        }
    }*/

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
        LandmarkHolder = etlandmark.getText().toString();
        PincodeHolder = etpincode.getText().toString();
        AddressHolder = etaddress.getText().toString();
        if (HospitalHolder.equals("") || FeeHolder.equals("")||FeeValidHolder.equals("") || VisitHolder.equals("")
                ||(ContactHolder.equals(""))||(PincodeHolder.equals(""))||(AddressHolder.equals(""))){
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
    public void DoctorRegistration(String cl_name, String fees,String valid_day, String day,String m_from1,String m_to1,String a_from1,
                                   String a_to1, String e_from1, String e_to1, String other_day,String m_from2,String m_to2, String a_from2, String a_to2, String e_from2,String e_to2,String contact,String state,String city,String landmark,String pincode,String address,String email,String image) {


        class DoctorRegistrationClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ClinicActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
if (httpResponseMsg.equals("success")) {

    Toast.makeText(ClinicActivity.this, "You are registered Successfully\nPlease login to access your account", Toast.LENGTH_LONG).show();
    Intent intent= new Intent(ClinicActivity.this, ActivityLogin.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
   // Toast.makeText(ClinicActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
   //Intent intent = new Intent(ClinicActivity.this, TabActivity.class);
   // intent.putExtra("email", EmailHolder);
   // startActivity(intent);
   // finish();
}else {
    Toast.makeText(ClinicActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
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
                hashMap.put("email", params[23]);
                hashMap.put("image", params[24]);
                Log.e("some value=", " " + hashMap);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                Log.e("some value=", " " + finalResult);
                return finalResult;
            }
        }

        DoctorRegistrationClass doctorRegistrationClass = new DoctorRegistrationClass();

        doctorRegistrationClass.execute(cl_name,fees,valid_day,day,m_from1,m_to1,a_from1,a_to1,e_from1,e_to1,
                other_day,m_from2,m_to2,a_from2,a_to2,e_from2,e_to2,contact,state,city,landmark,pincode,address,email,image);
    }
    String[] State = {
            "Select State",
            "Andaman And Nicobar Island",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chandigarh",
            "Chhattisgarh",
            "Dadra & Nagar Haveli",
            "Daman & Diu",
            "Delhi",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jammu & Kashmir",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Lakshadweep",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Pondicherry",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal",
    };
    String[] Code = {
            "",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "19",
            "20",
            "21",
            "22",
            "23",
            "24",
            "25",
            "26",
            "29",
            "31",
            "32",
            "33",
            "34",
            "35",
            "36",
            "37",
            "38",
            "39",
            "41",



    };
    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);


                se = httpResponseMsg;
                MyAsync myAsync = new MyAsync();
                myAsync.execute();



            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("state", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL1);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);

    }

    void clearData(){
        cityList.clear();



    }

    @Override
    public void onClick(View v) {

    }

    class MyAsync extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ClinicActivity.this, "Processing", "please wait moment...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String server_url = "https://ameygraphics.com/ayulr/api/fetch_city.php";

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
            clearData();
            try {
                if (se != null) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(se);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(i);
                            cityList.add(jobj.getString("name"));
                            city_spinner.setAdapter(cityAdapter);

                        }
                    } catch (Exception e) {
                       // Toast.makeText(ClinicActivity.this, "No Previews City"+e, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
               // Toast.makeText(ClinicActivity.this, "No Previews City"+e, Toast.LENGTH_SHORT).show();
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


}
