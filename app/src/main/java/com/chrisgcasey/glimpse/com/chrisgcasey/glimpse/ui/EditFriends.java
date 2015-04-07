package com.chrisgcasey.glimpse.com.chrisgcasey.glimpse.ui;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chrisgcasey.glimpse.ParseConstants;
import com.chrisgcasey.glimpse.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class EditFriends extends ListActivity {

    private static final String TAG = EditFriends.class.getSimpleName();

    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_edit_friends);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        setProgressBarIndeterminate(true);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null){
                    setProgressBarIndeterminate(false);
                    mUsers = parseUsers;
                    String[] userNames = new String[mUsers.size()];
                    int i = 0;
                    for(ParseUser user : mUsers){
                       userNames[i] = user.getUsername();
                       i++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditFriends.this, android.R.layout.simple_list_item_checked, userNames);
                    setListAdapter(adapter);

                    setCheckedFriends();

                }
                else{
                    Log.e(TAG, e.getMessage());

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriends.this);
                    builder.setMessage(e.getMessage());
                    builder.setTitle(R.string.error_notification);
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });
    }

    private void setCheckedFriends() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
               if (e == null){
                   for (int i = 0; i < mUsers.size(); i++){
                       ParseUser user = mUsers.get(i);
                       for (ParseUser friend : friends){
                           if (friend.getObjectId().equals(user.getObjectId())){
                               getListView().setItemChecked(i, true);
                           }
                       }
                   }
               }
               else {
                   Log.e(TAG, e.getMessage());
               }

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {

            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        if (id == R.id.action_logout){
            ParseConstants.logout();
            navigateToLogin();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (l.isItemChecked(position)) {
            mFriendsRelation.add(mUsers.get(position));

        }
        else{
            mFriendsRelation.remove(mUsers.get(position));
        }
        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
