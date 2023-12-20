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
public interface ${serviceName}{

    /**
     * 查询列表
     */
    List<${modelName}> get${modelName}List();

     /**
      * 查询实体
      */
     ${modelName} get${modelName}(int id);

    /**
     * 插入
     */
    int insert${modelName}(${modelName} ${modelName?lower_case});

    /**
     * 更新
     */
    int update${modelName}(${modelName} ${modelName?lower_case});

    /**
     * 删除
     */
    int delete${modelName}(int id);

}

