package ai.yue.library.data.log.util;

import ai.yue.library.data.mybatis.constant.DbCrudEnum;
import cn.hutool.v7.core.text.StrUtil;

/**
 * 日志工具类
 *
 * @author ylyue
 * @since 2025/5/13
 */
public class LogUtils {

    /**
     * 从 HTTP Method 推断操作类型
     * <p>POST→C, GET→R, PUT/PATCH→U, DELETE→D</p>
     *
     * @param httpMethod HTTP请求方法
     * @return 操作类型枚举
     */
    public static DbCrudEnum getOperTypeFromHttpMethod(String httpMethod) {
        if (StrUtil.isBlank(httpMethod)) {
            return DbCrudEnum.R;
        }

        switch (httpMethod.toUpperCase()) {
            case "POST":
                return DbCrudEnum.C;
            case "GET":
                return DbCrudEnum.R;
            case "PUT":
            case "PATCH":
                return DbCrudEnum.U;
            case "DELETE":
                return DbCrudEnum.D;
            default:
                return DbCrudEnum.R;
        }
    }

}