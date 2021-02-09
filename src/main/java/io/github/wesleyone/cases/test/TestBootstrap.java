package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.annotation.TestCase;
import io.github.wesleyone.cases.test.annotation.TestParam;
import io.github.wesleyone.cases.test.util.GenericsUtil;

import java.lang.reflect.Method;
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
        T t = this.newBizContext();
        if (t == null) {
            throw new RuntimeException("返回业务上下文NULL,请检查["+this.getClass()+"#newBizContext]实现");
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
                try {
                    doBefore(testCaseContext, testMethod);
                    doHandle(testCaseContext);
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
            throw new RuntimeException("调用后置方法失败["+this.getClass().getSimpleName()+"#"+testMethod.getName()+"#"+ testCaseContext.getCaseName()+"#"+testCaseContext.getParamNames()+"]", e);
        }
    }

    private void doHandle(TestCaseContext<T> testCaseContext) {
        handle(testCaseContext.getContext());
    }

    private void doBefore(TestCaseContext<T> testCaseContext, Method testMethod) {
        String beforeMethodName = BEFORE_TAG + testMethod.getName();
        try {
            Method beforeMethod = this.getClass().getDeclaredMethod(beforeMethodName, TestCaseContext.class);
            beforeMethod.invoke(this, testCaseContext);
        } catch (NoSuchMethodException ignore2) {
        } catch (Exception e) {
            throw new RuntimeException("调用前置方法失败["+this.getClass().getSimpleName()+"#"+beforeMethodName+"]", e);
        }
    }

    private HashMap<Method, List<TestCaseContext<T>>> buildCaseContext(HashMap<Method, HashMap<String, CaseObject>> testMethodMap) {
        if (testMethodMap == null || testMethodMap.isEmpty()) {
            return null;
        }
        HashMap<Method, List<TestCaseContext<T>>> methodTestCaseContextMap = new HashMap<>(16);
        for (Map.Entry<Method, HashMap<String, CaseObject>> testMethod : testMethodMap.entrySet()) {
            List<TestCaseContext<T>> testCaseContexts = new ArrayList<>();
            HashMap<String, CaseObject> caseObjectMap = testMethod.getValue();
            for (CaseObject caseObject : caseObjectMap.values()) {
                List<ParamObject> paramObjectList = caseObject.getParamObjectList();
                List<TestCaseContext<T>> caseContextList = autoSetParametersAndBuildContext(caseObject, paramObjectList);
                testCaseContexts.addAll(caseContextList);
            }
            methodTestCaseContextMap.put(testMethod.getKey(), testCaseContexts);
        }
        return methodTestCaseContextMap;
    }

    /**
     * 递归自动构建单个场景case中的所有参数对象
     */
    private List<TestCaseContext<T>> autoSetParametersAndBuildContext(CaseObject caseObject, List<ParamObject> paramObjectList) {
        return autoSetParametersAndBuildContext(caseObject, paramObjectList, paramObjectList.size()-1);
    }

    private List<TestCaseContext<T>> autoSetParametersAndBuildContext(CaseObject caseObject, List<ParamObject> paramObjectList, int index) {
        ParamObject paramObject = paramObjectList.get(index);
        String tag = paramObject.getTag();
        Map<String, Object> paramObjectMap = paramObject.getParamObjectMap();
        List<TestCaseContext<T>> contextList = new ArrayList<>();
        for (Map.Entry<String, Object> paramRealObject : paramObjectMap.entrySet()) {
            String key = paramRealObject.getKey();
            Object value = paramRealObject.getValue();
            if (index <= 0) {
                // final layer
                T t = this.newBizContext();
                setProperties(t, tag, value);
                TestCaseContext<T> testCaseContext = new TestCaseContext<>();
                testCaseContext.setCaseName(caseObject.getCaseName());
                testCaseContext.setCaseDesc(caseObject.getCaseDesc());
                List<String> paramNames = new ArrayList<>();
                paramNames.add(key);
                testCaseContext.setParamNames(paramNames);
                testCaseContext.setContext(t);
                contextList.add(testCaseContext);
            } else {
                List<TestCaseContext<T>> testCaseContexts = autoSetParametersAndBuildContext(caseObject, paramObjectList, index - 1);
                for (TestCaseContext<T> testCaseContext : testCaseContexts) {
                    setProperties(testCaseContext.getContext(), tag, value);
                    testCaseContext.getParamNames().add(key);
                }
                contextList.addAll(testCaseContexts);
            }
        }
        return contextList;
    }

    private HashMap<Method, HashMap<String, CaseObject>> parseTestMethods() {
        HashMap<Method, HashMap<String, CaseObject>> testMethodMap = new HashMap<>(16);
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            TestCase testCase = method.getAnnotation(TestCase.class);
            if (testCase == null) {
                continue;
            }
            HashMap<String, CaseObject> caseMap = new HashMap<>(16);
            List<ParamObject> paramList = dealParams( testCase.params());
            CaseObject caseObject = new CaseObject();
            caseObject.setCaseName(testCase.name());
            caseObject.setCaseDesc(testCase.desc());
            caseObject.setParamObjectList(paramList);
            caseMap.put(testCase.name(), caseObject);
            testMethodMap.put(method, caseMap);
        }
        return testMethodMap;
    }

    /**
     * 聚合公共参数和场景参数，构建参数对象
     * 参数追加模式
     * @param caseParams    场景参数
     * @return  参数对象列表
     */
    private List<ParamObject> dealParams(TestParam[] caseParams) {
        Map<Class<? extends IParam<?>>, Set<String>> paramClazzMethodNamesMap = changeParamArray2Map(caseParams);
        ArrayList<ParamObject> paramList = new ArrayList<>();
        for (Map.Entry<Class<? extends IParam<?>>, Set<String>> entry : paramClazzMethodNamesMap.entrySet()) {
            Class<? extends IParam<?>> clazz = entry.getKey();
            Set<String> paramMethodNames = entry.getValue();
            ParamObject paramObject = buildParamObject(clazz, paramMethodNames);
            paramList.add(paramObject);
        }
        return paramList;
    }

    private Map<Class<? extends IParam<?>>, Set<String>> changeParamArray2Map(TestParam[] params) {
        Map<Class<? extends IParam<?>>, Set<String>> paramClazzMethodNamesMap = new LinkedHashMap<>(16);
        if (params == null || params.length <= 0) {
            return paramClazzMethodNamesMap;
        }
        for (TestParam param : params) {
            changeParam2Map(paramClazzMethodNamesMap, param);
        }
        return paramClazzMethodNamesMap;
    }

    private void changeParam2Map(Map<Class<? extends IParam<?>>, Set<String>> paramsMap, TestParam testParam) {
        Class<? extends IParam<?>> clazz = testParam.clazz();
        Set<String> includes = paramsMap.getOrDefault(clazz, new LinkedHashSet<>());
        Collections.addAll(includes, testParam.in());
        if (includes.contains(ALL_MATCH_TAG) && includes.size()>1) {
            // 如果包含全匹配，则仅设置全匹配
            includes = Collections.singleton(ALL_MATCH_TAG);
        }
        paramsMap.put(clazz, includes);
    }

    private ParamObject buildParamObject(Class<? extends IParam<?>> paramClazz, Set<String> paramMethodNames) {
        IParam<?> paramInstance;
        try {
            paramInstance = paramClazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("实例化参数对象异常["+paramClazz.getSimpleName()+"]", e);
        }
        String allowReturnTypeName = GenericsUtil.getInterfaceGenericsTypeName(paramInstance, IParam.class, 0);
        boolean allParamMethods = false;
        if (paramMethodNames.contains(ALL_MATCH_TAG)) {
            allParamMethods = true;
        }
        Method[] paramClazzMethods = paramClazz.getDeclaredMethods();
        HashMap<String, Object> methodName2ParamObjectMap = new LinkedHashMap<>(16);
        for (Method paramClazzMethod : paramClazzMethods) {
            if (!checkReturnType(paramClazzMethod, allowReturnTypeName)) {
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
                Object paramBizObject = paramClazzMethod.invoke(paramInstance);
                if (paramBizObject == null) {
                    continue;
                }
                methodName2ParamObjectMap.put(paramClazzMethod.getName(), paramBizObject);
            } catch (Exception e) {
                throw new RuntimeException("执行参数方法异常["+paramClazz.getSimpleName()+"#"+paramClazzMethod.getName()+"]", e);
            }
        }
        ParamObject paramObject = new ParamObject();
        paramObject.setTag(paramInstance.getTag());
        paramObject.setParamObjectMap(methodName2ParamObjectMap);
        return paramObject;
    }

    private boolean checkReturnType(Method paramClazzMethod,String allowReturnTypeName) {
        return Objects.equals(paramClazzMethod.getGenericReturnType().getTypeName(), allowReturnTypeName);
    }

    private static class CaseObject {
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

        public List<ParamObject> getParamObjectList() {
            return paramObjectList;
        }

        public void setParamObjectList(List<ParamObject> paramObjectList) {
            this.paramObjectList = paramObjectList;
        }
    }

    private static class ParamObject {
        private String tag;
        private Map<String, Object> paramObjectMap = new HashMap<>(16);

        public Map<String, Object> getParamObjectMap() {
            return paramObjectMap;
        }

        public void setParamObjectMap(Map<String, Object> paramObjectMap) {
            this.paramObjectMap = paramObjectMap;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

}
