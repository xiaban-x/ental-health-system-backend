
package com.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.TokenDao;
import com.entity.TokenEntity;
import com.service.TokenService;
import com.utils.CommonUtil;
import com.utils.PageUtils;

@Service("tokenService")
public class TokenServiceImpl extends ServiceImpl<TokenDao, TokenEntity> implements TokenService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 创建查询条件
        QueryWrapper<TokenEntity> queryWrapper = new QueryWrapper<>();
        
        // 创建分页对象
        Page<TokenEntity> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
        
        // 使用page方法进行分页查询
        IPage<TokenEntity> iPage = this.page(page, queryWrapper);
        
        // 返回分页结果
        return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<TokenEntity> wrapper) {
        // 创建分页对象
        Page<TokenEntity> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
        
        // 使用page方法进行分页查询
        IPage<TokenEntity> iPage = this.page(page, wrapper);
        
        // 返回分页结果
        return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
    }

    @Override
    public List<TokenEntity> selectListView(QueryWrapper<TokenEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public String generateToken(Integer userId, String username, String tableName, String role) {
        TokenEntity tokenEntity = this.getOne(new QueryWrapper<TokenEntity>()
                .eq("user_id", userId)
                .eq("role", role));
        String token = CommonUtil.getRandomString(32);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);
        if (tokenEntity != null) {
            tokenEntity.setToken(token);
            tokenEntity.setExpiredAt(cal.getTime());
            this.updateById(tokenEntity);
        } else {
            TokenEntity newToken = new TokenEntity();
            newToken.setUserId(userId);
            newToken.setUsername(username);
            newToken.setTableName(tableName);
            newToken.setRole(role);
            newToken.setToken(token);
            newToken.setExpiredAt(cal.getTime());
            this.save(newToken);
        }
        return token;
    }

    @Override
    public TokenEntity getTokenEntity(String token) {
        TokenEntity tokenEntity = this.getOne(new QueryWrapper<TokenEntity>().eq("token", token));
        if (tokenEntity == null || tokenEntity.getExpiredAt().getTime() < new Date().getTime()) {
            return null;
        }
        return tokenEntity;
    }
}
