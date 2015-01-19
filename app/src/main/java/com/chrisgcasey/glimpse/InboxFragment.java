package com.chrisgcasey.glimpse;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chris on 1/8/2015.
 */
public class InboxFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.inbox_layout, container,false);
        Bundle args = getArguments();
        return  rootView;
    }
}
