package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.CounselorDao;
import com.entity.CounselorEntity;
import com.service.CounselorService;

@Service
public class CounselorServiceImpl extends ServiceImpl<CounselorDao, CounselorEntity> implements CounselorService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 获取角色类型过滤条件
        Object roleTypeObj = params.get("roleType");
        Integer roleType = null;
        if (roleTypeObj != null) {
            roleType = Integer.parseInt(roleTypeObj.toString());
        }

        QueryWrapper<CounselorEntity> queryWrapper = new QueryWrapper<>();

        // 如果指定了角色类型，添加过滤条件
        if (roleType != null) {
            if (roleType == 1) { // 仅咨询师
                queryWrapper.eq("role_type", 1);
            } else if (roleType == 2) { // 仅教师
                queryWrapper.eq("role_type", 2);
            } else if (roleType == 3) { // 兼职
                queryWrapper.eq("role_type", 3);
            }
        }

        // 添加其他查询条件
        String name = (String) params.get("name");
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }

        String department = (String) params.get("department");
        if (department != null && !department.isEmpty()) {
            queryWrapper.like("department", department);
        }

        // 默认按ID排序
        queryWrapper.orderByAsc("id");

        IPage<CounselorEntity> page = this.page(
                new Page<CounselorEntity>(
                        params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                        params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
                queryWrapper);

        return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }

    @Override
    public CounselorEntity getByUserId(Integer userId) {
        QueryWrapper<CounselorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.getOne(queryWrapper);
    }
}