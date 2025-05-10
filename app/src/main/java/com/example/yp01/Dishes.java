package com.example.yp01;

public class Dishes {
    public int Id;
    public int Category;
    public String NameDish;
    public String Price;
    public String Icon;
    public int Version;

    public int getId(){
        return Id;
    }

    public void setId(int Id){
        this.Id = Id;
    }

    public int getCategory(){
        return Category;
    }

    public void setCategory(int Category){
        this.Category = Category;
    }

    public String getNameDish(){
        return NameDish;
    }

    public void setNameDish(String NameDish){
        this.NameDish = NameDish;
    }

    public String getPrice(){
        return Price;
    }

    public void setPrice(String Price){
        this.Price = Price;
    }

    public String getIcon(){
        return Icon;
    }

    public void setIcon(String Icon){
        this.Icon = Icon;
    }

    public int getVersion(){
        return Version;
    }

    public void setVersion(int Version){
        this.Version = Version;
    }
}