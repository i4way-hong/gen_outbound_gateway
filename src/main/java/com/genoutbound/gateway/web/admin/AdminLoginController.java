package com.genoutbound.gateway.web.admin;

import com.genoutbound.gateway.config.SecurityProperties;
import com.genoutbound.gateway.security.admin.AdminSessionAuthenticationFilter;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminLoginController {

    private final SecurityProperties securityProperties;

    public AdminLoginController(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @GetMapping("/admin/login")
    public String login(@RequestParam Optional<String> error,
                        @RequestParam Optional<String> logout,
                        Model model) {
        model.addAttribute("error", error.orElse(null));
        model.addAttribute("logout", logout.orElse(null));
        model.addAttribute("adminConfigured", isAdminConfigured());
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        if (!isAdminConfigured()) {
            model.addAttribute("error", "관리자 계정이 설정되지 않았습니다. ADMIN_USERNAME/ADMIN_PASSWORD를 확인하세요.");
            model.addAttribute("adminConfigured", false);
            return "admin/login";
        }
        if (!username.equals(securityProperties.getAdminUsername())
            || !password.equals(securityProperties.getAdminPassword())) {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            model.addAttribute("adminConfigured", true);
            return "admin/login";
        }
        session.setAttribute(AdminSessionAuthenticationFilter.SESSION_KEY, Boolean.TRUE);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/admin/login?logout=true";
    }

    private boolean isAdminConfigured() {
        return securityProperties.getAdminUsername() != null && !securityProperties.getAdminUsername().isBlank()
            && securityProperties.getAdminPassword() != null && !securityProperties.getAdminPassword().isBlank();
    }
}