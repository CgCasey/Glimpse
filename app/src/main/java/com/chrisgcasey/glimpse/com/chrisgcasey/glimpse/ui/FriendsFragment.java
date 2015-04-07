package com.chrisgcasey.glimpse.com.chrisgcasey.glimpse.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.chrisgcasey.glimpse.ParseConstants;
import com.chrisgcasey.glimpse.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Chris on 1/8/2015.
 */
public class FriendsFragment extends ListFragment {
    //create member variables, parserelation, parseuser, list

    public static final String TAG = FriendsFragment.class.getSimpleName();

    ParseUser mCurrentUser;
    List<ParseUser> mUsers;
    ParseRelation<ParseUser> mFriendsRelation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.friends_layout, container,false);

        return  rootView;


    }
    //create on resume method

    @Override
    public void onResume() {
        super.onResume();
        //create query methods to pull list of friends from parse
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);


        getActivity().setProgressBarIndeterminate(true);
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                getActivity().setProgressBarIndeterminate(false);
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
                   ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, friendsNames);
                   setListAdapter(adapter);
                }
                else{
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setTitle(R.string.error_notification);
                    builder.setMessage(e.getMessage());
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.create().show();

                }
            }
        });


    }


}
