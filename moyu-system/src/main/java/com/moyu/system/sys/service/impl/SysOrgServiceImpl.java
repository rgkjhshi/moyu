package com.moyu.system.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.common.model.PageResult;
import com.moyu.common.web.model.Option;
import com.moyu.system.sys.mapper.SysOrgMapper;
import com.moyu.system.sys.model.entity.SysOrg;
import com.moyu.system.sys.model.param.SysOrgParam;
import com.moyu.system.sys.service.SysOrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shisong
 * @description 针对表【sys_org(组织机构表)】的数据库操作Service实现
 * @createDate 2024-11-26 09:55:33
 */
@Service
@RequiredArgsConstructor
public class SysOrgServiceImpl extends ServiceImpl<SysOrgMapper, SysOrg> implements SysOrgService {

    /**
     * 获取组织分页
     */
    @Override
    public PageResult<SysOrg> pageList(SysOrgParam orgParam) {
        QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<SysOrg>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 关键词搜索
                .like(StrUtil.isNotBlank(orgParam.getKeywords()), SysOrg::getName, orgParam.getKeywords())
                // 指定父节点
                .eq(ObjectUtil.isNotEmpty(orgParam.getPid()), SysOrg::getPid, orgParam.getPid())
                .orderByAsc(SysOrg::getSortNum);
        // 分页查询
        Page<SysOrg> page = new Page<>(orgParam.getPageNum(), orgParam.getPageSize());
        Page<SysOrg> orgPage = this.page(page, queryWrapper);
        return new PageResult<>(orgPage.getTotal(), orgPage.getRecords());
    }

    /**
     * 部门树形下拉选项
     */
    @Override
    public List<Option<?>> listTreeOptions() {
        // 查询所有组织结构
        List<SysOrg> orgList = this.list(new LambdaQueryWrapper<SysOrg>()
                // 查询部分字段
                .select(SysOrg::getId, SysOrg::getPid, SysOrg::getName)
                .eq(SysOrg::getDeleteFlag, 0)
                .orderByAsc(SysOrg::getSortNum)
        );
        // 所有的父节点
        Set<Long> parentIds = orgList.stream().map(SysOrg::getPid).collect(Collectors.toSet());
        // 所有的节点id
        Set<Long> deptIds = orgList.stream().map(SysOrg::getId).collect(Collectors.toSet());
        // 集合差，根结点
        List<Long> rootIds = CollectionUtil.subtractToList(parentIds, deptIds);
        // 遍历根结点
        List<Option<?>> rootList = orgList.stream()
                .filter(org -> rootIds.contains(org.getId()))
                .map(org -> {
                    Option<Long> root = new Option<>(org.getId(), org.getName());
                    root.setChildren(recursionBuildChildren(org.getId(), orgList));
                    return root;
                })
                .collect(Collectors.toList());
        return rootList;
    }

    /**
     * 递归生成部门子层级
     */
    public static List<Option<Long>> recursionBuildChildren(Long parentId, List<SysOrg> orgList) {
        List<Option<Long>> list = CollectionUtil.emptyIfNull(orgList).stream()
                .filter(org -> org.getPid().equals(parentId))
                .map(org -> {
                    Option<Long> option = new Option<>(org.getId(), org.getName());
                    List<Option<Long>> children = recursionBuildChildren(org.getId(), orgList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        option.setChildren(children);
                    }
                    return option;
                })
                .collect(Collectors.toList());
        return list;
    }

}




