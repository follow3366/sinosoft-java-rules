package com.sinosoft.claim.ui.control.facade;

public class UIPrpDcustomerUnitFacade extends Action{
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String forward = "";

        try{
            String codeType = (String)request.getAttribute("codeType"); // Noncompliant {{不允许使用HttpServletRequest 对象的方法获取参数值。}}
            ParamUtils paramUtils = new ParamUtils(request);
            String actionType = paramUtils.getParameter("actionType");
            String taskCode = paramUtils.getParameter("taskCode");
            //获取用户
            HttpSession session = request.getSession();
            UserDto userDto = (UserDto) (session.getAttribute("user"));

            PrpDuserDto prpDuserDto = new PrpDuserDto();

            prpDuserDto.setComCode(userDto.getComCode());
            prpDuserDto.setUserCode(userDto.getUserCode());
            prpDuserDto.setUserName(userDto.getUserName());

            String userCode = userDto.getUserCode();
            forward = IConstants.INVALID;
            //构造action实例
            com.sinosoft.claim.ui.control.action.UIPrpDcustomerUnitAction action = new com.sinosoft.claim.ui.control.action.UIPrpDcustomerUnitAction();
            ProcessUtils processUtils = new ProcessUtils();
            //如果有自定义的方法，请使用else if分支加入，要注意检查权限
            if (actionType == null || actionType.trim().equals("")) {
                forward = IConstants.INVALID;
            } else {
                forward = processUtils.generalExcecte(request, response, action,
                        actionType, prpDuserDto, taskCode, forward);
            }
        }
        catch(UserException ue)
        {
            ue.printStackTrace();
            //错误信息处理
            request.setAttribute("EXCEPTION", ue);
            forward = "userError";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("title.claimBeforeEdit.queryClaimList");
            errors.add(ActionErrors.GLOBAL_ERROR, error);
            request.setAttribute("errorMessage",e.getMessage()) ;
            saveErrors(request, errors);
            forward = "error";

        }
        finally
        {

        }
        return (actionMapping.findForward(forward));
    }

}
