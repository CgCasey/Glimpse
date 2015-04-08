package com.chrisgcasey.glimpse.com.chrisgcasey.glimpse.ui;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chrisgcasey.glimpse.AlertClass;
import com.chrisgcasey.glimpse.FileHelper;
import com.chrisgcasey.glimpse.ParseConstants;
import com.chrisgcasey.glimpse.R;
import com.parse.FindCallback;
import com.parse.ParseException;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class RecipientsActivity extends ListActivity {

    //1 get friends from parse.com
    //2 create listAdapter
    //3 populate list with friends

    //create send action bar icon
    //create menuItem member variable

    public static final String TAG = RecipientsActivity.class.getSimpleName();

    ParseUser mCurrentUser;
    List<ParseUser> mUsers;
    ParseRelation<ParseUser> mFriendsRelation;

    Uri mMediaUri;
    String mFileType;
    // alertdialog error message
    //String alertMessage = getString(R.string.error_send);

    MenuItem mSendMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_recipients);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mMediaUri = getIntent().getData();
        mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE);
    }
    @Override
    public void onResume() {
        super.onResume();
        //create query methods to pull list of friends from parse
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);


        setProgressBarIndeterminate(true);
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                setProgressBarIndeterminate(false);
                mUsers = friends;
                String[] friendsNames = new String[mUsers.size()];
                if (e == null) {
                    int i = 0;
                    //set results from query into array
                    for (ParseUser friend : mUsers) {
                        friendsNames[i] = friend.getUsername();
                        i++;
                    }
                    //create array adapter and set the adapter to the listview
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_checked, friendsNames);
                    setListAdapter(adapter);
                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
                    builder.setTitle(R.string.error_notification);
                    builder.setMessage(e.getMessage());
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.create().show();

                }
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //set the send button to visible when an item is clicked
        if (l.getCheckedItemCount() > 0) {
            mSendMenuItem.setVisible(true);

        }
        else {
            mSendMenuItem.setVisible(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        //get reference to send action bar item
        mSendMenuItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_send:
                ParseObject message = createMessage();
                if (message == null){

                    AlertClass.alertDialog(this, getString(R.string.error_send));
                }
                else {
                    send(message);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



    protected ParseObject createMessage(){
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_RECIPIENTS_IDS, getRecipientIds());
        message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_FILE_TYPE, mFileType);


        byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
        if (fileBytes == null){
            Toast.makeText(this, R.string.error_general, Toast.LENGTH_LONG);
            return null;
        }
        else {
            //if file is an image then compress the size
            if (mFileType == ParseConstants.TYPE_IMAGE) {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);

            }
            String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);
            ParseFile parseFile = new ParseFile(fileName, fileBytes);
            message.put(ParseConstants.KEY_FILE, parseFile);

            return message;


        }
        //get the file name, create the parse file, and return message

    }
    private void send(ParseObject message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    //fill sent successfully
                    Toast.makeText(RecipientsActivity.this, getString(R.string.toast_message_sent), Toast.LENGTH_LONG).show();
                }
                else {
                  AlertClass.alertDialog(RecipientsActivity.this, getString(R.string.error_send));
                }
            }
        });
    }
    protected ArrayList<String> getRecipientIds(){
        ArrayList<String> recipientIds = new ArrayList<String>();
        for (int i = 0; i < getListView().getCount(); i++){
           if (getListView().isItemChecked(i)){
               recipientIds.add(mUsers.get(i).getObjectId());
           }
        }
        return recipientIds;
    }
}
