package com.github.sdw8001.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by sdw80 on 2016-08-24.
 * User Adapter
 */
public class UserAdapter extends ArrayAdapter<User> {

    private List<User> items;
    private final LayoutInflater layoutInflater;
    private int viewResourceId;
    private UserViewHolder viewHolder;

    public UserAdapter(Context context, int resource) {
        super(context, resource);
        this.viewResourceId = resource;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = Collections.emptyList();
    }

    public void setItems(List<User> items){
        this.clear();
        this.items = items;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User item = this.items.get(position);
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(viewResourceId, parent, false);
            viewHolder = new UserViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserViewHolder) convertView.getTag();
        }

        viewHolder.txtView_Name.setText(item.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final User item = this.items.get(position);
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(viewResourceId, parent, false);
            viewHolder = new UserViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserViewHolder) convertView.getTag();
        }

        viewHolder.txtView_Name.setText(item.getName());

        return convertView;
    }

    static class UserViewHolder {
        public UserViewHolder(View view) {
            this.txtView_Name = (TextView) view.findViewById(R.id.TextView_Name);
        }

        TextView txtView_Name;
    }
}
