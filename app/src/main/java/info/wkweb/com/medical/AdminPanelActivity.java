package info.wkweb.com.medical;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import androidx.core.app.NavUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class AdminPanelActivity extends AppCompatActivity {


    JSONArray array_divs;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;

    ListView listview_divs;
    ListviewAdaptercart listview_customadpte;
    ProgressBar progress_divs;
    TextView text_result_divs,text_logout_divis;

    String str_orderid,str_status,str_custid;
    ProgressDialog progressDialog;
    AlertDialog alert11;
    Integer int_cancelor;
    SwipeRefreshLayout swipeToRefresh_admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

      //  text_back_divis=(TextView)findViewById(R.id.text_back_divis);
        text_logout_divis=(TextView)findViewById(R.id.text_logout_divis);
        text_result_divs=(TextView)findViewById(R.id.text_result_divs);
        progress_divs=(ProgressBar)findViewById(R.id.progress_divs);
        listview_divs=(ListView)findViewById(R.id.listview_divs);
        swipeToRefresh_admin =(SwipeRefreshLayout)findViewById(R.id.swipeToRefresh_adminpanel);
        swipeToRefresh_admin.setColorSchemeResources(R.color.colorAccent);
        array_divs=new JSONArray();
        text_result_divs.setVisibility(View.INVISIBLE);


//        text_back_divis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


        swipeToRefresh_admin.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    new Communication_delivert().execute();
                }
                else
                {
                    swipeToRefresh_admin.setRefreshing(false);
                    Connections();
                }

            }
        });

        text_logout_divis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
                editor.putString("login","no");
                editor.putString("address","no");
                editor.commit();
//                Intent intent = new Intent(getActivity(),MainActivity.class);
//                startActivity(intent);
            }
        });
        //Log.d("devicetoken", FirebaseInstanceId.getInstance().getToken());
        listview_divs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView

                try {
                    JSONObject objectval = new JSONObject(String.valueOf(array_divs.getJSONObject(position)));
                    JSONArray jsonArr = new JSONArray(objectval.getString("orderdata"));
                    OrderlistActivity.Array_orderlist = jsonArr;
                    OrderlistActivity.str_views="admin";
                    Intent intent = new Intent(AdminPanelActivity.this,OrderlistActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        new Communication_delivert().execute();
    }
    private class ListviewAdaptercart extends BaseAdapter {
        private LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return array_divs.length();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = inflater.inflate(R.layout.list_vendor_delivery, null,false);


            final TextView text_order_name_d=(TextView)view.findViewById(R.id.text_order_name_d);
            final TextView text_orderid_d=(TextView)view.findViewById(R.id.text_orderid_d);
            final TextView text_order_total_d=(TextView)view.findViewById(R.id.text_order_total_d);

            final TextView text_oeder_accept=(TextView)view.findViewById(R.id.text_oeder_accept);
            final TextView text_order_cancel_d=(TextView)view.findViewById(R.id.text_order_cancel_d);
            text_oeder_accept.setTag(i);
            text_order_cancel_d.setTag(i);

            try {
                final JSONObject object_values = new JSONObject(String.valueOf(array_divs.getJSONObject(i)));
                text_orderid_d.setText("Order_id: "+object_values.getString("orderid"));
                text_order_name_d.setText("Name: "+object_values.getString("custname"));
                text_order_total_d.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+String.valueOf(Math.round(Float.parseFloat(object_values.getString("total"))))+ " </b> </font>"));


                text_order_cancel_d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            JSONObject objectval = new JSONObject(String.valueOf(array_divs.getJSONObject((Integer)v.getTag())));

                            str_status = "Order Cancel by pritama";//""Reject";
                            str_orderid = objectval.getString("orderid");
                            str_custid = objectval.getString("custid");
                            int_cancelor = (Integer)v.getTag();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog = new ProgressDialog(AdminPanelActivity.this);
                        progressDialog.setMessage("Cancel..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                        new Communication_ordercancel().execute();
                    }
                });

                text_oeder_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            JSONObject objectval = new JSONObject(String.valueOf(array_divs.getJSONObject((Integer)v.getTag())));

                            str_status = "Accept";
                            str_orderid = objectval.getString("orderid");
                            str_custid = objectval.getString("custid");
                            int_cancelor = (Integer)v.getTag();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog = new ProgressDialog(AdminPanelActivity.this);
                        progressDialog.setMessage("Cancel..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                        new Communication_ordercancel().execute();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return view;
        }
    }
    public class Communication_delivert extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.getdelivery);

                JSONObject postDataParams = new JSONObject();

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
            progress_divs.setVisibility(View.INVISIBLE);
            text_result_divs.setVisibility(View.INVISIBLE);
            swipeToRefresh_admin.setRefreshing(false);
            if (progressDialog != null)
            {
                progressDialog.dismiss();
            }
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                }
                else if (result.equalsIgnoreCase("error"))

                {

                    text_result_divs.setVisibility(View.INVISIBLE);
                }
                else if (result.equalsIgnoreCase("nodata"))
                {

                    text_result_divs.setVisibility(View.VISIBLE);
                    array_divs = new JSONArray();
                    listview_customadpte = new ListviewAdaptercart();
                    listview_divs.setAdapter(listview_customadpte);
                }
                else if (result.equalsIgnoreCase("[]"))
                {
                    text_result_divs.setVisibility(View.VISIBLE);
                    array_divs = new JSONArray();
                    listview_customadpte = new ListviewAdaptercart();
                    listview_divs.setAdapter(listview_customadpte);
                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_divs = new JSONArray(result);
                            if (array_divs !=null) {

                                text_result_divs.setVisibility(View.INVISIBLE);
                                listview_customadpte = new ListviewAdaptercart();
                                listview_divs.setAdapter(listview_customadpte);

                            }
                            else
                            {
                                text_result_divs.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {


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
    public class Communication_ordercancel extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.orderstatus);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("custid", pref.getString("custid",""));
                postDataParams.put("custid1", str_custid);

                postDataParams.put("status",str_status);
                postDataParams.put("orderid", str_orderid);

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

            if (progressDialog !=null)
            {
                progressDialog.dismiss();
            }
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                }
                else if (result.equalsIgnoreCase("refresh"))
                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(AdminPanelActivity.this);
                    builder4.setTitle("Opps!");
                    builder4.setMessage("Customer has been cancel his order");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    progressDialog = new ProgressDialog(AdminPanelActivity.this);
                                    progressDialog.setMessage(" Refresh list..."); // Setting Message
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                    progressDialog.show(); // Display Progress Dialog
                                    progressDialog.setCancelable(false);
                                    new Communication_delivert().execute();

                                }
                            });
                    alert11 = builder4.create();
                    alert11.show();


                }
                else if (result.equalsIgnoreCase("error"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminPanelActivity.this);
                    builder1.setTitle("Oops");
                    builder1.setMessage("Server could not found, Please try again.");
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
                else if (result.equalsIgnoreCase("done"))
                {


                    if (array_divs.length() !=0)
                    {
                        array_divs.remove(int_cancelor);
                        listview_customadpte.notifyDataSetChanged();
                        if (array_divs.length() ==0)
                        {
                            text_result_divs.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        text_result_divs.setVisibility(View.VISIBLE);
                        listview_customadpte.notifyDataSetChanged();
                    }


                }
                else
                {


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
    public  void Connections()
    {
        AlertDialog.Builder builder4 = new AlertDialog.Builder(AdminPanelActivity.this);
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
