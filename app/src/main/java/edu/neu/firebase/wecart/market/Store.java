package edu.neu.firebase.wecart.market;

public class Store {

    private int storeId;
    private String storeName;
    private String storeBtn;
    private String storeImage;
    private String storeDes;
    private Double latitude;
    private Double longitude;
    private String ownerImage;
    private String ownerName;
    private String address;
    private String email;
    private String phone;

    public Store() {
        // default constructor
    }

//    public Store(int storeId, String storeName, String storeBtn, String storeImage, String storeDes, Double latitude, Double longitude, String ownerImage, String ownerName, String address, String email, String phone) {
//        this.storeId = storeId;
//        this.storeName = storeName;
//        this.storeBtn = storeBtn;
//        this.storeImage = storeImage;
//        this.storeDes = storeDes;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.ownerImage = ownerImage;
//        this.ownerName = ownerName;
//        this.address = address;
//        this.email = email;
//        this.phone = phone;
//    }


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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
                "id=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storeBtn='" + storeBtn + '\'' +
                ", storeImage='" + storeImage + '\'' +
                ", storeDes='" + storeDes + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", ownerImage='" + ownerImage + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}