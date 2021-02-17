package ai.yue.library.data.jdbc.client.dialect.impl;

import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.ObjectUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.jdbc.client.dialect.AnsiSqlDialect;
import ai.yue.library.data.jdbc.client.dialect.Dialect;
import ai.yue.library.data.jdbc.client.dialect.DialectNameEnum;
import ai.yue.library.data.jdbc.client.dialect.Wrapper;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import ai.yue.library.data.jdbc.dto.PageDTO;
import ai.yue.library.data.jdbc.ipo.Page;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageBeforeAndAfterVO;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * MySQL方言
 * 
 * @author	ylyue
 * @since	2020年6月13日
 */
@Slf4j
public class MysqlDialect extends AnsiSqlDialect {
	
	private static final long serialVersionUID = -3734718212043823636L;
	
	public MysqlDialect(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcProperties jdbcProperties) {
		super.wrapper = new Wrapper('`');
		super.dialect = this;
		super.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		super.jdbcProperties = jdbcProperties;
	}

	@Override
	public Dialect cloneDialect() {
		return clone();
	}

	@Override
	public MysqlDialect clone() {
		log.info("执行{}，深度克隆。", getClass());
		DataSource dataSource = ObjectUtils.cloneIfPossible(getNamedParameterJdbcTemplate().getJdbcTemplate().getDataSource());
		return new MysqlDialect(new NamedParameterJdbcTemplate(dataSource), jdbcProperties.clone());
	}

	@Override
	public DialectNameEnum dialectName() {
		return DialectNameEnum.MYSQL;
	}

	@Override
	public String getPageJoinSql() {
		StringBuffer pageJoinSql = new StringBuffer(" ");
		return pageJoinSql.append(Page.LIMIT_KEYWORD).append(" ").append(Page.PAGE_NAMED_PARAMETER).append(" , ")
				.append(Page.LIMIT_NAMED_PARAMETER).append(" ").toString();
	}

	// insert
	
	@Override
	public Long insertOrUpdate(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dBUpdateEnum) {
        paramValidate(tableName, paramJson, conditions);
		paramFormat(paramJson);
        tableName = wrapper.wrap(tableName);
        paramJson = wrapper.wrap(paramJson);
        conditions = wrapper.wrap(conditions);
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        Set<String> keys = paramJson.keySet();
        Iterator<String> it = keys.iterator();
        Iterator<String> iterator = keys.iterator();
        
        while (it.hasNext()) {
			String key = it.next();
			sql.append(key);
			if(it.hasNext()) {
				sql.append(", ");
			}
		}
        sql.append(") VALUES (");
        
        while (iterator.hasNext()) {
        	String key = iterator.next();
    		sql.append(":");
    		sql.append(key);
    		if(iterator.hasNext()) {
    			sql.append(", ");
    		}
		}
        sql.append(") ON DUPLICATE KEY UPDATE ");
        
		for (String condition : conditions) {
			sql.append(condition);
			sql.append(" = ");
			if (dBUpdateEnum == DbUpdateEnum.NORMAL) {// 正常更新
				sql.append(":" + condition);
			} else {
				sql.append(condition);
				if (dBUpdateEnum == DbUpdateEnum.INCREMENT) {// 递增更新
					sql.append(" + :");
				} else {// 递减更新
					sql.append(" - :");
				}
				sql.append(condition);
			}
			sql.append(", ");
		}
		sql = new StringBuffer(StringUtils.deleteLastEqualString(sql, ", "));

    	return (long) namedParameterJdbcTemplate.update(sql.toString(), paramJson);
	}
	
	// Page

	@Override
	public PageDTO pageDTOBuild(String tableName, PageIPO pageIPO, SortEnum sortEnum) {
		// 1. 参数验证
		paramValidate(tableName);
		
		// 2. 处理分页参数
		JSONObject paramJson = toParamJson(pageIPO);
		JSONObject conditions = pageIPO.getConditions();
		
		// 3. 预编译SQL拼接
		StringBuffer querySql = new StringBuffer();
		querySql.append("SELECT a.* FROM ");
		querySql.append(tableName + " a, ");
		querySql.append("(SELECT id FROM ");
		querySql.append(tableName);
		// 添加查询条件
		String whereSql = "";
		if (conditions != null) {
			whereSql = paramToWhereSql(conditions);
		}
		querySql.append(whereSql);
		// 排序
		if (sortEnum == null) {// 默认（不排序）
			querySql.append(getPageJoinSql()).append(") b WHERE a.id = b.id");
		} else {
			if (SortEnum.ASC == sortEnum) {// 升序
				querySql.append(" ORDER BY ").append(DbConstant.PRIMARY_KEY).append(getPageJoinSql()).append(") b WHERE a.id = b.id");
			} else {// 降序
				querySql.append(" ORDER BY ").append(DbConstant.PRIMARY_KEY).append(" DESC ").append(getPageJoinSql()).append(") b WHERE a.id = b.id");
			}
		}
		
		// 4. 统计总数
		StringBuffer countSql = new StringBuffer();
		countSql.append("SELECT COUNT(*) count FROM ");
		countSql.append(tableName);
		countSql.append(whereSql);
		Long count = (Long) namedParameterJdbcTemplate.queryForMap(countSql.toString(), paramJson).get("count");
		
		// 5. 返回结果
		return PageDTO.builder().count(count).querySql(querySql.toString()).paramJson(paramJson).build();
	}

	@Override
	public PageDTO pageDTOBuild(String tableName, String whereSql, PageIPO pageIPO) {
		// 1. 参数验证
		paramValidate(tableName, whereSql);
		
		// 2. 处理分页参数
		JSONObject paramJson = toParamJson(pageIPO);
		
		// 3. 预编译SQL拼接
		StringBuffer querySql = new StringBuffer();
		querySql.append("SELECT a.* FROM ");
		querySql.append(tableName + " a, ");
		querySql.append(" (select id from ");
		querySql.append(tableName);
		querySql.append(" ");
		querySql.append(whereSql);
		querySql.append(getPageJoinSql()).append(") b WHERE a.id = b.id");
		
		// 4. 统计总数
		StringBuffer countSql = new StringBuffer();
		countSql.append("SELECT COUNT(*) count FROM ");
		countSql.append(tableName);
		countSql.append(" ");
		countSql.append(whereSql);
		Long count = (Long) namedParameterJdbcTemplate.queryForMap(countSql.toString(), paramJson).get("count");
		
		// 5. 返回结果
		return PageDTO.builder().count(count).querySql(querySql.toString()).paramJson(paramJson).build();
	}

	@Override
	public PageDTO pageDTOBuild(String querySql, PageIPO pageIPO) {
		// 1. 参数校验
		if (StringUtils.isEmpty(querySql)) {
			throw new DbException("querySql不能为空");
		}
		
		// 2. 处理分页参数
		JSONObject paramJson = toParamJson(pageIPO);
		JSONObject conditions = pageIPO.getConditions();
		
		// 3. 统计
		int fromIndex = querySql.toUpperCase().indexOf("FROM");
		String countStr = DbConstant.PAGE_COUNT_SQL_PREFIX + querySql.substring(fromIndex);
		int limitIndex = countStr.toUpperCase().indexOf("LIMIT");
		if (-1 == limitIndex) {
			throw new DbException("querySql不能没有LIMIT");
		}
		int EndIndex = countStr.indexOf(")", limitIndex);
		if (-1 == EndIndex) {
			System.err.println("错误的querySql：\n");
			System.err.println(querySql);
			throw new DbException("querySql应当是一个优化后的语句，其中LIMIT必须放在子查询内，详细请参照示例语句编写。");
		}
		StringBuffer countSql = new StringBuffer(countStr);
		countSql = countSql.delete(limitIndex, EndIndex);
		Long count = (Long) namedParameterJdbcTemplate.queryForMap(countSql.toString(), conditions).get("count");
		
		// 4. 返回结果
		return PageDTO.builder().count(count).querySql(querySql.toString()).paramJson(paramJson).build();
	}

	@Override
	public PageBeforeAndAfterVO pageBeforeAndAfter(String querySql, PageIPO pageIPO, Long equalsId) {
		// 1. 参数校验
		if (StringUtils.isEmpty(querySql)) {
			throw new DbException("querySql不能为空");
		}
		
		// 2. 查询数据
		JSONArray array = new JSONArray();
		array.addAll(namedParameterJdbcTemplate.queryForList(querySql, toParamJson(pageIPO)));
		int size = array.size();
		
		// 3. 获得前后值
		Long beforeId = null;
		Long afterId = null;
		String key = DbConstant.PRIMARY_KEY;
		for (int i = 0; i < size; i++) {
			JSONObject json = array.getJSONObject(i);
			// 比较列表中相等的值，然后获取前一条与后一条数据
			if (equalsId.equals(json.getLong(key))) {
				if (i != 0) {// 不是列表中第一条数据
					beforeId = array.getJSONObject(i - 1).getLong(key);
				}
				if (i != size - 1) {// 不是列表中最后一条数据
					afterId = array.getJSONObject(i + 1).getLong(key);
				}
				break;
			}
		}
		
		// 4. 返回结果
		return PageBeforeAndAfterVO.builder()
		.beforeId(beforeId)
		.afterId(afterId)
		.build();
	}

	// Update

	@Override
	public String updateSqlBuild(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dBUpdateEnum) {
		paramValidate(tableName, paramJson, conditions);
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append(wrapper.wrap(tableName));
        sql.append(" SET ");
        
        Set<String> keys = paramJson.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			// 排除更新条件
			if (!ArrayUtil.contains(conditions, key)) {
				sql.append(wrapper.wrap(key));
				sql.append(" = ");
				if (dBUpdateEnum == DbUpdateEnum.INCREMENT) {// 递增更新
					sql.append(wrapper.wrap(key));
					sql.append(" + :");
				} else if (dBUpdateEnum == DbUpdateEnum.DECR // 递减更新
						|| dBUpdateEnum == DbUpdateEnum.DECR_UNSIGNED) {// 递减-无符号更新
					sql.append(wrapper.wrap(key));
					sql.append(" - :");
				} else {// 正常更新
					sql.append(":");
				}
				sql.append(key);
				sql.append(", ");
			}
		}
		sql = new StringBuffer(StringUtils.deleteLastEqualString(sql, ", "));
		String whereSql = paramToWhereSql(paramJson, conditions);
		sql.append(whereSql);
        
		if (dBUpdateEnum == DbUpdateEnum.DECR_UNSIGNED) {// 递减-无符号更新
			List<String> updateKeys = MapUtils.keyList(paramJson);
			for (String key : updateKeys) {
				// 排除更新条件
				if (!ArrayUtil.contains(conditions, key)) {
					sql.append(" AND ");
					sql.append(wrapper.wrap(key));
					sql.append(" >= :");
					sql.append(key);
				}
			}
		}
        
        return sql.toString();
	}
	
}
