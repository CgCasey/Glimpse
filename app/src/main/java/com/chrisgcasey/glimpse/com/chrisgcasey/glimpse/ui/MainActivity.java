package com.chrisgcasey.glimpse.com.chrisgcasey.glimpse.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
//import android.support.v7.app.ActionBar;


import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.Window;
import android.widget.Toast;

import com.chrisgcasey.glimpse.ParseConstants;
import com.chrisgcasey.glimpse.R;
import com.chrisgcasey.glimpse.TabPagerAdapter;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    TabPagerAdapter mTabPagerAdapter;
    ViewPager mViewPager;
    ActionBar actionBar;

    private static final String TAG = MainActivity.class.getSimpleName();

    private String[] tabNames = new String[]{"Inbox", "Friends",};
    private int[] tabIcons = new int[]{R.drawable.ic_tab_inbox, R.drawable.ic_tab_friends};
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int CHOOSE_PHOTO_REQUEST = 2;
    public static final int CHOOSE_VIDEO_REQUEST = 3;
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    public static final int FILE_SIZE_LIMIT = 1024 * 1024 * 10; //convert to MBs

    private Uri mMediaUri;

    private static Uri getOutPutMediaFileUri(int mediaType) {
        return Uri.fromFile(getOutPutMediaFile(mediaType));
    }


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
        if (currentUser == null) {
            navigateToLogin();
        }
        actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        setUpPageListener();
        for (int i = 0; i < mTabPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setIcon(tabIcons[i]).setTabListener(this));

        }
    }


    public void navigateToLogin() {
        //Toast.makeText(MainActivity.this, getString(R.string.error_file), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setUpPageListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

    }


    private void cameraChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.camera_choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mMediaUri = getOutPutMediaFileUri(MEDIA_TYPE_IMAGE);
                        if (mMediaUri != null) {
                            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                            startActivityForResult(intentCamera, TAKE_PHOTO_REQUEST);
                        } else {
                            //Toast toast = new Toast(MainActivity.this);
                            Toast.makeText(MainActivity.this, getString(R.string.error_general), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        Intent intentPhotoChooser = new Intent(Intent.ACTION_GET_CONTENT);
                        intentPhotoChooser.setType("image/*");
                        startActivityForResult(intentPhotoChooser, CHOOSE_PHOTO_REQUEST);

                        break;
                    case 2:
                        Intent intentVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        mMediaUri = getOutPutMediaFileUri(MEDIA_TYPE_VIDEO);
                        if (mMediaUri != null) {
                            intentVideo.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                            intentVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                            //set video quality to low for parse.com limitations
                            intentVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                            startActivityForResult(intentVideo, TAKE_VIDEO_REQUEST);
                        }
                    case 3:
                        Intent intentVideoChooser = new Intent(Intent.ACTION_GET_CONTENT);
                        intentVideoChooser.setType("video/*");
                        Toast.makeText(MainActivity.this, getString(R.string.video_file_size_warning), Toast.LENGTH_LONG).show();
                        startActivityForResult(intentVideoChooser, CHOOSE_VIDEO_REQUEST);
                        break;


                }
            }
        });
        builder.create().show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //if choice is existing photo or video store uri in mMediaUri member variable
            if (requestCode == CHOOSE_PHOTO_REQUEST || requestCode == CHOOSE_VIDEO_REQUEST) {
                if (data == null) {
                    Toast.makeText(MainActivity.this, R.string.error_general, Toast.LENGTH_LONG).show();
                } else {
                    mMediaUri = data.getData();
                }
                if (requestCode == CHOOSE_VIDEO_REQUEST) {
                    InputStream inputStream = null;
                    int fileSize = 0;
                    try {
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inputStream.available();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(MainActivity.this, getString(R.string.error_file), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, getString(R.string.error_file), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        return;
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                    if (fileSize >= FILE_SIZE_LIMIT) {
                        Toast.makeText(MainActivity.this, getString(R.string.error_file_size_too_large), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            } else {
                //save photo to gallery
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
                //Log.d(TAG, mMediaUri.toString());
            }
            Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);
            String fileType;
            if (requestCode == TAKE_PHOTO_REQUEST || requestCode == CHOOSE_PHOTO_REQUEST){
                fileType = ParseConstants.TYPE_IMAGE;

            }
            else {
                fileType = ParseConstants.TYPE_VIDEO;

            }
            recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);
            startActivity(recipientsIntent);


        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, getString(R.string.error_general), Toast.LENGTH_LONG).show();
        }

    }

    private static File getOutPutMediaFile(int mediaType) {
        //check for SDcard
        if (isExternalStorageAvailable()) {
            //get external storage directory
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Glimpse");
            // create the subdirectory
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }
            //create file name
            File mediaFile;
            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(date);
            String path = mediaStorageDir.getPath() + File.separator;
            //create file
            if (mediaType == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(path + "IMG_" + timeStamp + ".jpg");

            } else if (mediaType == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(path + "VID_" + timeStamp + ".mp4");

            } else {
                return null;
            }

            //return file uri
            //Log.d(TAG, Uri.fromFile(mediaFile).toString());
            return mediaFile;
        }
        return null;
    }

    private static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
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
        switch (id) {
            case R.id.action_edit_friends:
                Intent intent = new Intent(this, EditFriends.class);
                startActivity(intent);
                return true;

            case R.id.action_logout:
                ParseUser.logOut();
                navigateToLogin();
                return true;

            case R.id.action_camera:
                cameraChooser();
                //

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
