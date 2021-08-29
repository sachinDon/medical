package info.wkweb.com.medical;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ReviewOrderActivity extends AppCompatActivity {


    public SharedPreferences pref;
    SharedPreferences.Editor editor;
  public  static String Str_address;
    LinearLayout ll;
    LayoutInflater inflater1;
    String str_payment,str_totals;
    TextView text_back_revieworder,scroll_change_addd, scroll_address_type,scroll_check_cod,text_check_online,myorderchange,text_review_total
    ,text_revieworder_buy;

    JSONArray Array_cartArray;
    ArrayList<String> arrayList_productcode;
    ProgressDialog progressDialog;
    AlertDialog alert11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        inflater1 =ReviewOrderActivity.this.getLayoutInflater();
        ll = (LinearLayout)findViewById(R.id.linear_dyanamicrow_revieworder);
        text_back_revieworder= (TextView) findViewById(R.id.text_back_revieworder);
        scroll_address_type= (TextView) findViewById(R.id.scroll_address_type);

        scroll_check_cod= (TextView) findViewById(R.id.scroll_check_cod);
        text_check_online= (TextView) findViewById(R.id.text_check_online);
        myorderchange= (TextView) findViewById(R.id.myorderchange);

        text_review_total= (TextView) findViewById(R.id.text_review_total);
        text_revieworder_buy= (TextView) findViewById(R.id.text_revieworder_buy);

        arrayList_productcode = new ArrayList<String>();
            str_payment = "cod";
            scroll_check_cod.setBackgroundResource(R.drawable.checkadd);
            text_check_online.setBackgroundResource(R.drawable.uncheckadd);


                scroll_check_cod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        str_payment = "cod";
                        scroll_check_cod.setBackgroundResource(R.drawable.checkadd);
                        text_check_online.setBackgroundResource(R.drawable.uncheckadd);
                        editor.putString("mode","cod");
                        editor.commit();
                    }
                });

        text_check_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_payment = "online";
                scroll_check_cod.setBackgroundResource(R.drawable.uncheckadd);
                text_check_online.setBackgroundResource(R.drawable.checkadd);
                editor.putString("mode","online");
                editor.commit();


            }
        });


        text_back_revieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });



        myorderchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        text_revieworder_buy.setOnTouchListener(new View.OnTouchListener()
        {
            View v;
            private GestureDetector gestureDetector = new GestureDetector(ReviewOrderActivity.this, new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onDoubleTap(MotionEvent e)
                {

                    Toast.makeText(ReviewOrderActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {

                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting())
                    {

                        float float_totalvalues= Float.parseFloat(str_totals);

                        if (str_payment.equalsIgnoreCase("online"))
                        {
                            if (float_totalvalues >= 2000)
                            {
                                editor.putString("mode", "cod");
                                editor.commit();
                                progressDialog = new ProgressDialog(ReviewOrderActivity.this);
                                progressDialog.setMessage("Loading..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);

                                new Communication_Order().execute();
                            }
                            else
                            {
                                AlertDialog.Builder builder4 = new AlertDialog.Builder(ReviewOrderActivity.this);
                                    builder4.setTitle("Order Limit!");
                                    builder4.setMessage("Your cart items should be minimum order ₹2000/-");
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
                        else
                        {

                            if (float_totalvalues >= 2000 && float_totalvalues <= 10000)
                            {
                                editor.putString("mode", "cod");
                                editor.commit();
                                progressDialog = new ProgressDialog(ReviewOrderActivity.this);
                                progressDialog.setMessage("Loading..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);

                                new Communication_Order().execute();
                            }
                            else
                            {

                                if (float_totalvalues <= 2000) {

                                    AlertDialog.Builder builder4 = new AlertDialog.Builder(ReviewOrderActivity.this);
                                    builder4.setTitle("Order Limit!");
                                    builder4.setMessage("Your cart items should be minimum order ₹2000/-");
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
                                else if (float_totalvalues >= 10000)
                                {
                                    AlertDialog.Builder builder4 = new AlertDialog.Builder(ReviewOrderActivity.this);
                                    builder4.setTitle("Order Limit!");
                                    builder4.setMessage("Your cart items should be maximum order ₹10000/- on cash on delivery");
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


//        text_revieworder_buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                editor.putString("mode","cod");
//                editor.commit();
//                progressDialog = new ProgressDialog(ReviewOrderActivity.this);
//                progressDialog.setMessage("Loading..."); // Setting Message
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                progressDialog.show(); // Display Progress Dialog
//                progressDialog.setCancelable(false);
//
//                    new Communication_Order().execute();
//
//
//
//            }
//        });



                scroll_address_type.setText(Str_address);

       Array_cartArray = SingletonObject.Instance().getArray_Addcart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addRow();
            }
        }, Array_cartArray.length());

    }

    public  void  addRow()
    {

        float totals = 0;
        ll.removeAllViews();
        for (int i = 0; i < Array_cartArray.length(); i++)
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
                JSONObject obj_val = new JSONObject(String.valueOf(Array_cartArray.getJSONObject(i)));

                text_cart_qtytitle.setText(obj_val.getString("purch"));
                text_productname_cart.setText(obj_val.getString("productname"));
                text_mgfname_cart.setText(obj_val.getString("mfgcmp"));
                text_mrp_price_cart.setText("₹"+obj_val.getString("mrp"));
                text_salerate_price_cart.setText( "₹"+obj_val.getString("netsale"));
                text_expdate_cart.setText("₹"+obj_val.getString("subtotal"));
                String str_imageurl = obj_val.getString("imageurl");

                arrayList_productcode.add(obj_val.getString("productcode"));

                if (str_imageurl.length() == 0)
                {
                    str_imageurl ="http://www.sachinmokashi";
                }
                totals +=Float.parseFloat(obj_val.getString("subtotal"));

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
                text_review_total.setText("₹ "+String.valueOf(Math.round(totals)));


                Picasso.with(ReviewOrderActivity.this)
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
                    private GestureDetector gestureDetector = new GestureDetector(ReviewOrderActivity.this, new GestureDetector.SimpleOnGestureListener() {


                        @Override
                        public boolean onDoubleTap(MotionEvent e)
                        {

                            Toast.makeText(ReviewOrderActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                            return super.onDoubleTap(e);
                        }

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {



                            ImageView image = (ImageView)v.findViewWithTag(v.getTag());
                            image.invalidate();
                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            ZoomImageActivity.drawableBitmap=bitmap;
                            Intent intent=new Intent(ReviewOrderActivity.this,ZoomImageActivity.class);
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
//                        Intent intent=new Intent(ReviewOrderActivity.this,ZoomImageActivity.class);
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
    }



    public class Communication_Order extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.custorder);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("custid", pref.getString("custid",""));
                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("custname", pref.getString("custname",""));
                postDataParams.put("address",Str_address);
                postDataParams.put("paymentmode", str_payment);
                postDataParams.put("total", str_totals);


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000 /* milliseconds */);
                conn.setConnectTimeout(30000 /* milliseconds */);
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

progressDialog.dismiss();
            if (result != null) {

                if (result.equalsIgnoreCase("nullerror"))
                {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ReviewOrderActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Some field missing,Please try again.");
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
                else if (result.equalsIgnoreCase("error"))
                {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ReviewOrderActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Server could not found,Please try again.");
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
                else if (result.equalsIgnoreCase("added"))
                {
                    SingletonObject.Instance().setstr_revieworder("yes");
                    SingletonObject.Instance().setstr_updatehomecart("yes");



                    Intent intent1 = new Intent("ordercomplete");
                    intent1.putExtra("productcodes", arrayList_productcode.toString());
                    LocalBroadcastManager.getInstance(ReviewOrderActivity.this).sendBroadcast(intent1);

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ReviewOrderActivity.this);
                    builder2.setTitle("Successful order!");
                    builder2.setMessage("Your order has been submited");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    Intent intent1 = new Intent("cartupdate");
                                    LocalBroadcastManager.getInstance(ReviewOrderActivity.this).sendBroadcast(intent1);

                                    finish();
                                    if (str_payment.equalsIgnoreCase("online"))
                                    {
                                        Intent intens = new Intent(ReviewOrderActivity.this, OnlineActivity.class);
                                        startActivity(intens);
                                    }

                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();

                }
                else
                {

                        if (!result.equalsIgnoreCase("") && result.length()>=50) {
                            try {
                                JSONArray array_addcart = new JSONArray(result);
                                if (array_addcart !=null) {

                                 JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(0)));

                                 String str_proinfo = " ProductName: "+obj_val.getString("productname")+" Stock is not availabel." +
                                         "";

                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ReviewOrderActivity.this);
                                    builder2.setTitle("Oops!");
                                    builder2.setMessage(str_proinfo);
                                    builder2.setCancelable(false);
                                    builder2.setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    Intent intent2 = new Intent("cartproducts");
                                                    LocalBroadcastManager.getInstance(ReviewOrderActivity.this).sendBroadcast(intent2);
                                                    finish();
                                                }
                                            });
                                    alert11 = builder2.create();
                                    alert11.show();

                                }
                                else
                                {
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ReviewOrderActivity.this);
                                    builder2.setTitle("Oops");
                                    builder2.setMessage("Server could not found");
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




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {


                            AlertDialog.Builder builder2 = new AlertDialog.Builder(ReviewOrderActivity.this);
                            builder2.setTitle("Oops");
                            builder2.setMessage("Server could not found");
                            builder2.setCancelable(false);
                            builder2.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            SingletonObject.Instance().setstr_revieworder("yes");
                                            SingletonObject.Instance().setstr_updatehomecart("yes");
//                                    Intent intent1 = new Intent("ordercomplete");
//                                    intent1.putExtra("productcodes", arrayList_productcode.toString());
//                                    LocalBroadcastManager.getInstance(ReviewOrderActivity.this).sendBroadcast(intent1);
                                            Intent intent2 = new Intent("cartupdate");
                                            LocalBroadcastManager.getInstance(ReviewOrderActivity.this).sendBroadcast(intent2);
                                            finish();
                                        }
                                    });
                            alert11 = builder2.create();
                            alert11.show();
                        }
                }


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
        AlertDialog.Builder builder4 = new AlertDialog.Builder(ReviewOrderActivity.this);
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
