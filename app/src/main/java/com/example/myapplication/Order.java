package com.example.myapplication;

public class Order {

    private int imageResId;
    private String precio;
    private String cantidad;
    private String panId;

    public Order(int imageResId, String precio, String cantidad, String panId) {
        this.imageResId = imageResId;
        this.precio = precio;
        this.cantidad = cantidad;
        this.panId = panId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getPrecio() {
        return precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getPanId() {
        return panId;
    }


}
