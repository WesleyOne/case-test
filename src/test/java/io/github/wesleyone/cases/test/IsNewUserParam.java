package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.biz.params.PriceRequest;

/**
 * isNewUser对应的属性值对象
 *
 * @see PriceRequest#setNewUser(boolean)
 * @author http://wesleyone.github.io/
 */
public class IsNewUserParam implements IParam<Boolean> {

    /**
     * 新用户
     * @return true
     */
    public Boolean newUser() {
        return true;
    }

    /**
     * 老用户
     * @return false
     */
    public Boolean oldUser() {
        return false;
    }
}
