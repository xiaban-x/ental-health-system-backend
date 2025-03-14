
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
        IPage<TokenEntity> page = this.page(
                new Page<TokenEntity>(
                        params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                        params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
                new QueryWrapper<TokenEntity>());
        return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }

    @Override
    public List<TokenEntity> selectListView(QueryWrapper<TokenEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<TokenEntity> wrapper) {
        Page<TokenEntity> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public String generateToken(Long userid, String username, String tableName, String role) {
        TokenEntity tokenEntity = this.getOne(new QueryWrapper<TokenEntity>()
                .eq("userid", userid)
                .eq("role", role));
        String token = CommonUtil.getRandomString(32);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);
        if (tokenEntity != null) {
            tokenEntity.setToken(token);
            tokenEntity.setExpiratedtime(cal.getTime());
            this.updateById(tokenEntity);
        } else {
            this.save(new TokenEntity(userid, username, tableName, role, token, cal.getTime()));
        }
        return token;
    }

    @Override
    public TokenEntity getTokenEntity(String token) {
        TokenEntity tokenEntity = this.getOne(new QueryWrapper<TokenEntity>().eq("token", token));
        if (tokenEntity == null || tokenEntity.getExpiratedtime().getTime() < new Date().getTime()) {
            return null;
        }
        return tokenEntity;
    }
}
