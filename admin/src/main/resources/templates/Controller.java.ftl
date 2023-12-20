package ${packageName}.controller;

import ${packageName}.model.${modelName};
import ${packageName}.service.${serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * ${controllerName}
 *
 * @author shisong02
 * @since ${.now?string["yyyy-MM-dd"]}
 */
@RestController
@RequestMapping("/${modelName?lower_case}")
public class ${controllerName} {

    @Autowired
    private ${serviceName} ${serviceName?uncap_first};

    /**
     * 查询列表
     */
    @GetMapping("/list" )
    public List<${modelName}> get${modelName}List(){
        return ${serviceName?uncap_first}.get${modelName}List();
    }


    /**
     * 查询实体
     */
    @GetMapping("/{id}" )
    public ${modelName} get${modelName}(@PathVariable("id") int id){
        return ${serviceName?uncap_first}.get${modelName}();
    }


    /**
     * 插入
     */
    @PostMapping("/add" )
    public int insert${modelName}(${modelName} ${modelName?lower_case}){
        return ${serviceName?uncap_first}.insert${modelName}(${modelName?lower_case});
    }


    /**
     * 更新
     */
    @PostMapping("/update")
    public int update${modelName}(${modelName} ${modelName?lower_case}){
        return ${serviceName?uncap_first}.update${modelName}(${modelName?lower_case});
    }


    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public int delete${modelName}(@PathVariable("id") int id){
        return ${serviceName?uncap_first}.delete${modelName}(id);
    }

}
