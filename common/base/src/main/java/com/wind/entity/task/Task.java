package com.wind.entity.task;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("dm_datax_task")
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    //生成id方式
    @ApiModelProperty(value = "ID,不传")
    @TableId(type = IdType.UUID)
    private String id;
    //名称
    @ApiModelProperty(value = "名称")
    private String taskName;
    //说明
    @ApiModelProperty(value = "说明")
    private String taskDesc;
    //关联关系
    @ApiModelProperty(value = "关联关系")
    private String taskReal;

    //数据库存入的job
    @ApiModelProperty(value = "数据库存入的job,不传")
    private String taskJob;

    //前端传入job
    @TableField(exist = false)
    @ApiModelProperty(value = "job")
    private JSONObject json;

    @ApiModelProperty(value = "是否发布 0未发布 1发布" ,example = "0")
    @TableField(fill = FieldFill.INSERT)
    private Integer taskRele;

    @ApiModelProperty(value = "发布时间")
    private  Date taskReleTime;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间，不传")
    private  Date createTime;
    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间，不传")
    private Date updateTime;
    //版本
    @Version
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "版本，不传")
    private Integer version;
    //逻辑删除
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "逻辑删除，不传")
    private  Integer deleted;
}
