package info.wkweb.com.medical;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class LoginActivity extends AppCompatActivity
{
    String str_emailid,str_devicetoken;
TextView text_login_login,text_login_signup;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    AlertDialog alert11;
    LinearLayout linear_login_forpass;
    EditText edittext_login_id,edittext_login_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        text_login_login = (TextView)findViewById(R.id.text_login_login);
        text_login_signup = (TextView)findViewById(R.id.text_login_signup);
        edittext_login_id =(EditText)findViewById(R.id.edittext_login_id);
        edittext_login_pass =(EditText)findViewById(R.id.edittext_login_pass);
        linear_login_forpass =(LinearLayout)findViewById(R.id.linear_login_forpass);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
            text_login_login.setEnabled(false);
            text_login_login.setTextColor(Color.WHITE);


        str_devicetoken =  "";//FirebaseInstanceId.getInstance().getToken();

        text_login_login.setOnTouchListener(new View.OnTouchListener()
        {
            View v;
            private GestureDetector gestureDetector = new GestureDetector(LoginActivity.this, new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onDoubleTap(MotionEvent e)
                {

                    Toast.makeText(LoginActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {

                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage("Login..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        new Communication_Signup().execute();
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


//        text_login_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                progressDialog = new ProgressDialog(LoginActivity.this);
//                progressDialog.setMessage("Login..."); // Setting Message
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//                new Communication_Signup().execute();
//
//            }
//        });




        linear_login_forpass.setOnTouchListener(new View.OnTouchListener()
        {
            View v;
            private GestureDetector gestureDetector = new GestureDetector(LoginActivity.this, new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onDoubleTap(MotionEvent e)
                {

                    Toast.makeText(LoginActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {



                        LayoutInflater inflater =LoginActivity.this.getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.custom_textview_popup, null);
                        TextView textview_title = alertLayout.findViewById(R.id.textview_alert_invite_title);
                        TextView textview_title_dialog = alertLayout.findViewById(R.id.textview_alert_invite_message);
                        final EditText textview_email_No_enter = alertLayout.findViewById(R.id.textview_emailtext);
                        final TextView textview_canced = alertLayout.findViewById(R.id.textview_alert_msg_cancel);
                        final TextView textview_ok = alertLayout.findViewById(R.id.textview_alert_msg_ok);

                        RelativeLayout backround_view = alertLayout.findViewById(R.id.cutom_dilaog_invited2);

                        textview_title.setText("Forgot Password");
                        textview_title_dialog.setText("To send your password, please enter your registered mobile number.");
                        textview_email_No_enter.setHint("Enter Mobile Number");
                        textview_email_No_enter.setInputType(InputType.TYPE_CLASS_NUMBER);

                        InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                        // this is set the view from XML inside AlertDialog
                        alert.setView(alertLayout);
                        // disallow cancel of AlertDialog on click of back button and outside touch
                        alert.setCancelable(false);
                        final AlertDialog dialog1 = alert.create();
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.show();


                        GradientDrawable bgShape = (GradientDrawable)backround_view.getBackground();
                        bgShape.mutate();
                        bgShape.setColor(Color.WHITE);

                        GradientDrawable bgShape1 = (GradientDrawable)textview_email_No_enter.getBackground();
                        bgShape1.mutate();
                        bgShape1.setColor(Color.WHITE);

                        textview_ok.setText("Send");
                        textview_ok.setEnabled(false);
                        textview_ok.setTextColor(getResources().getColor(R.color.colorlightgray));



                        textview_email_No_enter.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {


                                String emailValues= String.valueOf(textview_email_No_enter.getText());
                                if (emailValues.length() >= 10)
                                {
                                    textview_ok.setEnabled(true);
                                    textview_ok.setTextColor(getResources().getColor(R.color.colorPrimary));

                                } else
                                {
                                    textview_ok.setEnabled(false);
                                    textview_ok.setTextColor(getResources().getColor(R.color.colorlightgray));

                                }


                            }
                            @Override
                            public void afterTextChanged(Editable s) {

                            }

                        });


                        textview_canced.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {


                                str_emailid="";
                                dialog1.dismiss();

                            }
                        });



                    textview_ok.setOnTouchListener(new View.OnTouchListener()
                    {
                        View v;
                        private GestureDetector gestureDetector = new GestureDetector(LoginActivity.this, new GestureDetector.SimpleOnGestureListener() {


                            @Override
                            public boolean onDoubleTap(MotionEvent e)
                            {

                                Toast.makeText(LoginActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                                return super.onDoubleTap(e);
                            }

                            @Override
                            public boolean onSingleTapConfirmed(MotionEvent e) {


                                ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                                if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                                    str_emailid = textview_email_No_enter.getText().toString();
                                    //   str_emailAdd="";
                                    dialog1.dismiss();

                                    progressDialog = new ProgressDialog(LoginActivity.this);
                                    progressDialog.setMessage("Loading..."); // Setting Message
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                    progressDialog.show(); // Display Progress Dialog
                                    progressDialog.setCancelable(false);

                                  //  new communication_forgotpassword().execute();

                                    Intent intent = new Intent(LoginActivity.this, OtpmsgActivity.class);
                                    intent.putExtra("mobile", str_emailid);
                                    OtpmsgActivity.str_pagetype = "for";
                                    startActivity(intent);

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

//                        textview_ok.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view)
//                            {
//                                str_emailid=textview_email_No_enter.getText().toString();
//                                //   str_emailAdd="";
//                                dialog1.dismiss();
//
//                                progressDialog = new ProgressDialog(LoginActivity.this);
//                                progressDialog.setMessage("Loading..."); // Setting Message
//                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                                progressDialog.show(); // Display Progress Dialog
//                                progressDialog.setCancelable(false);
//
//                                new communication_forgotpassword().execute();
//
//                            }
//                        });


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


//        linear_login_forpass.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        LayoutInflater inflater =LoginActivity.this.getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.custom_textview_popup, null);
//        TextView textview_title = alertLayout.findViewById(R.id.textview_alert_invite_title);
//        TextView textview_title_dialog = alertLayout.findViewById(R.id.textview_alert_invite_message);
//        final EditText textview_email_No_enter = alertLayout.findViewById(R.id.textview_emailtext);
//        final TextView textview_canced = alertLayout.findViewById(R.id.textview_alert_msg_cancel);
//        final TextView textview_ok = alertLayout.findViewById(R.id.textview_alert_msg_ok);
//
//        RelativeLayout backround_view = alertLayout.findViewById(R.id.cutom_dilaog_invited2);
//
//        textview_title.setText("Forgot Password");
//        textview_title_dialog.setText("To send your password, please enter your registered mobile number.");
//        textview_email_No_enter.setHint("Enter Mobile Number");
//        textview_email_No_enter.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//        InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
//        // this is set the view from XML inside AlertDialog
//        alert.setView(alertLayout);
//        // disallow cancel of AlertDialog on click of back button and outside touch
//        alert.setCancelable(false);
//        final AlertDialog dialog1 = alert.create();
//        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog1.show();
//
//
//        GradientDrawable bgShape = (GradientDrawable)backround_view.getBackground();
//        bgShape.mutate();
//        bgShape.setColor(Color.WHITE);
//
//        GradientDrawable bgShape1 = (GradientDrawable)textview_email_No_enter.getBackground();
//        bgShape1.mutate();
//        bgShape1.setColor(Color.WHITE);
//
//        textview_ok.setText("Send");
//        textview_ok.setEnabled(false);
//        textview_ok.setTextColor(getResources().getColor(R.color.colorlightgray));
//
//
//
//        textview_email_No_enter.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                String emailValues= String.valueOf(textview_email_No_enter.getText());
//                if (emailValues.length() >= 10)
//                {
//                    textview_ok.setEnabled(true);
//                    textview_ok.setTextColor(getResources().getColor(R.color.colorPrimary));
//
//                } else
//                {
//                    textview_ok.setEnabled(false);
//                    textview_ok.setTextColor(getResources().getColor(R.color.colorlightgray));
//
//                }
//
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//
//        });
//
//
//        textview_canced.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//
//
//                str_emailid="";
//                dialog1.dismiss();
//
//            }
//        });
//
//        textview_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                str_emailid=textview_email_No_enter.getText().toString();
//                //   str_emailAdd="";
//                dialog1.dismiss();
//
//                progressDialog = new ProgressDialog(LoginActivity.this);
//                progressDialog.setMessage("Loading..."); // Setting Message
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                progressDialog.show(); // Display Progress Dialog
//                progressDialog.setCancelable(false);
//
//                new communication_forgotpassword().execute();
//
//            }
//        });
//    }
//});



        text_login_signup.setOnTouchListener(new View.OnTouchListener()
        {
            View v;
            private GestureDetector gestureDetector = new GestureDetector(LoginActivity.this, new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onDoubleTap(MotionEvent e)
                {

                    Toast.makeText(LoginActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {


                    SignUpActivity.str_viewpage="login";
                    Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                    startActivity(intent);
//                    Intent intent = new Intent(LoginActivity.this,OtpmsgActivity.class);
//                    startActivity(intent);

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

//
//        text_login_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                SignUpActivity.str_viewpage="login";
//                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
//                startActivity(intent);
//            }
//        });



        edittext_login_id.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (edittext_login_id.getText().length() !=0 && edittext_login_pass.getText().length() !=0)
                {

                    text_login_login.setEnabled(true);
                    text_login_login.setTextColor(getResources().getColor(R.color.colorblue));

                }

                else
                {
                    text_login_login.setEnabled(false);
                    text_login_login.setTextColor(Color.WHITE);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edittext_login_pass.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (edittext_login_id.getText().length() !=0 && edittext_login_pass.getText().length() !=0)
                {

                    text_login_login.setEnabled(true);
                    text_login_login.setTextColor(getResources().getColor(R.color.colorblue));

                }

                else
                {
                    text_login_login.setEnabled(false);
                    text_login_login.setTextColor(Color.WHITE);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



    }

    public class Communication_Signup extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.login);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile",  String.valueOf(edittext_login_id.getText()));
                postDataParams.put("password",  String.valueOf(edittext_login_pass.getText()));
                postDataParams.put("devicetoken",  str_devicetoken);

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

            progressDialog.dismiss();
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(LoginActivity.this);
                    builder4.setTitle("Oops");
                    builder4.setMessage("Could not retrieve field values. Please login and try again.");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder4.create();
                    alert11.show();


                }
                else if (result.equalsIgnoreCase("error"))
                {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(LoginActivity.this);
                    builder5.setMessage("You have been enter wrong credential. Please try again.");
                    builder5.setTitle("Oops");
                    builder5.setCancelable(false);
                    builder5.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder5.create();
                    alert11.show();

                }
                else if (result.equalsIgnoreCase("mobile"))
                {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(LoginActivity.this);
                    builder5.setMessage("You have been enter wrong mobile number. Please try again.");
                    builder5.setTitle("Oops");
                    builder5.setCancelable(false);
                    builder5.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder5.create();
                    alert11.show();

                }
                else if (result.equalsIgnoreCase("password"))
                {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(LoginActivity.this);
                    builder5.setMessage("You have been enter wrong password number. Please try again.");
                    builder5.setTitle("Oops");
                    builder5.setCancelable(false);
                    builder5.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder5.create();
                    alert11.show();

                }
                else if (result.equalsIgnoreCase("mobilepassword"))
                {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(LoginActivity.this);
                    builder5.setMessage("You have been enter wrong mobile number and password. Please try again.");
                    builder5.setTitle("Oops");
                    builder5.setCancelable(false);
                    builder5.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder5.create();
                    alert11.show();

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            JSONArray jsonarray = new JSONArray(result);

                            JSONObject obj_values=new JSONObject(String.valueOf(jsonarray.getString(0)));


                            if (jsonarray !=null) {

                                editor.putString("custid", obj_values.getString("custid"));
                                editor.putString("custname", obj_values.getString("custname"));
                                editor.putString("firmname", obj_values.getString("firmname"));
                                editor.putString("userid", obj_values.getString("mobile"));
                                editor.putString("email", obj_values.getString("email"));
                                editor.putString("address1", obj_values.getString("address1"));
                                // editor.putString("address2",obj_values.getString("address2"));
                                editor.putString("pincode", obj_values.getString("pincode"));
                                //pnacard//adarcard//votercard
                                editor.putString("idproftype", obj_values.getString("idproftype"));
                                editor.putString("idprof", obj_values.getString("idprof"));

                                editor.putString("drugno", obj_values.getString("drugno"));
                                editor.putString("foodno", obj_values.getString("foodno"));
                                editor.putString("gstno", obj_values.getString("gstno"));

                                editor.putString("custname1", obj_values.getString("custname"));
                                editor.putString("custname2", "");
                                editor.putString("mobile1", obj_values.getString("mobile"));
                                editor.putString("mobile2", "");
                                editor.putString("email1", obj_values.getString("email"));
                                editor.putString("email2", "");
                                editor.putString("address1", obj_values.getString("address1"));
                                editor.putString("address2", "");
                                editor.putString("pincode1", obj_values.getString("pincode"));
                                editor.putString("regtype", obj_values.getString("regtype"));
                                editor.putString("pincode2", "");
                                editor.commit();


                                if (obj_values.getString("regtype").equalsIgnoreCase("admin"))
                                {
                                    editor.putString("login", "yes");
                                    editor.commit();
                                    Intent intent = new Intent(LoginActivity.this, AdminPanelActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {

                               if (obj_values.getString("otp").equalsIgnoreCase("yes")) {
                                    editor.putString("login", "yes");
                                    editor.commit();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                } else {
                                    editor.putString("login", "no");
                                   editor.commit();
                                   Intent intent = new Intent(LoginActivity.this, OtpmsgActivity.class);
                                   intent.putExtra("mobile", obj_values.getString("mobile"));
                                   OtpmsgActivity.str_pagetype = "reg";
                                   startActivity(intent);
                                }
                            }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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
            else
            {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            this.cancel(true);

        }
    }
    public class communication_forgotpassword extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args0) {

            try {
                URL url = new URL(Urlclass.forgotpassword);
                JSONObject postDataParams = new JSONObject();


                postDataParams.put("email",str_emailid);
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

            Log.d("resuult", result);
            progressDialog.dismiss();
            if (result !=null)
            {
                if (!result.equalsIgnoreCase(""))
                {

                    if (result.equalsIgnoreCase("noemail"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("The email address you have entered is not registered in our system or your account has been deactivated. Please try again.");
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
                    else if (result.equalsIgnoreCase("sent"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("Your password has been sent to your registered email address. Thank-you!");
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
                    if (result.equalsIgnoreCase("updateerror"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("Connection time out. Please try again.");
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
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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
        AlertDialog.Builder builder4 = new AlertDialog.Builder(LoginActivity.this);
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
