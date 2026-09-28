package murach.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MailUtilGmail {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML) {

        // API Key Brevo của bạn
        String apiKey = "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";

        try {
            // 1. Chuyển sang giao thức REST API qua cổng HTTPS (443) - Render sẽ nhận 100%
            URL url = new URL("https://api.brevo.com/v3/smtp/email");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("api-key", apiKey);
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000); // Chống kẹt socket (10s)
            conn.setReadTimeout(10000);

            // 2. Định dạng dữ liệu JSON gửi sang Brevo
            // Lưu ý: "email" ở mục sender phải là email bạn đăng ký Brevo (hoặc email dùng nhận thông báo)
            String senderEmail = (from != null && !from.isEmpty()) ? from : "24110210@student.hcmute.edu.vn";
            
            // Xử lý xuống dòng & ký tự đặc biệt trong HTML/Text để tránh lỗi JSON
            String cleanBody = body.replace("\\", "\\\\")
                                  .replace("\"", "\\\"")
                                  .replace("\r", "")
                                  .replace("\n", "\\n");
            
            String cleanSubject = subject.replace("\\", "\\\\")
                                         .replace("\"", "\\\"");

            String jsonInputString;
            if (bodyIsHTML) {
                jsonInputString = String.format(
                    "{\"sender\":{\"name\":\"Website Admin\",\"email\":\"%s\"},\"to\":[{\"email\":\"%s\"}],\"subject\":\"%s\",\"htmlContent\":\"%s\"}",
                    senderEmail, to, cleanSubject, cleanBody
                );
            } else {
                jsonInputString = String.format(
                    "{\"sender\":{\"name\":\"Website Admin\",\"email\":\"%s\"},\"to\":[{\"email\":\"%s\"}],\"subject\":\"%s\",\"textContent\":\"%s\"}",
                    senderEmail, to, cleanSubject, cleanBody
                );
            }

            // 3. Thực hiện gửi Request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 201 || responseCode == 200) {
                System.out.println("Gửi mail qua Brevo API thành công! Response code: " + responseCode);
            } else {
                System.err.println("Gửi mail thất bại! Response code từ Brevo: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi kết nối tới Brevo API: " + e.getMessage());
            e.printStackTrace();
        }
    }
}