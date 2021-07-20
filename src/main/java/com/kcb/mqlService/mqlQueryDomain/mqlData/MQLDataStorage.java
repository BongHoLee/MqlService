package com.kcb.mqlService.mqlQueryDomain.mqlData;

public class MQLDataStorage {
    private MQLDataSource mqlDataSource;
    private MQLTable mqlTable;
    private String queryID;
    private String queryScript;

    public MQLDataStorage(String queryID, String queryScript, MQLDataSource mqlDataSource, MQLTable mqlTable) {
        this.queryID = queryID;
        this.queryScript = queryScript;
        this.mqlDataSource = mqlDataSource;
        this.mqlTable = mqlTable;
    }

    public MQLDataStorage(MQLDataSource mqlDataSource, MQLTable mqlTable) {
        this("", "", mqlDataSource, mqlTable);
    }

    public MQLDataSource getMqlDataSource() {
        return mqlDataSource;
    }

    public MQLTable getMqlTable() {
        return mqlTable;
    }

    public void setMqlTable(MQLTable mqlTable) {
        this.mqlTable = mqlTable;
    }

    public String getQueryID() {
        return queryID;
    }

    public void setQueryID(String queryID) {
        this.queryID = queryID;
    }

    public String getQueryScript() {
        return queryScript;
    }

    public void setQueryScript(String queryScript) {
        this.queryScript = queryScript;
    }
}
