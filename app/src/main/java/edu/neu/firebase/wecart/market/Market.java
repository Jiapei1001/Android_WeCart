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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreBtn() {
        return storeBtn;
    }

    public void setStoreBtn(String storeBtn) {
        this.storeBtn = storeBtn;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getStoreDes() {
        return storeDes;
    }

    public void setStoreDes(String storeDes) {
        this.storeDes = storeDes;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Market{" +
                "id=" + id +
                ", storeName='" + storeName + '\'' +
                ", storeBtn='" + storeBtn + '\'' +
                ", storeImage='" + storeImage + '\'' +
                ", storeDes='" + storeDes + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}