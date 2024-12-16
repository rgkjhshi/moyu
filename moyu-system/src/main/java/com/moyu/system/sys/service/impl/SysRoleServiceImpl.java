package com.moyu.system.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.common.model.PageResult;
import com.moyu.system.sys.enums.MenuTypeEnum;
import com.moyu.system.sys.enums.RelationTypeEnum;
import com.moyu.system.sys.enums.StatusEnum;
import com.moyu.system.sys.mapper.SysRoleMapper;
import com.moyu.system.sys.model.entity.SysMenu;
import com.moyu.system.sys.model.entity.SysRelation;
import com.moyu.system.sys.model.entity.SysRole;
import com.moyu.system.sys.model.param.SysMenuParam;
import com.moyu.system.sys.model.param.SysRoleParam;
import com.moyu.system.sys.service.SysMenuService;
import com.moyu.system.sys.service.SysRelationService;
import com.moyu.system.sys.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author shisong
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
 * @createDate 2024-12-15 20:49:43
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRelationService relationService;

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public List<SysRole> list(SysRoleParam roleParam) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 查询部分字段
//                .select(SysMenu::getCode, SysMenu::getName, SysMenu::getSortNum)
                // 关键词搜索
                .like(StrUtil.isNotBlank(roleParam.getSearchKey()), SysRole::getName, roleParam.getSearchKey())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(roleParam.getModule()), SysRole::getModule, roleParam.getModule())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(roleParam.getStatus()), SysRole::getStatus, roleParam.getStatus())
                .eq(SysRole::getDeleteFlag, 0)
                .orderByAsc(SysRole::getSortNum);
        // 查询
        List<SysRole> roleList = this.list(queryWrapper);
        return roleList;
    }

    @Override
    public PageResult<SysRole> pageList(SysRoleParam roleParam) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().checkSqlInjection();
        // 查询条件
        queryWrapper.lambda()
                // 关键词搜索
                .like(StrUtil.isNotBlank(roleParam.getSearchKey()), SysRole::getName, roleParam.getSearchKey())
                // 指定模块
                .eq(ObjectUtil.isNotEmpty(roleParam.getModule()), SysRole::getModule, roleParam.getModule())
                // 指定状态
                .eq(ObjectUtil.isNotEmpty(roleParam.getStatus()), SysRole::getStatus, roleParam.getStatus())
                .eq(SysRole::getDeleteFlag, 0)
                .orderByAsc(SysRole::getSortNum);
        // 分页查询
        Page<SysRole> page = new Page<>(roleParam.getPageNum(), roleParam.getPageSize());
        Page<SysRole> rolePage = this.page(page, queryWrapper);
        return new PageResult<>(rolePage.getTotal(), rolePage.getRecords());
    }

    @Override
    public SysRole detail(SysRoleParam roleParam) {
        LambdaQueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().checkSqlInjection().lambda()
                .eq(ObjectUtil.isNotEmpty(roleParam.getId()), SysRole::getId, roleParam.getId())
                .eq(ObjectUtil.isNotEmpty(roleParam.getCode()), SysRole::getCode, roleParam.getCode());
        // id、code均为唯一标识
        SysRole sysRole = this.getOne(queryWrapper);
        if (sysRole == null) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "未查到指定数据");
        }
        return sysRole;
    }

    @Override
    public void add(SysRoleParam roleParam) {
        // 若指定了唯一编码code，则必须全局唯一
        if (!Strings.isNullOrEmpty(roleParam.getCode())) {
            // 查询指定code
            SysRole role = this.getOne(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getCode, roleParam.getCode())
                    .eq(SysRole::getDeleteFlag, 0));
            if (role != null) {
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER, "唯一编码重复，请更换或留空自动生成");
            }
        }
        // 属性复制
        SysRole role = BeanUtil.copyProperties(roleParam, SysRole.class);
        role.setId(null);
        // 若未指定唯一编码code，则自动生成
        if (Strings.isNullOrEmpty(role.getCode())) {
            // 唯一code RandomUtil.randomString(10)、IdUtil.objectId()24位
            role.setCode(IdUtil.objectId());
        }
        this.save(role);
    }

    @Override
    public void deleteByIds(SysRoleParam roleParam) {
        // 待删除的id集合
        Set<Long> idSet = roleParam.getIds();
        // 逻辑删除
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idSet).set("delete_flag", 1);
        this.update(updateWrapper);
    }

    @Override
    public void edit(SysRoleParam roleParam) {
        SysRole oldRole = this.detail(roleParam);
        // 属性复制
        SysRole updateOrg = BeanUtil.copyProperties(roleParam, SysRole.class);
        updateOrg.setId(oldRole.getId());
        this.updateById(updateOrg);
    }

    @Override
    public List<Tree<String>> treeForGrant(SysRoleParam roleParam) {
        // 模块编码
        SysMenuParam menuParam = new SysMenuParam();
        menuParam.setModule(roleParam.getModule());
        menuParam.setStatus(StatusEnum.ENABLE.getCode());
        // 查询所有菜单
        List<SysMenu> menuList = sysMenuService.list(menuParam);

        // 所有的role-menu关系(menu.code->menu)
        Map<String, SysRelation> rmMap = new HashMap<>();
        relationService.list(new LambdaQueryWrapper<SysRelation>()
                        // 指定关系类型
                        .eq(SysRelation::getRelationType, RelationTypeEnum.ROLE_HAS_MENU.getCode())
                        // 指定哪个role
                        .eq(SysRelation::getObjectId, roleParam.getCode()))
                .forEach(e -> rmMap.put(e.getTargetId(), e));

        // 过滤出button，转为 parentCode->button 格式的的 multimap
        Multimap<String, Map<String, Object>> multimap = HashMultimap.create();
        menuList.stream().filter(e -> MenuTypeEnum.BUTTON.getCode().equals(e.getMenuType()))
                .forEach(e -> {
                    Map<String, Object> btnMap = new HashMap<>();
                    btnMap.put("code", e.getCode());
                    btnMap.put("name", e.getName());
                    if (rmMap.containsKey(e.getCode())) {
                        btnMap.put("checked", true);
                    }
                    multimap.put(e.getParentCode(), btnMap);
                });

        // 过滤出menu转为treeNode
        List<TreeNode<String>> nodeList = new ArrayList<>();
        menuList.stream()
                .filter(e -> !MenuTypeEnum.BUTTON.getCode().equals(e.getMenuType()))
                .forEach(e -> {
                    TreeNode<String> node = new TreeNode<>(e.getCode(), e.getParentCode(), e.getName(), e.getSortNum());
                    Map<String, Object> extMap = new HashMap<>();
                    // rm关系中存在，表示有权限
                    extMap.put("checked", rmMap.containsKey(e.getCode()));
                    // 将把包含的按钮加进来
                    extMap.put("buttonList", multimap.get(e.getCode()));
                    node.setExtra(extMap);
                    nodeList.add(node);
                });

        // 配置TreeNode使用指定的字段名
        TreeNodeConfig nodeConfig = new TreeNodeConfig();
        nodeConfig.setIdKey("code");
        nodeConfig.setParentIdKey("parentCode");

        // 构建树
        return TreeUtil.build(nodeList, roleParam.getModule(), nodeConfig, new DefaultNodeParser<>());
    }

}




