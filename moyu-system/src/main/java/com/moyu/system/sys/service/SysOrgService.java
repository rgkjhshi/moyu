package com.moyu.system.sys.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.common.model.PageResult;
import com.moyu.common.web.model.Option;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.entity.SysOrg;
import com.moyu.system.sys.model.param.SysMenuParam;
import com.moyu.system.sys.model.param.SysOrgParam;

import java.util.List;

/**
 * @author shisong
 * @description 针对表【sys_org(组织机构表)】的数据库操作Service
 * @createDate 2024-11-26 09:55:33
 */
public interface SysOrgService extends IService<SysOrg> {

    /**
     * 分页获取组织列表
     */
    PageResult<SysOrg> pageList(SysOrgParam sysOrgParam);

    /**
     * 部门树(借助hutool的树结构)
     *
     * @return 部门树List集合
     */
    List<Tree<String>> tree();

    /**
     * 部门树形下拉选项
     *
     * @return 部门下拉List集合
     */
    List<Option<?>> listTreeOptions();

    /**
     * 获取组织机构详情
     */
    SysOrg detail(SysOrgParam orgParam);

    /**
     * 添加组织机构
     */
    void add(SysOrgParam orgParam);

    /**
     * 通过ids删除，且不会集联删除
     */
    void deleteByIds(SysOrgParam orgParam);

    /**
     * 通过codes删除，会集联删除
     */
    void deleteByCodes(SysOrgParam orgParam);

    /**
     * 修改组织机构
     */
    void edit(SysOrgParam orgParam);

}
