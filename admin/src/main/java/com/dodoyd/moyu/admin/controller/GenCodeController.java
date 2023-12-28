package com.dodoyd.moyu.admin.controller;

import com.dodoyd.moyu.admin.model.vo.TableInfo;
import com.dodoyd.moyu.admin.service.GenCodeService;
import com.dodoyd.moyu.common.annotation.Log;
import com.dodoyd.moyu.common.model.BaseResponse;
import com.google.common.io.ByteStreams;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        Map<String, String> codeMap = genCodeService.genCodeByTable(tableName);
        return BaseResponse.getSuccessResponse(codeMap);
    }

    @Log(jsonLog = true, response = false)
    @PostMapping(value = "/previewBySql")
    public BaseResponse<Map<String, String>> previewCodeBySql(String sql) {
        Map<String, String> codeMap = genCodeService.genCodeBySql(sql);
        return BaseResponse.getSuccessResponse(codeMap);
    }

    @Log
    @PostMapping(value = "/download")
    public void downloadCode(String tableNames, HttpServletResponse response) throws IOException {
        byte[] data = genCodeService.downloadCodeByTable(tableNames);
        // 设置响应头信息
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"download.zip\"");
        // 设置响应内容类型为ZIP
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        // 将ZIP文件数据写入到HTTP响应中
        try (InputStream input = new ByteArrayInputStream(data);
             OutputStream output = response.getOutputStream()) {
            // 使用 Guava 的 ByteStreams.copy() 方法将数据从 InputStream 复制到 OutputStream
            ByteStreams.copy(input, output);
        }
        // 刷新输出流，确保所有数据都被发送到客户端
        response.flushBuffer();
    }

    @Log
    @PostMapping(value = "/downloadBySql")
    public void downloadCodeBySql(String sql) {
        Map<String, String> codeMap = genCodeService.genCodeBySql(sql);
    }
}
