package com.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entity.ExamRecordEntity;
import com.entity.UserEntity;
import com.entity.ExamPaperEntity;
import com.service.ExamRecordService;
import com.service.UserService;
import com.service.ExamPaperService;
import com.utils.PageUtils;
import com.utils.R;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 试卷记录管理接口
 */
@RestController
@RequestMapping("/api/v1/exam-records")
@Tag(name = "考试记录管理", description = "管理考试记录的相关接口")
public class ExamRecordController {
    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExamPaperService examPaperService;

    /**
     * 分页查询考试记录
     */
    @Operation(summary = "分页查询考试记录", description = "按条件分页查询考试记录")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "size", description = "每页数量", required = true),
            @Parameter(name = "groupBy", description = "是否分组"),
            @Parameter(name = "examRecord", description = "考试记录查询条件")
    })
    @GetMapping
    public R getExamRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }

        QueryWrapper<ExamRecordEntity> queryWrapper = new QueryWrapper<>();
        // 添加用户ID条件
        queryWrapper.eq("user_id", userId);

        // 按创建时间降序排序
        queryWrapper.orderByDesc("created_at");
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("limit", size);
        PageUtils pageResult = examRecordService.queryPage(
                params,
                queryWrapper);

        try {
            // 获取记录列表
            // List<ExamRecordEntity> recordList = PageUtils.convert(pageResult);
            List<Map<String, Object>> resultList = new ArrayList<>();

            // 遍历记录，添加用户名和试卷名称
            for (Object recordObj : pageResult.getList()) {
                ExamRecordEntity record = (ExamRecordEntity) recordObj;
                Map<String, Object> map = new HashMap<>();
                map.put("record", record);

                // 获取用户信息
                if (record.getUserId() != null) {
                    UserEntity user = userService.getById(record.getUserId());
                    if (user != null) {
                        map.put("username", user.getUsername());
                    }
                }

                // 获取试卷信息
                if (record.getPaperId() != null) {
                    ExamPaperEntity paper = examPaperService.getById(record.getPaperId());
                    if (paper != null) {
                        map.put("paperName", paper.getTitle());
                        map.put("description", paper.getDescription());
                    }
                }

                resultList.add(map);
            }
            pageResult.setList(resultList);
            return R.ok().put("data", pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取单个考试记录
     */
    @Operation(summary = "获取考试记录详情", description = "根据ID获取考试记录详细信息")
    @Parameter(name = "id", description = "考试记录ID", required = true)
    @GetMapping("/{id}")
    public R getExamRecord(@PathVariable Integer id) {
        ExamRecordEntity examRecord = examRecordService.getById(id);
        if (examRecord == null) {
            return R.error("考试记录不存在");
        }
        return R.ok().put("data", examRecord);
    }

    /**
     * 创建考试记录
     */
    @Operation(summary = "创建考试记录", description = "创建新的考试记录")
    @Parameter(name = "examRecord", description = "考试记录信息", required = true)
    @PostMapping
    public R createExamRecord(@RequestBody ExamRecordEntity examRecord, HttpServletRequest request) {
        examRecord.setId((int) (new Date().getTime() + new Double(Math.floor(Math.random() * 1000))));
        examRecord.setUserId((Integer) request.getSession().getAttribute("userId"));
        boolean saved = examRecordService.save(examRecord);
        return saved ? R.ok().put("data", examRecord) : R.error("创建失败");
    }

    /**
     * 更新考试记录
     */
    @Operation(summary = "更新考试记录", description = "更新已有的考试记录信息")
    @Parameters({
            @Parameter(name = "id", description = "考试记录ID", required = true),
            @Parameter(name = "examRecord", description = "考试记录信息", required = true)
    })
    @PutMapping("/{id}")
    public R updateExamRecord(@PathVariable Integer id, @RequestBody ExamRecordEntity examRecord) {
        examRecord.setId(id);
        boolean updated = examRecordService.updateById(examRecord);
        return updated ? R.ok().put("data", examRecord) : R.error("更新失败");
    }

    /**
     * 删除考试记录
     */
    @Operation(summary = "删除考试记录", description = "删除指定的考试记录")
    @Parameter(name = "id", description = "考试记录ID", required = true)
    @DeleteMapping("/{id}")
    public R deleteExamRecord(@PathVariable Integer id) {
        boolean removed = examRecordService.removeById(id);
        return removed ? R.ok() : R.error("删除失败");
    }

    /**
     * 批量删除考试记录
     */
    @Operation(summary = "批量删除考试记录", description = "批量删除多个考试记录")
    @Parameter(name = "ids", description = "考试记录ID数组", required = true)
    @DeleteMapping("/batch")
    public R batchDeleteExamRecords(@RequestBody Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("删除ID不能为空");
        }
        boolean removed = examRecordService.removeByIds(Arrays.asList(ids));
        return removed ? R.ok() : R.error("批量删除失败");
    }

    /**
     * 获取提醒记录数
     */
    @Operation(summary = "获取提醒记录数", description = "获取需要提醒的记录数量")
    @GetMapping("/reminders/{columnName}/{type}")
    public R getRemindersCount(
            @PathVariable String columnName,
            @PathVariable String type,
            @RequestParam(required = false) Integer remindstart,
            @RequestParam(required = false) Integer remindend,
            HttpServletRequest request) {

        Map<String, Object> params = new HashMap<>();
        params.put("remindstart", remindstart);
        params.put("remindend", remindend);

        if (type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date remindStartDate = null;
            Date remindEndDate = null;
            if (params.get("remindstart") != null) {
                Integer remindStart = Integer.parseInt(params.get("remindstart").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindStart);
                remindStartDate = c.getTime();
                params.put("remindstart", sdf.format(remindStartDate));
            }
            if (params.get("remindend") != null) {
                Integer remindEnd = Integer.parseInt(params.get("remindend").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindEnd);
                remindEndDate = c.getTime();
                params.put("remindend", sdf.format(remindEndDate));
            }
        }

        QueryWrapper<ExamRecordEntity> queryWrapper = new QueryWrapper<>();
        if (params.get("remindstart") != null) {
            queryWrapper.ge(columnName, params.get("remindstart"));
        }
        if (params.get("remindend") != null) {
            queryWrapper.le(columnName, params.get("remindend"));
        }

        // 检查session中的role属性是否存在
        Object roleObj = request.getSession().getAttribute("role");
        String role = roleObj != null ? roleObj.toString() : "";

        if (!"管理员".equals(role)) {
            // 检查userId是否存在
            Object userIdObj = request.getSession().getAttribute("userId");
            if (userIdObj != null) {
                queryWrapper.eq("user_id", (Integer) userIdObj);
            } else {
                return R.error("未登录或会话已过期");
            }
        }

        int count = (int) examRecordService.count(queryWrapper);
        return R.ok().put("count", count);
    }

    /**
     * 删除用户的试卷记录
     */
    @Operation(summary = "删除用户试卷记录", description = "删除指定用户的指定试卷的所有考试记录")
    @DeleteMapping("/users/{userId}/papers/{paperId}")
    public R deleteUserPaperRecords(
            @PathVariable("userId") Integer userId,
            @PathVariable("paperId") Integer paperId) {
        boolean removed = examRecordService.remove(
                new QueryWrapper<ExamRecordEntity>()
                        .eq("paper_id", paperId)
                        .eq("user_id", userId));
        return removed ? R.ok() : R.error("删除失败");
    }
}
