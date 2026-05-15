package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    JavaMailSender javaMailSender;
    UserRepository userRepository;
    TemplateEngine templateEngine;

    @Async
    public void sendForgotPasswordEmail(String to, String username, String newPassword) {
        try {
            // 1. Tạo đối tượng Context để chứa biến
            Context context = new Context();
            context.setVariable("name", username); // Khớp với ${name} trong HTML
            context.setVariable("newPass", newPassword); // Khớp với ${newPass} trong HTML

            // 2. Đọc file HTML và bơm biến vào
            // "email-forgot-password" chính là tên file HTML bạn vừa tạo ở Bước 2
            String htmlBody = templateEngine.process("email-forgot-password", context);

            // 3. Tiến hành gửi mail
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("🔑 Yêu cầu cấp lại mật khẩu");
            helper.setText(htmlBody, true); // true = Cho phép dịch HTML

            javaMailSender.send(message);
            System.out.println("Đã gửi email khôi phục thành công tới: " + to);

        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}
