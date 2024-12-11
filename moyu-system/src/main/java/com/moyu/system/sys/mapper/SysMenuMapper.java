package com.moyu.system.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moyu.system.sys.model.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shisong
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
 * @createDate 2024-12-10 21:05:13
 * @Entity com.moyu.system.sys.model.entity.SysMenu
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}




