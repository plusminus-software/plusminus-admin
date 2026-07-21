package software.plusminus.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import software.plusminus.admin.converter.AdminPanelConverter;
import software.plusminus.authorization.service.role.Role;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@Role("admin")
@Controller
@RequestMapping("${admin.uri:/admin}")
@ConditionalOnProperty("admin.controller")
public class AdminController {

    private AdminPanelConverter panelConverter;

    @GetMapping
    public String adminPage(Model model, HttpServletRequest request) {
        model.addAttribute("OPEN", "{{");
        model.addAttribute("CLOSE", "}}");
        model.addAttribute("contextPath", request.getContextPath());
        model.addAttribute("panel", panelConverter.toAdminPanel());
        return "buefy/index";
    }

}
