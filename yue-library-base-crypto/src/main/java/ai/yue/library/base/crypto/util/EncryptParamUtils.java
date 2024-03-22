package ai.yue.library.base.crypto.util;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;

/**
 * 加密参数处理
 *
 * @author ylyue
 * @since 2021/4/14
 */
public class EncryptParamUtils {

    /**
     * 将加密对象进行可识别的序列化并转换为字节数据
     *
     * @param data 要加密的数据
     * @return 字节数据
     */
    public static byte[] toEncryptByte(Object data) {
        if (ObjectUtil.isNull(data)) {
            return null;
        }

        if (data instanceof String) {
            return ((String) data).getBytes();
        } else if (data instanceof Enum) {
            return ((Enum<?>) data).name().getBytes();

        }

        return JSON.toJSONBytes(data);
    }

}
