package com.example.feedbox;

public class AdminHelper
{
    String AdminID, FullName, Department, ContactNumber, Email, Password;
    boolean IsVisible;

    public String getAdminID() {
        return AdminID;
    }

    public String getFullName() {
        return FullName;
    }

    public String getDepartment() {
        return Department;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public boolean isVisible() {
        return IsVisible;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setVisible(boolean visible) {
        IsVisible = visible;
    }

    public AdminHelper(String adminID, String fullName, String department, String contactNumber, String email, String password, boolean isVisible) {
        AdminID = adminID;
        FullName = fullName;
        Department = department;
        ContactNumber = contactNumber;
        Email = email;
        Password = password;
        IsVisible = isVisible;
    }
}
