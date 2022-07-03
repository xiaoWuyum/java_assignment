package com.yun.hou.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yun.hou.entity.Check;
import com.yun.hou.entity.Device;
import com.yun.hou.service.ICheckService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/check")
public class CheckController {

    @Resource
    private ICheckService checkService;

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody Check check) {
        // 新增或者更新
        return checkService.saveOrUpdate(check);
    }

    // 查询所有数据
    @GetMapping
    public List<Check> findAll() {
        return checkService.list();
    }

    @DeleteMapping("/{idcheck}")
    public boolean delete(@PathVariable Integer idcheck) {
        return checkService.removeById(idcheck);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> idchecks) { // [1,2,3]
        return checkService.removeByIds(idchecks);
    }

    @GetMapping("/page")
    public IPage<Check> findPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "0") Integer idd,
                                  @RequestParam(defaultValue = "") String person,
                                  @RequestParam(defaultValue = "") String devicekind) {
        IPage<Check> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Check> queryWrapper = new QueryWrapper<>();
        if (!(idd ==0)){
            queryWrapper.eq("idd", idd);
        }
        if (!"".equals(devicekind)) {
            queryWrapper.like("devicekind", devicekind);
        }
        if (!"".equals(person)) {
            queryWrapper.like("person", person);
        }
        queryWrapper.orderByDesc("idcheck");
        return checkService.page(page,queryWrapper);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Check> list = checkService.list();
        // 通过工具类创建writer 写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("idcheck", "检查编号");
        writer.addHeaderAlias("result", "巡检结果");
        writer.addHeaderAlias("person", "巡检人");
        writer.addHeaderAlias("checkdate", "检查日期");
        writer.addHeaderAlias("description", "现场描述");
        writer.addHeaderAlias("idd", "设备编号");
        writer.addHeaderAlias("devicekind", "设备种类");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        writer.setOnlyAlias(true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("检查信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
}
