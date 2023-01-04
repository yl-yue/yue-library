//package ai.yue.library.data.es.config.sql;
//
//import java.sql.SQLException;
//import java.util.Properties;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.amazon.opendistroforelasticsearch.jdbc.ElasticsearchDataSource;
//import com.amazon.opendistroforelasticsearch.jdbc.config.HostnameVerificationConnectionProperty;
//import com.amazon.opendistroforelasticsearch.jdbc.config.PasswordConnectionProperty;
//import com.amazon.opendistroforelasticsearch.jdbc.config.TrustSelfSignedConnectionProperty;
//import com.amazon.opendistroforelasticsearch.jdbc.config.UserConnectionProperty;
//
//import ai.yue.library.data.jdbc.client.Db;
//import cn.hutool.core.util.StrUtil;
//
///**
// * ES SQL 配置
// *
// * @author	ylyue
// * @since	2020年8月3日
// */
//@Configuration
//@EnableConfigurationProperties(EsSqlProperties.class)
//@ConditionalOnProperty(prefix = "yue.es.sql", name = "enabled", havingValue = "true")
//public class EsSqlConfig {
//
//	@Autowired
//	EsSqlProperties esSqlProperties;
//
//	@Bean
//	public Db esDb() throws SQLException {
//		String url = "jdbc:elasticsearch://" + esSqlProperties.getUrl();
//
//		ElasticsearchDataSource ds = new ElasticsearchDataSource();
//		ds.setUrl(url);
//
//		Properties properties = new Properties();
//        properties.setProperty(TrustSelfSignedConnectionProperty.KEY, String.valueOf(esSqlProperties.isTrustSelfSigned()));
//        properties.setProperty(HostnameVerificationConnectionProperty.KEY, String.valueOf(esSqlProperties.isHostnameVerification()));
//        String username = esSqlProperties.getUsername();
//        String password = esSqlProperties.getPassword();
//        if (StrUtil.isAllNotEmpty(username, password)) {
//        	properties.setProperty(UserConnectionProperty.KEY, username);
//        	properties.setProperty(PasswordConnectionProperty.KEY, password);
//        }
//        ds.setProperties(properties);
//
//        return new Db(ds);
//	}
//
//}
