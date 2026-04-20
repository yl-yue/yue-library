package ai.yue.library.data.mybatis.constant;

/**
 * Db常量定义
 *
 * @author	ylyue
 * @since	2018年7月18日
 */
public class DbConstant {

	// ====================== 类字段名定义 ======================

	/** 系统租户：一级租户 ID（dict_tenant_sys） */
	public static final String CLASS_TENANT_SYS_ID = "tenantSysId";
    /** 企业租户：二级租户 ID */
    public static final String CLASS_TENANT_CO_ID = "tenantCoId";
    /** 删除时间：逻辑删除 */
    public static final String CLASS_DELETE_TIME = "deleteTime";
    /** 创建用户 */
    public static final String CLASS_CREATE_USER = "createUser";
    /** 创建用户 ID */
    public static final String CLASS_CREATE_USER_ID = "createUserId";
    /** 创建时间 */
    public static final String CLASS_CREATE_TIME = "createTime";
    /** 更新用户 */
    public static final String CLASS_UPDATE_USER = "updateUser";
    /** 更新用户 ID */
    public static final String CLASS_UPDATE_USER_ID = "updateUserId";
    /** 更新时间 */
    public static final String CLASS_UPDATE_TIME = "updateTime";

    // ====================== 数据库字段名定义 ======================

    /** 系统租户：一级租户 ID（dict_tenant_sys） */
    public static final String DB_TENANT_SYS_ID = "tenant_sys_id";
    /** 企业租户：二级租户 ID */
    public static final String DB_TENANT_CO_ID = "tenant_co_id";
    /** 删除时间：逻辑删除 */
    public static final String DB_DELETE_TIME = "delete_time";
    /** 创建用户 */
    public static final String DB_CREATE_USER = "create_user";
    /** 创建用户 ID */
    public static final String DB_CREATE_USER_ID = "create_user_id";
    /** 创建时间 */
    public static final String DB_CREATE_TIME = "create_time";
    /** 更新用户 */
    public static final String DB_UPDATE_USER = "update_user";
    /** 更新用户 ID */
    public static final String DB_UPDATE_USER_ID = "update_user_id";
    /** 更新时间 */
    public static final String DB_UPDATE_TIME = "update_time";
    
    // ====================== 字段默认值定义 ======================
    
    /** 默认系统租户 ID（一级租户） */
    public static final String DEFAULT_TENANT_SYS_ID = "sys";
    /** 默认企业租户 ID（二级租户） */
    public static final String DEFAULT_TENANT_CO_ID = "co";
    /** 默认用户名称 */
    public static final String DEFAULT_USER_NAME = "default";
    /** 默认用户 ID */
    public static final Long DEFAULT_USER_ID = 0L;
    
}
