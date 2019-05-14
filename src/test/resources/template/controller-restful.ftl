package ${targetPackage};

import com.github.pagehelper.PageHelper;
import ${basePackage}.core.entity.BatchResultDTO;
import ${basePackage}.core.entity.SuccessResult;
import ${tableClass.fullClassName};
import ${basePackage}.service.${tableClass.shortClassName}Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author duanxq
 * Created by duanxq on ${.now?string('yyyy-MM-hh')}.
 */
@Api(tags = "${tableClass.shortClassName}")
@RestController
@RequestMapping("${tableClass.lowerCaseName}")
public class ${tableClass.shortClassName}${suffix} {

    @Resource
    private ${tableClass.shortClassName}Service ${tableClass.lowerCaseName}Service;

    @ApiOperation("添加")
    @PostMapping("add")
    public SuccessResult add(@RequestBody ${tableClass.shortClassName} ${tableClass.lowerCaseName}) {
        ${tableClass.lowerCaseName}Service.add(${tableClass.lowerCaseName});
        return new SuccessResult();
    }

    @ApiOperation("删除")
    @PostMapping("delete/{id}")
    public SuccessResult delete(@PathVariable Integer id) {
        ${tableClass.lowerCaseName}Service.del(id);
        return new SuccessResult();
    }

    @ApiOperation("更新")
    @PostMapping("update")
    public SuccessResult update(@RequestBody ${tableClass.shortClassName} ${tableClass.lowerCaseName}) {
        ${tableClass.lowerCaseName}Service.editByPK(${tableClass.lowerCaseName});
        return new SuccessResult();
    }

    @ApiOperation("详情")
    @GetMapping("detail/{id}")
    public ${tableClass.shortClassName} detail(@PathVariable Integer id) {
        return ${tableClass.lowerCaseName}Service.queryByPK(id);
    }

    @ApiOperation("列表")
    @GetMapping("list")
    public BatchResultDTO<${tableClass.shortClassName}> list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        return new BatchResultDTO<>(${tableClass.lowerCaseName}Service.queryForList(new ${tableClass.shortClassName}()));
    }
}
