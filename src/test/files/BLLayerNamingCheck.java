package com.sinosoft.claim.bl.action;

import java.sql.SQLException;
import java.util.*;
import com.sinosoft.sysframework.reference.*;
import com.sinosoft.claim.bl.action.custom.BLRegistAction;
import com.sinosoft.claim.bl.action.custom.BLAcciCheckAction;
import com.sinosoft.claim.bl.action.domain.BLPrpLregistAction;
import com.sinosoft.claim.dto.custom.*;
import com.sinosoft.claim.dto.domain.PrpLregistDto;
import com.sinosoft.claim.dto.custom.WorkFlowDto;
import com.sinosoft.claim.bl.action.custom.BLWorkFlowAction;
/**
 *  报案逻辑分发
 * <p>Title: 车险理赔报案</p>
 * <p>Description: 车险理赔报案facade</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Sinosoft</p>
 * @author weishixin
 * @version 1.0
 */
public class LLLLLLAcciCheck  // Noncompliant {{业务层的类必须以 BL 开头。}}
{
    /**
     * 保存报案
     * @param registDto：自定义报案对象
     * @throws SQLException
     * @throws Exception
     */
    public void save(RegistDto registDto) throws SQLException,Exception
    {
        //创建数据库管理对象
        DBManager dbManager = new DBManager();
        dbManager.open(AppConfig.get("sysconst.DBJNDI")) ;
        //开始事务
        dbManager.beginTransaction();
        try
        {
            new BLAcciCheckAction().save(dbManager,registDto);
            //提交事务
            dbManager.commitTransaction();
        }
        catch(SQLException sqle)
        {
            //回滚事务
            dbManager.rollbackTransaction();
            throw sqle;
        }
        catch(Exception ex)
        {
            //回滚事务
            dbManager.rollbackTransaction();
            throw ex;
        }
        finally
        {
            //关闭数据库连接
            dbManager.close();
        }
    }

    /**
     * 保存报案带工作流处理的过程
     * @param registDto：自定义报案对象
     * @throws SQLException
     * @throws Exception
     */
    public void save(RegistDto registDto,WorkFlowDto workFlowDto) throws SQLException,Exception
    {
        //创建数据库管理对象
        DBManager dbManager = new DBManager();
        dbManager.open(AppConfig.get("sysconst.DBJNDI")) ;
        //开始事务
        dbManager.beginTransaction();
        try
        {
            new BLRegistAction().save(dbManager,registDto);

            if(workFlowDto!=null){
                new BLWorkFlowAction().deal(dbManager,workFlowDto);
            }

            //提交事务
            dbManager.commitTransaction();
        }
        catch(SQLException sqle)
        {
            //回滚事务
            dbManager.rollbackTransaction();
            throw sqle;
        }
        catch(Exception ex)
        {
            //回滚事务
            dbManager.rollbackTransaction();
            throw ex;
        }
        finally
        {
            //关闭数据库连接
            dbManager.close();
        }
    }

    /**
     * 删除报案
     * @param  registNo
     * @param dbManager  数据连接
     * @throws SQLException
     * @throws Exception
     */
    public void delete(String registNo) throws SQLException,Exception
    {
        //创建数据库管理对象
        DBManager dbManager = new DBManager();
        dbManager.open(AppConfig.get("sysconst.DBJNDI")) ;
        //开始事务
        dbManager.beginTransaction();
        try
        {
            new BLRegistAction().delete(dbManager,registNo);

            //工作流删除
            new BLWorkFlowAction().deleteByRegistNo(dbManager,registNo);
            //放回号码



            //提交事务
            dbManager.commitTransaction();
        }
        catch(SQLException sqle)
        {
            //回滚事务
            dbManager.rollbackTransaction();
            throw sqle;
        }
        catch(Exception ex)
        {
            //回滚事务
            dbManager.rollbackTransaction();
            throw ex;
        }
        finally
        {
            //关闭数据库连接
            dbManager.close();
        }

    }
    /**
     * 获得报案
     * @param  registNo
     * @param dbManager  数据连接
     * @return 自定义报案对象
     * @throws SQLException
     * @throws Exception
     */
    public AcciCheckDto findByPrimaryKey(String registNo) throws SQLException,Exception
    {
        AcciCheckDto acciCheckDto = null;
        //创建数据库管理对象
        DBManager dbManager = new DBManager();
        dbManager.open(AppConfig.get("sysconst.DBJNDI")) ;
        try
        {
            acciCheckDto = new BLAcciCheckAction().findByPrimaryKey(dbManager,registNo);
        }
        catch(SQLException sqle)
        {
            throw sqle;
        }
        catch(Exception ex)
        {
            throw ex;
        }
        finally
        {
            //关闭数据库连接
            dbManager.close();
        }
        return acciCheckDto;
    }

    /**
     * 判断报案通知号是否存在
     * @param registNo
     * @param dbManager  数据连接
     * @return 是/否
     * @throws SQLException
     * @throws Exception
     */
    public boolean isExist(String registNo) throws SQLException,Exception
    {

        boolean exist = false ;
        //创建数据库管理对象
        DBManager dbManager = new DBManager();
        dbManager.open(AppConfig.get("sysconst.DBJNDI")) ;
        try
        {
            exist = new BLRegistAction().isExist(dbManager,registNo);
        }
        catch(SQLException sqle)
        {
            throw sqle;
        }
        catch(Exception ex)
        {
            throw ex;
        }
        finally
        {
            //关闭数据库连接
            dbManager.close();
        }
        return exist;
    }

    /**
     * 根据条件查询报案主表信息
     * @param conditions String
     * @throws Exception
     * @return Collection
     */
    public Collection findByConditions(String conditions) throws Exception
    {
        DBManager dbManager = new DBManager();
        PrpLregistDto prpLregistDto = null;
        Collection registList = new ArrayList();
        try
        {

            dbManager.open(AppConfig.get("sysconst.DBJNDI")) ;
            // BLPrpLregistAction blPrpLregistAction = new BLPrpLregistAction();
            BLPrpLregistAction blPrpLregistAction = new BLPrpLregistAction();
            registList = (Collection)blPrpLregistAction.findByConditions(dbManager,conditions);
        }
        catch(SQLException sqle)
        {
            throw sqle;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally
        {
            dbManager.close();
        }
        return registList;
    }

    /**
     * 根据条件查询报案主表信息
     * @param conditions String
     * @throws Exception
     * @return Collection
     * Add By sunhao 2004-08-24 Reason:增加新的查询方法
     */
    public Collection findByQueryConditions(String conditions) throws Exception
    {
        DBManager dbManager = new DBManager();
        PrpLregistDto prpLregistDto = null;
        Collection registList = new ArrayList();
        try
        {

            dbManager.open(AppConfig.get("sysconst.DBJNDI")) ;
            // BLPrpLregistAction blPrpLregistAction = new BLPrpLregistAction();
            BLRegistAction blRegistAction = new BLRegistAction();
            registList = (Collection)blRegistAction.findByQueryConditions(dbManager,conditions,0,0);
        }
        catch(SQLException sqle)
        {
            throw sqle;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally
        {
            dbManager.close();
        }
        return registList;
    }

    /**
     * 根据条件查询报案信息
     * @param conditions String
     * @throws Exception
     * @return Collection
     */
    public Collection findSamePolicyRegist(String conditions) throws Exception
    {
        DBManager dbManager = new DBManager();
        PrpLregistDto prpLregistDto = null;
        Collection registList = new ArrayList();
        try
        {

            dbManager.open(AppConfig.get("sysconst.DBJNDI")) ;
            // BLPrpLregistAction blPrpLregistAction = new BLPrpLregistAction();
            BLRegistAction blRegistAction = new BLRegistAction();
            registList = (Collection)blRegistAction.findSamePolicyRegist(dbManager,conditions,0,0);
        }
        catch(SQLException sqle)
        {
            throw sqle;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally
        {
            dbManager.close();
        }
        return registList;
    }

    /**
     * 根据条件查询报案主表信息
     * @param conditions String
     * @throws Exception
     * @return Collection
     * Add By sunhao 2004-08-24 Reason:增加新的查询方法
     */
    public Collection getWorkFlowList(String conditions) throws Exception
    {
        DBManager dbManager = new DBManager();
        PrpLregistDto prpLregistDto = null;
        Collection registList = new ArrayList();
        try
        {

            dbManager.open(AppConfig.get("sysconst.DBJNDI")) ;
            // BLPrpLregistAction blPrpLregistAction = new BLPrpLregistAction();
            BLRegistAction blRegistAction = new BLRegistAction();
            registList = (Collection)blRegistAction.getWorkFlowList(dbManager,conditions,0,0);
        }
        catch(SQLException sqle)
        {
            throw sqle;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally
        {
            dbManager.close();
        }
        return registList;
    }

    /**
     * 获得相关的节点信息
     * @param  registNo
     * @param dbManager  数据连接
     * @return 自定义报案对象
     * @throws SQLException
     * @throws Exception
     */
    public CaseRelateNodeDto relateNode(String registNo) throws SQLException,Exception
    {
        CaseRelateNodeDto caseRelateNodeDto = null;
        //创建数据库管理对象
        DBManager dbManager = new DBManager();
        dbManager.open(AppConfig.get("sysconst.DBJNDI")) ;
        try
        {
            caseRelateNodeDto = new BLRegistAction().relateNode(dbManager,registNo);
        }
        catch(SQLException sqle)
        {
            throw sqle;
        }
        catch(Exception ex)
        {
            throw ex;
        }
        finally
        {
            //关闭数据库连接
            dbManager.close();
        }
        return caseRelateNodeDto;
    }

}
