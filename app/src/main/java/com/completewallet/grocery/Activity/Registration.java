package com.completewallet.grocery.Activity;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.completewallet.grocery.R;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Registration extends AppCompatActivity {

    LinearLayout mobileView , Regform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();



        MaterialSpinner spinner1 =  findViewById(R.id.Circle);
        MaterialSpinner spinner =  findViewById(R.id.Operator);
        String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner1.setAdapter(adapter);

        intialize();
    }

    private void intialize() {
        mobileView=findViewById(R.id.mobileView);
        Regform=findViewById(R.id.registration);
    }

    public void getOtp(View view) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.confimotp, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Registration.this);
        alertDialogBuilderUserInput.setView(mView);

       PinEntryEditText pinEntry = mView.findViewById(R.id.otp);
       ProgressBar OtpProgress=mView.findViewById(R.id.otpProgresss);
        final Button VrOtp=mView.findViewById(R.id.vrotp);
        final TextView Resend=mView.findViewById(R.id.resendotp);



        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

        VrOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobileView.setVisibility(View.GONE);
                Regform.setVisibility(View.VISIBLE);
                alertDialogAndroid.dismiss();

            }
        });




        alertDialogAndroid.show();

    }
}
