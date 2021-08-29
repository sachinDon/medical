package info.wkweb.com.medical;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {

    String str_name,str_mobilenumber;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView pro_paymentdetail,profo_logo,pro_name,pro_editprofile,pro_dtailsapp,pro_Cahngepass,pro_DeleteAccount;


    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        profo_logo = (TextView)view.findViewById(R.id.profo_logo);
        pro_name = (TextView)view.findViewById(R.id.pro_name);
        pro_editprofile = (TextView)view.findViewById(R.id.pro_editprofile);
        pro_Cahngepass = (TextView)view.findViewById(R.id.pro_Cahngepass);
        pro_paymentdetail = (TextView)view.findViewById(R.id.pro_paymentdetail);
        pro_DeleteAccount = (TextView)view.findViewById(R.id.pro_DeleteAccount);
        pro_dtailsapp = (TextView)view.findViewById(R.id.pro_dtailsapp);
        str_name = "";
        str_mobilenumber = "";
        Log.d("ccccnmae",pref.getString("custname",""));
        pro_name.setText(pref.getString("custname",""));
        profo_logo.setText(pref.getString("custname","").substring(0,1));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(Update_profile,
                new IntentFilter("updateprofile"));



        PackageInfo info;
        try {
            info = getActivity().getPackageManager().getPackageInfo("info.wkweb.com.medical", 0);

            pro_dtailsapp.setText("Pritama Medicals: "+"V"+String.valueOf(info.versionName)+"."+String.valueOf(info.versionCode));

        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        }  catch (Exception e) {
            Log.e("exception", e.toString());
        }

        pro_editprofile.setOnTouchListener(new View.OnTouchListener()
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


                    SignUpActivity.str_viewpage="edit";
                    Intent intents = new Intent(getActivity(),SignUpActivity.class);
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

//        pro_editprofile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                SignUpActivity.str_viewpage="edit";
//                Intent intents = new Intent(getActivity(),SignUpActivity.class);
//                startActivity(intents);
//            }
//        });
        pro_paymentdetail.setOnTouchListener(new View.OnTouchListener()
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


                    Intent intens = new Intent(getActivity(),OnlineActivity.class);
                    startActivity(intens);

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
        pro_Cahngepass.setOnTouchListener(new View.OnTouchListener()
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


                    Intent intens = new Intent(getActivity(),ChangePasswordActivity.class);
                    startActivity(intens);

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
//        pro_Cahngepass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intens = new Intent(getActivity(),ChangePasswordActivity.class);
//                startActivity(intens);
//            }
//        });

        pro_DeleteAccount.setOnTouchListener(new View.OnTouchListener()
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


                    editor.putString("login","no");
                    editor.putString("address","no");


                    editor.commit();
                    Intent intent = new Intent(getActivity(),MainActivity.class);
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
//        pro_DeleteAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                        editor.putString("login","no");
//        editor.putString("address","no");
//
//
//        editor.commit();
//        Intent intent = new Intent(getActivity(),MainActivity.class);
//        startActivity(intent);
//            }
//        });

        return view;


    }
    private BroadcastReceiver Update_profile = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            pro_name.setText(pref.getString("custname",""));
            profo_logo.setText(pref.getString("custname","").substring(0,1));

        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void onBackPressed() {

    }

}
