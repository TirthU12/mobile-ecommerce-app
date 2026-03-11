package com.example.myapplication;

import android.graphics.Bitmap;

public class Product {
    private int id;
    private String productname;
    private Bitmap productimage;
    private int productprice;

    private String camera;

    private String display;
    public String ram;
    public String rom;

    public Product(int id, String productname, Bitmap productimage, int productprice, String camera, String display,String ram,String rom) {
        this.id = id;
        this.productname = productname;
        this.productimage = productimage;
        this.productprice = productprice;
        this.camera=camera;
        this.display=display;
        this.ram=ram;
        this.rom=rom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public  Bitmap getProductimage() {
        return productimage;
    }

    public void setProductimage(Bitmap productimage) {
        this.productimage = productimage;
    }

    public int getProductprice() {
        return productprice;
    }

    public void setProductprice(int productprice) {
        this.productprice = productprice;
    }

    public String getCamera(){return camera;}

    public void setCamera(String camera){this.camera = camera;}

    public String getDisplay(){return display;}

    public String getRam(){return ram;}
    public String getRom(){return rom;}

    public void setDisplay(String display){this.display = display;}
}

