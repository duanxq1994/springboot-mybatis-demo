package ${basePackage}.web;

import com.github.pagehelper.PageHelper;
import ${basePackage}.core.entity.BatchResultDTO;
import ${basePackage}.core.entity.SuccessResult;
import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* Created by ${author} on ${date}.
*/
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller {

    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @PostMapping("add")
    public SuccessResult add(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.add(${modelNameLowerCamel});
        return new SuccessResult();
    }

    @PostMapping("delete/{id}")
    public SuccessResult delete(@PathVariable Integer id) {
        ${modelNameLowerCamel}Service.del(id);
        return new SuccessResult();
    }

    @PostMapping("update")
    public SuccessResult update(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.editByPK(${modelNameLowerCamel});
        return new SuccessResult();
    }

    @GetMapping("detail/{id}")
    public ${modelNameUpperCamel} detail(@PathVariable Integer id) {
        return ${modelNameLowerCamel}Service.queryByPK(id);
    }

    @GetMapping("list")
    public BatchResultDTO<${modelNameUpperCamel}> list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        return new BatchResultDTO<>(${modelNameLowerCamel}Service.queryForList(new ${modelNameUpperCamel}()));
    }
}
