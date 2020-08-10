package com.sinosoft.claim.lossCEAntiFraud.ui.facade;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sinosoft.claim.lossCEAntiFraud.bl.facade.BLPrpLAntiFraudRiskContentFacade;
import com.sinosoft.claim.lossCEAntiFraud.bl.facade.BLPrpLAntiFraudRiskFacade;
import com.sinosoft.claim.lossCEAntiFraud.dto.domain.PrpLAntiFraudRiskContentDto;
import com.sinosoft.claim.lossCEAntiFraud.dto.domain.PrpLAntiFraudRiskDto;
import com.sinosoft.claim.lossCEAntiFraud.util.Constants;

public class UIShowRiskReportForVerifFacade extends Action{

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forward = "success";
        try {
            String registNo = request.getParameter("registNo");
            String prpLcarLossLossItemName = request.getParameter("prpLcarLossLossItemName");
            List<PrpLAntiFraudRiskDto> fraudRisks = new ArrayList<PrpLAntiFraudRiskDto>();
            //查询欺诈风险列表所有信息
            String someConditions = " registNo = '"+registNo+"' and type = '"+Constants.RISK_TYPE_FRAUD+"' and nodeType = 'verif' and lisenceno = '"+prpLcarLossLossItemName+"' order by lisenceNo";
            List<PrpLAntiFraudRiskDto> specilFraudRisks = (List<PrpLAntiFraudRiskDto>) new BLPrpLAntiFraudRiskFacade().findByConditions(someConditions);
            if(specilFraudRisks!=null && specilFraudRisks.size()>0){
                for(int i=0;i<specilFraudRisks.size();i++){
                    PrpLAntiFraudRiskDto fraudRiskDto = new PrpLAntiFraudRiskDto();
                    String uuId = specilFraudRisks.get(i).getUuId();
                    //风险描述的内容
                    String specilConditions = " riskId = '"+uuId+"' and contentType = 'CE_RISKDESC' order by lineNo";
                    List<PrpLAntiFraudRiskContentDto> antiFraudRiskContentDtos = (List<PrpLAntiFraudRiskContentDto>) new BLPrpLAntiFraudRiskContentFacade().findByConditions(specilConditions);
                    StringBuffer descSb = new StringBuffer();//用于拼接desc
                    if(antiFraudRiskContentDtos!=null && antiFraudRiskContentDtos.size()>0){
                        for (PrpLAntiFraudRiskContentDto prpLAntiFraudRiskContentDto : antiFraudRiskContentDtos) {
                            descSb.append(prpLAntiFraudRiskContentDto.getContent());
                        }
                    }
                    //建议的内容
                    specilConditions = " riskId = '"+uuId+"' and contentType = 'CE_SUGGEST' order by lineNo";
                    antiFraudRiskContentDtos = (List<PrpLAntiFraudRiskContentDto>) new BLPrpLAntiFraudRiskContentFacade().findByConditions(specilConditions);
                    StringBuffer suggSb = new StringBuffer();//用于拼接desc
                    if(antiFraudRiskContentDtos!=null && antiFraudRiskContentDtos.size()>0){
                        for (PrpLAntiFraudRiskContentDto prpLAntiFraudRiskContentDto : antiFraudRiskContentDtos) {
                            suggSb.append(prpLAntiFraudRiskContentDto.getContent());
                        }
                    }

                    fraudRiskDto.setUuId(specilFraudRisks.get(i).getUuId());
                    fraudRiskDto.setAccuracyRate(specilFraudRisks.get(i).getAccuracyRate());
                    fraudRiskDto.setFactCode(specilFraudRisks.get(i).getFactCode());
                    fraudRiskDto.setFactName(specilFraudRisks.get(i).getFactName());
                    fraudRiskDto.setLisenceNo(specilFraudRisks.get(i).getLisenceNo());
                    fraudRiskDto.setNodeType(specilFraudRisks.get(i).getNodeType());
                    fraudRiskDto.setRegistNo(specilFraudRisks.get(i).getRegistNo());
                    fraudRiskDto.setRemark(specilFraudRisks.get(i).getRemark());
                    fraudRiskDto.setType(specilFraudRisks.get(i).getType());
                    fraudRiskDto.setRiskDesc(descSb.toString());
                    fraudRiskDto.setSuggest(suggSb.toString());
                    if(!"".equals(suggSb.toString()) || !"".equals(descSb.toString())){
                        fraudRisks.add(fraudRiskDto);
                    }
                }
            }

            List<PrpLAntiFraudRiskDto> riskPoints = new ArrayList<PrpLAntiFraudRiskDto>();
            //查询风险点提示所有信息
            someConditions = " registNo = '"+registNo+"' and type = '"+Constants.RISK_TYPE_POINT+"' and nodeType = 'verif' and lisenceno = '"+prpLcarLossLossItemName+"' order by lisenceNo";
            specilFraudRisks = (List<PrpLAntiFraudRiskDto>) new BLPrpLAntiFraudRiskFacade().findByConditions(someConditions);
            if(specilFraudRisks!=null && specilFraudRisks.size()>0){
                for(int i=0;i<specilFraudRisks.size();i++){
                    PrpLAntiFraudRiskDto fraudRiskDto = new PrpLAntiFraudRiskDto();
                    String uuId = specilFraudRisks.get(i).getUuId();
                    //风险描述的内容
                    String specilConditions = " riskId = '"+uuId+"' and contentType = 'CE_RISKDESC' order by lineNo";
                    List<PrpLAntiFraudRiskContentDto> antiFraudRiskContentDtos = (List<PrpLAntiFraudRiskContentDto>) new BLPrpLAntiFraudRiskContentFacade().findByConditions(specilConditions);
                    StringBuffer descSb = new StringBuffer();//用于拼接desc
                    if(antiFraudRiskContentDtos!=null && antiFraudRiskContentDtos.size()>0){
                        for (PrpLAntiFraudRiskContentDto prpLAntiFraudRiskContentDto : antiFraudRiskContentDtos) {
                            descSb.append(prpLAntiFraudRiskContentDto.getContent());
                        }
                    }
                    //建议的内容
                    specilConditions = " riskId = '"+uuId+"' and contentType = 'CE_SUGGEST' order by lineNo";
                    antiFraudRiskContentDtos = (List<PrpLAntiFraudRiskContentDto>) new BLPrpLAntiFraudRiskContentFacade().findByConditions(specilConditions);
                    StringBuffer suggSb = new StringBuffer();//用于拼接desc
                    if(antiFraudRiskContentDtos!=null && antiFraudRiskContentDtos.size()>0){
                        for (PrpLAntiFraudRiskContentDto prpLAntiFraudRiskContentDto : antiFraudRiskContentDtos) {
                            suggSb.append(prpLAntiFraudRiskContentDto.getContent());
                        }
                    }

                    fraudRiskDto.setUuId(specilFraudRisks.get(i).getUuId());
                    fraudRiskDto.setAccuracyRate(specilFraudRisks.get(i).getAccuracyRate());
                    fraudRiskDto.setFactCode(specilFraudRisks.get(i).getFactCode());
                    fraudRiskDto.setFactName(specilFraudRisks.get(i).getFactName());
                    fraudRiskDto.setLisenceNo(specilFraudRisks.get(i).getLisenceNo());
                    fraudRiskDto.setNodeType(specilFraudRisks.get(i).getNodeType());
                    fraudRiskDto.setRegistNo(specilFraudRisks.get(i).getRegistNo());
                    fraudRiskDto.setRemark(specilFraudRisks.get(i).getRemark());
                    fraudRiskDto.setType(specilFraudRisks.get(i).getType());
                    fraudRiskDto.setRiskDesc(descSb.toString());
                    fraudRiskDto.setSuggest(suggSb.toString());
                    if(!"".equals(suggSb.toString()) || !"".equals(descSb.toString())){
                        riskPoints.add(fraudRiskDto);
                    }
                }
            }

            List<PrpLAntiFraudRiskDto> nonStandardOperations = new ArrayList<PrpLAntiFraudRiskDto>();
            //查询操作不规范所有信息
            someConditions = " registNo = '"+registNo+"' and type = '"+Constants.RISK_TYPE_NONSTANDARD+"' and nodeType = 'verif' and lisenceno = '"+prpLcarLossLossItemName+"' order by lisenceNo";
            specilFraudRisks = (List<PrpLAntiFraudRiskDto>) new BLPrpLAntiFraudRiskFacade().findByConditions(someConditions);
            if(specilFraudRisks!=null && specilFraudRisks.size()>0){
                for(int i=0;i<specilFraudRisks.size();i++){
                    PrpLAntiFraudRiskDto fraudRiskDto = new PrpLAntiFraudRiskDto();
                    String uuId = specilFraudRisks.get(i).getUuId();
                    //风险描述的内容
                    String specilConditions = " riskId = '"+uuId+"' and contentType = 'CE_RISKDESC' order by lineNo";
                    List<PrpLAntiFraudRiskContentDto> antiFraudRiskContentDtos = (List<PrpLAntiFraudRiskContentDto>) new BLPrpLAntiFraudRiskContentFacade().findByConditions(specilConditions);
                    StringBuffer descSb = new StringBuffer();//用于拼接desc
                    if(antiFraudRiskContentDtos!=null && antiFraudRiskContentDtos.size()>0){
                        for (PrpLAntiFraudRiskContentDto prpLAntiFraudRiskContentDto : antiFraudRiskContentDtos) {
                            descSb.append(prpLAntiFraudRiskContentDto.getContent());
                        }
                    }
                    //建议的内容
                    specilConditions = " riskId = '"+uuId+"' and contentType = 'CE_SUGGEST' order by lineNo";
                    antiFraudRiskContentDtos = (List<PrpLAntiFraudRiskContentDto>) new BLPrpLAntiFraudRiskContentFacade().findByConditions(specilConditions);
                    StringBuffer suggSb = new StringBuffer();//用于拼接suggest
                    if(antiFraudRiskContentDtos!=null && antiFraudRiskContentDtos.size()>0){
                        for (PrpLAntiFraudRiskContentDto prpLAntiFraudRiskContentDto : antiFraudRiskContentDtos) {
                            suggSb.append(prpLAntiFraudRiskContentDto.getContent());
                        }
                    }

                    fraudRiskDto.setUuId(specilFraudRisks.get(i).getUuId());
                    fraudRiskDto.setAccuracyRate(specilFraudRisks.get(i).getAccuracyRate());
                    fraudRiskDto.setFactCode(specilFraudRisks.get(i).getFactCode());
                    fraudRiskDto.setFactName(specilFraudRisks.get(i).getFactName());
                    fraudRiskDto.setLisenceNo(specilFraudRisks.get(i).getLisenceNo());
                    fraudRiskDto.setNodeType(specilFraudRisks.get(i).getNodeType());
                    fraudRiskDto.setRegistNo(specilFraudRisks.get(i).getRegistNo());
                    fraudRiskDto.setRemark(specilFraudRisks.get(i).getRemark());
                    fraudRiskDto.setType(specilFraudRisks.get(i).getType());
                    fraudRiskDto.setRiskDesc(descSb.toString());
                    fraudRiskDto.setSuggest(suggSb.toString());
                    if(!"".equals(suggSb.toString()) || !"".equals(descSb.toString())){
                        nonStandardOperations.add(fraudRiskDto);
                    }
                }
            }


            HttpSession session = request.getSession();
            session.setAttribute("registNo", registNo); // Noncompliant {{Session中禁止存放业务数据或单个 String 对象。}}
            session.setAttribute("registNo", "registNo");// Noncompliant {{Session中禁止存放业务数据或单个 String 对象。}}
            session.setAttribute("fraudRisks", fraudRisks);
            session.setAttribute("riskPoints", riskPoints);
            session.setAttribute("nonStandardOperations", nonStandardOperations);

        } catch(Exception e){
            e.printStackTrace();
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("title.antiFraudPriceRisk.priceRiskCheck");
            errors.add(ActionErrors.GLOBAL_ERROR, error);
            request.setAttribute("errorMessage", e.getMessage());
            saveErrors(request, errors);
            forward = "error";
        }
        return mapping.findForward(forward);
    }



}
