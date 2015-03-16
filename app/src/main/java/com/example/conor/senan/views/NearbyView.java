package com.example.conor.senan.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.location.Location;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.conor.senan.SenanApp;
import com.example.conor.senan.models.NearbyModel;
import com.example.conor.senan.models.OnChangeListener;
import com.example.conor.senan.models.SellerAdModel;
import com.example.conor.senan.utils.DisplayUtils;
import com.example.conor.senan.utils.ImageUtils;
import com.example.conor.senan.utils.LocationUtils;
import com.example.conor.senan.utils.MathUtils;
import com.example.conor.senan.utils.TextViewCountdownTimer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.example.conor.senan.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

/**
 * This class is used as a view to display nearby ads.
 *
 */
public class NearbyView implements OnChangeListener<NearbyModel>{

    /**
     * The <code>view</code> passed to the constructor by the controller.
     */
    private View view;

    private NearbyModel model;

    private Activity activity;

    private ViewListener viewListener;

    private TextViewCountdownTimer timer;

    private Marker lastClickedMarker;

    private GoogleMap map;

    /**
     * This map associates marker that are on the map
     * with their respective ads.
     */
    private HashMap<Marker, SellerAdModel> markerModelMap;

    /**
     * This view holds the countdown timer and
     * 'Get Deal' button.
     */
    private View dealContainer;

    private TextView countdownTimer;

    private TextView getDealButton;

    /**
     * Animation used to fade in <code>dealContainer</code>
     */
    private Animation fadeInAnim;

    /**
     * Animation used to fade out <code>dealContainer</code>
     */
    private Animation fadeOutAnim;

    private boolean showingDealContainer = false;


    private class GoogleMapClickListener implements GoogleMap.OnMapClickListener{

        /**
         * When the map in clicked the
         * <code>dealContainer</code> is hidden.
         * The map's padding is set back to zero as it
         * will not be hidden by <code>dealContainer</code>.
         *
         * @param latLng
         */
        @Override
        public void onMapClick(LatLng latLng) {


            if(!showingDealContainer){
                return;
            }

            if(lastClickedMarker != null){
                lastClickedMarker.hideInfoWindow();
            }

            lastClickedMarker = null;


            map.setPadding(0, 0, 0, 0);
            dealContainer.clearAnimation();
            dealContainer.startAnimation(fadeOutAnim);
            showingDealContainer = false;

            //TODO: move somewhere outside listener
            dealContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

        }
    }

    private class InfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener{

        /**
         * When an infowindow is clicked we send the event
         * to the <code>viewListener</code>
         *
         * @param marker
         */
        @Override
        public void onInfoWindowClick(Marker marker) {
            SellerAdModel model = markerModelMap.get(marker);
            viewListener.onInfoWindowClick(model);

        }
    }

    private class MarkerClickListener implements GoogleMap.OnMarkerClickListener{

        /**
         * When a marker is clicked we
         * show the <code>dealContainer</code> view.
         *
         * @param marker
         * @return
         */
        @Override
        public boolean onMarkerClick(Marker marker) {

            //save the last marker that was clicked to
            //it can be hidden later on.
            lastClickedMarker = marker;

            //get the model associated with the marker.
            SellerAdModel model = markerModelMap.get(marker);

            //a marker has been clicked again so
            //we cancel the old <code>CountDownTimer</code>
            //so that is isn't leaked
            if(timer != null){
                timer.cancel();
            }

            //this part is used to make the dealContainer visible.
            //if the deal container is already visible (eg the user
            //click a marker with out clicking the map first)
            //we skip it two avoid a flicker.
            if(!showingDealContainer) {
                dealContainer.setVisibility(View.VISIBLE);
                //add a padding to the map to raise to Google
                //logo above the deal container.
                map.setPadding(0, 0, 0, DisplayUtils.dpToPx(activity, 70));
                dealContainer.clearAnimation();
                dealContainer.startAnimation(fadeInAnim);
                showingDealContainer = true;

            }

            //set a new countdown to the dead line of
            //the current ad
            timer = new TextViewCountdownTimer(
                    countdownTimer,
                    getDealButton,
                    model.getEndTimestamp() - System.currentTimeMillis()
            );



            return false;
        }
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

        View view;
        TextView title;
        TextView price;
        TextView oldPrice;
        TextView amountLeft;
        TextView details;
        ImageView image;


        public CustomInfoWindowAdapter(){

            //we save references to all the views so that they
            //don't need to be inflated every time a marker is clicked
            view = View.inflate(activity, R.layout.nearby_custom_info, null);
            title = ((TextView)view.findViewById(R.id.title));
            price = ((TextView)view.findViewById(R.id.price));
            oldPrice = ((TextView)view.findViewById(R.id.old_price));
            amountLeft = ((TextView)view.findViewById(R.id.amount_left));
            details = ((TextView)view.findViewById(R.id.details));
            image = ((ImageView)view.findViewById(R.id.image));

            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            TextView oldPriceCur =  ((TextView)view.findViewById(R.id.old_price_cur));
            oldPriceCur.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        /**
         * This model fills <code>view</code> with the details of the ad
         * associated with <code>marker</code>.
         *
         * @param marker
         * @return
         */
        @Override
        public View getInfoContents(Marker marker) {
            SellerAdModel model = markerModelMap.get(marker);

            title.setText(model.getTitle());
            price.setText(MathUtils.doubleToNiceString(model.getPrice() / 100));
            oldPrice.setText(MathUtils.doubleToNiceString(model.getOldPrice() / 100));
            amountLeft.setText(model.getAmountLeft() + "");
            details.setText(model.getDetails());
            image.setImageBitmap(ImageUtils.loadImageFromStorage("add_image_" + model.getId()));
            return view;
        }
    }


    /**
     * The listener used to interact with the controller
     */
    public interface ViewListener extends OnMapReadyCallback {

        void onInfoWindowClick(SellerAdModel model);

    }


    public NearbyView(View view, NearbyModel model, ViewListener viewListener, Activity activity){

        this.view = view;
        this.model = model;
        this.viewListener = viewListener;
        this.activity = activity;

        this.model.addListener(this);

        activity.getActionBar().hide();
        activity.getActionBar().setIcon(R.color.transparent);
        activity.getActionBar().setTitle(R.string.title_activity_nearby);


        markerModelMap = new HashMap<Marker, SellerAdModel>();

        dealContainer = view.findViewById(R.id.get_deal_container);
        countdownTimer = (TextView)view.findViewById(R.id.countdown_timer);
        getDealButton = (TextView)view.findViewById(R.id.get_deal_button);


        fadeInAnim = AnimationUtils.loadAnimation(SenanApp.getContext(), R.anim.fade_in_deal_container);
        fadeOutAnim = AnimationUtils.loadAnimation(SenanApp.getContext(), R.anim.fade_out_deal_container);

        MapFragment mapFragment = ((MapFragment)activity.getFragmentManager().findFragmentById(R.id.nearby_map));
        mapFragment.getMapAsync(viewListener);



    }

    public void hideSplashScreen(){

        activity.getActionBar().show();
        view.findViewById(R.id.splash_image).setVisibility(View.GONE);


    }


    @Override
    public void onChange(NearbyModel model) {
        updateView();
    }


    public void updateView(){

        updateMap();

    }


    /**
     * This method clears the map
     * and then places new markers on it.
     */
    public void updateMap(){

        if(map == null || model.getPoints().size() == 0){
            return;
        }

        //load the marker icon from resources
        final BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Marker marker;
                map.clear();

                for(SellerAdModel point: model.getPoints()){
                    marker = map.addMarker(
                            new MarkerOptions()
                                    .position(point.getLatLng())
                                    .icon(markerIcon)
                    );

                    //associate marker with models
                    markerModelMap.put(marker, point);


                }

            }
        });




    }


    public void setUpMap(GoogleMap map) {

        this.map = map;




        map.setMyLocationEnabled(true);
        //map.getUiSettings().setMyLocationButtonEnabled(true);

        Location location = LocationUtils.getLocation();
        LatLng coords;
        if(location == null){
            coords = LocationUtils.getDublinLatLng();
        }else{
            coords = new LatLng(location.getLatitude(), location.getLongitude());
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(coords)      // Sets the center of the map to Mountain View
                .zoom(12)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        map.setOnMarkerClickListener(new MarkerClickListener());
        map.setOnInfoWindowClickListener(new InfoWindowClickListener());
        map.setOnMapClickListener(new GoogleMapClickListener());


        updateMap();

    }
}
