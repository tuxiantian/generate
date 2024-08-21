package com.tuxt.generate.gen.controller;


import com.tuxt.generate.gen.entity.Model;
import com.tuxt.generate.gen.service.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author tuxt
 */
@RestController
@RequestMapping("model")
public class ModelController {
    @Autowired
    IModelService modelService;

    @PostMapping("save")
    public Long save(@RequestBody Model model){
      return   modelService.saveModel(model);

    }
}

