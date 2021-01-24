package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.annotation.TestCase;
import io.github.wesleyone.cases.test.annotation.TestCases;
import io.github.wesleyone.cases.test.annotation.TestParam;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 核型测试启动类
 * Bootstrap for testing cases
 *
 * @AUTHOR: http://wesleyone.github.io/
 */
public abstract class TestBootstrap<T> implements IBizContext<T> {

    /**
     * 前置方法前缀
     * before_用例名称
     */
    private static final String BEFORE_TAG = "before_";
    private static final String SET_TAG = "set";
    private static final String ALL_PARAMS_TAG = "*";

    /**
     * 通用设置参数方式，但是复杂对象请重写Override
     * @param bizContext    业务上下文对象
     * @param propertyClass 属性类型
     * @param tag           属性标识
     * @param value         属性值
     */
    @Override
    public void setProperties(T bizContext, Class<?> propertyClass, String tag, Object value) {
        String paramClazzName = propertyClass.getSimpleName();
        String setterMethodName = SET_TAG + paramClazzName;
        try {
            Method setterMethod = bizContext.getClass().getDeclaredMethod(setterMethodName, propertyClass);
            setterMethod.invoke(bizContext, value);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("没有设置参数方法["+bizContext.getClass().getSimpleName()+"#"+setterMethodName+"]");
        } catch (Exception e) {
            throw new RuntimeException("设置参数异常["+bizContext.getClass().getSimpleName()+"#"+setterMethodName+"]");
        }
    }

    static class CasePO {
        private String caseName;
        private String caseDesc;
        private List<ParamPO> paramList;

        public String getCaseName() {
            return caseName;
        }
        public void setCaseName(String caseName) {
            this.caseName = caseName;
        }

        public String getCaseDesc() {
            return caseDesc;
        }

        public void setCaseDesc(String caseDesc) {
            this.caseDesc = caseDesc;
        }

        public List<ParamPO> getParamList() {
            return paramList;
        }

        public void setParamList(List<ParamPO> paramList) {
            this.paramList = paramList;
        }
    }
    static class ParamPO {
        private Class<?> paramClazz;
        private String tag;
        private Map<String, Object> paramObjectMap = new HashMap<>();

        public Map<String, Object> getParamObjectMap() {
            return paramObjectMap;
        }

        public void setParamObjectMap(Map<String, Object> paramObjectMap) {
            this.paramObjectMap = paramObjectMap;
        }

        public Class<?> getParamClazz() {
            return paramClazz;
        }

        public void setParamClazz(Class<?> paramClazz) {
            this.paramClazz = paramClazz;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
    /**
     * 自动运行测试入口
     * 流程：
     * 1.校验
     * 2.解析
     * 3.构建
     * 4.执行
     */
    public void run() {
        // 检验
        check();
        // 解析
        HashMap<Method, HashMap<String, CasePO>> testMethodMap = parseTestMethods();
        // 构建
        HashMap<Method, List<TestCaseContext<T>>> testCaseContextMap = buildCaseContext(testMethodMap);
        // 执行
        invoke(testCaseContextMap);
    }

    private void check() {
        T t = newInstance();
        if (t == null) {
            throw new RuntimeException("返回业务上下文NULL,请检查["+this.getClass()+"#newInstance]");
        }
    }

    private void invoke(HashMap<Method, List<TestCaseContext<T>>> testCaseContextMap) {
        if (testCaseContextMap == null || testCaseContextMap.isEmpty()) {
            return;
        }
        for (Map.Entry<Method, List<TestCaseContext<T>>> methodTestCaseContext : testCaseContextMap.entrySet()) {
            Method testMethod = methodTestCaseContext.getKey();
            for (TestCaseContext<T> testCaseContext : methodTestCaseContext.getValue()) {
                doBefore(testCaseContext, testMethod);
                doHandle(testCaseContext.getContext());
                doAfter(testCaseContext, testMethod);
            }
        }
    }

    private void doAfter(TestCaseContext<T> testCaseContext, Method testMethod) {
        try {
            testMethod.invoke(this, testCaseContext);
        } catch (Exception e) {
            throw new RuntimeException("调用后置方法失败["+this.getClass().getSimpleName()+"#"+testMethod.getName()+"]");
        }
    }

    private void doHandle(T t) {
        handle(t);
    }

    private void doBefore(TestCaseContext<T> testCaseContext, Method testMethod) {
        String beforeMethodName = BEFORE_TAG + testCaseContext.getCaseName();
        try {
            // 查询场景级别的前置处理方法并执行
            Method beforeMethod = this.getClass().getDeclaredMethod(beforeMethodName, TestCaseContext.class);
            beforeMethod.invoke(this, testCaseContext);
        } catch (NoSuchMethodException ignore) {
            // 如果不存在场景级别的前置处理方法，则尝试聚合场景级别的前置处理方法，即对测试方法前加上前缀
            beforeMethodName = BEFORE_TAG + testMethod.getName();
            try {
                // 查询场景级别的前置处理方法并执行
                Method beforeMethod = this.getClass().getDeclaredMethod(beforeMethodName, TestCaseContext.class);
                beforeMethod.invoke(this, testCaseContext);
            } catch (NoSuchMethodException ignore2) {
            } catch (Exception e) {
                throw new RuntimeException("调用前置方法失败["+this.getClass().getSimpleName()+"#"+beforeMethodName+"]");
            }
        } catch (Exception e) {
            throw new RuntimeException("调用前置方法失败["+this.getClass().getSimpleName()+"#"+beforeMethodName+"]");
        }
    }

    private HashMap<Method, List<TestCaseContext<T>>> buildCaseContext(HashMap<Method, HashMap<String, CasePO>> testMethodMap) {
        if (testMethodMap == null || testMethodMap.isEmpty()) {
            return null;
        }
        HashMap<Method, List<TestCaseContext<T>>> methodTestCaseContextMap = new HashMap<>(16);
        for (Map.Entry<Method, HashMap<String, CasePO>> testMethod : testMethodMap.entrySet()) {
            List<TestCaseContext<T>> testCaseContexts = new ArrayList<>();
            HashMap<String, CasePO> caseMap = testMethod.getValue();
            for (CasePO casePo : caseMap.values()) {
                List<ParamPO> paramList = casePo.getParamList();
                List<T> tList = buildTestCaseContexts(paramList);
                for (T t : tList) {
                    TestCaseContext<T> testCaseContext = new TestCaseContext<>(casePo.getCaseName(),casePo.getCaseDesc(),t);
                    testCaseContexts.add(testCaseContext);
                }
            }
            methodTestCaseContextMap.put(testMethod.getKey(), testCaseContexts);
        }
        return methodTestCaseContextMap;
    }

    /**
     * 递归构建单个场景中的所有测试用例
     */
    private List<T> buildTestCaseContexts(List<ParamPO> paramList) {
        return buildTestCaseContexts(paramList, paramList.size()-1);
    }

    private List<T> buildTestCaseContexts(List<ParamPO> paramList, int index) {
        ParamPO param = paramList.get(index);
        Class<?> paramClazz = param.getParamClazz();
        String tag = param.getTag();
        Map<String, Object> paramObjectMap = param.getParamObjectMap();
        ArrayList<T> contextList = new ArrayList<>();
        for (Map.Entry<String, Object> paramObject : paramObjectMap.entrySet()) {
            Object value = paramObject.getValue();
            if (index <= 0) {
                // 最后一层
                T t = newInstance();
                setProperties(t, paramClazz, tag, value);
                contextList.add(t);
            } else {
                // 不是最后一层
                List<T> tList = buildTestCaseContexts(paramList, index - 1);
                for (T t : tList) {
                    setProperties(t, paramClazz, tag, value);
                }
                contextList.addAll(tList);
            }
        }
        return contextList;
    }

    private HashMap<Method, HashMap<String, CasePO>> parseTestMethods() {
        HashMap<Method, HashMap<String, CasePO>> testMethodMap = new HashMap<>(16);
        // 获取当前类的所有方法（包括public、protect、默认、private），但不包括继承方法
        Method[] methods = this.getClass().getDeclaredMethods();
        // 解析测试方法注解信息
        for (Method method : methods) {
            HashMap<String, CasePO> caseMap = new HashMap<>();
            // 先获取 TestCases#caseParams 及 内置的 TestCase#caseParams
            TestCases testCases = method.getAnnotation(TestCases.class);
            if (testCases == null) {
                continue;
            }
            List<ParamPO> commonParamList = new ArrayList<>();
            // 处理公共参数
            TestParam[] testParams = testCases.params();
            for (TestParam commonParam : testParams) {
                ParamPO testParam = getTestParam(commonParam);
                commonParamList.add(testParam);
            }
            // 处理各个case下参数
            TestCase[] cases = testCases.cases();
            if (cases.length <= 0) {
                continue;
            }
            for (TestCase testCase : cases) {
                List<ParamPO> caseParamList = new ArrayList<>();
                TestParam[] caseParams = testCase.params();
                for (TestParam testParam : caseParams) {
                    ParamPO testParamPo = getTestParam(testParam);
                    caseParamList.add(testParamPo);
                }
                // 添加公共参数
                caseParamList.addAll(commonParamList);
                CasePO casePo = new CasePO();
                casePo.setCaseName(testCase.name());
                casePo.setCaseDesc(testCase.desc());
                casePo.setParamList(caseParamList);
                caseMap.put(testCase.name(), casePo);
            }
            testMethodMap.put(method, caseMap);
        }
        return testMethodMap;
    }

    private ParamPO getTestParam(TestParam testParam) {
        Class<? extends IParam> paramClazz = testParam.clazz();
        IParam paramObject;
        try {
            paramObject = paramClazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("实例化参数对象异常["+paramClazz.getSimpleName()+"]", e);
        }
        Class<?> allowReturnType = paramObject.getParamType();
        List<String> includesParamTestMethods = Arrays.asList(testParam.in());
        boolean allParamMethods = false;
        if (includesParamTestMethods.contains(ALL_PARAMS_TAG)) {
            allParamMethods = true;
        }
        Method[] paramClazzMethods = paramClazz.getDeclaredMethods();
        HashMap<String, Object> methodName2ParamObjectMap = new HashMap<>();
        for (Method paramClazzMethod : paramClazzMethods) {
            // 验证方法返回值
            if (!checkReturnType(paramClazzMethod, allowReturnType)) {
                continue;
            }
            // 无参数
            if (paramClazzMethod.getParameterCount() != 0) {
                continue;
            }
            if (!allParamMethods) {
                // 不是*所有参数方法时，判断是否属于指定的参数方法
                if (!includesParamTestMethods.contains(paramClazzMethod.getName())) {
                    continue;
                }
            }
            try {
                // 方法调用
                Object paramBizObject = paramClazzMethod.invoke(paramObject);
                if (paramBizObject == null) {
                    continue;
                }
                methodName2ParamObjectMap.put(paramClazzMethod.getName(), paramBizObject);
            } catch (Exception e) {
                throw new RuntimeException("执行参数方法异常["+paramClazz.getSimpleName()+"#"+paramClazzMethod.getName()+"]", e);
            }
        }
        // 检验是否有方法不存在
        if (!allParamMethods) {
            List<String> lossMethodNames = new ArrayList<>();
            for (String includesParamTestMethod : includesParamTestMethods) {
                if (!methodName2ParamObjectMap.containsKey(includesParamTestMethod)) {
                    lossMethodNames.add(includesParamTestMethod);
                }
            }
            if (lossMethodNames.size() > 0) {
                throw new RuntimeException("缺失参数方法异常["+paramClazz.getSimpleName()+"#"+lossMethodNames+"]");
            }
        }
        ParamPO paramPo = new ParamPO();
        paramPo.setParamClazz(allowReturnType);
        paramPo.setTag(paramObject.getTag());
        paramPo.setParamObjectMap(methodName2ParamObjectMap);
        return paramPo;
    }

    private boolean checkReturnType(Method paramClazzMethod,Class<?> allowReturnType) {
        if (checkCollection(paramClazzMethod,allowReturnType)) {
            // 集合类型的验证元素类型
            return true;
        } else {
            return paramClazzMethod.getReturnType() == allowReturnType;
        }
    }

    private boolean checkCollection(Method paramClazzMethod,Class<?> allowReturnType) {
        Class<?> returnType = paramClazzMethod.getReturnType();
        String typeName = paramClazzMethod.getGenericReturnType().getTypeName();
        int length;
        if (List.class == returnType) {
            length = List.class.getTypeName().length();
        } else if (Set.class == returnType) {
            length = Set.class.getTypeName().length();
        } else if (Collection.class == returnType) {
            length = Collection.class.getTypeName().length();
        } else {
            return false;
        }
        String realType = typeName.substring(length + 1);
        realType = realType.substring(0, realType.length() - 1);
        return realType.equals(allowReturnType.getTypeName());
    }

}
