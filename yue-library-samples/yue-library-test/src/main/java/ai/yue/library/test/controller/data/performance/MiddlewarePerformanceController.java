//package ai.yue.library.test.controller.data.performance;
//
//import ai.yue.library.base.view.R;
//import ai.yue.library.base.view.Result;
//import ai.yue.library.data.es.config.sql.EsSqlProperties;
//import ai.yue.library.data.jdbc.client.Db;
//import ai.yue.library.data.redis.client.Redis;
//import ai.yue.library.test.dataobject.performance.UserPerformanceDO;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSONObject;
//import com.amazon.opendistroforelasticsearch.jdbc.ElasticsearchDataSource;
//import com.amazon.opendistroforelasticsearch.jdbc.config.HostnameVerificationConnectionProperty;
//import com.amazon.opendistroforelasticsearch.jdbc.config.PasswordConnectionProperty;
//import com.amazon.opendistroforelasticsearch.jdbc.config.TrustSelfSignedConnectionProperty;
//import com.amazon.opendistroforelasticsearch.jdbc.config.UserConnectionProperty;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.PostConstruct;
//import java.sql.SQLException;
//import java.util.Properties;
//
///**
// * 中间件性能测试
// *
// * @author	ylyue
// * @since	2020年2月21日
// */
//@Profile("test-performance")
//@RestController
//@RequestMapping("/middlewarePerformance")
//public class MiddlewarePerformanceController {
//
//	@Autowired
//	private Db db;
//	@Autowired
//	private EsSqlProperties esSqlProperties;
//	@Autowired
//	@Qualifier("esDb")
//	private Db esDb;
//	private JdbcTemplate esJdbcTemplate;
//	@Autowired
//	private RestHighLevelClient elasticsearchClient;
//	private ElasticsearchRestTemplate elasticsearchRestTemplate;
//	@Autowired
//	private Redis redis;
//
//	private String tableName = "user";
//	private Long id = 24134L;
//	private String key = "c17439cb32234963b4fd3d555089c44f";
//
//	@PostConstruct
//	private void init() throws SQLException {
//		// 初始化esrest
//		elasticsearchRestTemplate = new ElasticsearchRestTemplate(elasticsearchClient);
//
//		// 初始化essql
//		String url = "jdbc:elasticsearch://" + esSqlProperties.getUrl();
//
//		ElasticsearchDataSource ds = new ElasticsearchDataSource();
//		ds.setUrl(url);
//
//		Properties properties = new Properties();
//		properties.setProperty(TrustSelfSignedConnectionProperty.KEY, String.valueOf(esSqlProperties.isTrustSelfSigned()));
//		properties.setProperty(HostnameVerificationConnectionProperty.KEY, String.valueOf(esSqlProperties.isHostnameVerification()));
//		String username = esSqlProperties.getUsername();
//		String password = esSqlProperties.getPassword();
//		if (StrUtil.isAllNotEmpty(username, password)) {
//			properties.setProperty(UserConnectionProperty.KEY, username);
//			properties.setProperty(PasswordConnectionProperty.KEY, password);
//		}
//		ds.setProperties(properties);
//		esJdbcTemplate = new JdbcTemplate(ds);
//	}
//
//	@GetMapping("/java")
//	public Result<?> java() {
//		return R.success();
//	}
//
//	@GetMapping("/mysql")
//	public Result<?> mysql() {
//		JSONObject paramJson = new JSONObject();
//		paramJson.put("id", id);
//		paramJson.put("key", key);
//		return R.success(db.get(tableName, paramJson));
//	}
//
//	@GetMapping("/esSql")
//	public Result<?> esSql() {
//		JSONObject paramJson = new JSONObject();
//		paramJson.put("id", id);
//		paramJson.put("key", key);
//		return R.success(esDb.get(tableName, paramJson));
//	}
//
//	@GetMapping("/esSqlJdbc")
//	public Result<?> esSqlJdbc() {
//		return R.success(esJdbcTemplate.queryForMap("SELECT * FROM user WHERE id = ? AND key = ?", id, key));
//	}
//
//	@GetMapping("/esRest")
//	public Result<?> esRest() {
//		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("id", id)).must(QueryBuilders.matchQuery("key", key));
//		SearchHit<UserPerformanceDO> result = elasticsearchRestTemplate.searchOne(new NativeSearchQuery(boolQueryBuilder), UserPerformanceDO.class);
//		return R.success(result);
//	}
//
//	@GetMapping("/redis")
//	public Result<?> redis() {
//		Object result = redis.hget(tableName, id.toString());
//		return R.success(result);
//	}
//
//	@PostMapping("/setRedisKey")
//	public Result<?> setRedisKey() {
//		JSONObject data = db.getById(tableName, id);
//		redis.hset(tableName, id.toString(), data.toJSONString());
//		return R.success();
//	}
//
//}
