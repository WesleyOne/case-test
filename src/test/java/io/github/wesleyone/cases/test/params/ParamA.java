package io.github.wesleyone.cases.test.params;

/**
 * @author http://wesleyone.github.io/
 */
public class ParamA {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ParamA{" +
                "name='" + name + '\'' +
                '}';
    }
}
