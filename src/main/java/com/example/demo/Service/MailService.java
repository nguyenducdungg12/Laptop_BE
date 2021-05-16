package com.example.demo.Service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.CustomException;
import com.example.demo.model.UserModel;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailService {
	@Autowired
	private final JavaMailSender mailSender;
	
	public void sendVerificationEmail(UserModel user, String siteURL) throws UnsupportedEncodingException, MessagingException{
		String toAddress = user.getEmail();
		String fromAddress = "nguyenducdungg121@gmail.com";
		String senderName = "Dũng Pro";
		String subject = "Kích hoạt tài khoản của bạn";
		String content = "Kính gửi [[name]],<br>"
				+ "Nhấn vào link bên dưới để kích hoạt tài khoản của bạn:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
				+ "Thank you,<br>"
				+ "Your company name.";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", user.getUsername());
		String verifyURL = siteURL + "/api/auth/accountVerification/" + user.getVerificationcode();
		content = content.replace("[[URL]]", verifyURL);
		helper.setText(content, true);
		try {
			mailSender.send(message);
		}
		catch(Exception e) {
            throw new CustomException("Exception occured when sending mail to" + e.getMessage());

		}
	}
	public void sendForgotPassword(UserModel user, String siteURL) throws UnsupportedEncodingException, MessagingException{
		System.out.println(siteURL);
		String toAddress = user.getEmail();
		String fromAddress = "nguyenducdungg121@gmail.com";
		String senderName = "Dũng Pro";
		String subject = "Thay đổi mật khẩu";
		String content = "Kính gửi [[name]],<br>"
				+ "Nhấn vào link bên dưới để thay đổi mật khẩu tài khoản của bạn:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
				+ "Thank you,<br>"
				+ "Your company name.";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", user.getUsername());
		String verifyURL = siteURL + "/forgot/" + user.getForgotpassword();
		content = content.replace("[[URL]]", verifyURL);
		helper.setText(content, true);
		try {
			mailSender.send(message);
		}
		catch(Exception e) {
            throw new CustomException("Exception occured when sending mail to" + e.getMessage());

		}
	}
}
