package com.dior.dior.mq;

import com.dior.dior.bean.MailDto;
import com.dior.dior.util.MailUtil;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EMailMq {

    @Autowired
    MailUtil mailUtil;

    @RabbitListener(queuesToDeclare = @Queue(value = "sendMail"))
    public void sendMail(String message){
        String[] data = message.split("-");
        StringBuilder mailCent = new StringBuilder();
        mailCent.append("您已成功下单，为了保证宝贝能尽快送到您的手里，还请您尽快付款哦~");
        mailCent.append("付款地址：http://localhost:7584/Order/Alipay.aspx?orderSn=");
        mailCent.append(data[1]);

        MailDto dto=new MailDto("您已下单，请尽快支付哦~",mailCent.toString(),new String[]{data[0]});
        mailUtil.sendHTMLMail(dto);

    }
}
