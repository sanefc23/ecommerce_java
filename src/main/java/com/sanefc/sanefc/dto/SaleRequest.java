package com.sanefc.sanefc.dto;

import java.util.List;

public class SaleRequest {
    private Long clientId;
    private List<Long> productIds;
    private int quantity;
    private double total;

    public SaleRequest(Long clientId, List<Long> productIds, int quantity, double total) {
        this.clientId = clientId;
        this.productIds = productIds;
        this.quantity = quantity;
        this.total = total;
    }

    public Long getClientId() {
        return clientId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }
}