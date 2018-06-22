package com.completewallet.grocery.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.completewallet.grocery.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.kiranalogo1)
                .setDescription("We are glad to announce that we are coming up with new organization, an online grocery store & we need your valuable support & co operation.\n"+
                        "From our busy schedule, we have no time to buy our daily needs grocery items like snack ,cold drink, vegetables, pulses, cosmetics, detergents & room freshener & other grocery items.\n"+
                        "At this point of time, the technology made our life as easy life that by seating at home, office, travelling we can order our requirement by just one click.\n"+
                        "To make your life easy, we are coming up with new e commerce website www.kirana2door.com & android app through which you can order your required items & we will deliver it to your address.\n"+
                        "WITHOUT ANY EXTRA DELIVERY CHARGES.\n"+
                        "At Kirana2Door we deliver the freshest groceries directly to your doorstep! You can choose your favourite groceries anytime at the comfort of your cozy home. With stringent quality checks and chilled transportation, your groceries will arrive fresh and ready to be cooked.")
                .addItem(new Element().setTitle("Address : 29, Dwarkamai building no. 7/A, gurusharnam complex CHS Ltd. visruli naka, Old Panvel, Navi Mumbai, India 410206."))
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Connect with us")
                .addEmail("info@kiran2door.com","Kirana2Door")
                .addWebsite("http://kirana2door.com/")
                .addFacebook("CKT-168084223695284")
                .addTwitter("Kirana2D")
                .addYoutube("UC5YaiC_AvMioiWSQpUIbMVA")
                .addInstagram("kirana2door")
                .create();

        setContentView(aboutPage);
    }
}
