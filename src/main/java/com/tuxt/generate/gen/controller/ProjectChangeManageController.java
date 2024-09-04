package com.tuxt.generate.gen.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuxt.generate.gen.entity.ProjectChangeManage;
import com.tuxt.generate.gen.service.IProjectChangeManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sun墨
 */
@RestController
@RequestMapping("projectChangeManage")
public class ProjectChangeManageController {
    @Autowired
    private IProjectChangeManageService projectChangeManageService;

    @GetMapping(value = "find", produces = {"application/json;charset=UTF-8"})
    public List<ProjectChangeManage> find(){
        return projectChangeManageService.list();
    }

    @GetMapping(value = "page", produces = {"application/json;charset=UTF-8"})
    public IPage<ProjectChangeManage> page(){
        IPage<ProjectChangeManage> page=new Page<>(1,10);
        return projectChangeManageService.page(page);
    }
}

