package com.example.conor.senan.utils;

import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.conor.senan.R;
import com.example.conor.senan.SenanApp;

import java.util.concurrent.Future;

/**
 * Created by Conor on 03/02/2015.
 */
public class TextViewCountdownTimer {

    CountDownTimer timer;
    TextView view;
    TextView getDealButton;


    public TextViewCountdownTimer(final TextView view, final TextView getDealButton, long time){

        this.view = view;
        this.getDealButton = getDealButton;

        getDealButton.setBackgroundColor(SenanApp.getContext()
                .getResources().getColor(R.color.mytheme_color));

        timer = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {

                long seconds = (millisUntilFinished / 1000) ;
                long minutes = (seconds / 60) ;
                long hours = (minutes / 60) ;
                long days = hours / 24;

                String time = "";
                if(days > 0){
                    time += days + "d ";
                }


                time += String.format("%02d:%02d:%02d",  hours % 24,   minutes % 60, seconds % 60) ;

                view.setText(time);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                view.setText("Over!");
                getDealButton.setBackgroundDrawable(SenanApp.getContext().getResources().getDrawable(R.drawable.diagonal_lines));
            }
        }.start();




    }

    public void cancel(){
        timer.cancel();
    }


}
