package com.tuxt.generate.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MessageProducer messageProducer;

    @GetMapping("/sendMessage")
    public String sendMessage(@RequestParam String message) {
        messageProducer.sendMessage("TopicTest", message);
        return "Message sent: " + message;
    }

    @GetMapping("/publishEvent")
    public String publishEvent(){
        applicationEventPublisher.publishEvent(new WorkFlowEvent(WorkFlow.Train,1L));
        return "ok";
    }
}