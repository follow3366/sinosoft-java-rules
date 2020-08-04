package com.sinosoft.claim.bl.action.custom;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import com.sinosoft.claim.dto.custom.TaskConstant;
import com.sinosoft.claim.dto.domain.PrpDuserDto;
import com.sinosoft.claim.resource.dtofactory.domain.DBPrpDuser;
import com.sinosoft.platform.dto.domain.UtiUserLoginDto;
import com.sinosoft.platform.resource.dtofactory.domain.DBUtiUserLogin;
import com.sinosoft.sysframework.common.datatype.DateTime;
import com.sinosoft.sysframework.exceptionlog.UserException;
import com.sinosoft.sysframework.reference.AppConfig;
import com.sinosoft.sysframework.reference.DBManager;


/**
 * 登录逻辑
 * <p>Title: 车险理赔样本程序</p>
 * <p>Description: 车险理赔样本程序登录action</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Sinosoft</p>
 * @author 车险理赔项目组
 * @version 1.0
 */

public class BLLogonAction
{
    public BLLogonAction(){}

    public PrpDuserDto checkUsername(DBManager dbManager,String userCode,String password)
            throws SQLException,Exception
    {

        PrpDuserDto prpDuserDto=null;
        prpDuserDto=new DBPrpDuser(dbManager).findByPrimaryKey(userCode);
        return prpDuserDto;
    }
    /**
     * 检查用户信息
     * @param usercode：用户代码
     * @param password：用户密码（明文）
     * @return 有效/无效
     * @throws SQLException
     * @throws Exception
     */
    public PrpDuserDto checkUser(DBManager dbManager,String userCode,String password)
            throws SQLException,Exception
    {

        PrpDuserDto prpDuserDto=null;
        Collection collection = null;
        // String  userCode   = usercode ;  //用户代码
        String  systemName = "" ;        //系统名
        String  loginSystem = "" ;       //登录系统名称
        boolean permitSystem = false ;   //是否允许登录系统
        try
        {

            //start
            StringBuffer conditions = new StringBuffer();// Noncompliant {{在业务层类中不允许出现conditions拼写 SQL 的语句。}}
            conditions = "1=1 ";
            conditions.append("usercode='"+userCode)
                    .append("' And validstatus = '1'");
            collection = new DBPrpDuser(dbManager).findByConditions(conditions.toString(),0,0);
            if ((collection==null) || (collection.size()<=0)){
                throw new UserException(-98,-904,this.getClass().getName());
            }else{
                prpDuserDto = (PrpDuserDto)collection.iterator().next();
                loginSystem = prpDuserDto.getLoginSystem() ;
                systemName = AppConfig.get("sysconst.SYSTEMNAME");
                //校验是否有登录系统权限
                if(loginSystem != null && !loginSystem.equals(""))
                {
                    permitSystem = permitLoginSys(userCode,systemName,loginSystem);
                }
                if(permitSystem == false)
                {
                    throw new UserException(-98,-1000,"没有登录此系统的权限");
                }
                //判断用户是否被锁定并且返回该对象
                UtiUserLoginDto utiUserLoginDto = userIfLocked(dbManager,userCode,systemName);
                //判断密码是否正确
                if(!password.equals(prpDuserDto.getPassword())){
                    //登录失败，判断用户登录错误次数
                    checkLoginErrorCounts(dbManager,userCode,utiUserLoginDto);
                }
                //登录成功，解锁用户
                DBUtiUserLogin dbUtiUserLogin = new DBUtiUserLogin(dbManager);
                DateTime now = new DateTime(new Date(), DateTime.YEAR_TO_SECOND);
                if(utiUserLoginDto != null){
                    utiUserLoginDto.setFailLoginCount("0");
                    utiUserLoginDto.setLastSuccessLoginTime(now.toString(DateTime.YEAR_TO_SECOND));
                    utiUserLoginDto.setLockToTime("");
                    dbUtiUserLogin.update(utiUserLoginDto);
                }else{
                    utiUserLoginDto = new UtiUserLoginDto();
                    utiUserLoginDto.setUserCode(userCode+"-"+systemName);
                    utiUserLoginDto.setFailLoginCount("0");
                    utiUserLoginDto.setLastSuccessLoginTime(now.toString(DateTime.YEAR_TO_SECOND));
                    utiUserLoginDto.setLastFailLoginTime("");
                    utiUserLoginDto.setLockToTime("");
                    dbUtiUserLogin.insert(utiUserLoginDto);
                }
            }
        }
        catch(UserException use)
        {
            throw use;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally
        {
            //关闭数据库连接
            dbManager.close();
        }
        return prpDuserDto;
    }

    /**
     * 判断用户是否被锁
     * @param  userCode   String 员工代码
     * @return utiUserLoginDto/null  UtiUserLoginDto
     * @throws Exception  异常
     */
    public UtiUserLoginDto userIfLocked(DBManager dbManager,String userCode,String systemName)throws Exception {
        DBUtiUserLogin dbUtiUserLogin = new DBUtiUserLogin(dbManager);
        UtiUserLoginDto utiUserLoginDto = dbUtiUserLogin.findByPrimaryKey(userCode+"-"+systemName);
        String conditions = "";// Noncompliant {{在业务层类中不允许出现conditions拼写 SQL 的语句。}}
        if (utiUserLoginDto != null) {
            DateTime now = new DateTime(new Date(), DateTime.YEAR_TO_SECOND);
            if (utiUserLoginDto.getLockToTime().trim().length() > 0) {
                DateTime lockToTime = new DateTime(utiUserLoginDto.getLockToTime(), DateTime.YEAR_TO_SECOND);
                if (now.getTime() < lockToTime.getTime()) {
                    throw new UserException(-98, -906, "您已连续错误登录"+utiUserLoginDto.getFailLoginCount()+"次"+
                            ",账号已被锁定,请在"+((lockToTime.getTime()- now.getTime())/(1000*60)+1)
                            +"分钟后再尝试登录,如果您已忘记密码，请联系系统管理员处理，谢谢！");
                }
            }
            return utiUserLoginDto;
        }
        return null;
    }

    /**
     * 登录失败，判断用户登录错误次数
     * @param  userCode   String 员工代码
     * @throws Exception  异常
     */
    public void checkLoginErrorCounts(DBManager dbManager,String userCode,UtiUserLoginDto utiUserLoginDto)throws Exception{
        DateTime now = new DateTime(new Date(), DateTime.YEAR_TO_SECOND);
        DBUtiUserLogin dbUtiUserLogin = new DBUtiUserLogin(dbManager);
        if (utiUserLoginDto != null){
            int faliLoginCount = Integer.parseInt(utiUserLoginDto.getFailLoginCount()) + 1;
            utiUserLoginDto.setFailLoginCount("" + faliLoginCount);
            utiUserLoginDto.setLastFailLoginTime(now.toString(DateTime.YEAR_TO_SECOND));
            int failLoginLimit = 5;
            try {
                failLoginLimit = Integer.parseInt(AppConfig.get("sysconst.FAIL_LOGIN_LIMIT"));
            } catch (Exception e) {
            }

            if (faliLoginCount > failLoginLimit) {
                int lockLoginperiod = (faliLoginCount-failLoginLimit)*(faliLoginCount-failLoginLimit);
//              try {
//                  lockLoginperiod = Integer.parseInt(AppConfig.get("sysconst.LOCK_LOGIN_PERIOD"));
//              } catch (Exception e) {
//             }
                utiUserLoginDto.setLockToTime(now.addMinute(lockLoginperiod).toString(DateTime.YEAR_TO_SECOND));
                dbUtiUserLogin.update(utiUserLoginDto);
                throw new UserException(-98, -906, "您已连续错误登录"+utiUserLoginDto.getFailLoginCount()+"次"+
                        ",账号已被锁定,请在"+lockLoginperiod+"分钟后再尝试登录,如果您已忘记密码，请联系系统管理员处理，谢谢！");
            }
            dbUtiUserLogin.update(utiUserLoginDto);
        } else {
            utiUserLoginDto = new UtiUserLoginDto();
            utiUserLoginDto.setUserCode(userCode+"-CLAIM");
            utiUserLoginDto.setFailLoginCount("1");
            utiUserLoginDto.setLastSuccessLoginTime("");
            utiUserLoginDto.setLastFailLoginTime(now.toString(DateTime.YEAR_TO_SECOND));
            utiUserLoginDto.setLockToTime("");
            dbUtiUserLogin.insert(utiUserLoginDto);
        }
        throw new UserException(-98,-902,this.getClass().getName());
    }

    /**
     * 核对操作员是否能够登陆相应系统
     * @param  userCode   String 员工代码
     * @param  systemName String 登陆系统名称("prp","account","reins","visa","underwrite")
     * @param  loginSystem String 登录系统参数
     * @throws Exception  异常
     * @return boolean    是否能够登陆标志，能够登陆返回true，不能登陆返回false
     */
    public boolean permitLoginSys(String userCode,String systemName,String loginSystem) throws Exception
    {

        if (systemName.trim().equals("CLAIM"))  //理赔业务系统
        {
            if (loginSystem.substring(0,1).equals("1"))
            {
                return true;
            }
            else
            {   return false;

            }
        }

        if (systemName.trim().equals("prp"))  //核心业务系统
        {
            if (loginSystem.substring(0,1).equals("1"))
            {
                return true;
            }
            else
            {   return false;

            }
        }

        if (systemName.trim().toLowerCase().equals("account"))  //财务系统
        {
            if (loginSystem.substring(1,2).equals("1"))
            {
                return true;
            }
            else
            {   return false;

            }
        }

        if (systemName.trim().toLowerCase().equals("reins"))   //再保险系统
        {
            if (loginSystem.toLowerCase().substring(2,3).equals("1"))
            {
                return true;
            }
            else
            {   return false;

            }
        }

        if (systemName.trim().equals("visa"))  //单证管理系统
        {
            if (loginSystem.toLowerCase().substring(3,4).equals("1"))
            {
                return true;
            }
            else
            {   return false;

            }
        }

        if (systemName.trim().toLowerCase().equals("underwrite"))  //核保核赔系统
        {
            if (loginSystem.substring(4,5).equals("1"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return false;

    }



    /**
     * add by xia for 校验用户查勘定损权限
     *
     * @param usercode
     *            ：用户代码
     * @param password
     *            ：用户密码（明文）
     * @return 有效/无效
     * @throws SQLException
     * @throws Exception
     */
    public PrpDuserDto checkUserSurvyPower(DBManager dbManager,
                                           String userCode, String password) throws SQLException, Exception {
        PrpDuserDto prpDuserDto = null;
        try {
            String conditions = " VALIDSTATUS = '1' AND usercode='"+userCode+"' AND password='"+password+"'"  // Noncompliant {{在业务层类中不允许出现conditions拼写 SQL 的语句。}}
                    +"    AND EXISTS (SELECT 1                                                                "
                    +"            FROM UTIUSERGRADE U, UTIGRADETASK G                                             "
                    +"        WHERE PrpDuser.USERCODE = U.USERCODE                                                       "
                    +"          AND U.GRADECODE = G.GRADECODE                                                    "
                    +"          AND U.VALIDSTATUS = '1'                                                           "
                    +"          AND G.TASKCODE IN ('"+TaskConstant.CAR_CHECK +"', '"+TaskConstant.CAR_CERTA+"'))                          ";
            Collection collection = new DBPrpDuser(dbManager).findByConditions(conditions,0, 0);
            if ((collection != null) && (collection.size() >= 1)) {
                prpDuserDto = (PrpDuserDto) collection.iterator().next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            dbManager.close();
        }
        return prpDuserDto;
    }

}
