package com.example.feedbox;

public class CategoryHelper
{
    String CategoryID, CategoryName, DatePosted;
    boolean IsVisible;

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getDatePosted() {
        return DatePosted;
    }

    public void setDatePosted(String datePosted) {
        DatePosted = datePosted;
    }

    public boolean isVisible() {
        return IsVisible;
    }

    public void setVisible(boolean visible) {
        IsVisible = visible;
    }

    public CategoryHelper(String categoryID, String categoryName, String datePosted, boolean isVisible) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        DatePosted = datePosted;
        IsVisible = isVisible;
    }
}
