package com.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.ExamquestionEntity;
import com.entity.view.ExamquestionView;
import com.service.ExamquestionService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.ValidatorUtils;

/**
 * 试题表 后端接口
 */
@RestController
@RequestMapping("/examquestion")
public class ExamquestionController {
    @Autowired
    private ExamquestionService examquestionService;

    /**
     * 后端列表分页
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ExamquestionEntity examquestion) {
        QueryWrapper<ExamquestionEntity> queryWrapper = new QueryWrapper<>();
        Page<ExamquestionEntity> page = examquestionService.page(
                new Page<>(
                        Long.parseLong(params.get("page").toString()),
                        Long.parseLong(params.get("limit").toString())),
                queryWrapper);
        return R.ok().put("data", new PageUtils(page));
    }

    /**
     * 前端列表分页
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ExamquestionEntity examquestion) {
        QueryWrapper<ExamquestionEntity> queryWrapper = new QueryWrapper<>();
        Page<ExamquestionEntity> page = examquestionService.page(
                new Page<>(
                        Long.parseLong(params.get("page").toString()),
                        Long.parseLong(params.get("limit").toString())),
                queryWrapper);
        return R.ok().put("data", new PageUtils(page));
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R lists(ExamquestionEntity examquestion) {
        QueryWrapper<ExamquestionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(new HashMap<String, Object>() {
            {
                put("examquestion", examquestion);
            }
        });
        return R.ok().put("data", examquestionService.list(queryWrapper));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ExamquestionEntity examquestion) {
        QueryWrapper<ExamquestionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(new HashMap<String, Object>() {
            {
                put("examquestion", examquestion);
            }
        });
        ExamquestionView examquestionView = (ExamquestionView) examquestionService.getOne(queryWrapper);
        return R.ok("查询试题表成功").put("data", examquestionView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ExamquestionEntity examquestion = examquestionService.getById(id);
        return R.ok().put("data", examquestion);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        ExamquestionEntity examquestion = examquestionService.getById(id);
        return R.ok().put("data", examquestion);
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ExamquestionEntity examquestion) {
        examquestion.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        examquestionService.save(examquestion);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ExamquestionEntity examquestion) {
        examquestion.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        examquestionService.save(examquestion);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ExamquestionEntity examquestion) {
        examquestionService.updateById(examquestion);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        examquestionService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 提醒接口
     */
    @RequestMapping("/remind/{columnName}/{type}")
    public R remindCount(@PathVariable("columnName") String columnName, @PathVariable("type") String type,
            @RequestParam Map<String, Object> map) {
        map.put("column", columnName);
        map.put("type", type);

        if (type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date remindStartDate = null;
            Date remindEndDate = null;
            if (map.get("remindstart") != null) {
                Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindStart);
                remindStartDate = c.getTime();
                map.put("remindstart", sdf.format(remindStartDate));
            }
            if (map.get("remindend") != null) {
                Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindEnd);
                remindEndDate = c.getTime();
                map.put("remindend", sdf.format(remindEndDate));
            }
        }

        QueryWrapper<ExamquestionEntity> wrapper = new QueryWrapper<>();
        if (map.get("remindstart") != null) {
            wrapper.ge(columnName, map.get("remindstart"));
        }
        if (map.get("remindend") != null) {
            wrapper.le(columnName, map.get("remindend"));
        }

        int count = (int) examquestionService.count(wrapper);
        return R.ok().put("count", count);
    }
}
