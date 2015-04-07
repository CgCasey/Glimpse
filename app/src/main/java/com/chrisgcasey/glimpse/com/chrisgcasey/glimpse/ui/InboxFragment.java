package com.chrisgcasey.glimpse.com.chrisgcasey.glimpse.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chrisgcasey.glimpse.AlertClass;
import com.chrisgcasey.glimpse.MessageAdapter;
import com.chrisgcasey.glimpse.ParseConstants;
import com.chrisgcasey.glimpse.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 1/8/2015.
 */
public class InboxFragment extends ListFragment {
    //query for new messages in the onresume method
    //create a parsequery object for the query
    //get messages for our userid with the whereequalto method
    //sort the results in descending order to have the most current at the top
    //call findinbackground to get messages in the background
    //create string array to store the sendernames
    //in the done callback enumerate through the messages and retrieve the sendernames
    //create the arrayadapter
    //set sendernames in the adapter and set the adapter to the listview.

    List<ParseObject> mMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.inbox_layout, container,false);
        Bundle args = getArguments();
        return  rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setProgressBarIndeterminate(true);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENTS_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                getActivity().setProgressBarIndeterminate(false);
                if (e == null){
                    //success
                    mMessages = messages;
                    String[] senderNames = new String[messages.size()];
                    int i = 0;
                    for (ParseObject message : mMessages){
                        senderNames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                        i++;
                    }
                    if(getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);
                        setListAdapter(adapter);
                    }
                    else {
                        ((MessageAdapter)getListView().getAdapter()).refill(mMessages);
                    }
                }
                else{
                    //String alertMessage = "error";
                    AlertClass.alertDialog(getActivity(), getActivity().getString(R.string.error_general));
                }

            }
        });

    }
    //handle message clicks
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //get the object
        ParseObject message = mMessages.get(position);
        //get the type of file
        String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
        //get the file
        ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
        //convert the parse url to a uri
        Uri fileUri = Uri.parse(file.getUrl());

        if (messageType.equals(ParseConstants.TYPE_IMAGE)){
            Intent intent = new Intent(getActivity(), ViewImageActivity.class);
            intent.setData(fileUri);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.setDataAndType(fileUri, "video/*");
            startActivity(intent);
        }
        //delete
        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENTS_IDS);
        if (ids.size() == 1){
            message.deleteInBackground();
        }
        else{
            //remove current user id from the collection
            message.remove(ParseUser.getCurrentUser().getObjectId());
            //make an array list to store ids to be removed
            ArrayList<String> idsToRemove = new ArrayList<String>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());
            //remove the ids from the backend
            message.removeAll(ParseConstants.KEY_RECIPIENTS_IDS, idsToRemove);
            message.saveInBackground();

        }

    }
}
