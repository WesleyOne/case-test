package io.github.wesleyone.cases.test.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型工具类
 *
 * @author http://wesleyone.github.io/
 */
public class GenericsUtil {

    /**
     * 获取接口上的泛型T
     * 如果T是List<E>,则返回java.lang.List<E>
     *
     * @param instance        接口实现类
     * @param interfaceClazz  接口类型
     * @param index           泛型索引位置
     */
    public static String getInterfaceGenericsTypeName(Object instance, Class<?> interfaceClazz,int index) {
        Type[] interfaces = instance.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = null;
        for (Type inf: interfaces) {
            if (inf.getTypeName().startsWith(interfaceClazz.getTypeName())) {
                parameterizedType = (ParameterizedType) inf;
            }
        }
        if (parameterizedType == null) {
            return null;
        }
        Type type = parameterizedType.getActualTypeArguments()[index];
        return type.getTypeName();
    }
}
