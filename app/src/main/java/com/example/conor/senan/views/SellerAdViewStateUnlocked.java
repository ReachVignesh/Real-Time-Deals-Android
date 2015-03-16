package com.example.conor.senan.views;

import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.example.conor.senan.R;
import com.example.conor.senan.utils.DialogUtils;
import com.example.conor.senan.utils.GoogleMapUtils;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * This class contains the behaviour of the unlocked ad view.
 */
public class SellerAdViewStateUnlocked extends SellerAdViewState {

    public SellerAdViewStateUnlocked(SellerAdView view){
        super(view);
    }

    @Override
    public void updateView() {


        super.updateView();

        sellerAdView.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //set up the menu
                Menu menu = sellerAdView.getMenu();
                menu.removeItem(R.id.action_edit);
                MenuItem menuItem = menu.add(0, R.id.action_save, 1, R.string.save);
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                menuItem.setIcon(R.drawable.ic_action_save);


                //if we are not making a new ad show the cancel button
                if(sellerAdModel.getId() > 0) {
                    menuItem = menu.add(0, R.id.action_cancel, 2, R.string.cancel);
                    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    menuItem.setIcon(R.drawable.ic_action_cancel);
                }



                //set all the text views to enabled
                view.findViewById(R.id.set_image_button).setVisibility(View.VISIBLE);


                view.findViewById(R.id.title).setEnabled(true);
                view.findViewById(R.id.details).setEnabled(true);
                view.findViewById(R.id.price).setEnabled(true);
                view.findViewById(R.id.old_price).setEnabled(true);
                view.findViewById(R.id.amount_left).setEnabled(true);

                view.findViewById(R.id.date_display_container).setVisibility(View.VISIBLE);
                view.findViewById(R.id.countdown_display_container).setVisibility(View.GONE);

                view.findViewById(R.id.social_container).setVisibility(View.GONE);
                /*view.findViewById(R.id.address).setVisibility(View.GONE);*/
                SellerAdViewStateUnlocked.super.onFinishedUpdate();

            }
        });

    }

    @Override
    public void updateMap() {

        super.updateMap();

        /*sellerAdView.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sellerAdView.getMapMarker().setDraggable(true);
            }

        });*/
    }

    public void onCameraChange(CameraPosition cameraPosition) {
        //this moves map marker to the center of the map whenever the map is scrolled.
        //then the latlng for the model is always taken as the center of the map
        GoogleMapUtils.animateMarkerTo(sellerAdView.getMapMarker(), cameraPosition.target);
    }

}