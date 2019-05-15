package ${targetPackage};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author duanxq
 * Created by duanxq on ${.now?string('yyyy-MM-hh')}.
 */
@Data
@ApiModel("${tableClass.shortClassName}")
public class ${tableClass.shortClassName}${suffix} {

<#if tableClass.pkFields??>
<#list tableClass.pkFields as field>
    @ApiModelProperty("${field.remarks}")
    private ${field.shortTypeName} ${dashedToCamel(field.columnName)};
</#list>
</#if>
<#if tableClass.baseFields??>
<#list tableClass.baseFields as field>
    @ApiModelProperty("${field.remarks}")
    private ${field.shortTypeName} ${dashedToCamel(field.columnName)};
</#list>
</#if>

}

<#-- 下划线转驼峰 -->
<#function dashedToCamel(s)>
    <#return s
    ?replace('(^_+)|(_+$)', '', 'r')
    ?replace('\\_+(\\w)?', ' $1', 'r')
    ?replace('([A-Z])', ' $1', 'r')
    ?capitalize
    ?replace(' ' , '')
    ?uncap_first
    >
</#function>