package com.yun.hou.service;

import com.yun.hou.controller.dto.UserDTO;
import com.yun.hou.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yun
 * @since 2022-06-16
 */
public interface IUserService extends IService<User> {
    UserDTO login(UserDTO userDTO);
    User register(UserDTO userDTO);
}
