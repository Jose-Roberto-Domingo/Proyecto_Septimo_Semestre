package com.example.e_commerce;

public class Producto {
    private String imagenUrl;
    private String nombre;
    private Double precio;
    private String categoria;


    public Producto() {
    }

    public Producto(String imagenUrl, String nombre, Double precio, String categoria) {
        this.imagenUrl = imagenUrl;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
