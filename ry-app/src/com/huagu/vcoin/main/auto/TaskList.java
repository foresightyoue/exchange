package com.huagu.vcoin.main.auto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.huagu.vcoin.main.Enum.ValidateMailStatusEnum;
import com.huagu.vcoin.main.Enum.ValidateMessageStatusEnum;
import com.huagu.vcoin.main.model.Fvalidateemail;
import com.huagu.vcoin.main.model.Fvalidatemessage;
import com.huagu.vcoin.main.service.front.FrontValidateService;

public class TaskList implements Serializable {
    @Autowired
    private FrontValidateService frontValidateService;

    private LinkedList<Integer> messageList;
    private LinkedList<Integer> mailList;

    public TaskList() {
        // message
        messageList = new LinkedList<Integer>();
        // mail
        mailList = new LinkedList<Integer>();

    }

    public void init() {
        readData();
    }

    private void readData() {
        // message
        List<Fvalidatemessage> list1 = this.frontValidateService.findFvalidateMessageByProperty("fstatus",
                ValidateMessageStatusEnum.Not_send);

        for (Fvalidatemessage fvalidatemessage : list1) {
            messageList.add(fvalidatemessage.getFid());
        }
        // mail
        List<Fvalidateemail> list2 = this.frontValidateService.findValidateMailsByProperty("fstatus",
                ValidateMailStatusEnum.Not_send);
        for (Fvalidateemail fvalidateemail : list2) {
            this.mailList.add(fvalidateemail.getFid());
        }
    }

    public synchronized Integer getOneMessage() {
        synchronized (messageList) {
            Integer id = null;
            if (messageList.size() > 0) {
                id = messageList.pop();
            }
            return id;
        }
    }

    public synchronized void returnMessageList(Integer id) {
        synchronized (messageList) {
            messageList.add(id);
        }
    }

    public Integer getOneMail() {
        synchronized (this.mailList) {
            Integer id = null;
            if (this.mailList.size() > 0) {
                id = mailList.pop();
            }
            return id;
        }
    }

    public synchronized void returnMailList(Integer id) {
        synchronized (this.mailList) {
            mailList.add(id);
        }
    }

}
