package cn.rbf.core;

import cn.rbf.AutoScanApplicationContext;
import cn.rbf.container.Injection;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import org.apache.logging.log4j.ThreadContext;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * <p>
 * junit4
 * </p>
 *
 * @author Morgan
 * @date 2021/4/1 11:54
 */
public class RBFJunit4ClassRunner extends BlockJUnit4ClassRunner {

  private static final RuntimeMXBean mxb = ManagementFactory.getRuntimeMXBean();

  static {
    String pid = mxb.getName().split("@")[0];
    ThreadContext.put("pid", pid);
  }

  public RBFJunit4ClassRunner(Class<?> testClass) throws InitializationError {
    super(testClass);
    AutoScanApplicationContext.create(testClass);
  }

  @Override
  protected Object createTest() throws Exception {
    Object createTest = super.createTest();
    Injection.inject(createTest);
    return createTest;
  }

}
