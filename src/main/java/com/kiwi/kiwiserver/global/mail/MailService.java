package com.kiwi.kiwiserver.global.mail;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    // 회원가입 이메일 인증 코드 메일 발송
    public void sendVerificationCode(String toEmail, String code) {
        sendHtmlMail(
                toEmail,
                "[Kiwi] 이메일 인증 코드",
                "mail/verification-code",
                code
        );
    }

    // 비밀번호 재설정 인증 코드 메일 발송
    public void sendResetPasswordCode(String toEmail, String code) {
        sendHtmlMail(
                toEmail,
                "[Kiwi] 비밀번호 재설정 인증 코드",
                "mail/reset-password-code",
                code
        );
    }

    // 공통 HTML 메일 발송 로직
    private void sendHtmlMail(String toEmail, String subject, String templateName, String code) {
        try {
            Context context = new Context();
            context.setVariable("code", code);
            context.setVariable("serviceName", "Kiwi");

            String html = templateEngine.process(templateName, context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    false,
                    StandardCharsets.UTF_8.name()
            );

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(html, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("메일 발송 실패. toEmail={}, subject={}", toEmail, subject, e);
            throw new RuntimeException("메일 발송 중 오류가 발생했습니다", e);
        }
    }
}