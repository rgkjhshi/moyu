package com.moyu.system.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
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
     * 部门树(树太大需要加缓存)
     */
    @Override
    public List<Tree<String>> tree() {
        // 查询所有组织结构
        List<SysOrg> orgList = this.list(new LambdaQueryWrapper<SysOrg>()
                // 查询部分字段
                .select(SysOrg::getCode, SysOrg::getParentCode, SysOrg::getName, SysOrg::getSortNum)
                .eq(SysOrg::getDeleteFlag, 0)
                .orderByAsc(SysOrg::getSortNum)
        );
        // 结构转换
        List<TreeNode<String>> treeNodeList = orgList.stream()
                .map(org -> new TreeNode<>(org.getCode(), org.getParentCode(), org.getName(), org.getSortNum()))
                .collect(Collectors.toList());
        // 构建树
        return TreeUtil.build(treeNodeList, "0");
    }

    /**
     * 部门树形下拉选项
     */
    @Override
    public List<Option<?>> listTreeOptions() {
        // 查询所有组织结构
        List<SysOrg> orgList = this.list(new LambdaQueryWrapper<SysOrg>()
                // 查询部分字段
                .select(SysOrg::getParentCode, SysOrg::getCode, SysOrg::getName)
                .eq(SysOrg::getDeleteFlag, 0)
                .orderByAsc(SysOrg::getSortNum)
        );
        // 所有的父节点
        Set<String> parentIds = orgList.stream().map(SysOrg::getParentCode).collect(Collectors.toSet());
        // 所有的节点id
        Set<String> deptIds = orgList.stream().map(SysOrg::getCode).collect(Collectors.toSet());
        // 集合差，根结点
        List<String> rootIds = CollectionUtil.subtractToList(parentIds, deptIds);
        // 遍历根结点
        List<Option<?>> rootList = new ArrayList<>();
        for (String rootId : rootIds) {
            rootList.addAll(recursionBuildChildren(rootId, orgList));
        }
        return rootList;
    }

    /**
     * 获取组织分页
     */
    @Override
    public PageResult<SysOrg> pageList(SysOrgParam orgParam) {
        QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<SysOrg>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 关键词搜索
                .like(StrUtil.isNotBlank(orgParam.getSearchKey()), SysOrg::getName, orgParam.getSearchKey())
                // 指定父节点
                .eq(ObjectUtil.isNotEmpty(orgParam.getParentId()), SysOrg::getParentCode, orgParam.getParentId())
                .eq(SysOrg::getDeleteFlag, 0)
                .orderByAsc(SysOrg::getSortNum);
        // 分页查询
        Page<SysOrg> page = new Page<>(orgParam.getPageNum(), orgParam.getPageSize());
        Page<SysOrg> orgPage = this.page(page, queryWrapper);
        return new PageResult<>(orgPage.getTotal(), orgPage.getRecords());
    }

    /**
     * 递归生成部门子层级
     */
    public static List<Option<String>> recursionBuildChildren(String parentCode, List<SysOrg> orgList) {
        List<Option<String>> list = CollectionUtil.emptyIfNull(orgList).stream()
                .filter(org -> org.getParentCode().equals(parentCode))
                .map(org -> {
                    Option<String> option = new Option<>(org.getCode(), org.getName());
                    List<Option<String>> children = recursionBuildChildren(org.getCode(), orgList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        option.setChildren(children);
                    }
                    return option;
                })
                .collect(Collectors.toList());
        return list;
    }

}




