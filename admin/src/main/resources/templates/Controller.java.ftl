package ${packageName}.controller;

import ${packageName}.domain.${entity.className};
import ${packageName}.model.request.${entity.className}Request;
import ${packageName}.service.${entity.className}Service;
import com.dodoyd.moyu.common.model.BaseResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * ${entity.className}Controller控制器
 *
 * @author ${author!}
 * @since ${.now?string["yyyy-MM-dd"]}
 */
@RestController
@RequestMapping("/api/${entity.className?uncap_first}")
public class ${entity.className}Controller {

    @Resource
    private ${entity.className}Service ${entity.className?uncap_first}Service;

    /**
     * 查询${entity.className}列表
     */
    @PostMapping(value = "/list")
    public BaseResponse<List<${entity.className}>> queryList(@RequestBody ${entity.className}Request request) {
        List<${entity.className}> list = ${entity.className?uncap_first}Service.query${entity.className}List(request);
        return BaseResponse.getSuccessResponse(list);
    }

    /**
     * 查询${entity.className}详细信息
     */
    @GetMapping(value = "/get")
    public BaseResponse<${entity.className}> getInfo(${entity.pkColumn.javaType} ${entity.pkColumn.javaName}) {
        ${entity.className} info = ${entity.className?uncap_first}Service.query${entity.className}By${entity.pkColumn.javaName?cap_first}(${entity.pkColumn.javaName});
        return BaseResponse.getSuccessResponse(info);
    }

    /**
     * 查询${entity.className}详细信息
     */
    @PostMapping(value = "/query")
    public BaseResponse<${entity.className}> queryInfo(@RequestBody ${entity.className}Request request) {
        ${entity.className} info = ${entity.className?uncap_first}Service.query${entity.className}(request);
        return BaseResponse.getSuccessResponse(info);
    }

    /**
     * 新增${entity.className}
     */
    @PostMapping(value = "/add")
    public BaseResponse<?> add(@RequestBody ${entity.className}Request request) {
        ${entity.className?uncap_first}Service.add${entity.className}(request);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 修改${entity.className}
     */
    @PostMapping(value = "/edit")
    public BaseResponse<?> edit(@RequestBody ${entity.className}Request request) {
        ${entity.className?uncap_first}Service.update${entity.className}(request);
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 删除${entity.className}
     */
    @PostMapping(value = "/delete")
    public BaseResponse<?> delete(${entity.pkColumn.javaType} ${entity.pkColumn.javaName}) {
        ${entity.className?uncap_first}Service.delete${entity.className}(${entity.pkColumn.javaName});
        return BaseResponse.getSuccessResponse();
    }

    /**
     * 批量删除${entity.className}
     * 请求参数Key为${entity.pkColumn.javaName}List, value为逗号分割的字符串，如:idList=1,2,3
     */
    @PostMapping(value = "/batchDelete")
    public BaseResponse<?> batchDelete(@RequestParam("${entity.pkColumn.javaName}List") List<${entity.pkColumn.javaType}> ${entity.pkColumn.javaName}List) {
        ${entity.className?uncap_first}Service.batchDelete${entity.className}(${entity.pkColumn.javaName}List);
        return BaseResponse.getSuccessResponse();
    }
}
