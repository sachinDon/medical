package info.wkweb.com.medical;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
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

public class SignUpActivity extends AppCompatActivity
{
    public SharedPreferences pref;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;
    AlertDialog alert11;
    public  static String str_viewpage="";

String str_dln,str_diprof,str_devicetoken;
EditText edittext_signup_csutname,edittext_signup_firmname,edittext_signup_email,edittext_signupid,edittext_signup_pass,edittext_signup_add1,
        edittext_signup_pincode,edittext_signup_dlno,edittext_signup_gstno,edittext_signup_fdno,edittext_signup_gstnos;
    TextView text_login_signup,ext_signup_dlno,text_signup_idprof,text_signup_back,text_signup_title;
    RelativeLayout relative_signup_dlno,relative_signup_idprof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edittext_signup_csutname =(EditText)findViewById(R.id.edittext_signup_csutname);
        edittext_signup_firmname =(EditText)findViewById(R.id.edittext_signup_firmname);
        edittext_signup_email =(EditText)findViewById(R.id.edittext_signup_email);
        edittext_signupid =(EditText)findViewById(R.id.edittext_signupid);
        edittext_signup_pass =(EditText)findViewById(R.id.edittext_signup_pass);
        edittext_signup_add1 =(EditText)findViewById(R.id.edittext_signup_add1);
       // edittext_signup_add2 =(EditText)findViewById(R.id.edittext_signup_add2);
        edittext_signup_pincode =(EditText)findViewById(R.id.edittext_signup_pincode);

        edittext_signup_gstno =(EditText)findViewById(R.id.edittext_signup_gstno);
        text_login_signup =(TextView) findViewById(R.id.text_login_signup);
        text_signup_idprof =(TextView) findViewById(R.id.text_signup_idprof);
        text_signup_back =(TextView) findViewById(R.id.text_signup_back);
        text_signup_title =(TextView) findViewById(R.id.text_signup_title);
        relative_signup_idprof=(RelativeLayout) findViewById(R.id.relative_signup_idprof);

        edittext_signup_dlno =(EditText)findViewById(R.id.edittext_signup_dlno);
        edittext_signup_gstnos=(EditText)findViewById(R.id.edittext_signup_gstnos);
        edittext_signup_fdno=(EditText)findViewById(R.id.edittext_signup_fdno);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        str_dln="";
        str_diprof="";
        text_login_signup.setEnabled(false);
        text_login_signup.setTextColor(Color.WHITE);

Log.d("cccname==",pref.getString("custname",""));

        if(str_viewpage.equalsIgnoreCase("edit"))
        {

            edittext_signup_csutname.setText(pref.getString("custname",""));
            edittext_signup_firmname.setText(pref.getString("firmname",""));
            edittext_signup_email.setText(pref.getString("email",""));
            edittext_signup_add1.setText(pref.getString("address1",""));
            edittext_signup_pincode.setText(pref.getString("pincode1",""));
//            ext_signup_dlno.setText(pref.getString("dlntype",""));
//            edittext_signup_dlno.setText(pref.getString("dln",""));


            edittext_signup_dlno.setText(pref.getString("drugno",""));
            edittext_signup_gstnos.setText(pref.getString("gstno",""));
            edittext_signup_fdno.setText(pref.getString("foodno",""));


            text_signup_idprof.setText(pref.getString("idproftype",""));
            edittext_signup_gstno.setText(pref.getString("idprof",""));

            float d = getApplicationContext().getResources().getDisplayMetrics().density;
            int margin = (int)(10 * d);



            str_diprof = String.valueOf(pref.getString("idproftype",""));
            text_signup_idprof.setText(pref.getString("idproftype",""));
            edittext_signup_gstno.setText(pref.getString("idprof",""));
            edittext_signup_gstno.setHint("Enter "+pref.getString("idproftype",""));

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.BELOW, relative_signup_idprof.getId());
            params1.setMargins(margin,margin,margin,margin);
            edittext_signup_gstno.setLayoutParams(params1);
            edittext_signup_gstno.setVisibility(View.VISIBLE);

            edittext_signup_pass.setVisibility(View.GONE);
            edittext_signupid.setVisibility(View.GONE);
            text_signup_title.setText("Update Profile");
            text_login_signup.setText("Update");
            checkRegister();

        }
        else  if(str_viewpage.equalsIgnoreCase("editotp"))
        {

            edittext_signup_csutname.setText(pref.getString("custname",""));
            edittext_signup_firmname.setText(pref.getString("firmname",""));
            edittext_signup_email.setText(pref.getString("email",""));
            edittext_signup_add1.setText(pref.getString("address1",""));
            edittext_signup_pincode.setText(pref.getString("pincode1",""));
            edittext_signupid.setText(pref.getString("mobile1",""));
//            edittext_signup_dlno.setText(pref.getString("dln",""));


            edittext_signup_dlno.setText(pref.getString("drugno",""));
            edittext_signup_gstnos.setText(pref.getString("gstno",""));
            edittext_signup_fdno.setText(pref.getString("foodno",""));


            text_signup_idprof.setText(pref.getString("idproftype",""));
            edittext_signup_gstno.setText(pref.getString("idprof",""));
            edittext_signup_gstno.setHint("Enter "+pref.getString("idproftype",""));

            float d = getApplicationContext().getResources().getDisplayMetrics().density;
            int margin = (int)(10 * d);



            str_diprof = String.valueOf(pref.getString("idproftype",""));
            text_signup_idprof.setText(pref.getString("idproftype",""));
            edittext_signup_gstno.setText(pref.getString("idprof",""));

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.BELOW, relative_signup_idprof.getId());
            params1.setMargins(margin,margin,margin,margin);
            edittext_signup_gstno.setLayoutParams(params1);
            edittext_signup_gstno.setVisibility(View.VISIBLE);

            edittext_signup_pass.setVisibility(View.GONE);
            edittext_signupid.setVisibility(View.VISIBLE);
            text_signup_title.setText("Update Profile");
            text_login_signup.setText("Update");
            checkRegister();

        }
        else
        {
            editor.putString("login","no");
            editor.commit();
            edittext_signup_pass.setVisibility(View.VISIBLE);
            edittext_signupid.setVisibility(View.VISIBLE);
            text_signup_title.setText("GET STARTED");
            text_login_signup.setText("Register");
        }
        str_devicetoken = "";// FirebaseInstanceId.getInstance().getToken();
        text_login_signup.setOnTouchListener(new View.OnTouchListener()
        {

            View v;
            private GestureDetector gestureDetector = new GestureDetector(SignUpActivity.this, new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onDoubleTap(MotionEvent e)
                {

                    Toast.makeText(SignUpActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {

                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        if (str_viewpage.equalsIgnoreCase("edit") || str_viewpage.equalsIgnoreCase("editotp")) {
                            progressDialog = new ProgressDialog(SignUpActivity.this);
                            progressDialog.setMessage("Update..."); // Setting Message
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.show(); // Display Progress Dialog
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new Communication_Update().execute();
                        }
                        else
                            {
                            progressDialog = new ProgressDialog(SignUpActivity.this);
                            progressDialog.setMessage("Register..."); // Setting Message
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.show(); // Display Progress Dialog
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new Communication_Signup().execute();
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

//        text_login_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//
//                if(str_viewpage.equalsIgnoreCase("edit"))
//                {
//                    progressDialog = new ProgressDialog(SignUpActivity.this);
//                    progressDialog.setMessage("Update..."); // Setting Message
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    progressDialog.show(); // Display Progress Dialog
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                    new Communication_Update().execute();
//                }
//                else
//                {
//                    progressDialog = new ProgressDialog(SignUpActivity.this);
//                    progressDialog.setMessage("Register..."); // Setting Message
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    progressDialog.show(); // Display Progress Dialog
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                    new Communication_Signup().execute();
//                }
//
//            }
//
//
//        });
        text_signup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edittext_signup_csutname.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edittext_signup_firmname.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        edittext_signup_email.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        edittext_signupid.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        edittext_signup_pass.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        edittext_signup_add1.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
//        edittext_signup_add2.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//
//                checkRegister();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
        edittext_signup_pincode.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
//        edittext_signup_dlno.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//
//                checkRegister();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
        edittext_signup_gstno.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



//        relative_signup_dlno.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                PopupMenu menu = new PopupMenu(SignUpActivity.this, v);
//                menu.getMenu().add("Drug");
//                menu.getMenu().add("Food");
//                menu.getMenu().add("GST");
//                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
//                 //       Integer int_index = item.getGroupId();
////                        text_setcat_homecat.setText(String.valueOf(item));
////                        str_category_select = String.valueOf(item);
//                        if (String.valueOf(item).equalsIgnoreCase("Drug"))
//                        {
//                            str_dln = String.valueOf(item);
//                            ext_signup_dlno.setText(str_dln);
//                            float d = getApplicationContext().getResources().getDisplayMetrics().density;
//                            int margin = (int)(10 * d);
//                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//                            params.addRule(RelativeLayout.BELOW, relative_signup_dlno.getId());
//                            params.setMargins(margin,margin,margin,margin);
//                            edittext_signup_dlno.setLayoutParams(params);
//                            edittext_signup_dlno.setVisibility(View.VISIBLE);
//                            edittext_signup_dlno.setText("");
//                            edittext_signup_dlno.requestFocus(edittext_signup_dlno.getText().length());
//
//                            checkRegister();
//
//
//                        }
//                        else if (String.valueOf(item).equalsIgnoreCase("Food"))
//                        {
//                            str_dln = String.valueOf(item);
//                            ext_signup_dlno.setText(str_dln);
//                            float d = getApplicationContext().getResources().getDisplayMetrics().density;
//                            int margin = (int)(10 * d);
//                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//                            params.addRule(RelativeLayout.BELOW, relative_signup_dlno.getId());
//                            params.setMargins(margin,margin,margin,margin);
//                            edittext_signup_dlno.setLayoutParams(params);
//                            edittext_signup_dlno.setVisibility(View.VISIBLE);
//                            edittext_signup_dlno.setHint("Enter Food Licence");
//                            edittext_signup_dlno.setText("");
//                            edittext_signup_dlno.requestFocus(edittext_signup_dlno.getText().length());
//                            checkRegister();
//                        }
//                        else if (String.valueOf(item).equalsIgnoreCase("GST"))
//                        {
//                            str_dln = String.valueOf(item);
//                            ext_signup_dlno.setText(str_dln);
//                            float d = getApplicationContext().getResources().getDisplayMetrics().density;
//                            int margin = (int)(10 * d);
//                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//                            params.addRule(RelativeLayout.BELOW, relative_signup_dlno.getId());
//                            params.setMargins(margin,margin,margin,margin);
//                            edittext_signup_dlno.setLayoutParams(params);
//                            edittext_signup_dlno.setVisibility(View.VISIBLE);
//                            edittext_signup_dlno.setText("");
//                            edittext_signup_dlno.requestFocus(edittext_signup_dlno.getText().length());
//                            edittext_signup_dlno.setHint("Enter GST number");
//
//                            checkRegister();
//                        }
//                        else
//                        {
//                            str_dln = "";
//                            ext_signup_dlno.setText("Select Licences");
//                            checkRegister();
//                        }
//
//                        return false;
//                    }
//                });
//
//                menu.show();
//            }
//        });

        relative_signup_idprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu menu = new PopupMenu(SignUpActivity.this, v);
                menu.getMenu().add("PAN Card No.");
                menu.getMenu().add("Aadhar Card No.");
                menu.getMenu().add("Voter Card No.");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //       Integer int_index = item.getGroupId();
//                        text_setcat_homecat.setText(String.valueOf(item));
//                        str_category_select = String.valueOf(item);
                        if (String.valueOf(item).equalsIgnoreCase("PAN Card No."))
                        {
                            str_diprof = String.valueOf(item);
                            text_signup_idprof.setText(str_diprof);
                            float d = getApplicationContext().getResources().getDisplayMetrics().density;
                            int margin = (int)(10 * d);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.BELOW, relative_signup_idprof.getId());
                            params.setMargins(margin,margin,margin,margin);
                            edittext_signup_gstno.setLayoutParams(params);
                            edittext_signup_gstno.setVisibility(View.VISIBLE);
                            edittext_signup_gstno.setText("");
                            edittext_signup_gstno.requestFocus(edittext_signup_gstno.getText().length());
                            edittext_signup_gstno.setHint("Enter PAN card No");


                        }
                        else if (String.valueOf(item).equalsIgnoreCase("Aadhar Card No."))
                        {
                            str_diprof = String.valueOf(item);
                            text_signup_idprof.setText(str_diprof);
                            float d = getApplicationContext().getResources().getDisplayMetrics().density;
                            int margin = (int)(10 * d);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.BELOW, relative_signup_idprof.getId());
                            params.setMargins(margin,margin,margin,margin);
                            edittext_signup_gstno.setLayoutParams(params);
                            edittext_signup_gstno.setVisibility(View.VISIBLE);
                            edittext_signup_gstno.setText("");
                            edittext_signup_gstno.requestFocus(edittext_signup_gstno.getText().length());
                            edittext_signup_gstno.setHint("Enter Aadhar Card No.");
                        }
                        else if (String.valueOf(item).equalsIgnoreCase("Voter Card No."))
                        {
                            str_diprof = String.valueOf(item);
                            text_signup_idprof.setText(str_diprof);
                            float d = getApplicationContext().getResources().getDisplayMetrics().density;
                            int margin = (int)(10 * d);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.BELOW, relative_signup_idprof.getId());
                            params.setMargins(margin,margin,margin,margin);
                            edittext_signup_gstno.setLayoutParams(params);
                            edittext_signup_gstno.setVisibility(View.VISIBLE);
                            edittext_signup_gstno.setText("");
                            edittext_signup_gstno.requestFocus(edittext_signup_gstno.getText().length());
                            edittext_signup_gstno.setHint("Enter Votercard No");
                        }
                        else
                        {
                            str_dln = "";
                            ext_signup_dlno.setText("Select Id Prof");
                        }

                        return false;
                    }
                });

                menu.show();
            }
        });
    }

public  void checkRegister() {

    if (str_viewpage.equalsIgnoreCase("edit"))
    {

    if (edittext_signup_csutname.getText().length() != 0 && edittext_signup_firmname.getText().length() != 0 && edittext_signup_email.getText().length() != 0
            && edittext_signup_add1.getText().length() != 0
            && edittext_signup_pincode.getText().length() != 0 && str_diprof.length() != 0 && edittext_signup_gstno.getText().length()!=0 )
    {

        text_login_signup.setEnabled(true);
        text_login_signup.setTextColor(getResources().getColor(R.color.colorblue));
    }
    else
        {
        text_login_signup.setEnabled(false);
        text_login_signup.setTextColor(Color.WHITE);

    }
}
  else if (str_viewpage.equalsIgnoreCase("editotp")) {

        if (edittext_signup_csutname.getText().length() != 0 && edittext_signup_firmname.getText().length() != 0 && edittext_signup_email.getText().length() != 0
                && edittext_signup_add1.getText().length() != 0
                && edittext_signup_pincode.getText().length() != 0  && str_diprof.length() != 0 && edittext_signupid.getText().length() != 0 && edittext_signup_gstno.getText().length()!=0 )
        {

            text_login_signup.setEnabled(true);
            text_login_signup.setTextColor(getResources().getColor(R.color.colorblue));
        }
        else
        {
            text_login_signup.setEnabled(false);
            text_login_signup.setTextColor(Color.WHITE);

        }
    }
else
    {
        if (edittext_signup_csutname.getText().length() != 0 && edittext_signup_firmname.getText().length() != 0
                && edittext_signupid.getText().length() != 0 && edittext_signup_pass.getText().length() != 0 && edittext_signup_add1.getText().length() != 0
                && edittext_signup_pincode.getText().length() != 0 && str_diprof.length()!=0 && edittext_signup_gstno.getText().length()!=0)
                  {

            text_login_signup.setEnabled(true);
            text_login_signup.setTextColor(getResources().getColor(R.color.colorblue));
        } else
            {
            text_login_signup.setEnabled(false);
            text_login_signup.setTextColor(Color.WHITE);

        }
    }
}
    public class Communication_Signup extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.registerlogin);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("custname", edittext_signup_csutname.getText());
                postDataParams.put("firmname", edittext_signup_firmname.getText());
                postDataParams.put("email", edittext_signup_email.getText());
                postDataParams.put("mobile", edittext_signupid.getText());
                postDataParams.put("password",edittext_signup_pass.getText());
                postDataParams.put("address1", edittext_signup_add1.getText());
                //postDataParams.put("address2", edittext_signup_add2.getText());
                postDataParams.put("pincode", edittext_signup_pincode.getText());
                postDataParams.put("gstno", edittext_signup_gstnos.getText());
                postDataParams.put("foodno", edittext_signup_fdno.getText());
                postDataParams.put("drugno", edittext_signup_dlno.getText());
//                postDataParams.put("pancard", edittext_signup_pancard.getText());
//                postDataParams.put("adarcard", edittext_signup_adarcard.getText());
               postDataParams.put("idproftype", text_signup_idprof.getText());
                postDataParams.put("idprof", edittext_signup_gstno.getText());
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

                if (result.equalsIgnoreCase("mobileexist"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUpActivity.this);
                    builder1.setTitle("Oops");
                    builder1.setMessage("You have already account registered with this mobile number. Please login with another mobile number.");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder1.create();
                    alert11.show();



                }
                else if (result.equalsIgnoreCase("nullerror"))
                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(SignUpActivity.this);
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
                else if (result.equalsIgnoreCase("register"))
                {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(SignUpActivity.this);
                    builder5.setMessage("Your account has been registered with Pritama medicals.");
                    builder5.setTitle("Successful!");
                    builder5.setCancelable(false);
                    builder5.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();



                                     finish();
                                    Intent intent = new Intent(SignUpActivity.this,OTPActivity.class);
                                    OTPActivity.Strmobileno = String.valueOf(edittext_signupid.getText());
                                    startActivity(intent);
                                }
                            });
                    alert11 = builder5.create();
                    alert11.show();

                }
                else if (result.equalsIgnoreCase("inserterror"))
                {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(SignUpActivity.this);
                    builder5.setMessage("Your account has not register.Please try again.");
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

//                        AlertDialog.Builder builder2 = new AlertDialog.Builder(SignUpActivity.this);
//                        builder2.setTitle("Oops");
//                        builder2.setMessage("Server could not found.");
//                        builder2.setCancelable(false);
//                        builder2.setPositiveButton("Ok",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                         finish();
//                                    }
//                                });
//                        alert11 = builder2.create();
//                        alert11.show();

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
                                        Intent intent = new Intent(SignUpActivity.this, AdminPanelActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {

                                        if (obj_values.getString("otp").equalsIgnoreCase("yes")) {
                                            editor.putString("login", "yes");
                                            editor.commit();
                                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                       } else {
                                          editor.putString("login", "no");
                                           editor.commit();
                                            Intent intent = new Intent(SignUpActivity.this, OtpmsgActivity.class);
                                            OtpmsgActivity.str_pagetype = "reg";
                                            intent.putExtra("mobile", obj_values.getString("mobile"));
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
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(SignUpActivity.this);
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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(SignUpActivity.this);
                builder2.setTitle("Oops");
                builder2.setMessage("Server could not found.");
                builder2.setCancelable(false);
                builder2.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                 finish();
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
    public class Communication_Update extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.updateprofile);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("custid", pref.getString("custid",""));
                postDataParams.put("custname", edittext_signup_csutname.getText());
                postDataParams.put("firmname", edittext_signup_firmname.getText());
                postDataParams.put("email", edittext_signup_email.getText());
                postDataParams.put("address1", edittext_signup_add1.getText());
                //postDataParams.put("address2", edittext_signup_add2.getText());
                postDataParams.put("pincode", edittext_signup_pincode.getText());
                postDataParams.put("gstno", edittext_signup_gstnos.getText());
                postDataParams.put("foodno", edittext_signup_fdno.getText());
                postDataParams.put("drugno", edittext_signup_dlno.getText());
                postDataParams.put("mobile", edittext_signupid.getText());
                postDataParams.put("updatetype",str_viewpage);
                postDataParams.put("idproftype", text_signup_idprof.getText());
                postDataParams.put("idprof", edittext_signup_gstno.getText());

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

                if (result.equalsIgnoreCase("mobileexist"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUpActivity.this);
                    builder1.setTitle("Oops");
                    builder1.setMessage("You have already account registered with this mobile number. Please login with another mobile number.");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder1.create();
                    alert11.show();



                }
                else if (result.equalsIgnoreCase("nullerror"))
                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(SignUpActivity.this);
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
                else if (result.equalsIgnoreCase("done"))
                {
                    Intent intent1 = new Intent("updateprofile");
                       LocalBroadcastManager.getInstance(SignUpActivity.this).sendBroadcast(intent1);


                    editor.putString("custname1", String.valueOf(edittext_signup_csutname.getText()));
                    editor.putString("custname", String.valueOf(edittext_signup_csutname.getText()));
                    editor.putString("firmname", String.valueOf(edittext_signup_firmname.getText()));
                    editor.putString("email", String.valueOf(edittext_signup_email.getText()));
                    editor.putString("email1", String.valueOf(edittext_signup_email.getText()));
                    editor.putString("address1", String.valueOf(edittext_signup_add1.getText()));

                    editor.putString("pincode", String.valueOf(edittext_signup_pincode.getText()));
                    editor.putString("pincode1", String.valueOf(edittext_signup_pincode.getText()));


                    editor.putString("drugno", String.valueOf(edittext_signup_dlno.getText()));
                    editor.putString("foodno",String.valueOf(edittext_signup_fdno.getText()));
                    editor.putString("gstno",String.valueOf(edittext_signup_gstnos.getText()));



                    editor.putString("idproftype", String.valueOf(text_signup_idprof.getText()));
                    editor.putString("idprof", String.valueOf(edittext_signup_gstno.getText()));

                  //  editor.putString("userid", String.valueOf(edittext_signupid.getText()));

                    editor.putString("mobile1", String.valueOf(edittext_signupid.getText()));
                    editor.putString("mobile2", "");
                    editor.commit();
//                    if (str_viewpage.equalsIgnoreCase("editotp"))
//                    {
//                        OTPActivity.Strmobileno= String.valueOf(edittext_signupid.getText());
//                        Intent intents1 = new Intent("updateotp");
//                        LocalBroadcastManager.getInstance(SignUpActivity.this).sendBroadcast(intents1);
//
//                    }

                    if (str_viewpage.equalsIgnoreCase("editotp"))
                    {

                        Intent intents1 = new Intent("updateotp");
                        intents1.putExtra("mobile",String.valueOf(edittext_signupid.getText()));
                        LocalBroadcastManager.getInstance(SignUpActivity.this).sendBroadcast(intents1);
                        editor.putString("userid", String.valueOf(edittext_signupid.getText()));
                        editor.putString("mobile1", String.valueOf(edittext_signupid.getText()));
                        editor.putString("mobile2", "");
                        editor.commit();

                    }
                    else  if (str_viewpage.equalsIgnoreCase("edit"))
                    {
                       // OTPActivity.Strmobileno= String.valueOf(edittext_signupid.getText());
                        Intent intents1 = new Intent("updateotp");
                        LocalBroadcastManager.getInstance(SignUpActivity.this).sendBroadcast(intents1);

                    }
                    else
                    {
                        editor.putString("userid", String.valueOf(edittext_signupid.getText()));
                        editor.putString("mobile1", String.valueOf(edittext_signupid.getText()));
                        editor.putString("mobile2", "");
                        editor.commit();
                    }


                    AlertDialog.Builder builder5 = new AlertDialog.Builder(SignUpActivity.this);
                    builder5.setMessage("Your account has been updated successful");
                    builder5.setTitle("Successful!");
                    builder5.setCancelable(false);
                    builder5.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    alert11 = builder5.create();
                    alert11.show();

                }
                else if (result.equalsIgnoreCase("inserterror"))
                {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(SignUpActivity.this);
                    builder5.setMessage("Your account has not register.Please try again.");
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

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(SignUpActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Server could not found.");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();

                }
            }
            else
            {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(SignUpActivity.this);
                builder2.setTitle("Oops");
                builder2.setMessage("Server could not found.");
                builder2.setCancelable(false);
                builder2.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
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
        AlertDialog.Builder builder4 = new AlertDialog.Builder(SignUpActivity.this);
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
