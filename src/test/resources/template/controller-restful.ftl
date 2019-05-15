package ${targetPackage};

import ${basePackage}.common.util.DozerUtil;
import ${basePackage}.core.entity.BatchResultDTO;
import ${basePackage}.core.entity.PageDO;
import ${basePackage}.core.entity.SuccessResult;
import ${tableClass.fullClassName};
import ${basePackage}.service.${tableClass.shortClassName}Service;
import ${basePackage}.vo.${tableClass.shortClassName}VO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author duanxq
 * Created by duanxq on ${.now?string('yyyy-MM-hh')}.
 */
@Api(tags = "${tableClass.shortClassName}")
@RestController
@RequestMapping("${tableClass.variableName}")
public class ${tableClass.shortClassName}${suffix} {

    @Autowired
    private ${tableClass.shortClassName}Service ${tableClass.variableName}Service;
    @Autowired
    private DozerUtil dozerUtil;

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
    public ${tableClass.shortClassName}VO detail(@ApiIgnore @PathVariable Integer id) {
        ${tableClass.shortClassName} ${tableClass.variableName} = ${tableClass.variableName}Service.queryByPK(id);
        return dozerUtil.convert(${tableClass.variableName}, ${tableClass.shortClassName}VO.class);
    }

    @ApiOperation("列表")
    @GetMapping("list")
    public BatchResultDTO<${tableClass.shortClassName}VO> list(PageDO pageDO) {
        List<${tableClass.shortClassName}> ${tableClass.variableName}s = ${tableClass.variableName}Service.queryForPageList(new ${tableClass.shortClassName}(), pageDO);
        List<${tableClass.shortClassName}VO> ${tableClass.variableName}VOList = dozerUtil.convert(${tableClass.variableName}s, ${tableClass.shortClassName}VO.class);
        return BatchResultDTO.of(${tableClass.variableName}VOList);
    }
}
