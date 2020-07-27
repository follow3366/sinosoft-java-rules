package com.sinosoft.claim.bl.action.custom;

public class BLCertainLossAction {
    public PageRecord findByConditions(DBManager dbManager,String conditions,int pageNo,int rowsPerPage) throws Exception{
        for (int i = 0 ;i<1 ;i++){
            System.out.println(i);
        }
        for (int i = 0 ;i<1 ;i++){
            System.out.println(i);
            PageRecord pageRecord5 = new PageRecord(count,pageNo,1,rowsPerPage,collection);// Noncompliant {{PageRecord 类型的变量命名必须为 pageRecord！}}
        }
        PageRecord pageRecord2 = new PageRecord(count,pageNo,1,rowsPerPage,collection);// Noncompliant {{PageRecord 类型的变量命名必须为 pageRecord！}}
        return pageRecord2;
    }

    public PageRecord findByCondition(DBManager dbManager,String conditions,int pageNo,int rowsPerPage) throws Exception{
        PageRecord pageRecord3 = new PageRecord(count,pageNo,1,rowsPerPage,collection);// Noncompliant {{PageRecord 类型的变量命名必须为 pageRecord！}}
        return pageRecord3;
    }
}
