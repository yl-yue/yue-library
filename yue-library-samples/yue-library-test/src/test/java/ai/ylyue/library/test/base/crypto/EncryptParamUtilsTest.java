package ai.yue.library.test.base.crypto;

import ai.yue.library.base.crypto.util.EncryptParamUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * EncryptParamUtils 单元测试
 * <p>加密字节必须显式使用 UTF-8：不能依赖 JVM 平台默认字符集（Windows 中文版为 GBK），
 * 否则同一段中文在服务端与客户端会得到不同字面量，密文互通失败。
 * <p>判别性说明：在 UTF-8 默认平台（Linux/macOS）上该断言对旧实现也成立，
 * 需以 {@code -Dfile.encoding=GBK} 启动测试 JVM 才能复现旧实现的失败（等价于 Windows 现场）。
 *
 * @author ylyue
 */
class EncryptParamUtilsTest {

	@Test
	void toEncryptByte_usesUtf8() {
		String data = "中文测试";
		assertArrayEquals(data.getBytes(StandardCharsets.UTF_8), EncryptParamUtils.toEncryptByte(data));
	}

}
