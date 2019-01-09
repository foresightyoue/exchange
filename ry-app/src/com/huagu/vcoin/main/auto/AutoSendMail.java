package com.huagu.vcoin.main.auto;

import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.huagu.vcoin.main.Enum.ValidateMailStatusEnum;
import com.huagu.vcoin.main.comm.ConstantMap;
import com.huagu.vcoin.main.model.Fvalidateemail;
import com.huagu.vcoin.main.service.front.FrontValidateService;
import com.huagu.vcoin.util.SendMailUtil;
import com.huagu.vcoin.util.Utils;

public class AutoSendMail extends TimerTask {

    @Autowired
    private TaskList taskList;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private ConstantMap constantMap;

    @Override
    public void run() {
        try {
            Integer id = taskList.getOneMail();
            if (id != null) {
                Fvalidateemail validateemails = this.frontValidateService.findValidateMailsById(id);
                if (validateemails != null) {
                    String email = validateemails.getEmail();
                    if (email == null) {
                        email = validateemails.getFuser().getFemail();
                    }

                    boolean flag = SendMailUtil.send(this.constantMap.get("smtp").toString(),
                            this.constantMap.get("mailName").toString(),
                            this.constantMap.get("mailPassword").toString(), email, validateemails.getFtitle(),
                            validateemails.getFcontent());

                    if (flag) {
                        validateemails.setFsendTime(Utils.getTimestamp());
                        validateemails.setFstatus(ValidateMailStatusEnum.Send);
                        this.frontValidateService.updateValidateMails(validateemails);
                    } else {
                        validateemails.setFsendTime(Utils.getTimestamp());
                        validateemails.setFstatus(ValidateMailStatusEnum.Fail);
                        this.frontValidateService.updateValidateMails(validateemails);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
