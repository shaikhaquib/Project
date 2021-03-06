package com.abatechnology.kirana2door.Fragement;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abatechnology.kirana2door.Activity.AboutUs;
import com.abatechnology.kirana2door.Activity.AccountUpdate;
import com.abatechnology.kirana2door.Activity.ChangePassword;
import com.abatechnology.kirana2door.Activity.LoginActivity;
import com.abatechnology.kirana2door.Activity.OrderHistoryActivity;
import com.abatechnology.kirana2door.R;
import com.abatechnology.kirana2door.SessionManager;

/**
 * Created by Shaikh Aquib on 10-Apr-18.
 */

public class Menu extends Fragment {

    TextView login ,logout,chpassword,acc,about,ohis,menuLogout;
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


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.setLogin(false);
                manager.setSkip(false);
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });

        chpassword=rootview.findViewById(R.id.changepass);
        acc=rootview.findViewById(R.id.acc);
        about=rootview.findViewById(R.id.about);
        menuLogout=rootview.findViewById(R.id.menuLogout);
        ohis=rootview.findViewById(R.id.ohis);
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
                if (!manager.isLoggedIn()){  AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity());
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }
                    builder.setTitle("Sorry ! please login first")
                            .setMessage("This feature not available for guest user")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    //SessionManager
                                    //context.startActivity(new Intent(context,LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                }
                            })
                            .show();}else {
                    Intent intent = new Intent(getActivity(), ChangePassword.class);
                    startActivity(intent);
            }

            }
        });
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!manager.isLoggedIn()){  AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity());
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }
                    builder.setTitle("Sorry ! please login first")
                            .setMessage("This feature not available for guest user")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    //SessionManager
                                    //context.startActivity(new Intent(context,LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                }
                            })
                            .show();}else {
                Intent intent = new Intent(getActivity(), AccountUpdate.class);
                startActivity(intent);}
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUs.class);
                startActivity(intent);
            }
        });
        ohis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!manager.isLoggedIn()){  AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity());
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }
                    builder.setTitle("Sorry ! please login first")
                            .setMessage("This feature not available for guest user")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    //SessionManager
                                    //context.startActivity(new Intent(context,LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                }
                            })
                            .show();}else {
                    Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                    startActivity(intent);}

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.setLogin(false);
                manager.setSkip(false);
                SharedPreferences login = getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor deditor = login.edit();
                deditor.clear();
                deditor.commit();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });
        menuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.setLogin(false);
                manager.setSkip(false);
                SharedPreferences login = getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor deditor = login.edit();
                deditor.clear();
                deditor.commit();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });
        return  rootview;
    }

}


