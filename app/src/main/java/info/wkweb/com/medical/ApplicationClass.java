package info.wkweb.com.medical;

import android.app.Application;

import com.msg91.sendotpandroid.library.internal.SendOTP;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
       // SendOTP.initializeApp(this);
        SendOTP.initializeApp(this,"info.wkweb.com.medical");
    }
}
