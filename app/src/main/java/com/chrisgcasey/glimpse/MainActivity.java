package com.chrisgcasey.glimpse;
import android.app.ActionBar;
import android.app.FragmentTransaction;
//import android.support.v7.app.ActionBar;


import android.os.Bundle;

import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.Window;

import com.parse.ParseUser;




public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    TabPagerAdapter mTabPagerAdapter;
    ViewPager mViewPager;
    ActionBar actionBar;


    public String[] tabNames = new String[]{"tab1", "tab2",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        mTabPagerAdapter = new TabPagerAdapter(this, getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mTabPagerAdapter);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null) {
            navigateToLogin();
        }
        actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        setUpPageListener();
        for(int i = 0; i < mTabPagerAdapter.getCount(); i++){
            actionBar.addTab(actionBar.newTab().setText(tabNames[i]).setTabListener(this));
        }
    }




    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setUpPageListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
           actionBar.setSelectedNavigationItem(position);
        }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            ParseUser.logOut();
            navigateToLogin();
            return true;
        }
        else if (id == R.id.action_edit_friends){
            Intent intent = new Intent(this, EditFriends.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
