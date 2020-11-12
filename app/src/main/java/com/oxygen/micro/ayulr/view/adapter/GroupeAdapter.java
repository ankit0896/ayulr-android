package com.oxygen.micro.ayulr.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oxygen.micro.ayulr.R;

public class GroupeAdapter extends BaseAdapter {
    Context context;
    String[] groupe_name;

    public GroupeAdapter(Context context, String[] groupe_name) {
        this.context = context;
        this.groupe_name = groupe_name;
        }
        @Override
    public int getCount() {
        return groupe_name.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView groupe_TV;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.groupe_list_item, null);
            holder = new ViewHolder();
            holder.groupe_TV = (TextView) convertView.findViewById(R.id.groupe_TV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.groupe_TV.setText(groupe_name[position]);
        return convertView;
    }
}
