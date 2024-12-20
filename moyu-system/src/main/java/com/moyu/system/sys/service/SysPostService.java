package com.moyu.system.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.model.entity.SysPost;
import com.moyu.system.sys.model.param.SysPostParam;

import java.util.List;

/**
 * @author shisong
 * @description 针对表【sys_pos(岗位信息表)】的数据库操作Service
 * @createDate 2024-12-20 14:29:15
 */
public interface SysPostService extends IService<SysPost> {

    /**
     * 获取记录列表
     */
    List<SysPost> list(SysPostParam postParam);

    /**
     * 分页获取记录列表
     */
    PageResult<SysPost> pageList(SysPostParam postParam);

    /**
     * 获取记录详情
     */
    SysPost detail(SysPostParam postParam);

    /**
     * 添加记录
     */
    void add(SysPostParam postParam);

    /**
     * 通过ids删除记录
     */
    void deleteByIds(SysPostParam postParam);

    /**
     * 修改记录
     */
    void edit(SysPostParam postParam);

}
