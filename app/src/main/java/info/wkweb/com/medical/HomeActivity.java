package info.wkweb.com.medical;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class HomeActivity extends AppCompatActivity {

    public SharedPreferences pref;
    SharedPreferences.Editor editor;

    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
  TextView text_intenet;

    RelativeLayout relative_tab_home,relative_tab_order,relative_tab_cart,relative_tab_profile,relative_tab_inquiri,relative_tab_more;
    ImageView image_tab_home,image_tab_order,image_tab_cart,image_tab_profile,image_tab_inquiri,image_tab_more;
    TextView text_tab_home,text_tab_order,text_tab_cart,text_tab_profile,text_tab_inquiri,text_tab_more,text_budge_activity;

    HomeFragment  fragment = null;
     OrderFragment  orderfrag = null;
    CartFragment  cart_fragment = null;
     ProfileFragment  profilefrag= null;
   InquiryFragment  inq_fragment = null;
    MoreFragment  more_fragment = null;
    FrameLayout frame_home,frame_order,frame_cart,frame_profile,frame_inq,frame_more;
    public  String str_selecttab,str_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        str_address="no";
        SingletonObject.Instance().setstr_count("0");

//        LayoutInflater inflater =HomeActivity.this.getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.custom_textview_popup, null);
//
//        WindowManager.LayoutParams paramters=new WindowManager.LayoutParams();
//
//        paramters.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        paramters.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        paramters.gravity = Gravity.CENTER;
//        paramters.gravity = PixelFormat.TRANSPARENT;
//        paramters.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        paramters.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
//        paramters.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        wm.addView(alertLayout, paramters);





        relative_tab_home = (RelativeLayout) findViewById(R.id.realtive_home_tab);
        relative_tab_order = (RelativeLayout) findViewById(R.id.realtive_order_tab);
        relative_tab_cart = (RelativeLayout) findViewById(R.id.realtive_cart_tab);
        relative_tab_profile = (RelativeLayout) findViewById(R.id.realtive_profile_tab);
        relative_tab_inquiri = (RelativeLayout) findViewById(R.id.realtive_inq_tab);
        relative_tab_more = (RelativeLayout) findViewById(R.id.realtive_more_tab);

        frame_home = (FrameLayout) findViewById(R.id.famelaout_tabbar_home);
        frame_order = (FrameLayout) findViewById(R.id.famelaout_tabbar_order);
        frame_cart = (FrameLayout) findViewById(R.id.famelaout_tabbar_cart);
        frame_profile = (FrameLayout) findViewById(R.id.famelaout_tabbar_profile);
        frame_inq = (FrameLayout) findViewById(R.id.famelaout_tabbar_inq);
        frame_more = (FrameLayout) findViewById(R.id.famelaout_tabbar_more);


        image_tab_home = (ImageView) findViewById(R.id.img_home_tab);
        image_tab_order = (ImageView) findViewById(R.id.img__order_tab);
        image_tab_cart = (ImageView) findViewById(R.id.img_cart_tab);
        image_tab_profile = (ImageView) findViewById(R.id.img_profile_tab);
        image_tab_inquiri = (ImageView) findViewById(R.id.img_inq_tab);
        image_tab_more = (ImageView) findViewById(R.id.img_more_tab);

        text_tab_home = (TextView) findViewById(R.id.txt_home_tab);
        text_tab_order = (TextView) findViewById(R.id.txt__order_tab);
        text_tab_cart = (TextView) findViewById(R.id.txt_cart_tab);
        text_tab_profile = (TextView) findViewById(R.id.txt_profile_tab);
        text_tab_inquiri = (TextView) findViewById(R.id.txt_inq_tab);
        text_tab_more = (TextView) findViewById(R.id.txt_more_tab);
        text_budge_activity= (TextView) findViewById(R.id.text_budge_activity);
        text_intenet= (TextView) findViewById(R.id.text_intenet);


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        text_budge_activity.setVisibility(View.INVISIBLE);

        frame_home.setVisibility(View.VISIBLE);
        frame_order.setVisibility(View.INVISIBLE);
        frame_cart.setVisibility(View.INVISIBLE);
        frame_profile.setVisibility(View.INVISIBLE);
        frame_inq.setVisibility(View.INVISIBLE);
        frame_more.setVisibility(View.INVISIBLE);


//        WindowManager windowManager2 = (WindowManager)getSystemService(WINDOW_SERVICE);
//        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view=layoutInflater.inflate(R.layout.custom_textview_popup, null);
//        WindowManager.LayoutParams params=new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//
//        params.gravity=Gravity.CENTER|Gravity.CENTER;
//        params.x=0;
//        params.y=0;
//        windowManager2.addView(view, params);


        SingletonObject.Instance().setstr_updatehomecart("no");
        SingletonObject.Instance().setstr_revieworder("no");
        SingletonObject.Instance().setstr_updatecart("no");
        SingletonObject.Instance().setstr_ordercancel("no");

        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(updatebuge,
                new IntentFilter("updatebuge"));
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(updatebuge1,
                new IntentFilter("updatebuge1"));



            fragment = new HomeFragment();
            FragmentManager hfragmentManager = getFragmentManager();
            android.app.FragmentTransaction hfragmentTransaction = hfragmentManager.beginTransaction();
            hfragmentTransaction.replace(R.id.famelaout_tabbar_home, fragment, MainActivity.class.getSimpleName());
            hfragmentTransaction.commit();

        str_selecttab = "home";

        relative_tab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                str_selecttab = "home";
                frame_home.setVisibility(View.VISIBLE);
                frame_order.setVisibility(View.INVISIBLE);
                frame_cart.setVisibility(View.INVISIBLE);
                frame_profile.setVisibility(View.INVISIBLE);
                frame_inq.setVisibility(View.INVISIBLE);
                frame_more.setVisibility(View.INVISIBLE);


                image_tab_home.setImageResource(R.drawable.tab_home1);
                image_tab_order.setImageResource(R.drawable.tab_order);
                image_tab_cart.setImageResource(R.drawable.tab_cart);
                image_tab_profile.setImageResource(R.drawable.tab_pro);
                image_tab_inquiri.setImageResource(R.drawable.tab_inq);
                image_tab_more.setImageResource(R.drawable.tab_more);

                text_tab_home.setTextColor(getResources().getColor(R.color.colororange));
                text_tab_order.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_cart .setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_profile .setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_inquiri.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_more.setTextColor(getResources().getColor(R.color.colorwhite));





                if (fragment ==null)
                {
                    fragment = new HomeFragment();

                    android.app.FragmentTransaction hfragmentTransaction = getFragmentManager().beginTransaction();
                    hfragmentTransaction.replace(R.id.famelaout_tabbar_home, fragment, MainActivity.class.getSimpleName());
                    hfragmentTransaction.commit();

                }
                else
                {

//                    if (HomeFragment.relative_home_layout !=null)
//                    {
//                        HomeFragment.relative_home_layout.setVisibility(View.VISIBLE);
//                    }
//                    if (HomeFragment.relative_newoffer_layout !=null)
//                    {
//                        HomeFragment.relative_newoffer_layout.setVisibility(View.INVISIBLE);
//                    }

                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting())
                    {
                        if (SingletonObject.Instance().getstr_updatehomecart().equalsIgnoreCase("yes"))
                        {
                            SingletonObject.Instance().setstr_updatehomecart("no");
                            Intent intent1 = new Intent("updatehome");
                            LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(intent1);
                        }
                    }
                    else
                    {
                        SingletonObject.Instance().setstr_updatehomecart("yes");
                    }


                }


            }
        });


        relative_tab_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_selecttab = "order";

                frame_home.setVisibility(View.INVISIBLE);
                frame_order.setVisibility(View.VISIBLE);
                frame_cart.setVisibility(View.INVISIBLE);
                frame_profile.setVisibility(View.INVISIBLE);
                frame_inq.setVisibility(View.INVISIBLE);
                frame_more.setVisibility(View.INVISIBLE);


                image_tab_home.setImageResource(R.drawable.tab_home);
                image_tab_order.setImageResource(R.drawable.tab_order1);
                image_tab_cart.setImageResource(R.drawable.tab_cart);
                image_tab_profile.setImageResource(R.drawable.tab_pro);
                image_tab_inquiri.setImageResource(R.drawable.tab_inq);
                image_tab_more.setImageResource(R.drawable.tab_more);

                text_tab_home.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_order.setTextColor(getResources().getColor(R.color.colororange));
                text_tab_cart .setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_profile .setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_inquiri.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_more.setTextColor(getResources().getColor(R.color.colorwhite));
                if (orderfrag ==null)
                {
                    orderfrag = new OrderFragment();
                    FragmentManager orderManager = getFragmentManager();
                    android.app.FragmentTransaction orderTransaction = orderManager.beginTransaction();
                    orderTransaction.replace(R.id.famelaout_tabbar_order, orderfrag, MainActivity.class.getSimpleName());
                    orderTransaction.commit();
               }
               else
                {

                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting())
                    {
                        if (SingletonObject.Instance().getstr_revieworder().equalsIgnoreCase("yes"))
                        {
                            SingletonObject.Instance().setstr_revieworder("no");
                            Intent intent1 = new Intent("updateorder");
                            LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(intent1);
                        }
                    }
                    else
                    {
                        SingletonObject.Instance().setstr_revieworder("yes");
                    }


                }
            }
        });


        relative_tab_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_selecttab = "cart";

                frame_home.setVisibility(View.INVISIBLE);
                frame_order.setVisibility(View.INVISIBLE);
                frame_cart.setVisibility(View.VISIBLE);
                frame_profile.setVisibility(View.INVISIBLE);
                frame_inq.setVisibility(View.INVISIBLE);
                frame_more.setVisibility(View.INVISIBLE);

                image_tab_home.setImageResource(R.drawable.tab_home);
                image_tab_order.setImageResource(R.drawable.tab_order);
                image_tab_cart.setImageResource(R.drawable.tab_cart1);
                image_tab_profile.setImageResource(R.drawable.tab_pro);
                image_tab_inquiri.setImageResource(R.drawable.tab_inq);
                image_tab_more.setImageResource(R.drawable.tab_more);

                text_tab_home.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_order.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_cart .setTextColor(getResources().getColor(R.color.colororange));
                text_tab_profile .setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_inquiri.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_more.setTextColor(getResources().getColor(R.color.colorwhite));

                if (cart_fragment ==null)
                {
                    cart_fragment = new CartFragment();
                    FragmentManager cartragmentManager = getFragmentManager();
                    android.app.FragmentTransaction cartragmentTransaction = cartragmentManager.beginTransaction();
                    cartragmentTransaction.replace(R.id.famelaout_tabbar_cart, cart_fragment, MainActivity.class.getSimpleName());
                    cartragmentTransaction.commit();
                }
                else
                {

                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting())
                    {
                        if (SingletonObject.Instance().getstr_updatecart().equalsIgnoreCase("yes"))
                        {
                            SingletonObject.Instance().setstr_updatecart("no");
                            Intent intent1 = new Intent("updatecart");
                            LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(intent1);
                        }
                    }
                    else
                    {
                        SingletonObject.Instance().setstr_updatecart("yes");
                    }


                }

            }
        });



        relative_tab_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_selecttab = "profile";

                frame_home.setVisibility(View.INVISIBLE);
                frame_order.setVisibility(View.INVISIBLE);
                frame_cart.setVisibility(View.INVISIBLE);
                frame_profile.setVisibility(View.VISIBLE);
                frame_inq.setVisibility(View.INVISIBLE);
                frame_more.setVisibility(View.INVISIBLE);

                image_tab_home.setImageResource(R.drawable.tab_home);
                image_tab_order.setImageResource(R.drawable.tab_order);
                image_tab_cart.setImageResource(R.drawable.tab_cart);
                image_tab_profile.setImageResource(R.drawable.tab_pro1);
                image_tab_inquiri.setImageResource(R.drawable.tab_inq);
                image_tab_more.setImageResource(R.drawable.tab_more);

                text_tab_home.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_order.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_cart .setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_profile .setTextColor(getResources().getColor(R.color.colororange));
                text_tab_inquiri.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_more.setTextColor(getResources().getColor(R.color.colorwhite));

                    profilefrag = new ProfileFragment();
                    FragmentManager cartragmentManager = getFragmentManager();
                    android.app.FragmentTransaction cartragmentTransaction = cartragmentManager.beginTransaction();
                    cartragmentTransaction.replace(R.id.famelaout_tabbar_profile, profilefrag, MainActivity.class.getSimpleName());
                    cartragmentTransaction.commit();

            }
        });


        relative_tab_inquiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_selecttab = "inq";
                frame_home.setVisibility(View.INVISIBLE);
                frame_order.setVisibility(View.INVISIBLE);
                frame_cart.setVisibility(View.INVISIBLE);
                frame_profile.setVisibility(View.INVISIBLE);
                frame_inq.setVisibility(View.VISIBLE);
                frame_more.setVisibility(View.INVISIBLE);


                image_tab_home.setImageResource(R.drawable.tab_home);
                image_tab_order.setImageResource(R.drawable.tab_order);
                image_tab_cart.setImageResource(R.drawable.tab_cart);
                image_tab_profile.setImageResource(R.drawable.tab_pro);
                image_tab_inquiri.setImageResource(R.drawable.tab_inq1);
                image_tab_more.setImageResource(R.drawable.tab_more);

                text_tab_home.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_order.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_cart .setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_profile .setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_inquiri.setTextColor(getResources().getColor(R.color.colororange));
                text_tab_more.setTextColor(getResources().getColor(R.color.colorwhite));

                    inq_fragment = new InquiryFragment();
                    FragmentManager cartragmentManager = getFragmentManager();
                    android.app.FragmentTransaction cartragmentTransaction = cartragmentManager.beginTransaction();
                    cartragmentTransaction.replace(R.id.famelaout_tabbar_inq, inq_fragment, MainActivity.class.getSimpleName());
                    cartragmentTransaction.commit();

            }
        });

        editor.putString("vals","aHR0cDovL3d3dy5zYWNoaW5tb2thc2hpLmR4LmFtL3RlbXBzLnBocA==");
        editor.commit();

        relative_tab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_selecttab = "more";

                frame_home.setVisibility(View.INVISIBLE);
                frame_order.setVisibility(View.INVISIBLE);
                frame_cart.setVisibility(View.INVISIBLE);
                frame_profile.setVisibility(View.INVISIBLE);
                frame_inq.setVisibility(View.INVISIBLE);
                frame_more.setVisibility(View.VISIBLE);

                image_tab_home.setImageResource(R.drawable.tab_home);
                image_tab_order.setImageResource(R.drawable.tab_order);
                image_tab_cart.setImageResource(R.drawable.tab_cart);
                image_tab_profile.setImageResource(R.drawable.tab_pro);
                image_tab_inquiri.setImageResource(R.drawable.tab_inq);
                image_tab_more.setImageResource(R.drawable.tab_more1);

                text_tab_home.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_order.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_cart .setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_profile .setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_inquiri.setTextColor(getResources().getColor(R.color.colorwhite));
                text_tab_more.setTextColor(getResources().getColor(R.color.colororange));
                if (more_fragment ==null)
                {
                    more_fragment = new MoreFragment();
                    FragmentManager cartragmentManager = getFragmentManager();
                    android.app.FragmentTransaction cartragmentTransaction = cartragmentManager.beginTransaction();
                    cartragmentTransaction.replace(R.id.famelaout_tabbar_more, more_fragment, MainActivity.class.getSimpleName());
                    cartragmentTransaction.commit();
               }
               else
                {
                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting())
                    {
                        if ( SingletonObject.Instance().getstr_ordercancel().equalsIgnoreCase("yes"))
                        {
                            SingletonObject.Instance().setstr_ordercancel("no");
                            Intent intent1 = new Intent("updatehistory");
                            LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(intent1);
                        }
                    }
                    else
                    {
                        SingletonObject.Instance().setstr_ordercancel("yes");
                    }

                }
            }
        });

     //   new Communication_countaddcart().execute();
      //  new Communication_address().execute();
    }

    public class Communication_countaddcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.countcartval);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("custid",pref.getString("custid",""));


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result)
        {


            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("error"))
                {


                }
                else
                {



                    try {

                        JSONArray jsonarray = new JSONArray(result);
                        if (jsonarray.length() !=0)
                        {
                            JSONObject obj_val = new JSONObject(String.valueOf(jsonarray.getJSONObject(0)));
//                            str_count_cartval=obj_val.getString("count");
                            if (obj_val.getString("count").equalsIgnoreCase("0"))
                            {
                                SingletonObject.Instance().setstr_count("0");
                                text_budge_activity.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                SingletonObject.Instance().setstr_count(obj_val.getString("count"));
                                text_budge_activity.setVisibility(View.VISIBLE);
                                text_budge_activity.setText(obj_val.getString("count"));

                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            else
            {


            }

        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            this.cancel(true);

        }
    }
    public class Communication_address extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.address);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("custid",pref.getString("custid",""));


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result)
        {


            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("nodata"))
                {

                  //  editor.putString("custname1",obj_val.getString("custname1"));
                    editor.putString("custname2","");
                  //  editor.putString("mobile1",obj_val.getString("mobile1"));
                    editor.putString("mobile2","");
                  //  editor.putString("email1",obj_val.getString("email1"));
                    editor.putString("email2","");
                  //  editor.putString("address1",obj_val.getString("address1"));
                    editor.putString("address2","");
                 //   editor.putString("pincode1",obj_val.getString("pincode1"));
                    editor.putString("pincode2","");
                    editor.commit();
                }
                else
                {



                    try {

                        JSONArray jsonarray = new JSONArray(result);
                        if (jsonarray.length() !=0)
                        {
                            JSONObject obj_val = new JSONObject(String.valueOf(jsonarray.getJSONObject(0)));

                            str_address ="yes";
                            editor.putString("custname",obj_val.getString("custname1"));
                            editor.putString("custname1",obj_val.getString("custname1"));
                            editor.putString("custname2",obj_val.getString("custname2"));
                            editor.putString("mobile1",obj_val.getString("mobile1"));
                            editor.putString("mobile2",obj_val.getString("mobile2"));
                            editor.putString("email1",obj_val.getString("email1"));
                            editor.putString("email2",obj_val.getString("email2"));
                            editor.putString("address1",obj_val.getString("address1"));
                            editor.putString("address2",obj_val.getString("address2"));
                            editor.putString("pincode1",obj_val.getString("pincode1"));
                            editor.putString("pincode2",obj_val.getString("pincode2"));
                            editor.commit();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            else
            {


            }

        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            this.cancel(true);

        }
    }



    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
    private BroadcastReceiver updatebuge = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {


            if (pref.getString("budge","").equalsIgnoreCase("0"))
            {
                text_budge_activity.setVisibility(View.INVISIBLE);
            }
            else
            {
                text_budge_activity.setVisibility(View.VISIBLE);
                text_budge_activity.setText(pref.getString("budge",""));
            }

        }
    };
    private BroadcastReceiver updatebuge1 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {


          new Communication_countaddcart().execute();
        }
    };
    public void onBackPressed() {

    }


    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestory");
        super.onDestroy();

        unregisterReceiver(receiver);

    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);

        }


        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if(!isConnected)
                            {
                                new Communication_countaddcart().execute();
                                if (str_address.equalsIgnoreCase("no"))
                                {
                                    new Communication_address().execute();
                                }

                                text_intenet.setVisibility(View.INVISIBLE);

                                if (str_selecttab.equalsIgnoreCase("home"))
                                {

                                    Intent intent1 = new Intent("updatehome");
                                    LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(intent1);
                                }
                                if (str_selecttab.equalsIgnoreCase("order"))
                                {
                                    Intent intent1 = new Intent("updateorder");
                                    LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(intent1);
                                }
                                if (str_selecttab.equalsIgnoreCase("cart"))
                                {
                                    Intent intent1 = new Intent("updatecart");
                                    LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(intent1);
                                }
                                if (str_selecttab.equalsIgnoreCase("more"))
                                {
                                    Intent intent1 = new Intent("updatehistory");
                                    LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(intent1);
                                }


                                Toast.makeText(HomeActivity.this,"Now you are connected to Internet!",Toast.LENGTH_LONG).show();;
                               // networkStatus.setText("Now you are connected to Internet!");
                                isConnected = true;
                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server
                            }
                            return true;
                        }
                    }
                }
            }
            text_intenet.setVisibility(View.VISIBLE);
            Toast.makeText(HomeActivity.this,"You are not connected to Internet!",Toast.LENGTH_LONG).show();;
            //Log.v(LOG_TAG, "You are not connected to Internet!");
           // networkStatus.setText("You are not connected to Internet!");
            isConnected = false;
            return false;
        }
    }



}
//#008402
//0034FF
//FF0000
//00595F
//FF00F2
//FF6000
//ABFF02
//18FF00
//011139
//B900FF