package ai.yue.library.template.cloud.controller;

import ai.yue.library.template.cloud.service.MqDelayMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mqDelayMsg")
public class MqDelayMsgController {

    @Autowired
    MqDelayMsgService mqDelayMsgService;

    @GetMapping("/sendDelayedMessage")
    public void sendDelayedMessage(@RequestParam(value = "message", defaultValue = "hello world") String message, Integer delay) {
        mqDelayMsgService.producer(message, delay);
    }

}
