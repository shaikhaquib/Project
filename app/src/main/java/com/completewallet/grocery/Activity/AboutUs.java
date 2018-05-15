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
                .setDescription("Established under the sprawling business umbrella of Pune based APA Group of Companies; Veg Mart spearheads the organic lifestyle industry. Founded in 2014 by ‘Technopreneur’ and agro-enthusiast, Mr. Ashok Patil; the brand has evolved to become a sustainable lifestyle solution and lives up to its commitment of providing certified, eco-friendly, healthy and organic dietary options at affordable prices. Most importantly Veg Mart’s key goals are to empower farmers, retain sustaianble environment, enable consumer health and respect Indian farming culture.\n" +
                        "\n" +
                        "Currently supplying GAP (Good Agricultural Practices) and organic certified farm produce through its HO in Pune and liaising office in Mumbai, the brand has also mapped its international presence with representatives in UK, Europe, UAE & Singapore. Operating on a franchisee model, the brand accepts and delivers orders through its mobile app and franchisee outlets with free home delivery by establishing successful backward and forward integration of the entire agro supply chain.\n" +
                        "\n" +
                        "Offering a wide array of organic food choices in perishable and non-perishable items, its product portfolio encompasses of fresh fruits, vegetables, pulses, grains, flour, spices and dry fruits. Under its sub-brand ‘Keshar’, Veg Mart offers A2 milk and dairy products whereas ‘Fresh Fusion’ offers fruits and vegetable juices. Ensuring, quality and highest standards in hygiene, the company operates independently through its self-owned orchards and plantations in collaboration with organic farming groups as strategic partners. Company also has APEDA approved cold storage, processing and packaging units. Promoting environmentally conscious farming that aids in consumer health as well as farmers’ profits, the company has aligned its business strategy with over 500 certified organic farmers who together own over 1000 acres of land.")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Connect with us")
                .addEmail("pbhushan246@gmail.com","Kirana2Door")
                .addWebsite("http://kirana2door.com/")
                .addFacebook("CKT-168084223695284")
                .addTwitter("Bhushan_Patil__")
                .addYoutube("UC5YaiC_AvMioiWSQpUIbMVA")
                .addInstagram("bhushan_patil____")
                .addGitHub("Bhushan-CPatil")
                .create();

        setContentView(aboutPage);
    }
}
