package ${packageName}.mapper;

import ${packageName}.model.${modelName};
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * ${mapperName}
 *
 * @author shisong02
 * @since ${.now?string["yyyy-MM-dd"]}
 */
@Mapper
public interface ${mapperName}{

    /**
     * 查询列表
     */
    List<${modelName}> selete${modelName}All ();

    /**
     * 查询实体
     */
    ${modelName} selete${modelName}ById (int id);

    /**
     * 插入
     */
    int insert${modelName} (${modelName} ${modelName?lower_case});

    /**
     * 更新
     */
    int update${modelName} (${modelName} ${modelName?lower_case});

    /**
     * 删除
     */
    int delete${modelName}ById (int id);

}
