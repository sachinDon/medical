package info.wkweb.com.medical;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class InquiryFragment extends Fragment {

    public  static Uri uriImage=null;
    private Bitmap bitmap;
    private Uri file;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ImageView image_inq_upload ;
    TextView text_inq_submit,text_inq_mreqtype ;
    EditText edit_genricname ,edit_inq_mfg,edit_inq_qty ,edit_inq_note;
    String str_selcttype,str_encodedImg;
    ProgressDialog progressDialog;
    AlertDialog alert11;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    public InquiryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_inquiry, container, false);

        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
         image_inq_upload = (ImageView) view.findViewById(R.id.image_inq_upload);

         text_inq_submit = (TextView) view.findViewById(R.id.text_inq_submit);
         text_inq_mreqtype = (TextView) view.findViewById(R.id.text_inq_mreqtype);

         edit_genricname = (EditText) view.findViewById(R.id.edit_genricname);
         edit_inq_mfg = (EditText) view.findViewById(R.id.edit_inq_mfg);
         edit_inq_qty = (EditText) view.findViewById(R.id.edit_inq_qty);
         edit_inq_note = (EditText) view.findViewById(R.id.edit_inq_note);

        RelativeLayout reletive_inq_select = (RelativeLayout) view.findViewById(R.id.reletive_inq_select);

        text_inq_submit.setEnabled(false);
        text_inq_submit.setTextColor(Color.WHITE);
        text_inq_submit.setBackgroundResource(R.drawable.roundwhite);

        str_selcttype ="";
        str_encodedImg="";
        image_inq_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//                    ActivityCompat.requestPermissions(getActivity(), new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
//                }
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    selectImage();
                }
                else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

            }
        });

        reletive_inq_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu menu = new PopupMenu(getActivity(), v);

                menu.getMenu().add("Stock");
                menu.getMenu().add("Order");
                menu.getMenu().add("Stock & Order");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        text_inq_mreqtype.setText(String.valueOf(item));
                       str_selcttype = String.valueOf(item);
                        checkRegister();




                        return false;
                    }
                });

                menu.show();
            }
        });

        edit_genricname.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_inq_mfg.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_inq_qty.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_inq_note.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        text_inq_submit.setOnTouchListener(new View.OnTouchListener()
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
                        progressDialog.setMessage("Uploading..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        new Communication_addprescription().execute();
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


//        text_inq_submit.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Uploading..."); // Setting Message
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show(); // Display Progress Dialog
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        new Communication_addprescription().execute();
//    }
//});
        return view;
    }




    public  void checkRegister()
    {
        if (edit_genricname.getText().length() !=0 || str_encodedImg.length() !=0)
        {
            text_inq_submit.setEnabled(true);
            text_inq_submit.setTextColor(getResources().getColor(R.color.colorwhite));
            text_inq_submit.setBackgroundResource(R.drawable.round_geen);
        }
        else
        {
            text_inq_submit.setEnabled(false);
            text_inq_submit.setTextColor(getResources().getColor(R.color.colorblack));
            text_inq_submit.setBackgroundResource(R.drawable.roundwhite);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {


                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file = Uri.fromFile(getOutputMediaFile());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                    startActivityForResult(intent, 100);
                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 200);



                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                //ProfileImg.setImageBitmap(loadBitmap(String.valueOf(file)));

                if (file != null) {

                    bitmap = loadBitmap(String.valueOf(file));

                    Handler refresh = new Handler(Looper.getMainLooper());
                    refresh.post(new Runnable() {
                        public void run() {



                            if (bitmap.getHeight()>=500 && bitmap.getWidth()>=500)
                            {
                                bitmap=Bitmap.createScaledBitmap(bitmap, 500, 500, false);

                            }
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);


                            image_inq_upload.setImageBitmap(bitmap);

                            byte[] imageBytes = bytes.toByteArray();
                            str_encodedImg = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            checkRegister();

                        }
                    });


                }
            }
        }
        else if (requestCode == 200)
        {


            if (data !=null) {

                Uri selectedImage = data.getData();
                try {

                    editor.putString("select_photo", "no");
                    editor.commit();

                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                    Handler refresh = new Handler(Looper.getMainLooper());
                    refresh.post(new Runnable() {
                        public void run() {

                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                            if (bitmap.getHeight()>=500 && bitmap.getWidth()>=500)
                            {
                                bitmap=Bitmap.createScaledBitmap(bitmap, 500, 500, false);

                            }
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
                            image_inq_upload.setImageBitmap(bitmap);
                            byte[] imageBytes = bytes.toByteArray();

                            str_encodedImg = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            checkRegister();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        }

    }
    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return bm;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {


            }
        }
    }

    public class Communication_addprescription extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(Urlclass.prescription);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("custid", pref.getString("custid",""));
                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("custname", pref.getString("custname",""));
                postDataParams.put("productname",edit_genricname.getText());
                postDataParams.put("mfgcmp",edit_inq_mfg.getText());
                postDataParams.put("qty",edit_inq_qty.getText());
                postDataParams.put("note",edit_inq_note.getText());
                postDataParams.put("reqtype",str_selcttype);
                postDataParams.put("image",str_encodedImg);


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

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Opps!");
                    builder4.setMessage("Could not retrieve details, Please try again.");
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
                else if (result.equalsIgnoreCase("inserterror"))
                {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Opps!");
                    builder4.setMessage("Your details has not added successfuly, Please try again");
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
                else if (result.equalsIgnoreCase("added"))
                {

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Successful!");
                    builder4.setMessage("Your details has been added successfuly");
                    builder4.setCancelable(false);
                    builder4.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    bitmap=null;
                                    image_inq_upload.setImageResource(0);
                                    image_inq_upload.setBackgroundResource(R.drawable.round_green1);


                                    edit_genricname.getText().clear();
                                    edit_inq_mfg.getText().clear();
                                    edit_inq_qty.getText().clear();
                                    edit_inq_note.getText().clear();
                                    text_inq_mreqtype.setText("Select");
                                    str_encodedImg ="";
                                    str_selcttype="";

                                    text_inq_submit.setEnabled(false);
                                    text_inq_submit.setTextColor(Color.WHITE);
                                    text_inq_submit.setBackgroundResource(R.drawable.roundwhite);

                                }
                            });
                    alert11 = builder4.create();
                    alert11.show();


                }
                else
                {

                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                    builder4.setTitle("Opps!");
                    builder4.setMessage("Server could not found , Please try again");
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
                AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
                builder4.setTitle("Opps!");
                builder4.setMessage("Server could not found , Please try again");
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
    public void onBackPressed() {

    }
}
