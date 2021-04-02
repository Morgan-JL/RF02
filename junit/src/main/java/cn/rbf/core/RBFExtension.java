package cn.rbf.core;

import cn.rbf.AutoScanApplicationContext;
import cn.rbf.container.Injection;
import cn.rbf.reflect.ClassUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.jupiter.api.extension.TestInstantiationException;

/**
 * <p>
 *   junit5 - 会自动扫描XXExtension.class下aop的实现
 * </p>
 * @author Morgan
 * @date 2021/4/1 14:58
 */
public class RBFExtension implements BeforeAllCallback, AfterAllCallback,
    TestInstancePostProcessor, BeforeEachCallback, AfterEachCallback, BeforeTestExecutionCallback,
    AfterTestExecutionCallback, ParameterResolver, TestInstanceFactory {

  public void afterAll(ExtensionContext extensionContext) throws Exception {

  }

  public void afterEach(ExtensionContext extensionContext) throws Exception {

  }

  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {

  }

  public void beforeAll(ExtensionContext extensionContext) throws Exception {
    Class<?> testClass = extensionContext.getTestClass().get();
    AutoScanApplicationContext.create(testClass);
  }

  public void beforeEach(ExtensionContext extensionContext) throws Exception {

  }

  public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {

  }

  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return false;
  }

  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return null;
  }


  public void postProcessTestInstance(Object o, ExtensionContext extensionContext)
      throws Exception {

  }

  public Object createTestInstance(TestInstanceFactoryContext testInstanceFactoryContext,
      ExtensionContext extensionContext) throws TestInstantiationException {
    Class<?> testClass = extensionContext.getTestClass().get();
    Object obj = ClassUtils.newObject(testClass);
    Injection.inject(obj);
    return obj;
  }
}
