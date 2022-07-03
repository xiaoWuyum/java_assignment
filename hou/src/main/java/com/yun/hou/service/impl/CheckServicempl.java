package com.yun.hou.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.hou.entity.Check;
import com.yun.hou.mapper.CheckMapper;
import com.yun.hou.service.ICheckService;
import org.springframework.stereotype.Service;

@Service
public class CheckServicempl extends ServiceImpl<CheckMapper, Check> implements ICheckService {

}