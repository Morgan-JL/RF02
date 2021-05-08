package cn.rbf.fileload;


import cn.rbf.annotation.Nullable;
import cn.rbf.utils.ResourceUtils;

/**
 * @author fk
 * @version 1.0
 * @date 2021/1/14 0014 15:52
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;

    @Nullable
    ClassLoader getClassLoader();

    Resource getResource(String location);

}
