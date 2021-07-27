package ai.yue.library.data.jdbc.client.dialect.impl;

import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.ArithCompute;
import ai.yue.library.base.util.ObjectUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.jdbc.client.DbBase;
import ai.yue.library.data.jdbc.client.dialect.Dialect;
import ai.yue.library.data.jdbc.client.dialect.DialectNameEnum;
import ai.yue.library.data.jdbc.client.dialect.Wrapper;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import ai.yue.library.data.jdbc.ipo.Page;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * ANSI SQL
 * 
 * @author	ylyue
 * @since	2020年6月13日
 */
@Slf4j
@Getter
@Setter
public class AnsiDialect extends DbBase implements Dialect {
	
	private static final long serialVersionUID = 1841162445914907750L;

	protected Wrapper wrapper = new Wrapper();
	protected JdbcProperties jdbcProperties;
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public AnsiDialect(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcProperties jdbcProperties) {
		super.dialect = this;
		this.jdbcProperties = jdbcProperties;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	// ---------------------------------------------------------------------------- ANSI SQL Dialect implements start

	@Override
	public Dialect cloneDialect() {
		return clone();
	}

	@Override
	public AnsiDialect clone() {
		log.info("执行{}，深度克隆。", getClass());
		DataSource dataSource = ObjectUtils.cloneIfPossible(getNamedParameterJdbcTemplate().getJdbcTemplate().getDataSource());
		return new MysqlDialect(new NamedParameterJdbcTemplate(dataSource), jdbcProperties.clone());
	}

	@Override
	public DialectNameEnum dialectName() {
		return DialectNameEnum.ANSI;
	}

	/**
	 * 获得用于SQL字符串拼接的SQL分页字符串
	 * <p>默认实现MySQL标准，如：</p>
	 * <ul>
	 *     <li>LIMIT 0,10（从第一条数据开始，查询10条数据）</li>
	 * </ul>
	 *
	 * @return 用于SQL字符串拼接的SQL分页字符串（带具名参数的SQL字符串，非SQL拼接）
	 */
	@Override
	public String getPageJoinSql() {
		return new StringBuffer()
				.append(" ")
				.append(Page.KEYWORD_LIMIT)
				.append(" ")
				.append(Page.NAMED_PARAMETER_PAGE)
				.append(" , ")
				.append(Page.NAMED_PARAMETER_LIMIT)
				.append(" ").toString();
	}

	// insert

	@Override
	public Long insertOrUpdate(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dBUpdateEnum) {
		paramValidate(tableName, paramJson, conditions);
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);
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
	public Page toPage(PageIPO pageIPO) {
		// 1. 处理分页参数
		long page = pageIPO.getPage();
		int limit = pageIPO.getLimit();
		JSONObject conditions = pageIPO.getConditions();
		page--;
		if (page >= 1) {
			page = (long) ArithCompute.mul(page, limit);
		}

		// 2. 返回结果
		return Page.builder()
				.page(page)
				.limit(limit)
				.conditions(conditions)
				.build();
	}

	@Override
	public <T> PageVO<T> page(String tableName, PageIPO pageIPO, SortEnum sortEnum, Class<T> mappedClass) {
		// 1. 参数验证
		paramValidate(tableName);

		// 2. 处理分页参数
		JSONObject conditions = pageIPO.getConditions();
		tableName = wrapper.wrap(tableName);

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
		countSql.append("SELECT count(*) count FROM ");
		countSql.append(tableName);
		countSql.append(whereSql);

		// 5. 分页查询
		return pageSql(countSql.toString(), querySql.toString(), pageIPO, mappedClass);
	}

	@Override
	public <T> PageVO<T> pageWhere(String tableName, String whereSql, PageIPO pageIPO, Class<T> mappedClass) {
		// 1. 参数验证
		paramValidate(tableName, whereSql);
		tableName = wrapper.wrap(tableName);
		if (jdbcProperties.isEnableDeleteQueryFilter() && !StrUtil.containsIgnoreCase(whereSql, DbConstant.FIELD_DEFINITION_DELETE_TIME)) {
			whereSql = getDeleteWhereSql() + StrUtil.replaceIgnoreCase(whereSql, "WHERE", "AND");
		}

		// 2. 预编译SQL拼接
		StringBuffer querySql = new StringBuffer();
		querySql.append("SELECT a.* FROM ");
		querySql.append(tableName + " a, ");
		querySql.append(" (SELECT id FROM ");
		querySql.append(tableName);
		querySql.append(" ");
		querySql.append(whereSql);
		querySql.append(getPageJoinSql()).append(") b WHERE a.id = b.id");

		// 3. 统计总数
		StringBuffer countSql = new StringBuffer();
		countSql.append("SELECT count(*) count FROM ");
		countSql.append(tableName);
		countSql.append(" ");
		countSql.append(whereSql);

		// 4. 分页查询
		return pageSql(countSql.toString(), querySql.toString(), pageIPO, mappedClass);
	}

	@Override
	public <T> PageVO<T> pageSql(String querySql, PageIPO pageIPO, Class<T> mappedClass) {
		// 1. 参数校验
		if (StringUtils.isEmpty(querySql)) {
			throw new DbException("querySql不能为空");
		}

		// 2. 统计SQL
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

		// 3. 分页查询
		return pageSql(countSql.toString(), querySql, pageIPO, mappedClass);
	}

	@Override
	public <T> PageVO<T> pageSql(@Nullable String countSql, String querySql, PageIPO pageIPO, Class<T> mappedClass) {
		// 1. 参数校验
		if (StringUtils.isEmpty(querySql)) {
			throw new DbException("querySql不能为空");
		}

		// 2. 处理分页参数
		JSONObject paramJson = toPage(pageIPO).toParamJson();
		JSONObject conditions = pageIPO.getConditions();

		// 3. 统计
		Long count = null;
		if (StringUtils.isNotEmpty(countSql)) {
			count = queryForObject(countSql, conditions, Long.class);
		}

		// 4. 查询数据
		List<T> data = new ArrayList<>();
		if (count == null || count != 0) {
			data = queryForList(querySql, paramJson, mappedClass);
		}

		// 5. 返回结果
		PageVO<T> pageVO = new PageVO<>();
		return pageVO.toBuilder().count(count).data(data).build();
	}

	// ---------------------------------------------------------------------------- ANSI SQL Dialect implements end

}
