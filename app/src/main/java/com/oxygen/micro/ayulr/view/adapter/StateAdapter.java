package com.oxygen.micro.ayulr.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oxygen.micro.ayulr.R;

public class StateAdapter extends BaseAdapter {
    Context context;
    String[] state_name;
    String[] code_name;

    public StateAdapter(Context context, String[] state_name,String[] code_name) {
        this.context = context;
        this.state_name = state_name;
        this.code_name = code_name;


    }

    @Override
    public int getCount() {
        return state_name.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public String getPosition(String stateHolder) {
        return stateHolder;
    }


    private class ViewHolder {
        TextView state_TV,code_TV;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.state_list_item, null);
            holder = new ViewHolder();
            holder.state_TV = (TextView) convertView.findViewById(R.id.state_TV);
            holder.code_TV = (TextView) convertView.findViewById(R.id.code_TV);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // RowItem rowItem = (RowItem) getItem(position);
        holder.state_TV.setText(state_name[position]);
        holder.code_TV.setText(code_name[position]);


        return convertView;
    }
}
