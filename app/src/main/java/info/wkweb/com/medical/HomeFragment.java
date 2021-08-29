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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Debug;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment
{

   // private RecyclerView recyclerView_home;
  //  private ListHomeAdapter adapter_home;
   // private ListViewAdapter listViewAdapter;

    private List<Person> listPerson;
    private List<Person> listCompleteData;
    private ProgressBar progressBar;
    private AsyncTaskWait asyncTaskWait;

    ListView listview_home_list1;
    ProgressBar progress_home1;


    String str_stockinhand1,str_productcode1,str_mgfcmp1,str_productname1,str_packing1,str_expiry1,str_image1,str_mrp1,str_note1,str_netsale1,str_netsale11,str_purch1,str_subtotal1;
    Integer int_tag_val1;
    SearchView searchbar1;
    JSONArray array_preoduct11,array_preoduct111;
    ListviewAdaptercartnew listview_customadpte1;
    String str_offersper1,str_searchtext,str_loaddata;
    SwipeRefreshLayout swipeToRefresh_home1;

    JSONArray array_phrma,array_genral,array_surgical,array_vtenary;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView listview_home_list;
    ProgressBar progress_home;
    ProgressDialog progressDialog;

    AlertDialog alert11;
    String str_stockinhand,str_productcode,str_mgfcmp,str_productname,str_packing,str_expiry,str_image,str_mrp,str_netsale,str_purch,str_subtotal;
    Integer int_tag_val;
    SearchView searchbar;
    JSONArray array_preoduct;
    ListviewAdaptercart listview_customadpte;
    public static  RelativeLayout relative_newoffer_layout,relative_home_layout;
    RelativeLayout relative_pharama,relative_genral,relative_surgicale,relative_new;
    TextView text_check_pharma,text_check_genral,text_check_surgicale,text_check_new;
    String str_category_select;
    SwipeRefreshLayout swipeToRefresh_home;


    private LayoutInflater inflater1;

    String currentVersion;
    int verCode;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            addMoreItems();
            Log.d("asyncstatus", "status = " + asyncTaskWait.getStatus().name());
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("result");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();


    }

    public HomeFragment() {
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

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progress_home1=(ProgressBar)view.findViewById(R.id.progress_home_new);
        listview_home_list1=(ListView)view.findViewById(R.id.listview_home_list_new);
        searchbar1 = (SearchView)view.findViewById(R.id.searchView_new);
        swipeToRefresh_home1 =(SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh_home_new);
        swipeToRefresh_home1.setColorSchemeResources(R.color.colorAccent);

        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        inflater1 = inflater;//(LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

        progress_home=(ProgressBar)view.findViewById(R.id.progress_home);
        listview_home_list=(ListView)view.findViewById(R.id.listview_home_list);
        searchbar = (SearchView) view.findViewById(R.id.searchView_home);
        relative_pharama = (RelativeLayout) view.findViewById(R.id.relative_pharama);
        relative_genral = (RelativeLayout) view.findViewById(R.id.relative_genral);
        relative_surgicale = (RelativeLayout) view.findViewById(R.id.relative_surgicale);
        relative_new = (RelativeLayout) view.findViewById(R.id.relative_new);
        relative_home_layout = (RelativeLayout) view.findViewById(R.id.relative_home_layout);
        relative_newoffer_layout= (RelativeLayout) view.findViewById(R.id.relative_newoffer_layout);
       // recyclerView_home = (RecyclerView) view.findViewById(R.id.recycler_view);

        swipeToRefresh_home = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh_home);
        swipeToRefresh_home.setColorSchemeResources(R.color.colorAccent);

        text_check_pharma = (TextView) view.findViewById(R.id.text_check_pharma);
        text_check_genral = (TextView) view.findViewById(R.id.text_check_genral);
        text_check_surgicale = (TextView) view.findViewById(R.id.text_check_surgicale);
        text_check_new = (TextView) view.findViewById(R.id.text_check_new);
        array_preoduct = new JSONArray();
        array_phrma= new JSONArray();
        array_genral= new JSONArray();
        array_surgical= new JSONArray();
        array_vtenary = new JSONArray();
        str_category_select="PHRAMA";
        str_searchtext="";

        relative_newoffer_layout.setVisibility(View.INVISIBLE);
        relative_home_layout.setVisibility(View.VISIBLE);

        text_check_pharma.setBackgroundResource(R.drawable.radio_check);
        text_check_genral.setBackgroundResource(R.drawable.radio_uncheck);
        text_check_surgicale.setBackgroundResource(R.drawable.radio_uncheck);
        text_check_new.setBackgroundResource(R.drawable.radio_uncheck);

//        Log.d("devicetoken", FirebaseInstanceId.getInstance().getToken());
        array_preoduct11 = new JSONArray();
        array_preoduct111 = new JSONArray();

        listview_customadpte1 = new  ListviewAdaptercartnew();
        listview_home_list1.setAdapter(listview_customadpte1);
        listview_home_list1.setSmoothScrollbarEnabled(true);

        str_loaddata="no";

       listview_customadpte = new ListviewAdaptercart();
        listview_home_list.setAdapter(listview_customadpte);
        setListViewFooter();
        setListOnScrollListener();
        progressBar.setVisibility(View.INVISIBLE);



        PackageInfo info;
        try {
            info = getActivity().getPackageManager().getPackageInfo("info.wkweb.com.medical", 0);
            currentVersion =info.versionName;
            verCode = info.versionCode;

            Log.d("Version name", String.valueOf(verCode));
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hash key", something);
//            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        }  catch (Exception e) {
            Log.e("exception", e.toString());
        }

//        adapter_home = new ListHomeAdapter();
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView_home.setLayoutManager(mLayoutManager);
//        recyclerView_home.setItemAnimator(new DefaultItemAnimator());
//        recyclerView_home.setAdapter(adapter_home);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(homeupdate1,
                new IntentFilter("updatehome"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(homeupdate2,
                new IntentFilter("updatecarts"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(homeupdate3,
                new IntentFilter("ordercomplete"));



        searchbar1.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

            public boolean onQueryTextSubmit(String query) {
                Log.d("seach_query", query);
                // do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                array_preoduct11 = new JSONArray();
                if (array_preoduct111 != null)
                {
                    if (TextUtils.isEmpty(newText.toString())) {

                        //str_search_txt = "";
                        array_preoduct11 = array_preoduct111;
                    } else
                    {


                        for (int i = 0; i < array_preoduct111.length(); i++) {

                            try {
                                String string = array_preoduct111.getJSONObject(i).getString("productname");
                                String str_category = array_preoduct111.getJSONObject(i).getString("mfgcmp");
                                // str_search_txt = newText;
                                if ((string.toLowerCase()).contains(newText.toLowerCase()) || (str_category.toLowerCase()).contains(newText.toLowerCase()) ) {

                                    array_preoduct11.put(array_preoduct111.getJSONObject(i));


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }


                    }

                        if (listview_home_list1.getAdapter() != null)
                        {
                            listview_customadpte1.notifyDataSetChanged();
                        }

                }
                else
                {
                    if (listview_home_list1.getAdapter() != null)
                    {
                        listview_customadpte1.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });
        swipeToRefresh_home1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                   // new Communication_productsnew().execute();
                }
                else
                {
                    swipeToRefresh_home1.setRefreshing(false);
                    Connections();
                }

            }
        });


        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                Log.d("seach_query", query);
                // do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {



                if (array_phrma != null)
                {
                    if (TextUtils.isEmpty(newText.toString())) {

                        str_searchtext = "";
                        array_preoduct = new JSONArray();

                        if (str_category_select.equalsIgnoreCase("PHRAMA"))
                        {

                            array_preoduct = array_phrma;
                        }
                        if (str_category_select.equalsIgnoreCase("GENERAL"))
                        {
                            array_preoduct = array_genral;
                        }
                        if (str_category_select.equalsIgnoreCase("SURGICAL"))
                        {
                            array_preoduct = array_surgical;
                        }
                        if (str_category_select.equalsIgnoreCase("VETERINARY"))
                        {
                            array_preoduct = array_vtenary;
                        }


                    } else
                        {
                            array_preoduct = new JSONArray();
                            str_searchtext = "yes";
                            if (str_category_select.equalsIgnoreCase("PHRAMA"))
                            {
                                for (int i = 0; i < array_phrma.length(); i++) {

                                    try {
                                        String string = array_phrma.getJSONObject(i).getString("productname");
                                        String str_category = array_phrma.getJSONObject(i).getString("mfgcmp");
                                        String str_note = array_phrma.getJSONObject(i).getString("note");
                                        String str_note1 = "";
                                        if(str_note.length() >=1)
                                        {
                                            str_note1 = "offer";
                                        }

                                        // str_search_txt = newText;
                                        if ((string.toLowerCase()).contains(newText.toLowerCase()) || (str_category.toLowerCase()).contains(newText.toLowerCase()) || (str_note.toLowerCase()).contains(newText.toLowerCase()) || (str_note1.toLowerCase()).contains(newText.toLowerCase())) {

                                            array_preoduct.put(array_phrma.getJSONObject(i));


                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }
                            if (str_category_select.equalsIgnoreCase("GENERAL"))
                            {
                                for (int i = 0; i < array_genral.length(); i++) {

                                    try {
                                        String string = array_genral.getJSONObject(i).getString("productname");
                                        String str_category = array_genral.getJSONObject(i).getString("mfgcmp");
                                        String str_note = array_phrma.getJSONObject(i).getString("note");
                                        String str_note1 = "";
                                        if(str_note.length() >=1)
                                        {
                                            str_note1 = "offer";
                                        }
                                        if ((string.toLowerCase()).contains(newText.toLowerCase()) || (str_category.toLowerCase()).contains(newText.toLowerCase()) || (str_note.toLowerCase()).contains(newText.toLowerCase()) || (str_note1.toLowerCase()).contains(newText.toLowerCase())) {

                                            array_preoduct.put(array_genral.getJSONObject(i));


                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }
                            if (str_category_select.equalsIgnoreCase("SURGICAL"))
                            {

                                for (int i = 0; i < array_surgical.length(); i++) {

                                    try {
                                        String string = array_surgical.getJSONObject(i).getString("productname");
                                        String str_category = array_surgical.getJSONObject(i).getString("mfgcmp");
                                        String str_note = array_phrma.getJSONObject(i).getString("note");
                                        String str_note1 = "";
                                        if(str_note.length() >=1)
                                        {
                                            str_note1 = "offer";
                                        }
                                        if ((string.toLowerCase()).contains(newText.toLowerCase()) || (str_category.toLowerCase()).contains(newText.toLowerCase()) || (str_note.toLowerCase()).contains(newText.toLowerCase())|| (str_note1.toLowerCase()).contains(newText.toLowerCase())) {

                                            array_preoduct.put(array_surgical.getJSONObject(i));

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }

                            if (str_category_select.equalsIgnoreCase("VETERINARY")) {

                                for (int i = 0; i < array_vtenary.length(); i++) {

                                    try {
                                        String string = array_vtenary.getJSONObject(i).getString("productname");
                                        String str_category = array_vtenary.getJSONObject(i).getString("mfgcmp");
                                        String str_note = array_phrma.getJSONObject(i).getString("note");
                                        String str_note1 = "";
                                        if(str_note.length() >=1)
                                        {
                                            str_note1 = "offer";
                                        }
                                        if ((string.toLowerCase()).contains(newText.toLowerCase()) || (str_category.toLowerCase()).contains(newText.toLowerCase()) || (str_note.toLowerCase()).contains(newText.toLowerCase())|| (str_note1.toLowerCase()).contains(newText.toLowerCase())) {

                                            array_preoduct.put(array_vtenary.getJSONObject(i));

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }


                    }


                    if (listview_home_list.getAdapter() !=null)
                    {
                        listview_customadpte.notifyDataSetChanged();
                    }


                }
                else
                {
                    if (listview_home_list.getAdapter() !=null)
                    {
                        listview_customadpte.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });

        relative_pharama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_check_pharma.setBackgroundResource(R.drawable.radio_check);
                text_check_genral.setBackgroundResource(R.drawable.radio_uncheck);
                text_check_surgicale.setBackgroundResource(R.drawable.radio_uncheck);
                text_check_new.setBackgroundResource(R.drawable.radio_uncheck);
                str_category_select="PHRAMA";
                array_preoduct = array_phrma;
                if (array_phrma.length() !=0) {

                 //   progressBar.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                listview_customadpte.notifyDataSetChanged();
//                listview_customadpte = new ListviewAdaptercart();
//                listview_home_list.setAdapter(listview_customadpte);
            }
        });
//
        relative_genral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_check_pharma.setBackgroundResource(R.drawable.radio_uncheck);
                text_check_genral.setBackgroundResource(R.drawable.radio_check);
                text_check_surgicale.setBackgroundResource(R.drawable.radio_uncheck);
                text_check_new.setBackgroundResource(R.drawable.radio_uncheck);
                str_category_select="GENERAL";
                array_preoduct = array_genral;
                if (array_genral.length() !=0) {
                  //  progressBar.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                listview_customadpte.notifyDataSetChanged();

            }
        });
//
        relative_surgicale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_check_pharma.setBackgroundResource(R.drawable.radio_uncheck);
                text_check_genral.setBackgroundResource(R.drawable.radio_uncheck);
                text_check_surgicale.setBackgroundResource(R.drawable.radio_check);
                text_check_new.setBackgroundResource(R.drawable.radio_uncheck);
                str_category_select="SURGICAL";
                array_preoduct = array_surgical;
                if (array_surgical.length() !=0) {
                 //   progressBar.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                listview_customadpte.notifyDataSetChanged();
//                listview_customadpte = new ListviewAdaptercart();
//                listview_home_list.setAdapter(listview_customadpte);
            }
        });

        relative_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_check_pharma.setBackgroundResource(R.drawable.radio_uncheck);
                text_check_genral.setBackgroundResource(R.drawable.radio_uncheck);
                text_check_surgicale.setBackgroundResource(R.drawable.radio_uncheck);
                text_check_new.setBackgroundResource(R.drawable.radio_check);


                str_category_select="VETERINARY";
                array_preoduct = array_vtenary;
                if (array_vtenary.length() !=0)
                {
                  //  progressBar.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                listview_customadpte.notifyDataSetChanged();

            }
        });

//        relative_new.setOnTouchListener(new View.OnTouchListener()
//        {
//            View v;
//            private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
//
//
//                @Override
//                public boolean onDoubleTap(MotionEvent e)
//                {
//
//                    Toast.makeText(getActivity(), "Not allow to double tapped.",Toast.LENGTH_LONG).show();
//                    return super.onDoubleTap(e);
//                }
//
//                @Override
//                public boolean onSingleTapConfirmed(MotionEvent e) {
//
//
//
////
////
////                    if (array_preoduct111.length() == 0)
////                    {
////                        new Communication_productsnew().execute();
////                    }
//                    return super.onSingleTapConfirmed(e);
//                }
//
//
//
//            });
//
//            @Override
//            public boolean onTouch(View v1, MotionEvent event) {
//
//                v = v1;
//
//                gestureDetector.onTouchEvent(event);
//                return true;
//            }
//        });





        swipeToRefresh_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {

                            ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = cm.getActiveNetworkInfo();
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                                swipeToRefresh_home.setRefreshing(false);
                                if (str_searchtext.length() == 0)
                                {
                                    swipeToRefresh_home.setRefreshing(true);
                                    new Communication_products().execute();

                                }
                            }
                            else
                            {
                                swipeToRefresh_home.setRefreshing(false);
                                Connections();
                            }

                            // Your implementation goes here
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }).start();


            }
        });

        new Communication_products().execute();
      //

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private class ListviewAdaptercart extends BaseAdapter
    {


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
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ViewHolder viewHolder;
            if(view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.listrow_home, null);

                viewHolder = new ViewHolder();


                viewHolder. image_tab =  view.findViewById(R.id.image_product);
                viewHolder. text_tab_name =  view.findViewById(R.id.text_productname);
                viewHolder. text_mgfname =  view.findViewById(R.id.text_mgfname);

                viewHolder. text_tab_mrp = view.findViewById(R.id.text_mrp_price);
                viewHolder. text_tab_exp =  view.findViewById(R.id.text_expdate);
                viewHolder.  text_tab_salerate =  view.findViewById(R.id.text_salerate_price);
                viewHolder.  text_tab_salerate2 =  view.findViewById(R.id.text_salerate_price2);
                viewHolder.  text_tab_salerate3 =  view.findViewById(R.id.text_salerate_price3);
                viewHolder.  text_tab_pkg =  view.findViewById(R.id.text_packingdate);
                viewHolder.  text_AvailabelStock =  view.findViewById(R.id.text_AvailabelStock);
                viewHolder.  text_home_addtocart =  view.findViewById(R.id.text_home_addtocart);
                viewHolder.  text_offer_per =  view.findViewById(R.id.text_offer_per);
                viewHolder. text_offer_note =  view.findViewById(R.id.text_offer_note);

                view.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder)view.getTag();
            }

            viewHolder.text_tab_salerate.setTextColor(getResources().getColor(R.color.colorgreen1));

            viewHolder.text_tab_salerate2.setText("");
            viewHolder.text_tab_salerate3.setText("");
            viewHolder.text_offer_per.setText("");
            viewHolder.text_tab_salerate2.setVisibility(View.GONE);
            viewHolder.text_tab_salerate3.setVisibility(View.GONE);
            viewHolder.text_offer_per.setVisibility(View.GONE);
            viewHolder.text_offer_note.setVisibility(View.GONE);

            viewHolder.text_home_addtocart.setTag(i);
            viewHolder.image_tab.setTag(i);
            String str_msg1 = "Offer: ",str_msg2 = "";
            final JSONObject object_values;
            try {
                object_values = new JSONObject(String.valueOf(array_preoduct.getJSONObject(i)));

            String str_imageurl = object_values.getString("imageurl");
                viewHolder.text_tab_name.setText(String.valueOf(object_values.getString("productname")));
                viewHolder.text_tab_mrp.setText(String.valueOf("₹"+object_values.getString("mrp")));
                viewHolder.text_tab_salerate.setText(String.valueOf("₹"+object_values.getString("netsale")));
                viewHolder.text_tab_exp.setText(String.valueOf(object_values.getString("expdate")));
                viewHolder.text_tab_pkg.setText(String.valueOf(object_values.getString("packing")));
                str_msg2 = String.valueOf(object_values.getString("note"));

                viewHolder.text_offer_note.setText(Html.fromHtml( " <font color=#000000>  "+str_msg1+ " </font> <font color=#0000ff> <b> "+str_msg2+ " </b> </font>"));


                if (str_msg2.length() >= 2)
                {
                    viewHolder.text_offer_note.setVisibility(View.VISIBLE);
                }


                Integer value1 ;
                if (object_values.getString("stockinhand").equalsIgnoreCase("null") || object_values.getString("stockinhand").length()== 0)
                {
                     value1 = 0;

                }
                else
                {
                    value1 = Integer.parseInt(object_values.getString("stockinhand"));
                }
                if (value1 <= 0)
                {
                    viewHolder.text_AvailabelStock.setTextColor(Color.RED);
                    viewHolder.text_AvailabelStock.setText("Out of Stock");
                    viewHolder.text_home_addtocart.setVisibility(View.INVISIBLE);
                }
                else
                {
                    viewHolder.text_AvailabelStock.setTextColor(getResources().getColor(R.color.colordarkblue));
                    viewHolder.text_AvailabelStock.setText(String.valueOf(object_values.getString("stockinhand")));
                    if (object_values.getString("cart").equalsIgnoreCase("yes"))
                    {
                        viewHolder.text_home_addtocart.setVisibility(View.INVISIBLE);
                    } else
                    {
                        viewHolder.text_home_addtocart.setVisibility(View.VISIBLE);
                    }
                }

                if (str_imageurl.length() == 0 || str_imageurl.equalsIgnoreCase("null"))
                {
                    str_imageurl ="http://www.sachinmokashi";
                }

                if (String.valueOf(object_values.getString("mfgcmp")).length() == 0)
                {
                    viewHolder.text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")));
                }
                else
                {
                    if (String.valueOf(object_values.getString("mfgcmp")).length() >=8)
                    {
                        viewHolder.text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")).substring(0,7));

                    }
                    else
                    {
                        viewHolder.text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")).substring(0,String.valueOf(object_values.getString("mfgcmp")).length()-1));
                    }

                }


                Picasso.with(getActivity())
                        .load(str_imageurl)
                        .placeholder(R.drawable.default1)
                        .into( viewHolder.image_tab, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });

                viewHolder.image_tab.setOnTouchListener(new View.OnTouchListener()
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


                                 int int_countitem = Integer.parseInt(SingletonObject.Instance().getstr_count());

                                 if (int_countitem <= 100)
                                 {
                                     progressDialog = new ProgressDialog(getActivity());
                                 progressDialog.setMessage("Add to cart..."); // Setting Message
                                 progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
                                     str_subtotal = String.valueOf(obj_val.getString("netsale"));
                                     str_netsale = String.valueOf(obj_val.getString("netsale"));
                                     str_expiry = String.valueOf(obj_val.getString("expdate"));
                                     str_packing = String.valueOf(obj_val.getString("packing"));
                                     str_stockinhand = String.valueOf(obj_val.getString("stockinhand"));


                                     int_tag_val = (Integer) v.getTag();

                                 } catch (JSONException ee) {
                                     ee.printStackTrace();
                                 }

                                 new Communication_addcart().execute();
                             }
                             else
                                 {
                                     AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                                     builder4.setTitle("Your cart is full!");
                                     builder4.setMessage("Now you could not added more item into cart list, because You can added only 100 item per order");
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
//                        text_home_addtocart.setFocusable(false);
//                        progressDialog = new ProgressDialog(getActivity());
//                        progressDialog.setMessage("Add to cart..."); // Setting Message
//                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
//

            } catch (JSONException e) {
                e.printStackTrace();
            }



            return view;
        }
        class ViewHolder
        {
            ImageView image_tab ;
            TextView text_tab_name ;
            TextView text_mgfname;

            TextView text_tab_mrp ;
            TextView text_tab_exp ;
            TextView  text_tab_salerate ;
            TextView  text_tab_salerate2;
            TextView  text_tab_salerate3;
            TextView  text_tab_pkg ;
            TextView  text_AvailabelStock;
            TextView  text_home_addtocart;
            TextView  text_offer_per;
            TextView text_offer_note;
        }

//        {
//
//            view = inflater1.inflate(R.layout.listrow_home, null,false);
//           ImageView image_tab = (ImageView) view.findViewById(R.id.image_product);
//            TextView text_tab_name = (TextView) view.findViewById(R.id.text_productname);
//            TextView text_mgfname = (TextView) view.findViewById(R.id.text_mgfname);
//
//            TextView text_tab_mrp = (TextView) view.findViewById(R.id.text_mrp_price);
//            TextView text_tab_exp = (TextView) view.findViewById(R.id.text_expdate);
//            TextView  text_tab_salerate = (TextView) view.findViewById(R.id.text_salerate_price);
//            TextView  text_tab_salerate2 = (TextView) view.findViewById(R.id.text_salerate_price2);
//            TextView  text_tab_salerate3 = (TextView) view.findViewById(R.id.text_salerate_price3);
//            TextView  text_tab_pkg = (TextView) view.findViewById(R.id.text_packingdate);
//            TextView  text_AvailabelStock = (TextView) view.findViewById(R.id.text_AvailabelStock);
//            final TextView  text_home_addtocart = (TextView) view.findViewById(R.id.text_home_addtocart);
//            TextView  text_offer_per = (TextView) view.findViewById(R.id.text_offer_per);
//            TextView text_offer_note = (TextView) view.findViewById(R.id.text_offer_note);
//
//
//            text_tab_salerate.setTextColor(getResources().getColor(R.color.colorgreen1));
//
//            text_tab_salerate2.setText("");
//            text_tab_salerate3.setText("");
//            text_offer_per.setText("");
//              text_tab_salerate2.setVisibility(View.GONE);
//            text_tab_salerate3.setVisibility(View.GONE);
//            text_offer_per.setVisibility(View.GONE);
//            text_offer_note.setVisibility(View.GONE);
//
//            text_home_addtocart.setTag(i);
//            image_tab.setTag(i);
//            final JSONObject object_values;
//            try {
//                object_values = new JSONObject(String.valueOf(array_preoduct.getJSONObject(i)));
//                 String str_imageurl = object_values.getString("imageurl");
//                text_tab_name.setText(String.valueOf(object_values.getString("productname")));
//                text_tab_mrp.setText(String.valueOf("₹"+object_values.getString("mrp")));
//                text_tab_salerate.setText(String.valueOf("₹"+object_values.getString("netsale")));
//                text_tab_exp.setText(String.valueOf(object_values.getString("expdate")));
//                text_tab_pkg.setText(String.valueOf(object_values.getString("packing")));
//                text_AvailabelStock.setText(String.valueOf(object_values.getString("stockinhand")));
//
//                if (str_imageurl.length() == 0)
//                {
//                    str_imageurl ="http://www.sachinmokashi";
//                }
//
//                if (String.valueOf(object_values.getString("mfgcmp")).length() == 0)
//                {
//                    text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")));
//                }
//                else
//                {
//                    if (String.valueOf(object_values.getString("mfgcmp")).length() >=8)
//                    {
//                        text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")).substring(0,7));
//
//                    }
//                    else
//                    {
//                        text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")).substring(0,String.valueOf(object_values.getString("mfgcmp")).length()-1));
//                    }
//
//                }
//
//                if (object_values.getString("cart").equalsIgnoreCase("yes")) {
//                    text_home_addtocart.setVisibility(View.INVISIBLE);
//                } else {
//                    text_home_addtocart.setVisibility(View.VISIBLE);
//                }
//                Picasso.with(getActivity())
//                        .load(str_imageurl)
//                        .placeholder(R.drawable.default1)
//                        .into( image_tab, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//
//                            }
//
//                            @Override
//                            public void onError() {
//
//                            }
//                        });
//
////                image_tab.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////                        ImageView image = (ImageView)v.findViewWithTag(v.getTag());
////                        image.invalidate();
////                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
////                        Bitmap bitmap = drawable.getBitmap();
////                        ZoomImageActivity.drawableBitmap=bitmap;
////                        Intent intent=new Intent(getActivity(),ZoomImageActivity.class);
////                        startActivity(intent);
////
////                    }
////                });
//
//                image_tab.setOnTouchListener(new View.OnTouchListener()
//                {
//                    View v;
//                    private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
//
//
//                        @Override
//                        public boolean onDoubleTap(MotionEvent e)
//                        {
//
//                            Toast.makeText(getActivity(), "Not allow to double tapped.",Toast.LENGTH_LONG).show();
//                            return super.onDoubleTap(e);
//                        }
//
//                        @Override
//                        public boolean onSingleTapConfirmed(MotionEvent e) {
//
//
//                            ImageView image = (ImageView)v.findViewWithTag(v.getTag());
//                        image.invalidate();
//                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//                        Bitmap bitmap = drawable.getBitmap();
//                        ZoomImageActivity.drawableBitmap=bitmap;
//                        Intent intent=new Intent(getActivity(),ZoomImageActivity.class);
//                        startActivity(intent);
//
//                            return super.onSingleTapConfirmed(e);
//                        }
//
//
//
//                    });
//
//                    @Override
//                    public boolean onTouch(View v1, MotionEvent event) {
//
//                        v = v1;
//
//                        gestureDetector.onTouchEvent(event);
//                        return true;
//                    }
//                });
//
//
//                text_home_addtocart.setOnTouchListener(new View.OnTouchListener()
//                {
//                    View v;
//                    private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
//
//                        @Override
//                        public boolean onDoubleTap(MotionEvent e)
//                        {
//
//                            Toast.makeText(getActivity(), "Not allow to double tapped.",Toast.LENGTH_LONG).show();
//                            return super.onDoubleTap(e);
//                        }
//
//                         @Override
//                        public boolean onSingleTapConfirmed(MotionEvent e) {
//
//
//                             ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//                             NetworkInfo netInfo = cm.getActiveNetworkInfo();
//                             if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//
//
//
//                        progressDialog1 = new ProgressDialog(getActivity());
//                        progressDialog1.setMessage("Add to cart..."); // Setting Message
//                        progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                        progressDialog1.setCancelable(false);
//                        progressDialog1.show();
//
//                        try {
//                            JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct.getJSONObject((Integer) v.getTag())));
//
//                            str_productcode1 = String.valueOf(obj_val.getString("productcode"));
//                            str_productname1 = String.valueOf(obj_val.getString("productname"));
//                            str_mgfcmp1 = String.valueOf(obj_val.getString("mfgcmp"));
//                            str_mrp1 = String.valueOf(obj_val.getString("mrp"));
//                            str_image1 = String.valueOf(obj_val.getString("imageurl"));
//                            str_purch1 = "1";
//                            str_subtotal1 = String.valueOf(obj_val.getString("netsale"));
//                            str_netsale1 = String.valueOf(obj_val.getString("netsale"));
//                            str_expiry1= String.valueOf(obj_val.getString("expdate"));
//                            str_packing1 = String.valueOf(obj_val.getString("packing"));
//
//
//                            int_tag_val1 = (Integer) v.getTag();
//
//                        } catch (JSONException ea) {
//                            ea.printStackTrace();
//                        }
//
//                        new Communication_addcartnew().execute();
//                             }
//                             else
//                             {
//
//                                 Connections();
//
//                             }
//
//                            return super.onSingleTapConfirmed(e);
//                        }
//
//
//
//                    });
//
//                    @Override
//                    public boolean onTouch(View v1, MotionEvent event) {
//
//                      v = v1;
//
//                        gestureDetector.onTouchEvent(event);
//                        return true;
//                    }
//                });
//
//
////                text_home_addtocart.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////
////                        text_home_addtocart.setFocusable(false);
////                        progressDialog = new ProgressDialog(getActivity());
////                        progressDialog.setMessage("Add to cart..."); // Setting Message
////                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////                        progressDialog.setCancelable(false);
////                        progressDialog.show();
////
////                        try {
////                            JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct.getJSONObject((Integer) v.getTag())));
////
////                            str_productcode = String.valueOf(obj_val.getString("productcode"));
////                            str_productname = String.valueOf(obj_val.getString("productname"));
////                            str_mgfcmp = String.valueOf(obj_val.getString("mfgcmp"));
////                            str_mrp = String.valueOf(obj_val.getString("mrp"));
////                            str_image = String.valueOf(obj_val.getString("imageurl"));
////                            str_purch = "1";
////                            str_subtotal = String.valueOf(obj_val.getString("netsale"));
////                            str_netsale = String.valueOf(obj_val.getString("netsale"));
////                            str_expiry = String.valueOf(obj_val.getString("expdate"));
////                            str_packing = String.valueOf(obj_val.getString("packing"));
////
////
////                            int_tag_val = (Integer) v.getTag();
////
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////
////                        new Communication_addcart().execute();
////
////
////                    }
////                });
//
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            return view;
//        }
    }

    private class ListviewAdaptercartnew extends BaseAdapter {
        private LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return array_preoduct11.length();
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
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ViewHolder viewHolder;
            if(view == null)
            {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.listrow_home, null);
                viewHolder = new ViewHolder();

                viewHolder.image_tab = (ImageView) view.findViewById(R.id.image_product);
                viewHolder.text_tab_name = (TextView) view.findViewById(R.id.text_productname);
                viewHolder.text_mgfname = (TextView) view.findViewById(R.id.text_mgfname);
                viewHolder.text_offer_note = (TextView) view.findViewById(R.id.text_offer_note);

                viewHolder.text_tab_mrp = (TextView) view.findViewById(R.id.text_mrp_price);
                viewHolder.text_tab_exp = (TextView) view.findViewById(R.id.text_expdate);
                viewHolder.text_tab_salerate = (TextView) view.findViewById(R.id.text_salerate_price);
                viewHolder.text_tab_salerate2 = (TextView) view.findViewById(R.id.text_salerate_price2);
                viewHolder.text_tab_salerate3 = (TextView) view.findViewById(R.id.text_salerate_price3);
                viewHolder.text_tab_pkg = (TextView) view.findViewById(R.id.text_packingdate);
                viewHolder.text_AvailabelStock = (TextView) view.findViewById(R.id.text_AvailabelStock);
                viewHolder.text_home_addtocart = (TextView) view.findViewById(R.id.text_home_addtocart);
                viewHolder.text_offer_per = (TextView) view.findViewById(R.id.text_offer_per);

                view.setTag(viewHolder);

            }
            else
            {
                viewHolder = (ViewHolder)view.getTag();
            }



            viewHolder.text_tab_salerate2.setText("");
            viewHolder.text_tab_salerate3.setText("");
            viewHolder.text_offer_per.setText("");
            viewHolder.text_tab_salerate2.setVisibility(View.VISIBLE);
            viewHolder.text_tab_salerate3.setVisibility(View.VISIBLE);
            viewHolder.text_offer_per.setVisibility(View.VISIBLE);

            viewHolder.text_tab_salerate.setTextColor(getResources().getColor(R.color.colorlightgray));
            viewHolder.text_tab_salerate2.setTextColor(getResources().getColor(R.color.colorlightgray));

            viewHolder.text_home_addtocart.setTag(i);
            viewHolder.image_tab.setTag(i);
            final JSONObject object_values;
            try {
                object_values = new JSONObject(String.valueOf(array_preoduct11.getJSONObject(i)));
                String str_imageurl = object_values.getString("imageurl");
                viewHolder.text_tab_name.setText(String.valueOf(object_values.getString("productname")));
                viewHolder.text_tab_mrp.setText(String.valueOf("₹"+object_values.getString("mrp")));
                viewHolder.text_tab_salerate.setText(String.valueOf("₹"+object_values.getString("netsale")));
                viewHolder.text_tab_salerate2.setText(String.valueOf("₹"+object_values.getString("netsale")));
                viewHolder.text_tab_salerate3.setText(String.valueOf("₹"+object_values.getString("netsale1")));
                viewHolder.text_offer_per.setText(String.valueOf(object_values.getString("offer") + "%"));
                viewHolder.text_tab_exp.setText(String.valueOf(object_values.getString("expdate")));
                viewHolder.text_tab_pkg.setText(String.valueOf(object_values.getString("packing")));
                // text_AvailabelStock.setText(String.valueOf(object_values.getString("stockinhand")));
                viewHolder.text_offer_note.setText(String.valueOf(object_values.getString("note")));
                viewHolder.text_offer_note.setVisibility(View.GONE);





                Integer value ;
                if (object_values.getString("stockinhand").equalsIgnoreCase("null"))
                {
                    value = 0;

                }
                else
                {
                    value = Integer.parseInt(object_values.getString("stockinhand"));
                }
                if (value <= 0)
                {
                    viewHolder.text_AvailabelStock.setTextColor(Color.RED);
                    viewHolder.text_AvailabelStock.setText("Out of Stock");
                    viewHolder.text_home_addtocart.setVisibility(View.INVISIBLE);
                }
                else
                {
                    viewHolder.text_AvailabelStock.setTextColor(getResources().getColor(R.color.colordarkblue));
                    viewHolder.text_AvailabelStock.setText(String.valueOf(object_values.getString("stockinhand")));
                    if (object_values.getString("cart").equalsIgnoreCase("yes"))
                    {
                        viewHolder.text_home_addtocart.setVisibility(View.INVISIBLE);
                    } else
                    {
                        viewHolder.text_home_addtocart.setVisibility(View.VISIBLE);
                    }


                }


                if (str_imageurl.length() == 0 || str_imageurl.equalsIgnoreCase("null"))
                {
                    str_imageurl = "http://www.sachinmokashi" ;
                }

                if (String.valueOf(object_values.getString("note")).length() != 0)
                {
                    viewHolder.text_offer_note.setVisibility(View.VISIBLE);
                }

                if (String.valueOf(object_values.getString("mfgcmp")).length() == 0)
                {
                    viewHolder.text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")));
                }
                else
                {
                    if (String.valueOf(object_values.getString("mfgcmp")).length() >=8)
                    {

                        viewHolder.text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")).substring(0,7));

                    }
                    else
                    {
                        viewHolder.text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")).substring(0,String.valueOf(object_values.getString("mfgcmp")).length()-1));
                    }

                }


                Picasso.with(getActivity())
                        .load(str_imageurl)
                        .placeholder(R.drawable.default1)
                        .into( viewHolder.image_tab, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });

                viewHolder.image_tab.setOnTouchListener(new View.OnTouchListener()
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
                                progressDialog.setMessage("Add to cart..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                try {
                                    JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct11.getJSONObject((Integer) v.getTag())));

                                    str_productcode1 = String.valueOf(obj_val.getString("productcode"));
                                    str_productname1 = String.valueOf(obj_val.getString("productname"));
                                    str_mgfcmp1 = String.valueOf(obj_val.getString("mfgcmp"));
                                    str_mrp1 = String.valueOf(obj_val.getString("mrp"));
                                    str_image1 = String.valueOf(obj_val.getString("imageurl"));
                                    str_purch1 = "1";
                                    str_subtotal1 = String.valueOf(obj_val.getString("netsale1"));
                                    str_netsale1= String.valueOf(obj_val.getString("netsale"));
                                    str_netsale11 = String.valueOf(obj_val.getString("netsale1"));
                                    str_note1 = String.valueOf(obj_val.getString("note"));
                                    str_expiry1 = String.valueOf(obj_val.getString("expdate"));
                                    str_packing1 = String.valueOf(obj_val.getString("packing"));
                                    str_offersper1= String.valueOf(obj_val.getString("offer"));
                                    str_stockinhand1=String.valueOf(obj_val.getString("stockinhand"));





                                    int_tag_val1 = (Integer) v.getTag();

                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }

                                new Communication_addcartnew().execute();

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
        class ViewHolder
        {
            ImageView image_tab ;
            TextView text_tab_name ;
            TextView text_mgfname;
            TextView text_offer_note;
            TextView text_tab_mrp;
            TextView text_tab_exp ;
            TextView  text_tab_salerate;
            TextView  text_tab_salerate2;
            TextView  text_tab_salerate3;
            TextView  text_tab_pkg;
            TextView  text_AvailabelStock;
            TextView  text_home_addtocart;
            TextView  text_offer_per;

        }
//        {
//
//            view = inflater.inflate(R.layout.listrow_home, null,false);
//            ImageView image_tab = (ImageView) view.findViewById(R.id.image_product);
//            TextView text_tab_name = (TextView) view.findViewById(R.id.text_productname);
//            TextView text_mgfname = (TextView) view.findViewById(R.id.text_mgfname);
//            TextView text_offer_note = (TextView) view.findViewById(R.id.text_offer_note);
//
//            TextView text_tab_mrp = (TextView) view.findViewById(R.id.text_mrp_price);
//            TextView text_tab_exp = (TextView) view.findViewById(R.id.text_expdate);
//            TextView  text_tab_salerate = (TextView) view.findViewById(R.id.text_salerate_price);
//            TextView  text_tab_salerate2 = (TextView) view.findViewById(R.id.text_salerate_price2);
//            TextView  text_tab_salerate3 = (TextView) view.findViewById(R.id.text_salerate_price3);
//            TextView  text_tab_pkg = (TextView) view.findViewById(R.id.text_packingdate);
//            TextView  text_AvailabelStock = (TextView) view.findViewById(R.id.text_AvailabelStock);
//            TextView  text_home_addtocart = (TextView) view.findViewById(R.id.text_home_addtocart);
//            TextView  text_offer_per = (TextView) view.findViewById(R.id.text_offer_per);
//
//
//            text_tab_salerate.setTextColor(getResources().getColor(R.color.colorlightgray));
//            text_tab_salerate2.setTextColor(getResources().getColor(R.color.colorlightgray));
//
//            text_tab_salerate2.setText("");
//            text_tab_salerate3.setText("");
//            text_offer_per.setText("");
//            text_tab_salerate2.setVisibility(View.VISIBLE);
//            text_tab_salerate3.setVisibility(View.VISIBLE);
//            text_offer_per.setVisibility(View.VISIBLE);
//
//
//            text_home_addtocart.setTag(i);
//            image_tab.setTag(i);
//            final JSONObject object_values;
//            try {
//                object_values = new JSONObject(String.valueOf(array_preoduct11.getJSONObject(i)));
//                String str_imageurl = object_values.getString("imageurl");
//                text_tab_name.setText(String.valueOf(object_values.getString("productname")));
//                text_tab_mrp.setText(String.valueOf("₹"+object_values.getString("mrp")));
//                text_tab_salerate.setText(String.valueOf("₹"+object_values.getString("netsale")));
//                text_tab_salerate2.setText(String.valueOf("₹"+object_values.getString("netsale")));
//                text_tab_salerate3.setText(String.valueOf("₹"+object_values.getString("netsale1")));
//                text_offer_per.setText(String.valueOf(object_values.getString("offer") + "%"));
//                text_tab_exp.setText(String.valueOf(object_values.getString("expdate")));
//                text_tab_pkg.setText(String.valueOf(object_values.getString("packing")));
//               // text_AvailabelStock.setText(String.valueOf(object_values.getString("stockinhand")));
//                text_offer_note.setText(String.valueOf(object_values.getString("note")));
//                text_offer_note.setVisibility(View.GONE);
//
//                Integer value1 = Integer.parseInt(object_values.getString("stockinhand"));
//
//
//                Integer value ;
//                if (object_values.getString("stockinhand").equalsIgnoreCase("null"))
//                {
//                    value = 0;
//
//                }
//                else
//                {
//                    value = Integer.parseInt(object_values.getString("stockinhand"));
//                }
//                if (value <= 0)
//                {
//                    text_AvailabelStock.setTextColor(Color.RED);
//                    text_AvailabelStock.setText("Out of Stock");
//                    text_home_addtocart.setVisibility(View.INVISIBLE);
//                }
//                else
//                {
//                    text_AvailabelStock.setTextColor(getResources().getColor(R.color.colordarkblue));
//                    text_AvailabelStock.setText(String.valueOf(object_values.getString("stockinhand")));
//                    if (object_values.getString("cart").equalsIgnoreCase("yes")) {
//                        text_home_addtocart.setVisibility(View.INVISIBLE);
//                    } else {
//                        text_home_addtocart.setVisibility(View.VISIBLE);
//                    }
//                }
//
//
//                if (str_imageurl.length() == 0 || str_imageurl.equalsIgnoreCase("null"))
//                {
//                    str_imageurl = "http://www.sachinmokashi" ;
//                }
//
//                if (String.valueOf(object_values.getString("note")).length() != 0)
//                {
//                    text_offer_note.setVisibility(View.VISIBLE);
//                }
//
//                if (String.valueOf(object_values.getString("mfgcmp")).length() == 0)
//                {
//                    text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")));
//                }
//                else
//                {
//                    if (String.valueOf(object_values.getString("mfgcmp")).length() >=8)
//                    {
//
//                        text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")).substring(0,7));
//
//                    }
//                    else
//                    {
//                        text_mgfname.setText(String.valueOf(object_values.getString("mfgcmp")).substring(0,String.valueOf(object_values.getString("mfgcmp")).length()-1));
//                    }
//
//                }
//
//
//                Picasso.with(getActivity())
//                        .load(str_imageurl)
//                        .placeholder(R.drawable.default1)
//                        .into( image_tab, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//
//                            }
//
//                            @Override
//                            public void onError() {
//
//                            }
//                        });
//
//                image_tab.setOnTouchListener(new View.OnTouchListener()
//                {
//                    View v;
//                    private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
//
//
//                        @Override
//                        public boolean onDoubleTap(MotionEvent e)
//                        {
//
//                            Toast.makeText(getActivity(), "Not allow to double tapped.",Toast.LENGTH_LONG).show();
//                            return super.onDoubleTap(e);
//                        }
//
//                        @Override
//                        public boolean onSingleTapConfirmed(MotionEvent e) {
//
//                            ImageView image = (ImageView)v.findViewWithTag(v.getTag());
//                            image.invalidate();
//                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//                            Bitmap bitmap = drawable.getBitmap();
//                            ZoomImageActivity.drawableBitmap=bitmap;
//                            Intent intent=new Intent(getActivity(),ZoomImageActivity.class);
//                            startActivity(intent);
//
//                            return super.onSingleTapConfirmed(e);
//                        }
//
//
//
//                    });
//
//                    @Override
//                    public boolean onTouch(View v1, MotionEvent event) {
//
//                        v = v1;
//
//                        gestureDetector.onTouchEvent(event);
//                        return true;
//                    }
//                });
//
//
////                image_tab.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////                        ImageView image = (ImageView)v.findViewWithTag(v.getTag());
////                        image.invalidate();
////                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
////                        Bitmap bitmap = drawable.getBitmap();
////                        ZoomImageActivity.drawableBitmap=bitmap;
////                        Intent intent=new Intent(NewOfferActivity.this,ZoomImageActivity.class);
////                        startActivity(intent);
////
////                    }
////                });
//
//
//                text_home_addtocart.setOnTouchListener(new View.OnTouchListener()
//                {
//                    View v;
//                    private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
//
//
//                        @Override
//                        public boolean onDoubleTap(MotionEvent e)
//                        {
//
//                            Toast.makeText(getActivity(), "Not allow to double tapped.",Toast.LENGTH_LONG).show();
//                            return super.onDoubleTap(e);
//                        }
//
//                        @Override
//                        public boolean onSingleTapConfirmed(MotionEvent e) {
//
//
//                            ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//                            NetworkInfo netInfo = cm.getActiveNetworkInfo();
//                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//                                progressDialog = new ProgressDialog(getActivity());
//                                progressDialog.setMessage("Add to cart..."); // Setting Message
//                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                                progressDialog.show(); // Display Progress Dialog
//                                progressDialog.setCancelable(false);
//                                progressDialog.show();
//
//                                try {
//                                    JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct11.getJSONObject((Integer) v.getTag())));
//
//                                    str_productcode1 = String.valueOf(obj_val.getString("productcode"));
//                                    str_productname1 = String.valueOf(obj_val.getString("productname"));
//                                    str_mgfcmp1 = String.valueOf(obj_val.getString("mfgcmp"));
//                                    str_mrp1 = String.valueOf(obj_val.getString("mrp"));
//                                    str_image1 = String.valueOf(obj_val.getString("imageurl"));
//                                    str_purch1 = "1";
//                                    str_subtotal1 = String.valueOf(obj_val.getString("netsale1"));
//                                    str_netsale1= String.valueOf(obj_val.getString("netsale"));
//                                    str_netsale11 = String.valueOf(obj_val.getString("netsale1"));
//                                    str_note1 = String.valueOf(obj_val.getString("note"));
//                                    str_expiry1 = String.valueOf(obj_val.getString("expdate"));
//                                    str_packing1 = String.valueOf(obj_val.getString("packing"));
//                                    str_offersper1= String.valueOf(obj_val.getString("offer"));
//                                    str_stockinhand1=String.valueOf(obj_val.getString("stockinhand"));
//
//
//
//
//
//                                    int_tag_val1 = (Integer) v.getTag();
//
//                                } catch (JSONException ee) {
//                                    ee.printStackTrace();
//                                }
//
//                                new Communication_addcartnew().execute();
//
//                            }
//                            else
//                            {
//                                Connections();
//                            }
//
//
//                            return super.onSingleTapConfirmed(e);
//                        }
//
//
//
//                    });
//
//                    @Override
//                    public boolean onTouch(View v1, MotionEvent event) {
//
//                        v = v1;
//
//                        gestureDetector.onTouchEvent(event);
//                        return true;
//                    }
//                });
//
////                text_home_addtocart.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////
////                        progressDialog = new ProgressDialog(NewOfferActivity.this);
////                        progressDialog.setMessage("Add to cart..."); // Setting Message
////                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////                        progressDialog.show(); // Display Progress Dialog
////                        progressDialog.setCancelable(false);
////                        progressDialog.show();
////
////                        try {
////                            JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct.getJSONObject((Integer) v.getTag())));
////
////                            str_productcode = String.valueOf(obj_val.getString("productcode"));
////                            str_productname = String.valueOf(obj_val.getString("productname"));
////                            str_mgfcmp = String.valueOf(obj_val.getString("mfgcmp"));
////                            str_mrp = String.valueOf(obj_val.getString("mrp"));
////                            str_image = String.valueOf(obj_val.getString("imageurl"));
////                            str_purch = "1";
////                            str_subtotal = String.valueOf(obj_val.getString("netsale"));
////                            str_netsale = String.valueOf(obj_val.getString("netsale"));
////                            str_expiry = String.valueOf(obj_val.getString("expdate"));
////                            str_packing = String.valueOf(obj_val.getString("packing"));
////
////
////                            int_tag_val = (Integer) v.getTag();
////
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////
////                        new Communication_addcart().execute();
////
////
////                    }
////                });
//
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            return view;
//        }
    }
    public class Communication_productsnew extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

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

            progress_home1.setVisibility(View.INVISIBLE);
            swipeToRefresh_home1.setRefreshing(false);
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))
                {

                }
                else if (result.equalsIgnoreCase("error"))
                {
                }
                else if (result.equalsIgnoreCase("nodata"))
                {
                    array_preoduct11 = new JSONArray();
                    array_preoduct111 = new JSONArray();
                    listview_customadpte1 = new ListviewAdaptercartnew();
                    listview_home_list1.setAdapter(listview_customadpte1);
                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_preoduct11 = new JSONArray();
                            array_preoduct111 = new JSONArray();
                            array_preoduct11 = new JSONArray(result);
                            array_preoduct111 = new JSONArray(result);
                            //  array_preoduct = new JSONArray(array);



                            listview_customadpte1 = new ListviewAdaptercartnew();
                            listview_home_list1.setAdapter(listview_customadpte1);


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

    public class Communication_addcartnew extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(Urlclass.addcart);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("custid", pref.getString("custid",""));
                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("productcode",str_productcode1);
                postDataParams.put("productname",str_productname1);
                postDataParams.put("mfgcmp",str_mgfcmp1);
                postDataParams.put("mrp",str_mrp1);
                postDataParams.put("subtotal",str_subtotal1);
                postDataParams.put("purch",str_purch1);
                postDataParams.put("netsale",str_netsale1);
                postDataParams.put("netsale1",str_netsale11);
                postDataParams.put("note",str_note1);
                postDataParams.put("expdate",str_expiry1);
                postDataParams.put("packing",str_packing1);
                postDataParams.put("imageurl",str_image1);
                postDataParams.put("offers","yes");
                postDataParams.put("offersper",str_offersper1);
                postDataParams.put("stockinhand",str_stockinhand1);
                postDataParams.put("index",int_tag_val1);


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
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent1);

                    SingletonObject.Instance().setstr_updatecart("yes");

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
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
                        JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct11.getJSONObject(int_tag_val1)));

                        obj_val.put("cart","yes");
                        array_preoduct11.put(int_tag_val1,obj_val);
                        listview_customadpte1.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (result.equalsIgnoreCase("stock"))
                {

                    SingletonObject.Instance().setstr_updatecart("no");

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

                    try {
                        JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct11.getJSONObject(int_tag_val1)));

                        obj_val.put("stockinhand","0");
                        array_preoduct11.put(int_tag_val1,obj_val);
                        listview_customadpte1.notifyDataSetChanged();
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

    public class Communication_products extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.product);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("custid", pref.getString("custid",""));
                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("version",String.valueOf(verCode));

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
                else if (result.equalsIgnoreCase("update"))
                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Update App!");
                    builder4.setMessage("You will be need to update your app, Please go on GooglePay and update it");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();


                                    final String appPackageName = getActivity().getPackageName();

                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }

                                }
                            });
                    alert11 = builder4.create();
                    alert11.show();
                }


                else if (result.equalsIgnoreCase("Exception: timeout"))
                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Opps!");
                    builder4.setMessage("Server is timeout, Please refresh");
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
                else if (result.equalsIgnoreCase("nodata"))
                {
                    array_preoduct = new JSONArray();
                    if (listview_home_list.getAdapter() !=null) {
                        listview_customadpte.notifyDataSetChanged();
                    }
                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {

                            JSONArray jsonArray = new JSONArray(result);
                            JSONObject obj_all =new JSONObject(String.valueOf(jsonArray.getJSONObject(0)));
                          //  array_preoduct = new JSONArray(obj_all.getString("array_phrama"));

                            array_phrma= new JSONArray(obj_all.getString("array_phrama"));
                            array_genral= new JSONArray(obj_all.getString("array_general"));
                            array_surgical= new JSONArray(obj_all.getString("array_surgical"));
                            array_vtenary= new JSONArray(obj_all.getString("array_vet"));
                            array_preoduct = new JSONArray();



                          //  listCompleteData = readListFromFile(obj_all.getString("array_phrama"));
                        //    listPerson = new ArrayList<>(listCompleteData.subList(0, 10));


                            if (jsonArray !=null)
                            {
                              //  array_preoduct = new JSONArray(array);

                                if (str_category_select.equalsIgnoreCase("PHRAMA"))
                                {


                                    if (array_phrma.length()>=50)
                                    {
                                    for (int i=0;i<50;i++)
                                    {
                                        array_preoduct.put(array_phrma.getJSONObject(i));
                                    }
                                    }
                                    else
                                    {
                                    array_preoduct = array_phrma;
                                    }
                                }
                                if (str_category_select.equalsIgnoreCase("GENERAL"))
                                {

                                    if (array_genral.length()>=50) {
                                        for (int i = 0; i < 50; i++) {
                                            array_preoduct.put(array_genral.getJSONObject(i));
                                        }
                                    }
                                    else
                                    {
                                        array_preoduct = array_genral;
                                    }
                                }
                                if (str_category_select.equalsIgnoreCase("SURGICAL"))
                                {

                                    if (array_surgical.length()>=50) {
                                        for (int i = 0; i < 50; i++) {
                                            array_preoduct.put(array_surgical.getJSONObject(i));
                                        }
                                    }
                                    else
                                    {
                                        array_preoduct = array_surgical;
                                    }
                                }
                                if (str_category_select.equalsIgnoreCase("VETERINARY"))
                                {

                                    if (array_vtenary.length()>=50) {
                                        for (int i = 0; i < 50; i++) {
                                            array_preoduct.put(array_vtenary.getJSONObject(i));
                                        }
                                    }
                                    else
                                    {
                                        array_preoduct = array_vtenary;
                                    }
                                }

                                SingletonObject.Instance().setArray_Addcart(array_preoduct);
//                                if (str_loaddata.equalsIgnoreCase("no") && array_preoduct.length() !=0)
//                                {
//                                    str_loaddata="yes";
//                                    listview_customadpte = new ListviewAdaptercart();
//                                    listview_home_list.setAdapter(listview_customadpte);

                                    listview_home_list.setSmoothScrollbarEnabled(true);

//                                    setListViewFooter();
//                                    setListOnScrollListener();
//                                }
//                                else {

                                    listview_customadpte.notifyDataSetChanged();
//                                }


//                                adapter_home = new ListHomeAdapter();
//                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//                                recyclerView_home.setLayoutManager(mLayoutManager);
//                                recyclerView_home.setItemAnimator(new DefaultItemAnimator());
//                                recyclerView_home.setAdapter(adapter_home);


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
                postDataParams.put("expdate",str_expiry);
                postDataParams.put("packing",str_packing);
                postDataParams.put("imageurl",str_image);
                postDataParams.put("offers","no");
                postDataParams.put("note","");
                postDataParams.put("offersper","0");
                postDataParams.put("stockinhand",str_stockinhand);


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

                if (result.equalsIgnoreCase("Connection reset"))
                {

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Oops!");
                    builder4.setMessage("Connection is reset, Please try again.");
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

                else if (result.equalsIgnoreCase("Exception: Timeout"))
                {

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Oops!");
                    builder4.setMessage("Connection is timeout, Please try again.");
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
                else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("added"))
                {
                    Intent intent1 = new Intent("updatebuge1");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent1);
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Successful!");
                    builder4.setMessage("Your item added to list");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    int int_countitem = Integer.parseInt(SingletonObject.Instance().getstr_count());
                                    int_countitem = int_countitem + 1 ;
                                    SingletonObject.Instance().setstr_count(String.valueOf(int_countitem));
                                    SingletonObject.Instance().setstr_updatecart("yes");


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
                        String str_product=obj_val.getString("productcode");
                        for(int i =0 ;i<array_phrma.length();i++)
                        {
                            JSONObject objpharama = new JSONObject(String.valueOf(array_phrma.getJSONObject(i)));

                            if (objpharama.getString("productcode").equalsIgnoreCase(str_product))
                            {
                                objpharama.put("cart","yes");
                                array_phrma.put(i,objpharama);
                            }
                        }


                        for(int i =0 ;i<array_genral.length();i++)
                        {
                            JSONObject objpharama = new JSONObject(String.valueOf(array_genral.getJSONObject(i)));

                            if (objpharama.getString("productcode").equalsIgnoreCase(str_product))
                            {
                                objpharama.put("cart","yes");
                                array_genral.put(i,objpharama);
                            }
                        }


                        for(int i =0 ;i<array_surgical.length();i++)
                        {
                            JSONObject objpharama = new JSONObject(String.valueOf(array_surgical.getJSONObject(i)));

                            if (objpharama.getString("productcode").equalsIgnoreCase(str_product))
                            {
                                objpharama.put("cart","yes");
                                array_surgical.put(i,objpharama);
                            }
                        }

                        for(int i =0 ;i<array_vtenary.length();i++)
                        {
                            JSONObject objpharama = new JSONObject(String.valueOf(array_vtenary.getJSONObject(i)));

                            if (objpharama.getString("productcode").equalsIgnoreCase(str_product))
                            {
                                objpharama.put("cart","yes");
                                array_vtenary.put(i,objpharama);
                            }
                        }


                        SingletonObject.Instance().setArray_Addcart(array_preoduct);
                        listview_customadpte.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (result.equalsIgnoreCase("stock"))
                {
                    SingletonObject.Instance().setstr_updatecart("no");

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



                    try {
                        JSONObject obj_val = new JSONObject(String.valueOf(array_preoduct.getJSONObject(int_tag_val)));

                        obj_val.put("stockinhand","0");
                        array_preoduct.put(int_tag_val,obj_val);
                        String str_product=obj_val.getString("productcode");
                        for(int i =0 ;i<array_phrma.length();i++)
                        {
                            JSONObject objpharama = new JSONObject(String.valueOf(array_phrma.getJSONObject(i)));

                            if (objpharama.getString("productcode").equalsIgnoreCase(str_product))
                            {
                                objpharama.put("stockinhand","0");
                                array_phrma.put(i,objpharama);
                            }
                        }


                        for(int i =0 ;i<array_genral.length();i++)
                        {
                            JSONObject objpharama = new JSONObject(String.valueOf(array_genral.getJSONObject(i)));

                            if (objpharama.getString("productcode").equalsIgnoreCase(str_product))
                            {
                                objpharama.put("stockinhand","0");
                                array_genral.put(i,objpharama);
                            }
                        }


                        for(int i =0 ;i<array_surgical.length();i++)
                        {
                            JSONObject objpharama = new JSONObject(String.valueOf(array_surgical.getJSONObject(i)));

                            if (objpharama.getString("productcode").equalsIgnoreCase(str_product))
                            {
                                objpharama.put("stockinhand","0");
                                array_surgical.put(i,objpharama);
                            }
                        }

                        for(int i =0 ;i<array_vtenary.length();i++)
                        {
                            JSONObject objpharama = new JSONObject(String.valueOf(array_vtenary.getJSONObject(i)));

                            if (objpharama.getString("productcode").equalsIgnoreCase(str_product))
                            {
                                objpharama.put("stockinhand","0");
                                array_vtenary.put(i,objpharama);
                            }
                        }



                        SingletonObject.Instance().setArray_Addcart(array_preoduct);
                        listview_customadpte.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else if (result.equalsIgnoreCase("already"))
                {

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Item is exist!");
                    builder4.setMessage("You has been already this item added into cart list");
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

                }
            }
            else
            {


            }

            if (progressDialog !=null) {
                progressDialog.dismiss();
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
    private BroadcastReceiver homeupdate1 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (array_preoduct.length() == 0)
            {
                progress_home.setVisibility(View.VISIBLE);
            }
            new Communication_products().execute();


        }
    };

    private BroadcastReceiver homeupdate2 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (array_preoduct.length() != 0)
            {
                String str_productcode = intent.getStringExtra("productcode");
                Log.d("prroduct id ",str_productcode);
                try {
                for (int i =0;i<array_preoduct.length();i++)
                {

                        JSONObject obj = new JSONObject(String.valueOf(array_preoduct.getJSONObject(i)));

                        if (obj.getString("productcode").equalsIgnoreCase(str_productcode))
                        {
                            obj.put("cart", "no");
                            array_preoduct.put(i, obj);
                            listview_customadpte.notifyDataSetChanged();
                        }


                }

                for(int i =0 ;i<array_phrma.length();i++)
                {
                    JSONObject objpharama = new JSONObject(String.valueOf(array_phrma.getJSONObject(i)));

                    if (objpharama.getString("productcode").equalsIgnoreCase(str_productcode))
                    {
                        objpharama.put("cart","no");
                        array_phrma.put(i,objpharama);
                    }
                }


                for(int i =0 ;i<array_genral.length();i++)
                {
                    JSONObject objpharama = new JSONObject(String.valueOf(array_genral.getJSONObject(i)));

                    if (objpharama.getString("productcode").equalsIgnoreCase(str_productcode))
                    {
                        objpharama.put("cart","no");
                        array_genral.put(i,objpharama);
                    }
                }


                for(int i =0 ;i<array_surgical.length();i++)
                {
                    JSONObject objpharama = new JSONObject(String.valueOf(array_surgical.getJSONObject(i)));

                    if (objpharama.getString("productcode").equalsIgnoreCase(str_productcode))
                    {
                        objpharama.put("cart","no");
                        array_surgical.put(i,objpharama);
                    }
                }

                    for(int i =0 ;i<array_vtenary.length();i++)
                    {
                        JSONObject objpharama = new JSONObject(String.valueOf(array_vtenary.getJSONObject(i)));

                        if (objpharama.getString("productcode").equalsIgnoreCase(str_productcode))
                        {
                            objpharama.put("cart","no");
                            array_vtenary.put(i,objpharama);
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



        }
    };


    private BroadcastReceiver homeupdate3 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (array_preoduct.length() != 0)
            {
                String str_productcode = intent.getStringExtra("productcodes");

                ArrayList<String> array_productcode = new ArrayList<String>(Arrays.asList(str_productcode));

                for (int i =0;i<array_preoduct.length();i++)
                {
                    try {
                        JSONObject obj = new JSONObject(String.valueOf(array_preoduct.getJSONObject(i)));
                        for (int j=0;j<array_productcode.size();j++)
                        {
                            String str_productcode1 = array_productcode.get(j);

                            if (str_productcode1.equalsIgnoreCase(str_productcode))
                            {


                                obj.put("cart", "no");
                                array_preoduct.put(i, obj);
                                listview_customadpte.notifyDataSetChanged();
                            }

                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }



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

    private List<Person> readListFromFile(String result){
//        try{
////            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets()
//                    .open("persons.txt")));
           // StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while((line = bufferedReader.readLine()) != null){
//              stringBuilder.append(line);
//            }
            Gson gson = new Gson();
            return gson.fromJson(result, new TypeToken<List<Person>>(){}
                    .getType());
//        }catch (IOException exception){
//            exception.printStackTrace();
//            return new ArrayList<>();
//        }
    }

    private void setListViewFooter(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.footer_listview_progressbar, null);
        progressBar = view.findViewById(R.id.progressBar);
        listview_home_list.addFooterView(progressBar);
    }

    private void setListOnScrollListener(){
        listview_home_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE && listview_home_list.getLastVisiblePosition() ==
                        array_preoduct.length()){
                    if(asyncTaskWait == null || asyncTaskWait.getStatus() != AsyncTask.Status
                            .RUNNING){

                        progressBar.setVisibility(View.VISIBLE);
                        asyncTaskWait = new AsyncTaskWait(new WeakReference<Context>(getActivity()));
                        asyncTaskWait.execute();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });
    }

    private void addMoreItems(){
        int size = array_preoduct.length();
        if (str_searchtext.length()==0) {
            if (str_category_select.equalsIgnoreCase("PHRAMA")) {
                for (int i = 1; i <= 50; i++) {
                    if ((size + i) < array_phrma.length()) {
                        try {
                            array_preoduct.put(array_phrma.getJSONObject(size + i));
                            SingletonObject.Instance().setArray_Addcart(array_preoduct);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
            if (str_category_select.equalsIgnoreCase("GENERAL")) {
                for (int i = 1; i <= 50; i++) {
                    if ((size + i) < array_genral.length()) {
                        try {
                            array_preoduct.put(array_genral.getJSONObject(size + i));
                            SingletonObject.Instance().setArray_Addcart(array_preoduct);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
            if (str_category_select.equalsIgnoreCase("SURGICAL")) {
                for (int i = 1; i <= 50; i++) {
                    if ((size + i) < array_surgical.length()) {
                        try {
                            array_preoduct.put(array_surgical.getJSONObject(size + i));
                            SingletonObject.Instance().setArray_Addcart(array_preoduct);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

            if (str_category_select.equalsIgnoreCase("VETERINARY"))
            {
                for (int i = 1; i <= 50; i++) {
                    if ((size + i) < array_vtenary.length()) {
                        try {
                            array_preoduct.put(array_vtenary.getJSONObject(size + i));
                            SingletonObject.Instance().setArray_Addcart(array_preoduct);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

            //VETERINARY


            listview_customadpte.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }

    }

//    public class ListHomeAdapter extends RecyclerView.Adapter<ListHomeAdapter.MyViewHolder> {
//
////        private JSONArray moviesList;
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//
//            public ImageView image_tab ;
//            public TextView text_tab_name ;
//            public TextView text_mgfname;
//
//            public TextView text_tab_mrp ;
//            public TextView text_tab_exp ;
//            public TextView  text_tab_salerate ;
//            public TextView  text_tab_salerate2;
//            public TextView  text_tab_salerate3;
//            public TextView  text_tab_pkg ;
//            public TextView  text_AvailabelStock;
//            public TextView  text_home_addtocart;
//            public TextView  text_offer_per;
//            public TextView text_offer_note;
//
//            public MyViewHolder(View view) {
//                super(view);
//                text_tab_name = (TextView) view.findViewById(R.id.text_productname);
//
//            }
//        }
//
//
//        public ListHomeAdapter() {
//
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//           // View itemView = LayoutInflater.from(parent.getContext())
//                  //  .inflate(R.layout.listrow_home, parent, null);
//            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.listrow_home, null);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position) {
//
//            holder.text_tab_name.setText("sdsdds");
//        }
//
//        @Override
//        public int getItemCount() {
//            return array_preoduct.length();
//        }
//    }

}
