package com.example.feedbox;

public class UserHelper
{
    String UserID, FullName, FirstName, LastName, Email, Position, Status;
    int MuteCounter;
    public String getUserID() {
        return UserID;
    }
    public String getFirstName() {
        return FirstName;
    }
    public String getLastName() {
        return LastName;
    }

    public String getPosition() {
        return Position;
    }

    public String getEmail() {
        return Email;
    }
    public int getMuteCounter() {
        return MuteCounter;
    }


//    public String getFullName() {
//        return FullName;
//    }

//    public String getStatus() {
//        return Status;
//    }

    public UserHelper(String userID, String firstName, String lastName, int muteCounter, String email, String position
//                      String fullName,
//                      String status
    ) {
        UserID = userID;
        FirstName = firstName;
        LastName = lastName;
        MuteCounter = muteCounter;
        Email = email;
        Position = position;
//        FullName = fullName;
//        Status = status;
    }
}
