package com.tuxt.generate.gen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 项目变更管理表
 *
 * @author tuxt
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectChangeManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String createBy;

    private LocalDateTime createDate;

    private String updateBy;

    private LocalDateTime updateDate;

    private Integer deleteFlag;

    private Long projectId;

    private String changeNo;

    private Long modelId;


    private String fileToken;


    private String associationModelFlag;


    private String changeType;


    private String changeOrg;


    private LocalDateTime changeDate;

    private String remark;

    private String changeModelBy;


    private LocalDateTime changeModelDate;


    private String modelThumbnail;


    private String drawing;

    private String cdeModelName;


    private Integer associationModelVersion;


    private Integer changeModelVersion;


    private String associationModelToken;


    private String changeModelToken;

    private String thumbnail;


}