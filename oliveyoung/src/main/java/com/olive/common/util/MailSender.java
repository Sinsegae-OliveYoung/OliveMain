package com.olive.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import com.olive.common.exception.EmailException;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/* 이메일 보내주는 객체 */
public class MailSender {
	String account_user = "owhitekitty@gmail.com";

	/* 내 앱 비밀번호 넣어주기 - 구글-계정-앱비밀번호 */
	String app_pwd = "vhah lnhg mcgg kpes";
	Session session;

	public MailSender() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true"); // 메일 보내는 권한 허가
		// tls: Transport Layer Security: 데이터 암호화 후 안전히 전송하는 통신 프로토콜
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com"); // host 서버 설정
		props.put("mail.smtp", "587"); // port 번호 설정

		session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account_user, app_pwd);
			}
		});
	}

	public void send(String targetMail, String title, String content) throws EmailException {
		try {
			// 메일의 내용 구성하기
			Message message = new MimeMessage(session);
			// 보내는 사람 메일
			message.setFrom(new InternetAddress(account_user));
			// 받는 사람 메일
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(targetMail));
			// 메일 제목
			message.setSubject(title);
			// 메일 내용
			message.setText(content);

			// 메일 전송
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
			throw new EmailException("메일 발송 실패", e);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new EmailException("메일 발송 실패", e);
		}
	}

	public void sendHtml(String targetMail, String title) throws EmailException {
		// 문서 띄우기
		FileInputStream fis = null;
		BufferedReader buffr = null;
		StringBuffer sb = new StringBuffer();
		try {
			fis = new FileInputStream("C:/github/lecture_workspace/java_workspace/dbproject/data/mailform.html");
			buffr = new BufferedReader(new InputStreamReader(fis));

			while (true) {
				String tag = null;
				tag = buffr.readLine(); // 한 줄씩 읽기
				if (tag == null) break;
				sb.append(tag); // 한 줄씩 읽어들인 문자열을 누적
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(buffr != null)
				try {
					buffr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		
		try {
			// 메일의 내용 구성하기
			Message message = new MimeMessage(session);
			// 보내는 사람 메일
			message.setFrom(new InternetAddress(account_user));
			// 받는 사람 메일
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(targetMail));
			// 메일 제목
			message.setSubject(title);

			// 메일 내용 (html) 작성해서 띄우기
			/*StringBuffer tag = new StringBuffer();
			tag.append("<h1>가입을 축하드립니다</h1>");
			tag.append("<p>본 메일은 회원 가입 완료 시 보내지는 자동 메일입니다</p>");*/

			message.setContent(sb.toString(), "text/html; charset=utf-8");

			// 메일 전송
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
			throw new EmailException("메일 발송 실패", e);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new EmailException("메일 발송 실패", e);
		}
	}
}
