package com.yun.hou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("sys_device")
@ToString
public class Device implements Serializable{

        @ApiModelProperty("idd")
        @TableId(value = "idd",type = IdType.AUTO)
        private Integer idd;

        @ApiModelProperty("设备种类")
        private String devicekind;

        @ApiModelProperty("有效日期")
        private Date availdate;

}

