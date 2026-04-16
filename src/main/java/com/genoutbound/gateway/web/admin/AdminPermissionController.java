package com.genoutbound.gateway.web.admin;

import com.genoutbound.gateway.config.AdminConsolePaths;
import com.genoutbound.gateway.security.role.AppPermissionRepository;
import com.genoutbound.gateway.web.admin.dto.PermissionListItem;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(AdminConsolePaths.PERMISSIONS_BASE)
public class AdminPermissionController {

    private final AppPermissionRepository permissionRepository;

    public AdminPermissionController(AppPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @GetMapping
    public String list(Model model) {
        List<PermissionListItem> permissions = permissionRepository.findAll(Sort.by("code")).stream()
            .map(permission -> new PermissionListItem(permission.getId(), permission.getCode(),
                permission.getDescription()))
            .toList();
        model.addAttribute("permissions", permissions);
        return "admin/permissions";
    }

    @GetMapping("/new")
    public String createForm() {
        throwNotFound();
        return null;
    }

    @GetMapping("/{id}")
    public String editForm(@PathVariable Long id) {
        throwNotFound();
        return null;
    }

    @PostMapping
    public String create() {
        throwNotFound();
        return null;
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id) {
        throwNotFound();
        return null;
    }

    private void throwNotFound() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "권한 관리는 읽기 전용입니다.");
    }

}
