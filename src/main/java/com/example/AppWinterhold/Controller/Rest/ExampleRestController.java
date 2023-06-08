//package com.example.AppWinterhold.Controller.Rest;
//
//import com.example.AppWinterhold.Entity.Product;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.http.HttpHeaders;
//import java.util.Arrays;
//
//@RestController
//public class ExampleRestController {
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @RequestMapping(value = "",method = RequestMethod.POST)
//    public String createProducts(@RequestBody Product product) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        HttpEntity<Product> entity = new HttpEntity<Product>(product,headers);
//
//        return restTemplate.exchange(
//                "http://localhost:7081/products", HttpMethod.POST, entity, String.class).getBody();
//    }
//}
