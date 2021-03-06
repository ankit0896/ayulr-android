package com.oxygen.micro.ayulr.view.paramedical.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.model.GridViewItem;
import com.oxygen.micro.ayulr.view.activity.ServiceActivity;
import com.oxygen.micro.ayulr.view.adapter.AdapaterGridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParaHomeActivity extends Fragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.gridView1)
    GridView gridview;
    @BindView(R.id.viewpager)
    AdapterViewFlipper adapterViewFlipper;
    AdapaterGridView gridviewAdapter;
    ArrayList<GridViewItem> data = new ArrayList<GridViewItem>();
    View v;
    private static final int[] IMAGES = {R.drawable.head, R.drawable.head2, R.drawable.head3, R.drawable.head4, R.drawable.head5};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_list, container, false);
        initViews();
        return v;
    }

    private void initViews() {

        ButterKnife.bind(this, v);
        gridview.setOnItemClickListener(this);

        //data.add(new GridViewItem(getResources().getString(Integer.parseInt("CreateProfile")),
        //data.add(new GridViewItem(getResources().getString(R.string.Create_profile), getResources().getDrawable(R.drawable.pro)));
        data.add(new GridViewItem(getResources().getString(R.string.view_profile), getResources().getDrawable(R.drawable.viewprofile)));
        data.add(new GridViewItem(getResources().getString(R.string.Appointment), getResources().getDrawable(R.drawable.appointment)));
        data.add(new GridViewItem(getResources().getString(R.string.Testimonial), getResources().getDrawable(R.drawable.testimonial)));
        data.add(new GridViewItem(getResources().getString(R.string.Chooseplan), getResources().getDrawable(R.drawable.pay_icon)));
        data.add(new GridViewItem(getResources().getString(R.string.Video), getResources().getDrawable(R.drawable.video)));
        data.add(new GridViewItem(getResources().getString(R.string.Service), getResources().getDrawable(R.drawable.serviceicon)));
        //data.add(new GridViewItem(getResources().getString(R.string.menu_babycare), getResources().getDrawable(R.drawable.babycare)));
        //data.add(new GridViewItem(getResources().getString(R.string.menu_egg), getResources().getDrawable(R.mipmap.ic_launcher)));
        setDataAdapter();
        MyCustom adapter = new MyCustom(getActivity(), IMAGES);
        adapterViewFlipper.setAdapter(adapter);
        adapterViewFlipper.setAutoStart(true);
    }

    // Set the Data Adapter
    private void setDataAdapter() {
        gridviewAdapter = new AdapaterGridView(getActivity(), R.layout.fragment_list_item, data);
        gridview.setAdapter(gridviewAdapter);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id) {
        //if (position==0){
        //startActivity(new Intent(getActivity(), ActivityRegistration.class));
        //	getActivity().overridePendingTransition (R.animator.open_next, R.animator.close_next);
        //	}
        if (position == 0) {
            startActivity(new Intent(getActivity(), ParaViewProfileActivity.class));
            getActivity().overridePendingTransition(R.animator.open_next, R.animator.close_next);
        } else if (position == 1) {
            startActivity(new Intent(getActivity(), ParaAppointmentActivity.class));
            getActivity().overridePendingTransition(R.animator.open_next, R.animator.close_next);
        } else if (position == 2) {
            startActivity(new Intent(getActivity(), ParaForumActivity.class));
            getActivity().overridePendingTransition(R.animator.open_next, R.animator.close_next);
        } else if (position == 3) {
            startActivity(new Intent(getActivity(), ParaChoosePlanActivity.class));
            getActivity().overridePendingTransition(R.animator.open_next, R.animator.close_next);
        } else if (position == 4) {
            startActivity(new Intent(getActivity(), ParaAddVideoActivity.class));
            getActivity().overridePendingTransition(R.animator.open_next, R.animator.close_next);
        } else {
            startActivity(new Intent(getActivity(), ServiceActivity.class));
            getActivity().overridePendingTransition(R.animator.open_next, R.animator.close_next);
        }
        //else if (position==6){
        //startActivity(new Intent(getActivity(), ActivityHouseHoldCategoryList.class));
        //	getActivity().overridePendingTransition (R.animator.open_next, R.animator.close_next);
			/*Intent sendInt = new Intent(Intent.ACTION_SEND);
			sendInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
			sendInt.putExtra(Intent.EXTRA_TEXT, "E-Commerce Android App\n\""+getString(R.string.app_name)+"\" \nhttps://play.google.com/store/apps/details?id="+getActivity().getPackageName());
			sendInt.setType("text/plain");
			startActivity(Intent.createChooser(sendInt, "Share"));*/
        //	}
        //else {
        //startActivity(new Intent(getActivity(), ActivityBabyCareCategoryList.class));
        //	getActivity().overridePendingTransition (R.animator.open_next, R.animator.close_next);
        //	}

    }


}

class MyCustom extends BaseAdapter {
    Context otx;
    int[] images;
    LayoutInflater layoutInflater;

    public MyCustom(Context otx, int[] myimages) {
        this.otx = otx;
        this.images = myimages;
        this.layoutInflater = LayoutInflater.from(otx);

    }

    @Override
    public int getCount() {

        return images.length;

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.slideimage, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.slideimage);
        imageView.setImageResource(images[i]);
        return view;
    }


}