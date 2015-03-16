package com.example.conor.senan.lists;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.conor.senan.R;
import com.example.conor.senan.models.SellerAdModel;
import com.example.conor.senan.utils.ImageUtils;
import com.example.conor.senan.utils.MathUtils;

import java.util.List;


public class AdListAdapter extends ArrayAdapter<SellerAdModel> {


    private LayoutInflater inflater;

    public AdListAdapter(Context context, List<SellerAdModel> items) {
        super(context, R.layout.ad_list_item, items);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Populates a view with details of an add.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SellerAdModel sellerAdModel = getItem(position);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.ad_list_item, parent, false);

            ((TextView)convertView.findViewById(R.id.old_price_cur))
                    .setPaintFlags(((TextView)convertView.findViewById(R.id.old_price)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



            ((TextView)convertView.findViewById(R.id.old_price))
                    .setPaintFlags(((TextView)convertView.findViewById(R.id.old_price)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }


        Bitmap image;
        String name = "add_image_" + sellerAdModel.getId();
        image = ImageUtils.loadImageFromStorage(name);

        ((ImageView)convertView.findViewById(R.id.image)).setImageBitmap(image);
        ((TextView)convertView.findViewById(R.id.title)).setText(sellerAdModel.getTitle());


        ((TextView)convertView.findViewById(R.id.price))
                .setText( MathUtils.doubleToNiceString(sellerAdModel.getPrice()/100) );

        ((TextView)convertView.findViewById(R.id.old_price))
                .setText( MathUtils.doubleToNiceString(sellerAdModel.getOldPrice()/100) );

        ((TextView)convertView.findViewById(R.id.amount_left)).setText(String.valueOf(sellerAdModel.getAmountLeft()));

        return convertView;


    }

}
