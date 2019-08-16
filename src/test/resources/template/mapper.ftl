package ${targetPackage};

import ${basePackage}.common.util.MyMapper;
import ${tableClass.fullClassName};
import org.springframework.stereotype.Repository;

/**
 * @author duanxq
 * Created by duanxq on ${.now?string('yyyy-MM-dd')}.
 */
@Repository
public interface ${tableClass.shortClassName}${suffix} extends MyMapper<${tableClass.shortClassName}> {

}