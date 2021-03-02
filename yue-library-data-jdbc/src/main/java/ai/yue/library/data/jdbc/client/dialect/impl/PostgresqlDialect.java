package ai.yue.library.data.jdbc.client.dialect.impl;

import ai.yue.library.base.util.ObjectUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.jdbc.client.dialect.Dialect;
import ai.yue.library.data.jdbc.client.dialect.DialectNameEnum;
import ai.yue.library.data.jdbc.client.dialect.Wrapper;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import ai.yue.library.data.jdbc.ipo.Page;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Set;

/**
 * PostgreSQL方言
 *
 * @author	ylyue
 * @since	2020年6月13日
 */
@Slf4j
public class PostgresqlDialect extends AnsiDialect {
	
	private static final long serialVersionUID = 3889210427543389642L;
	
	public PostgresqlDialect(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcProperties jdbcProperties) {
		super(namedParameterJdbcTemplate, jdbcProperties);
		super.wrapper = new Wrapper('"');
		super.dialect = this;
	}

	@Override
	public Dialect cloneDialect() {
		return clone();
	}

	@Override
	public PostgresqlDialect clone() {
		log.info("执行{}，深度克隆。", getClass());
		DataSource dataSource = ObjectUtils.cloneIfPossible(getNamedParameterJdbcTemplate().getJdbcTemplate().getDataSource());
		return new PostgresqlDialect(new NamedParameterJdbcTemplate(dataSource), jdbcProperties.clone());
	}

	@Override
	public DialectNameEnum dialectName() {
		return DialectNameEnum.POSTGRESQL;
	}

	@Override
	public String getPageJoinSql() {
		// limit A offset B 表示：A就是你需要多少行，B就是查询的起点位置。
		return new StringBuffer()
				.append(" ")
				.append(Page.KEYWORD_LIMIT)
				.append(" ")
				.append(Page.NAMED_PARAMETER_LIMIT)
				.append(" ")
				.append(Page.KEYWORD_OFFSET)
				.append(" ")
				.append(Page.NAMED_PARAMETER_PAGE)
				.append(" ").toString();
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

}
