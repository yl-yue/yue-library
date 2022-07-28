package ai.yue.library.data.jdbc.client.dialect;

import com.alibaba.druid.DbType;

/**
 * 方言名
 * <p>定义yue-library支持的所有数据库方言
 * 
 * @author	ylyue
 * @since	2020年6月13日
 */
public enum DialectNameEnum {

	/**
	 * SQL92、SQL99
	 */
	ANSI{
		@Override
		public DbType getDbType() {
			return DbType.other;
		}
	},

	/**
	 * MySQL
	 */
	MYSQL {
		@Override
		public DbType getDbType() {
			return DbType.mysql;
		}
	},

	/**
	 * PostgreSQL
	 */
	POSTGRESQL {
		@Override
		public DbType getDbType() {
			return DbType.postgresql;
		}
	},

	/**
	 * 达梦
	 */
	DM {
		@Override
		public DbType getDbType() {
			return DbType.dm;
		}
	};

	public abstract DbType getDbType();

}
