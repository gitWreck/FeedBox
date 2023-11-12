package com.example.feedbox;

public class GenerateReportHelper {
    String AY_Range, AY_Status;
    int AY_ID;

    public int getAY_ID() {
        return AY_ID;
    }

    public String getAY_Range() {
        return AY_Range;
    }

    public String getAY_Status() {
        return AY_Status;
    }

    public GenerateReportHelper (String ay_range, String ay_status, int ay_id) {
        AY_ID = ay_id;
        AY_Range = ay_range;
        AY_Status = ay_status;
    }
}
