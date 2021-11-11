package work.wlwl.toolman.service.sms.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import work.wlwl.toolman.service.sms.service.MailService;

@Api("发送邮件")
@RestController
@RequestMapping("/api/sms/mail")

public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("send")
    public String send(@RequestParam("to") String to,
                       @RequestParam("title") String title,
                       @RequestParam("context") String context) {

        mailService.sendQQMail(to, title, context);
        return "ok";
    }
}
