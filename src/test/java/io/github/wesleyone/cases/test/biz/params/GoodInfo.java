package io.github.wesleyone.cases.test.biz.params;

/**
 * 模拟定价请求商品信息
 *
 * @author http://wesleyone.github.io/
 */
public class GoodInfo {

    private Long id;

    private Integer number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "GoodInfo{" +
                "id=" + id +
                ", number=" + number +
                '}';
    }
}
