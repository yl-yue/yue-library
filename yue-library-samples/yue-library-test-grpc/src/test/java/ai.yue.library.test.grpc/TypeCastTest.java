package ai.yue.library.test.grpc;

import ai.yue.library.base.util.DateUtils;
import ai.yue.library.base.util.IdUtils;
import ai.yue.library.test.proto.*;
import ai.yue.library.web.grpc.util.ProtoUtils;
import com.alibaba.fastjson.JSONValidator;
import com.google.protobuf.Any;
import com.google.protobuf.Duration;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class TypeCastTest {

    /**
     * 【推荐】使用string类型代替enum类型，以约定的方式将枚举值说明写在注释中，但程序中必须定义枚举来解析此字段，并处理相应逻辑。<br>
     *   说明：因为在proto中定义的枚举类型只能用来做类型与值约束，并不能添加相应的逻辑处理，使得在程序中反倒成为一种负担。<br>
     * 【推荐】使用string类型代替Timestamp类型，并使用yyyy-MM-dd HH:mm:ss格式化时间格式字符串<br>
     * 【推荐】使用string类型代替duration类型，传输时直接使用序列化值，需要时再做相应的反序列化
     */
    private void typeTest() {
        TypeTest.Builder builder = TypeTest.newBuilder();
        TypeTest typeTest = builder.build();
        // java enum类
        PhoneType phoneType = typeTest.getType();
        // java enum类
        TypeTest.PhoneType2 phoneType2 = typeTest.getType2();
        // java map类
        Map<String, Any> mapMap = typeTest.getMapMap();
        // google自定义类
        Timestamp timestamp = typeTest.getTimestamp();
        // google自定义类
        Duration duration = typeTest.getDuration();
    }

    @Test
    public void anyTypeCastTest() {
        // MapResult
        MapResult.Builder mapResultBuilder = MapResult.newBuilder().setCode(200).setCode(200).setMsg("成功").setFlag(true);
        GetTableExampleResponse getTableExampleResponse = GetTableExampleResponse.newBuilder().setId(520).setUuid(IdUtils.getSimpleUUID()).setSortIdx(0)
                .setCreateUser("ylyue").setCreateUserUuid(IdUtils.getSimpleUUID()).setCreateTime(DateUtils.getDatetimeFormatter())
                .setUpdateUser("ylyue").setUpdateUserUuid(IdUtils.getSimpleUUID()).setUpdateTime(DateUtils.getDatetimeFormatter())
                .setDeleteUser("ylyue").setDeleteUserUuid(IdUtils.getSimpleUUID()).setDeleteTime(0)
                .setFieldOne("field_one").setFieldTwo("field_two").setFieldThree("field_three").build();
        Any mapResultData = Any.pack(getTableExampleResponse);
        MapResult mapResult = mapResultBuilder.setData(mapResultData).build();
        ProtoUtils.registerType(GetTableExampleResponse.getDescriptor());
        String mapResultJsonStr = ProtoUtils.toJsonString(mapResult);
        MapResult.Builder mapResultBuilderTest = MapResult.newBuilder();
        ProtoUtils.merge(mapResultJsonStr, mapResultBuilderTest);
        MapResult.Builder mapResultBuilderTest2 = ProtoUtils.toBuilder(mapResultJsonStr, MapResult.Builder.class);
        Assertions.assertTrue(JSONValidator.from(mapResultJsonStr).validate());
        Assertions.assertTrue(mapResultJsonStr.contains("type.googleapis.com/ai.yue.library.test.proto.GetTableExampleResponse"));
        Assertions.assertTrue(mapResultJsonStr.contains("ylyue"));
        Assertions.assertEquals(200, mapResultBuilderTest.getCode());
        Assertions.assertEquals(200, mapResultBuilderTest2.getCode());

        // ListResult
        GetTableExampleResponse getTableExampleResponse2 = GetTableExampleResponse.newBuilder().setId(521).setUuid(IdUtils.getSimpleUUID()).setSortIdx(1)
                .setCreateUser("ylyue2").setCreateUserUuid(IdUtils.getSimpleUUID()).setCreateTime(DateUtils.getDatetimeFormatter())
                .setUpdateUser("ylyue2").setUpdateUserUuid(IdUtils.getSimpleUUID()).setUpdateTime(DateUtils.getDatetimeFormatter())
                .setDeleteUser("ylyue2").setDeleteUserUuid(IdUtils.getSimpleUUID()).setDeleteTime(0)
                .setFieldOne("field_one2").setFieldTwo("field_two2").setFieldThree("field_three2").build();
        Any mapResultData2 = Any.pack(getTableExampleResponse2);
        List<Any> data = new ArrayList<>();
        data.add(mapResultData);
        data.add(mapResultData2);
        ListResult listResult = ListResult.newBuilder().setCode(200).setMsg("成功").setFlag(true).setCount(2).addAllData(data).build();
        String listResultJsonStr = ProtoUtils.toJsonString(listResult);
        Assertions.assertTrue(JSONValidator.from(listResultJsonStr).validate());
        Assertions.assertTrue(listResultJsonStr.contains("type.googleapis.com/ai.yue.library.test.proto.GetTableExampleResponse"));
        Assertions.assertTrue(listResultJsonStr.contains("ylyue"));
    }

}
