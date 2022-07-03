package com.yun.hou.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yun.hou.entity.Device;
import com.yun.hou.entity.User;
import com.yun.hou.mapper.DeviceMapper;
import com.yun.hou.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/device")
public class DeviceController {

        @Resource
        private IDeviceService deviceService;

        // 新增和修改
        @PostMapping
        public boolean save(@RequestBody Device device) {
            // 新增或者更新
            return deviceService.saveOrUpdate(device);
        }

        // 查询所有数据
        @GetMapping
        public List<Device> findAll() {
            return deviceService.list();
        }

        @DeleteMapping("/{idd}")
        public boolean delete(@PathVariable Integer idd) {
            return deviceService.removeById(idd);
        }

        @PostMapping("/del/batch")
        public boolean deleteBatch(@RequestBody List<Integer> idds) { // [1,2,3]
            return deviceService.removeByIds(idds);
        }

     //分页查询
      //接口路径：/user/page?pageNum=1&pageSize=10
    // @RequestParam接受
   // limit第一个参数 = (pageNum - 1) * pageSize
     //pageSize
//    @GetMapping("/page")
//    public Map<String, Object> findPage(@RequestParam Integer pageNum,
//                                        @RequestParam Integer pageSize,
//                                        @RequestParam String username) {
//        pageNum = (pageNum - 1) * pageSize;
//        username = "%" + username + "%";
//        List<Device> data = deviceMapper.selectPage(pageNum, pageSize, username);
//        Integer total = deviceMapper.selectTotal(username);
//        Map<String, Object> res = new HashMap<>();
//        res.put("data", data);
//        res.put("total", total);
//        return res;
//    }

        // 分页查询 - mybatis-plus的方式
        @GetMapping("/page")
        public IPage<Device> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String devicekind,
                                    @RequestParam(defaultValue = "") String availdate) {
            IPage<Device> page = new Page<>(pageNum, pageSize);
            QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
            if (!"".equals(devicekind)) {
                queryWrapper.like("devicekind", devicekind);
            }
            if (!"".equals(availdate)) {
                queryWrapper.like("availdate", availdate);
            }
            queryWrapper.orderByDesc("idd");
            return deviceService.page(page,queryWrapper);
        }



        @GetMapping("/export")
        public void export(HttpServletResponse response) throws Exception {
            // 从数据库查询出所有的数据
            List<Device> list = deviceService.list();
            // 通过工具类创建writer 写出到磁盘路径
            //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
            // 在内存操作，写出到浏览器
            ExcelWriter writer = ExcelUtil.getWriter(true);
            //自定义标题别名
            writer.addHeaderAlias("idd", "设备编号");
            writer.addHeaderAlias("devicekind", "设备种类");
            writer.addHeaderAlias("availdate", "有效时间");

            // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
            writer.write(list, true);

            writer.setOnlyAlias(true);
            // 设置浏览器响应的格式
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            String fileName = URLEncoder.encode("设备信息", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out, true);
            out.close();
            writer.close();
        }
/*

        @PostMapping("/import")
        public Boolean imp(MultipartFile file) throws Exception {
            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);
            // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
            // List<User> list = reader.readAll(User.class);

            // 方式2：忽略表头的中文，直接读取表的内容
            List<List<Object>> list = reader.read(1);
            List<Device> devices = CollUtil.newArrayList();
            for (List<Object> row : list) {
                Device device = new Device();
                device.setIdd(Integer.valueOf(row.get(0).toString()));
                device.setDevicekind(row.get(1).toString());
                device.setAvaildate(Date.valueOf((String) row.get(2)));
                devices.add(device);
            }
            deviceService.saveBatch(devices);
            return true;
        }*/
    }


