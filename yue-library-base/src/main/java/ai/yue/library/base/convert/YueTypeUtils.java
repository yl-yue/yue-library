package ai.yue.library.base.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 临时修复FastJson JavaBean转换BUG
 *
 * @author ylyue
 * @since 2021/3/24
 */
public class YueTypeUtils extends TypeUtils {

    public static <T> T castToJavaBean(Map<String,Object> map, Class<T> clazz, ParserConfig config){
        try{
            if(clazz == StackTraceElement.class){
                String declaringClass = (String) map.get("className");
                String methodName = (String) map.get("methodName");
                String fileName = (String) map.get("fileName");
                int lineNumber;
                {
                    Number value = (Number) map.get("lineNumber");
                    if(value == null) {
                        lineNumber = 0;
                    } else if (value instanceof BigDecimal) {
                        lineNumber = ((BigDecimal) value).intValueExact();
                    } else{
                        lineNumber = value.intValue();
                    }
                }
                return (T) new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
            }

            {
                Object iClassObject = map.get(JSON.DEFAULT_TYPE_KEY);
                if(iClassObject instanceof String){
                    String className = (String) iClassObject;
                    Class<?> loadClazz;
                    if(config == null){
                        config = ParserConfig.global;
                    }
                    loadClazz = config.checkAutoType(className, null);
                    if(loadClazz == null){
                        throw new ClassNotFoundException(className + " not found");
                    }
                    if(!loadClazz.equals(clazz)){
                        return (T) castToJavaBean(map, loadClazz, config);
                    }
                }
            }

            if(clazz.isInterface()){
                JSONObject object;
                if(map instanceof JSONObject){
                    object = (JSONObject) map;
                } else{
                    object = new JSONObject(map);
                }
                if(config == null){
                    config = ParserConfig.getGlobalInstance();
                }
                ObjectDeserializer deserializer = config.get(clazz);
                if(deserializer != null){
                    String json = JSON.toJSONString(object);
                    return JSON.parseObject(json, clazz);
                }
                return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class<?>[]{clazz}, object);
            }

            if(clazz == Locale.class){
                Object arg0 = map.get("language");
                Object arg1 = map.get("country");
                if(arg0 instanceof String){
                    String language = (String) arg0;
                    if(arg1 instanceof String){
                        String country = (String) arg1;
                        return (T) new Locale(language, country);
                    } else if(arg1 == null){
                        return (T) new Locale(language);
                    }
                }
            }

            if (clazz == String.class && map instanceof JSONObject) {
                return (T) map.toString();
            }

            if (clazz == JSON.class && map instanceof JSONObject) {
                return (T) map;
            }

            if (clazz == LinkedHashMap.class && map instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) map;
                Map innerMap = jsonObject.getInnerMap();
                if (innerMap instanceof LinkedHashMap) {
                    return (T) innerMap;
                }
            }

            if (clazz.isInstance(map)) {
                return (T) map;
            }

            if (clazz == JSONObject.class) {
                return (T) new JSONObject(map);
            }

            if (config == null) {
                config = ParserConfig.getGlobalInstance();
            }

//            YueJavaBeanDeserializer javaBeanDeser = null;
            YueJavaBeanDeserializer javaBeanDeser = new YueJavaBeanDeserializer(config, clazz);
//            ObjectDeserializer deserializer = config.getDeserializer(clazz);
//            ObjectDeserializer deserializer = createYueJavaBeanDeserializer(clazz, clazz, config);
//            if (deserializer instanceof YueJavaBeanDeserializer) {
//                javaBeanDeser = (YueJavaBeanDeserializer) deserializer;
//                config.putDeserializer(clazz, deserializer);
                config.putDeserializer(clazz, javaBeanDeser);
//            }

            if(javaBeanDeser == null){
                throw new JSONException("can not get javaBeanDeserializer. " + clazz.getName());
            }

            return (T) javaBeanDeser.createInstance(map, config);
        } catch(Exception e){
            throw new JSONException(e.getMessage(), e);
        }
    }

//    public static ObjectDeserializer createYueJavaBeanDeserializer(Class<?> clazz, Type type, ParserConfig config) {
//        boolean asmEnable = (boolean) ReflectUtil.getFieldValue(config, "asmEnable") & !config.fieldBased;
//        if (asmEnable) {
//            JSONType jsonType = TypeUtils.getAnnotation(clazz,JSONType.class);
//
//            if (jsonType != null) {
//                Class<?> deserializerClass = jsonType.deserializer();
//                if (deserializerClass != Void.class) {
//                    try {
//                        Object deseralizer = deserializerClass.newInstance();
//                        if (deseralizer instanceof ObjectDeserializer) {
//                            return (ObjectDeserializer) deseralizer;
//                        }
//                    } catch (Throwable e) {
//                        // skip
//                    }
//                }
//
//                asmEnable = jsonType.asm()
//                        && jsonType.parseFeatures().length == 0;
//            }
//
//            if (asmEnable) {
//                Class<?> superClass = JavaBeanInfo.getBuilderClass(clazz, jsonType);
//                if (superClass == null) {
//                    superClass = clazz;
//                }
//
//                for (;;) {
//                    if (!Modifier.isPublic(superClass.getModifiers())) {
//                        asmEnable = false;
//                        break;
//                    }
//
//                    superClass = superClass.getSuperclass();
//                    if (superClass == Object.class || superClass == null) {
//                        break;
//                    }
//                }
//            }
//        }
//
//        if (clazz.getTypeParameters().length != 0) {
//            asmEnable = false;
//        }
//
//        ASMDeserializerFactory asmFactory = (ASMDeserializerFactory) ReflectUtil.getFieldValue(config, "asmFactory");
//        if (asmEnable && asmFactory != null && asmFactory.classLoader.isExternalClass(clazz)) {
//            asmEnable = false;
//        }
//
//        if (asmEnable) {
//            asmEnable = ASMUtils.checkName(clazz.getSimpleName());
//        }
//
//        if (asmEnable) {
//            if (clazz.isInterface()) {
//                asmEnable = false;
//            }
//            boolean jacksonCompatible = (boolean) ReflectUtil.getFieldValue(config, "jacksonCompatible");
//            JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz
//                    , type
//                    , config.propertyNamingStrategy
//                    ,false
//                    , TypeUtils.compatibleWithJavaBean
//                    , jacksonCompatible
//            );
//
//            if (asmEnable && beanInfo.fields.length > 200) {
//                asmEnable = false;
//            }
//
//            Constructor<?> defaultConstructor = beanInfo.defaultConstructor;
//            if (asmEnable && defaultConstructor == null && !clazz.isInterface()) {
//                asmEnable = false;
//            }
//
//            for (FieldInfo fieldInfo : beanInfo.fields) {
//                if (fieldInfo.getOnly) {
//                    asmEnable = false;
//                    break;
//                }
//
//                Class<?> fieldClass = fieldInfo.fieldClass;
//                if (!Modifier.isPublic(fieldClass.getModifiers())) {
//                    asmEnable = false;
//                    break;
//                }
//
//                if (fieldClass.isMemberClass() && !Modifier.isStatic(fieldClass.getModifiers())) {
//                    asmEnable = false;
//                    break;
//                }
//
//                if (fieldInfo.getMember() != null //
//                        && !ASMUtils.checkName(fieldInfo.getMember().getName())) {
//                    asmEnable = false;
//                    break;
//                }
//
//                JSONField annotation = fieldInfo.getAnnotation();
//                if (annotation != null //
//                        && ((!ASMUtils.checkName(annotation.name())) //
//                        || annotation.format().length() != 0 //
//                        || annotation.deserializeUsing() != Void.class //
//                        || annotation.parseFeatures().length != 0 //
//                        || annotation.unwrapped())
//                        || (fieldInfo.method != null && fieldInfo.method.getParameterTypes().length > 1)) {
//                    asmEnable = false;
//                    break;
//                }
//
//                if (fieldClass.isEnum()) { // EnumDeserializer
//                    ObjectDeserializer fieldDeser = config.getDeserializer(fieldClass);
//                    if (!(fieldDeser instanceof EnumDeserializer)) {
//                        asmEnable = false;
//                        break;
//                    }
//                }
//            }
//        }
//
//        if (asmEnable) {
//            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
//                asmEnable = false;
//            }
//        }
//
//        if (asmEnable) {
//            if (TypeUtils.isXmlField(clazz)) {
//                asmEnable = false;
//            }
//        }
//
//        if (!asmEnable) {
//            return new YueJavaBeanDeserializer(config, clazz, type);
//        }
//
//        JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, type, config.propertyNamingStrategy);
//        try {
//            return asmFactory.createJavaBeanDeserializer(config, beanInfo);
//            // } catch (VerifyError e) {
//            // e.printStackTrace();
//            // return new YueJavaBeanDeserializer(config, clazz, type);
//        } catch (NoSuchMethodException ex) {
//            return new YueJavaBeanDeserializer(config, clazz, type);
//        } catch (JSONException asmError) {
//            return new YueJavaBeanDeserializer(config, beanInfo);
//        } catch (Exception e) {
//            throw new JSONException("create asm deserializer error, " + clazz.getName(), e);
//        }
//    }

}
