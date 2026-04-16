package com.genoutbound.gateway.web.admin;

import com.genoutbound.gateway.config.AdminConsolePaths;
import com.genoutbound.gateway.security.role.AppPermission;
import com.genoutbound.gateway.security.role.AppPermissionRepository;
import com.genoutbound.gateway.security.role.AppRole;
import com.genoutbound.gateway.security.role.AppRoleRepository;
import com.genoutbound.gateway.web.admin.dto.RoleForm;
import com.genoutbound.gateway.web.admin.dto.RoleListItem;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(AdminConsolePaths.ROLES_BASE)
public class AdminRoleController {

    private final AppRoleRepository roleRepository;
    private final AppPermissionRepository permissionRepository;

    public AdminRoleController(AppRoleRepository roleRepository,
                               AppPermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @GetMapping
    public String list(Model model) {
        List<RoleListItem> roles = roleRepository.findAllBy(Sort.by("name")).stream()
            .map(role -> new RoleListItem(role.getId(), role.getName(), role.isEnabled(),
                role.getPermissions() == null ? 0 : role.getPermissions().size()))
            .toList();
        model.addAttribute("roles", roles);
        return "admin/roles";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        RoleForm form = new RoleForm();
        return renderForm(model, form, null);
    }

    @GetMapping("/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        AppRole role = roleRepository.findWithPermissionsById(id)
            .orElseThrow(() -> new IllegalArgumentException("역할을 찾을 수 없습니다."));
        RoleForm form = new RoleForm();
        form.setId(role.getId());
        form.setName(role.getName());
        form.setDescription(role.getDescription());
        form.setEnabled(role.isEnabled());
        Set<AppPermission> permissions = role.getPermissions();
        form.setPermissionIds(permissions == null ? List.of() : permissions.stream()
            .map(AppPermission::getId)
            .filter(Objects::nonNull)
            .toList());
        return renderForm(model, form, null);
    }

    @PostMapping
    public String create(@ModelAttribute("form") RoleForm form, Model model) {
        String validation = validateForm(form, true);
        if (validation != null) {
            return renderForm(model, form, validation);
        }
        Optional<AppRole> existing = roleRepository.findByName(form.getName());
        if (existing.isPresent()) {
            return renderForm(model, form, "이미 존재하는 역할입니다.");
        }
        AppRole role = new AppRole();
        applyForm(role, form);
        roleRepository.save(role);
    return "redirect:" + AdminConsolePaths.ROLES_BASE;
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") RoleForm form, Model model) {
        String validation = validateForm(form, false);
        if (validation != null) {
            form.setId(id);
            return renderForm(model, form, validation);
        }
        AppRole role = roleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("역할을 찾을 수 없습니다."));
        applyForm(role, form);
        roleRepository.save(role);
    return "redirect:" + AdminConsolePaths.ROLES_BASE;
    }

    private String renderForm(Model model, RoleForm form, String error) {
        List<AppPermission> permissions = permissionRepository.findAll(Sort.by("code"));
        model.addAttribute("form", form);
        model.addAttribute("permissions", permissions);
        model.addAttribute("error", error);
        return "admin/role-form";
    }

    private String validateForm(RoleForm form, boolean requireName) {
        if (requireName && (form.getName() == null || form.getName().isBlank())) {
            return "역할명을 입력하세요.";
        }
        if (form.getName() != null && form.getName().isBlank()) {
            return "역할명을 입력하세요.";
        }
        return null;
    }

    private void applyForm(AppRole role, RoleForm form) {
        role.setName(form.getName());
        role.setDescription(form.getDescription());
        role.setEnabled(form.isEnabled());
        role.setPermissions(resolvePermissions(form.getPermissionIds()));
    }

    private Set<AppPermission> resolvePermissions(List<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return Set.of();
        }
        return permissionRepository.findAllById(permissionIds).stream()
            .collect(Collectors.toSet());
    }
}
