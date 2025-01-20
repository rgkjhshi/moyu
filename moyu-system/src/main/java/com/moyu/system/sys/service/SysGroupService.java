package com.moyu.system.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysGroup;
import com.moyu.system.sys.model.param.SysGroupParam;

import java.util.List;

/**
 * @author shisong
 * @description 针对表【sys_pos(岗位信息表)】的数据库操作Service
 * @createDate 2024-12-20 14:29:15
 */
public interface SysGroupService extends IService<SysGroup> {

    /**
     * 获取记录列表
     */
    List<SysGroup> list(SysGroupParam groupParam);

    /**
     * 分页获取记录列表
     */
    PageResult<SysGroup> pageList(SysGroupParam groupParam);

    /**
     * 获取记录详情
     */
    SysGroup detail(SysGroupParam groupParam);

    /**
     * 添加记录
     */
    void add(SysGroupParam groupParam);

    /**
     * 通过ids删除记录
     */
    void deleteByIds(SysGroupParam groupParam);

    /**
     * 修改记录
     */
    void edit(SysGroupParam groupParam);

}
