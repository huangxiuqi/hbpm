package com.hbpm.contact.controller;

import com.hbpm.contact.dto.resource.ResourceDTO;
import com.hbpm.contact.service.ContactResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author huangxiuqi
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final ContactResourceService contactResourceService;

    public MenuController(ContactResourceService contactResourceService) {
        this.contactResourceService = contactResourceService;
    }

    @GetMapping
    public List<ResourceDTO> tree() {
        return contactResourceService.tree(null);
    }
}
