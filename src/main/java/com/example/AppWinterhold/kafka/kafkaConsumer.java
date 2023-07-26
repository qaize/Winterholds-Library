//package com.example.AppWinterhold.kafka;
//
//import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class kafkaConsumer {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(kafkaConsumer.class);
//
//    @KafkaListener(topics = "topiciwan", groupId = "group_id")
//    public void listenGroupFoo(String message) {
//        LOGGER.info(message);
//    }
//
////
////    @KafkaListener(topics = "topiciwan")
////    public void listenWithHeaders(
////            @Payload String message,
////            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
////
////        LOGGER.info(message);
////        LOGGER.info(String.valueOf(partition));
////
////    }
//}
