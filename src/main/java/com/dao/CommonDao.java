
package com.dao;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CommonDao extends BaseMapper<Object> {
    List<String> getOption(Map<String, Object> params);
    
    Map<String, Object> getFollowByOption(Map<String, Object> params);
    
    List<String> getFollowByOption2(Map<String, Object> params);
    
    void sh(Map<String, Object> params);
    
    int remindCount(Map<String, Object> params);
    
    Map<String, Object> selectCal(Map<String, Object> params);
    
    List<Map<String, Object>> selectGroup(Map<String, Object> params);
    
    List<Map<String, Object>> selectValue(Map<String, Object> params);
}
