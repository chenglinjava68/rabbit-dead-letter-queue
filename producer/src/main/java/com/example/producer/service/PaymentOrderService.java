package com.example.producer.service;

import com.example.producer.api.Constants;
import com.example.producer.api.PaymentOrder;
import com.example.producer.util.AfterCommitExecutor;
import com.example.producer.util.CommitExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * Created by Administrator on 2017/4/26.
 */
@Service
public class PaymentOrderService {

    private  static final Logger logger = LoggerFactory.getLogger(PaymentOrderService.class);
    private  static final Random random = new Random();

    private AmqpTemplate amqpTemplate;

    public PaymentOrderService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }


    @Autowired
    private CommitExecutor afterCommitExecutor;

    @Transactional
    public void  saveOrder( PaymentOrder paymentOrder ){
        sendMessage( paymentOrder);

        afterCommitExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // code that will run after commit goes here
                //sendMessage(paymentOrder);
            }
        });
        //throw  new RuntimeException();
    }


    public void sendMessage(PaymentOrder paymentOrder) {
        amqpTemplate.convertAndSend(Constants.EXCHANGE_NAME, Constants.ROUTING_KEY_NAME, paymentOrder);
    }


}
