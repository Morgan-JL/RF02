package cn.rbf.config;

import cn.rbf.base.Assert;
import cn.rbf.conversion.JavaConversion;
import cn.rbf.reflect.ClassUtils;
import cn.rbf.serializable.JSONSerializationScheme;
import cn.rbf.serializable.XMLSerializationScheme;
import cn.rbf.servlet.param.ParameterAnalysis;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author fk7075
 * @version 1.0
 * @date 2020/11/19 10:58
 */
public abstract class YamlParsing {

    private static YamlConfAnalysis yaml;

    public static void loadWeb(WebConfig conf){
        yaml = ConfigUtils.getYamlConfAnalysis();
        if(Assert.isNotNull(yaml)){
            load(yaml.getMap(),conf);
        }
        conf.setFirst(false);
    }

    private static Object get(Object suffix){
        return yaml.getObject(suffix);
    }

    private static void load(Map<String,Object> config,WebConfig web){
        if(config.containsKey("lucky")){
            Object luckyNode = config.get("lucky");
            if(luckyNode instanceof Map){
                Map<String,Object> luckyMap= (Map<String, Object>) luckyNode;
                if(luckyMap.containsKey("web")){
                    Object webNode = luckyMap.get("web");
                    if(webNode instanceof Map){
                        Map<String,Object> webMap= (Map<String, Object>) webNode;
                        if(webMap.containsKey("encoding")){
                            web.setEncoding((String)get(webMap.get("encoding")));
                        }
                        if(webMap.containsKey("web-root")){
                            web.setWebRoot((String)get(webMap.get("web-root")));
                        }
                        if(webMap.containsKey("post-change-method")){
                            Object postChangeMethod = get(webMap.get("post-change-method"));
                            if(postChangeMethod instanceof Boolean){
                                web.setPostChangeMethod((Boolean) postChangeMethod);
                            }else{
                                web.setPostChangeMethod((Boolean) JavaConversion
                                    .strToBasic(postChangeMethod.toString(),boolean.class));
                            }
                        }
                        if(webMap.containsKey("static-resource-manage")){
                            Object srm = get(webMap.get("static-resource-manage"));
                            if(srm instanceof Boolean){
                                web.setOpenStaticResourceManage((boolean)srm);
                            }else{
                                web.setOpenStaticResourceManage((Boolean)JavaConversion.strToBasic(srm.toString(),boolean.class));
                            }

                        }
                        if(webMap.containsKey("multipart-max-file-size")){
                            Object mmfs = get(webMap.get("multipart-max-file-size"));
                            if(mmfs instanceof Integer){
                                web.setMultipartMaxFileSize((int)mmfs);
                            }else if(mmfs instanceof Long){
                                web.setMultipartMaxFileSize((long)mmfs);
                            }
                        }
                        if(webMap.containsKey("multipart-max-request-size")){
                            Object mmrs = get(webMap.get("multipart-max-request-size"));
                            if(mmrs instanceof Integer){
                                web.setMultipartMaxRequestSize((int)mmrs);
                            }else if(mmrs instanceof Long){
                                web.setMultipartMaxRequestSize((long)mmrs);
                            }
                        }
                        if(webMap.containsKey("prefix")){
                            web.setPrefix((String)get(webMap.get("prefix")));
                        }
                        if(webMap.containsKey("suffix")){
                            web.setSuffix((String)get(webMap.get("suffix")));
                        }
                        if(webMap.containsKey("httpclient-connection-timeout")){
                            Object hct = get(webMap.get("httpclient-connection-timeout"));
                            if(hct instanceof Integer){
                                web.setConnectTimeout((int)hct);
                            }else{
                                web.setConnectTimeout((int)JavaConversion.strToBasic(hct.toString(),
                                        int.class,
                                        true));
                            }

                        }
                        if(webMap.containsKey("httpclient-request-timeout")){
                            Object hrt = get(webMap.get("httpclient-request-timeout"));
                            if(hrt instanceof Integer){
                                web.setRequestTimeout((Integer) hrt);
                            }else{
                                web.setRequestTimeout((int)JavaConversion.strToBasic(
                                        hrt.toString(),
                                        int.class,
                                        true));
                            }
                        }
                        if(webMap.containsKey("httpclient-socket-timeout")){
                            Object hst = get(webMap.get("httpclient-socket-timeout"));
                            if(hst instanceof Integer){
                                web.setSocketTimeout((int) hst);
                            }else{
                                web.setSocketTimeout((int)JavaConversion.strToBasic(
                                        hst.toString(),
                                        int.class,
                                        true));
                            }

                        }
                        if(webMap.containsKey("static-handler")){
                            Map<String, String> staticHandlerMap = (Map<String, String>) webMap.get("static-handler");
                            for(Map.Entry<String,String> entry:staticHandlerMap.entrySet()){
                                web.addStaticHander(entry.getKey(),get(entry.getValue()).toString());
                            }
                        }
                        if(webMap.containsKey("specifi-resources-restrict-ip")){
                            Map<String, Set<String>> srriMap = (Map<String, Set<String>>) webMap.get("specifi-resources-restrict-ip");
                            for(Map.Entry<String,Set<String>> entry:srriMap.entrySet()){
                                Set<String> valueSet = entry.getValue().stream().map($v -> get($v).toString()).collect(Collectors.toSet());
                                web.addSpecifiResourcesIpRestrict(entry.getKey(), valueSet);
                            }
                        }
                        if(webMap.containsKey("global-resources-restrict-ip")){
                            List<String> grriList = (List<String>) webMap.get("global-resources-restrict-ip");
                            for (String $value : grriList) {
                                web.addGlobalResourcesIpRestrict(get($value).toString());
                            }
                        }
                        if(webMap.containsKey("static-resources-restrict-ip")){
                            List<String> srriList= (List<String>) webMap.get("static-resources-restrict-ip");
                            for (String $value : srriList) {
                                web.addStaticResourcesIpRestrict(get($value).toString());
                            }
                        }
                        if(webMap.containsKey("error-page")){
                            Map<String, String> errorPageMap = (Map<String, String>) webMap.get("error-page");
                            for(Map.Entry<String,String> entry:errorPageMap.entrySet()){
                                web.addErrorPage(entry.getKey(),get(entry.getValue()).toString());
                            }
                        }
                        if(webMap.containsKey("favicon-ico")){
                            web.setFavicon(get(webMap.get("favicon-ico")).toString());
                        }


                        if(webMap.containsKey("call-api")){
                            Map<String,String> callApi=(Map<String,String>)webMap.get("call-api");
                            for(Map.Entry<String,String> entry:callApi.entrySet()){
                                web.addCallApi(entry.getKey(),get(entry.getValue()).toString());
                            }
                        }


                        if(webMap.containsKey("serialization")){
                            Object serializationNode = webMap.get("serialization");
                            if(serializationNode instanceof Map){
                                Map<String,Object> serializationMap= (Map<String, Object>) serializationNode;
                                Object json = get(serializationMap.get("json"));
                                Object xml = get(serializationMap.get("xml"));
                                if(json instanceof String){
                                    web.setJsonSerializationScheme((JSONSerializationScheme)ClassUtils.newObject(json.toString()));
                                }
                                if(xml instanceof String){
                                    web.setXmlSerializationScheme((XMLSerializationScheme) ClassUtils.newObject(xml.toString()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private static List<String> to$List(List<String> list){
        return list.stream().map($v->get($v).toString()).collect(Collectors.toList());
    }
    private static String[] listToArrayByStr(List<String> list){
        String[] array=new String[list.size()];
        list.toArray(array);
        return array;
    }
}
