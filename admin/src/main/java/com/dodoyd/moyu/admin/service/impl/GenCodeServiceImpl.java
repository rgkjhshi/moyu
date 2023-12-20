package com.dodoyd.moyu.admin.service.impl;

import com.dodoyd.moyu.admin.dao.GenCodeDao;
import com.dodoyd.moyu.admin.model.vo.TableInfo;
import com.dodoyd.moyu.admin.service.GenCodeService;
import com.google.common.base.CaseFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shisong02
 * @since 2023-12-19
 */

@Slf4j
@Service
public class GenCodeServiceImpl implements GenCodeService {

    @Resource
    private GenCodeDao genCodeDao;

    @Resource
    private Configuration configuration;

    @Override
    public List<TableInfo> queryAllTableList() {
        return genCodeDao.selectAllTableList();
    }

    @Override
    public String genCode() {
        String result = "";
        try {
            // 加载模板文件
            Template template = configuration.getTemplate("Entity.java.ftl");
            // 设置模板变量
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("packageName", "packageName");
            String tableName = "user_info";
            String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
            dataMap.put("className", className);
            result = FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap);
        } catch (Exception e) {
            log.error("生成代码失败", e);
        }
        return result;
    }

}
