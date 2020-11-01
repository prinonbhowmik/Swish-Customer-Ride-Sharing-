package com.hydertechno.swishcustomer.Model;

public class TripReportModel {
    private String r_id;
    private String d_id;
    private String c_id;
    private String issue;
    private String status;

    public TripReportModel(String r_id, String d_id, String c_id, String issue) {
        this.r_id = r_id;
        this.d_id = d_id;
        this.c_id = c_id;
        this.issue = issue;
    }

    public TripReportModel() {
    }

    public String getR_id() {
        return r_id;
    }

    public String getD_id() {
        return d_id;
    }

    public String getC_id() {
        return c_id;
    }

    public String getIssue() {
        return issue;
    }

    public String getStatus() {
        return status;
    }
}
