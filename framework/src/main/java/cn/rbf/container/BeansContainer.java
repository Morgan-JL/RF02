package cn.rbf.container;


import cn.rbf.exception.BeanNotFindException;
import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Morgan
 * @date 2021/3/31 14:38
 */
public class BeansContainer extends Container<Module> {

  private static final Logger logger = LoggerFactory.getLogger(BeansContainer.class);

  private static BeansContainer INSTANCE = new BeansContainer();

  private BeansContainer() {
  }

  public static BeansContainer getInstance() {
    return INSTANCE;
  }

  public Object getBean(Class clz) {
    Collection<Module> values = super.getBeans().values();
    for (Module module : values) {
      if (module.getBean().getClass().isAssignableFrom(clz)) {
        return module.getBean();
      }
    }
    throw new BeanNotFindException("`" + clz + "`未在bean容器中找到！");
  }
}
