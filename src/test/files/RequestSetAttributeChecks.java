package com.sinosoft.claim.ui.control.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinosoft.platform.bl.facade.BLPowerFacade;
import com.sinosoft.sysframework.common.util.DataUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.sinosoft.claim.bl.facade.BLUwblacklistFacade;
import com.sinosoft.claim.dto.custom.TurnPageDto;
import com.sinosoft.claim.dto.custom.UserDto;
import com.sinosoft.claim.ui.control.action.UIPowerInterface;
import com.sinosoft.claim.util.BusinessRuleUtil;
import com.sinosoft.sysframework.common.datatype.PageRecord;
import com.sinosoft.sysframework.common.util.StringUtils;
import com.sinosoft.sysframework.reference.AppConfig;

public class UIBlackListFacade extends Action{
    public ActionForward execute(ActionMapping mapping, HttpServletRequest request)
            throws Exception {
        String conditions      = "1=1";
        String forward         = "";

        conditions = conditions + " and listtype = '1' AND c.policyno = u.policyno order by u.inputdate";

        PageRecord pageRecord = (PageRecord)blUwblacklistFacade.findByConditionsAndPrpCmain(conditions, intPageNo, intRecordPerPage);
        TurnPageDto turnPageDto = new TurnPageDto();//翻页内容
        Collection collection = pageRecord.getResult();
        request.setAttribute("abstractForm", new AbstractForm(collection));// Noncompliant {{在查询和翻页查询的方法中，必须使用 request.setAttribute("fm", new AbstractForm(pageRecord));把PageRecord 对象存放到 javax.servlet.http.HttpServletRequest 对象中。}}
        request.setAttribute("collection", collection);
        request.setAttribute("conditions", conditions);

        turnPageDto.setResultList((ArrayList)pageRecord.getResult());
        request.setAttribute("turnPageDto", turnPageDto);
        int curPage=turnPageDto.getPageNo();
        request.setAttribute("curPage", String.valueOf(curPage));
        forward = "query";
        return mapping.findForward(forward);
    }
}
