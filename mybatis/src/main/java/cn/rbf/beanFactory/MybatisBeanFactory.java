package cn.rbf.beanFactory;

import cn.rbf.sql.RBFDataSource;
import java.util.List;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * @author Morgan
 * @date 2021/5/7 11:30
 */
public class MybatisBeanFactory extends BaseMybatisFactory {

  public MybatisBeanFactory() {
  }

  public List<Object> createBean() {
    List<Object> beans = super.createBean();
    List<RBFDataSource> allDataSource = getAllDataSource();
    Configuration configuration = new Configuration();
    configurationSetting(configuration);
    for (RBFDataSource rbfDataSource : allDataSource) {
      configuration.setEnvironment(new Environment("",getJdbcTransactionFactory(),rbfDataSource.createDataSource()));
      List<MapperSource> mapperSources = getMapperLocations();
      if(mapperSources!=null){
        mapperSources.forEach(in->new XMLMapperBuilder(in.getIn(),configuration,in.getDescription(),configuration.getSqlFragments()).parse());
      }
      SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
      beans.addAll(getMappers(sqlSessionFactory,configuration,rbfDataSource.getDbname()));
    }
    return beans;
  }
}
