package com.example.zzuk9.personaltrainerapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerListViewAdapter extends ArrayAdapter<Customer> {
    private final int layoutResourceId;
    private int layoutResourcesId;

    public CustomerListViewAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourcesId);
        this.layoutResourceId = layoutResourcesId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        convertView = inflater.inflate(layoutResourceId, parent, false);
        ImageView customerImage = (ImageView) convertView.findViewById(R.id.list.cust_image);
        TextView customerName = (TextView) convertView.findViewById(R.id.list.cust_name);
        customerImage.setImageResource(this.getItem(position).getCustomerImage());
        customerName.setText(this.getItem(position).getName());
        return convertView;
    }
}
