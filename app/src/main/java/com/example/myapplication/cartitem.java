package com.example.myapplication;

import android.graphics.Bitmap;
import android.widget.TextView;

public class cartitem {
    private int id;
    private int userid;
    private String productname;
    private Bitmap productimage;
    private int productprice;
    private int selectedQuantity;
    private int productprice2;
    private TextView textView;

    public cartitem(int id, int userid, String productname, Bitmap productimage, int productprice, int productqty, int productprice2,TextView text) {
        this.id = id;
        this.userid = userid;
        this.productname = productname;
        this.productimage = productimage;
        this.productprice = productprice;
        this.selectedQuantity = selectedQuantity;
        this.productprice2 = productprice2;
        this.textView=text;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Bitmap getProductimage() {
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

    public int getProductprice2() {
        return productprice2;
    }

    public void setProductprice2(int productprice2) {this.productprice2 = productprice2;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }


}
