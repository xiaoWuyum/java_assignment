package com.yun.hou.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.hou.entity.Device;
import com.yun.hou.mapper.DeviceMapper;
import com.yun.hou.service.IDeviceService;
import org.springframework.stereotype.Service;

@Service
public class DeviceServicempl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

}
