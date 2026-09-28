package murach.util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class MailUtilGmail {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        // 1. Cấu hình kết nối SMTP Google (Port 465 - SSL Chuẩn cho Cloud/Render)
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");

        // Cấu hình SSLSocketFactory bắt buộc cho Port 465
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props);

        // 🔴 BẬT DEBUG ĐỂ IN LOG KẾT NỐI SMTP LÊN RENDER:
        session.setDebug(true);

        // 2. Soạn nội dung thư
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=UTF-8");
        } else {
            message.setText(body);
        }

        // 3. Đặt địa chỉ người gửi và người nhận
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4. THÔNG TIN TÀI KHOẢN GỬI MAIL HỆ THỐNG
        String systemEmail = "baitap868@gmail.com";
        String appPassword = "ruho quzl wlqc zfjo";

        Transport transport = session.getTransport("smtp");
        transport.connect(systemEmail, appPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}