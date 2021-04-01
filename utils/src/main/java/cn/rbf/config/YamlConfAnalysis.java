package cn.rbf.config;

import org.yaml.snakeyaml.Yaml;

import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author fk7075
 * @version 1.0.0
 * @date 2020/11/16 上午4:42
 */
public class YamlConfAnalysis implements ConfAnalysis{

    private Yaml yaml;
    private LinkedHashMap<String,Object> map;


    public YamlConfAnalysis(Reader yamlReader){
        yaml=new Yaml();
        map= new LinkedHashMap<>();
        Iterator<Object> iterator = yaml.loadAll(yamlReader).iterator();
        while (iterator.hasNext()){
             map = (LinkedHashMap) iterator.next();
        }
    }

    public Map<String, Object> getMap() {
        return map;
    }

//    @Override
    public Object getObject(String prefix) {
        if(isExpression(prefix)){

        }else{

        }
        return null;
    }

}
