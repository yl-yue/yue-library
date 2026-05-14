package ai.yue.library.data.mybatis.config;

import ai.yue.library.base.config.BaseAutoConfig;
import ai.yue.library.data.mybatis.interceptor.LogicDeleteInterceptor;
import ai.yue.library.data.mybatis.interceptor.TenantCoIdInterceptor;
import ai.yue.library.data.mybatis.mapper.SqlMapper;
import ai.yue.library.data.mybatis.provider.AuditDataFill;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Configuration
@AutoConfigureAfter({BaseAutoConfig.class})
@EnableConfigurationProperties(MybatisProperties.class)
@Import({AuditDataFill.class, LogicDeleteInterceptor.class, TenantCoIdInterceptor.class})
public class MybatisAutoConfig {
    
    private static final String MAPPER_PACKAGES_RESOURCE_LOCATION = "META-INF/yue/mapper-packages";
    
    /**
     * 扫描当前项目包下的 Mapper 接口
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(ApplicationContext applicationContext, MybatisProperties properties) {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();

        // 获取 SqlMapper 的包名
        String sqlMapperPackageName = SqlMapper.class.getPackage().getName();

        // 获取运行项目 mapper 的包名，规范：启动类的包名 + ".mapper"
        String startupMapperPackageName = getStartupPackageName(applicationContext) + ".mapper";

        // 设置 mapper 扫描包
        Set<String> packages = new LinkedHashSet<>();
        packages.add(sqlMapperPackageName);
        packages.add(startupMapperPackageName);
        packages.addAll(properties.getExtraMapperPackages());
        discoverModuleMapperPackages(packages, applicationContext.getClassLoader());

        configurer.setBasePackage(String.join(",", packages));
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

    private void discoverModuleMapperPackages(Set<String> packages, ClassLoader classLoader) {
        try {
            Enumeration<URL> resources = classLoader.getResources(MAPPER_PACKAGES_RESOURCE_LOCATION);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (!line.isEmpty() && !line.startsWith("#")) {
                            packages.add(line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.warn("扫描模块 Mapper 包配置文件异常：{}", e.getMessage());
        }
    }

}
