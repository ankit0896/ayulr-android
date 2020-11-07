package com.oxygen.micro.ayulr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClinicalCityAdapter extends BaseAdapter {
    Context context;



    public ClinicalCityAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return ClinicActivity.cityList.size();
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
        TextView city_TV;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ClinicalCityAdapter.ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.city_list_item, null);
            holder = new ClinicalCityAdapter.ViewHolder();
            holder.city_TV = (TextView) convertView.findViewById(R.id.city_TV);


            convertView.setTag(holder);
        } else {
            holder = (ClinicalCityAdapter.ViewHolder) convertView.getTag();
        }

        holder.city_TV.setText(ClinicActivity.cityList.get(position));


        return convertView;
    }
}
