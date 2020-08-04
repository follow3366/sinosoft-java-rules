package com.sinosoft.claim.bl.facade;

import java.sql.SQLException;

import com.sinosoft.claim.bl.action.custom.BLClaimPendAction;
import com.sinosoft.claim.dto.custom.ClaimPendDto;
import com.sinosoft.sysframework.reference.AppConfig;
import com.sinosoft.sysframework.reference.DBManager;

public class BLClaimPendFacade {

    public void save(ClaimPendDto claimPendDto) throws SQLException, Exception {
        // 创建数据库管理对象
        DBManager dbManager = new DBManager();
        dbManager.open(AppConfig.get("sysconst.DBJNDI"));
        // 开始事务
        dbManager.beginTransaction();
        new DBClaimPend1().save(dbManager, claimPendDto); // Noncompliant {{BLfacade 层不能调用持久层的类}}
        try {
            new DBClaimPend2().save(dbManager, claimPendDto); // Noncompliant {{BLfacade 层不能调用持久层的类}}
            // 提交事务
            dbManager.commitTransaction();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            // 回滚事务
            dbManager.rollbackTransaction();
            throw sqle;
        } catch (Exception ex) {
            ex.printStackTrace();
            // 回滚事务
            dbManager.rollbackTransaction();
            throw ex;
        } finally {
            // 关闭数据库连接
            dbManager.close();
        }
        new DBClaimPend3().save(dbManager, claimPendDto); // Noncompliant {{BLfacade 层不能调用持久层的类}}
    }
}
