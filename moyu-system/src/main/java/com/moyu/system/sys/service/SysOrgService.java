package com.moyu.system.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.common.model.PageResult;
import com.moyu.common.web.model.Option;
import com.moyu.system.sys.model.entity.SysOrg;
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
     * 部门树形下拉选项
     *
     * @return 部门下拉List集合
     */
    List<Option<?>> listTreeOptions();

}
