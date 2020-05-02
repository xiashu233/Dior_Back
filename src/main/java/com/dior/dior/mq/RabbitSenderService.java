package com.dior.dior.mq; /**
 * Created by Administrator on 2019/6/21.
 */

import com.dior.dior.dto.OrderSuccessInfo;
import com.dior.dior.dto.PayingInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ发送邮件服务
 * @Author:debug (SteadyJack)
 * @Date: 2019/6/21 21:47
 **/
@Service
public class RabbitSenderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;


    /**
     * 下单成功异步发送邮件通知消息
     */
    public void sendOrderSuccessEmailMsg(OrderSuccessInfo orderInfo){

        try {
            if (StringUtils.isNotBlank(orderInfo.getOrderSn())){
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("mq.order.success.email.exchange"));
                rabbitTemplate.setRoutingKey(env.getProperty("mq.order.success.email.routing.key"));

                //TODO：将info充当消息发送至队列
                rabbitTemplate.convertAndSend(orderInfo, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,OrderSuccessInfo.class);
                        return message;
                    }
                });

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 进入支付界面后利用延迟队列检测支付状态（被动回调变主动回调）
     * @param payingInfo 支付时状态
     */
    public void sendPayingOrderExpireMsg(PayingInfo payingInfo){
        try {
            if (StringUtils.isNotBlank(payingInfo.getOut_trad_no())){
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("mq.paying.dead.prod.exchange"));
                rabbitTemplate.setRoutingKey(env.getProperty("mq.paying.dead.prod.routing.key"));
                rabbitTemplate.convertAndSend(payingInfo, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        MessageProperties mp=message.getMessageProperties();
                        mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,PayingInfo.class);

                        //TODO：动态设置TTL(为了测试方便，暂且设置10s)
                        mp.setExpiration(env.getProperty("mq.paying.expire"));
                        return message;
                    }
                });

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}



