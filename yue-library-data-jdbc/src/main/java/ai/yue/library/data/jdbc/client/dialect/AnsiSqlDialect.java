package ai.yue.library.data.jdbc.client.dialect;

import ai.yue.library.base.util.ArithCompute;
import ai.yue.library.data.jdbc.client.DbBase;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.ipo.Page;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * ANSI SQL
 * 
 * @author	ylyue
 * @since	2020年6月13日
 */
@Getter
@Setter
public abstract class AnsiSqlDialect extends DbBase implements Dialect {
	
	private static final long serialVersionUID = 1841162445914907750L;
	
	protected Wrapper wrapper = new Wrapper();
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	protected JdbcProperties jdbcProperties = new JdbcProperties();

	@Override
	public DialectNameEnum dialectName() {
		return DialectNameEnum.ANSI;
	}

	@Override
	public String getPageJoinSql() {
		// limit A offset B 表示：A就是你需要多少行，B就是查询的起点位置。
		StringBuffer pageJoinSql = new StringBuffer(" ");
		return pageJoinSql.append(Page.LIMIT_KEYWORD).append(" ").append(Page.LIMIT_NAMED_PARAMETER).append(" ")
				.append(Page.PAGE_NAMED_PARAMETER).append(" ").toString();
	}
	
	// ---------------------------------------------------------------------------- ANSI SQL Dialect implements start
	
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
	public JSONObject toParamJson(PageIPO pageIPO) {
		return toPage(pageIPO).toParamJson();
	}

	// ---------------------------------------------------------------------------- ANSI SQL Dialect implements end
	
	// ---------------------------------------------------------------------------- ANSI SQL method start
	
	// ---------------------------------------------------------------------------- ANSI SQL method end
	
}
