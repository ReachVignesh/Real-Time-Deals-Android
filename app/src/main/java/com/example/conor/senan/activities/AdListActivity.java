package com.example.conor.senan.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.conor.senan.R;
import com.example.conor.senan.daos.SellerAdDao;
import com.example.conor.senan.models.SellerAdModel;
import com.example.conor.senan.views.AdListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to show a list of ads
 *
 * At the moment this activity always show all the ads
 * in the database. This activity acts as a controller.
 * This model is list of <code>SellerAdModel</code>s and the view is
 * a <code>AdListView</code>
 */
public class AdListActivity extends Activity{

    private List<SellerAdModel> model;
    private AdListView adListView;

    private HandlerThread handlerThread;
    private Handler workerHandler;

    /**
     * Listener used to listen on event on the view.
     */
    private AdListView.ViewListener viewListener = new AdListView.ViewListener(){

        /**
         * When an item in the list is clicked a new <code>SellerAdActivity</code> activity is started
         * to display the ad.
         *
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(AdListActivity.this, SellerAdActivity.class);
            intent.putExtra("id", adListView.getAdListAdapter().getItem(position).getId());
            startActivity(intent);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        handlerThread = new HandlerThread("Worker Handler");
        handlerThread.start();
        workerHandler = new Handler(handlerThread.getLooper());

        View screenView = View.inflate(this, R.layout.activity_ad_list, null);

        model = new ArrayList<SellerAdModel>();
        adListView = new AdListView(screenView, model, this);
        adListView.setViewListener(viewListener);

        fetchData();

        setContentView(screenView);

    }


    /**
     * This method fetches the data to be displayed in the
     * list and notifies the view.
     */
    public void fetchData(){

        workerHandler.post(new Runnable() {
            @Override
            public void run() {

                ArrayList<SellerAdModel> ads = new SellerAdDao().getAll();
                synchronized (model) {
                    while (model.size() > 0) {
                        model.remove(0);
                    }
                    for (SellerAdModel ad : ads) {
                        model.add(ad);
                    }
                }


                adListView.notifyDataSetChanged();
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add_list, menu);
        MenuItem menuItem = menu.add(0, R.id.action_new, Menu.NONE, "New");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(R.drawable.ic_action_new);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            Intent sellerAdActivity = new Intent(this, SellerAdActivity.class);
            startActivity(sellerAdActivity);
            return true;
        }else if(id == R.id.action_settings){
            Intent dbmanager = new Intent(this, NearbyActivity.class);
            startActivity(dbmanager);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume(){
        super.onResume();
        fetchData();

    }

}
