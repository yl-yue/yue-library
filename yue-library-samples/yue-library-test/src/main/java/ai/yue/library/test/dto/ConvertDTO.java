package ai.yue.library.test.dto;

import ai.yue.library.test.constant.ConvertEnum;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ylyue
 * @since 2021/3/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertDTO implements Serializable {

    private static final long serialVersionUID = 3987648902475498726L;

    private int inta;
    private Integer intb;
    private long longa;
    private Long longb;
    private boolean booleana;
    private Boolean booleanb;
    private Character character;
    private String str;
    private String strChinese;
    private String strUrl;

    private String[] arrayStr;
    private long[] arrayLong;
    private List<String> list;

    private Date date;
//    private DateTime dateTime;
    private LocalDate localDate;
    private LocalTime localTime;
    private LocalDateTime localDateTime;

    private Map<String, Object> map;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private List<JSONObject> jsonObjectList;

    private Map<String, Object> strToMap;
    private JSONObject strToJsonObject;
    private JSONArray strToJsonArray;
    private List<JSONObject> strToJsonObjectList;

    private ConvertEnum convertEnum;
    private FastJsonHttpMessageConverterDTO fastJsonHttpMessageConverterDTO;

}
