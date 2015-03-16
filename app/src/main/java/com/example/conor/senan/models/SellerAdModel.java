package com.example.conor.senan.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * A model used to represent an ad
 *
 * Only setting the <code>locked</code> property will
 * will implicitly call the <code>notifyListeners</code> method.
 */
public class SellerAdModel extends SimpleObservable<SellerAdModel>{


    private int id = -1;
    private String title = "";
    private String details = "";
    private String imageName = "";
    private int price = 0;
    private int amountLeft = 0;
    private boolean locked = true;
    private LatLng latLng;

    private int oldPrice = 0;
    private long endTimestamp;

    public SellerAdModel(){
        this.endTimestamp = System.currentTimeMillis();
    }

    @Override
         public String toString() {
        return "SellerAddModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", imageName='" + imageName + '\'' +
                ", price=" + price +
                ", amountLeft=" + amountLeft +
                ", locked=" + locked +
                '}';
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmountLeft() {
        return amountLeft;
    }

    public void setAmountLeft(int amountLeft) {
        this.amountLeft = amountLeft;
    }



    public void setLocked(boolean locked) {
        this.locked = locked;
        notifyListeners(this);
    }

    public boolean isLocked(){
        return locked;
    }


    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;

    }


    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    synchronized public void consume(SellerAdModel model){

        this.id = model.id;
        this.title = model.title;
        this.details = model.details;
        this.imageName = model.imageName;
        this.price = model.price;
        this.amountLeft = model.amountLeft;
        this.locked = model.locked;

        this.latLng = model.latLng;
        this.oldPrice = model.oldPrice;
        this.endTimestamp = model.endTimestamp;

        notifyListeners(this);
    }

    synchronized public void consumeWithoutNotification(SellerAdModel model){
        this.title = model.title;
        this.details = model.details;
        this.imageName = model.imageName;
        this.price = model.price;
        this.amountLeft = model.amountLeft;
        this.locked = model.locked;

        this.latLng = model.latLng;
        this.oldPrice = model.oldPrice;
        this.endTimestamp = model.endTimestamp;

    }


}
