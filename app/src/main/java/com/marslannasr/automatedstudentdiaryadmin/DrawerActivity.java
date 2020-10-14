package com.marslannasr.automatedstudentdiaryadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    android.support.v4.app.Fragment selectedFragment;
    FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_manageStud:
                    selectedFragment = new StudentManagFragment();
                    break;
                case R.id.navigation_manageTeac:
                    selectedFragment = new TeacherManagFragment();
                    //Toast.makeText(DrawerActivity.this, "Navigation", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.navigation_manageTimetable:
                    selectedFragment = new TimeTableFragment();
                    //Toast.makeText(DrawerActivity.this, "Navigation", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.navigation_manageSubject:
                    selectedFragment = new SubjectManagFragment();
                    //Toast.makeText(DrawerActivity.this, "Navigation", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.navigation_manageSchedule:
                    selectedFragment = new ScheduleManagFragment();
                    //Toast.makeText(DrawerActivity.this, "Navigation", Toast.LENGTH_SHORT).show();
                    break;



            }


            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new StudentManagFragment());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.drawer, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(DrawerActivity.this,StudentViewDataActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(DrawerActivity.this,TeacherViewDataActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(DrawerActivity.this,TimeTableViewActivity.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(DrawerActivity.this,SubjectDataViewActivity.class));
        } else if (id == R.id.nav_schedule) {
            startActivity(new Intent(DrawerActivity.this,ScheduleDataViewActivity.class));
        } else if (id == R.id.nav_send) {


            if(mAuth !=null){

                mAuth.signOut();
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DrawerActivity.this,MainActivity.class));
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
