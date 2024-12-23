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
import com.moyu.system.sys.mapper.SysPostMapper;
import com.moyu.system.sys.model.entity.SysPost;
import com.moyu.system.sys.model.param.SysPostParam;
import com.moyu.system.sys.service.SysOrgService;
import com.moyu.system.sys.service.SysPostService;
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
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    @Resource
    private SysOrgService sysOrgService;

    @Override
    public List<SysPost> list(SysPostParam postParam) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<SysPost>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 查询部分字段
//                .select(SysMenu::getCode, SysMenu::getName, SysMenu::getSortNum)
                // 关键词搜索
                .like(StrUtil.isNotBlank(postParam.getSearchKey()), SysPost::getName, postParam.getSearchKey())
                // 模糊搜索所属组织
                .like(StrUtil.isNotBlank(postParam.getOrgs()), SysPost::getOrgs, postParam.getOrgs())
                // 指定类型
                .eq(ObjectUtil.isNotEmpty(postParam.getPostType()), SysPost::getPostType, postParam.getPostType())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(postParam.getStatus()), SysPost::getStatus, postParam.getStatus())
                .eq(SysPost::getDeleteFlag, 0)
                .orderByAsc(SysPost::getSortNum);
        // 查询
        List<SysPost> postList = this.list(queryWrapper);
        return postList;
    }

    @Override
    public PageResult<SysPost> pageList(SysPostParam postParam) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<SysPost>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 关键词搜索
                .like(StrUtil.isNotBlank(postParam.getSearchKey()), SysPost::getName, postParam.getSearchKey())
                // 模糊搜索所属组织
                .like(StrUtil.isNotBlank(postParam.getOrgs()), SysPost::getOrgs, postParam.getOrgs())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(postParam.getStatus()), SysPost::getStatus, postParam.getStatus())
                .eq(SysPost::getDeleteFlag, 0)
                .orderByAsc(SysPost::getSortNum);
        // 分页查询
        Page<SysPost> page = new Page<>(postParam.getPageNum(), postParam.getPageSize());
        Page<SysPost> postPage = this.page(page, queryWrapper);
        return new PageResult<>(postPage.getTotal(), postPage.getRecords());
    }

    @Override
    public SysPost detail(SysPostParam postParam) {
        LambdaQueryWrapper<SysPost> queryWrapper = new QueryWrapper<SysPost>().checkSqlInjection().lambda()
                .eq(ObjectUtil.isNotEmpty(postParam.getId()), SysPost::getId, postParam.getId())
                .eq(ObjectUtil.isNotEmpty(postParam.getCode()), SysPost::getCode, postParam.getCode());
        // id、code均为唯一标识
        SysPost SysPost = this.getOne(queryWrapper);
        if (SysPost == null) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "未查到指定数据");
        }
        return SysPost;
    }

    @Override
    public void add(SysPostParam postParam) {
        // 属性复制
        SysPost post = BeanUtil.copyProperties(postParam, SysPost.class);
        post.setId(null);
        // 若未指定唯一编码code，则自动生成
        if (Strings.isNullOrEmpty(post.getCode())) {
            // 唯一code RandomUtil.randomString(10)、IdUtil.objectId()24位
            post.setCode(IdUtil.objectId());
        }
        // 若指定了直属组织，则设置所属组织
        if (ObjectUtil.isNotEmpty(post.getOrgCode())) {
            // 获取组织结构树
            Tree<String> orgTree = sysOrgService.singleTree("0");
            Tree<String> orgNode = orgTree.getNode(post.getOrgCode());
            // 设置直属机构名称
            post.setOrgName(orgNode.getName().toString());
            // 所属机构列表
            List<String> list = TreeUtil.getParentsId(orgNode, true);
            post.setOrgs(Joiner.on(",").join(list));
        }
        this.save(post);
    }

    @Override
    public void deleteByIds(SysPostParam postParam) {
        // 待删除的id集合
        Set<Long> idSet = postParam.getIds();
        // 逻辑删除
        UpdateWrapper<SysPost> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idSet).set("delete_flag", 1);
        this.update(updateWrapper);
    }

    @Override
    public void edit(SysPostParam postParam) {
        SysPost oldPost = this.detail(postParam);
        // 属性复制
        SysPost updateOrg = BeanUtil.copyProperties(postParam, SysPost.class);
        updateOrg.setId(oldPost.getId());
        // 若指定了直属组织，则设置所属组织
        if (ObjectUtil.isNotEmpty(updateOrg.getOrgCode())) {
            // 获取组织结构树
            Tree<String> orgTree = sysOrgService.singleTree("0");
            Tree<String> orgNode = orgTree.getNode(updateOrg.getOrgCode());
            List<String> list = TreeUtil.getParentsId(orgNode, true);
            updateOrg.setOrgs(Joiner.on(",").join(list));
        }
        this.updateById(updateOrg);
    }

}




