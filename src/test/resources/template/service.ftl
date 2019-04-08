package ${basePackage}.service;

import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.core.entity.BatchResultDTO;
import ${basePackage}.core.IService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by ${author} on ${date}.
 */
public interface ${modelNameUpperCamel}Service extends IService<${modelNameUpperCamel}> {

    /**
     * 新增
     *
     * @param obj
     * @return
     */
    void add(${modelNameUpperCamel} obj);

    /**
     * 删除
     *
     * @param key
     * @return
     */
    void del(Object key);

    /**
     * 根据主键编辑
     *
     * @param obj
     * @return
     */
    void editByPK(${modelNameUpperCamel} obj);

    /**
     * 根据条件编辑
     *
     * @param obj     需要编辑的内容
     * @param example 编辑条件
     */
    void editByExample(${modelNameUpperCamel} obj, Example example);

    /**
     * 根据主键查询
     *
     * @param key
     * @return
     */
    ${modelNameUpperCamel} queryByPK(Object key);

    /**
     * 根据条件查询list
     *
     * @param obj
     * @return
     */
    List<${modelNameUpperCamel}> queryForList(${modelNameUpperCamel} obj);

    /**
    * 根据条件count
    * @param obj
    * @return
    */
    Integer queryForCount(${modelNameUpperCamel} obj);

    /**
    * 分页查询list
    *
    * @param obj
    * @return
    */
    BatchResultDTO<${modelNameUpperCamel}> queryForPageList(${modelNameUpperCamel} obj);


}
