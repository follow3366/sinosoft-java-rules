package com.sinosoft.claim.resource.dtofactory.custom;

import java.sql.SQLException;

import com.sinosoft.claim.dto.custom.ClaimAcciPendDto;
import com.sinosoft.claim.dto.domain.PrpLacciPendDto;
import com.sinosoft.claim.resource.dtofactory.domain.DBPrpLacciPend;
import com.sinosoft.claim.resource.dtofactory.domain.DBPrpLacciPendText;
import com.sinosoft.claim.resource.dtofactory.domain.DBPrpLacciPendTrace;
import com.sinosoft.sysframework.reference.DBManager;

public class ABAcciClaimPend {  // Noncompliant {{持久层的类名必须以 DB 开头}}

    public void insert(DBManager dbManager, ClaimAcciPendDto claimAcciPendDto)
            throws SQLException, Exception {

        if (null == claimAcciPendDto.getPrpLacciPendDto()) {
            throw new Exception();
        }
        // 删除未决跟踪主表,其他都是增量数据,不需要删除
        deleteAcciClaimPendDtoSubInfo(dbManager, claimAcciPendDto.getPrpLacciPendDto());

        //插入PrpLacciPend
        new DBPrpLacciPend(dbManager).insert(claimAcciPendDto.getPrpLacciPendDto());

        new DBPrpLacciPendTrace(dbManager).insert(claimAcciPendDto.getPrpLaccipendTraceDto());

        new DBPrpLacciPendText(dbManager).insert(claimAcciPendDto.getPrpLaccipendTextDto());
    }

    private void deleteAcciClaimPendDtoSubInfo(DBManager dbManager,
                                               PrpLacciPendDto prpLacciPendDto) throws SQLException, Exception {

        String statement = " DELETE FROM prpLacciPend where registNo = '"+prpLacciPendDto.getRegistNo()+"' ";
        dbManager.executeUpdate(statement);
    }
}
