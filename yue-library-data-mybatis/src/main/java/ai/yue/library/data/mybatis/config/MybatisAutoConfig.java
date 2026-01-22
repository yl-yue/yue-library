package ai.yue.library.data.mybatis.config;

import ai.yue.library.base.config.BaseAutoConfig;
import ai.yue.library.data.mybatis.interceptor.LogicDeleteInterceptor;
import ai.yue.library.data.mybatis.interceptor.TenantCoInterceptor;
import ai.yue.library.data.mybatis.mapper.SqlMapper;
import ai.yue.library.data.mybatis.provider.AuditDataFill;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter({BaseAutoConfig.class})
@EnableConfigurationProperties(MybatisProperties.class)
@Import({AuditDataFill.class, LogicDeleteInterceptor.class, TenantCoInterceptor.class})
public class MybatisAutoConfig {

    /**
     * 扫描当前项目包下的 Mapper 接口
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(ApplicationContext applicationContext) {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();

        // 获取 SqlMapper 的包名
        String sqlMapperPackageName = SqlMapper.class.getPackage().getName();

        // 获取运行项目 mapper 的包名，规范：启动类的包名 + ".mapper"
        String startupMapperPackageName = getStartupPackageName(applicationContext) + ".mapper";

        // 设置 mapper 扫描包
        configurer.setBasePackage(sqlMapperPackageName + "," + startupMapperPackageName);
        return configurer;
    }

    /**
     * 获取运行项目启动类的包名
     */
    public String getStartupPackageName(ApplicationContext applicationContext) {
        return applicationContext.getBeansWithAnnotation(SpringBootApplication.class)
                .values()
                .stream()
                .findFirst()
                .map(Object::getClass)
                .map(Class::getPackage)
                .map(Package::getName)
                .orElse(null);
    }

}
