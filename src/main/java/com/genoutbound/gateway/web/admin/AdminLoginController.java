package com.genoutbound.gateway.web.admin;

import com.genoutbound.gateway.config.AdminConsolePaths;
import com.genoutbound.gateway.config.SecurityProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 설정 참조를 로그인 검증 로직에서만 사용하며 외부로 노출하지 않습니다.")
    public AdminLoginController(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @GetMapping(AdminConsolePaths.SESSION_NEW)
    public String login(@RequestParam Optional<String> error,
                        @RequestParam Optional<String> logout,
                        Model model) {
        model.addAttribute("error", error.orElse(null));
        model.addAttribute("logout", logout.orElse(null));
        model.addAttribute("adminConfigured", isAdminConfigured());
        return "admin/login";
    }

    @PostMapping(AdminConsolePaths.SESSION_NEW)
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
        return "redirect:" + AdminConsolePaths.USERS_BASE;
    }

    @GetMapping(AdminConsolePaths.SESSION_END)
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:" + AdminConsolePaths.SESSION_NEW + "?logout=true";
    }

    private boolean isAdminConfigured() {
        return securityProperties.getAdminUsername() != null && !securityProperties.getAdminUsername().isBlank()
            && securityProperties.getAdminPassword() != null && !securityProperties.getAdminPassword().isBlank();
    }
}