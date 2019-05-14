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
    @Override
    void add(${modelNameUpperCamel} obj);

    /**
     * 删除
     *
     * @param key
     * @return
     */
    @Override
    void del(Object key);

    /**
     * 根据主键编辑
     *
     * @param obj
     * @return
     */
    @Override
    void editByPK(${modelNameUpperCamel} obj);

    /**
     * 根据条件编辑
     *
     * @param obj     需要编辑的内容
     * @param example 编辑条件
     */
    @Override
    void editByExample(${modelNameUpperCamel} obj, Example example);

    /**
     * 根据主键查询
     *
     * @param key
     * @return
     */
    @Override
    ${modelNameUpperCamel} queryByPK(Object key);

    /**
     * 根据条件查询list
     *
     * @param obj
     * @return
     */
    @Override
    List<${modelNameUpperCamel}> queryForList(${modelNameUpperCamel} obj);

    /**
    * 根据条件count
    * @param obj
    * @return
    */
    @Override
    Integer queryForCount(${modelNameUpperCamel} obj);

    /**
    * 分页查询list
    *
    * @param obj
    * @return
    */
    @Override
    BatchResultDTO<${modelNameUpperCamel}> queryForPageList(${modelNameUpperCamel} obj);


}
