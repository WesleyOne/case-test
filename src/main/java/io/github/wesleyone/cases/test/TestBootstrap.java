package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.annotation.TestCase;
import io.github.wesleyone.cases.test.annotation.TestCases;
import io.github.wesleyone.cases.test.annotation.TestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 核型测试启动类
 * Bootstrap for testing cases
 * @param <T> 使用领域业务的上文对象
 *
 * @author http://wesleyone.github.io/
 */
public abstract class TestBootstrap<T> implements IBizContext<T> {

    /**
     * 前置方法前缀
     * before_用例名称
     */
    private static final String BEFORE_TAG = "before_";
    private static final String ALL_MATCH_TAG = "*";

    /**
     * 自动运行测试
     */
    public void run() {
        // 检验
        check();
        // 解析case注解配置
        HashMap<Method, HashMap<String, CaseObject>> testMethodMap = parseTestMethods();
        // 构建所有case测试用例上下文
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
        boolean isSuccess = true;
        for (Map.Entry<Method, List<TestCaseContext<T>>> methodTestCaseContext : testCaseContextMap.entrySet()) {
            Method testMethod = methodTestCaseContext.getKey();
            for (TestCaseContext<T> testCaseContext : methodTestCaseContext.getValue()) {
                System.out.println(testCaseContext.getContextName());
                try {
                    doBefore(testCaseContext, testMethod);
                    doHandle(testCaseContext.getContext());
                    doAfter(testCaseContext, testMethod);
                } catch (Throwable e) {
                    e.printStackTrace();
                    isSuccess = false;
                }
            }
        }
        if (!isSuccess) {
            throw new RuntimeException("没有全部成功");
        }
    }

    private void doAfter(TestCaseContext<T> testCaseContext, Method testMethod) {
        try {
            testMethod.invoke(this, testCaseContext);
        } catch (Exception e) {
            throw new RuntimeException("调用后置方法失败["+this.getClass().getSimpleName()+"#"+testMethod.getName()+"#"+ testCaseContext.getCaseName()+"#"+testCaseContext.getContextName()+"]", e);
        }
    }

    private void doHandle(T t) {
        handle(t);
    }

    private void doBefore(TestCaseContext<T> testCaseContext, Method testMethod) {
        // 测试场景方法对应的前置处理方法名称
        String beforeMethodName = BEFORE_TAG + testMethod.getName();
        try {
            // 查询场景级别的前置处理方法并执行
            Method beforeMethod = this.getClass().getDeclaredMethod(beforeMethodName, TestCaseContext.class);
            beforeMethod.invoke(this, testCaseContext);
        } catch (NoSuchMethodException ignore2) {
        } catch (Exception e) {
            throw new RuntimeException("调用前置方法失败["+this.getClass().getSimpleName()+"#"+beforeMethodName+"]");
        }
    }

    private HashMap<Method, List<TestCaseContext<T>>> buildCaseContext(HashMap<Method, HashMap<String, CaseObject>> testMethodMap) {
        if (testMethodMap == null || testMethodMap.isEmpty()) {
            return null;
        }
        HashMap<Method, List<TestCaseContext<T>>> methodTestCaseContextMap = new HashMap<>();
        for (Map.Entry<Method, HashMap<String, CaseObject>> testMethod : testMethodMap.entrySet()) {
            List<TestCaseContext<T>> testCaseContexts = new ArrayList<>();
            HashMap<String, CaseObject> casePOMap = testMethod.getValue();
            for (CaseObject caseObject : casePOMap.values()) {
                List<ParamObject> paramObjectList = caseObject.getParamPOList();
                List<TestCaseContext<T>> caseContextList = autoSetParametersAndBuildContext(caseObject, paramObjectList);
                testCaseContexts.addAll(caseContextList);
            }
            methodTestCaseContextMap.put(testMethod.getKey(), testCaseContexts);
        }
        return methodTestCaseContextMap;
    }

    /**
     * 递归自动构建单个场景case中的所有测试用例上下文
     */
    private List<TestCaseContext<T>> autoSetParametersAndBuildContext(CaseObject caseObject, List<ParamObject> paramObjectList) {
        return autoSetParametersAndBuildContext(caseObject, paramObjectList, paramObjectList.size()-1);
    }

    private List<TestCaseContext<T>> autoSetParametersAndBuildContext(CaseObject caseObject, List<ParamObject> paramObjectList, int index) {
        ParamObject paramPO = paramObjectList.get(index);
        Class<?> paramClazz = paramPO.getParamClazz();
        String tag = paramPO.getTag();
        Map<String, Object> paramObjectMap = paramPO.getParamObjectMap();
        List<TestCaseContext<T>> contextList = new ArrayList<>();
        for (Map.Entry<String, Object> paramObject : paramObjectMap.entrySet()) {
            String key = paramObject.getKey();
            Object value = paramObject.getValue();
            if (index <= 0) {
                // 最后一层
                T t = newInstance();
                setProperties(t, paramClazz, tag, value);
                TestCaseContext<T> testCaseContext = new TestCaseContext<>();
                testCaseContext.setCaseName(caseObject.caseName);
                testCaseContext.setCaseDesc(caseObject.caseDesc);
                testCaseContext.setContextName(key);
                testCaseContext.setContext(t);
                contextList.add(testCaseContext);
            } else {
                // 不是最后一层
                List<TestCaseContext<T>> testCaseContexts = autoSetParametersAndBuildContext(caseObject, paramObjectList, index - 1);
                for (TestCaseContext<T> testCaseContext : testCaseContexts) {
                    setProperties(testCaseContext.getContext(), paramClazz, tag, value);
                    testCaseContext.setContextName(testCaseContext.getContextName()+key);
                }
                contextList.addAll(testCaseContexts);
            }
        }
        return contextList;
    }

    private HashMap<Method, HashMap<String, CaseObject>> parseTestMethods() {
        HashMap<Method, HashMap<String, CaseObject>> testMethodMap = new HashMap<>();
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            TestCase[] cases;
            TestParam[] commonParams = null;
            TestCases testCases = method.getAnnotation(TestCases.class);
            if (testCases != null) {
                cases = testCases.cases();
                commonParams = testCases.params();
            } else {
                TestCase singleCase = method.getAnnotation(TestCase.class);
                if (singleCase == null) {
                    continue;
                }
                cases = new TestCase[]{singleCase};
            }
            if (cases.length <= 0) {
                continue;
            }
            HashMap<String, CaseObject> caseMap = new HashMap<>();
            for (TestCase testCase : cases) {
                List<ParamObject> paramList = dealParams(commonParams, testCase.params());
                CaseObject caseObject = new CaseObject();
                caseObject.setCaseName(testCase.name());
                caseObject.setCaseDesc(testCase.desc());
                caseObject.setParamPOList(paramList);
                caseMap.put(testCase.name(), caseObject);
            }
            testMethodMap.put(method, caseMap);
        }
        return testMethodMap;
    }

    /**
     * 聚合公共参数和场景参数，构建参数对象
     * 参数追加模式
     * @param commonParams  公共参数
     * @param caseParams    场景参数
     * @return  参数对象列表
     */
    private List<ParamObject> dealParams(TestParam[] commonParams, TestParam[] caseParams) {
        Map<Class<? extends IParam>, Set<String>> paramClazzMethodNamesMap = new LinkedHashMap<>(16);
        changeParamArray2Map(paramClazzMethodNamesMap, commonParams);
        changeParamArray2Map(paramClazzMethodNamesMap, caseParams);
        ArrayList<ParamObject> paramList = new ArrayList<>();
        for (Map.Entry<Class<? extends IParam>, Set<String>> entry : paramClazzMethodNamesMap.entrySet()) {
            Class<? extends IParam> clazz = entry.getKey();
            Set<String> paramMethodNames = entry.getValue();
            ParamObject paramObject = buildParamObject(clazz, paramMethodNames);
            paramList.add(paramObject);
        }
        return paramList;
    }

    private void changeParamArray2Map(Map<Class<? extends IParam>, Set<String>>  paramClazzMethodNamesMap, TestParam[] params) {
        if (params == null || params.length <= 0) {
            return;
        }
        for (TestParam param : params) {
            changeParam2Map(paramClazzMethodNamesMap, param);
        }
    }

    private void changeParam2Map(Map<Class<? extends IParam>, Set<String>> paramsMap, TestParam testParam) {
        Class<? extends IParam> clazz = testParam.clazz();
        Set<String> includes = paramsMap.getOrDefault(clazz, new LinkedHashSet<>());
        Collections.addAll(includes, testParam.in());
        if (includes.contains(ALL_MATCH_TAG) && includes.size()>1) {
            // 如果包含全匹配，则仅设置全匹配
            includes = Collections.singleton(ALL_MATCH_TAG);
        }
        paramsMap.put(clazz, includes);
    }

    private ParamObject buildParamObject(Class<? extends IParam> paramClazz, Set<String> paramMethodNames) {
        IParam paramObject;
        try {
            paramObject = paramClazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("实例化参数对象异常["+paramClazz.getSimpleName()+"]", e);
        }
        Class<?> allowReturnType = paramObject.getParamType();
        boolean allParamMethods = false;
        if (paramMethodNames.contains(ALL_MATCH_TAG)) {
            allParamMethods = true;
        }
        Method[] paramClazzMethods = paramClazz.getDeclaredMethods();
        HashMap<String, Object> methodName2ParamObjectMap = new LinkedHashMap<>();
        for (Method paramClazzMethod : paramClazzMethods) {
            if (!checkReturnType(paramClazzMethod, allowReturnType)) {
                continue;
            }
            if (paramClazzMethod.getParameterCount() != 0) {
                continue;
            }
            if (!allParamMethods) {
                if (!paramMethodNames.contains(paramClazzMethod.getName())) {
                    continue;
                }
            }
            try {
                Object paramBizObject = paramClazzMethod.invoke(paramObject);
                if (paramBizObject == null) {
                    continue;
                }
                methodName2ParamObjectMap.put(paramClazzMethod.getName(), paramBizObject);
            } catch (Exception e) {
                throw new RuntimeException("执行参数方法异常["+paramClazz.getSimpleName()+"#"+paramClazzMethod.getName()+"]", e);
            }
        }
        ParamObject paramPO = new ParamObject();
        paramPO.setParamClazz(allowReturnType);
        paramPO.setTag(paramObject.getTag());
        paramPO.setParamObjectMap(methodName2ParamObjectMap);
        return paramPO;
    }

    private boolean checkReturnType(Method paramClazzMethod,Class<?> allowReturnType) {
        if (checkCollection(paramClazzMethod,allowReturnType)) {
            return true;
        } else {
            return paramClazzMethod.getReturnType() == allowReturnType;
        }
    }

    private boolean checkCollection(Method paramClazzMethod,Class<?> allowReturnType) {
        Class<?> returnType = paramClazzMethod.getReturnType();
        Type genericReturnType = paramClazzMethod.getGenericReturnType();
        String typeName = genericReturnType.getTypeName();
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
        String substring = typeName.substring(length + 1);
        substring = substring.substring(0, substring.length() - 1);
        return substring.equals(allowReturnType.getTypeName());
    }


    static class CaseObject {
        private String caseName;
        private String caseDesc;
        private List<ParamObject> paramObjectList;

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

        public List<ParamObject> getParamPOList() {
            return paramObjectList;
        }

        public void setParamPOList(List<ParamObject> paramObjectList) {
            this.paramObjectList = paramObjectList;
        }
    }

    static class ParamObject {
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

}
