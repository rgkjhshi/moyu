package ${packageName}.service;

import ${packageName}.model.${modelName};
import ${packageName}.mapper.${mapperName};
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * ${serviceName}
 *
 * @author: YZ.YANG
 * @date: ${.now?string["yyyy-MM-dd HH:mm:ss"]}
 */
@Service
public class ${serviceName}Impl implements ${serviceName}{

    @Autowired
    private ${mapperName} ${mapperName?uncap_first};


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
