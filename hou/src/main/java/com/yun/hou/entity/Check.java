package com.yun.hou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

@Data
    @TableName("sys_check")
    @ToString
    public class Check implements Serializable {

        @ApiModelProperty("idcheck")
        @TableId(value = "idcheck",type = IdType.AUTO)
        private Integer idcheck;

        @ApiModelProperty("检查结果")
        private String result;

        @ApiModelProperty("巡检人")
        private String person;

        @ApiModelProperty("现场描述")
        private String description;

        @ApiModelProperty("巡检日期")
        private Date checkdate;

        @ApiModelProperty("设备编号")
        private Integer idd;

        @ApiModelProperty("设备种类")
        private String devicekind;


    }

