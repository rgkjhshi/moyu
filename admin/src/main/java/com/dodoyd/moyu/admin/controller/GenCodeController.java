package com.dodoyd.moyu.admin.controller;

import com.dodoyd.moyu.admin.model.vo.TableInfo;
import com.dodoyd.moyu.admin.service.GenCodeService;
import com.dodoyd.moyu.common.annotation.Log;
import com.dodoyd.moyu.common.model.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 生成代码服务的控制器
 *
 * @author shisong02
 * @since 2023-12-19
 */
@RestController
@RequestMapping(value = "/api/tool/gen")
public class GenCodeController {

    @Resource
    private GenCodeService genCodeService;

    @Log(jsonLog = true, response = false)
    @PostMapping(value = "/list")
    public BaseResponse<List<TableInfo>> queryAllTableList() {
        List<TableInfo> list = genCodeService.queryAllTableList();
        return BaseResponse.getSuccessResponse(list);
    }

    @Log(jsonLog = true, response = false)
    @PostMapping(value = "/preview")
    public BaseResponse<Map<String, String>> previewCode(String tableName) {
        Map<String, String> codeMap = genCodeService.genCode(tableName);
        return BaseResponse.getSuccessResponse(codeMap);
    }

}
