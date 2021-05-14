package com.kcb.mqlService.mqlQueryDomain.mqlData;

public class MQLDataStorage {
    private MQLDataSource mqlDataSource;
    private MQLTable mqlTable;

    public MQLDataStorage(MQLDataSource mqlDataSource, MQLTable mqlTable) {
        this.mqlDataSource = mqlDataSource;
        this.mqlTable = mqlTable;
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
}
