package ${targetPackage};

import ${tableClass.fullClassName};
import ${basePackage}.service.${tableClass.shortClassName}Service;
import org.springframework.stereotype.Service;


/**
 * @author duanxq
 * Created by duanxq on ${.now?string('yyyy-MM-dd')}.
 */
@Service
public class ${tableClass.shortClassName}${suffix} extends BaseService<${tableClass.shortClassName}> implements ${tableClass.shortClassName}Service {


}
