package com.example.spree;


import java.util.List;

public class ReceiptDetailsDecorator {
    private ReceiptDetails receiptDetails;
    private int maxShopNameLength;
    private int maxShopCategoryLength;
    private int maxShopPhnoLength;
    private int maxShopEmailLength;
    private int maxShopAddressLength;
    private int maxShopGstNumberLength;
    private int maxItemNameLength;

    public ReceiptDetailsDecorator(ReceiptDetails receiptDetails) {
        this.receiptDetails = receiptDetails;
        calculateMaxLengths();
    }

    private void calculateMaxLengths() {
        maxShopNameLength = calculateMaxAttributeLength(receiptDetails.getShopName());
        maxShopCategoryLength = calculateMaxAttributeLength(receiptDetails.getShopCategory());
        maxShopPhnoLength = calculateMaxAttributeLength(receiptDetails.getShopPhno());
        maxShopEmailLength = calculateMaxAttributeLength(receiptDetails.getShopEmail());
        maxShopAddressLength = calculateMaxAttributeLength(receiptDetails.getShopAddress());
        maxShopGstNumberLength = calculateMaxAttributeLength(receiptDetails.getShopGstNumber());

        for (ReceiptDetails.ItemDetails item : receiptDetails.getItemsDetails()) {
            int itemNameLength = calculateMaxAttributeLength(item.getItemName());
            if (itemNameLength > maxItemNameLength) {
                maxItemNameLength = itemNameLength;
            }
        }
    }

    private int calculateMaxAttributeLength(String attribute) {
        return attribute != null ? attribute.length() : 0;
    }

    public String getReceiptAscii() {
        int receiptWidth = calculateReceiptWidth();

        StringBuilder builder = new StringBuilder();
        builder.append("╔").append(generateLine("═", 41)).append("╗\n");
        builder.append("║").append(generateLine(" ", 27)).append(centerText("🌟 RECEIPT DETAILS 🌟", receiptWidth)).append(generateLine(" ", 40)).append("║\n");
        builder.append("╠").append(generateLine("═", 41)).append("╝\n");
        builder.append("║  🏪 Shop Name:        ").append(receiptDetails.getShopName()).append("\n");
        builder.append("║  🏷️ Shop Category:    ").append(receiptDetails.getShopCategory()).append("\n");
        builder.append("║  📞 Shop Phone:       ").append(receiptDetails.getShopPhno()).append("\n");
        builder.append("║  📧 Shop Email:       ").append(receiptDetails.getShopEmail()).append("\n");
        builder.append("║  🏢 Shop Address:     ").append(receiptDetails.getShopAddress()).append("\n");
        builder.append("║  🧾 Shop GST Number:  ").append(receiptDetails.getShopGstNumber()).append("\n");
        builder.append("║  \uD83D\uDCC5 Date of Purchase:  ").append(receiptDetails.getItemsDetails().get(0).getDateOfPurchase()).append("\n");
        builder.append("╠").append(generateLine("═", 41)).append("╗\n");
        builder.append("║").append(generateLine(" ", 41)).append("📦 ITEMS DETAILS 📦").append(generateLine(" ", 31)).append("║\n");
        builder.append("╠").append(generateLine("═", 41)).append("╣\n");
        builder.append("║  🛍️ Item Name").append(generateLine(" ", 19)).append("Quantity").append(generateLine(" ", 19)).append("Total Price").append("║\n");
        builder.append("╠").append(generateLine("═", 41)).append("╝\n");

        for (ReceiptDetails.ItemDetails item : receiptDetails.getItemsDetails()) {
            String itemName = item.getItemName();
            builder.append("║  📦 ").append(itemName).append(generateLine(" ", 20))
                    .append(formatQuantity(item.getQuantity())).append(generateLine(" ", 20))
                    .append(formatPrice(item.getTotalPrice())).append("\n");
        }

        builder.append("╠").append(generateLine("═", 41)).append("╗\n");
        builder.append("║").append(generateLine(" ", 41)).append("Total Spending: ")
                .append(formatPrice(calculateTotalSpending(receiptDetails.getItemsDetails()))).append("\n");

        builder.append("╚").append(generateLine("═", 41)).append("╝\n");

        return builder.toString();
    }

    private double calculateTotalSpending(List<ReceiptDetails.ItemDetails> itemDetails) {
        return itemDetails.stream()
                .mapToDouble(ReceiptDetails.ItemDetails::getTotalPrice)
                .sum();
    }

    private int calculateReceiptWidth() {
        int maxAttributeLength = Math.max(
                maxShopNameLength,
                Math.max(
                        maxShopCategoryLength,
                        Math.max(
                                maxShopPhnoLength,
                                Math.max(
                                        maxShopEmailLength,
                                        Math.max(maxShopAddressLength, maxShopGstNumberLength)
                                )
                        )
                )
        );

        int itemNameColumnWidth = Math.max(maxItemNameLength, "Item Name".length());

        return Math.max(58, Math.max(maxAttributeLength + 30, itemNameColumnWidth + 32));
    }

    private String centerText(String text, int width) {
        int totalSpaces = width - text.length();
        int leftSpaces = totalSpaces / 2;
        int rightSpaces = totalSpaces - leftSpaces;
        return text;
    }

    private String generateLine(String character, int width) {
        return " " + getSpaces(character, 0, width - 2) + " ";
    }

    private String getSpaces(String character, int count, int width) {
        int totalSpaces = width - count;
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < totalSpaces; i++) {
            spaces.append(character);
        }
        return spaces.toString();
    }

    private String formatQuantity(int quantity) {
        return String.format("%-6s", quantity);
    }

    private String formatPrice(double price) {
        return String.format("\u20B9%.2f", price);
    }
}
