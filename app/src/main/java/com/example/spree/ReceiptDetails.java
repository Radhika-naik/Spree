package com.example.spree;

import java.util.List;
import java.util.UUID;

public class ReceiptDetails {

    private String receipt_id;
    private String shop_name;
    private String shop_category;
    private String shop_phno;
    private String shop_email;
    private String shop_address;
    private String shop_gst_number;
    private String seller_password;
    private List<ItemDetails> items_details;

    public ReceiptDetails() {
        // Generate a unique receipt_id using UUID
        receipt_id = UUID.randomUUID().toString();
    }

    public String getReceiptId() {
        return receipt_id;
    }

    public String getShopName() {
        return shop_name;
    }

    public void setShopName(String shopName) {
        this.shop_name = shopName;
    }

    public String getShopCategory() {
        return shop_category;
    }

    public void setShopCategory(String shopCategory) {
        this.shop_category = shopCategory;
    }

    public String getShopPhno() {
        return shop_phno;
    }

    public void setShopPhno(String shopPhno) {
        this.shop_phno = shopPhno;
    }

    public String getShopEmail() {
        return shop_email;
    }

    public void setShopEmail(String shopEmail) {
        this.shop_email = shopEmail;
    }

    public String getShopAddress() {
        return shop_address;
    }

    public void setShopAddress(String shopAddress) {
        this.shop_address = shopAddress;
    }

    public String getShopGstNumber() {
        return shop_gst_number;
    }

    public void setShopGstNumber(String shopGstNumber) {
        this.shop_gst_number = shopGstNumber;
    }

    public String getSellerPassword() {
        return seller_password;
    }

    public void setSellerPassword(String sellerPassword) {
        this.seller_password = sellerPassword;
    }

    public List<ItemDetails> getItemsDetails() {
        return items_details;
    }

    public void setItemsDetails(List<ItemDetails> itemsDetails) {
        this.items_details = itemsDetails;
    }

    public static class ItemDetails {
        private String item_name;
        private int quantity;
        private double total_price;
        private String date_of_purchase;

        public String getItemName() {
            return item_name;
        }

        public void setItemName(String itemName) {
            this.item_name = itemName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getTotalPrice() {
            return total_price;
        }

        public void setTotalPrice(double totalPrice) {
            this.total_price = totalPrice;
        }

        public String getDateOfPurchase() {
            return date_of_purchase;
        }

        public void setDateOfPurchase(String dateOfPurchase) {
            this.date_of_purchase = dateOfPurchase;
        }
    }
}
