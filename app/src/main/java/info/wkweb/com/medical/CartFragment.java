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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static android.content.Context.MODE_PRIVATE;


public class CartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    JSONArray array_addcart;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    LayoutInflater inflater1;
    LinearLayout ll;
    String str_custid,str_productcode,str_subtotal,str_purch,str_pincode,str_selectadd;
    Integer int_tag_val,int_arrayindex;
    float float_values,float_totalvalues;
    ProgressDialog progressDialog;
    TextView text_cart_subtotal,text_cart_buy,text_result_cust_cartsitems;
    TextView text_add_div_addres1,text_add_div_addres12,text_add_div_name,text_add_div_name2;
    ProgressBar progress_customer_cart;
    AlertDialog alert11;
    public static String str_stringname,str_address;

    RelativeLayout relative_address2_addm,relative_address_address2_text;
    TextView text_address2_addaddress,text_select_add_div,text_select_add_div2;
    ListviewAdaptercart listview_adaptercart;
    ListView listview_addcart_list;


    public CartFragment() {
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

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        progress_customer_cart = (ProgressBar)view.findViewById(R.id.progress_customer_cart);
        str_selectadd ="address1";
        str_subtotal="0";
        int_arrayindex = 0;
        float_values = 0;
        float_totalvalues = 0;
        str_stringname = pref.getString("custname1","");
        str_address = pref.getString("address1","") +"\n"+"Phone No: "+pref.getString("mobile1","")+"\n"+"Pincode: "+pref.getString("pincode1","");

        ReviewOrderActivity.Str_address = str_stringname + str_address;
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(Update_Address,
                new IntentFilter("updateaddress"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(cartupdate,
                new IntentFilter("cartupdate"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(cartupdate1,
                new IntentFilter("updatecart"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(cartproducts,
                new IntentFilter("cartproducts"));



//        listview_cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                view.setSelected(true);
//
//
//            }
//        });
//


        listview_addcart_list=(ListView)view.findViewById(R.id.listview_carts_list);
        RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.relative_dyanamicrow_cu);
        ScrollView sv = (ScrollView)view.findViewById(R.id.scrollView_cart_cu);
        ll = (LinearLayout)view.findViewById(R.id.linear_dyanamicrow_cu);
        text_cart_subtotal = (TextView)view.findViewById(R.id.text_cart_subtotal_cu);
        text_cart_buy = (TextView)view.findViewById(R.id.text_cart_buy_cu);

        text_result_cust_cartsitems = (TextView) view.findViewById(R.id.text_result_cust_cartsitems12);
        text_result_cust_cartsitems.setVisibility(View.INVISIBLE);

        text_cart_subtotal.setText("");
        text_cart_subtotal.setVisibility(View.VISIBLE);
        text_cart_buy.setVisibility(View.INVISIBLE);
//        text_cart_buy.setTextColor(getResources().getColor(R.color.white));
//        GradientDrawable bgShape1 = (GradientDrawable)text_cart_buy.getBackground();
//        bgShape1.mutate();
//        bgShape1.setColor(getResources().getColor(R.color.color_green));
//        bgShape1.setStroke(text_cart_buy.getWidth(),getResources().getColor(R.color.color_green));

//        LocalBroadcastManager.getInstance(CartCustomerActivity.this).registerReceiver(cartupdate,
//                new IntentFilter("cartupdate"));




      //  LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        inflater1 = inflater;

        array_addcart = new JSONArray();

        listview_adaptercart = new ListviewAdaptercart();
        listview_addcart_list.setAdapter(listview_adaptercart);
        listview_addcart_list.setSmoothScrollbarEnabled(true);

        new Communication_addcart().execute();

        text_cart_buy.setOnTouchListener(new View.OnTouchListener()
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
                    if (netInfo != null && netInfo.isConnectedOrConnecting())
                    {


//                        if (float_totalvalues >= 2000 && float_totalvalues <= 10000)
//                        {
//
                            LayoutInflater inflater = getActivity().getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.custom_textview_popup, null);
                            TextView textview_title = alertLayout.findViewById(R.id.textview_alert_invite_title);
                            TextView textview_title_dialog = alertLayout.findViewById(R.id.textview_alert_invite_message);
                            final EditText textview_email_No_enter = alertLayout.findViewById(R.id.textview_emailtext);
                            final TextView textview_canced = alertLayout.findViewById(R.id.textview_alert_msg_cancel);
                            final TextView textview_ok = alertLayout.findViewById(R.id.textview_alert_msg_ok);

                            RelativeLayout backround_view = alertLayout.findViewById(R.id.cutom_dilaog_invited2);

                            textview_title.setText("Verify Pincode");
                            textview_title_dialog.setText("Please verify your correct delivery pincode address");
                            textview_email_No_enter.setHint("Enter pincode");
                            textview_email_No_enter.setInputType(InputType.TYPE_CLASS_NUMBER);
                            textview_email_No_enter.setTextColor(getResources().getColor(R.color.signupColor));


                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            // this is set the view from XML inside AlertDialog
                            alert.setView(alertLayout);
                            // disallow cancel of AlertDialog on click of back button and outside touch
                            alert.setCancelable(false);
                            final AlertDialog dialog1 = alert.create();
                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog1.show();


                            GradientDrawable bgShape = (GradientDrawable) backround_view.getBackground();
                            bgShape.mutate();
                            bgShape.setColor(Color.WHITE);

                            GradientDrawable bgShape1 = (GradientDrawable) textview_email_No_enter.getBackground();
                            bgShape1.mutate();
                            bgShape1.setColor(Color.WHITE);

                            textview_ok.setText("Verify");
                            textview_ok.setEnabled(true);
                            textview_ok.setTextColor(getResources().getColor(R.color.colordarkblue));
                            textview_email_No_enter.setText(pref.getString("pincode", ""));

                            textview_email_No_enter.setEnabled(false);

                            textview_email_No_enter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    String emailValues = String.valueOf(textview_email_No_enter.getText());
                                    if (emailValues.length() >= 10) {
                                        textview_ok.setEnabled(true);
                                        textview_ok.setTextColor(getResources().getColor(R.color.colorPrimary));

                                    } else {
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
                                public void onClick(View view) {


                                    str_pincode = "";
                                    dialog1.dismiss();

                                }
                            });

                            textview_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                                        str_pincode = textview_email_No_enter.getText().toString();
                                        //   str_emailAdd="";
                                        dialog1.dismiss();

                                        progressDialog = new ProgressDialog(getActivity());
                                        progressDialog.setMessage("Loading..."); // Setting Message
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                        progressDialog.show(); // Display Progress Dialog
                                        progressDialog.setCancelable(false);

                                        new Communication_verifypincode().execute();
                                    } else {
                                        Connections();
                                    }

                                }
                            });
//
//
//                        }
//                        else
//                         {
//
//                            if (float_totalvalues <= 2000) {
//
//                                AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
//                                builder4.setTitle("Order Limit!");
//                                builder4.setMessage("Your cart items should be minimum order ₹2000/-");
//                                builder4.setCancelable(false);
//                                builder4.setPositiveButton("Ok",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//
//
//                                            }
//                                        });
//                                alert11 = builder4.create();
//                                alert11.show();
//                            }
//                            else if (float_totalvalues >= 10000)
//                            {
//                                AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
//                                builder4.setTitle("Order Limit!");
//                                builder4.setMessage("Your cart items should be maximum order ₹10000/-");
//                                builder4.setCancelable(false);
//                                builder4.setPositiveButton("Ok",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//
//
//                                            }
//                                        });
//                                alert11 = builder4.create();
//                                alert11.show();
//
//                            }
//
//                        }

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
//        text_cart_buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Float.parseFloat(str_subtotal)<= 2000)
//                {
//                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
//                    builder4.setTitle("Order Limit!");
//                    builder4.setMessage("Your cart items should be minimum order ₹2000/-");
//                    builder4.setCancelable(false);
//                    builder4.setPositiveButton("Ok",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
//
//
//                                }
//                            });
//                    alert11 = builder4.create();
//                    alert11.show();
//                }
//               else
//               {
//
//                LayoutInflater inflater = getActivity().getLayoutInflater();
//                View alertLayout = inflater.inflate(R.layout.custom_textview_popup, null);
//                TextView textview_title = alertLayout.findViewById(R.id.textview_alert_invite_title);
//                TextView textview_title_dialog = alertLayout.findViewById(R.id.textview_alert_invite_message);
//                final EditText textview_email_No_enter = alertLayout.findViewById(R.id.textview_emailtext);
//                final TextView textview_canced = alertLayout.findViewById(R.id.textview_alert_msg_cancel);
//                final TextView textview_ok = alertLayout.findViewById(R.id.textview_alert_msg_ok);
//
//                RelativeLayout backround_view = alertLayout.findViewById(R.id.cutom_dilaog_invited2);
//
//                textview_title.setText("Verify Pincode");
//                textview_title_dialog.setText("Please verify your correct delivery pincode address");
//                textview_email_No_enter.setHint("Enter pincode");
//                textview_email_No_enter.setInputType(InputType.TYPE_CLASS_NUMBER);
//                textview_email_No_enter.setTextColor(getResources().getColor(R.color.signupColor));
//
//
//                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                // this is set the view from XML inside AlertDialog
//                alert.setView(alertLayout);
//                // disallow cancel of AlertDialog on click of back button and outside touch
//                alert.setCancelable(false);
//                final AlertDialog dialog1 = alert.create();
//                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog1.show();
//
//
//                GradientDrawable bgShape = (GradientDrawable) backround_view.getBackground();
//                bgShape.mutate();
//                bgShape.setColor(Color.WHITE);
//
//                GradientDrawable bgShape1 = (GradientDrawable) textview_email_No_enter.getBackground();
//                bgShape1.mutate();
//                bgShape1.setColor(Color.WHITE);
//
//                textview_ok.setText("Verify");
//                textview_ok.setEnabled(true);
//                textview_ok.setTextColor(getResources().getColor(R.color.colordarkblue));
//                textview_email_No_enter.setText(pref.getString("pincode", ""));
//
//                textview_email_No_enter.setEnabled(false);
//
//                textview_email_No_enter.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                        String emailValues = String.valueOf(textview_email_No_enter.getText());
//                        if (emailValues.length() >= 10) {
//                            textview_ok.setEnabled(true);
//                            textview_ok.setTextColor(getResources().getColor(R.color.colorPrimary));
//
//                        } else {
//                            textview_ok.setEnabled(false);
//                            textview_ok.setTextColor(getResources().getColor(R.color.colorlightgray));
//
//                        }
//
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//
//                });
//
//
//                textview_canced.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                        str_pincode = "";
//                        dialog1.dismiss();
//
//                    }
//                });
//
//                textview_ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        str_pincode = textview_email_No_enter.getText().toString();
//                        //   str_emailAdd="";
//                        dialog1.dismiss();
//
//                        progressDialog = new ProgressDialog(getActivity());
//                        progressDialog.setMessage("Loading..."); // Setting Message
//                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                        progressDialog.show(); // Display Progress Dialog
//                        progressDialog.setCancelable(false);
//
//                        new Communication_verifypincode().execute();
//
//                    }
//                });
//
//
//                if (pref.getString("address", "").equalsIgnoreCase("yes")) {
//                    //  ReviewOrderActivity.Array_cartArray = array_addcart;
////                    Intent intens = new Intent(CartCustomerActivity.this,ReviewOrderActivity.class);
////                    startActivity(intens);
//                } else {
////                    MyAddressActivity.str_selectactivity = "no";
////                    Intent intens = new Intent(CartCustomerActivity.this,MyAddressActivity.class);
////                    startActivity(intens);
//                }
//
//            }
//        }
//        });
        return view;
    }

    public class Communication_addcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.getaddcart);

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

            if(progressDialog !=null)
            {
                progressDialog.dismiss();
            }
            progress_customer_cart.setVisibility(View.INVISIBLE);
            text_result_cust_cartsitems.setVisibility(View.INVISIBLE);
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {
                    if (progressDialog !=null)
                    {
                        progressDialog.dismiss();
                    }


                } else if (result.equalsIgnoreCase("error"))

                {

                    if (progressDialog !=null)
                    {
                        progressDialog.dismiss();
                    }
                }
                else if (result.equalsIgnoreCase("nodata"))
                {

                    array_addcart = new JSONArray();
                    ll.removeAllViews();

                    text_cart_subtotal.setText("");
                    text_cart_buy.setVisibility(View.INVISIBLE);
                    text_result_cust_cartsitems.setVisibility(View.VISIBLE);
                    editor.putString("budge","0");
                    editor.commit();
                    Intent intent1 = new Intent("updatebuge");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent1);
                    if (progressDialog !=null)
                    {
                        progressDialog.dismiss();
                    }
                }
                else
                {
                            if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_addcart = new JSONArray(result);
                            if (array_addcart !=null) {

                                SingletonObject.Instance().setArray_Addcart(array_addcart);
                               // addRow();
                                listview_adaptercart.notifyDataSetChanged();
                                   text_cart_subtotal.setVisibility(View.VISIBLE);
                                text_cart_buy.setVisibility(View.VISIBLE);

                                editor.putString("budge", String.valueOf(array_addcart.length()));
                                editor.commit();
                                Intent intent1 = new Intent("updatebuge");
                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent1);
                                float totals = 0;
                                for (int i =0 ; i<array_addcart.length();i++)
                                {
                                    try {
                                        JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(i)));
                                        totals += Float.parseFloat(obj_val.getString("subtotal"));
                                        text_cart_subtotal.setText("Subtotal( " + array_addcart.length() + " items ): ₹" + String.valueOf(Math.round(totals)));
                                        float_totalvalues = totals;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (progressDialog !=null)
                                {
                                    progressDialog.dismiss();
                                }

                            }
                            else
                            {
                                if (progressDialog !=null)
                                {
                                    progressDialog.dismiss();
                                }
                            }

                            if (array_addcart.length() == 0)
                            {
                                ll.removeAllViews();
                                text_cart_subtotal.setText("");
                                text_cart_buy.setVisibility(View.INVISIBLE);
                                if (progressDialog !=null)
                                {
                                    progressDialog.dismiss();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        if (progressDialog !=null)
                        {
                            progressDialog.dismiss();
                        }

                    }
                }
                if (progressDialog !=null)
                {
                    progressDialog.dismiss();
                }
            }
            else
            {

                if (progressDialog !=null)
                {
                    progressDialog.dismiss();
                }
            }

        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            this.cancel(true);

        }
    }

    public class Communication_deletecart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.deleteaddcart);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("productcode", str_productcode);
                postDataParams.put("custid",str_custid);


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


            if (result !=null)
            {
                progressDialog.dismiss();
                if (result.equalsIgnoreCase("nullerror"))

                {

                    progressDialog.dismiss();

                } else if (result.equalsIgnoreCase("error"))
                {

                    progressDialog.dismiss();
                }
                else if (result.equalsIgnoreCase("deleted"))
                {


                   // SingletonObject.Instance().setstr_updatehomecart("yes");
                    array_addcart.remove(int_tag_val);

                    Intent intent1 = new Intent("updatecarts");
                    intent1.putExtra("productcode", str_productcode);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent1);
                    listview_adaptercart.notifyDataSetChanged();
                    editor.putString("budge", String.valueOf(array_addcart.length()));
                    editor.commit();
                    Intent intent2 = new Intent("updatebuge");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent2);
//                    new Communication_addcart().execute();
                    if (array_addcart.length() == 0)
                    {

                        SingletonObject.Instance().setstr_count("0");
                        array_addcart = new JSONArray();
                        ll.removeAllViews();
                        text_result_cust_cartsitems.setVisibility(View.VISIBLE);
                        text_cart_subtotal.setText("");
                        text_cart_buy.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        //ll.removeViewAt(int_tag_val);
                        text_cart_buy.setVisibility(View.VISIBLE);
                        SingletonObject.Instance().setstr_count(String.valueOf(array_addcart.length()));
                    }


                    float totals = 0;
                    for (int i =0 ; i<array_addcart.length();i++)
                    {
                        try {
                            JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(i)));
                            totals += Float.parseFloat(obj_val.getString("subtotal"));
                            text_cart_subtotal.setText("Subtotal( " + array_addcart.length() + " items ): ₹" + String.valueOf(Math.round(totals)));
                            float_totalvalues = totals;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                }
                else
                {
                    progressDialog.dismiss();
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
    public class Communication_updateaddcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.updateaddcart);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("custid", str_custid);
                postDataParams.put("purch",str_purch);
                postDataParams.put("subtotal",str_subtotal);
                postDataParams.put("productcode",str_productcode);



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


            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {
                    progressDialog.dismiss();
                    //addRow();
                    listview_adaptercart.notifyDataSetChanged();

                }
                else if (result.equalsIgnoreCase("error"))
                {

                    progressDialog.dismiss();
                   // addRow();
                    listview_adaptercart.notifyDataSetChanged();
                }
                else if (result.equalsIgnoreCase("updated"))
                {

                    JSONObject objectval = null;
                    try {
                        objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject(int_arrayindex)));
                        objectval.put("purch", str_purch);
                        objectval.put("subtotal", str_subtotal);
                        array_addcart.put(int_arrayindex, objectval);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    float totals = 0;
                    for (int i =0 ; i<array_addcart.length();i++)
                    {
                        try {
                            JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(i)));
                            totals += Float.parseFloat(obj_val.getString("subtotal"));
                            text_cart_subtotal.setText("Subtotal( " + array_addcart.length() + " items ): ₹" + String.valueOf(Math.round(totals)));
                            float_totalvalues = totals;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    listview_adaptercart.notifyDataSetChanged();
                   // new Communication_addcart().execute();
                    progressDialog.dismiss();


                }
                else
                {
                    progressDialog.dismiss();
                }
            }
            else
            {

                progressDialog.dismiss();
            }

        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            this.cancel(true);

        }
    }
    public class Communication_verifypincode extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.verifypincode);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("pincode",str_pincode);
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


            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))
                {
                    progressDialog.dismiss();

                } else if (result.equalsIgnoreCase("error"))
                {

                    progressDialog.dismiss();
                }
                else if (result.equalsIgnoreCase("match"))
                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Verifyed!");
                    builder4.setMessage("Your pincode has been match delivery area");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                            Matchpincode();

                                }
                            });
                    alert11 = builder4.create();
                    alert11.show();
                    progressDialog.dismiss();
                }
                else if (result.equalsIgnoreCase("unmatch"))
                {

                    progressDialog.dismiss();
                   Unmatchpincode();

                }
                else if (result.equalsIgnoreCase("invalid"))
                {
                    progressDialog.dismiss();

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Invalid Pincode");
                    builder4.setMessage("You have been enter wrong pincode,Please check register pincode");
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
                }
                else
                {
                    progressDialog.dismiss();

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("opps!");
                    builder4.setMessage("Server has not responding,please try again!");
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
                }
            }
            else
            {
                progressDialog.dismiss();
                AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                builder4.setTitle("Opps!");
                builder4.setMessage("Server has not responding,please try again!");
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
    public  void  addRow()
    {

        float totals = 0;
        ll.removeAllViews();
       for (int i = 0; i < array_addcart.length(); i++)
       {
           final View view1 = inflater1.inflate(R.layout.list_cart_items, null);

          ImageView imageView = (ImageView) view1.findViewById(R.id.image_product_cart);
           TextView text_offer_per_cart = (TextView) view1.findViewById(R.id.text_offer_per_cart);
            TextView text_productname_cart = (TextView) view1.findViewById(R.id.text_productname_cart);
            TextView text_mgfname_cart = (TextView) view1.findViewById(R.id.text_mgfname_cart);
            TextView text_mrp_price_cart = (TextView) view1.findViewById(R.id.text_mrp_price_cart);
            TextView text_salerate_price_cart = (TextView) view1.findViewById(R.id.text_salerate_price_cart);
           TextView text_salerate_price_cart2 = (TextView) view1.findViewById(R.id.text_salerate_price_cart2);
           TextView text_salerate_price_cart3 = (TextView) view1.findViewById(R.id.text_salerate_price_cart3);
            TextView text_packingdate_cart = (TextView) view1.findViewById(R.id.text_packingdate_cart);
            TextView text_expdate_cart = (TextView) view1.findViewById(R.id.text_expdate_cart);
            TextView text_cart_minus = (TextView) view1.findViewById(R.id.text_cart_minus);
            TextView text_cart_qtytitle = (TextView) view1.findViewById(R.id.text_cart_qtytitle);
            TextView text_cart_plus = (TextView) view1.findViewById(R.id.text_cart_plus);
           TextView text_offer_note = (TextView) view1.findViewById(R.id.text_offer_note_cart);

            TextView text_home_addtocart = (TextView) view1.findViewById(R.id.text_home_addtocart1);


           text_home_addtocart.setTag(i);
            text_cart_minus.setTag(i);
            text_cart_plus.setTag(i);
           imageView.setTag(i);
            try {
                JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(i)));

                text_cart_qtytitle.setText(obj_val.getString("purch"));
                text_productname_cart.setText(obj_val.getString("productname"));
                text_mgfname_cart.setText(obj_val.getString("mfgcmp"));
                text_mrp_price_cart.setText("₹"+obj_val.getString("mrp"));
                text_salerate_price_cart.setText( "₹"+obj_val.getString("netsale"));

                text_expdate_cart.setText("₹"+obj_val.getString("subtotal"));
                String str_imageurl = obj_val.getString("imageurl");

                if (str_imageurl.length() ==0)
                {
                    str_imageurl ="http://www.sachinmokashi";
                }
                totals +=Float.parseFloat(obj_val.getString("subtotal"));

                str_subtotal = String.valueOf(totals);

                if (String.valueOf(obj_val.getString("offers")).equalsIgnoreCase("yes"))
                {

                    text_packingdate_cart.setText(obj_val.getString("purch") + " x " +"₹"+ obj_val.getString("netsale1"));
                    text_salerate_price_cart.setTextColor(getResources().getColor(R.color.colorlightgray));
                    text_salerate_price_cart2.setTextColor(getResources().getColor(R.color.colorlightgray));

                    text_offer_per_cart.setVisibility(View.VISIBLE);
                    text_salerate_price_cart.setVisibility(View.VISIBLE);
                    text_salerate_price_cart2.setVisibility(View.VISIBLE);
                    text_salerate_price_cart3.setVisibility(View.VISIBLE);

                  text_salerate_price_cart2.setText( "₹"+obj_val.getString("netsale"));
                    text_salerate_price_cart3.setText( "₹"+obj_val.getString("netsale1"));

                    text_offer_per_cart.setText(obj_val.getString("offersper")+"%");
                    text_offer_note.setText(String.valueOf(obj_val.getString("note")));
                    text_offer_note.setVisibility(View.GONE);
                    if (String.valueOf(obj_val.getString("note")).length() != 0)
                    {
                        text_offer_note.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    text_packingdate_cart.setText(obj_val.getString("purch") + " x " +"₹"+ obj_val.getString("netsale"));
                    text_salerate_price_cart.setTextColor(getResources().getColor(R.color.colorgreen1));
                    text_salerate_price_cart2.setTextColor(getResources().getColor(R.color.colorlightgray));
                    text_offer_per_cart.setVisibility(View.GONE);
                    text_salerate_price_cart.setVisibility(View.VISIBLE);
                    text_salerate_price_cart2.setVisibility(View.GONE);
                    text_salerate_price_cart3.setVisibility(View.GONE);
                    text_offer_note.setVisibility(View.GONE);
                    text_salerate_price_cart2.setText("");
                    text_salerate_price_cart3.setText("");
                    text_offer_per_cart.setText("");
                    text_offer_note.setText("");
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
               String str_msg1 = "Subtotal( "+array_addcart.length()+" items ): ";
                String str_msg2 = "₹ "+String.valueOf(Math.round(totals));
                text_cart_subtotal.setText(Html.fromHtml( " <font color=#ffffff>  "+str_msg1+ " </font> <font color=#7ccf00> <b> "+str_msg2+ " </b> </font>"));




                text_cart_minus.setOnTouchListener(new View.OnTouchListener()
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
                                    JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));

                                    Integer value = Integer.parseInt(objectval.getString("purch"));
                                    if (value > 1) {
                                        value--;
                                        float totalval;
                                        if (String.valueOf(objectval.getString("offers")).equalsIgnoreCase("yes"))
                                        {
                                            totalval = value * Float.parseFloat(objectval.getString("netsale1"));
                                        }
                                        else
                                        {
                                            totalval = value * Float.parseFloat(objectval.getString("netsale"));
                                        }

                                        str_subtotal = String.valueOf(totalval);
                                        str_custid = objectval.getString("custid");
                                        str_productcode = objectval.getString("productcode");
                                        str_purch = String.valueOf(value);

                                        objectval.put("purch", value);
                                        objectval.put("subtotal", String.valueOf(totalval));
                                        array_addcart.put((Integer) v.getTag(), objectval);
                                        //addRow();

                                        progressDialog = new ProgressDialog(getActivity());
                                        progressDialog.setMessage("Update cart..."); // Setting Message
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog.show(); // Display Progress Dialog
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        new Communication_updateaddcart().execute();

                                    }

                                } catch (JSONException ee) {
                                    ee.printStackTrace();
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


//                text_cart_minus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        try {
//                            JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));
//
//                            Integer value = Integer.parseInt(objectval.getString("purch"));
//                            if (value > 1 )
//                            {
//                                value--;
//                                float totalval = value * Float.parseFloat(objectval.getString("netsale"));
//
//                                str_subtotal = String.valueOf(totalval);
//                                str_custid = objectval.getString("custid");
//                                str_productcode= objectval.getString("productcode");
//                                str_purch = String.valueOf(value);
//
//                                objectval.put("purch",value);
//                                objectval.put("subtotal",String.valueOf(totalval));
//                                array_addcart.put((Integer) v.getTag(),objectval);
//                                //addRow();
//
//                                progressDialog = new ProgressDialog(getActivity());
//                                progressDialog.setMessage("Update cart..."); // Setting Message
//                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
//                                progressDialog.setCancelable(false);
//                                progressDialog.show();
//                                new Communication_updateaddcart().execute();
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
////
//
//                    }
//                });


                text_cart_plus.setOnTouchListener(new View.OnTouchListener()
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
                                    JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));

                                    Integer value = Integer.parseInt(objectval.getString("purch"));
                                    Integer value1 = Integer.parseInt(objectval.getString("stockinhand"));
                                    if (value >= value1)
                                    {
                                        AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                                        builder4.setTitle("Opps!");
                                        builder4.setMessage("Stock is not available!");
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
                                    else

                                    {
                                        if (value >= 1) {
                                        value++;

                                        float totalval;
                                        if (String.valueOf(objectval.getString("offers")).equalsIgnoreCase("yes")) {
                                            totalval = value * Float.parseFloat(objectval.getString("netsale1"));
                                        } else {
                                            totalval = value * Float.parseFloat(objectval.getString("netsale"));
                                        }
                                        str_subtotal = String.valueOf(totalval);
                                        str_custid = objectval.getString("custid");
                                        str_productcode = objectval.getString("productcode");
                                        str_purch = String.valueOf(value);


                                        objectval.put("purch", value);
                                        objectval.put("subtotal", String.valueOf(totalval));
                                        array_addcart.put((Integer) v.getTag(), objectval);
                                        //addRow();

                                        progressDialog = new ProgressDialog(getActivity());
                                        progressDialog.setMessage("Update cart..."); // Setting Message
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog.show(); // Display Progress Dialog
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        new Communication_updateaddcart().execute();

                                    }
                                }

                                } catch (JSONException ee) {
                                    ee.printStackTrace();
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

//                text_cart_plus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        try {
//                            JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));
//
//                            Integer value = Integer.parseInt(objectval.getString("purch"));
//                            if (value >= 1 )
//                            {
//                                value++;
//                                float totalval = value * Float.parseFloat(objectval.getString("netsale"));
//
//                                str_subtotal = String.valueOf(totalval);
//                                str_custid = objectval.getString("custid");
//                                str_productcode= objectval.getString("productcode");
//                                str_purch = String.valueOf(value);
//
//
//                                objectval.put("purch",value);
//                                objectval.put("subtotal",String.valueOf(totalval));
//                                array_addcart.put((Integer) v.getTag(),objectval);
//                                //addRow();
//
//                                progressDialog = new ProgressDialog(getActivity());
//                                progressDialog.setMessage("Update cart..."); // Setting Message
//                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
//                                progressDialog.setCancelable(false);
//                                progressDialog.show();
//                                new Communication_updateaddcart().execute();
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
////
//
//                    }
//                });



                Picasso.with(getActivity())
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
                    private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {


                        @Override
                        public boolean onDoubleTap(MotionEvent e)
                        {

                            Toast.makeText(getActivity(), "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                            return super.onDoubleTap(e);
                        }

                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {


                            ImageView image = (ImageView)v.findViewWithTag(v.getTag());
                            image.invalidate();
                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            ZoomImageActivity.drawableBitmap=bitmap;
                            Intent intent=new Intent(getActivity(),ZoomImageActivity.class);
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

                text_home_addtocart.setOnTouchListener(new View.OnTouchListener()
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

                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Delete cart..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                try {
                                    JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));

                                    str_custid = obj_val.getString("custid");
                                    str_productcode = obj_val.getString("productcode");
                                    int_tag_val = (Integer) v.getTag();
                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }

                                new Communication_deletecart().execute();
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


//
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        ImageView image = (ImageView)v.findViewWithTag(v.getTag());
//                        image.invalidate();
//                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//                        Bitmap bitmap = drawable.getBitmap();
//                        ZoomImageActivity.drawableBitmap=bitmap;
//                        Intent intent=new Intent(getActivity(),ZoomImageActivity.class);
//                        startActivity(intent);
//
//                    }
//                });


            } catch (JSONException e)
            {
                e.printStackTrace();
            }



//           text_home_addtocart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    progressDialog = new ProgressDialog(getActivity());
//                    progressDialog.setMessage("Delete cart..."); // Setting Message
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);                                             progressDialog.show(); // Display Progress Dialog
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//
//                    try {
//                        JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));
//
//                        str_custid = obj_val.getString("custid");
//                        str_productcode = obj_val.getString("productcode");
//                        int_tag_val = (Integer) v.getTag();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    new Communication_deletecart().execute();
//                }
//            });


            ll.addView(view1);
        }
    }

    public void Unmatchpincode()
    {
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_popup_valid_loc, null);

        final TextView textview_canced = alertLayout.findViewById(R.id.textview_alert_msg_cancel_loc);
        final TextView textview_ok = alertLayout.findViewById(R.id.textview_alert_msg_ok_loc);

        RelativeLayout backround_view = alertLayout.findViewById(R.id.cutom_dilaog_invited2_loc);



        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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



        textview_canced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                dialog1.dismiss();

            }
        });

        textview_ok.setOnTouchListener(new View.OnTouchListener()
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

                    dialog1.dismiss();
                    Intent intents = new Intent(getActivity(),ValidPincodeActivity.class);
                    startActivity(intents);

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

//        textview_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//
//                dialog1.dismiss();
//                Intent intents = new Intent(getActivity(),ValidPincodeActivity.class);
//                startActivity(intents);
//
//            }
//        });
    }
    public void Matchpincode()
    {
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_popup_address, null);

       text_select_add_div= (TextView) alertLayout.findViewById(R.id.text_select_add_div);
         text_add_div_name= (TextView) alertLayout.findViewById(R.id.text_add_div_name);
         text_add_div_addres1= (TextView) alertLayout.findViewById(R.id.text_add_div_addres1);

         text_select_add_div2= (TextView) alertLayout.findViewById(R.id.text_select_add_div2);
         text_add_div_name2= (TextView) alertLayout.findViewById(R.id.text_add_div_name2);
         text_add_div_addres12= (TextView) alertLayout.findViewById(R.id.text_add_div_addres12);


         relative_address2_addm= (RelativeLayout) alertLayout.findViewById(R.id.relative_address2_addm);
         text_address2_addaddress= (TextView) alertLayout.findViewById(R.id.text_address2_addaddress);
         relative_address_address2_text= (RelativeLayout) alertLayout.findViewById(R.id.relative_address_address2_text);


        TextView text_editdeliverytoadd= (TextView) alertLayout.findViewById(R.id.text_editdeliverytoadd);

        TextView text_deliverytoadd= (TextView) alertLayout.findViewById(R.id.text_deliverytoadd);

        TextView text_canceldeliverytoadd= (TextView) alertLayout.findViewById(R.id.text_canceldeliverytoadd);

        RelativeLayout backround_view = alertLayout.findViewById(R.id.cutom_dilaog_address);



        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        final AlertDialog dialog1 = alert.create();
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();


        text_add_div_name.setText(pref.getString("custname1",""));
        text_add_div_name2.setText(pref.getString("custname2",""));

        final String str_add1 = pref.getString("address1","") +"\n"+"Phone No: "+pref.getString("mobile1","")+"\n"+"Pincode: "+pref.getString("pincode1","");
        final String str_add2 = pref.getString("address2","") +"\n"+"Phone No: "+pref.getString("mobile2","")+"\n"+"Pincode: "+pref.getString("pincode2","");

        text_add_div_addres1.setText(str_add1);
        text_add_div_addres12.setText(str_add2);

        ReviewOrderActivity.Str_address = pref.getString("custname","")+"\n"+ str_add1;

        if (pref.getString("address2","").length() == 0)
        {
            relative_address2_addm.setVisibility(View.VISIBLE);
            relative_address_address2_text.setVisibility(View.GONE);
        }
        else
        {
            relative_address2_addm.setVisibility(View.GONE);
            relative_address_address2_text.setVisibility(View.VISIBLE);
        }

        text_select_add_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                str_selectadd="address1";
                text_select_add_div.setBackgroundResource(R.drawable.checkadd);
                text_select_add_div2.setBackgroundResource(R.drawable.uncheckadd);
                ReviewOrderActivity.Str_address = pref.getString("custname","") +"\n"+ str_add1;

            }
        });

        text_select_add_div2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                str_selectadd="address2";
                text_select_add_div.setBackgroundResource(R.drawable.uncheckadd);
                text_select_add_div2.setBackgroundResource(R.drawable.checkadd);
                ReviewOrderActivity.Str_address = pref.getString("custname","") +"\n"+ str_add2;
            }
        });



        text_canceldeliverytoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                dialog1.dismiss();

            }
        });
        text_deliverytoadd.setOnTouchListener(new View.OnTouchListener()
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

                    dialog1.dismiss();


                            Intent intents = new Intent(getActivity(),ReviewOrderActivity.class);
                            startActivity(intents);





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

//        text_deliverytoadd.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view)
//        {
//
//            dialog1.dismiss();
//                Intent intents = new Intent(getActivity(),ReviewOrderActivity.class);
//                startActivity(intents);
//
//        }
//    });

//        text_editdeliverytoadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//
//                AddresschangeActivity.str_select_addtype=str_selectadd;
//                Intent intents = new Intent(getActivity(),AddresschangeActivity.class);
//                startActivity(intents);
//
//            }
//        });

        text_editdeliverytoadd.setOnTouchListener(new View.OnTouchListener()
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

                    AddresschangeActivity.str_select_addtype=str_selectadd;
                    Intent intents = new Intent(getActivity(),AddresschangeActivity.class);
                    startActivity(intents);

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


        text_address2_addaddress.setOnTouchListener(new View.OnTouchListener()
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

                    text_select_add_div.setBackgroundResource(R.drawable.uncheckadd);
                    text_select_add_div2.setBackgroundResource(R.drawable.checkadd);
                    str_selectadd ="address2";
                    AddresschangeActivity.str_select_addtype="address2";
                    Intent intents = new Intent(getActivity(),AddresschangeActivity.class);
                    startActivity(intents);

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

//        text_address2_addaddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//
//                text_select_add_div.setBackgroundResource(R.drawable.uncheckadd);
//                text_select_add_div2.setBackgroundResource(R.drawable.checkadd);
//                str_selectadd ="address2";
//                AddresschangeActivity.str_select_addtype="address2";
//                Intent intents = new Intent(getActivity(),AddresschangeActivity.class);
//                startActivity(intents);
//
//            }
//        });


    }
    public void onBackPressed() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    private BroadcastReceiver Update_Address = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if ( str_selectadd.equalsIgnoreCase("address1"))
            {
                text_add_div_addres1.setText(str_address);
                text_add_div_name.setText(str_stringname);
                text_select_add_div.setBackgroundResource(R.drawable.checkadd);
                text_select_add_div2.setBackgroundResource(R.drawable.uncheckadd);
                ReviewOrderActivity.Str_address = (str_stringname+ "\n" + str_address);

            }
            if ( str_selectadd.equalsIgnoreCase("address2"))
            {
                if (str_address.length() == 0)
                {
                    relative_address2_addm.setVisibility(View.VISIBLE);
                    relative_address_address2_text.setVisibility(View.GONE);
                }
                else
                {
                    relative_address2_addm.setVisibility(View.GONE);
                    relative_address_address2_text.setVisibility(View.VISIBLE);
                    text_select_add_div.setBackgroundResource(R.drawable.uncheckadd);
                    text_select_add_div2.setBackgroundResource(R.drawable.checkadd);
                }
                text_add_div_addres12.setText(str_address);
                text_add_div_name2.setText(str_stringname);
                ReviewOrderActivity.Str_address = (str_stringname+ "\n" + str_address);


            }


        }
    };
    private BroadcastReceiver cartproducts = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Refresh cart..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Communication_addcart().execute();


        }
    };

    private BroadcastReceiver cartupdate = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            array_addcart = new JSONArray();
            SingletonObject.Instance().setArray_Addcart(array_addcart);
            ll.removeAllViews();
            text_cart_buy.setVisibility(View.INVISIBLE);
            text_cart_subtotal.setText("");
            text_cart_subtotal.setVisibility(View.INVISIBLE);
            text_result_cust_cartsitems.setVisibility(View.VISIBLE);
            editor.putString("budge","0");
            editor.commit();

            Intent intent1 = new Intent("updatebuge");
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent1);
            listview_adaptercart.notifyDataSetChanged();

        }
    };
    private BroadcastReceiver cartupdate1 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (array_addcart.length() ==0)
            {
             progress_customer_cart.setVisibility(View.VISIBLE);
            }
            listview_adaptercart.notifyDataSetChanged();
          new Communication_addcart().execute();


        }
    };
    @Override
    public void onDetach() {
        super.onDetach();

    }
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


    /////////////////////////////


    private class ListviewAdaptercart extends BaseAdapter
    {


        @Override
        public int getCount()
        {
            Log.d("getCount","getCount");
            return array_addcart.length();
        }

        @Override
        public Object getItem(int i) {
            Log.d("getItem","getItem");
            return null;
        }

        @Override
        public long getItemId(int i) {

            Log.d("getitemid","getitemid");

            return 0;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ListviewAdaptercart.ViewHolder viewHolder;
//            float totals = 0;
            if(view == null)
            {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.list_cart_items, null);

                viewHolder = new ListviewAdaptercart.ViewHolder();

                viewHolder.imageView = (ImageView) view.findViewById(R.id.image_product_cart);
                viewHolder.text_offer_per_cart = (TextView) view.findViewById(R.id.text_offer_per_cart);
                viewHolder.text_productname_cart = (TextView) view.findViewById(R.id.text_productname_cart);
                viewHolder.text_mgfname_cart = (TextView) view.findViewById(R.id.text_mgfname_cart);
                viewHolder.text_mrp_price_cart = (TextView) view.findViewById(R.id.text_mrp_price_cart);
                viewHolder.text_salerate_price_cart = (TextView) view.findViewById(R.id.text_salerate_price_cart);
                viewHolder.text_salerate_price_cart2 = (TextView) view.findViewById(R.id.text_salerate_price_cart2);
                viewHolder.text_salerate_price_cart3 = (TextView) view.findViewById(R.id.text_salerate_price_cart3);
                viewHolder.text_packingdate_cart = (TextView) view.findViewById(R.id.text_packingdate_cart);
                viewHolder.text_expdate_cart = (TextView) view.findViewById(R.id.text_expdate_cart);
                viewHolder.text_cart_minus = (TextView) view.findViewById(R.id.text_cart_minus);
                viewHolder.text_cart_qtytitle = (TextView) view.findViewById(R.id.text_cart_qtytitle);
                viewHolder.text_cart_plus = (TextView) view.findViewById(R.id.text_cart_plus);
                viewHolder.text_offer_note = (TextView) view.findViewById(R.id.text_offer_note_cart);
                viewHolder.text_home_addtocart = (TextView) view.findViewById(R.id.text_home_addtocart1);

                view.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ListviewAdaptercart.ViewHolder)view.getTag();
            }

            viewHolder.text_home_addtocart.setTag(i);
            viewHolder.text_cart_minus.setTag(i);
            viewHolder.text_cart_plus.setTag(i);
            viewHolder.imageView.setTag(i);

            final JSONObject object_values;

            try {
                JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject(i)));

                viewHolder.text_cart_qtytitle.setText(obj_val.getString("purch"));
                viewHolder.text_productname_cart.setText(obj_val.getString("productname"));
                viewHolder.text_mgfname_cart.setText(obj_val.getString("mfgcmp"));
                viewHolder.text_mrp_price_cart.setText("₹"+obj_val.getString("mrp"));
                viewHolder.text_salerate_price_cart.setText( "₹"+obj_val.getString("netsale"));

                viewHolder.text_expdate_cart.setText("₹"+obj_val.getString("subtotal"));
                String str_imageurl = obj_val.getString("imageurl");

                if (str_imageurl.length() ==0)
                {
                    str_imageurl ="http://www.sachinmokashi";
                }
                //totals +=Float.parseFloat(obj_val.getString("subtotal"));

               // str_subtotal = String.valueOf(totals);

                if (String.valueOf(obj_val.getString("offers")).equalsIgnoreCase("yes"))
                {

                    viewHolder.text_packingdate_cart.setText(obj_val.getString("purch") + " x " +"₹"+ obj_val.getString("netsale1"));
                    viewHolder.text_salerate_price_cart.setTextColor(getResources().getColor(R.color.colorlightgray));
                    viewHolder.text_salerate_price_cart2.setTextColor(getResources().getColor(R.color.colorlightgray));

                    viewHolder.text_offer_per_cart.setVisibility(View.VISIBLE);
                    viewHolder.text_salerate_price_cart.setVisibility(View.VISIBLE);
                    viewHolder.text_salerate_price_cart2.setVisibility(View.VISIBLE);
                    viewHolder.text_salerate_price_cart3.setVisibility(View.VISIBLE);

                    viewHolder.text_salerate_price_cart2.setText( "₹"+obj_val.getString("netsale"));
                    viewHolder.text_salerate_price_cart3.setText( "₹"+obj_val.getString("netsale1"));

                    viewHolder.text_offer_per_cart.setText(obj_val.getString("offersper")+"%");
                    viewHolder.text_offer_note.setText(String.valueOf(obj_val.getString("note")));
                    viewHolder.text_offer_note.setVisibility(View.GONE);
                    if (String.valueOf(obj_val.getString("note")).length() != 0)
                    {
                        viewHolder.text_offer_note.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    viewHolder.text_packingdate_cart.setText(obj_val.getString("purch") + " x " +"₹"+ obj_val.getString("netsale"));
                    viewHolder.text_salerate_price_cart.setTextColor(getResources().getColor(R.color.colorgreen1));
                    viewHolder.text_salerate_price_cart2.setTextColor(getResources().getColor(R.color.colorlightgray));
                    viewHolder.text_offer_per_cart.setVisibility(View.GONE);
                    viewHolder.text_salerate_price_cart.setVisibility(View.VISIBLE);
                    viewHolder.text_salerate_price_cart2.setVisibility(View.GONE);
                    viewHolder.text_salerate_price_cart3.setVisibility(View.GONE);
                    viewHolder.text_offer_note.setVisibility(View.GONE);
                    viewHolder.text_salerate_price_cart2.setText("");
                    viewHolder.text_salerate_price_cart3.setText("");
                    viewHolder.text_offer_per_cart.setText("");
                    viewHolder.text_offer_note.setText("");
                }

                if (String.valueOf(obj_val.getString("mfgcmp")).length() == 0)
                {
                    viewHolder.text_mgfname_cart.setText(String.valueOf(obj_val.getString("mfgcmp")));
                }
                else
                {
                    if (String.valueOf(obj_val.getString("mfgcmp")).length() >=8)
                    {
                        viewHolder.text_mgfname_cart.setText(String.valueOf(obj_val.getString("mfgcmp")).substring(0,7));

                    }
                    else
                    {
                        viewHolder.text_mgfname_cart.setText(String.valueOf(obj_val.getString("mfgcmp")).substring(0,String.valueOf(obj_val.getString("mfgcmp")).length()-1));
                    }

                }
             //   String str_msg1 = "Subtotal( "+array_addcart.length()+" items ): ";
               // String str_msg2 = "₹ "+String.valueOf(Math.round(totals));
              //  text_cart_subtotal.setText(Html.fromHtml( " <font color=#ffffff>  "+str_msg1+ " </font> <font color=#7ccf00> <b> "+str_msg2+ " </b> </font>"));




                viewHolder.text_cart_minus.setOnTouchListener(new View.OnTouchListener()
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
                                    JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));

                                    Integer value = Integer.parseInt(objectval.getString("purch"));
                                    if (value > 1) {
                                        value--;
                                        float totalval;
                                        if (String.valueOf(objectval.getString("offers")).equalsIgnoreCase("yes"))
                                        {
                                            totalval = value * Float.parseFloat(objectval.getString("netsale1"));
                                        }
                                        else
                                        {
                                            totalval = value * Float.parseFloat(objectval.getString("netsale"));
                                        }

                                        str_subtotal = String.valueOf(totalval);
                                        str_custid = objectval.getString("custid");
                                        str_productcode = objectval.getString("productcode");
                                        str_purch = String.valueOf(value);
                                        int_arrayindex = (Integer) v.getTag();


//                                        objectval.put("purch", value);
//                                        objectval.put("subtotal", String.valueOf(totalval));
//                                        array_addcart.put((Integer) v.getTag(), objectval);
                                        //addRow();

                                        progressDialog = new ProgressDialog(getActivity());
                                        progressDialog.setMessage("Update cart..."); // Setting Message
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog.show(); // Display Progress Dialog
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        new Communication_updateaddcart().execute();

                                    }

                                } catch (JSONException ee) {
                                    ee.printStackTrace();
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


                viewHolder.text_cart_plus.setOnTouchListener(new View.OnTouchListener()
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
                                    JSONObject objectval = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));

                                    Integer value = Integer.parseInt(objectval.getString("purch"));
                                    Integer value1 = Integer.parseInt(objectval.getString("stockinhand"));
                                    if (value >= value1)
                                    {
                                        AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                                        builder4.setTitle("Opps!");
                                        builder4.setMessage("Stock is not available!");
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
                                    else

                                    {
                                        if (value >= 1) {
                                            value++;

                                            float totalval;
                                            if (String.valueOf(objectval.getString("offers")).equalsIgnoreCase("yes")) {
                                                totalval = value * Float.parseFloat(objectval.getString("netsale1"));
                                            } else {
                                                totalval = value * Float.parseFloat(objectval.getString("netsale"));
                                            }
                                            str_subtotal = String.valueOf(totalval);
                                            str_custid = objectval.getString("custid");
                                            str_productcode = objectval.getString("productcode");
                                            str_purch = String.valueOf(value);

                                                int_arrayindex = (Integer)v.getTag();
//                                            objectval.put("purch", value);
//                                            objectval.put("subtotal", String.valueOf(totalval));
//                                            array_addcart.put((Integer) v.getTag(), objectval);
                                            //addRow();

                                            progressDialog = new ProgressDialog(getActivity());
                                            progressDialog.setMessage("Update cart..."); // Setting Message
                                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            progressDialog.show(); // Display Progress Dialog
                                            progressDialog.setCancelable(false);
                                            progressDialog.show();
                                            new Communication_updateaddcart().execute();

                                        }
                                    }

                                } catch (JSONException ee) {
                                    ee.printStackTrace();
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


                Picasso.with(getActivity())
                        .load(str_imageurl)
                        .placeholder(R.drawable.default1)
                        .into(viewHolder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });

                viewHolder.imageView.setOnTouchListener(new View.OnTouchListener()
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


                            ImageView image = (ImageView)v.findViewWithTag(v.getTag());
                            image.invalidate();
                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            ZoomImageActivity.drawableBitmap=bitmap;
                            Intent intent=new Intent(getActivity(),ZoomImageActivity.class);
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


                viewHolder.text_home_addtocart.setOnTouchListener(new View.OnTouchListener()
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

                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Delete cart..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                try {
                                    JSONObject obj_val = new JSONObject(String.valueOf(array_addcart.getJSONObject((Integer) v.getTag())));

                                    str_custid = obj_val.getString("custid");
                                    str_productcode = obj_val.getString("productcode");
                                    int_tag_val = (Integer) v.getTag();
                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }

                                new Communication_deletecart().execute();
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

            } catch (JSONException e)
            {
                e.printStackTrace();
            }



            return view;
        }
        class ViewHolder
        {
            ImageView imageView;
            TextView text_offer_per_cart;
            TextView text_productname_cart;
            TextView text_mgfname_cart;
            TextView text_mrp_price_cart;
            TextView text_salerate_price_cart;
            TextView text_salerate_price_cart2;
            TextView text_salerate_price_cart3;
            TextView text_packingdate_cart;
            TextView text_expdate_cart;
            TextView text_cart_minus;
            TextView text_cart_qtytitle;
            TextView text_cart_plus;
            TextView text_offer_note;
            TextView text_home_addtocart;

        }


    }




    //////////////////

}
