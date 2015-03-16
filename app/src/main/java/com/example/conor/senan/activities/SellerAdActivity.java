package com.example.conor.senan.activities;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;

import com.example.conor.senan.R;
import com.example.conor.senan.daos.SellerAdDao;
import com.example.conor.senan.dialogs.ConfirmDeleteAdDialog;
import com.example.conor.senan.models.SellerAdModel;
import com.example.conor.senan.utils.DialogUtils;
import com.example.conor.senan.utils.ImageUtils;
import com.example.conor.senan.views.SellerAdView;
import com.google.android.gms.maps.GoogleMap;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.conor.senan.R.layout.activity_seller_ad;

/**
 * This is the class used to display an ad.
 *
 * This class also used as the form to create and edit ads.
 * This activity acts as a controller.
 * Its view is a <code>SellerAdView</code> and its model is a <code>SellerAdView</code>
 */
public class SellerAdActivity extends FragmentActivity implements ConfirmDeleteAdDialog.DeleteAddListener{

    private SellerAdView sellerAdView;
    private SellerAdModel sellerAdModel;

    private HandlerThread handlerThread;
    private Handler handler;

    //this is the id of the view that was passed to the activity.
    //-1 signifies that it's a new ad
    private int originalId;

    private static final int SELECT_PHOTO = 10045;

    /**
     * Listener used to listner to events on the view.
     */
    SellerAdView.ViewListener viewListener = new SellerAdView.ViewListener() {

        /**
         * When data in extracted from the View in the sellerAdView
         * we save it
         *
         * @param model
         * @param image
         */
        @Override
        public void onDataScraped(SellerAdModel model, Bitmap image) {
            saveModel(model, image);
        }

        /**
         * This method lets the user choose a photo for the ad
         * when the set_image_button is clicked
         *
         * @param v
         */
        @Override
        public void onSetImageButtonClick(View v){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);

        }

        /**
         * This method shows a dialog to pick a time and date
         * for the dead line of the ad.         *
         */
        @Override
        public void onSetTimeDate(){
            final Dialog dialog  = DialogUtils.getTimeDateDialog(SellerAdActivity.this);
            dialog.setTitle("Set Deadline");


            dialog.findViewById(R.id.accept_date_change_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    long timeStamp = DialogUtils.getTimeStampFromPickers(
                            dialog.findViewById(R.id.date_picker),
                            dialog.findViewById(R.id.time_picker)
                            );

                    sellerAdModel.setEndTimestamp(timeStamp);
                    sellerAdView.updateTime();
                    dialog.dismiss();

                }
            });

            dialog.show();

        }

        @Override
        public void onMapReady(GoogleMap googleMap){
            sellerAdView.setUpMap(googleMap);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        //check if an id is being passed in.
        if(     (bundle == null || !bundle.containsKey("id") || bundle.getInt("id") < 0)
           &&   (savedInstanceState == null || !savedInstanceState.containsKey("id") || savedInstanceState.getInt("id") < 0)
                ){
            this.originalId = -1;
        }else{

            if((bundle == null || !bundle.containsKey("id") || bundle.getInt("id") < 0)) {
                this.originalId = bundle.getInt("id");
            }else{
                this.originalId = savedInstanceState.getInt("id");
            }
        }

        View screenView = View.inflate(this, R.layout.activity_seller_ad, null);
        setContentView(screenView);

        handlerThread = new HandlerThread("Handler Thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        sellerAdModel = new SellerAdModel();
        sellerAdView = new SellerAdView(screenView, sellerAdModel, viewListener, this);

        //*** populateModel is called after we have a reference to menu as this is needed by the view ***


    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);



        switch(requestCode) {
            //if a photo is being picked, get it as a bitmap
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){

                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        //InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        //Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        Bitmap yourSelectedImage = ImageUtils.decodeUri(selectedImage, this);
                        sellerAdView.setImageBitmap(yourSelectedImage);

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            break;
        }
    }

    /**
     * This fetches the ad from the dao by the id
     * and then gives it to the model.
     */
    public void populateModel(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (sellerAdModel) {
                    SellerAdModel model = new SellerAdDao().getId(originalId);


                    if (model == null) {
                        model = new SellerAdModel();
                    }
                    sellerAdModel.consume(model);
                }


            }
        });
    }


    public void makeAddEditable(){
        sellerAdModel.setLocked(false);
    }

    public void cancelEdit(){
        sellerAdModel.setLocked(true);
    }


    public void saveModel(final SellerAdModel model, final Bitmap image){

        handler.post(new Runnable() {
            @Override
            public void run() {


                synchronized (sellerAdModel) {

                    //the model passed in here might have
                    //been scraped and so wont have the id or timestamp
                    //set properly, we do that here.
                    model.setId(sellerAdModel.getId());
                    model.setEndTimestamp(sellerAdModel.getEndTimestamp());

                    sellerAdModel.consumeWithoutNotification(model);

                    SellerAdDao dao = new SellerAdDao();
                    if (sellerAdModel.getId() > 0) {
                        int effected = dao.update(model);

                        // this would be the case if
                        // item is saved, item is deleted from list, user goes history back,
                        // old model still have id value.
                        if (effected < 1) {
                            long id = dao.insert(sellerAdModel);
                            sellerAdModel.setId((int)id);
                        }
                    } else {
                        long id = dao.insert(sellerAdModel);
                        sellerAdModel.setId((int)id);
                    }

                    sellerAdModel.setImageName("add_image_" + sellerAdModel.getId());
                    //TODO: move this to the dao
                    ImageUtils.saveImageToInternalStorage(image, sellerAdModel.getImageName());

                    sellerAdModel.setLocked(true);


                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState (Bundle outState){
        outState.putInt("id", sellerAdModel.getId());
    }


    @Override
    public void onBackPressed(){

        //if we are editing an old model go back to nonediting state.
        if(!sellerAdModel.isLocked() && sellerAdModel.getId() > 0){
            sellerAdModel.setLocked(true);
        }else{
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_seller_ad, menu);

        sellerAdView.setMenu(menu);

        //if ... we have an ad to display so we display that add
        if(this.originalId > 0) {
            //populate model once we have a reference to the menu
            populateModel();
        //else we make the form editable
        }else{
            makeAddEditable();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


         if(id == R.id.action_settings){
            Intent dbmanager = new Intent(this,AndroidDatabaseManager.class);
            startActivity(dbmanager);

        }else if(id == R.id.action_cancel) {
             cancelEdit();

        }else if(id == R.id.action_edit) {
            makeAddEditable();

        }else if(id == R.id.action_save){
            sellerAdView.scrapeData();

         }else if(id == R.id.action_delete){
             DialogFragment dialogFragment = new ConfirmDeleteAdDialog();
             dialogFragment.show(getSupportFragmentManager(), "dialog");
         }

        return super.onOptionsItemSelected(item);



    }


    @Override
    public void onDeleteAdd() {

       handler.post(new Runnable() {
           @Override
           public void run() {
               new SellerAdDao().delete(sellerAdModel.getId());

           }
       });

       finish();
    }
}
