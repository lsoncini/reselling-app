package me.raptor.resellingapp.model;

/**
 * Created by Lucas on 18/09/2016.
 */
public class Product {

    private Integer productID;
    private String name;
    private String category;
    private Client buyer;
    private Sale sale;
    private Purchase purchase;
    private Double purchasePrice;
    private Double salePrice;

    public Product(Integer productID, String name, String category, Client buyer, Sale sale, Purchase purchase, Double purchasePrice, Double salePrice) {
        this.productID = productID;
        this.name = name;
        this.category = category;
        this.buyer = buyer;
        this.sale = sale;
        this.purchase = purchase;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return productID.equals(product.productID);

    }

    @Override
    public int hashCode() {
        return productID.hashCode();
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Client getBuyer() {
        return buyer;
    }

    public void setBuyer(Client buyer) {
        this.buyer = buyer;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }
}
