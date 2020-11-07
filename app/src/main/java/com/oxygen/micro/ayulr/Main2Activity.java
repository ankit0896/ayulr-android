package com.oxygen.micro.ayulr;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AlertDialog.Builder navDialog;
    ProgressDialog pDialog;
    String HttpURL1 = "https://ameygraphics.com/ayulr/api/filter_days.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String s;
    HttpParse httpParse = new HttpParse();
    String IdHolder, PlanHolder, DayHolder;
    TextView days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User user = SharedPrefManager.getInstance(this).getUser();
        IdHolder = String.valueOf(user.getId());
        if (NetworkDetactor.isNetworkAvailable(Main2Activity.this)) {
            HttpWebCall(IdHolder);
        } else {
            Toast.makeText(Main2Activity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
        Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tool, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActivityHome1 fragment = new ActivityHome1();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.screenarea, fragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changepass:
                Intent intent = new Intent(Main2Activity.this, PasswordActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();


        String title;
        if (id == R.id.term) {
            title = "Term of Services";
            String termsservices1 = getResources().getString(R.string.termsandservices);
            Spanned term_service_html1 = Html.fromHtml(termsservices1);
            settingDialog(term_service_html1, title);
            overridePendingTransition(R.animator.open_next, R.animator.close_next);
            return true;
        } else if (id == R.id.privacy) {
            title = "Privacy Policy";
            String termsservices1 = getResources().getString(R.string.privacy);
            Spanned term_service_html1 = Html.fromHtml(termsservices1);
            settingDialog(term_service_html1,title);
            overridePendingTransition(R.animator.open_next, R.animator.close_next);
            return true;
        } else if (id == R.id.about) {
            title = "About Us";
            String termsservices1 = getResources().getString(R.string.about);
            Spanned term_service_html1 = Html.fromHtml(termsservices1);
            settingDialog(term_service_html1,title);
            overridePendingTransition(R.animator.open_next, R.animator.close_next);
            return true;
        } else if (id == R.id.pacakage) {
            Intent intent = new Intent(getApplicationContext(), PacakageDealActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.logout) {
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            finish();
            return true;
        } else if (id == R.id.exit) {

            Main2Activity.this.finish();

            return true;
        } else if (id == R.id.share) {

            Intent sendInt = new Intent(Intent.ACTION_SEND);
            sendInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            sendInt.putExtra(Intent.EXTRA_TEXT, "get appointment in your pocket.Go with our " + getString(R.string.app_name)
                    + "\n" + getString(R.string.link));
            sendInt.setType("text/plain");
            startActivity(Intent.createChooser(sendInt, "Share"));
            overridePendingTransition(R.animator.open_next, R.animator.close_next);
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void settingDialog(Spanned content,String title) {
        navDialog = new AlertDialog.Builder(Main2Activity.this);
        //navDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //navDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        navDialog.setCancelable(true);
        navDialog.setTitle(title);
        // navDialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.settingelement_dialog, null);
        navDialog.setView(view);
        // navDialog.setContentView(R.layout.settingelement_dialog);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        // navDialog.getWindow().setLayout((8 * width) / 8, (13 * height) / 14);
        TextView dismisstv = view.findViewById(R.id.dismisstv);
        TextView navimgDialogTv = view.findViewById(R.id.navimgDialogTv);
        navimgDialogTv.setText(content);
        navDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
//        dismisstv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navDialog
//            }
//        });
        navDialog.show();
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
                Toast.makeText(Main2Activity.this, "" + s, Toast.LENGTH_SHORT).show();
                days = (TextView) findViewById(R.id.days);
                days.setText(s);


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


}
