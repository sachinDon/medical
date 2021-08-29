package info.wkweb.com.medical;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    public SharedPreferences pref;
    RelativeLayout relative_main_page;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relative_main_page =(RelativeLayout)findViewById(R.id.relative_main_page);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();






        relative_main_page.setOnTouchListener(new View.OnTouchListener()
        {
            View v;
            private GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onDoubleTap(MotionEvent e)
                {

                    Toast.makeText(MainActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {



                    if (pref.getString("regtype","").equalsIgnoreCase("admin"))
                    {
                        if (pref.getString("login", "").equalsIgnoreCase("yes"))
                        {
                            Intent intent = new Intent(MainActivity.this, AdminPanelActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                        {
                        if (pref.getString("login", "").equalsIgnoreCase("yes"))
                        {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
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


//        relative_main_page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (pref.getString("login","").equalsIgnoreCase("yes"))
//                {
//                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
//                    startActivity(intent);
//                }
//                else
//                {
//                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });


//        new Communication_updateapp().execute();
//
//        progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setMessage("Verifying Updates..."); // Setting Message
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//        progressDialog.show(); // Display Progress Dialog
//        progressDialog.setCancelable(false);


        if (pref.getString("regtype","").equalsIgnoreCase("admin"))
        {
            if (pref.getString("login", "").equalsIgnoreCase("yes"))
            {
                Intent intent = new Intent(MainActivity.this, AdminPanelActivity.class);
                startActivity(intent);
            }
        }
        else
        {
            if (pref.getString("login", "").equalsIgnoreCase("yes"))
            {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//
//
//
//
//            }
//        }, 2000);
    }

    public class Communication_updateapp extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.updateapp);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile", pref.getString("userid",""));
              //  postDataParams.put("custid",pref.getString("custid",""));


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
                            if (obj_val.getString("version").equalsIgnoreCase("0"))
                            {

                            }
                            else
                            {

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
    public void onBackPressed() {

    }
}
