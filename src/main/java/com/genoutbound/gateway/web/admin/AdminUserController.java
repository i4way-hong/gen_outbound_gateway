package com.genoutbound.gateway.web.admin;

import com.genoutbound.gateway.security.AppUser;
import com.genoutbound.gateway.security.AppUserRepository;
import com.genoutbound.gateway.security.role.AppRole;
import com.genoutbound.gateway.security.role.AppRoleRepository;
import com.genoutbound.gateway.web.admin.dto.UserForm;
import com.genoutbound.gateway.web.admin.dto.UserListItem;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(AppUserRepository userRepository,
                               AppRoleRepository roleRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(Model model) {
        List<UserListItem> users = userRepository.findAll(Sort.by("id")).stream()
            .map(user -> new UserListItem(user.getId(), user.getUsername(), user.isEnabled(), resolveRoleNames(user)))
            .toList();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        UserForm form = new UserForm();
        return renderForm(model, form, null);
    }

    @GetMapping("/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        AppUser user = userRepository.findWithRolesById(id)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        UserForm form = new UserForm();
        form.setId(user.getId());
        form.setUsername(user.getUsername());
        form.setEnabled(user.isEnabled());
        form.setRoleIds(user.getRoleEntities().stream()
            .map(AppRole::getId)
            .filter(Objects::nonNull)
            .toList());
        return renderForm(model, form, null);
    }

    @PostMapping
    public String create(@ModelAttribute("form") UserForm form, Model model) {
        String validation = validateForm(form, true);
        if (validation != null) {
            return renderForm(model, form, validation);
        }
        Optional<AppUser> existing = userRepository.findByUsername(form.getUsername());
        if (existing.isPresent()) {
            return renderForm(model, form, "이미 존재하는 사용자입니다.");
        }
        AppUser user = new AppUser();
        user.setUsername(form.getUsername());
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        user.setEnabled(form.isEnabled());
        Set<AppRole> roles = resolveRoles(form.getRoleIds());
        user.setRoleEntities(roles);
        user.setRoles(toRoleString(roles));
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") UserForm form, Model model) {
        String validation = validateForm(form, false);
        if (validation != null) {
            form.setId(id);
            return renderForm(model, form, validation);
        }
        AppUser user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setEnabled(form.isEnabled());
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        }
        Set<AppRole> roles = resolveRoles(form.getRoleIds());
        user.setRoleEntities(roles);
        user.setRoles(toRoleString(roles));
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    private String renderForm(Model model, UserForm form, String error) {
        List<AppRole> roles = roleRepository.findAll(Sort.by("name"));
        model.addAttribute("form", form);
        model.addAttribute("roles", roles);
        model.addAttribute("error", error);
        return "admin/user-form";
    }

    private String validateForm(UserForm form, boolean requirePassword) {
        if (form.getUsername() == null || form.getUsername().isBlank()) {
            return "사용자명을 입력하세요.";
        }
        if (requirePassword && (form.getPassword() == null || form.getPassword().isBlank())) {
            return "비밀번호를 입력하세요.";
        }
        return null;
    }

    private Set<AppRole> resolveRoles(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Set.of();
        }
        return roleRepository.findAllById(roleIds).stream()
            .collect(Collectors.toSet());
    }

    private String resolveRoleNames(AppUser user) {
        if (user.getRoleEntities() == null || user.getRoleEntities().isEmpty()) {
            return user.getRoles() == null ? "" : user.getRoles();
        }
        return user.getRoleEntities().stream()
            .map(AppRole::getName)
            .filter(Objects::nonNull)
            .sorted(Comparator.naturalOrder())
            .collect(Collectors.joining(","));
    }

    private String toRoleString(Set<AppRole> roles) {
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return roles.stream()
            .map(AppRole::getName)
            .filter(Objects::nonNull)
            .sorted()
            .collect(Collectors.joining(","));
    }
}
