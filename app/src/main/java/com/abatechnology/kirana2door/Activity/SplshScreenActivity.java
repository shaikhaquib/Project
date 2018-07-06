package com.abatechnology.kirana2door.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.abatechnology.kirana2door.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplshScreenActivity extends Activity {


    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splsh_screen);
        View parentLayout = findViewById(android.R.id.content);
        Timer t = new Timer();
        boolean checkConnection=new ApplicationUtility().checkConnection(this);
        if (checkConnection) {
            t.schedule(new splash(), 3000);
        } else {
            Snackbar snackbar = Snackbar.make(parentLayout, "Connection not found...plz check Internet Connection!", Snackbar.LENGTH_LONG);

            snackbar.show();
            //Toast.makeText(SplshScreenActivity.this,"connection not found...plz check connection", 3000).show();
        }
    }

    class splash extends TimerTask {

        @Override
        public void run() {

                Intent i = new Intent(SplshScreenActivity.this,LoginActivity.class);
                finish();
                startActivity(i);



        }

    }



}