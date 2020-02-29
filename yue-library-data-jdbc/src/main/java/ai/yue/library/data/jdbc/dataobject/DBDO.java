package ai.yue.library.data.jdbc.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * DO基类-下划线命名法 {@link BaseSnakeCaseDO}
 * 
 * <p><b><code style="color:red">注意：继承 {@link DBDO} get set ... 采用 &#064;{@link Data} 注解生成时，需加上：</code></b>
 * <blockquote>
 * 	<p>&#064;ToString(callSuper = true)
 * 	<p>&#064;EqualsAndHashCode(callSuper = true)
 * </blockquote>
 * 
 * @deprecated 请使用 {@link BaseSnakeCaseDO}
 * @author	ylyue
 * @since	2018年7月26日
 */
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Deprecated
public abstract class DBDO extends BaseSnakeCaseDO {
	
	private static final long serialVersionUID = 8878972879297611765L;
	
}
