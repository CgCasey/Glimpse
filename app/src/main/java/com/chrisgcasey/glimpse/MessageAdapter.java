package com.chrisgcasey.glimpse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Chris on 2/25/2015.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject> {
    //custom adapter for the messages listview

    //context member variable passed in from the constructor
    Context mContext;
    //list of parseObjects member variable passed in from the constructor
    List<ParseObject> mList;

    //create a constructor taking the context calling it, and the list in the parameters
    public MessageAdapter(Context mContext, List<ParseObject> mList) {
        //call super() and pass in the context, the list item layout, and the list
        super(mContext, R.layout.message_item,  mList);
        this.mContext = mContext;
        this.mList = mList;
    }
    //overide the getView method
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.message_icon);
            holder.senderLabel = (TextView) convertView.findViewById(R.id.senderLabel);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        ParseObject message = mList.get(position);
        if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
            holder.imageView.setImageResource(R.drawable.ic_action_picture);
        }
        else{
            holder.imageView.setImageResource(R.drawable.ic_action_play_over_video);
        }
        holder.senderLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));


        return  convertView;
    }
    //implement the viewholder design pattern for performance
    private static class ViewHolder {
        ImageView imageView;
        TextView senderLabel;
    }
    public void refill(List<ParseObject> messages){
        mList.clear();
        mList.addAll(messages);
        notifyDataSetChanged();
    }





}
