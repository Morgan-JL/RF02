//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.container.factory;

import cn.rbf.base.BaseUtils;

public interface Namer {
  /**
   * 返回Bean实例在IOC容器中的唯一ID
   * @param beanClass BeanClass
   * @return 唯一ID
   */
  static String getBeanName(Class<?> beanClass){
    String simpleName = beanClass.getSimpleName();
    if("".equals(simpleName)){
      String classStr = beanClass.toString();
      simpleName=classStr.substring(classStr.lastIndexOf(".")+1);
    }
    return BaseUtils.lowercaseFirstLetter(simpleName);
  }

  String getBeanType(Class<?> beanClass);
}
