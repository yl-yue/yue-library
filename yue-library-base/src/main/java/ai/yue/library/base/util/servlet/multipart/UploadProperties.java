package ai.yue.library.base.util.servlet.multipart;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 上传文件设定属性<br>
 * 源自 hutool-extra
 * 
 * @author	ylyue
 * @since	2019年8月14日
 */
@Data
@ConfigurationProperties("yue.servlet.upload")
public class UploadProperties {

	/**
	 * 最大文件大小
	 * <p>
	 * 默认：-1（无限制）
	 */
	protected Integer maxFileSize = -1;
	
	/**
	 * 文件保存到内存的边界，如果文件大小小于这个边界，将保存于内存中，否则保存至临时目录中
	 * <p>
	 * 默认：8192（8GB）
	 */
	protected Integer memoryThreshold = 8192;
	
	/**
	 * 临时文件目录，上传文件的临时目录，若为空，使用系统目录
	 */
	protected String tmpUploadPath;
	
	/**
	 * 文件扩展名限定，禁止列表还是允许列表取决于{@link #isAllowFileExts}
	 */
	protected List<String> fileExts;
	
	/**
	 * 扩展名是允许列表还是禁止列表，若true表示只允许列表里的扩展名，否则是禁止列表里的扩展名
	 */
	protected Boolean isAllowFileExts = true;
	
}
