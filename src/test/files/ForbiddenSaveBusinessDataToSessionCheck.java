
package com.sinosoft.claim.webservices.mobilesurvey.domain;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "ClaimService", targetNamespace = "http://service.mobilesurvey.webservices.claim.sinosoft.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ClaimService {

    @WebMethod(operationName = "saveSceneInfo", action = "")
    @WebResult(name = "return", targetNamespace = "")
    public ReturnMsgVO saveSceneInfo(
            @WebParam(name = "arg0", targetNamespace = "") SubmitSceneMainInfoVO arg0);

    @WebMethod(operationName = "getLossPrintPreview", action = "")
    @WebResult(name = "return", targetNamespace = "")
    public ReturnLossPrintPreviewVO getLossPrintPreview(
            @WebParam(name = "arg0", targetNamespace = "") LossPrintPreviewVO arg0);

    @WebMethod(operationName = "saveCarInfo", action = "")
    @WebResult(name = "return", targetNamespace = "")
    public ReturnMsgVO saveCarInfo(
            @WebParam(name = "arg0", targetNamespace = "") SubmitCarMainInfoVO arg0);

    @WebMethod(operationName = "saveReadTaskTime", action = "")
    @WebResult(name = "return", targetNamespace = "")
    public ReturnMsgVO saveReadTaskTime(
            @WebParam(name = "arg0", targetNamespace = "") ReadTaskTimeVO arg0);

    @WebMethod(operationName = "saveContactCustTime", action = "")
    @WebResult(name = "return", targetNamespace = "")
    public ReturnMsgVO saveContactCustTime(
            @WebParam(name = "arg0", targetNamespace = "") ContactCustTimeVO arg0);

    @WebMethod(operationName = "savePropInfo", action = "")
    @WebResult(name = "return", targetNamespace = "")
    public ReturnMsgVO savePropInfo(
            @WebParam(name = "arg0", targetNamespace = "") SubmitPropMainInfoVO arg0);

    @WebMethod(operationName = "saveSurveyLogonTime", action = "")
    @WebResult(name = "return", targetNamespace = "")
    public ReturnMsgVO saveSurveyLogonTime(
            @WebParam(name = "arg0", targetNamespace = "") SurveyLogonTimeVO arg0);

    @WebMethod(operationName = "uploadImg", action = "")
    @WebResult(name = "return", targetNamespace = "")
    public ReturnMsgVO uploadImg(
            @WebParam(name = "arg0", targetNamespace = "") SubmitImageDetailInfoVO arg0);

    /**
     * 保存支付信息
     *
     * @param paymentInfoVO
     * @return
     */
    public ReturnMsgVO savePaymentInfo(ArrayList<PaymentInfoVO> paymentInfoVOs);

    /**
     * 查看预览信息
     *
     * @param registNo
     * @return
     */
    public PreviewInfoResultVO queryPreviewInfo(String registNo);

    /**
     * 查询是否可以支付状态，0表示不可支付，1表示可支付
     * @param registNo
     * @return
     */
    public String getPayStatus(String registNo);

}
