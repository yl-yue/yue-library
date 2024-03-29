package ai.yue.library.data.mybatis.constant;

/**
 * Db常量定义
 *
 * @author	ylyue
 * @since	2018年7月18日
 */
public class DbConstant {

	// ====================== 类字段名定义 ======================

	/** 系统租户：一级租户（dict_tenant_sys） */
	public static final String CLASS_TENANT_SYS = "tenantSys";
    /** 企业租户：二级租户 */
    public static final String CLASS_TENANT_CO = "tenantCo";
    /** 删除时间：逻辑删除 */
    public static final String CLASS_DELETE_TIME = "deleteTime";

    // ====================== 数据库字段名定义 ======================

    /** 系统租户：一级租户（dict_tenant_sys） */
    public static final String DB_TENANT_SYS = "tenant_sys";
    /** 企业租户：二级租户 */
    public static final String DB_TENANT_CO = "tenant_co";
    /** 删除时间：逻辑删除 */
    public static final String DB_DELETE_TIME = "delete_time";

}
