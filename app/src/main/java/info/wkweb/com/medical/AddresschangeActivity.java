package info.wkweb.com.medical;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class AddresschangeActivity extends AppCompatActivity {

    public static String str_select_addtype;
    TextView text_back_addresss,text_addd_save,text_addchange_descip;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    EditText edit_addd_fullname,edit_adddress ,edit_landmark_addd2,edit_pncode_addd,edit_mobilenumber_addd;


    ProgressDialog progressDialog;
    AlertDialog alert11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresschange);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        edit_addd_fullname = (EditText)findViewById(R.id.edit_addd_fullname);
        edit_adddress = (EditText)findViewById(R.id.edit_adddress);
        edit_landmark_addd2 = (EditText)findViewById(R.id.edit_landmark_addd);
        edit_pncode_addd = (EditText)findViewById(R.id.edit_pncode_addd);
        edit_mobilenumber_addd = (EditText)findViewById(R.id.edit_mobilenumber_addd);

        text_addchange_descip= (TextView) findViewById(R.id.text_addchange_descip);
        text_back_addresss= (TextView) findViewById(R.id.text_back_addresss);
        text_addd_save= (TextView) findViewById(R.id.text_addd_save);

        edit_addd_fullname.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkparameters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_adddress.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkparameters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        edit_mobilenumber_addd.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkparameters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_landmark_addd2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkparameters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_pncode_addd.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkparameters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (str_select_addtype.equalsIgnoreCase("address1"))
        {

            edit_addd_fullname.setText(pref.getString("custname1",""));
            edit_adddress.setText(pref.getString("address1",""));
            edit_landmark_addd2.setText("");
            edit_pncode_addd.setText(pref.getString("pincode1",""));
            edit_mobilenumber_addd.setText(pref.getString("mobile1",""));

            text_addchange_descip.setVisibility(View.VISIBLE);
            edit_adddress.setEnabled(true);
            edit_landmark_addd2.setEnabled(false);
            edit_adddress.setVisibility(View.VISIBLE);
            edit_landmark_addd2.setVisibility(View.GONE);
            edit_pncode_addd.setEnabled(false);
                    edit_mobilenumber_addd.setEnabled(false);
        }
        else if (str_select_addtype.equalsIgnoreCase("address2"))
        {
            edit_landmark_addd2.setText(pref.getString("address2",""));
            edit_addd_fullname.setText(pref.getString("custname2",""));
            edit_adddress.setText("");
            edit_pncode_addd.setText(pref.getString("pincode2",""));
            edit_mobilenumber_addd.setText(pref.getString("mobile2",""));
            edit_adddress.setEnabled(true);

            edit_landmark_addd2.setEnabled(true);
            edit_pncode_addd.setEnabled(false);
            edit_landmark_addd2.setVisibility(View.VISIBLE);
            edit_adddress.setVisibility(View.GONE);
            text_addchange_descip.setVisibility(View.GONE);
            edit_mobilenumber_addd.setEnabled(true);

            checkparameters();
        }
        else
        {
            edit_adddress.setEnabled(true);
            edit_landmark_addd2.setEnabled(true);
            edit_pncode_addd.setEnabled(true);
            edit_mobilenumber_addd.setEnabled(false);
        }


        text_back_addresss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        text_addd_save.setOnTouchListener(new View.OnTouchListener()
        {
            View v;
            private GestureDetector gestureDetector = new GestureDetector(AddresschangeActivity.this, new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onDoubleTap(MotionEvent e)
                {

                    Toast.makeText(AddresschangeActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {


                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {


                        final String str_add1 = String.valueOf(edit_adddress.getText()) + "\n" + "Phone No: " + pref.getString("mobile1", "") + "\n" + "Pincode: " + pref.getString("pincode1", "");
                        final String str_add2 = String.valueOf(edit_landmark_addd2.getText()) + "\n" + "Phone No: " + String.valueOf(edit_mobilenumber_addd.getText()) + "\n" + "Pincode: " + String.valueOf(edit_pncode_addd.getText());

                        if (str_select_addtype.equalsIgnoreCase("address1")) {

                            CartFragment.str_address = str_add1;
                            CartFragment.str_stringname = String.valueOf(edit_addd_fullname.getText());
                            Intent intent1 = new Intent("updateaddress");
                            LocalBroadcastManager.getInstance(AddresschangeActivity.this).sendBroadcast(intent1);
                            new communication_updateaddress().execute();
                            finish();
                        } else if (str_select_addtype.equalsIgnoreCase("address2")) {

                            CartFragment.str_address = str_add2;
                            CartFragment.str_stringname = String.valueOf(edit_addd_fullname.getText());
                            Intent intent1 = new Intent("updateaddress");
                            LocalBroadcastManager.getInstance(AddresschangeActivity.this).sendBroadcast(intent1);
                            new communication_updateaddress().execute();
                            finish();

                        }
                    }
                    else
                    {
                        Connections();
                    }

                    return super.onSingleTapConfirmed(e);
                }



            });

            @Override
            public boolean onTouch(View v1, MotionEvent event) {

                v = v1;

                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

//        text_addd_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final String str_add1 = String.valueOf(edit_adddress.getText()) +"\n"+"Phone No: "+pref.getString("mobile1","")+"\n"+"Pincode: "+pref.getString("pincode1","");
//                final String str_add2 =String.valueOf(edit_landmark_addd2.getText()) +"\n"+"Phone No: "+String.valueOf(edit_mobilenumber_addd.getText()) +"\n"+"Pincode: "+String.valueOf(edit_pncode_addd.getText());
//
//                if (str_select_addtype.equalsIgnoreCase("address1"))
//                {
//
//                    CartFragment.str_address = str_add1;
//                    CartFragment.str_stringname =  String.valueOf(edit_addd_fullname.getText());
//                    Intent intent1 = new Intent("updateaddress");
//                    LocalBroadcastManager.getInstance(AddresschangeActivity.this).sendBroadcast(intent1);
//                    new communication_updateaddress().execute();
//                    finish();
//                }
//                else if (str_select_addtype.equalsIgnoreCase("address2"))
//                {
//
//                    CartFragment.str_address = str_add2;
//                    CartFragment.str_stringname = String.valueOf(edit_addd_fullname.getText());
//                    Intent intent1 = new Intent("updateaddress");
//                    LocalBroadcastManager.getInstance(AddresschangeActivity.this).sendBroadcast(intent1);
//                    new communication_updateaddress().execute();
//                    finish();
//
//                }
//
//
//
//            }
//        });
    }
//    public class communication_updateprofile extends AsyncTask<String, Void, String> {
//
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected String doInBackground(String... args0) {
//
//            try {
//                URL url = new URL(Urlclass.updateprofile);
//                JSONObject postDataParams = new JSONObject();
//
//
//                postDataParams.put("mobile",pref.getString("userid",""));
//                postDataParams.put("custid",pref.getString("custid",""));
//                postDataParams.put("custname",edit_addd_fullname.getText());
//                postDataParams.put("address1",edit_adddress.getText());
//                postDataParams.put("address2",edit_landmark_addd2.getText());
//                postDataParams.put("pincode",edit_pncode_addd.getText());
//
//                Log.e("params", postDataParams.toString());
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000 /* milliseconds */);
//                conn.setConnectTimeout(15000 /* milliseconds */);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataString(postDataParams));
//
//                writer.flush();
//                writer.close();
//                os.close();
//
//                int responseCode = conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                    BufferedReader in = new BufferedReader(new
//                            InputStreamReader(
//                            conn.getInputStream()));
//
//                    StringBuffer sb = new StringBuffer("");
//                    String line = "";
//
//                    while ((line = in.readLine()) != null) {
//
//                        sb.append(line);
//                        break;
//                    }
//
//                    in.close();
//                    return sb.toString();
//
//                } else {
//                    return new String("false : " + responseCode);
//                }
//            } catch (Exception e) {
//                return new String("Exception: " + e.getMessage());
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            Log.d("resuult", result);
//            progressDialog.dismiss();
//            if (result !=null)
//            {
//                if (!result.equalsIgnoreCase(""))
//                {
//
//                    if (result.equalsIgnoreCase("error"))
//                    {
//
//
//
//                    }
//                    else if (result.equalsIgnoreCase("done"))
//                    {
//
//                        editor.putString("custname", String.valueOf(edit_addd_fullname.getText()));
//                        editor.putString("address1", String.valueOf(edit_adddress.getText()));
//                        editor.putString("address2", String.valueOf(edit_landmark_addd2.getText()));
//                        editor.putString("pincode", String.valueOf(edit_pncode_addd.getText()));
//                        editor.commit();
//
//                                    Intent intent1 = new Intent("updateprofile");
//                                   LocalBroadcastManager.getInstance(AddresschangeActivity.this).sendBroadcast(intent1);
//
//
//                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AddresschangeActivity.this);
//                        builder2.setTitle("Successful!");
//                        builder2.setMessage("Your profile has been updated.");
//                        builder2.setCancelable(false);
//                        builder2.setPositiveButton("Ok",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                        finish();
//                                    }
//                                });
//                        alert11 = builder2.create();
//                        alert11.show();
//
//
//                    }
//                    if (result.equalsIgnoreCase("updateerror"))
//                    {
//                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AddresschangeActivity.this);
//                        builder2.setTitle("Oops");
//                        builder2.setMessage("Your deatils could not update. Please try again.");
//                        builder2.setCancelable(false);
//                        builder2.setPositiveButton("Ok",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                        // finish();
//                                    }
//                                });
//                        alert11 = builder2.create();
//                        alert11.show();
//
//
//                    }
//
//                }
//                else
//                {
//                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AddresschangeActivity.this);
//                    builder2.setTitle("Oops");
//                    builder2.setMessage("Server could not found.");
//                    builder2.setCancelable(false);
//                    builder2.setPositiveButton("Ok",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    // finish();
//                                }
//                            });
//                    alert11 = builder2.create();
//                    alert11.show();
//
//                }
//            }
//            else
//            {
//                AlertDialog.Builder builder2 = new AlertDialog.Builder(AddresschangeActivity.this);
//                builder2.setTitle("Oops");
//                builder2.setMessage("Server could not found.");
//                builder2.setCancelable(false);
//                builder2.setPositiveButton("Ok",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                // finish();
//                            }
//                        });
//                alert11 = builder2.create();
//                alert11.show();
//
//            }
//
//        }
//
//    }
    public  void checkparameters()
    {
        if (str_select_addtype.equalsIgnoreCase("address1"))
        {
            if (edit_addd_fullname.getText().length() != 0 && edit_adddress.getText().length() != 0 ) {

                text_addd_save.setEnabled(true);
                text_addd_save.setBackgroundResource(R.drawable.round_green1);
                text_addd_save.setTextColor(getResources().getColor(R.color.colorwhite));
            } else {
                text_addd_save.setEnabled(false);
                text_addd_save.setBackgroundResource(R.drawable.round_lightgray);
                text_addd_save.setTextColor(getResources().getColor(R.color.colorblack));

            }
        }
        else {
            if (edit_addd_fullname.getText().length() != 0 && edit_landmark_addd2.getText().length() != 0
                    && edit_mobilenumber_addd.getText().length() != 0 && edit_pncode_addd.getText().length() != 0){

                text_addd_save.setEnabled(true);
                text_addd_save.setBackgroundResource(R.drawable.round_green1);
                text_addd_save.setTextColor(getResources().getColor(R.color.colorwhite));
            } else
                {
                text_addd_save.setEnabled(false);
                text_addd_save.setBackgroundResource(R.drawable.round_lightgray);
                text_addd_save.setTextColor(getResources().getColor(R.color.colorblack));

            }
        }
    }

    public class communication_updateaddress extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args0) {

            try {
                URL url = new URL(Urlclass.updateaddress);
                JSONObject postDataParams = new JSONObject();



                postDataParams.put("custid",pref.getString("custid",""));
                postDataParams.put("custname",edit_addd_fullname.getText());
                postDataParams.put("pincode",edit_pncode_addd.getText());
                postDataParams.put("select",str_select_addtype);
                if (str_select_addtype.equalsIgnoreCase("address1"))
                {
                    postDataParams.put("address",edit_adddress.getText());
                }
                else
                {
                    postDataParams.put("address",edit_landmark_addd2.getText());
                    postDataParams.put("mobile",edit_mobilenumber_addd.getText());
                }

                Log.e("params", postDataParams.toString());
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
        protected void onPostExecute(String result) {

            //Log.d("resuult", result);
         //   progressDialog.dismiss();
            if (result !=null)
            {
                if (!result.equalsIgnoreCase(""))
                {

                    if (result.equalsIgnoreCase("error"))
                    {



                    }
                    else if (result.equalsIgnoreCase("updated"))
                    {

                        if (str_select_addtype.equalsIgnoreCase("address1"))
                        {
                            editor.putString("custname", String.valueOf(edit_addd_fullname.getText()));
                            editor.putString("custname1", String.valueOf(edit_addd_fullname.getText()));
                            editor.putString("address1", String.valueOf(edit_adddress.getText()));
                            editor.commit();
                        }
                        else
                        {
                            editor.putString("mobile2", String.valueOf(edit_mobilenumber_addd.getText()));
                            editor.putString("custname2", String.valueOf(edit_addd_fullname.getText()));
                            editor.putString("address2", String.valueOf(edit_landmark_addd2.getText()));
                            editor.putString("pincode2", String.valueOf(edit_pncode_addd.getText()));
                            editor.commit();
                        }



//
//                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AddresschangeActivity.this);
//                        builder2.setTitle("Sucesfull!");
//                        builder2.setMessage("Your profile has been updated.");
//                        builder2.setCancelable(false);
//                        builder2.setPositiveButton("Ok",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                        finish();
//                                    }
//                                });
//                        alert11 = builder2.create();
//                        alert11.show();


                    }
                    if (result.equalsIgnoreCase("updateerror"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AddresschangeActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("Your deatils could not update. Please try again.");
                        builder2.setCancelable(false);
                        builder2.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        // finish();
                                    }
                                });
                        alert11 = builder2.create();
                        alert11.show();


                    }

                }
                else
                {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AddresschangeActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Server could not found.");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();

                }
            }
            else
            {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(AddresschangeActivity.this);
                builder2.setTitle("Oops");
                builder2.setMessage("Server could not found.");
                builder2.setCancelable(false);
                builder2.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                // finish();
                            }
                        });
                alert11 = builder2.create();
                alert11.show();

            }

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
    public  void Connections()
    {
        AlertDialog.Builder builder4 = new AlertDialog.Builder(AddresschangeActivity.this);
        builder4.setTitle("No Internet");
        builder4.setMessage("Your internet connection not available");
        builder4.setCancelable(false);
        builder4.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        alert11 = builder4.create();
        alert11.show();
    }
    public void onBackPressed() {

    }
}
