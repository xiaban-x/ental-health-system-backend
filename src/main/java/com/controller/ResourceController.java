package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.ResourceEntity;
import com.entity.TagEntity;
import com.entity.UserEntity;
import com.service.ResourceService;
import com.service.TagService;
import com.service.UserService;
import com.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * 资源管理控制器
 */
@RestController
@RequestMapping("/api/v1/resource")
@Tag(name = "资源管理", description = "资源的增删改查接口")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    /**
     * 分页查询资源列表
     */
    @Operation(summary = "分页查询资源", description = "获取资源的分页列表")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "size", description = "每页数量", required = true),
            @Parameter(name = "type", description = "资源类型：all-全部，article-文章，video-视频，tool-工具")
    })
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "all") String type) {

        QueryWrapper<ResourceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1); // 只查询已发布的资源

        // 根据类型筛选
        if (!"all".equals(type)) {
            queryWrapper.eq("type", type);
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc("created_at");

        // 构建查询参数
        Map<String, Object> params = Map.of(
                "page", page,
                "size", size);

        return R.ok().put("data", resourceService.queryPage(params, queryWrapper));
    }

    /**
     * 获取资源详情
     */
    @Operation(summary = "获取资源详情", description = "根据ID获取资源详细信息")
    @Parameter(name = "id", description = "资源ID", required = true)
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        Map<String, Object> resourceDetail = resourceService.getResourceDetail(id);
        if (resourceDetail == null) {
            return R.error("资源不存在");
        }

        // 增加浏览次数
        resourceService.incrementViewCount(id);

        return R.ok().put("data", resourceDetail);
    }

    /**
     * 创建资源
     */
    @Operation(summary = "创建资源", description = "创建新的资源")
    @PostMapping
    public R save(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        // 从token中获取用户信息
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }

        // 获取用户信息
        UserEntity user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        ResourceEntity resource = new ResourceEntity();

        // 设置资源基本信息
        resource.setTitle((String) params.get("title"));
        resource.setDescription((String) params.get("description"));
        resource.setContent((String) params.get("content"));
        resource.setUrl((String) params.get("url"));
        resource.setCoverImage((String) params.get("coverImage"));
        resource.setType((String) params.get("type"));
        resource.setDuration(
                params.get("duration") != null ? Integer.parseInt(params.get("duration").toString()) : null);
        resource.setSize(params.get("size") != null ? Long.parseLong(params.get("size").toString()) : null);
        resource.setFormat((String) params.get("format"));

        // 从token中获取的用户信息设置作者ID和名称
        resource.setAuthorId(userId);
        resource.setAuthorName(user.getName());

        resource.setViewCount(0);
        resource.setLikeCount(0);
        resource.setStatus(1); // 默认为已发布状态

        // 处理标签
        List<Integer> tagIds = null;
        if (params.get("tags") != null) {
            tagIds = ((List<Object>) params.get("tags")).stream()
                    .map(tag -> Integer.parseInt(tag.toString()))
                    .collect(Collectors.toList());
        }

        // 保存资源及标签
        resourceService.saveResourceWithTags(resource, tagIds);

        return R.ok();
    }

    /**
     * 更新资源
     */
    @Operation(summary = "更新资源", description = "更新已有资源")
    @PutMapping("/{id}")
    public R update(@PathVariable("id") Integer id, @RequestBody Map<String, Object> params) {
        ResourceEntity resource = resourceService.getById(id);
        if (resource == null) {
            return R.error("资源不存在");
        }

        // 更新资源基本信息
        if (params.get("title") != null)
            resource.setTitle((String) params.get("title"));
        if (params.get("description") != null)
            resource.setDescription((String) params.get("description"));
        if (params.get("content") != null)
            resource.setContent((String) params.get("content"));
        if (params.get("url") != null)
            resource.setUrl((String) params.get("url"));
        if (params.get("coverImage") != null)
            resource.setCoverImage((String) params.get("coverImage"));
        if (params.get("type") != null)
            resource.setType((String) params.get("type"));
        if (params.get("duration") != null)
            resource.setDuration(Integer.parseInt(params.get("duration").toString()));
        if (params.get("size") != null)
            resource.setSize(Long.parseLong(params.get("size").toString()));
        if (params.get("format") != null)
            resource.setFormat((String) params.get("format"));
        if (params.get("status") != null)
            resource.setStatus(Integer.parseInt(params.get("status").toString()));

        // 处理标签
        List<Integer> tagIds = null;
        if (params.get("tags") != null) {
            tagIds = ((List<Object>) params.get("tags")).stream()
                    .map(tag -> Integer.parseInt(tag.toString()))
                    .collect(Collectors.toList());
        }

        // 更新资源及标签
        resourceService.updateResourceWithTags(resource, tagIds);

        return R.ok();
    }

    /**
     * 删除资源
     */
    @Operation(summary = "删除资源", description = "根据ID删除资源")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable("id") Integer id) {
        // 检查资源是否存在
        ResourceEntity resource = resourceService.getById(id);
        if (resource == null) {
            return R.error("资源不存在");
        }

        // 删除资源及其标签关联
        resourceService.removeById(id);

        return R.ok();
    }

    /**
     * 点赞资源
     */
    @Operation(summary = "点赞资源", description = "为资源点赞")
    @PostMapping("/{id}/like")
    public R like(@PathVariable("id") Integer id) {
        ResourceEntity resource = resourceService.getById(id);
        if (resource == null) {
            return R.error("资源不存在");
        }

        // 增加点赞次数
        resourceService.incrementLikeCount(id);

        return R.ok();
    }

    /**
     * 获取所有标签
     */
    @Operation(summary = "获取所有标签", description = "获取系统中所有可用的标签")
    @GetMapping("/tags")
    public R getAllTags() {
        List<TagEntity> tags = tagService.getAllTags();
        return R.ok().put("data", tags);
    }

    /**
     * 根据标签查询资源
     */
    @Operation(summary = "根据标签查询资源", description = "获取包含指定标签的资源列表")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "size", description = "每页数量", required = true),
            @Parameter(name = "tagId", description = "标签ID", required = true)
    })
    @GetMapping("/bytag/{tagId}")
    public R listByTag(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @PathVariable("tagId") Integer tagId) {

        // 构建查询参数
        Map<String, Object> params = Map.of(
                "page", page,
                "size", size);

        // 查询包含指定标签的资源
        QueryWrapper<ResourceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id", "SELECT resource_id FROM resource_tag WHERE tag_id = " + tagId);
        queryWrapper.eq("status", 1); // 只查询已发布的资源
        queryWrapper.orderByDesc("created_at");

        return R.ok().put("data", resourceService.queryPage(params, queryWrapper));
    }
}