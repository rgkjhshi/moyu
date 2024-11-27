package com.moyu.system.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.common.web.model.Option;
import com.moyu.system.sys.entity.SysOrg;

import java.util.List;

/**
 * @author shisong
 * @description 针对表【sys_org(组织机构表)】的数据库操作Service
 * @createDate 2024-11-26 09:55:33
 */
public interface SysOrgService extends IService<SysOrg> {

    /**
     * 部门树形下拉选项
     *
     * @return 部门下拉List集合
     */
    List<Option<?>> listTreeOptions();

}
