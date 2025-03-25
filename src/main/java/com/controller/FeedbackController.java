package com.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.FeedbackEntity;
import com.service.FeedbackService;
import com.service.UserService;
import com.utils.PageUtils;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户反馈管理接口
 */
@RestController
@RequestMapping("/api/v1/feedback")
@Tag(name = "反馈管理", description = "用户反馈信息管理相关接口")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    /**
     * 分页查询反馈信息
     */
    @Operation(summary = "分页查询反馈信息", description = "按条件分页查询反馈信息")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "limit", description = "每页数量", required = true),
            @Parameter(name = "type", description = "反馈类型"),
            @Parameter(name = "status", description = "反馈状态")
    })
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = feedbackService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 获取反馈详情
     */
    @Operation(summary = "获取反馈详情", description = "根据ID获取反馈详细信息")
    @Parameter(name = "id", description = "反馈ID", required = true)
    @GetMapping("/{id}")
    public R info(@PathVariable("id") Integer id) {
        FeedbackEntity feedback = feedbackService.getById(id);
        if (feedback == null) {
            return R.error("反馈信息不存在");
        }

        return R.ok().put("data", feedback);
    }

    /**
     * 获取当前用户的反馈列表
     */
    @Operation(summary = "获取我的反馈", description = "获取当前登录用户的所有反馈")
    @GetMapping("/my")
    public R myFeedback(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }

        QueryWrapper<FeedbackEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("created_at");
        List<FeedbackEntity> list = feedbackService.list(queryWrapper);

        return R.ok().put("data", list);
    }

    /**
     * 提交反馈
     */
    @Operation(summary = "提交反馈", description = "创建新的反馈信息")
    @Parameters({
            @Parameter(name = "title", description = "反馈标题", required = true),
            @Parameter(name = "content", description = "反馈内容", required = true),
            @Parameter(name = "type", description = "反馈类型", required = true)
    })
    @PostMapping
    public R save(@RequestBody FeedbackEntity feedback, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }

        // 验证反馈类型是否有效
        String type = feedback.getType();
        if (type == null || (!type.equals("suggestion") && !type.equals("complaint") &&
                !type.equals("bug") && !type.equals("other"))) {
            return R.error("无效的反馈类型");
        }

        // 设置用户ID和初始状态
        feedback.setUserId(userId);
        feedback.setStatus("pending");

        // 设置创建和更新时间
        Date now = new Date();
        feedback.setCreatedAt(now);
        feedback.setUpdatedAt(now);

        feedbackService.save(feedback);
        return R.ok();
    }

    /**
     * 修改反馈
     */
    @Operation(summary = "修改反馈", description = "更新已有的反馈信息")
    @Parameter(name = "id", description = "反馈ID", required = true)
    @PutMapping("/{id}")
    public R update(@PathVariable("id") Integer id, @RequestBody FeedbackEntity feedback, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }

        FeedbackEntity existFeedback = feedbackService.getById(id);
        if (existFeedback == null) {
            return R.error("反馈信息不存在");
        }

        // 检查是否是自己的反馈
        if (!userId.equals(existFeedback.getUserId())) {
            return R.error("无权修改此反馈");
        }

        // 只能修改pending状态的反馈
        if (!"pending".equals(existFeedback.getStatus())) {
            return R.error("该反馈已处理，无法修改");
        }

        feedback.setId(id);
        feedback.setUserId(userId);
        feedback.setUpdatedAt(new Date());

        feedbackService.updateById(feedback);
        return R.ok();
    }

    /**
     * 删除反馈
     */
    @Operation(summary = "删除反馈", description = "删除指定的反馈信息")
    @Parameter(name = "id", description = "反馈ID", required = true)
    @DeleteMapping("/{id}")
    public R delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }

        FeedbackEntity feedback = feedbackService.getById(id);
        if (feedback == null) {
            return R.error("反馈信息不存在");
        }

        // 检查是否是自己的反馈
        if (!userId.equals(feedback.getUserId())) {
            return R.error("无权删除此反馈");
        }

        feedbackService.removeById(id);
        return R.ok();
    }

    /**
     * 管理员回复反馈
     */
    @Operation(summary = "回复反馈", description = "管理员回复用户反馈")
    @Parameters({
            @Parameter(name = "id", description = "反馈ID", required = true),
            @Parameter(name = "reply", description = "回复内容", required = true),
            @Parameter(name = "status", description = "更新状态", required = true)
    })
    @PostMapping("/{id}/reply")
    public R reply(@PathVariable("id") Integer id, @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        // 这里可以添加管理员权限验证

        FeedbackEntity feedback = feedbackService.getById(id);
        if (feedback == null) {
            return R.error("反馈信息不存在");
        }

        String reply = params.get("reply");
        String status = params.get("status");

        // 验证状态是否有效
        if (status == null
                || (!status.equals("processing") && !status.equals("resolved") && !status.equals("rejected"))) {
            return R.error("无效的状态值");
        }

        feedback.setReply(reply);
        feedback.setStatus(status);
        feedback.setUpdatedAt(new Date());

        feedbackService.updateById(feedback);
        return R.ok();
    }
}