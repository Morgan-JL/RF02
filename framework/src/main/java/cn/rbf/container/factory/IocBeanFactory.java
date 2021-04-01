//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container.factory;

import cn.rbf.base.BaseUtils;
import cn.rbf.container.BeansContainer;
import cn.rbf.container.Module;
import java.util.ArrayList;
import java.util.List;

public abstract class IocBeanFactory implements BeanFactory, Namer {
  private BeansContainer beanContainer = BeansContainer.getInstance();

  public IocBeanFactory() {
  }

  public final void add(Class aClass) {
//    Assert.isNull(this.createBean(), "当前" + aClass + "添加的bean为空！");
    this.createBean().forEach((bean) -> {
      String id = this.getBeanName(bean.getClass());
      beanContainer.append(id, new Module(id, bean));
    });
  }

  public List<Object> createBean() {
    return new ArrayList();
  }

  public String getBeanType(Class<?> beanClass) {
    return "component";
  }

  public String getBeanName(Class<?> beanClass) {
    return BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName());
  }
}
