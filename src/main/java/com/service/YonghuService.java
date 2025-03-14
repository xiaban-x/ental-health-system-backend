package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.YonghuEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.YonghuVO;
import com.entity.view.YonghuView;

public interface YonghuService extends IService<YonghuEntity> {
    PageUtils queryPage(Map<String, Object> params);
    
    List<YonghuVO> selectListVO(QueryWrapper<YonghuEntity> wrapper);
    
    YonghuVO selectVO(QueryWrapper<YonghuEntity> wrapper);
    
    List<YonghuView> selectListView(QueryWrapper<YonghuEntity> wrapper);
    
    YonghuView selectView(QueryWrapper<YonghuEntity> wrapper);
    
    PageUtils queryPage(Map<String, Object> params, QueryWrapper<YonghuEntity> wrapper);
}
