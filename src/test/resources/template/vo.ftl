package ${targetPackage};

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author duanxq
 * Created by duanxq on ${.now?string('yyyy-MM-dd')}.
 */
@Data
public class ${tableClass.shortClassName}${suffix} {

<#if tableClass.allFields??>
<#list tableClass.allFields as field>
    @ApiModelProperty("${field.remarks}")
    private ${field.shortTypeName} ${field.fieldName};
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