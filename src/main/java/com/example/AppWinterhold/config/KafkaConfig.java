//package com.example.AppWinterhold.config;
//
//// Java Program to Illustrate Kafka Configuration
//
//// Importing required classes
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//// Annotations
//@EnableKafka
//@Configuration
//public class KafkaConfig {
//
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServer;
//    @Value("${spring.kafka.consumer.group-id}")
//    private String group;
//
//    //Kafka Producer
//    @Bean
//    public ProducerFactory<String, String> producerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(
//                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                bootstrapServer);
//        configProps.put(
//                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//                StringSerializer.class);
//        configProps.put(
//                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//                StringSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    //Kafka Producer
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//
//    //Konfig Consumer
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//
//        // Creating a Map of string-object pairs
//        Map<String, Object> config = new HashMap<>();
//
//        // Adding the Configuration
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                bootstrapServer);
//        config.put(ConsumerConfig.GROUP_ID_CONFIG,
//                group);
//        config.put(
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
//        config.put(
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
//
//        return new DefaultKafkaConsumerFactory<>(config);
//    }
//
//    // Creating a Listener
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String>
//    kafkaListenerContainerFactory() {
//
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//}
