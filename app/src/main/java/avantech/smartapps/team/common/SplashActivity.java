package avantech.smartapps.team.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.FirebaseApp;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.AdminHomeActivity;
import avantech.smartapps.team.staff.StaffHomeActivity;

public class SplashActivity extends AppCompatActivity {
    private Context context;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);
        context = SplashActivity.this;
        sharedPreferences = new SharedPreferences(context);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getIS_LOGGED_IN()) {
                    //if already login then open the dashboard
                    //check if it is supervisor or worker
                    if(sharedPreferences.getTYPE().contains("admin")) {
                        Log.d("user type in sp",sharedPreferences.getTYPE());
                        Log.d("user name in sp",sharedPreferences.getNAME());
                        Intent intent = new Intent(context, AdminHomeActivity.class);
                        finish();
                        startActivity(intent);
                    }
                    else {
                        Log.d("user type in sp",sharedPreferences.getTYPE());
                        Intent intent = new Intent(context, StaffHomeActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }
                else {
                    Intent intent = new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
}