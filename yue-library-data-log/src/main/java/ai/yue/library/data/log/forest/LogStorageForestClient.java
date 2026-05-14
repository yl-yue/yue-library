package ai.yue.library.data.log.forest;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;

@BaseRequest(baseURL = "${yue.data.log.http.base-url}")
public interface LogStorageForestClient {

	@Post("/lan/v1/log/loginLog/insert")
	void storeLoginLog(@JSONBody LoginLogEntity entity);

	@Post("/lan/v1/log/operLog/insert")
	void storeOperLog(@JSONBody OperLogEntity entity);
}
