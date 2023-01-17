package com.hbpm.contact.controller;

import com.hbpm.base.curd.BatchDeleteReqDTO;
import com.hbpm.base.web.error.ApiException;
import com.hbpm.contact.dto.resource.ResourceDTO;
import com.hbpm.contact.dto.resource.ResourceMapper;
import com.hbpm.contact.service.ContactResourceService;
import com.hbpm.model.contact.ContactResourceEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author huangxiuqi
 */
@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    private final ContactResourceService contactResourceService;

    public ResourceController(ContactResourceService contactResourceService) {
        this.contactResourceService = contactResourceService;
    }

    @GetMapping("/{id}")
    public ResourceDTO get(@PathVariable("id") Long id) {
        return contactResourceService.findById(id)
                .map(ResourceMapper.INSTANCE::entity2dto)
                .orElseThrow(() -> new ApiException("实体不存在"));
    }

    @GetMapping("/tree")
    public List<ResourceDTO> tree(@RequestParam(value = "name", required = false) String name) {
        return contactResourceService.tree(name);
    }

    @PostMapping
    public void save(@RequestBody @Valid ResourceDTO input) {
        ContactResourceEntity entity = ResourceMapper.INSTANCE.dto2entity(input);
        contactResourceService.save(entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        contactResourceService.deleteById(id);
    }

    @DeleteMapping("/batch")
    public void batchDelete(@RequestBody @Valid BatchDeleteReqDTO<Long> input) {
        contactResourceService.batchDeleteByIds(input.getIds());
    }
}
