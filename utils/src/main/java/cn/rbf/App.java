package cn.rbf;

import cn.rbf.base.JexlEngineUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Map<String,Object> map = new HashMap<>();

        map.put("sd","ss${fd}");
        map.put("fd","morgan");
        JexlEngineUtil j =  new JexlEngineUtil(map);
        System.out.println(j.getProperties("${sd}"));
    }
}
