package edu.neu.firebase.wecart;

public class Product {

    private int productId;
    private String productName;
    private String productBrand;
    private String productStore;
    private int storeId;
    private double price;
    private int quantity;
    private String productImageId;
    private Boolean inStock;
    private String productUnit;
    // Use for multiple filters with firebase
    private String storeIdToProductId;

    private int totalInCart;

    public int getTotalInCart() {
        return totalInCart;
    }

    public void setTotalInCart(int totalInCart) {
        this.totalInCart = totalInCart;
    }

    public Product() {
        // Default constructor
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductStore() {
        return productStore;
    }

    public void setProductStore(String productStore) {
        this.productStore = productStore;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(String productImageId) {
        this.productImageId = productImageId;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getStoreIdToProductId() {
        return getStoreId() + "_" + getProductId();
    }

    public void setStoreIdToProduct(String storeIdToProduct) {
        this.storeIdToProductId = storeIdToProduct;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productBrand='" + productBrand + '\'' +
                ", productStore='" + productStore + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", productImageId='" + productImageId + '\'' +
                ", inStock=" + inStock +
                ", productUnit='" + productUnit + '\'' +
                '}';
    }
}
