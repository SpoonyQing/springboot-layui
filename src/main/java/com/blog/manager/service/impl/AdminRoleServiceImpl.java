package com.blog.manager.service.impl;

import com.blog.manager.response.PageDataResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.blog.manager.dao.BaseAdminPermissionMapper;
import com.blog.manager.pojo.BaseAdminRole;
import com.blog.manager.dao.BaseAdminRoleMapper;
import com.blog.manager.dto.AdminRoleDTO;
import com.blog.manager.pojo.BaseAdminPermission;
import com.blog.manager.service.AdminRoleService;
import com.blog.manager.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: AdminRoleServiceImpl
 * @Description:
 * @author: dongwn
 * @version: 1.0
 */
@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BaseAdminRoleMapper baseAdminRoleMapper;

    @Autowired
    private BaseAdminPermissionMapper baseAdminPermission;

    @Override
    public PageDataResult getRoleList(Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        List<BaseAdminRole> roles = baseAdminRoleMapper.getRoleList();

        List<AdminRoleDTO> roleList = new ArrayList <>();
        for(BaseAdminRole r:roles){
            AdminRoleDTO roleDTO =  new AdminRoleDTO();

            String permissions = r.getPermissions();
            BeanUtils.copyProperties(r,roleDTO);
            roleDTO.setPermissionIds(permissions);

            if(!StringUtils.isEmpty(permissions)){
                String[] ids = permissions.split(",");
                List<String> p = new ArrayList <>();
                for(String id: ids){
                    BaseAdminPermission baseAdminPermission = this.baseAdminPermission.selectByPrimaryKey(id);
                    String name = baseAdminPermission.getName();
                    p.add(name);
                }
                roleDTO.setPermissions(p.toString());
            }
            roleList.add(roleDTO);
        }

        PageHelper.startPage(pageNum, pageSize);

        if(roleList.size() != 0){
            PageInfo<AdminRoleDTO> pageInfo = new PageInfo<>(roleList);
            pageDataResult.setList(roleList);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }
        return pageDataResult;
    }

    @Override
    public Map<String,Object> addRole(BaseAdminRole role) {
        Map<String,Object> data = new HashMap();
        try {
            role.setCreateTime(DateUtils.getCurrentDate());
            role.setUpdateTime(DateUtils.getCurrentDate());
            role.setRoleStatus(1);
            int result = baseAdminRoleMapper.insert(role);
            if(result == 0){
                data.put("code",0);
                data.put("msg","??????????????????");
                logger.error("??????????????????");
                return data;
            }
            data.put("code",1);
            data.put("msg","??????????????????");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("?????????????????????????????????", e);
        }
        return data;

    }

    @Override
    public BaseAdminRole findRoleById(Integer id) {
        return baseAdminRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Map<String,Object> updateRole(BaseAdminRole role) {
        Map<String,Object> data = new HashMap();
        try{
            role.setUpdateTime(DateUtils.getCurrentDate());
            int result = baseAdminRoleMapper.updateRole(role);
            if(result == 0){
                data.put("code",0);
                data.put("msg","???????????????");
                logger.error("??????[??????]?????????=???????????????");
                return data;
            }
            data.put("code",1);
            data.put("msg","???????????????");
            logger.info("??????[??????]?????????=???????????????");
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("??????[??????]?????????", e);
            return data;
        }
        return data;
    }

    @Override
    public Map<String, Object> delRole(Integer id,Integer status) {
        Map<String, Object> data = new HashMap<>();
        try {
            int result = baseAdminRoleMapper.updateRoleStatus(id,status);
            if(result == 0){
                data.put("code",0);
                data.put("msg","??????????????????");
            }
            data.put("code",1);
            data.put("msg","??????????????????");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("?????????????????????", e);
        }
        return data;
    }

    @Override
    public Map <String, Object> recoverRole(Integer id, Integer status) {
        Map<String, Object> data = new HashMap<>();
        try {
            int result = baseAdminRoleMapper.updateRoleStatus(id,status);
            if(result == 0){
                data.put("code",0);
                data.put("msg","??????????????????");
            }
            data.put("code",1);
            data.put("msg","??????????????????");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("?????????????????????", e);
        }
        return data;
    }

    @Override
    public List<BaseAdminRole> getRoles() {
        return baseAdminRoleMapper.getRoleList();
    }
}
