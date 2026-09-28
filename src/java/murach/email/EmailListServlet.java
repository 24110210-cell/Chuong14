package murach.email;

import java.io.IOException;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import murach.business.User;
import murach.data.UserDB;
import murach.util.MailUtilGmail;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/index.jsp";
        String message = "";

        // Lấy action từ request
        String action = request.getParameter("action");
        if (action == null) {
            action = "join"; // Mặc định action là join
        }

        // Xử lý action và thiết lập URL tương ứng
        if (action.equals("join")) {
            url = "/index.jsp"; // Trang nhập liệu
        } else if (action.equals("add")) {
            // Lấy thông tin người dùng nhập từ form
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // Tạo đối tượng User
            User user = new User(firstName, lastName, email);

            // 1. KIỂM TRA EMAIL ĐÃ TỒN TẠI TRONG CSDL CHƯA
            if (UserDB.emailExists(user.getEmail())) {
                // Email ĐÃ tồn tại -> Gán thông báo lỗi và quay về index.jsp (Không gửi mail)
                message = "Địa chỉ email này đã tồn tại trong hệ thống.\n"
                        + "Vui lòng nhập một địa chỉ email khác.";
                url = "/index.jsp";
            } else {
                // Email CHƯA tồn tại -> Đăng ký mới, chuyển hướng tới thanks.jsp và gửi email xác nhận
                message = "";
                url = "/thanks.jsp";

                // Thêm user mới vào CSDL
                UserDB.insert(user);

                // --- GỬI MAIL XÁC NHẬN CHO EMAIL MỚI ĐĂNG KÝ (ĐỊNH DẠNG HTML ĐẸP MẮT) ---
                String to = email;                                // Gửi tới email thực tế người dùng vừa nhập
                String from = "baitap868@gmail.com";             // Thay bằng Gmail dùng làm Server của bạn
                String subject = "🎉 Chúc mừng bạn đã đăng ký thành công!";

                // Thiết kế giao diện Email dạng Card (Đã bỏ nút Khám Phá Ngay)
                String body = "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset='UTF-8'>"
                        + "</head>"
                        + "<body style='margin:0; padding:0; background-color:#f4f4f4; "
                        + "font-family: Arial, Helvetica, sans-serif;'>"
                        + "<div style='max-width:600px; margin:20px auto; background-color:#ffffff; "
                        + "border-radius:8px; overflow:hidden; "
                        + "box-shadow:0 2px 8px rgba(0,0,0,0.1);'>"
                        + "<div style='background-color:#4CAF50; color:#ffffff; "
                        + "padding:20px; text-align:center;'>"
                        + "<h1 style='margin:0; font-size:24px;'>"
                        + "Chào mừng " + firstName + " " + lastName + "! 🎉"
                        + "</h1>"
                        + "</div>"
                        + "<div style='padding:30px; color:#333333; line-height:1.6;'>"
                        + "<p>Cảm ơn bạn đã đăng ký nhận thông báo từ hệ thống của chúng tôi.</p>"
                        + "<p>Chúng tôi sẽ liên tục cập nhật những thông tin, sản phẩm mới nhất "
                        + "và các chương trình ưu đãi đặc biệt dành riêng cho bạn qua hòm thư này.</p>"
                        + "<p>Trân trọng,<br>"
                        + "<strong>Đội ngũ Hỗ trợ khách hàng</strong><br>"
                        + "Mike Murach &amp; Associates</p>"
                        + "</div>"
                        + "<div style='background-color:#eeeeee; color:#777777; "
                        + "padding:15px; text-align:center; font-size:12px;'>"
                        + "Bạn nhận được email này vì đã đăng ký tài khoản tại website của chúng tôi."
                        + "</div>"
                        + "</div>"
                        + "</body>"
                        + "</html>";

                boolean isBodyHTML = true; // Bật chế độ gửi email dạng HTML

                try {
                    // Tự động gửi email qua Gmail
                    MailUtilGmail.sendMail(to, from, subject, body, isBodyHTML);
                } catch (MessagingException e) {
                    String errorMessage = "ERROR: Unable to send email. "
                            + "Check Tomcat logs for details.\n"
                            + "ERROR MESSAGE: " + e.getMessage();

                    request.setAttribute("errorMessage", errorMessage);

                    // Ghi log lỗi vào Console của Tomcat
                    this.log("Unable to send email.\n"
                            + "TO: " + email + "\n"
                            + "FROM: " + from + "\n"
                            + "SUBJECT: " + subject + "\n"
                            + e.getMessage());
                }
                // --- KẾT THÚC GỬI MAIL ---
            }

            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}