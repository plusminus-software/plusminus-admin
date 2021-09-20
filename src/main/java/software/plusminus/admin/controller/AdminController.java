package software.plusminus.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import software.plusminus.admin.converter.AdminPanelConverter;

@Controller
@RequestMapping("${admin.uri:/admin}")
public class AdminController {

    @Autowired
    private AdminPanelConverter panelConverter;

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("OPEN", "{{");
        model.addAttribute("CLOSE", "}}");
        model.addAttribute("panel", panelConverter.toAdminPanel());
        return "buefy/index";
    }

}
