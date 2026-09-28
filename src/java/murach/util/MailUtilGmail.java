package murach.util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class MailUtilGmail {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        // 1. Cấu hình kết nối SMTP Google (Port 587 - STARTTLS)
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props);

        // 2. Soạn nội dung thư
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=UTF-8");
        } else {
            message.setText(body);
        }

        // 3. Đặt địa chỉ người gửi và người nhận
        // 'to' chính là email thực tế mà người dùng vừa nhập trên Form
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4. THÔNG TIN TÀI KHOẢN GỬI MAIL HỆ THỐNG
        // Đây là Gmail của BẠN đóng vai trò làm Server gửi tin nhắn
        String systemEmail = "baitap868@gmail.com";          // <--- Điền Gmail của bạn
        String appPassword = "ruho quzl wlqc zfjo";         // <--- Mật khẩu ứng dụng 16 ký tự

        Transport transport = session.getTransport("smtp");
        transport.connect(systemEmail, appPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}