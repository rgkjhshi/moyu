package com.dodoyd.moyu.admin.controller;

import com.dodoyd.moyu.admin.service.GenCodeService;
import com.dodoyd.moyu.common.annotation.Log;
import com.dodoyd.moyu.common.model.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生成代码服务的控制器
 *
 * @author shisong02
 * @since 2023-12-19
 */
@RestController
@RequestMapping(value = "/api/gen")
public class GenCodeController {

    @Resource
    private GenCodeService genCodeService;

    @Log(jsonLog = true, response = false)
    @PostMapping(value = "/list")
    public BaseResponse<List<?>> queryAllTableList() {
        List<?> list = genCodeService.queryAllTableList();
        return BaseResponse.getSuccessResponse(list);
    }

}
