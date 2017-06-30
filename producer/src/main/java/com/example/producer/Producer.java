package com.example.producer;

import com.example.producer.api.Constants;
import com.example.producer.api.PaymentOrder;
import com.example.producer.service.PaymentOrderService;
import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;


@Component
public class Producer {

    @Autowired
    private PaymentOrderService paymentOrderService;

    private  static final Logger logger = LoggerFactory.getLogger(PaymentOrderService.class);
    private  static final Random random = new Random();


    @Scheduled(fixedDelay = 5000L)
    public void send() {
        PaymentOrder paymentOrder = new PaymentOrder(
                Iban.random().toFormattedString(),
                Iban.random().toFormattedString(),
                new BigDecimal(1D + random.nextDouble() * 100D).setScale(2, BigDecimal.ROUND_FLOOR));
        logger.info("Sending payload \'{}\'", paymentOrder);
        paymentOrderService.saveOrder(paymentOrder);


    }
}
