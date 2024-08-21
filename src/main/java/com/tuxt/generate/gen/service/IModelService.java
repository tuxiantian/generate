package com.tuxt.generate.gen.service;

import com.tuxt.generate.gen.entity.Model;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author tuxt
*/
public interface IModelService extends IService<Model> {

    Long saveModel(Model model);
}


