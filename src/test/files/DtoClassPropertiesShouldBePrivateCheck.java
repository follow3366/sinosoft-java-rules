package com.sinosoft.claim.dto.claimdamages;

public class DamagesItemKind {
    private String insurecode1 = "011100001010101"; // Noncompliant {{Dto类属性需用private修饰并初始化为null。}}
    private String insureTerm;//险别名称
    private String insureTermCode;//险别代码
    public String insurecode; // Noncompliant {{Dto类属性需用private修饰并初始化为null。}}
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