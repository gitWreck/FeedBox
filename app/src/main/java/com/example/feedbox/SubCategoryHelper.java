package com.example.feedbox;

public class SubCategoryHelper
{
    String SubCategoryID, CategoryID, SubCategoryName, DatePosted, CategoryName;

    public String getSubCategoryID() {
        return SubCategoryID;
    }

    public void setSubCategoryID(String subCategoryID) {
        SubCategoryID = subCategoryID;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getDatePosted() {
        return DatePosted;
    }

    public void setDatePosted(String datePosted) {
        DatePosted = datePosted;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public SubCategoryHelper(String subCategoryID, String categoryID, String subCategoryName, String datePosted, String categoryName) {
        SubCategoryID = subCategoryID;
        CategoryID = categoryID;
        SubCategoryName = subCategoryName;
        DatePosted = datePosted;
        CategoryName = categoryName;
    }
}
