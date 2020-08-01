package com.sinosoft.claim.bl.facade;

import org.apache.log4j.Logger;

import com.sinosoft.claim.bl.action.domain.BLCiClaimUploadDetailLogAction;
import com.sinosoft.claim.bl.action.domain.BLCiClaimUploadMainLogAction;
import com.sinosoft.claim.dto.domain.CiClaimUploadMainLogDto;
import com.sinosoft.sysframework.reference.DBManager;

/**
 * 这是CIClaimUploadMainLog的业务对象Facade类<br>
 */
public class BLCiClaimUploadMainLogFacade extends BLCiClaimUploadMainLogFacadeBase{
    private static Logger logger = Logger.getLogger(BLCiClaimUploadMainLogFacade.class);
    private static int i; // Noncompliant {{com.sinosoft.claim.bl.facade 包下类不能定义任何变量。}}
    private String str; // Noncompliant {{com.sinosoft.claim.bl.facade 包下类不能定义任何变量。}}

    /**
     * 构造函数
     */
    public BLCiClaimUploadMainLogFacade(){
        super();
    }

    /**
     * 插入一条数据
     * @param ciClaimUploadMainLogDto ciClaimUploadMainLogDto
     * @throws Exception
     */
    public void insertMainAndDetailLog(CiClaimUploadMainLogDto ciClaimUploadMainLogDto)
            throws Exception{
        DBManager dbManager = new DBManager();
        BLCiClaimUploadMainLogAction blCiClaimUploadMainLogAction = new BLCiClaimUploadMainLogAction();
        BLCiClaimUploadDetailLogAction blCiClaimUploadDetailLogAction = new BLCiClaimUploadDetailLogAction();
        try{
            dbManager.open("claimDataSource");
            dbManager.beginTransaction();
            //插入记录
            blCiClaimUploadMainLogAction.insert(dbManager, ciClaimUploadMainLogDto);
            blCiClaimUploadDetailLogAction.insertAll(dbManager, ciClaimUploadMainLogDto.getDetailList());
            dbManager.commitTransaction();
        }catch(Exception exception){
            dbManager.rollbackTransaction();
            throw exception;
        }finally{
            dbManager.close();
        }
    }

}
