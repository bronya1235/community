package com.nowcoder.app.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author: Bao
 * @Date: 2022/7/11-07-11-14:43
 * @Description com.nowcoder.app.community.util
 * @Function
 */
@Component
public class MailClient {
	private static final Logger logger = LoggerFactory.getLogger(MailClient.class);
	@Autowired
	private JavaMailSender mailSender;
	//这里的用户名从配置文件中提取，避免了在Java代码中直接写死用户名
	@Value("${spring.mail.username}")
	private String from;

	public void sendMail(String to, String subject, String content) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		try {
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content,true);//允许支持html
			mailSender.send(helper.getMimeMessage());
		} catch (MessagingException e) {
			logger.error("邮件发送失败"+e.getMessage());
		}

	}
}
