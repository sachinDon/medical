package info.wkweb.com.medical;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

public class OrderlistActivity extends AppCompatActivity {


    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    String str_orderid;
    LinearLayout ll;
    LayoutInflater inflater1;
    String str_payment,Str_address,str_totals;

    TextView  text_back_orderdetail,text_orderid_details,text_orderlist_status,text_orderlist_datetime,text_orderlist_payment,
            text_orderlist_totalamt,text_orderlist_cancel,text_orderlist_addrss_dr;

   public static JSONArray Array_orderlist;
   public  static String str_views;

    ProgressDialog progressDialog;
    AlertDialog alert11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        inflater1 =OrderlistActivity.this.getLayoutInflater();
        ll = (LinearLayout)findViewById(R.id.linear_dyn_orderlist);
        text_back_orderdetail= (TextView) findViewById(R.id.text_back_orderdetail);
        text_orderid_details= (TextView) findViewById(R.id.text_orderid_details);
        text_orderlist_status= (TextView) findViewById(R.id.text_orderlist_status);
        text_orderlist_datetime= (TextView) findViewById(R.id.text_orderlist_datetime);
        text_orderlist_payment= (TextView) findViewById(R.id.text_orderlist_payment);
        text_orderlist_totalamt= (TextView) findViewById(R.id.text_orderlist_totalamt);
        text_orderlist_cancel= (TextView) findViewById(R.id.text_orderlist_cancel);
        text_orderlist_addrss_dr= (TextView) findViewById(R.id.text_orderlist_addrss_dr);

        text_orderlist_cancel.setTextColor(getResources().getColor(R.color.colorwhite));
        GradientDrawable bgShape1 = (GradientDrawable)text_orderlist_cancel.getBackground();
        bgShape1.mutate();
        bgShape1.setColor(getResources().getColor(R.color.colorred));
        bgShape1.setStroke(text_orderlist_cancel.getWidth(),getResources().getColor(R.color.colorred));
        text_orderlist_cancel.setVisibility(View.VISIBLE);
//if (str_views.equalsIgnoreCase("his") || str_views.equalsIgnoreCase("admin"))
//{
    text_orderlist_cancel.setVisibility(View.GONE);
//}

        text_orderlist_cancel.setOnTouchListener(new View.OnTouchListener()
        {
            View v;
            private GestureDetector gestureDetector = new GestureDetector(OrderlistActivity.this, new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onDoubleTap(MotionEvent e)
                {

                    Toast.makeText(OrderlistActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {

                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        progressDialog = new ProgressDialog(OrderlistActivity.this);
                        progressDialog.setMessage("Cancel..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                        new Communication_ordercancel().execute();
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


//        text_orderlist_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressDialog = new ProgressDialog(OrderlistActivity.this);
//                progressDialog.setMessage("Cancel..."); // Setting Message
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                progressDialog.show(); // Display Progress Dialog
//                progressDialog.setCancelable(false);
//        new Communication_ordercancel().execute();
//
//            }
//        });




        text_back_orderdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//[{"orderid":"45c2e-d3f5120250257a8c-2ae96","status":"pending","total":"0","paymentmode":"cod","orderdata":[{"orderid":"45c2e-d3f5120250257a8c-2ae96","status":"pending","total":"0","paymentmode":"cod"},{"orderid":"45c2e-d3f5120250257a8c-2ae96","status":"pending","total":"0","paymentmode":"cod"}]},{"orderid":"299ea-e249c202502b1c6c-a6389","status":"pending","total":"0","paymentmode":"cod","orderdata":[{"orderid":"299ea-e249c202502b1c6c-a6389","status":"pending","total":"0","paymentmode":"cod"},{"orderid":"299ea-e249c202502b1c6c-a6389","status":"pending","total":"0","paymentmode":"cod"},{"orderid":"299ea-e249c202502b1c6c-a6389","status":"pending","total":"0","paymentmode":"cod"}]}]
            try {
                JSONObject jsonObj_address = new JSONObject(String.valueOf(Array_orderlist.getJSONObject(0)));
                str_orderid =jsonObj_address.getString("orderid");
                text_orderid_details.setText("Order_id: "+jsonObj_address.getString("orderid"));
               // text_orderlist_status.setText("Status: "+jsonObj_address.getString("status"));
              text_orderlist_datetime.setText("Date: "+jsonObj_address.getString("date"));
              //  text_orderlist_payment.setText("Payement: "+jsonObj_address.getString("paymentmode"));
               // text_orderlist_totalamt.setText("Total: "+jsonObj_address.getString("total"));


                if (jsonObj_address.getString("paymentmode").equalsIgnoreCase("cod"))
                {
                    // text_order_paymentmode.setText("Payment: Cash On Dilevery");
                    text_orderlist_payment.setText(Html.fromHtml( " <font color=#414141>  "+"Payment: "+ " </font> <font color=#00b33c> <b> "+"Cash On Dilevery"+ " </b> </font>"));

                }
                else
                {
                    // text_order_paymentmode.setText("Payment: Online");
                    text_orderlist_payment.setText(Html.fromHtml( " <font color=#414141>  "+"Payment: "+ " </font> <font color=#00b33c> <b> "+"Online"+ " </b> </font>"));

                }
                if (str_views.equalsIgnoreCase("his"))
                {
                    if (jsonObj_address.getString("status").equalsIgnoreCase("Order Cancel by self"))
                    {
                        text_orderlist_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#ff0000> <b> "+jsonObj_address.getString("status")+ " </b> </font>"));

                    }
                    else  if (jsonObj_address.getString("status").equalsIgnoreCase("Order Cancel by pritama"))
                    {
                        text_orderlist_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#00b33c> <b> "+jsonObj_address.getString("status")+ " </b> </font>"));

                    }
                    else
                    {
                        text_orderlist_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#00b33c> <b> "+jsonObj_address.getString("status")+ " </b> </font>"));
                    }
                }
                else {
                    if (jsonObj_address.getString("status").equalsIgnoreCase("pending"))
                    {

//                        if (str_views.equalsIgnoreCase("his") || str_views.equalsIgnoreCase("admin"))
//                        {
                            text_orderlist_cancel.setVisibility(View.GONE);
//                        }
//                        else
//                        {
//                            text_orderlist_cancel.setVisibility(View.VISIBLE);
//                        }
                        text_orderlist_status.setText(Html.fromHtml(" <font color=#414141>  " + "Status: " + " </font> <font color=#ff0000> <b> " + jsonObj_address.getString("status") + " </b> </font>"));

                    }
                    else
                        {
                            text_orderlist_cancel.setVisibility(View.GONE);
                        text_orderlist_status.setText(Html.fromHtml(" <font color=#414141>  " + "Status: " + " </font> <font color=#00b33c> <b> " + jsonObj_address.getString("status") + " </b> </font>"));

                    }
                }
                text_orderlist_totalamt.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+jsonObj_address.getString("total")+ " </b> </font>"));


                text_orderlist_addrss_dr.setText(jsonObj_address.getString("address"));


            } catch (JSONException e) {
                e.printStackTrace();
            }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addRow();
            }
        }, Array_orderlist.length());



    }
    public  void  addRow()
    {

        float totals = 0;
        ll.removeAllViews();
        for (int i = 0; i < Array_orderlist.length(); i++)
        {
            final View view1 = inflater1.inflate(R.layout.list_revieworderlist, null);

            ImageView imageView = (ImageView) view1.findViewById(R.id.image_rvorder);
            TextView text_productname_cart = (TextView) view1.findViewById(R.id.text_title_rvor);
            TextView text_mgfname_cart = (TextView) view1.findViewById(R.id.text_title_rvor_mfg);
            TextView text_mrp_price_cart = (TextView) view1.findViewById(R.id.text_title_rvor_price);
            TextView text_salerate_price_cart = (TextView) view1.findViewById(R.id.text_title_rvor_netsale);
            TextView text_expdate_cart = (TextView) view1.findViewById(R.id.text_title_rvor_total);
            TextView text_cart_qtytitle = (TextView) view1.findViewById(R.id.text_title_rvor_kgs1);

            imageView.setTag(i);

            try {
                JSONObject obj_val = new JSONObject(String.valueOf(Array_orderlist.getJSONObject(i)));

                text_cart_qtytitle.setText(obj_val.getString("purch"));
                text_productname_cart.setText(obj_val.getString("productname"));
                text_mgfname_cart.setText(obj_val.getString("mfgcmp"));
                text_mrp_price_cart.setText("₹"+obj_val.getString("mrp"));
                text_salerate_price_cart.setText( "₹"+obj_val.getString("netsale"));
                text_expdate_cart.setText("₹"+obj_val.getString("subtotal"));
                String str_imageurl = obj_val.getString("imageurl");
                if (str_imageurl.length() == 0)
                {
                    str_imageurl ="http://www.sachinmokashi";
                }
                if (!(obj_val.getString("subtotal").equalsIgnoreCase(""))) {
                    totals += Float.parseFloat(obj_val.getString("subtotal"));
                }

                if (String.valueOf(obj_val.getString("mfgcmp")).length() == 0)
                {
                    text_mgfname_cart.setText(String.valueOf(obj_val.getString("mfgcmp")));
                }
                else
                {
                    if (String.valueOf(obj_val.getString("mfgcmp")).length() >=8)
                    {
                        text_mgfname_cart.setText(String.valueOf(obj_val.getString("mfgcmp")).substring(0,7));

                    }
                    else
                    {
                        text_mgfname_cart.setText(String.valueOf(obj_val.getString("mfgcmp")).substring(0,String.valueOf(obj_val.getString("mfgcmp")).length()-1));
                    }

                }

                str_totals = String.valueOf(totals);

                text_orderlist_totalamt.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+String.valueOf(Math.round(totals))+ " </b> </font>"));



                Picasso.with(OrderlistActivity.this)
                        .load(str_imageurl)
                        .placeholder(R.drawable.default1)
                        .into( imageView, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });


                imageView.setOnTouchListener(new View.OnTouchListener()
                {
                    View v;
                    private GestureDetector gestureDetector = new GestureDetector(OrderlistActivity.this, new GestureDetector.SimpleOnGestureListener() {


                        @Override
                        public boolean onDoubleTap(MotionEvent e)
                        {

                            Toast.makeText(OrderlistActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                            return super.onDoubleTap(e);
                        }

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {


                            ImageView image = (ImageView)v.findViewWithTag(v.getTag());
                            image.invalidate();
                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            ZoomImageActivity.drawableBitmap=bitmap;
                            Intent intent=new Intent(OrderlistActivity.this,ZoomImageActivity.class);
                            startActivity(intent);

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

//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        ImageView image = (ImageView)v.findViewWithTag(v.getTag());
//                        image.invalidate();
//                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//                        Bitmap bitmap = drawable.getBitmap();
//                        ZoomImageActivity.drawableBitmap=bitmap;
//                        Intent intent=new Intent(OrderlistActivity.this,ZoomImageActivity.class);
//                        startActivity(intent);
//
//                    }
//                });


            } catch (JSONException e)
            {
                e.printStackTrace();
            }






//


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 25);
            view1.setLayoutParams(params);


            ll.addView(view1);
        }

//        {
//            final View view1 = inflater1.inflate(R.layout.list_revieworderlist, null);
//            final ImageView image_vend = (ImageView) view1.findViewById(R.id.image_rvorder);
//            final TextView text_title_cart = (TextView) view1.findViewById(R.id.text_title_rvor);
//            final TextView text_title_vend_price = (TextView) view1.findViewById(R.id.text_title_rvor_price);
//            final TextView text_title_rvor_kgs = (TextView) view1.findViewById(R.id.text_title_rvor_kgs1);
//
//
//            try
//            {
//
//                JSONObject obj_val = new JSONObject(String.valueOf(Array_orderlist.getJSONObject(i)));
//                text_title_cart.setText(obj_val.getString("name"));
//                text_title_vend_price.setText("₹ "+obj_val.getString("amount"));
//                text_title_rvor_kgs.setText(obj_val.getString("purch")+"kg");
//                final String str_imageurl = obj_val.getString("image");
//                totals +=Integer.parseInt(obj_val.getString("subtotal"));
//                str_totals = String.valueOf(totals);
//              //  text_orderlist_totalamt.setText("Rs "+String.valueOf(totals));
//                text_orderlist_totalamt.setText(Html.fromHtml( " <font color=#414141>  "+"Total amount: "+ " </font> <font color=#00b33c> <b> "+"₹"+String.valueOf(totals)+ " </b> </font>"));
//
//                Picasso.with(OrderlistActivity.this)
//                        .load(str_imageurl)
//                        .placeholder(R.drawable.default1)
//                        .into( image_vend, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError() {
//
//                            }
//                        });
//
//
//
//
//            } catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(0, 0, 0, 25);
//            view1.setLayoutParams(params);
//
//
//            ll.addView(view1);
//        }
    }

    public class Communication_ordercancel extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.ordercancel);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("custid", pref.getString("custid",""));
                postDataParams.put("orderid", str_orderid);
                postDataParams.put("status", "Order Cancel by self");

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
                else if (result.equalsIgnoreCase("refresh"))
                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(OrderlistActivity.this);
                    builder4.setTitle("Opps!");
                    builder4.setMessage("Admin has been updated your order you can check in history section");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    SingletonObject.Instance().setstr_updatehomecart("yes");
                                    SingletonObject.Instance().setstr_ordercancel("yes");
                                    Intent intent1 = new Intent("ordercancel");
                                    LocalBroadcastManager.getInstance(OrderlistActivity.this).sendBroadcast(intent1);
                                    finish();
                                }
                            });
                    alert11 = builder4.create();
                    alert11.show();


                }
                else if (result.equalsIgnoreCase("deleted"))
                {
                    SingletonObject.Instance().setstr_updatehomecart("yes");
                    SingletonObject.Instance().setstr_ordercancel("yes");
                    Intent intent1 = new Intent("ordercancel");
                    LocalBroadcastManager.getInstance(OrderlistActivity.this).sendBroadcast(intent1);

                    finish();

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
        AlertDialog.Builder builder4 = new AlertDialog.Builder(OrderlistActivity.this);
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
