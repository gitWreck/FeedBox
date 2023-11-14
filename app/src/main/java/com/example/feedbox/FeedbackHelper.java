package com.example.feedbox;

public class FeedbackHelper
{
    String FeedBackID, FirstName, LastName, Details, SubDetails, Reasons,
//            FullName,
            Email, CategoryName, SubCategoryName, Sentiment, Description, Status, SubStatus, DatePosted;

    public String getFeedBackID() {
        return FeedBackID;
    }

    public String getReasons() {
        return Reasons;
    }

    public String getDetails() {
        return Details;
    }

    public String getSubDetails() {
        return SubDetails;
    }

    //    public String getFullName() {
//        return FullName;
//    }
    public String getFirstName() {
    return FirstName;
}
    public String getLastName() {
    return LastName;
}
    public String getEmail() {
        return Email;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public String getSentiment() {
        return Sentiment;
    }

    public String getDescription() {
        return Description;
    }

    public String getStatus() {
        return Status;
    }

    public String getSubStatus() {
        return SubStatus;
    }

    public String getDatePosted() {
        return DatePosted;
    }

    public FeedbackHelper(String feedBackID, String firstName, String lastName,
//                          String fullName,
                          String email, String categoryName, String subCategoryName, String sentiment, String description, String status, String subStatus, String datePosted,
                          String details, String subdetais, String reasons
                          ) {
        FeedBackID = feedBackID;
        FirstName = firstName;
        LastName = lastName;
//        FullName = fullName;
        Email = email;
        CategoryName = categoryName;
        SubCategoryName = subCategoryName;
        Sentiment = sentiment;
        Description = description;
        Status = status;
        SubStatus = subStatus;
        DatePosted = datePosted;
        Details = details;
        SubDetails = subdetais;
        Reasons = reasons;
    }
}
