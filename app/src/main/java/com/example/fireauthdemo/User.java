package com.example.fireauthdemo;

public class User {
    private String name,des;

    public User() {
    }

    public  User(String name,String des){
        this.name=name;
        this.des=des;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public  String toString(){

      return this.name.toUpperCase() +"\n"+this.des;

}
}
