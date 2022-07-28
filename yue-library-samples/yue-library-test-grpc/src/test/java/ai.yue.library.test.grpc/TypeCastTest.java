package ai.yue.library.test.grpc;

import ai.yue.library.base.util.DateUtils;
import ai.yue.library.base.util.IdUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.test.grpc.dataobject.TableExampleTestDO;
import ai.yue.library.test.proto.GetTableExampleResponse;
import ai.yue.library.test.proto.PhoneType;
import ai.yue.library.test.proto.TypeTest;
import ai.yue.library.web.grpc.util.ProtoUtils;
import com.alibaba.fastjson.JSONValidator;
import com.google.protobuf.Any;
import com.google.protobuf.Duration;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import yue.library.AnyResult;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
public class TypeCastTest {

    /**
     * <pre>
     * 【推荐】使用string类型代替enum类型，以约定的方式将枚举值说明写在注释中，但程序中必须定义枚举来解析此字段，并处理相应逻辑。<br>
     *   说明：因为在proto中定义的枚举类型只能用来做类型与值约束，并不能添加相应的逻辑处理，使得在程序中反倒成为一种负担。<br>
     * 【推荐】使用string类型代替Timestamp类型，并使用yyyy-MM-dd HH:mm:ss格式化时间格式字符串<br>
     * 【推荐】使用string类型代替duration类型，传输时直接使用序列化值，需要时再做相应的反序列化
     * </pre>
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

    private TableExampleTestDO getTableExampleTestDO() {
        TableExampleTestDO tableExampleTestDO = new TableExampleTestDO();
        tableExampleTestDO.setId(520L);
        tableExampleTestDO.setUuid(IdUtils.getSimpleUUID());
        tableExampleTestDO.setSortIdx(0);
        tableExampleTestDO.setCreateUser("ylyue");
        tableExampleTestDO.setCreateUserUuid(IdUtils.getSimpleUUID());
        tableExampleTestDO.setCreateTime(LocalDateTime.now());
        tableExampleTestDO.setUpdateUser("ylyue");
        tableExampleTestDO.setUpdateUserUuid(IdUtils.getSimpleUUID());
        tableExampleTestDO.setUpdateTime(LocalDateTime.now());
        tableExampleTestDO.setDeleteUser("ylyue");
        tableExampleTestDO.setDeleteUserUuid(IdUtils.getSimpleUUID());
        tableExampleTestDO.setDeleteTime(0L);
        tableExampleTestDO.setFieldOne("field_one");
        tableExampleTestDO.setFieldTwo("field_two");
        tableExampleTestDO.setFieldThree("field_three");
        return tableExampleTestDO;
    }

    private TableExampleTestDO getTableExampleTestDO2() {
        TableExampleTestDO tableExampleTestDO = new TableExampleTestDO();
        tableExampleTestDO.setId(521L);
        tableExampleTestDO.setUuid(IdUtils.getSimpleUUID());
        tableExampleTestDO.setSortIdx(1);
        tableExampleTestDO.setCreateUser("ylyue2");
        tableExampleTestDO.setCreateUserUuid(IdUtils.getSimpleUUID());
        tableExampleTestDO.setCreateTime(LocalDateTime.now());
        tableExampleTestDO.setUpdateUser("ylyue2");
        tableExampleTestDO.setUpdateUserUuid(IdUtils.getSimpleUUID());
        tableExampleTestDO.setUpdateTime(LocalDateTime.now());
        tableExampleTestDO.setDeleteUser("ylyue2");
        tableExampleTestDO.setDeleteUserUuid(IdUtils.getSimpleUUID());
        tableExampleTestDO.setDeleteTime(0L);
        tableExampleTestDO.setFieldOne("field_one2");
        tableExampleTestDO.setFieldTwo("field_two2");
        tableExampleTestDO.setFieldThree("field_three2");
        return tableExampleTestDO;
    }

    private GetTableExampleResponse getGetTableExampleResponse() {
        GetTableExampleResponse getTableExampleResponse = GetTableExampleResponse.newBuilder().setId(520).setUuid(IdUtils.getSimpleUUID()).setSortIdx(0)
                .setCreateUser("ylyue").setCreateUserUuid(IdUtils.getSimpleUUID()).setCreateTime(DateUtils.getDatetimeFormatter())
                .setUpdateUser("ylyue").setUpdateUserUuid(IdUtils.getSimpleUUID()).setUpdateTime(DateUtils.getDatetimeFormatter())
                .setDeleteUser("ylyue").setDeleteUserUuid(IdUtils.getSimpleUUID()).setDeleteTime(0)
                .setFieldOne("field_one").setFieldTwo("field_two").setFieldThree("field_three").build();
        return getTableExampleResponse;
    }

    private GetTableExampleResponse getGetTableExampleResponse2() {
        GetTableExampleResponse getTableExampleResponse2 = GetTableExampleResponse.newBuilder().setId(521).setUuid(IdUtils.getSimpleUUID()).setSortIdx(1)
                .setCreateUser("ylyue2").setCreateUserUuid(IdUtils.getSimpleUUID()).setCreateTime(DateUtils.getDatetimeFormatter())
                .setUpdateUser("ylyue2").setUpdateUserUuid(IdUtils.getSimpleUUID()).setUpdateTime(DateUtils.getDatetimeFormatter())
                .setDeleteUser("ylyue2").setDeleteUserUuid(IdUtils.getSimpleUUID()).setDeleteTime(0)
                .setFieldOne("field_one2").setFieldTwo("field_two2").setFieldThree("field_three2").build();
        return getTableExampleResponse2;
    }

    @Test
    public void anyTypeCastTest() {
        // AnyResult
        AnyResult.Builder mapResultBuilder = AnyResult.newBuilder().setCode(200).setCode(200).setMsg("成功").setFlag(true);
        GetTableExampleResponse getTableExampleResponse = getGetTableExampleResponse();
        Any mapResultData = Any.pack(getTableExampleResponse);
        AnyResult mapResult = mapResultBuilder.setData(mapResultData).build();
        ProtoUtils.registerType(GetTableExampleResponse.getDescriptor());
        String mapResultJsonStr = ProtoUtils.toJsonString(mapResult);
        AnyResult.Builder mapResultBuilderTest = AnyResult.newBuilder();
        ProtoUtils.merge(mapResultJsonStr, mapResultBuilderTest);
        AnyResult.Builder mapResultBuilderTest2 = ProtoUtils.toBuilder(mapResultJsonStr, AnyResult.Builder.class);
        Assertions.assertTrue(JSONValidator.from(mapResultJsonStr).validate());
        Assertions.assertTrue(mapResultJsonStr.contains("type.googleapis.com/ai.yue.library.test.proto.GetTableExampleResponse"));
        Assertions.assertTrue(mapResultJsonStr.contains("ylyue"));
        Assertions.assertEquals(200, mapResultBuilderTest.getCode());
        Assertions.assertEquals(200, mapResultBuilderTest2.getCode());
    }

    @Test
    public void toAnyResultTest() throws InvalidProtocolBufferException {
        // Message to AnyResult
        GetTableExampleResponse getTableExampleResponse = getGetTableExampleResponse();
        AnyResult mapResult = ProtoUtils.toAnyResult(R.success(getTableExampleResponse));
        Assertions.assertEquals(200, mapResult.getCode());
        Assertions.assertEquals("ylyue", mapResult.getData().unpack(GetTableExampleResponse.class).getCreateUser());

        // DO to AnyResult
        TableExampleTestDO tableExampleTestDO = getTableExampleTestDO2();
        AnyResult mapResult2 = ProtoUtils.toAnyResult(R.success(tableExampleTestDO), GetTableExampleResponse.Builder.class);
        Assertions.assertEquals(200, mapResult.getCode());
        Assertions.assertEquals("ylyue2", mapResult2.getData().unpack(GetTableExampleResponse.class).getCreateUser());
    }

    @Test
    public void success() throws InvalidProtocolBufferException {
        AnyResult success = ProtoUtils.success();
        AnyResult success1 = ProtoUtils.success(getGetTableExampleResponse());
        AnyResult success2 = ProtoUtils.success(getTableExampleTestDO(), GetTableExampleResponse.Builder.class);
        Assertions.assertEquals(success.getCode(), 200);
        Assertions.assertEquals(success1.getCode(), 200);
        Assertions.assertEquals(success2.getCode(), 200);
        Assertions.assertEquals(success.getFlag(), true);
        Assertions.assertEquals(success1.getFlag(), true);
        Assertions.assertEquals(success2.getFlag(), true);
        Assertions.assertEquals(success1.getData().unpack(GetTableExampleResponse.class).getCreateUser(), "ylyue");
        Assertions.assertEquals(success2.getData().unpack(GetTableExampleResponse.class).getCreateUser(), "ylyue");
    }

}
