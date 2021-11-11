package work.wlwl.toolman.service.sms.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import work.wlwl.toolman.service.sms.service.MailService;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public void sendQQMail(String to,String title, String context) {
//        构建邮件对象
        SimpleMailMessage message = new SimpleMailMessage();

//        设置主题
        message.setSubject(title);
//        设置发送者要跟application中的一致
        message.setFrom("861775074@qq.com");
//        设置邮箱接受者，可以多个 用，分割
        message.setTo(to);
//        设置邮件发送日期
        message.setSentDate(new Date());
//        设置邮件发送正文
        message.setText(context);
//        发送
        javaMailSender.send(message);
    }
}
