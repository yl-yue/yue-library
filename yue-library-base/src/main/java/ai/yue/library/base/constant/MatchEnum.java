package ai.yue.library.base.constant;

import cn.hutool.core.util.StrUtil;

import java.util.List;

/**
 * 匹配方式
 *
 * @author ylyue
 * @since 2022/4/28
 */
public enum MatchEnum {

    /**
     * 匹配
     */
    MATCH{
        @Override
        public boolean getExecResult(String value, List<String> matchList) {
            if (matchList == null) {
                return false;
            }

            for (String match : matchList) {
                if (StrUtil.equalsIgnoreCase(value, match)) {
                    return true;
                }
            }

            return false;
        }
    },

    /**
     * 排除
     */
    EXCLUDE {
        @Override
        public boolean getExecResult(String value, List<String> matchList) {
            if (matchList == null) {
                return true;
            }

            for (String match : matchList) {
                if (StrUtil.equalsIgnoreCase(value, match)) {
                    return false;
                }
            }

            return true;
        }
    };

    /**
     * 获得执行结果
     *
     * @param value     需要匹配的值
     * @param matchList 被匹配的列表
     * @return 对应匹配方式执行的结果
     */
    public abstract boolean getExecResult(String value, List<String> matchList);

}
