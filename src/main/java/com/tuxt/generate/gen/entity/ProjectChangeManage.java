package com.tuxt.generate.gen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="ProjectChangeManage对象", description="项目变更管理表")
public class ProjectChangeManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "数据状态 0可用 -1不可用")
    private Integer deleteFlag;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "变更版本编号")
    private String changeNo;

    @ApiModelProperty(value = "模型id")
    private Long modelId;

    @ApiModelProperty(value = "变更文件token")
    private String fileToken;

    @ApiModelProperty(value = "是否关联模型0已关联模型1待关联模型")
    private String associationModelFlag;

    @ApiModelProperty(value = "变更类型：1报审类变更、2非报审类变更")
    private String changeType;

    @ApiModelProperty(value = "变更人")
    private String changeOrg;

    @ApiModelProperty(value = "变更时间")
    private LocalDateTime changeDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "更新模型人")
    private String changeModelBy;

    @ApiModelProperty(value = "更新模型时间")
    private LocalDateTime changeModelDate;

    @ApiModelProperty(value = "模型缩略图")
    private String modelThumbnail;

    @ApiModelProperty(value = "图纸")
    private String drawing;

    @ApiModelProperty(value = "模型名称")
    private String cdeModelName;

    @ApiModelProperty(value = "关联模型版本号")
    private Integer associationModelVersion;

    @ApiModelProperty(value = "变更后模型版本号")
    private Integer changeModelVersion;

    @ApiModelProperty(value = "关联模型token")
    private String associationModelToken;

    @ApiModelProperty(value = "变更模型token")
    private String changeModelToken;

    @ApiModelProperty(value = "缩略图")
    private String thumbnail;


}