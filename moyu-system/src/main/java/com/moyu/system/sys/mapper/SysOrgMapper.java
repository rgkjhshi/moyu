package com.moyu.system.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moyu.system.sys.model.entity.SysOrg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author shisong
 * @description 针对表【sys_org(组织机构表)】的数据库操作Mapper
 * @createDate 2024-11-26 09:55:33
 * @Entity com.moyu.system.modular.org.domain.SysOrg
 */
@Mapper
public interface SysOrgMapper extends BaseMapper<SysOrg> {

    /**
     * 查询下属组织机构code列表(包含本身)
     */
    @Select("SELECT * FROM sys_org WHERE code = #{orgCode} OR find_in_set(#{orgCode}, org_chain)")
    List<SysOrg> selectChildren(@Param("orgCode") String orgCode);
}




