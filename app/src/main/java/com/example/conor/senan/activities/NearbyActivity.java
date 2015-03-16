package com.example.conor.senan.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.conor.senan.R;
import com.example.conor.senan.daos.SellerAdDao;
import com.example.conor.senan.models.NearbyModel;
import com.example.conor.senan.models.SellerAdModel;
import com.example.conor.senan.views.NearbyView;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;


/**
 * Activity for displaying nearby deals.
 *
 * This activity acts as a controller.
 * It uses a <code>NearbyView</code> object as the view and a <code>NearbyModel</code> object as the model.
 */
public class NearbyActivity extends Activity {

    NearbyModel model;
    NearbyView nearbyView;

    private HandlerThread handlerThread;
    private Handler workerHandler;


    /**
     * This listener is used to listen on the <code>nearbyView</code>'s event.
     */
    private NearbyView.ViewListener viewListener = new NearbyView.ViewListener(){

        @Override
        public void onMapReady(GoogleMap map){
            nearbyView.setUpMap(map);
        }

        /**
         * This method is invoked when one of the view's infowindows in clicked.
         * These infowindows belong to a Google Map.
         * A new activity is started to show the ad.
         *
         * @param model The model assoiciated with the info window.
         */
        @Override
        public void onInfoWindowClick(SellerAdModel model) {
            Intent intent = new Intent(NearbyActivity.this, SellerAdActivity.class);
            intent.putExtra("id", model.getId());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //set up model and view
        View screenView = View.inflate(this, R.layout.activity_nearby, null);
        model = new NearbyModel();
        nearbyView = new NearbyView(screenView, model, viewListener, this);
        setContentView(screenView);

        handlerThread = new HandlerThread("Handler Nearby Thread");
        handlerThread.start();
        workerHandler = new Handler(handlerThread.getLooper());

        fetchData();

        //if we have already seen the splash screen, hide it.
        if(savedInstanceState != null && savedInstanceState.containsKey("no_splash")){
            nearbyView.hideSplashScreen();

        //else hide it in a few seconds.
        }else {
            screenView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nearbyView.hideSplashScreen();
                }
            }, 5000);

        }

    }

    public void fetchData(){

        workerHandler.post(new Runnable() {
            @Override
            public void run() {
                //get all the add models from the database.
                ArrayList<SellerAdModel> ads = new SellerAdDao().getAll();
                synchronized (model){
                    model.setPoints(ads);
                }

            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nearby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_as_list) {
            Intent adList = new Intent(this, AdListActivity.class);
            startActivity(adList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This methods records that we have seen the
     * splash screen.
     *
     * @param state
     */
    @Override
    public void onSaveInstanceState(Bundle state){
        state.putInt("no_splash", 1);

    }
}
