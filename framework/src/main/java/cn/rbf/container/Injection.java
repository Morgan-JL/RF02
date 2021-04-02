//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container;

import cn.rbf.annotations.Inject;
import cn.rbf.exception.BeanNotFindException;
import cn.rbf.exception.ClassBeanNotFindException;
import cn.rbf.reflect.AnnotationUtils;
import cn.rbf.reflect.ClassUtils;
import cn.rbf.reflect.FieldUtils;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

public class Injection {

  private static BeansContainer beanContainer = BeansContainer.getInstance();

  public Injection() {
  }

  public static void inject(Object obj) {
    Class<?> aClass = obj.getClass();
    Field[] allFields = ClassUtils.getAllFields(aClass);
    Field[] fields = allFields;
    int fLen = allFields.length;

    for (int i = 0; i < fLen; ++i) {
      Field field = fields[i];
      Class<?> fieldType = field.getType();
      if (field.isAnnotationPresent(Inject.class)) {
        Inject inject = (Inject) AnnotationUtils.get(field, Inject.class);
        String confId = inject.value();
        if (!"".equals(confId)) {
          if (beanContainer.getBean(confId) == null) {
            throw new BeanNotFindException("`" + confId + "`依赖注入失败！");
          }

          FieldUtils.setValue(obj, field, beanContainer.getBean(confId));
        } else {
          boolean flag = false;
          Collection<Module> modules = beanContainer.getBeans().values();
          Iterator iterator = modules.iterator();

          while (iterator.hasNext()) {
            Module module = (Module) iterator.next();
            Class<?> beanClass = module.getBean().getClass();
            if (fieldType.isAssignableFrom(beanClass)) {
              FieldUtils.setValue(obj, field, module.getBean());
              flag = true;
              break;
            }
          }

          if (!flag) {
            throw new ClassBeanNotFindException("`" + fieldType + "`依赖注入失败！");
          }
        }
      }
    }

  }
}
