package ${packageName}.service.impl;

import ${packageName}.domain.${entity.className};
import ${packageName}.dao.${entity.className}Dao;
import ${packageName}.service.${entity.className}Service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * ${entity.className}服务实现类
 *
 * @author ${author!}
 * @since ${.now?string["yyyy-MM-dd"]}
 */
@Service
public class ${entity.className}ServiceImpl implements ${entity.className}Service {

    @Resource
    private ${entity.className}Service ${entity.className?uncap_first}Service;

    @Override
    public List<${modelName}> get${modelName}List(){
        return ${mapperName?uncap_first}.selete${modelName}All();
    }


    @Override
    public ${modelName} get${modelName}(int id){
        return ${serviceName?uncap_first}.selete${modelName}ById(id);
    }


    @Override
    public int insert${modelName}(${modelName} ${modelName?lower_case}){
        return ${serviceName?uncap_first}.insert${modelName}(${modelName?lower_case});
    }


    @Override
    public int update${modelName}(${modelName} ${modelName?lower_case}){
        return ${serviceName?uncap_first}.update${modelName}(${modelName?lower_case});
    }


    @Override
    public int delete${modelName}(int id){
        return ${serviceName?uncap_first}.delete${modelName}ById(id);
    }

}
