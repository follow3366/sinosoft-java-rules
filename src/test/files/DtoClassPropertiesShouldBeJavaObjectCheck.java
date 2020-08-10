package com.sinosoft.claim.dto.claimdamages;

public class DamagesItemKind {

    private String insureTerm;//险别名称
    private String insureTermCode;//险别代码
    private int insurecode; // Noncompliant {{所有的 Dto 类的属性必须是 JAVA 对象，不允许使用原始数据类型。}}
    public String getInsureTerm() {
        return insureTerm;
    }
    public void setInsureTerm(String insureTerm) {
        this.insureTerm = insureTerm;
    }
    public String getInsureTermCode() {
        return insureTermCode;
    }
    public void setInsureTermCode(String insureTermCode) {
        this.insureTermCode = insureTermCode;
    }

}