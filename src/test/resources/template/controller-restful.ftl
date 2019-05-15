package ${targetPackage};

import ${basePackage}.core.entity.BatchResultDTO;
import ${basePackage}.core.entity.PageDO;
import ${basePackage}.core.entity.SuccessResult;
import ${tableClass.fullClassName};
import ${basePackage}.service.${tableClass.shortClassName}Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * @author duanxq
 * Created by duanxq on ${.now?string('yyyy-MM-hh')}.
 */
@Api(tags = "${tableClass.shortClassName}")
@RestController
@RequestMapping("${tableClass.variableName}")
public class ${tableClass.shortClassName}${suffix} {

    @Resource
    private ${tableClass.shortClassName}Service ${tableClass.variableName}Service;

    @ApiOperation("添加")
    @PostMapping("add")
    public SuccessResult add(@RequestBody ${tableClass.shortClassName} ${tableClass.variableName}) {
        ${tableClass.variableName}Service.add(${tableClass.variableName});
        return new SuccessResult();
    }

    @ApiOperation("删除")
    @PostMapping("delete/{id}")
    public SuccessResult delete(@ApiIgnore @PathVariable Integer id) {
        ${tableClass.variableName}Service.del(id);
        return new SuccessResult();
    }

    @ApiOperation("更新")
    @PostMapping("update")
    public SuccessResult update(@RequestBody ${tableClass.shortClassName} ${tableClass.variableName}) {
        ${tableClass.variableName}Service.editByPK(${tableClass.variableName});
        return new SuccessResult();
    }

    @ApiOperation("详情")
    @GetMapping("detail/{id}")
    public ${tableClass.shortClassName} detail(@ApiIgnore @PathVariable Integer id) {
        return ${tableClass.variableName}Service.queryByPK(id);
    }

    @ApiOperation("列表")
    @GetMapping("list")
    public BatchResultDTO<${tableClass.shortClassName}> list(PageDO pageDO) {
        return ${tableClass.variableName}Service.queryForPageList(new ${tableClass.shortClassName}(), pageDO);
    }
}
