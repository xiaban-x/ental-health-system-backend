package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.UserEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.UserVO;
import com.entity.view.UserView;

public interface UserService extends IService<UserEntity> {
    PageUtils queryPage(Map<String, Object> params);

    List<UserVO> selectListVO(QueryWrapper<UserEntity> wrapper);

    UserVO selectVO(QueryWrapper<UserEntity> wrapper);

    List<UserView> selectListView(QueryWrapper<UserEntity> wrapper);

    UserView selectView(QueryWrapper<UserEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<UserEntity> wrapper);
}
