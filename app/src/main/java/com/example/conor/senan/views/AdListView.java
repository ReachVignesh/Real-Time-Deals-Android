package com.example.conor.senan.views;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.conor.senan.R;
import com.example.conor.senan.lists.AdListAdapter;
import com.example.conor.senan.models.SellerAdModel;

import java.util.List;

/**
 * This class is used as a view to display a
 * list of ads.
 */
public class AdListView {

    private View view;
    private Activity activity;

    //the list of ads to display
    private List<SellerAdModel> ads;

    private AdListAdapter adListAdapter;

    private ListView listView;

    private ViewListener viewListener;

    public interface ViewListener{
        void onItemClick(AdapterView<?> parent, View view, int position, long id);

    }

    public AdListView(View view, List<SellerAdModel> ads, final Activity activity){
        this.view = view;
        this.ads = ads;
        this.activity = activity;

        adListAdapter = new AdListAdapter(activity, ads);

        activity.getActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getActionBar().setIcon(R.color.transparent);

        listView = (ListView)view.findViewById(R.id.ad_list_view);
        listView.setAdapter(adListAdapter);

        //when a listview item in clicked send it to the controller
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewListener.onItemClick(parent, view, position,  id);

            }
        });


    }





    public void setViewListener(ViewListener viewListener){
        this.viewListener = viewListener;
    }


    public void notifyDataSetChanged(){

        activity.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               adListAdapter.notifyDataSetChanged();
           }
        });

    }


    public AdListAdapter getAdListAdapter() {
        return adListAdapter;
    }
}
