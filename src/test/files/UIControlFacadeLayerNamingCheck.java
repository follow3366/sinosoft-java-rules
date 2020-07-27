package com.sinosoft.claim.ui.control.facade;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.json.JSONArray;
import org.json.JSONObject;


import com.sinosoft.claim.bl.facade.BLPrpLlockFacade;
import com.sinosoft.claim.bl.facade.BLPrpLregistFacade;
import com.sinosoft.claim.dto.domain.PrpLlockDto;
import com.sinosoft.claim.dto.domain.PrpLregistDto;
import com.sinosoft.claim.util.BusinessRuleUtil;
import com.sinosoft.claimciplatform.bl.facade.BLCIClaimPlatFormInterfaceFacade;
import com.sinosoft.claimciplatform.dto.custom.AccountDto;
import com.sinosoft.claimciplatform.dto.custom.Iconstants;
import com.sinosoft.claimciplatform.dto.custom.PacketInfoDto;
import com.sinosoft.claimciplatform.dto.custom.ReturnAccountsInfoDto;
import com.sinosoft.claimciplatform.dto.custom.ReturnInfo;
import com.sinosoft.sysframework.common.util.DataUtils;
import com.sinosoft.sysframework.exceptionlog.UserException;

/**
 * 结算查询接口调用（只做查询功能）
 * @author Administrator
 *
 */
public class AuditSearchFacade  extends Action{ // Noncompliant {{重命名此类名以匹配 '^UI[A-Z][a-zA-Z0-9]*Facade$' 正则表达式}}

    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm, HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse) throws Exception {


        String msgType = httpServletRequest.getParameter("msgType");
        if(null!=msgType){
            String registNo = httpServletRequest.getParameter("registNo");
            String isNewCommercialCase = "";
            if(BusinessRuleUtil.isNewCommercialCase(registNo, "RegistNo")){
                isNewCommercialCase = "true";
            }else{
                isNewCommercialCase = "false";
            }
            httpServletResponse.getWriter().print(isNewCommercialCase);
            return null;
        }
        String forward = ""; // 向前
        String registNo = httpServletRequest.getParameter("registNo"); // 清算码
        String accountsNoStatus = httpServletRequest.getParameter("AccountsNoStatus"); // 清算码状态
        String coverageCode = httpServletRequest.getParameter("CoverageCode"); // 追偿/清付险种
        String accountDateStart = httpServletRequest.getParameter("AccountDateStart"); // 清算时间
        String accountDateEnd = httpServletRequest.getParameter("AccountDateEnd");
        String recoverComCode = httpServletRequest.getParameter("RecoverComCode"); // 追偿方保险公司
        String recoverAreaCode = httpServletRequest.getParameter("RecoverAreaCode"); // 追偿方承保地区
        String recoverAmountStart = DataUtils.nullToZero(httpServletRequest.getParameter("RecoverAmountStart")); // 追偿金额
        String recoverAmountEnd = DataUtils.nullToZero(httpServletRequest.getParameter("RecoverAmountEnd"));
        String compAmountStart = DataUtils.nullToZero(httpServletRequest.getParameter("CompAmountStart")); // 清付金额
        String compAmountEnd = DataUtils.nullToZero(httpServletRequest.getParameter("CompAmountEnd"));
        String acrossProvinceFlag = httpServletRequest.getParameter("AcrossProvinceFlag");//跨省标志
        try {
            if(null != accountDateStart && accountDateStart.length()>0){
                accountDateStart = accountDateStart + " 00:00";
            }
            if(null != accountDateEnd && accountDateEnd.length()>0){
                accountDateEnd = accountDateEnd + " 24:00";
            }
            //首先查询报案号是否存在
            PrpLregistDto prpLregistDto = new BLPrpLregistFacade().findByPrimaryKey(registNo);
            if(null == prpLregistDto){
                throw new UserException(1,3,"清付查询","输入的报案号不存在，请确定后重新输入！");
            }
            if(null != prpLregistDto && !prpLregistDto.getRiskCode().substring(0, 2).equals("05")){
                throw new UserException(1,3,"该功能只针对于车险，其他险种不支持，请重新输入报案号进行查询！");
            }

            BLPrpLlockFacade blPrpLLockFacade = new BLPrpLlockFacade();
            AccountDto accountDto = new AccountDto();
            PacketInfoDto packetInfoDto = new PacketInfoDto();
            Collection prpLLockColl = blPrpLLockFacade.findByConditions(" registNo ='" + registNo + "' and recoverycodestatus <> '9' "); // 获取清算码
            ArrayList list =new ArrayList();
            if(null != prpLLockColl && prpLLockColl.size()>0){
                Iterator it  = prpLLockColl.iterator();
                while(it.hasNext()){
                    PrpLlockDto prpLlockDto = (PrpLlockDto)it.next();
                    if(null != prpLlockDto){
                        accountDto.setAccountDateEnd(accountDateEnd);
                        accountDto.setAccountDateStart(accountDateStart);
                        accountDto.setAccountsNo(prpLlockDto.getRecoveryCode());
                        accountDto.setAccountsNoStatus(accountsNoStatus);
                        accountDto.setCompAmountEnd(Double.parseDouble(compAmountEnd));
                        accountDto.setCompAmountStart(Double.parseDouble(compAmountStart));
                        accountDto.setCoverageCode(coverageCode);
                        accountDto.setOppoentAreaCode(recoverAreaCode);
                        accountDto.setOppoentCompanyCode(recoverComCode);
                        accountDto.setRecoverAmountEnd(Double.parseDouble(recoverAmountEnd));
                        accountDto.setRecoverAmountStart(Double.parseDouble(recoverAmountStart));
                        accountDto.setRecoverStatus(prpLlockDto.getRecoverStatus());
                        accountDto.setPolicyNo(prpLregistDto.getPolicyNo());
                        accountDto.setComcode(prpLregistDto.getComCode());
                        accountDto.setAcrossProvinceFlag(Integer.parseInt(acrossProvinceFlag));
                        //首次查询默认数据包序号为1
                        packetInfoDto.setPacketNo(1);
                        accountDto.setPacketInfoDto(packetInfoDto);
                        //进行交互
                        ReturnAccountsInfoDto  returnInfo = BLCIClaimPlatFormInterfaceFacade.getInstance().getReturnAuditSearchDto(accountDto,Iconstants.RecoveryRequestType.RECOVERY_AUDITSEACH);
                        if(null != returnInfo && returnInfo.getAccountsInfoDtoList() != null && returnInfo.getAccountsInfoDtoList().size()>0){
                            list.addAll(returnInfo.getAccountsInfoDtoList());
                        }
                        //判断是否是新商业险，是否走新的结算流程 （根据返回数据判断是否需继续进行请求获取完整-结算查询-数据）
                        if(BusinessRuleUtil.isNewCommercialCase(prpLregistDto.getPolicyNo(), "PolicyNo")){
                            if(null!=returnInfo && null!=returnInfo.getPacketInfo()){
                                for(int i=2;i<=returnInfo.getPacketInfo().getTotalPacketCount();i++){
                                    packetInfoDto.setPacketNo(i);
                                    accountDto.setPacketInfoDto(packetInfoDto);
                                    returnInfo = BLCIClaimPlatFormInterfaceFacade.getInstance().getReturnAuditSearchDto(accountDto,Iconstants.RecoveryRequestType.RECOVERY_AUDITSEACH);
                                    if(null != returnInfo && returnInfo.getAccountsInfoDtoList() != null && returnInfo.getAccountsInfoDtoList().size()>0){
                                        list.addAll(returnInfo.getAccountsInfoDtoList());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(list.size()>0){
                httpServletRequest.setAttribute("list", list);

            }
            forward = "successList";
        } catch (UserException usee) {
            usee.printStackTrace();
            // 错误信息处理
            ActionMessages errors = new ActionMessages();
            ActionMessage error = new ActionMessage("清算信息查询");
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            httpServletRequest.setAttribute("errorMessage", usee.getErrorModule());
            saveErrors(httpServletRequest, errors);
            forward = "error";
        } catch (Exception e) {
            e.printStackTrace();
            ActionMessages errors = new ActionMessages();
            ActionMessage error = new ActionMessage("清算信息查询");
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            httpServletRequest.setAttribute("errorMessage", "操作失败！请重新操作！");
            saveErrors(httpServletRequest, errors);
            forward = "error";
        }
        return actionMapping.findForward(forward);
    }

}
