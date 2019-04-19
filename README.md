![Licence](https://img.shields.io/badge/licence-none-green.svg)
## 简介
基于Spring Boot & MyBatis的项目，用于快速构建中小型Restful API项目，减少重复劳动，专注于业务代码的编写。

## 特征 & 提供
- 代码生成器（CodeGenerator生成controller、service ？生成model、mapper ）
- 集成MyBatis、通用Mapper插件、PageHelper分页插件，实现单表业务零SQL
- 集成Druid数据库连接池与监控
- 日志拦截、登录拦截
- 统一异常处理
- 统一响应结果封装
- 统一的API接口文档
- 常用工具类
- 其他
 
## 快速开始
  1. 克隆项目
  2. 启动项目（用户启动内置数据库）
  3. ```mvn mybatis-generator:generate``` &  运行```CodeGenerator.main()```方法生成代码
  4. 启动项目 访问 [http://localhost/doc.html](http://localhost/doc.html)
  

![效果](https://github.com/duanxq1994/springboot-mybatis-demo/tree/master/image/example.png)
  
## 开发建议
- 表名小写，多个单词使用下划线分隔
- Model内成员变量与表字段对应，如需扩展成员变量（比如连表查询）创建DTO，否则需在扩展的成员变量上加```@Transient```注解
- 业务失败直接抛出```BizException("message")```，由统一异常处理器来封装业务失败的响应结果，比如```throw new BizException("该手机号已被注册")```
- 需要工具类的话先从```apache-commons-*```和```guava```中找，没有再造轮子或引入类库，尽量精简项目
- 开发规范遵循阿里巴巴Java开发手册（[最新版下载](https://github.com/alibaba/p3c))
- 使用Swagger2（[官网](https://swagger.io/)、[简介](https://www.cnblogs.com/JoiT/p/6378086.html)） 管理API文档
 
## 技术选型 & 文档
- [Spring Boot](http://www.jianshu.com/p/1a9fd8936bd8)
- [MyBatis](http://www.mybatis.org/mybatis-3/zh/index.html)
- [MyBatisb 通用Mapper插件](https://github.com/abel533/Mapper)
- [MyBatis PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper)
- [Druid Spring Boot Starter](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter/)
- 其他

## 其他
- 代码质量分析（[查看](https://sonarcloud.io/dashboard?id=duanxq1994_springboot-mybatis-demo)）
- 代码分享，欢迎 [Star](https://github.com/duanxq1994/springboot-mybatis-demo/stargazers) & [Fork](https://github.com/duanxq1994/springboot-mybatis-demo/network/members) 
