package com.tuxt.generate.rocketmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private MessageProducer messageProducer;

    @GetMapping("/sendMessage")
    public String sendMessage(@RequestParam String message) {
        messageProducer.sendMessage("TopicTest", message);
        return "Message sent: " + message;
    }

}