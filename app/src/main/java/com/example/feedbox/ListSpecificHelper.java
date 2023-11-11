package com.example.feedbox;

public class ListSpecificHelper {
    String  Spc_d_Cat ,Spc_d_Name, Sc_Name, Cat_Name;
    int Spc_d_ID;
    public int getSpc_d_ID() {
        return Spc_d_ID;
    }

    public void setSpc_d_ID(int spc_d_ID) {
        Spc_d_ID = spc_d_ID;
    }

    public String getSpc_d_Cat() {
        return Spc_d_Cat;
    }

    public void setSpc_d_Cat(String spc_d_Cat) {
        Spc_d_Cat = spc_d_Cat;
    }

    public String getSpc_d_Name() {
        return Spc_d_Name;
    }

    public void setSpc_d_Name(String spc_d_Name) {
        Spc_d_Name = spc_d_Name;
    }

    public String getSc_Name() {
        return Sc_Name;
    }

    public void setSc_Name(String sc_Name) {
        Sc_Name = sc_Name;
    }

    public void setCat_Name(String cat_Name) {
        Cat_Name = cat_Name;
    }

    public String getCat_Name() {
        return Cat_Name;
    }

    public ListSpecificHelper(int spc_d_ID, String spc_d_Cat, String spc_d_Name, String sc_name, String cat_name ) {
        Spc_d_ID = spc_d_ID;
        Spc_d_Cat = spc_d_Cat;
        Spc_d_Name = spc_d_Name;
        Sc_Name = sc_name;
        Cat_Name =cat_name;
    }
}
