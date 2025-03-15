package com.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entity.ExamrecordEntity;
import com.entity.view.ExamrecordView;

import com.service.ExamrecordService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 试卷记录管理接口
 */
@Tag(name = "考试记录管理", description = "管理考试记录的相关接口")
@RestController
@RequestMapping("/examrecord")
public class ExamrecordController {
    @Autowired
    private ExamrecordService examrecordService;

    /**
     * 考试记录接口
     */
    @Operation(summary = "分组查询考试记录", description = "按条件分组查询考试记录")
    @Parameters({
            @Parameter(name = "params", description = "分页和查询参数", required = true),
            @Parameter(name = "examrecord", description = "考试记录查询条件"),
            @Parameter(name = "request", description = "HTTP请求对象")
    })
    @RequestMapping("/groupby")
    public R page2(@RequestParam Map<String, Object> params, ExamrecordEntity examrecord, HttpServletRequest request) {
        if (!request.getSession().getAttribute("role").toString().equals("管理员")) {
            examrecord.setUserid((Long) request.getSession().getAttribute("userId"));
        }

        QueryWrapper<ExamrecordEntity> queryWrapper = new QueryWrapper<>();
        PageUtils page = examrecordService.queryPageGroupBy(params, (QueryWrapper<ExamrecordEntity>) MPUtil
                .sort(MPUtil.between(MPUtil.likeOrEq(queryWrapper, examrecord), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 后端列表
     */
    @Operation(summary = "后台分页查询", description = "管理员查看考试记录的分页列表")
    @Parameters({
            @Parameter(name = "params", description = "分页参数", required = true),
            @Parameter(name = "examrecord", description = "考试记录查询条件"),
            @Parameter(name = "request", description = "HTTP请求对象")
    })
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ExamrecordEntity examrecord, HttpServletRequest request) {
        if (!request.getSession().getAttribute("role").toString().equals("管理员")) {
            examrecord.setUserid((Long) request.getSession().getAttribute("userId"));
        }
        QueryWrapper<ExamrecordEntity> queryWrapper = new QueryWrapper<>();
        PageUtils page = examrecordService.queryPage(params, queryWrapper);
        MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(queryWrapper, examrecord), params), params);

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @Operation(summary = "前台分页查询", description = "用户查看自己的考试记录")
    @Parameters({
            @Parameter(name = "params", description = "分页参数", required = true),
            @Parameter(name = "examrecord", description = "考试记录查询条件"),
            @Parameter(name = "request", description = "HTTP请求对象")
    })
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ExamrecordEntity examrecord, HttpServletRequest request) {
        if (!request.getSession().getAttribute("role").toString().equals("管理员")) {
            examrecord.setUserid((Long) request.getSession().getAttribute("userId"));
        }
        QueryWrapper<ExamrecordEntity> queryWrapper = new QueryWrapper<>();
        PageUtils page = examrecordService.queryPage(params, (QueryWrapper<ExamrecordEntity>) MPUtil
                .sort(MPUtil.between(MPUtil.likeOrEq(queryWrapper, examrecord), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @Operation(summary = "获取考试记录列表", description = "获取所有符合条件的考试记录")
    @Parameter(name = "examrecord", description = "考试记录查询条件")
    @RequestMapping("/lists")
    public R list(ExamrecordEntity examrecord) {
        QueryWrapper<ExamrecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(MPUtil.allEQMapPre(examrecord, "examrecord"));
        return R.ok().put("data", examrecordService.list(queryWrapper));
    }

    /**
     * 查询
     */
    @Operation(summary = "查询单个考试记录", description = "根据条件查询单个考试记录详情")
    @Parameter(name = "examrecord", description = "考试记录查询条件", required = true)
    @RequestMapping("/query")
    public R query(ExamrecordEntity examrecord) {
        QueryWrapper<ExamrecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(MPUtil.allEQMapPre(examrecord, "examrecord"));
        ExamrecordView examrecordView = (ExamrecordView) examrecordService.getOne(queryWrapper);
        return R.ok("查询考试记录表成功").put("data", examrecordView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ExamrecordEntity examrecord = examrecordService.getById(id);
        return R.ok().put("data", examrecord);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        ExamrecordEntity examrecord = examrecordService.getById(id);
        return R.ok().put("data", examrecord);
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ExamrecordEntity examrecord, HttpServletRequest request) {
        examrecord.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        // ValidatorUtils.validateEntity(examrecord);
        examrecord.setUserid((Long) request.getSession().getAttribute("userId"));
        examrecordService.save(examrecord);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ExamrecordEntity examrecord, HttpServletRequest request) {
        examrecord.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        // ValidatorUtils.validateEntity(examrecord);
        examrecord.setUserid((Long) request.getSession().getAttribute("userId"));
        examrecordService.save(examrecord);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ExamrecordEntity examrecord, HttpServletRequest request) {
        // ValidatorUtils.validateEntity(examrecord);
        examrecordService.updateById(examrecord); // 全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        examrecordService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 提醒接口
     */
    @RequestMapping("/remind/{columnName}/{type}")
    public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
            @PathVariable("type") String type, @RequestParam Map<String, Object> map) {
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

        QueryWrapper<ExamrecordEntity> queryWrapper = new QueryWrapper<>();
        if (map.get("remindstart") != null) {
            queryWrapper.ge(columnName, map.get("remindstart"));
        }
        if (map.get("remindend") != null) {
            queryWrapper.le(columnName, map.get("remindend"));
        }
        if (!request.getSession().getAttribute("role").toString().equals("管理员")) {
            queryWrapper.eq("userid", (Long) request.getSession().getAttribute("userId"));
        }

        int count = (int) examrecordService.count(queryWrapper);
        return R.ok().put("count", count);
    }

    /**
     * 当重新考试时，删除考生的某个试卷的所有考试记录
     */
    @Operation(summary = "删除指定考试记录", description = "删除指定用户的指定试卷的所有考试记录")
    @Parameters({
            @Parameter(name = "userid", description = "用户ID", required = true),
            @Parameter(name = "paperid", description = "试卷ID", required = true)
    })
    @RequestMapping("/deleteRecords")
    public R deleteRecords(@RequestParam Long userid, @RequestParam Long paperid) {
        examrecordService.remove(new QueryWrapper<ExamrecordEntity>().eq("paperid", paperid).eq("userid", userid));
        return R.ok();
    }
}
