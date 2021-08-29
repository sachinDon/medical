package info.wkweb.com.medical;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.content.Context.MODE_PRIVATE;

public class MoreFragment extends Fragment {

RelativeLayout relative_aboutus,relative_history,relative_history_tab,relative_about_tab;
    WebView webView;
    ProgressBar progressBar;


    JSONArray array_vegitables,array_vegitables1;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView listview_customer_veg;
    String str_orderid;
    ProgressDialog progressDialog;
    AlertDialog alert11;
    Integer int_cancelor;
    TextView text_result_cust_order;
    ListviewAdaptercart listview_customadpte;
    ProgressBar progress_order;
    private LayoutInflater inflater1;
    SearchView searchbar;
    SwipeRefreshLayout swipeToRefresh_his;
    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);


        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        inflater1 =inflater;// (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

        progress_order=(ProgressBar) view.findViewById(R.id.progress_histab);
        listview_customer_veg=(ListView)view.findViewById(R.id.listview_history);
        text_result_cust_order=(TextView) view.findViewById(R.id.text_result_hist);
        searchbar = (SearchView)view.findViewById(R.id.searchView_his);

        webView = (WebView)view.findViewById(R.id.webView);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar1);
        relative_aboutus=(RelativeLayout)view.findViewById(R.id.relative_aboutus);
        relative_history=(RelativeLayout)view.findViewById(R.id.relative_history);
        relative_history_tab=(RelativeLayout)view.findViewById(R.id.relative_history_tab);
        relative_about_tab=(RelativeLayout)view.findViewById(R.id.relative_about_tab);
        swipeToRefresh_his = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh_his);
        swipeToRefresh_his.setColorSchemeResources(R.color.colorAccent);
        str_orderid="";


       LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updatehistory,
                new IntentFilter("updatehistory"));
        text_result_cust_order.setVisibility(View.INVISIBLE);
        relative_history.setVisibility(View.VISIBLE);
        relative_aboutus.setVisibility(View.INVISIBLE);
        relative_history_tab.setBackgroundColor(getResources().getColor(R.color.colortab));
        relative_about_tab.setBackgroundColor(getResources().getColor(R.color.colorblack));

        relative_history_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relative_history_tab.setBackgroundColor(getResources().getColor(R.color.colortab));
                relative_about_tab.setBackgroundColor(getResources().getColor(R.color.colorblack));
                relative_history.setVisibility(View.VISIBLE);
                relative_aboutus.setVisibility(View.INVISIBLE);

            }
        });

        relative_about_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relative_history_tab.setBackgroundColor(getResources().getColor(R.color.colorblack));
                relative_about_tab.setBackgroundColor(getResources().getColor(R.color.colortab));

                relative_history.setVisibility(View.INVISIBLE);
                relative_aboutus.setVisibility(View.VISIBLE);

            }
        });


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Urlclass.aboutus);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });

        array_vegitables = new JSONArray();
        array_vegitables1 = new JSONArray();

        listview_customadpte = new ListviewAdaptercart();
        listview_customer_veg.setAdapter(listview_customadpte);
        listview_customer_veg.setSmoothScrollbarEnabled(true);

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                Log.d("seach_query", query);
                // do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                array_vegitables = new JSONArray();
                if (array_vegitables1 != null)
                {
                    if (TextUtils.isEmpty(newText.toString())) {

                        //str_search_txt = "";
                        array_vegitables = array_vegitables1;
                    } else
                    {


                        for (int i = 0; i < array_vegitables1.length(); i++) {

                            try {
                                String string = array_vegitables1.getJSONObject(i).getString("orderid");
                                String str_category = array_vegitables1.getJSONObject(i).getString("total");
                                // str_search_txt = newText;
                                if ((string.toLowerCase()).contains(newText.toLowerCase()) || (str_category.toLowerCase()).contains(newText.toLowerCase()) ) {

                                    array_vegitables.put(array_vegitables1.getJSONObject(i));


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }


                    }


                    listview_customadpte = new ListviewAdaptercart();
                    listview_customer_veg.setAdapter(listview_customadpte);


                }
                else
                {
                    listview_customadpte = new ListviewAdaptercart();
                    listview_customer_veg.setAdapter(listview_customadpte);
                }
                return false;
            }
        });

        listview_customer_veg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView

                try {
                    JSONObject objectval = new JSONObject(String.valueOf(array_vegitables.getJSONObject(position)));
                    JSONArray jsonArr = new JSONArray(objectval.getString("orderdata"));
                    OrderlistActivity.Array_orderlist = jsonArr;
                    OrderlistActivity.str_views = "his";
                    int_cancelor = position;

                    Intent intent = new Intent(getActivity(),OrderlistActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        swipeToRefresh_his.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
                            ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = cm.getActiveNetworkInfo();
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                                new Communication_Order().execute();
                            }
                            else
                            {
                                Connections();
                                swipeToRefresh_his.setRefreshing(false);
                            }

                            // Your implementation goes here
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
                   // }
               // }).start();


            }
        });

        array_vegitables = new JSONArray();
        new Communication_Order().execute();


        return view;
    }
    private class ListviewAdaptercart extends BaseAdapter {

        @Override
        public int getCount() {
            return array_vegitables.length();
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

            view = inflater1.inflate(R.layout.list_row_his, null,false);

            final TextView text_hist_amount=(TextView)view.findViewById(R.id.text_hist_amount);
            final TextView text_order_id=(TextView)view.findViewById(R.id.text_order_id);
            final TextView text_his_status=(TextView)view.findViewById(R.id.text_his_status);
            final TextView text_his_cancel=(TextView)view.findViewById(R.id.text_his_cancel);
            final TextView text_his_Details=(TextView)view.findViewById(R.id.text_his_Details);




            text_his_Details.setTag(i);
            text_his_cancel.setTag(i);

            try {
                final JSONObject object_values = new JSONObject(String.valueOf(array_vegitables.getJSONObject(i)));
                text_order_id.setText("Order_id: "+object_values.getString("orderid"));
                text_hist_amount.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+String.valueOf(Math.round(Float.parseFloat(object_values.getString("total"))))+ " </b> </font>"));

                if (object_values.getString("status").equalsIgnoreCase("Order Cancel by self"))
                {
                    text_his_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#ff0000> <b> "+object_values.getString("status")+ " </b> </font>"));

                }
                else  if (object_values.getString("status").equalsIgnoreCase("Order Cancel by pritama"))
                {
                    text_his_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#00b33c> <b> "+object_values.getString("status")+ " </b> </font>"));

                }
                else
                {
                    text_his_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#00b33c> <b> "+object_values.getString("status")+ " </b> </font>"));
                }

                text_his_Details.setOnTouchListener(new View.OnTouchListener()
                {
                    View v;
                    private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {


                        @Override
                        public boolean onDoubleTap(MotionEvent e)
                        {

                            Toast.makeText(getActivity(), "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                            return super.onDoubleTap(e);
                        }

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {



                                try {
                                    JSONObject objectval = new JSONObject(String.valueOf(array_vegitables.getJSONObject((Integer)v.getTag())));
                                    JSONArray jsonArr = new JSONArray(objectval.getString("orderdata"));
                                    OrderlistActivity.Array_orderlist = jsonArr;
                                    OrderlistActivity.str_views = "his";
                                    int_cancelor = (Integer)v.getTag();

                                    Intent intent = new Intent(getActivity(),OrderlistActivity.class);
                                    startActivity(intent);

                                } catch (JSONException ee) {
                                    ee.printStackTrace();
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

//                text_his_Details.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        try {
//                            JSONObject objectval = new JSONObject(String.valueOf(array_vegitables.getJSONObject((Integer)v.getTag())));
//                            JSONArray jsonArr = new JSONArray(objectval.getString("orderdata"));
//                            OrderlistActivity.Array_orderlist = jsonArr;
//                            OrderlistActivity.str_views = "his";
//                            int_cancelor = (Integer)v.getTag();
//
//                            Intent intent = new Intent(getActivity(),OrderlistActivity.class);
//                            startActivity(intent);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });

                text_his_cancel.setOnTouchListener(new View.OnTouchListener()
                {
                    View v;
                    private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {


                        @Override
                        public boolean onDoubleTap(MotionEvent e)
                        {

                            Toast.makeText(getActivity(), "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                            return super.onDoubleTap(e);
                        }

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {

                            ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = cm.getActiveNetworkInfo();
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                                try {
                                    JSONObject objectval = new JSONObject(String.valueOf(array_vegitables.getJSONObject((Integer) v.getTag())));

                                    str_orderid = objectval.getString("orderid");
                                    int_cancelor = (Integer) v.getTag();
                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }

                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Cancel..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);
                                new Communication_orderDelete().execute();
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

//                text_his_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        try {
//                            JSONObject objectval = new JSONObject(String.valueOf(array_vegitables.getJSONObject((Integer)v.getTag())));
//
//                            str_orderid = objectval.getString("orderid");
//                            int_cancelor = (Integer)v.getTag();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        progressDialog = new ProgressDialog(getActivity());
//                        progressDialog.setMessage("Cancel..."); // Setting Message
//                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                        progressDialog.show(); // Display Progress Dialog
//                        progressDialog.setCancelable(false);
//                        new Communication_orderDelete().execute();
//                    }
//                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }

    public void onBackPressed() {

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public class Communication_orderDelete extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.orderhistorydelete);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("custid", pref.getString("custid",""));
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
                else if (result.equalsIgnoreCase("deleteerror"))
                {


                }
                else if (result.equalsIgnoreCase("deleted"))
                {


                    if (array_vegitables.length() !=0)
                    {
                        array_vegitables.remove(int_cancelor);
                        listview_customadpte.notifyDataSetChanged();
                        if (array_vegitables.length() ==0)
                        {
                            text_result_cust_order.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        text_result_cust_order.setVisibility(View.VISIBLE);
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


    public class Communication_Order extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.orderhistory);

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
            swipeToRefresh_his.setRefreshing(false);
            progress_order.setVisibility(View.INVISIBLE);
            text_result_cust_order.setVisibility(View.INVISIBLE);
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                }
                else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("[]"))
                {
                    text_result_cust_order.setVisibility(View.VISIBLE);

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_vegitables = new JSONArray(result);
                            array_vegitables1 = new JSONArray(result);
                            if (array_vegitables !=null) {

                                listview_customadpte = new ListviewAdaptercart();
                                listview_customer_veg.setAdapter(listview_customadpte);

                            }
                            else
                            {

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
    private BroadcastReceiver updatehistory = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (array_vegitables.length() ==0)
            {
                progress_order.setVisibility(View.VISIBLE);
            }
            new Communication_Order().execute();

        }
    };
    public  void Connections()
    {
        AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
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
}
