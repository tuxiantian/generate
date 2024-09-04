package com.tuxt.generate.gen.controller;


import com.tuxt.generate.gen.entity.Model;
import com.tuxt.generate.gen.service.IModelService;
import com.tuxt.generate.workflow.service.IWorkFlowInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author tuxt
 */
@RestController
@RequestMapping("model")
public class ModelController {
    @Autowired
    IModelService modelService;
    @Autowired
    IWorkFlowInstanceService workFlowInstanceService;

    @PostMapping("save")
    public Long save(@RequestBody Model model){
      return   modelService.saveModel(model);

    }

}

