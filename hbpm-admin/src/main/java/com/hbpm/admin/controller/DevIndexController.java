package com.hbpm.admin.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huangxiuqi
 */
@RequestMapping
public class DevIndexController {

    @RequestMapping("/")
    public String index(Model model) {
        IndexController controller = new IndexController();
        controller.index(model);
        return "dev-index";
    }
}
