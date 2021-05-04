package ai.yue.library.test.controller.data.performance;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.test.dataobject.performance.UserPerformanceDO;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 中间件性能测试
 *
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/middlewarePerformance")
public class MiddlewarePerformanceController {

	@Autowired
	private Db db;
	@Autowired
	@Qualifier("esDb")
	private Db esDb;
	@Autowired
	private RestHighLevelClient elasticsearchClient;
	private ElasticsearchRestTemplate elasticsearchRestTemplate;
	@Autowired
	private Redis redis;

	private String tableName = "user";
	private Long id = 24134L;
	private String key = "c17439cb32234963b4fd3d555089c44f";

	@PostConstruct
	private void init() {
		elasticsearchRestTemplate = new ElasticsearchRestTemplate(elasticsearchClient);
	}

	@GetMapping("/mysql")
	public Result<?> mysql() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);
		paramJson.put("key", key);
		return R.success(db.get(tableName, paramJson));
	}

	@GetMapping("/esSql")
	public Result<?> esSql() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);
		paramJson.put("key", key);
		return R.success(esDb.get(tableName, paramJson));
	}

	@GetMapping("/esRest")
	public Result<?> esRest() {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("id", id)).must(QueryBuilders.matchQuery("key", key));
		SearchHit<UserPerformanceDO> result = elasticsearchRestTemplate.searchOne(new NativeSearchQuery(boolQueryBuilder), UserPerformanceDO.class);
//		UserPerformanceDO result = elasticsearchRestTemplate.get("c17439cb32234963b4fd3d555089c44f", UserPerformanceDO.class);
		return R.success(result);
	}

//	@GetMapping("/redis")
//	public Result<?> redis() {
//		Object result = redis.hget(tableName, id.toString());
//		return R.success(result);
//	}

//	@PostMapping("/redisKey")
//	public Result<?> redisKey() {
//		List<JSONObject> listAll = db.listAll(tableName);
//		redis.getRedisTemplate().opsForList().get
//		Long result = redis.getRedisTemplate().opsForList().leftPushAll(tableName, listAll);
//		return R.success(result);
//	}

}
