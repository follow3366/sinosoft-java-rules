package com.sinosoft.resource.dtofactory.domain;

import java.sql.*;
import java.util.*;
import com.sinosoft.sysframework.log.*;
import com.sinosoft.sysframework.common.util.*;
import com.sinosoft.sysframework.common.datatype.DateTime;
import com.sinosoft.sysframework.reference.DBManager;
import com.sinosoft.dto.domain.PrpPproductDto;

public class DBPrpPproductBase{
    public int getCount(String conditions)
            throws Exception{
        int count = -1;
        StringBuffer buffer = new StringBuffer(100);
        buffer.append("SELECT count(*) FROM (SELECT * FROM PrpPproduct WHERE ");
        buffer.append("SELECT prpdcode FROM PrpPproduct WHERE prpdcode like '%2020%'");// Noncompliant {{like子句尽量前端匹配，前端匹配可以使用索引，其他不能使用索引。}}
        buffer.append(conditions);
        buffer.append(")");
        ResultSet resultSet = dbManager.executeQuery(buffer.toString());
        resultSet.next();
        count = dbManager.getInt(resultSet,1);
        resultSet.close();
        return count;
    }
}
