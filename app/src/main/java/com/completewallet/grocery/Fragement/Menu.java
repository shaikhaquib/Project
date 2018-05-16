package com.completewallet.grocery.Fragement;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.completewallet.grocery.Activity.AboutUs;
import com.completewallet.grocery.Activity.AccountUpdate;
import com.completewallet.grocery.Activity.ChangePassword;
import com.completewallet.grocery.R;
import com.completewallet.grocery.SessionManager;

/**
 * Created by Shaikh Aquib on 10-Apr-18.
 */

public class Menu extends Fragment {

    TextView login ,logout,chpassword,acc,about;
    SessionManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.usermenu, container, false);



        final CollapsingToolbarLayout collapsingToolbarLayout =  rootview.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.trans)); // transperent color = #00000000
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorWhite));


        login=rootview.findViewById(R.id.menuLogin);
        logout=rootview.findViewById(R.id.menuLogout);
        chpassword=rootview.findViewById(R.id.changepass);
        acc=rootview.findViewById(R.id.acc);
        about=rootview.findViewById(R.id.about);
        manager=new SessionManager(getActivity());

        if (manager.isSkip()){
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }else {
            login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
        }
        chpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);
            }
        });
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountUpdate.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUs.class);
                startActivity(intent);
            }
        });

        return  rootview;
    }

}


