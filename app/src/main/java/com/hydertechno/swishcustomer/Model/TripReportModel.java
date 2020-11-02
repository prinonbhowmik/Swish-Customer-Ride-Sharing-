package com.hydertechno.swishcustomer.Model;

public class TripReportModel {
    private String r_id;
    private String d_id;
    private String c_id;
    private String issue;
    private String status;
    private String next_action;

    public TripReportModel(String r_id, String d_id, String c_id, String issue, String status, String next_action) {
        this.r_id = r_id;
        this.d_id = d_id;
        this.c_id = c_id;
        this.issue = issue;
        this.status = status;
        this.next_action = next_action;
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

    public String getNext_action() {
        return next_action;
    }
}
