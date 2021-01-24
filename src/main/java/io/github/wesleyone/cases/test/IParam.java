package io.github.wesleyone.cases.test;

/**
 * 参数对象用接口
 * Interface is used to discover parameters
 *
 * @AUTHOR: http://wesleyone.github.io/
 */
public interface IParam {

    /**
     * 指定参数类型
     * specify type of parameter
     *
     * @return 参数类型
     */
    Class<?> getParamType();

    /**
     * 根据参数标记，可用于自定义设置参数
     * get unique tag of parameter
     *
     * @return 参数标记
     */
    default String getTag() {
        return this.getClass().getTypeName();
    }
}
