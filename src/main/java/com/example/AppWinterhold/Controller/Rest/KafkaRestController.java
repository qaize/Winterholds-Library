package com.example.AppWinterhold.Controller.Rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/kafka")
public class KafkaRestController {
    String topicName = "newtopic";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @GetMapping("/hit-kafka")
    public ResponseEntity<Object> sendMessage() {

        kafkaTemplate.send(topicName, "Hallo test kafka");

        return ResponseEntity.status(HttpStatus.OK).body("Success send kafka");
    }
}
