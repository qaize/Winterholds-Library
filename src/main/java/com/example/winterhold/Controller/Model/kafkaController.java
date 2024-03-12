//package com.example.AppWinterhold.Controller.Model;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//
//
//public class kafkaController {
//    String topicName = "newtopic";
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    public void sendMessage(String msg) {
//        kafkaTemplate.send(topicName, msg);
//    }
//}
