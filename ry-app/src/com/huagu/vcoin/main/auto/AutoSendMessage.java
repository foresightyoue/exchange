package com.huagu.vcoin.main.auto;

import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.huagu.vcoin.main.Enum.ValidateMessageStatusEnum;
import com.huagu.vcoin.main.model.Fvalidatemessage;
import com.huagu.vcoin.main.service.front.FrontSystemArgsService;
import com.huagu.vcoin.main.service.front.FrontValidateService;
import com.huagu.vcoin.util.ConstantKeys;
import com.huagu.vcoin.util.MessagesUtils;
import com.huagu.vcoin.util.Utils;

public class AutoSendMessage extends TimerTask {

    @Autowired
    private TaskList taskList;
    @Autowired
    private FrontSystemArgsService frontSystemArgsService;
    @Autowired
    private FrontValidateService frontValidateService;

    @Override
    public void run() {
        Integer id = this.taskList.getOneMessage();
        if (id != null) {
            Fvalidatemessage fvalidatemessage = this.frontValidateService.findFvalidateMessageById(id);
            if (fvalidatemessage == null) {
                return;
            }

            int retCode;
            try {
                retCode = MessagesUtils.send(
                        this.frontSystemArgsService.getSystemArgs(ConstantKeys.MESSAGE_NAME).trim(),
                        this.frontSystemArgsService.getSystemArgs(ConstantKeys.MESSAGE_PASSWORD).trim(),
                        this.frontSystemArgsService.getSystemArgs(ConstantKeys.MESSAGE_KEY).trim(),
                        fvalidatemessage.getFphone(), fvalidatemessage.getFcontent());
                fvalidatemessage.setFsendTime(Utils.getTimestamp());
                fvalidatemessage.setFstatus(ValidateMessageStatusEnum.Send);
                this.frontValidateService.updateFvalidateMessage(fvalidatemessage);

                System.out.println(fvalidatemessage.getFcontent());
            } catch (Exception e) {
                taskList.returnMessageList(id);
                e.printStackTrace();
            }
        }

    }
}
