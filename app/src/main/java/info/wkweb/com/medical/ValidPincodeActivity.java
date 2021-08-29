package info.wkweb.com.medical;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

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

public class ValidPincodeActivity extends AppCompatActivity {

    JSONArray array_areapincode,array_areapincode1;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
      ListviewAdaptercart listview_customadpte1;
    ProgressBar progress_vloc;
    AlertDialog alert11;
    ListView listview_home_list;
    TextView text_back;
    SearchView searchbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid_pincode);


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        progress_vloc = (ProgressBar)findViewById(R.id.progress_validloc);
        listview_home_list=(ListView)findViewById(R.id.listview_home_list);
        text_back=(TextView) findViewById(R.id.text_back_vloc);
        listview_home_list =(ListView) findViewById(R.id.listview_validloclist);
        searchbar = (SearchView) findViewById(R.id.searchView_vloc);
        array_areapincode = new JSONArray();
        array_areapincode1 = new JSONArray();

        progress_vloc.setVisibility(View.VISIBLE);

        text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
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


                array_areapincode = new JSONArray();
                if (array_areapincode1 != null)
                {
                    if (TextUtils.isEmpty(newText.toString()))
                    {
                        array_areapincode = array_areapincode1;

                    } else {

                        for (int i = 0; i < array_areapincode1.length(); i++) {

                            try {
                                String string = array_areapincode1.getJSONObject(i).getString("area");
                                String str_category = array_areapincode1.getJSONObject(i).getString("pincode");
                                // str_search_txt = newText;
                                if ((string.toLowerCase()).contains(newText.toLowerCase()))
                                {


                                    array_areapincode.put(array_areapincode1.getJSONObject(i));



                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    }
                    if (array_areapincode != null) {

                        listview_customadpte1 = new ListviewAdaptercart();
                        listview_home_list.setAdapter(listview_customadpte1);
                    }


                }
                else
                {
                    listview_customadpte1 = new ListviewAdaptercart();
                    listview_home_list.setAdapter(listview_customadpte1);
                }
                return false;
            }
        });

        new Communication_addcart().execute();
    }

    private class ListviewAdaptercart extends BaseAdapter {
        private LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return array_areapincode.length();
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

            view = inflater.inflate(R.layout.list_valid_locations, null,false);

            TextView text_vloc_locno  = (TextView) view.findViewById(R.id.text_vloc_locno);
            TextView text_loc_name = (TextView) view.findViewById(R.id.text_vloc_locname);
            TextView text_pincode = (TextView) view.findViewById(R.id.text_vloc_pincode);
            final JSONObject object_values;
            try {
                object_values = new JSONObject(String.valueOf(array_areapincode.getJSONObject(i)));

                text_loc_name.setText(String.valueOf(object_values.getString("area")));
                text_pincode.setText(String.valueOf(object_values.getString("pincode")));
                text_vloc_locno.setText(String.valueOf(i+1));


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return view;
        }
    }
    public class Communication_addcart extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.getareapincode);

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


            progress_vloc.setVisibility(View.INVISIBLE);


            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("nodata"))

                {


                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=20) {
                        try {
                            array_areapincode = new JSONArray(result);
                            array_areapincode1 = new JSONArray(result);
                            if (array_areapincode !=null) {


                                listview_customadpte1 = new ListviewAdaptercart();
                                listview_home_list.setAdapter(listview_customadpte1);
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
    public void onBackPressed() {

    }
}
