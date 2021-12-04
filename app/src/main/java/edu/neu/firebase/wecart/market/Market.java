package edu.neu.firebase.wecart.market;

public class Market {
    int id;
    String storeName;
    String storeBtn;
    String storeImage;
    String storeDes;
    Double latitude;
    Double longitude;
    String address;
    String email;
    String phone;

    public Market() {
        // default constructor
    }

    public Market(int id, String storeName, String storeBtn, String storeImage, String storeDes, Double latitude, Double longitude, String address, String email, String phone) {
        this.id = id;
        this.storeName = storeName;
        this.storeBtn = storeBtn;
        this.storeImage = storeImage;
        this.storeDes = storeDes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }



}
