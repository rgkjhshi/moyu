package com.dodoyd.moyu.admin.controller;

import com.dodoyd.moyu.admin.model.request.GenCodeRequest;
import com.dodoyd.moyu.admin.model.vo.TableInfo;
import com.dodoyd.moyu.admin.service.GenCodeService;
import com.dodoyd.moyu.common.annotation.Log;
import com.dodoyd.moyu.common.model.BaseResponse;
import com.dodoyd.moyu.common.model.PageResult;
import com.google.common.io.ByteStreams;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public BaseResponse<PageResult<TableInfo>> queryAllTableList(GenCodeRequest request) {
        PageResult<TableInfo> list = genCodeService.queryAllTableList(request);
        return BaseResponse.getSuccessResponse(list);
    }

    @Log(jsonLog = true, response = false)
    @PostMapping(value = "/preview")
    public BaseResponse<Map<String, String>> previewCode(@RequestBody GenCodeRequest request) {
        Map<String, String> codeMap = genCodeService.genCodeByTable(request.getTableName());
        return BaseResponse.getSuccessResponse(codeMap);
    }

    @Log(jsonLog = true, response = false)
    @PostMapping(value = "/previewBySql")
    public BaseResponse<Map<String, String>> previewCodeBySql(@RequestBody GenCodeRequest request) {
        Map<String, String> codeMap = genCodeService.genCodeBySql(request.getSql());
        return BaseResponse.getSuccessResponse(codeMap);
    }

    @Log
    @PostMapping(value = "/download")
    public void downloadCode(@RequestBody GenCodeRequest request, HttpServletResponse response) throws IOException {
        byte[] data = genCodeService.downloadCodeByTable(request.getTableNames());
        buildResponse(response, data);
    }

    @Log
    @PostMapping(value = "/downloadBySql")
    public void downloadCodeBySql(@RequestBody GenCodeRequest request, HttpServletResponse response) throws IOException {
        byte[] data = genCodeService.downloadCodeBySql(request.getSql());
        buildResponse(response, data);
    }

    /**
     * 构造响应结果
     */
    private void buildResponse(HttpServletResponse response, byte[] data) throws IOException {
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
}
