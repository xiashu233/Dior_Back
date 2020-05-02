package com.dior.dior.mq;

import com.dior.dior.bean.MailDto;
import com.dior.dior.dto.OrderSuccessInfo;
import com.dior.dior.dto.PayingInfo;
import com.dior.dior.service.OrderService;
import com.dior.dior.util.MailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class RabbitReceiverService {

    @Autowired
    private Environment env;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MailUtil mailUtil;
    @Autowired
    OrderService orderService;
    @Autowired
    RabbitSenderService senderService;

    /**
     * 秒杀异步邮件通知-接收消息
     */
    @RabbitListener(queues = {"${mq.order.success.email.queue}"},containerFactory = "singleListenerContainer")
    public void consumeEmailMsg(OrderSuccessInfo orderInfo){
        try {
            StringBuilder mailCent = new StringBuilder();
            mailCent.append("您已成功下单，为了保证宝贝能尽快送到您的手里，还请您尽快付款哦~");
            mailCent.append("付款地址：http://localhost:7584/Order/Alipay.aspx?orderSn=");
            mailCent.append(orderInfo.getOrderSn());

            MailDto dto=new MailDto("您已下单，请尽快支付哦~",mailCent.toString(),new String[]{orderInfo.getEmail()});
            mailUtil.sendHTMLMail(dto);

        }catch (Exception e){
            System.out.println("报错");
        }
    }

    /**
     * 用户秒杀成功后超时未支付-监听者
     * @param info
     */
    @RabbitListener(queues = {"${mq.paying.dead.real.queue}"},containerFactory = "singleListenerContainer")
    public void consumeExpireOrder(PayingInfo info){
        try {
            System.out.println("发送延迟队列成功！");
            if (info.getCount()>0){

                String out_trad_no = info.getOut_trad_no();

                // 调用 paymentService 的接口进行延迟检查
                System.out.println("进行延迟检查，调用支付检查的接口服务");

                Map<String,Object> resultMap = orderService.checkAlipayPayment(out_trad_no);
                // 检测是否支付
                if (resultMap != null && !resultMap.isEmpty()){
                    String trade_status = (String) resultMap.get("trade_status");

                    // 根据查询的支付结果，判断是否进行下一次延迟任务还是支付成功更新数据和后续任务
                    if (trade_status.equals("TRADE_SUCCESS")){

                        System.out.println("支付成功，修改支付信息发送支付成功队列");
                        // 进行支付更新的幂等性检查
                        orderService.ChangeOrderStatus(info.getOut_trad_no(),info.getMemberId());

                        return;
                    }
                }

                info.setCount(info.getCount()-1);
                senderService.sendPayingOrderExpireMsg(info);
            }
        }catch (Exception e){
            System.out.println("发送延迟队列失败！");
        }
    }
}
