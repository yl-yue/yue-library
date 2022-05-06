package ai.yue.library.test.spring.jdbc.ipo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JDBC参数美化IPO
 *
 * @author ylyue
 * @since 2021/1/13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamFormatIPO {

    Long id;
    Character character;
    LocalDateTime localDateTime;
    JSONObject jsonObject;
    List<JSONObject> jsonObjectList;
    JSONArray jsonArray;

}
