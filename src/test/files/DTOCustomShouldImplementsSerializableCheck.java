package com.sinosoft.claim.dto.custom;

import java.io.Serializable;
import java.util.*;
import com.sinosoft.claim.dto.domain.*;


public class AnaResultDto // Noncompliant {{com.sinosoft.claim.dto.custom 包下的类必须实现java.io.Serializable 接口。}}
{
    /**记录数*/
    private int rowsCount = 0;
    /**结果记录集*/
    private Collection collection = new ArrayList();

    /**
     * 默认构造方法,构造一个默认的AnaResultDto对象
     */
    public AnaResultDto()
    {
    }

    public void setRowsCount(int rowsCount)
    {
        this.rowsCount = rowsCount;
    }

    public int getRowsCount()
    {
        return rowsCount;
    }

    public void setCollection(Collection collection)
    {
        this.collection = collection;
    }

    public Collection getCollection()
    {
        return collection;
    }
}

