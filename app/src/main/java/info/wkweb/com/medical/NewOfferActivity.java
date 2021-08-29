package info.wkweb.com.medical;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class NewOfferActivity extends AppCompatActivity {

    //

    JSONArray array_home;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView listview_home_list;
    ProgressBar progress_home;
    ProgressDialog progressDialog;

    AlertDialog alert11;
    String str_productcode,str_mgfcmp,str_productname,str_packing,str_expiry,str_image,str_mrp,str_note,str_netsale,str_netsale1,str_purch,str_subtotal;
    Integer int_tag_val;
    SearchView searchbar;
    JSONArray array_preoduct,array_preoduct1;
    ListviewAdaptercart listview_customadpte;
    TextView text_back_newoff;
    String str_offersper;
    SwipeRefreshLayout swipeToRefresh_home;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        progress_home=(ProgressBar)findViewById(R.id.progress_home_new);
        listview_home_list=(ListView)findViewById(R.id.listview_home_list_new);
        searchbar = (SearchView) findViewById(R.id.searchView_new);
        swipeToRefresh_home = (SwipeRefreshLayout)findViewById(R.id.swipeToRefresh_home_new);
        swipeToRefresh_home.setColorSchemeResources(R.color.colorAccent);
        text_back_newoff = (TextView)findViewById(R.id.text_back_newoff);


        text_back_newoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
            }
        });

        array_preoduct = new JSONArray();
        array_preoduct1 = new JSONArray();

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                Log.d("seach_query", query);
                // do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                array_preoduct = new JSONArray();
                if (array_preoduct1 != null)
                {
                    if (TextUtils.isEmpty(newText.toString())) {

                        //str_search_txt = "";
                            array_preoduct = array_preoduct1;
                    } else
                    {


                            for (int i = 0; i < array_preoduct1.length(); i++) {

                                try {
                                    String string = array_preoduct1.getJSONObject(i).getString("productname");
                                    String str_category = array_preoduct1.getJSONObject(i).getString("mfgcmp");
                                    // str_search_txt = newText;
                                    if ((string.toLowerCase()).contains(newText.toLowerCase()) || (str_category.toLowerCase()).contains(newText.toLowerCase()) ) {

                                        array_preoduct.put(array_preoduct1.getJSONObject(i));


                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                        }


                    }


                    listview_customadpte = new ListviewAdaptercart();
                    listview_home_list.setAdapter(listview_customadpte);


                }
                else
                {
                    listview_customadpte = new ListviewAdaptercart();
                    listview_home_list.setAdapter(listview_customadpte);
                }
                return false;
            }
        });
        swipeToRefresh_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {

                            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = cm.getActiveNetworkInfo();
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                                new Communication_products().execute();
                            }
                            else
                            {
                                swipeToRefresh_home.setRefreshing(false);
                                Connections();
                            }

//                            // Your implementation goes here
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }).start();


            }
        });

        new Communication_products().execute();
    }

    private class ListviewAdaptercart extends BaseAdapter {
        private LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return array_preoduct.length();
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

            view = inflater.inflate(R.layout.listrow_home, null,false);
            ImageView image_tab = (ImageView) view.findViewById(R.id.image_product);
            TextView text_tab_name = (TextView) view.findViewById(R.id.text_productname);
            TextView text_mgfname = (TextView) view.findViewById(R.id.text_mgfname);
            TextView text_offer_note = (TextView) view.findViewById(R.id.text_offer_note);

            TextView text_tab_mrp = (TextView) view.findViewById(R.id.text_mrp_price);
            TextView text_tab_exp = (TextView) view.findViewById(R.id.text_expdate);
            TextView  text_tab_salerate = (TextView) view.findViewById(R.id.text_salerate_price);
            TextView  text_tab_salerate2 = (TextView) view.findViewById(R.id.text_salerate_price2);
            TextView  text_tab_salerate3 = (TextView) view.findViewById(R.id.text_salerate_price3);
            TextView  text_tab_pkg = (TextView) view.findViewById(R.id.text_packingdate);
            TextView  text_AvailabelStock = (TextView) view.findViewById(R.id.text_AvailabelStock);
            TextView  text_home_addtocart = (TextView) view.findViewById(R.id.text_home_addtocart);
            TextView  text_offer_per = (TextView) view.findViewById(R.id.text_offer_per);


            text_tab_salerate.setTextColor(getResources().getColor(R.color.colorlightgray));
            text_tab_salerate2.setTextColor(getResources().getColor(R.color.colorlightgray));

            text_tab_salerate2.setText("");
            text_tab_salerate3.setText("");
            text_offer_per.setText("");
            text_tab_salerate2.setVisibility(View.VISIBLE);
            text_tab_salerate3.setVisibility(View.VISIBLE);
            text_offer_per.setVisibility(View.VISIBLE);


            text_home_addtocart.setTag(i);
            image_tab.setTag(i);
            final JSONObject object_values;
            try {
                object_values = new JSONObject(String.valueOf(array_preoduct.getJSONObject(i)));
                 String str_imageurl = object_values.getString("imageurl");
                text_tab_name.setText(String.valueOf(object_values.getString("productname")));
                text_tab_mrp.setText(String.valueOf("₹"+object_values.getString("mrp")));
                text_tab_salerate.setText(String.valueOf("₹"+object_values.getString("netsale")));
                text_tab_salerate2.setText(String.valueOf("₹"+object_values.getString("netsale")));
               text_tab_salerate3.setText(String.valueOf("₹"+object_values.getString("netsale1")));
                text_offer_per.setText(String.valueOf(object_values.getString("offer") + "%"));
                text_tab_exp.setText(String.valueOf(object_values.getString("expdate")));
                text_tab_pkg.setText(String.valueOf(object_values.getString("packing")));
                text_AvailabelStock.setText(String.valueOf(object_values.getString("stockinhand")));
                text_offer_note.setText(String.valueOf(object_values.getString("note")));
                text_offer_note.setVisibility(View.GONE);
                if (str_imageurl.length() == 0)
                {
                    str_imageurl ="http://www.sachinmokashi";
                }

                if (String.valueOf(object_values.getString("note")).length() != 0)
                {
                    text_offer_note.setVisibility(View.VISIBLE);
                }

                if (String.valueOf(object_values.getString("mfgcmp")).length() == 0)
                {
                    text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")));
                }
                else
                {
                    if (String.valueOf(object_values.getString("mfgcmp")).length() >=8)
                    {
                        text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")).substring(0,7));

                    }
                    else
                    {
                        text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")).substring(0,String.valueOf(object_values.getString("mfgcmp")).length()-1));
                    }

                }

                if (object_values.getString("cart").equalsIgnoreCase("yes")) {
                    text_home_addtocart.setVisibility(View.INVISIBLE);
                } else {
                    text_home_addtocart.setVisibility(View.VISIBLE);
                }
                Picasso.with(NewOfferActivity.this)
                        .load(str_imageurl)
                        .placeholder(R.drawable.default1)
                        .into( image_tab, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });

                image_tab.setOnTouchListener(new View.OnTouchListener()
                {
                    View v;
                    private GestureDetector gestureDetector = new GestureDetector(NewOfferActivity.this, new GestureDetector.SimpleOnGestureListener() {


                        @Override
                        public boolean onDoubleTap(MotionEvent e)
                        {

                            Toast.makeText(NewOfferActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                            return super.onDoubleTap(e);
                        }

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {

                            ImageView image = (ImageView)v.findViewWithTag(v.getTag());
                            image.invalidate();
                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            ZoomImageActivity.drawableBitmap=bitmap;
                            Intent intent=new Intent(NewOfferActivity.this,ZoomImageActivity.class);
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


//                image_tab.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        ImageView image = (ImageView)v.findViewWithTag(v.getTag());
//                        image.invalidate();
//                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//                        Bitmap bitmap = drawable.getBitmap();
//                        ZoomImageActivity.drawableBitmap=bitmap;
//                        Intent intent=new Intent(NewOfferActivity.this,ZoomImageActivity.class);
//                        startActivity(intent);
//
//                    }
//                });


                text_home_addtocart.setOnTouchListener(new View.OnTouchListener()
                {
                    View v;
                    private GestureDetector gestureDetector = new GestureDetector(NewOfferActivity.this, new GestureDetector.SimpleOnGestureListener() {


                        @Override
                        public boolean onDoubleTap(MotionEvent e)
                        {

                            Toast.makeText(NewOfferActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                            return super.onDoubleTap(e);
                        }

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {


                            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = cm.getActiveNetworkInfo();
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                                progressDialog = new ProgressDialog(NewOfferActivity.this);
                                progressDialog.setMessage("Add to cart..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                try {
                                    JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct.getJSONObject((Integer) v.getTag())));

                                    str_productcode = String.valueOf(obj_val.getString("productcode"));
                                    str_productname = String.valueOf(obj_val.getString("productname"));
                                    str_mgfcmp = String.valueOf(obj_val.getString("mfgcmp"));
                                    str_mrp = String.valueOf(obj_val.getString("mrp"));
                                    str_image = String.valueOf(obj_val.getString("imageurl"));
                                    str_purch = "1";
                                    str_subtotal = String.valueOf(obj_val.getString("netsale1"));
                                    str_netsale = String.valueOf(obj_val.getString("netsale"));
                                    str_netsale1 = String.valueOf(obj_val.getString("netsale1"));
                                    str_note = String.valueOf(obj_val.getString("note"));
                                    str_expiry = String.valueOf(obj_val.getString("expdate"));
                                    str_packing = String.valueOf(obj_val.getString("packing"));
                                    str_offersper= String.valueOf(obj_val.getString("offer"));


                                    int_tag_val = (Integer) v.getTag();

                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }

                                new Communication_addcart().execute();

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

//                text_home_addtocart.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                        progressDialog = new ProgressDialog(NewOfferActivity.this);
//                        progressDialog.setMessage("Add to cart..."); // Setting Message
//                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                        progressDialog.show(); // Display Progress Dialog
//                        progressDialog.setCancelable(false);
//                        progressDialog.show();
//
//                        try {
//                            JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct.getJSONObject((Integer) v.getTag())));
//
//                            str_productcode = String.valueOf(obj_val.getString("productcode"));
//                            str_productname = String.valueOf(obj_val.getString("productname"));
//                            str_mgfcmp = String.valueOf(obj_val.getString("mfgcmp"));
//                            str_mrp = String.valueOf(obj_val.getString("mrp"));
//                            str_image = String.valueOf(obj_val.getString("imageurl"));
//                            str_purch = "1";
//                            str_subtotal = String.valueOf(obj_val.getString("netsale"));
//                            str_netsale = String.valueOf(obj_val.getString("netsale"));
//                            str_expiry = String.valueOf(obj_val.getString("expdate"));
//                            str_packing = String.valueOf(obj_val.getString("packing"));
//
//
//                            int_tag_val = (Integer) v.getTag();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        new Communication_addcart().execute();
//
//
//                    }
//                });



            } catch (JSONException e) {
                e.printStackTrace();
            }


            return view;
        }
    }
    public class Communication_products extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

            URL url = new URL(Urlclass.productoffer);




                JSONObject postDataParams = new JSONObject();

                postDataParams.put("custid", pref.getString("custid",""));
                postDataParams.put("mobile", pref.getString("userid",""));

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

            progress_home.setVisibility(View.INVISIBLE);
            swipeToRefresh_home.setRefreshing(false);
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))
                {

                }
                else if (result.equalsIgnoreCase("error"))
                {
                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {

                               array_preoduct = new JSONArray(result);
                               array_preoduct1 = new JSONArray(result);
                                //  array_preoduct = new JSONArray(array);



                                listview_customadpte = new ListviewAdaptercart();
                                listview_home_list.setAdapter(listview_customadpte);


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

    public class Communication_addcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(Urlclass.addcart);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("custid", pref.getString("custid",""));
                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("productcode",str_productcode);
                postDataParams.put("productname",str_productname);
                postDataParams.put("mfgcmp",str_mgfcmp);
                postDataParams.put("mrp",str_mrp);
                postDataParams.put("subtotal",str_subtotal);
                postDataParams.put("purch",str_purch);
                postDataParams.put("netsale",str_netsale);
                postDataParams.put("netsale1",str_netsale1);
                postDataParams.put("note",str_note);
                postDataParams.put("expdate",str_expiry);
                postDataParams.put("packing",str_packing);
                postDataParams.put("imageurl",str_image);
                postDataParams.put("offers","yes");
                postDataParams.put("offersper",str_offersper);


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000 /* milliseconds */);
                conn.setConnectTimeout(60000 /* milliseconds */);
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

            if (progressDialog !=null) {
                progressDialog.dismiss();
            }
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("added"))
                {
                    Intent intent1 = new Intent("updatebuge1");
                    LocalBroadcastManager.getInstance(NewOfferActivity.this).sendBroadcast(intent1);

                    SingletonObject.Instance().setstr_updatecart("yes");

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(NewOfferActivity.this);
                    builder4.setTitle("Successful");
                    builder4.setMessage("Your item added to list");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

//                                    new Communication_countaddcart().execute();
//                                    Intent intent1 = new Intent("cartcustomerupdate1");
//                                    LocalBroadcastManager.getInstance(CustomersActivity.this).sendBroadcast(intent1);

                                }
                            });
                    alert11 = builder4.create();
                    alert11.show();

                    try {
                        JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct.getJSONObject(int_tag_val)));

                        obj_val.put("cart","yes");
                        array_preoduct.put(int_tag_val,obj_val);
                        listview_customadpte.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
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
    public  void Connections()
    {
        AlertDialog.Builder builder4 = new AlertDialog.Builder(NewOfferActivity.this);
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
