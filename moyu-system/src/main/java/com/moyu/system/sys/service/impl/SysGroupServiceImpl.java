package com.moyu.system.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.constant.SysConstants;
import com.moyu.system.sys.mapper.SysGroupMapper;
import com.moyu.system.sys.model.entity.SysGroup;
import com.moyu.system.sys.model.param.SysGroupParam;
import com.moyu.system.sys.service.SysOrgService;
import com.moyu.system.sys.service.SysGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author shisong
 * @description 针对表【sys_pos(岗位信息表)】的数据库操作Service实现
 * @createDate 2024-12-20 14:29:15
 */
@Service
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements SysGroupService {

    @Resource
    private SysOrgService sysOrgService;

    @Override
    public List<SysGroup> list(SysGroupParam groupParam) {
        QueryWrapper<SysGroup> queryWrapper = new QueryWrapper<SysGroup>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 查询部分字段
//                .select(SysMenu::getCode, SysMenu::getName, SysMenu::getSortNum)
                // 关键词搜索
                .like(StrUtil.isNotBlank(groupParam.getSearchKey()), SysGroup::getName, groupParam.getSearchKey())
                // 模糊搜索所属组织(选的是一个code，搜的是所有code)
                .like(StrUtil.isNotBlank(groupParam.getOrgCode()), SysGroup::getOrgChain, groupParam.getOrgCode())
                // 指定类型
                .eq(ObjectUtil.isNotEmpty(groupParam.getGroupType()), SysGroup::getGroupType, groupParam.getGroupType())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(groupParam.getStatus()), SysGroup::getStatus, groupParam.getStatus())
                .eq(SysGroup::getDeleteFlag, 0)
                .orderByAsc(SysGroup::getSortNum);
        // 查询
        List<SysGroup> groupList = this.list(queryWrapper);
        return groupList;
    }

    @Override
    public PageResult<SysGroup> pageList(SysGroupParam groupParam) {
        QueryWrapper<SysGroup> queryWrapper = new QueryWrapper<SysGroup>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 关键词搜索
                .like(StrUtil.isNotBlank(groupParam.getSearchKey()), SysGroup::getName, groupParam.getSearchKey())
                // 模糊搜索所属组织(选的是一个code，搜的是所有code)
                .like(StrUtil.isNotBlank(groupParam.getOrgCode()), SysGroup::getOrgChain, groupParam.getOrgCode())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(groupParam.getStatus()), SysGroup::getStatus, groupParam.getStatus())
                .eq(SysGroup::getDeleteFlag, 0)
                .orderByAsc(SysGroup::getSortNum);
        // 分页查询
        Page<SysGroup> page = new Page<>(groupParam.getPageNum(), groupParam.getPageSize());
        Page<SysGroup> groupPage = this.page(page, queryWrapper);
        return new PageResult<>(groupPage.getTotal(), groupPage.getRecords());
    }

    @Override
    public SysGroup detail(SysGroupParam groupParam) {
        LambdaQueryWrapper<SysGroup> queryWrapper = new QueryWrapper<SysGroup>().checkSqlInjection().lambda()
                .eq(ObjectUtil.isNotEmpty(groupParam.getId()), SysGroup::getId, groupParam.getId())
                .eq(ObjectUtil.isNotEmpty(groupParam.getCode()), SysGroup::getCode, groupParam.getCode());
        // id、code均为唯一标识
        SysGroup SysGroup = this.getOne(queryWrapper);
        if (SysGroup == null) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "未查到指定数据");
        }
        return SysGroup;
    }

    @Override
    public void add(SysGroupParam groupParam) {
        // 属性复制
        SysGroup group = BeanUtil.copyProperties(groupParam, SysGroup.class);
        group.setId(null);
        // 若未指定唯一编码code，则自动生成
        if (Strings.isNullOrEmpty(group.getCode())) {
            // 唯一code RandomUtil.randomString(10)、IdUtil.objectId()24位
            group.setCode(IdUtil.objectId());
        }
        // 若指定了直属组织，则设置所属组织
        if (ObjectUtil.isNotEmpty(group.getOrgCode())) {
            // 获取组织结构树
            Tree<String> orgTree = sysOrgService.singleTree(SysConstants.ROOT_ID);
            Tree<String> orgNode = orgTree.getNode(group.getOrgCode());
            // 设置直属机构名称
            group.setOrgName(orgNode.getName().toString());
            // 所属机构列表
            List<String> list = TreeUtil.getParentsId(orgNode, true);
            group.setOrgChain(Joiner.on(",").join(list));
        }
        this.save(group);
    }

    @Override
    public void deleteByIds(SysGroupParam groupParam) {
        // 待删除的id集合
        Set<Long> idSet = groupParam.getIds();
        // 逻辑删除
        UpdateWrapper<SysGroup> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idSet).set("delete_flag", 1);
        this.update(updateWrapper);
    }

    @Override
    public void edit(SysGroupParam groupParam) {
        SysGroup oldGroup = this.detail(groupParam);
        // 属性复制
        SysGroup updateOrg = BeanUtil.copyProperties(groupParam, SysGroup.class);
        updateOrg.setId(oldGroup.getId());
        // 若指定了直属组织，则设置所属组织
        if (ObjectUtil.isNotEmpty(updateOrg.getOrgCode())) {
            // 获取组织结构树
            Tree<String> orgTree = sysOrgService.singleTree(SysConstants.ROOT_ID);
            Tree<String> orgNode = orgTree.getNode(updateOrg.getOrgCode());
            List<String> list = TreeUtil.getParentsId(orgNode, true);
            updateOrg.setOrgChain(Joiner.on(",").join(list));
        }
        this.updateById(updateOrg);
    }

}




