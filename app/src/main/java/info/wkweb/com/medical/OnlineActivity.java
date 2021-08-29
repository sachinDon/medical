package info.wkweb.com.medical;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class OnlineActivity extends AppCompatActivity {

    RelativeLayout relative_banks11,relative_mobilepay;
    TextView text_back_online,text_online_bank,text_online_googlepay,text_online_phonepay,text_online_payatm;
    TextView text_banks_title,text_bank_mob,text_bankholdername1,text_bank_accno1,text_bank_ifsc,text_bank_name;
    TextView text_banks_title_mob,text_mobilepay,text_bank_mob1;
    ProgressBar progress_online_act;
    JSONArray array_account;
    String str_slect;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    JSONObject obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        text_back_online= (TextView) findViewById(R.id.text_back_online);
        progress_online_act = (ProgressBar) findViewById(R.id.progress_online_act);

        text_online_bank= (TextView) findViewById(R.id.text_online_bank);
        text_online_googlepay= (TextView) findViewById(R.id.text_online_googlepay);
        text_online_phonepay= (TextView) findViewById(R.id.text_online_phonepay);
        text_online_payatm= (TextView) findViewById(R.id.text_online_payatm);

        text_banks_title= (TextView) findViewById(R.id.text_banks_title);
        text_bankholdername1= (TextView) findViewById(R.id.text_bankholdername1);
        text_bank_accno1= (TextView) findViewById(R.id.text_bank_accno1);
        text_bank_name= (TextView) findViewById(R.id.text_bank_name);
        text_bank_ifsc= (TextView) findViewById(R.id.text_bank_ifsc);
        text_bank_mob= (TextView) findViewById(R.id.text_bank_mob);
        text_bank_mob1= (TextView) findViewById(R.id.text_bank_mob1);

        text_banks_title_mob= (TextView) findViewById(R.id.text_banks_title_mob);
        text_mobilepay= (TextView) findViewById(R.id.text_mobilepay);

        relative_banks11= (RelativeLayout) findViewById(R.id.relative_banks11);
        relative_mobilepay= (RelativeLayout) findViewById(R.id.relative_mobilepay);

        relative_banks11.setVisibility(View.INVISIBLE);
        relative_mobilepay.setVisibility(View.INVISIBLE);
        array_account = new JSONArray();
        str_slect = "";



        text_online_bank.setBackgroundResource(R.drawable.round_white1);
        text_online_googlepay.setBackgroundResource(R.drawable.round_white1);
        text_online_phonepay.setBackgroundResource(R.drawable.round_white1);
        text_online_payatm.setBackgroundResource(R.drawable.round_white1);

        text_online_bank.setTextColor(getResources().getColor(R.color.colortab));
        text_online_googlepay.setTextColor(getResources().getColor(R.color.colortab));
        text_online_phonepay.setTextColor(getResources().getColor(R.color.colortab));
        text_online_payatm.setTextColor(getResources().getColor(R.color.colortab));

        text_online_bank.setVisibility(View.INVISIBLE);
        text_online_googlepay.setVisibility(View.INVISIBLE);
        text_online_phonepay.setVisibility(View.INVISIBLE);
        text_online_payatm.setVisibility(View.INVISIBLE);

        text_back_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            finish();
            }
        });

        text_online_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_online_bank.setBackgroundResource(R.drawable.round_green1);
                text_online_googlepay.setBackgroundResource(R.drawable.round_white1);
                text_online_phonepay.setBackgroundResource(R.drawable.round_white1);
                text_online_payatm.setBackgroundResource(R.drawable.round_white1);

                text_online_bank.setTextColor(getResources().getColor(R.color.colorwhite));
                text_online_googlepay.setTextColor(getResources().getColor(R.color.colortab));
                text_online_phonepay.setTextColor(getResources().getColor(R.color.colortab));
                text_online_payatm.setTextColor(getResources().getColor(R.color.colortab));

                text_banks_title.setText("Bank Details");
                relative_banks11.setVisibility(View.VISIBLE);
                relative_mobilepay.setVisibility(View.INVISIBLE);
                text_bank_mob.setVisibility(View.GONE);
                text_bank_mob1.setVisibility(View.GONE);
                if (array_account.length() !=0)
                {
                    try {
                        text_bankholdername1.setText(obj.getString("name"));
                        text_bank_accno1.setText(obj.getString("account"));
                        text_bank_name.setText(obj.getString("bankname"));
                        text_bank_ifsc.setText(obj.getString("ifsccode"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


        text_online_googlepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_banks_title.setText("GooglePay Details");

                text_online_bank.setBackgroundResource(R.drawable.round_white1);
                text_online_googlepay.setBackgroundResource(R.drawable.round_green1);
                text_online_phonepay.setBackgroundResource(R.drawable.round_white1);
                text_online_payatm.setBackgroundResource(R.drawable.round_white1);

                text_online_bank.setTextColor(getResources().getColor(R.color.colortab));
                text_online_googlepay.setTextColor(getResources().getColor(R.color.colorwhite));
                text_online_phonepay.setTextColor(getResources().getColor(R.color.colortab));
                text_online_payatm.setTextColor(getResources().getColor(R.color.colortab));

                relative_banks11.setVisibility(View.VISIBLE);
                relative_mobilepay.setVisibility(View.INVISIBLE);
                text_bank_mob.setVisibility(View.VISIBLE);
                text_bank_mob1.setVisibility(View.VISIBLE);
                if (array_account.length() !=0) {
                    try {
                        text_bankholdername1.setText(obj.getString("name"));
                        text_bank_accno1.setText(obj.getString("account"));
                        text_bank_name.setText(obj.getString("bankname"));
                        text_bank_ifsc.setText(obj.getString("ifsccode"));
                        text_bank_mob.setText(obj.getString("googlepay"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        text_online_phonepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_banks_title_mob.setText("PhonePay Details");

                text_online_bank.setBackgroundResource(R.drawable.round_white1);
                text_online_googlepay.setBackgroundResource(R.drawable.round_white1);
                text_online_phonepay.setBackgroundResource(R.drawable.round_green1);
                text_online_payatm.setBackgroundResource(R.drawable.round_white1);

                text_online_bank.setTextColor(getResources().getColor(R.color.colortab));
                text_online_googlepay.setTextColor(getResources().getColor(R.color.colortab));
                text_online_phonepay.setTextColor(getResources().getColor(R.color.colorwhite));
                text_online_payatm.setTextColor(getResources().getColor(R.color.colortab));

                relative_banks11.setVisibility(View.INVISIBLE);
                relative_mobilepay.setVisibility(View.VISIBLE);
                if (array_account.length() !=0)
                {
                    try {
                        text_mobilepay.setText(obj.getString("phonepay"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }
        });

        text_online_payatm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                text_banks_title_mob.setText("Payatm Details");

                text_online_bank.setBackgroundResource(R.drawable.round_white1);
                text_online_googlepay.setBackgroundResource(R.drawable.round_white1);
                text_online_phonepay.setBackgroundResource(R.drawable.round_white1);
                text_online_payatm.setBackgroundResource(R.drawable.round_green1);

                text_online_bank.setTextColor(getResources().getColor(R.color.colortab));
                text_online_googlepay.setTextColor(getResources().getColor(R.color.colortab));
                text_online_phonepay.setTextColor(getResources().getColor(R.color.colortab));
                text_online_payatm.setTextColor(getResources().getColor(R.color.colorwhite));

                relative_banks11.setVisibility(View.INVISIBLE);
                relative_mobilepay.setVisibility(View.VISIBLE);
                if (array_account.length() !=0) {
                    try {
                        text_mobilepay.setText(obj.getString("payatm"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        new Communication_accountinfo().execute();

    }

    public class Communication_accountinfo extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.getaccountinfo);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("custid", pref.getString("custid",""));

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

                }
                else
                {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result)
        {


            progress_online_act.setVisibility(View.INVISIBLE);


            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {
                    text_online_bank.setVisibility(View.INVISIBLE);
                    text_online_googlepay.setVisibility(View.INVISIBLE);
                    text_online_phonepay.setVisibility(View.INVISIBLE);
                    text_online_payatm.setVisibility(View.INVISIBLE);


                } else if (result.equalsIgnoreCase("nodata"))

                {
                    text_online_bank.setVisibility(View.INVISIBLE);
                    text_online_googlepay.setVisibility(View.INVISIBLE);
                    text_online_phonepay.setVisibility(View.INVISIBLE);
                    text_online_payatm.setVisibility(View.INVISIBLE);

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=20) {
                        try {
                            array_account = new JSONArray(result);
                            array_account = new JSONArray(result);
                            if (array_account !=null) {

                                text_online_bank.setVisibility(View.VISIBLE);
                                text_online_googlepay.setVisibility(View.VISIBLE);
                                text_online_phonepay.setVisibility(View.VISIBLE);
                                text_online_payatm.setVisibility(View.VISIBLE);

                                 obj = new JSONObject(String.valueOf(array_account.getJSONObject(0)));


                                if(obj.getString("account").equalsIgnoreCase("") || obj.getString("account").length() <=9)
                                {
                                    text_online_bank.setVisibility(View.GONE);
                                }
                                if(obj.getString("googlepay").equalsIgnoreCase("") || obj.getString("googlepay").length() <=9)
                                {
                                    text_online_googlepay.setVisibility(View.GONE);
                                }
                                if(obj.getString("phonepay").equalsIgnoreCase("") || obj.getString("phonepay").length() <=9)
                                {
                                    text_online_phonepay.setVisibility(View.GONE);
                                }
                                 if(obj.getString("payatm").equalsIgnoreCase("") || obj.getString("payatm").length() <=9)
                                 {
                                     text_online_payatm.setVisibility(View.GONE);
                                 }

                            }
                            else
                            {
                                text_online_bank.setVisibility(View.INVISIBLE);
                                text_online_googlepay.setVisibility(View.INVISIBLE);
                                text_online_phonepay.setVisibility(View.INVISIBLE);
                                text_online_payatm.setVisibility(View.INVISIBLE);

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {

                        text_online_bank.setVisibility(View.INVISIBLE);
                        text_online_googlepay.setVisibility(View.INVISIBLE);
                        text_online_phonepay.setVisibility(View.INVISIBLE);
                        text_online_payatm.setVisibility(View.INVISIBLE);
                    }
                }
            }
            else
            {
                text_online_bank.setVisibility(View.INVISIBLE);
                text_online_googlepay.setVisibility(View.INVISIBLE);
                text_online_phonepay.setVisibility(View.INVISIBLE);
                text_online_payatm.setVisibility(View.INVISIBLE);

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
