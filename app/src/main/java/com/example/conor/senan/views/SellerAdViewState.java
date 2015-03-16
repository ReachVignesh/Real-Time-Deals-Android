package com.example.conor.senan.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.conor.senan.R;
import com.example.conor.senan.models.SellerAdModel;
import com.example.conor.senan.utils.ImageUtils;
import com.example.conor.senan.utils.LocationUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is the superclass of <code>SellerAdViewStateLocked</code>
 * and <code>SellerAdViewStateUnlocked</code>.
 * It contains behaviour that both share.
 *
 */
public abstract class SellerAdViewState {


    protected SellerAdView sellerAdView;
    protected View view;
    protected SellerAdModel sellerAdModel;

    private boolean mainContentIsVisible = false;

    public SellerAdViewState(SellerAdView sellerAdView){
        this.sellerAdView = sellerAdView;
        this.view = sellerAdView.getView();



        this.sellerAdModel = sellerAdView.getModel();
    }


    public void onFinishedUpdate(){

        if(!mainContentIsVisible){

            view.findViewById(R.id.loading).setVisibility(View.GONE);
            view.findViewById(R.id.main_scrollview).setVisibility(View.VISIBLE);
            view.findViewById(R.id.get_deal_container).setVisibility(View.VISIBLE);

            mainContentIsVisible = true;

        }

    }


    public void updateTime(){

        sellerAdView.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Date d = new Date();
                d.setTime(sellerAdModel.getEndTimestamp());

                SimpleDateFormat f = new SimpleDateFormat("EEE d MMM h:maa");
                ((TextView)view.findViewById(R.id.date_time_text))
                        .setText(f.format(d));
            }
        });

    }

    public  void updateView() {



        sellerAdView.getActivity().runOnUiThread(new Runnable() {



            @Override
            public void run() {

                Bitmap image;
                File f = new File("/data/data/com.example.conor.senan/app_imageDir/add_image_" + sellerAdModel.getId());
                //if the model is new we always use the default camera picture.
                //we save it into the imageview like this so that we get a bitmap
                //reference to the default picture that is used later, keeping everything simple
                if (sellerAdModel.getId() < 0 || !f.exists()) {
                    image = BitmapFactory
                            .decodeResource(sellerAdView.getActivity().getResources(), R.drawable.ic_action_camera);
                } else {
                    String name = "add_image_" + sellerAdModel.getId();
                    image = ImageUtils.loadImageFromStorage(name);
                }


                sellerAdView.setImageBitmap(image);

            }

        });

    }


    public void endUpdate(){

    }

    /**
     * Updates the map.
     *
     * At the moment this method gets called twice per update
     * if the map loads twice.
     * Once when the map loads and again when the model loads.
     * TODO: fix this
     *
     */
    public void updateMap(){

        sellerAdView.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Log.d("conor bonor", sellerAdModel.toString());

                LatLng coords;
                //if the model has not yet been saved use the
                //users current position as the default marker position
                if (sellerAdModel.getId() < 0) {
                    Location location = LocationUtils.getLocation();

                    if(location == null){
                        coords = LocationUtils.getDublinLatLng();
                        Log.d("conor bonor", "no location info");


                    }else {
                        Log.d("conor bonor", "location info");
                        coords = new LatLng(
                                location.getLatitude(),
                                location.getLongitude());
                    }

                    Log.d("conor bonor",  "less than");

                } else {
                    coords = sellerAdModel.getLatLng();
                }

                //float zoom = (sellerAddView.getMap().getMaxZoomLevel() <9)? 9: sellerAddView.getMap().getMaxZoomLevel();


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(coords)      // Sets the center of the map to Mountain View
                        .zoom(12)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                sellerAdView.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                if(sellerAdView.getMapMarker() != null)
                    sellerAdView.getMapMarker().remove();

                final BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker);

                sellerAdView.setMapMarker(sellerAdView.getMap().addMarker(new MarkerOptions()
                                // .title("Sydney")
                                // .snippet("The most populous city in Australia.")
                                .position(coords)
                                .icon(markerIcon)
                ));



            }
        });

    }

    public void onCameraChange(CameraPosition cameraPosition) {

    }

}
