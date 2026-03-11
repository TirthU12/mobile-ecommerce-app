package com.example.myapplication;

import android.graphics.Bitmap;

public class orderitem {
    private int id;
    private int userid;
    private String productname;
    private Bitmap productimage;
    private int productprice;

    private int qty;

    private int orderid;

    private String Status;



    public orderitem(int id, int userid, String productname, Bitmap productimage, int productprice, int qty, int orderid,String Status) {
        this.id = id;
        this.userid = userid;
        this.productname = productname;
        this.productimage = productimage;
        this.productprice = productprice;
        this.qty = qty;
        this.orderid = orderid;
        this.Status = Status;


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

    public int getQty(){return qty;}

    public void setQty(int qty){this.qty = qty;}

    public int getOrderid(){return orderid;}

    public void setOrderid(int orderid){this.orderid = orderid;}

    public String getStatus(){return Status;}

    public void setStatus(String Status){this.Status = Status;}

    }






