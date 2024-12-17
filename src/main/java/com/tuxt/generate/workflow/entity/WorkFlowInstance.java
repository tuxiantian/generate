package com.tuxt.generate.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
* 
*
* @author tuxt
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WorkFlowInstance implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String status;
    /**
     * 上次执行的工作流任务名称
     */
    private String lastTask;

    private String context;

    private String errorMessage;
    /**
     * 工作流名称，值来自 com.tuxt.generate.workflow.WorkFlow枚举类型
     */
    private String name;

    private java.util.Date createDate;

    private java.util.Date updateDate;

    private Integer version;
    /**
     * 默认值为 0，停止服务时工作流队列中未来得及执行的工作流值为 1
     */
    private Integer shutdown;
}
