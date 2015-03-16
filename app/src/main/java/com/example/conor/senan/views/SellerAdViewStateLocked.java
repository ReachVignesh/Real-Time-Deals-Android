package com.example.conor.senan.views;

import android.app.Dialog;
import android.graphics.Paint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.conor.senan.R;
import com.example.conor.senan.utils.MathUtils;
import com.example.conor.senan.utils.TextViewCountdownTimer;

/**
 * This class contains the behaviour of the unlocked ad view.
 */
public class SellerAdViewStateLocked extends SellerAdViewState {

    TextViewCountdownTimer timer;


    public SellerAdViewStateLocked(SellerAdView sellerAdView){
        super(sellerAdView);
    }

    @Override
    public void updateView() {

        super.updateView();

        sellerAdView.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {



                //set up the menu
                Menu menu = sellerAdView.getMenu();
                menu.removeItem(R.id.action_save);
                menu.removeItem(R.id.action_cancel);

                MenuItem menuItem = menu.add(0, R.id.action_edit, Menu.NONE, R.string.edit);
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                menuItem.setIcon(R.drawable.ic_action_edit);



                view.findViewById(R.id.set_image_button).setVisibility(View.GONE);

                //set textviews' text and make them non-editable
                ((TextView)view.findViewById(R.id.title)).setText(sellerAdModel.getTitle());
                view.findViewById(R.id.title).setEnabled(false);

                ((TextView)view.findViewById(R.id.details)).setText(sellerAdModel.getDetails());
                view.findViewById(R.id.details).setEnabled(false);

                ((TextView)view.findViewById(R.id.price))
                        .setText(MathUtils.doubleToNiceString(sellerAdModel.getPrice()/100));

                view.findViewById(R.id.price).setEnabled(false);

                ((TextView)view.findViewById(R.id.old_price)).setText(MathUtils.doubleToNiceString(sellerAdModel.getOldPrice() / 100));
                view.findViewById(R.id.old_price).setEnabled(false);




                ((TextView)view.findViewById(R.id.amount_left)).setText(String.valueOf(sellerAdModel.getAmountLeft()));
                view.findViewById(R.id.amount_left).setEnabled(false);

                view.findViewById(R.id.date_display_container).setVisibility(View.GONE);
                view.findViewById(R.id.countdown_display_container).setVisibility(View.VISIBLE);

                //set up the timer
                timer = new TextViewCountdownTimer(
                        (TextView)view.findViewById(R.id.countdown_timer),
                        (TextView)view.findViewById(R.id.get_deal_button),
                        sellerAdModel.getEndTimestamp() - System.currentTimeMillis()
                );

                view.findViewById(R.id.social_container).setVisibility(View.VISIBLE);

                /*((TextView)view.findViewById(R.id.address))
                        .setText(GoogleMapUtils.getAddress(sellerAddView.getMapMarker().getPosition()));

                view.findViewById(R.id.address).setVisibility(View.VISIBLE);
*/

                SellerAdViewStateLocked.super.onFinishedUpdate();
            }
        });

    }

    @Override
    public void endUpdate(){
        if(timer != null)
            timer.cancel();
    }


    @Override
    public void updateMap() {


        super.updateMap();

        /*sellerAdView.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


            }

        });*/
    }

}
