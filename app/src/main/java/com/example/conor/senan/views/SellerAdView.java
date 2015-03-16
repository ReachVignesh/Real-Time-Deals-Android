package com.example.conor.senan.views;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.conor.senan.R;
import com.example.conor.senan.models.OnChangeListener;
import com.example.conor.senan.models.SellerAdModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;

/**
 * View used to display an ad
 *
 * This view can display an add in an editable  or non editable state.
 * This class used the state pattern to swap between the above states.
 */
public class SellerAdView implements OnChangeListener<SellerAdModel>{


    private View view;
    private SellerAdModel model;
    private Activity activity;

    private ImageView image;
    private Menu menu;
    private ViewListener viewListener;

    private Bitmap imageBitmap;

    private GoogleMap map;
    private MapFragment mapFragment;
    private Marker mapMarker;

    SellerAdViewState viewState;



    public interface ViewListener extends OnMapReadyCallback {

        void onDataScraped(SellerAdModel model, Bitmap bitmap);
        void onSetImageButtonClick(View v);
        void onSetTimeDate();

    }

    public SellerAdView(View view, SellerAdModel model, final ViewListener viewListener,  Activity activity){
        this.view = view;
        this.model= model;
        this.activity = activity;

        model.addListener(this);

        viewState = new SellerAdViewStateLocked(this);

        this.viewListener = viewListener;

        activity.getActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getActionBar().setIcon(R.color.transparent);

        view.findViewById(R.id.set_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onSetImageButtonClick(v);
            }
        });

        view.findViewById(R.id.date_time_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onSetTimeDate();
            }
        });

        ((TextView)view.findViewById(R.id.old_price))
                .setPaintFlags(((TextView)view.findViewById(R.id.old_price)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        ((TextView)view.findViewById(R.id.old_price_cur))
                .setPaintFlags(((TextView)view.findViewById(R.id.old_price)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



        mapFragment = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.add_map));
        mapFragment.getMapAsync(viewListener);

        final ScrollView mainScrollView = (ScrollView) view.findViewById(R.id.main_scrollview);
        ImageView transparentImageView = (ImageView) view.findViewById(R.id.transparent_image);

        //this prevents the mainScrollView from scrolling when the map is being touched
        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

    }







    public void setUpMap(GoogleMap map){
        this.map = map;


        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                viewState.onCameraChange(cameraPosition);
            }
        });

        viewState.updateMap();

    }


    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void onChange(SellerAdModel model) {


        viewState.endUpdate();

        if(model.isLocked()){
            viewState = new SellerAdViewStateLocked(this);
        }else{
            viewState = new SellerAdViewStateUnlocked(this);
        }

        updateView();
    }


    public void scrapeData(){

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                SellerAdModel model = new SellerAdModel();

                model.setTitle(((TextView) view.findViewById(R.id.title)).getText() + "");
                model.setDetails(((TextView) view.findViewById(R.id.details)).getText() + "");
                model.setPrice(
                        (int) (Double.valueOf(((TextView) view.findViewById(R.id.price)).getText() + "") * 100)
                );

                model.setOldPrice(
                        (int) (Double.valueOf(((TextView) view.findViewById(R.id.old_price)).getText() + "") * 100)
                );

                /*model.setEndTimestamp(
                        (int) (Double.valueOf(((TextView) view.findViewById(R.id.old_price)).getText() + "") * 100)
                );*/

                model.setAmountLeft(
                        Integer.parseInt(((TextView) view.findViewById(R.id.amount_left)).getText() + ""));


                model.setLatLng(mapMarker.getPosition());

                Log.d("conorbonor", mapMarker.getPosition().latitude + "k : " + mapMarker.getPosition().longitude);

                viewListener.onDataScraped(model, imageBitmap);

            }
        });


    }

    /**
     * This method set the ad's image but also saves a reference to
     * the bitmap
     *
     * @param yourSelectedImage
     */
    public void setImageBitmap(final Bitmap yourSelectedImage){

        imageBitmap = yourSelectedImage;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ImageView) view.findViewById(R.id.image)).setImageBitmap(yourSelectedImage);
            }
        });


    }

    public void updateView(){

        viewState.updateView();
        viewState.updateTime();

        if(map != null){
            viewState.updateMap();
        }

    }

    public void updateTime(){
        viewState.updateTime();
    }

    public View getView(){
        return view;
    }

    public SellerAdModel getModel(){
        return model;
    }

    public Activity getActivity(){
        return activity;

    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {

        this.map = map;


    }

    public Marker getMapMarker() {
        return mapMarker;
    }

    public void setMapMarker(Marker marker) {
        this.mapMarker = marker;
    }
}
