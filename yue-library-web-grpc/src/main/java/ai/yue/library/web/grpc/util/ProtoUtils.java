package ai.yue.library.web.grpc.util;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultEnum;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.protobuf.*;
import com.google.protobuf.util.JsonFormat;
import lombok.SneakyThrows;
import yue.library.AnyResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <b>Protobuf工具类</b>
 * <p>实现Proto、Bean、Json之间的转换</p>
 *
 * @author ylyue
 * @since 2022/5/19
 */
public class ProtoUtils extends Convert {

    /**
     * Protobuf反序列化解析器
     * <p>用于将proto3 json解析为protobuf message</p>
     * <p>支持配置：忽略未知字段、添加多个不同的TypeRegistry用来处理Any类型</p>
     */
    public static JsonFormat.Parser parser = JsonFormat.parser().ignoringUnknownFields();
    public static JsonFormat.Printer printer = JsonFormat.printer();
    private static List<Descriptors.Descriptor> messageTypes = new ArrayList<>();

    // ============= 注册Any转换类型 =============

    /**
     * 注册 {@linkplain Any} 类型转换时需要的Protobuf类型描述
     *
     * @param descriptors Protobuf类型描述
     */
    public static void registerType(Descriptors.Descriptor... descriptors) {
        for (Descriptors.Descriptor descriptor : descriptors) {
            messageTypes.add(descriptor);
        }

        TypeRegistry typeRegistry = TypeRegistry.newBuilder().add(messageTypes).build();
        parser = parser.usingTypeRegistry(typeRegistry);
        printer = printer.usingTypeRegistry(typeRegistry);
    }

    /**
     * 注册 {@linkplain Any} 类型转换时需要的Protobuf类型描述
     *
     * @param registerClasss protobuf message class
     */
    public static <T extends GeneratedMessageV3> void registerType(Class<T>... registerClasss) {
        List<Descriptors.Descriptor> descriptors = new ArrayList<>();
        for (Class<T> registryClass : registerClasss) {
            Descriptors.Descriptor descriptor = ReflectUtil.invokeStatic(ReflectUtil.getPublicMethod(registryClass, "getDescriptor"));
            descriptors.add(descriptor);
        }

        registerType(ArrayUtil.toArray(descriptors, Descriptors.Descriptor.class));
    }

    // ============= AnyResult success builder =============

    /**
     * 成功后调用，返回的data为null
     *
     * @return Protobuf响应序列化最外层对象
     */
    public static AnyResult success() {
        int code = ResultEnum.SUCCESS.getCode();
        String msg = ResultEnum.SUCCESS.getMsg();
        return AnyResult.newBuilder().setCode(code).setMsg(msg).setFlag(true).build();
    }

    /**
     * 成功后调用，返回的data为一个Protobuf Message对象
     *
     * @param data 数据
     * @return Protobuf响应序列化最外层对象
     */
    public static AnyResult success(Message data) {
        int code = ResultEnum.SUCCESS.getCode();
        String msg = ResultEnum.SUCCESS.getMsg();
        return AnyResult.newBuilder().setCode(code).setMsg(msg).setFlag(true).setData(Any.pack(data)).build();
    }

    /**
     * 成功后调用，返回的data为一个Protobuf Message对象
     *
     * @param data         要转化为Protobuf Message的数据对象
     * @param builderClass Protobuf message builder class
     * @param <T>          Message.Builder
     * @return Protobuf响应序列化最外层对象
     */
    public static <T extends Message.Builder> AnyResult success(Object data, Class<T> builderClass) {
        int code = ResultEnum.SUCCESS.getCode();
        String msg = ResultEnum.SUCCESS.getMsg();
        Message message = toBuilder(data, builderClass).build();
        return AnyResult.newBuilder().setCode(code).setMsg(msg).setFlag(true).setData(Any.pack(message)).build();
    }

    // ============= 转换 =============

    public static JSONObject toJSONObject(MessageOrBuilder message) {
        return JSONObject.parseObject(toJsonString(message));
    }

    public static PageIPO toPageIPO(MessageOrBuilder message) {
        return PageIPO.parsePageIPO(toJSONObject(message));
    }

    /**
     * Protobuf响应体约定-将Restful Result转换为Protobuf AnyResult
     *
     * @param result 将Restful Result
     * @return Protobuf AnyResult
     */
    public static AnyResult toAnyResult(Result<Message> result) {
        Integer code = result.getCode();
        String msg = result.getMsg();
        boolean flag = result.isFlag();
        Message data = result.getData();
        return AnyResult.newBuilder().setCode(code).setMsg(msg).setFlag(flag).setData(Any.pack(data)).build();
    }

    /**
     * Protobuf响应体约定-将Restful Result转换为Protobuf AnyResult
     *
     * @param result       将Restful Result
     * @param builderClass Protobuf message builder class
     * @return Protobuf AnyResult
     */
    public static <T extends Message.Builder> AnyResult toAnyResult(Result result, Class<T> builderClass) {
        Integer code = result.getCode();
        String msg = result.getMsg();
        boolean flag = result.isFlag();
        Object data = result.getData();
        Message message = toBuilder(data, builderClass).build();
        return AnyResult.newBuilder().setCode(code).setMsg(msg).setFlag(flag).setData(Any.pack(message)).build();
    }

    // ============= 序列化 =============

    /**
     * 转换为Json字符串
     *
     * @param message protobuf message
     * @return Json字符串
     */
    @SneakyThrows
    @SuppressWarnings("all")
    public static String toJsonString(MessageOrBuilder message) {
        return printer.print(message);
    }

    /**
     * 转换为Json字符串
     * <p>输出null值为默认value</p>
     *
     * @param message protobuf message
     * @return Json字符串
     */
    @SneakyThrows
    @SuppressWarnings("all")
    public static String toJsonStringIncludingDefaultValue(MessageOrBuilder message) {
        return printer.includingDefaultValueFields().print(message);
    }

    // ============= 反序列化 =============

    private static void mergeObject(Object jsonOrJavaBean, Message.Builder builder) {
        merge(JSONObject.toJSONString(jsonOrJavaBean, SerializerFeature.WriteDateUseDateFormat), builder);
    }

    /**
     * json字符串对象合并到protobuf message
     *
     * @param json    json字符串
     * @param builder protobuf message
     */
    public static void merge(String json, Message.Builder builder) {
        try {
            parser.merge(json, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * json对象合并到protobuf message
     *
     * @param json    json对象
     * @param builder protobuf message
     */
    public static void merge(Map json, Message.Builder builder) {
        mergeObject(json, builder);
    }

    /**
     * JavaBean合并到protobuf message
     *
     * @param javaBean JavaBean
     * @param builder  protobuf message
     */
    public static void merge(Object javaBean, Message.Builder builder) {
        mergeObject(javaBean, builder);
    }

    /**
     * json字符串对象转换为protobuf message
     *
     * @param json         json字符串
     * @param builderClass protobuf message class
     * @return protobuf message builder对象
     */
    @SneakyThrows
    @SuppressWarnings("all")
    public static <T extends Message.Builder> T toBuilder(String json, Class<T> builderClass) {
        Message.Builder builder = ReflectUtil.newInstance(builderClass);
        merge(json, builder);
        return (T) builder;
    }

    /**
     * json对象转换为protobuf message
     *
     * @param json         json对象
     * @param builderClass protobuf message class
     * @return protobuf message builder对象
     */
    @SneakyThrows
    @SuppressWarnings("all")
    public static <T extends Message.Builder> T toBuilder(Map json, Class<T> builderClass) {
        Message.Builder builder = ReflectUtil.newInstance(builderClass);
        mergeObject(json, builder);
        return (T) builder;
    }

    /**
     * JavaBean转换为protobuf message
     *
     * @param javaBean     JavaBean
     * @param builderClass protobuf message class
     * @return protobuf message builder对象
     */
    @SneakyThrows
    @SuppressWarnings("all")
    public static <T extends Message.Builder> T toBuilder(Object javaBean, Class<T> builderClass) {
        Message.Builder builder = ReflectUtil.newInstance(builderClass);
        mergeObject(javaBean, builder);
        return (T) builder;
    }

}
