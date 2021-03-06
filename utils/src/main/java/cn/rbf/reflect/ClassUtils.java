package cn.rbf.reflect;

import cn.rbf.annotation.Nullable;
import cn.rbf.base.Assert;
import cn.rbf.exception.LuckyReflectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public abstract class ClassUtils {

    private static final Logger log= LoggerFactory.getLogger(ClassUtils.class);
    public static final Class<?>[] SIMPLE_CLASSES={String.class,Byte.class,Short.class,Integer.class,
    Long.class,Float.class,Double.class,Boolean.class};
    /** The package separator character: {@code '.'}. */
    private static final char PACKAGE_SEPARATOR = '.';
    /** The path separator character: {@code '/'}. */
    private static final char PATH_SEPARATOR = '/';
    /** A reusable empty class array constant. */
    private static final Class<?>[] EMPTY_CLASS_ARRAY = {};
    /**
     * Map with primitive type as key and corresponding wrapper
     * type as value, for example: int.class -> Integer.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(8);

    /**
     * Map with primitive wrapper type as key and corresponding primitive
     * type as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(8);

    public static Class<?> forName(String fullPath,ClassLoader loader){
        Assert.notNull(fullPath, "Name must not be null");
        try {
            return Class.forName(fullPath,true,loader);
        } catch (ClassNotFoundException e) {
            LuckyReflectionException lex = new LuckyReflectionException(e);
            log.error("ClassNotFoundException: `"+fullPath+"` ????????????",lex);
            throw lex;
        }
    }

    /**
     * ?????????????????????????????????(?????????Object)???????????????(Field)
     * @param clzz ????????????Class
     * @return
     */
    public static Field[] getAllFields(Class<?> clzz) {
        if (clzz.getSuperclass() == Object.class) {
            return clzz.getDeclaredFields();
        }
        Field[] clzzFields = clzz.getDeclaredFields();
        Field[] superFields = getAllFields(clzz.getSuperclass());
        return delCoverFields(clzzFields,superFields);
    }

    public static <T> Constructor<T> getConstructor(Class<T> aClass,Class<?>[] paramTypes){
        try {
            return aClass.getConstructor(paramTypes);
        } catch (NoSuchMethodException e) {
            LuckyReflectionException lex = new LuckyReflectionException(e);
            log.error("NoSuchMethodException: ????????? `"+aClass.getName()+"` ??????????????????????????????????????????",lex);
            throw lex;
        }
    }

    /**
     * ????????????@Cover?????????????????????
     * @param thisFields ????????????????????????
     * @param superFields ??????????????????????????????
     * @return
     */
    private static Field[] delCoverFields(Field[] thisFields,Field[] superFields){
        List<Field> delCvoerFields=new ArrayList<>();
        Set<String> coverFieldNames=new HashSet<>();
        for (Field thisField : thisFields) {
            if(thisField.isAnnotationPresent(Cover.class)){
                coverFieldNames.add(thisField.getName());
            }
            delCvoerFields.add(thisField);
        }
        for (Field superField : superFields) {
            if(!coverFieldNames.contains(superField.getName())){
                delCvoerFields.add(superField);
            }
        }
        return delCvoerFields.toArray(new Field[delCvoerFields.size()]);
    }

    /**
     * ?????????????????????????????????(?????????Object)???????????????(Method)
     * @param clzz ????????????Class
     * @return
     */
    public static Method[] getAllMethod(Class<?> clzz){
        if (clzz.getSuperclass() == Object.class) {
            return clzz.getDeclaredMethods();
        }
        Method[] clzzMethods = clzz.getDeclaredMethods();
        Method[] superMethods = getAllMethod(clzz.getSuperclass());
        return delCoverMethods(clzzMethods,superMethods);
    }

    //????????????????????????,??????????????????????????????????????????????????????
    public static List<Method> getAllMethodForClass(Class<?> beanClass) {
        List<Method> allMethods = new LinkedList<>();
        //??????beanClass???????????????
        Set<Class<?>> classes = new LinkedHashSet<>(ClassUtils.getAllInterfacesForClassAsSet(beanClass));
        classes.add(beanClass);

        //?????????????????????????????????????????????????????????
        for (Class<?> clazz : classes) {
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
            allMethods.addAll(Arrays.asList(methods));
        }

        return allMethods;
    }



    public static String classPackageAsResourcePath(@Nullable Class<?> clazz) {
        if (clazz == null) {
            return "";
        }
        String className = clazz.getName();
        int packageEndIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        if (packageEndIndex == -1) {
            return "";
        }
        String packageName = className.substring(0, packageEndIndex);
        return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }
    /**
     * ????????????@Cover?????????????????????
     * @param thisMethods ????????????????????????
     * @param superMethods ??????????????????????????????
     * @return
     */
    private static Method[] delCoverMethods(Method[] thisMethods,Method[] superMethods){
        List<Method> delCoverMethods=new ArrayList<>();
        Set<String> coverMethodNames=new HashSet<>();
        for (Method thisMethod : thisMethods) {
            if(thisMethod.isAnnotationPresent(Cover.class)){
                coverMethodNames.add(thisMethod.getName());
            }
            delCoverMethods.add(thisMethod);
        }
        for (Method superMethod : superMethods) {
            if(!coverMethodNames.contains(superMethod.getName())){
                delCoverMethods.add(superMethod);
            }
        }
        return delCoverMethods.toArray(new Method[0]);
    }

    /**
     * ??????????????????????????????????????????????????????
     * @param tClass ???????????????Class
     * @param args ????????????????????????
     * @param <T>
     * @return
     */
    public static <T> T newObject(Class<T> tClass,Object...args){
        try {
            Constructor<T> constructor = findConstructor(tClass, array2Class(args));
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        }catch (Exception e){
            LuckyReflectionException lex = new LuckyReflectionException(e);
            log.error("?????????????????????class: '"+tClass+"',args: '"+Arrays.toString(args)+"'",lex);
            throw lex;
        }
    }

    public static <T> T newObject(Constructor<T> constructor,Object...args){
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LuckyReflectionException lex = new LuckyReflectionException(e);
            log.error("?????????????????????Constructor: '"+constructor+"',args: '"+Arrays.toString(args)+"'",lex);
            throw lex;
        }
    }

    public static Method findMethod(Class<?> tClass,String methodName,Class<?>[] methodParamClasses){
        try {
            return tClass.getMethod(methodName,methodParamClasses);
        }catch (Exception ignored){

        }
        List<Method> methods = getAllMethodForClass(tClass);
        out:for (Method m : methods) {
            if(!m.getName().equals(methodName)){
                continue;
            }
            Class<?>[] parameterTypes = m.getParameterTypes();
            if(parameterTypes.length == methodParamClasses.length){
                for (int i = 0 ,j= parameterTypes.length; i < j; i++) {
                    if(!parameterTypes[i].isAssignableFrom(methodParamClasses[i])){
                        continue out;
                    }
                }
                return m;
            }
        }
        throw new LuckyReflectionException("There is no static factory method named '"+methodName+"' parameter type '"+Arrays.toString(methodParamClasses)+"' in '"+tClass+"'");
    }

    public static <T> Constructor<T> findConstructor(Class<T> tClass, Class<?>[] argsClasses) {
        if(argsClasses == null || argsClasses.length == 0){
            try {
                return tClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new LuckyReflectionException(e);
            }
        }
        Constructor<?> ct=null;
        try {
           ct = tClass.getConstructor(argsClasses);
        }catch (Exception ignored){
            //?????????????????????????????????????????????...
        }

        if(ct == null){
            Constructor<?>[] constructors = tClass.getConstructors();
            out:for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if(argsClasses.length==parameterTypes.length){
                    for (int i = 0,j= parameterTypes.length; i < j; i++) {
                        if(!parameterTypes[i].isAssignableFrom(argsClasses[i])){
                            continue out;
                        }
                    }
                    ct=constructor;
                    break;
                }
            }
        }

        if(ct == null){
            throw new LuckyReflectionException("There is no corresponding construction method class = '"+tClass+"' , args = '"+Arrays.toString(argsClasses)+"'");
        }

        return (Constructor<T>) ct;


    }

    /**
     * ?????????Object[]????????????????????????Class[]
     * @param objs ????????????Object[]
     * @return
     */
    public static Class<?>[] array2Class(Object[] objs){
        Class<?>[] paramsClass=new Class<?>[objs.length];
        for (int i = 0; i < objs.length; i++) {
            paramsClass[i]=objs[i].getClass();
        }
        return paramsClass;
    }

    /**
     * ??????????????????Type?????????????????????
     * @param type ??????Type
     * @return
     */
    public static Class<?>[] getGenericType(Type type){
        if(type!=null && type instanceof ParameterizedType){
            ParameterizedType pt=(ParameterizedType) type;
            Type[] types=pt.getActualTypeArguments();
            Class<?>[] genericType=new Class<?>[types.length];
            for(int i=0;i<types.length;i++) {
                genericType[i]=(Class<?>)types[i];
            }
            return genericType;
        }else{
            return null;
        }
    }

    /**
     * ???????????????????????????JDK???????????????
     * @param clzz ????????????
     * @return
     */
    public static boolean isBasic(Class<?> clzz){
        return clzz.getClassLoader()==null;
    }

    /**
     * ?????????????????????????????????Class
     * @param className ???????????????
     * @return
     */
    public static Class<?> getClass(String className){
        try {
            Class<?> aClass = Class.forName(className);
            return aClass;
        } catch (ClassNotFoundException e) {
            LuckyReflectionException lex = new LuckyReflectionException(e);
            log.error("ClassNotFoundException",lex);
            throw lex;
        }
    }

    public static Object newObject(String fullPath){
        return newObject(getClass(fullPath));
    }

    public static Object newObject(String fullPath,Object...params){
        return newObject(getClass(fullPath),params);
    }

    /**
     * ??????????????????????????????????????????????????????
     * @param clzz ???CLass
     * @param annotation ????????????
     * @return ????????????????????????Field
     */
    public static List<Field> getFieldByAnnotation(Class<?> clzz, Class<? extends Annotation> annotation){
        Field[] allFields = getAllFields(clzz);
        List<Field> annFields=new ArrayList<>();
        for (Field field : allFields) {
            if(AnnotationUtils.isExist(field,annotation)){
                annFields.add(field);
            }
        }
        return annFields;
    }

    public static List<Field> getFieldByAnnotationArrayOR(Class<?> clzz, Class<? extends Annotation>[] annotationArray){
        Field[] allFields = getAllFields(clzz);
        List<Field> annFields=new ArrayList<>();
        for (Field field : allFields) {
            if(AnnotationUtils.isExistOrByArray(field,annotationArray)){
                annFields.add(field);
            }
        }
        return annFields;
    }

    /**
     * ??????????????????????????????????????????????????????(??????????????????????????????)
     * @param clzz ???CLass
     * @param annotation ????????????
     * @return ????????????????????????Field
     */
    public static List<Field> getFieldByStrengthenAnnotation(Class<?> clzz, Class<? extends Annotation> annotation){
        Field[] allFields = getAllFields(clzz);
        List<Field> annFields=new ArrayList<>();
        for (Field field : allFields) {
            if(AnnotationUtils.strengthenIsExist(field,annotation)){
                annFields.add(field);
            }
        }
        return annFields;
    }


    /**
     * ??????????????????????????????????????????????????????
     * @param clzz ???CLass
     * @param annotation ????????????
     * @return ????????????????????????Method
     */
    public static List<Method> getMethodByAnnotation(Class<?> clzz, Class<? extends Annotation> annotation){
        Method[] allMethods = getAllMethod(clzz);
        List<Method> annMethods=new ArrayList<>();
        for (Method method : allMethods) {
            if(AnnotationUtils.isExist(method,annotation)){
                annMethods.add(method);
            }
        }
        return annMethods;
    }

    public static List<Method> getMethodByAnnotationArrayOR(Class<?> clzz, Class<? extends Annotation>[] annotationArray){
        Method[] allMethod = getAllMethod(clzz);
        List<Method> annMethods=new ArrayList<>();
        for (Method method : allMethod) {
            if(AnnotationUtils.isExistOrByArray(method,annotationArray)){
                annMethods.add(method);
            }
        }
        return annMethods;
    }

    /**
     * ??????????????????????????????????????????????????????(??????????????????????????????)
     * @param clzz ???CLass
     * @param annotation ????????????
     * @return ????????????????????????Method
     */
    public static List<Method> getMethodByStrengthenAnnotation(Class<?> clzz, Class<? extends Annotation> annotation){
        Method[] allMethods = getAllMethod(clzz);
        List<Method> annMethods=new ArrayList<>();
        for (Method method : allMethods) {
            if(AnnotationUtils.strengthenIsExist(method,annotation)){
                annMethods.add(method);
            }
        }
        return annMethods;
    }


    /**
     * ???????????????????????????Java????????????
     * @param aClass ????????????
     * @return
     */
    public static boolean isPrimitive(Class<?> aClass){
        return aClass.isPrimitive();
    }

    /**
     * ???????????????????????????Java???????????????????????????
     * @param aClass ????????????
     * @return
     */
    public static boolean isSimple(Class<?> aClass){
        for (Class<?> simpleClass : SIMPLE_CLASSES) {
            if(aClass==simpleClass){
                return true;
            }
        }
        return false;
    }

    public static final Class<?>[] SIMPLE_ARRAY_CLASSES={
            String[].class ,  Byte[].class ,  Short[].class ,Integer[].class,
              Long[].class , Float[].class , Double[].class ,Boolean[].class,
              char[].class ,  byte[].class ,  short[].class ,    int[].class,
              long[].class , float[].class , double[].class ,boolean[].class
    };

    /**
     * ???????????????????????????Java???????????????????????????
     * @param aClass ????????????
     * @return
     */
    public static boolean isSimpleArray(Class<?> aClass){
        for (Class<?> simpleClass : SIMPLE_ARRAY_CLASSES) {
            if(aClass==simpleClass){
                return true;
            }
        }
        return false;
    }

    public static boolean isAssignableFromArrayOr(Class<?> targetClass,Class<?>[] arrayClass){
        for (Class<?> aClass : arrayClass) {
            if(aClass.isAssignableFrom(targetClass)){
                return true;
            }
        }
        return false;
    }

    public static boolean isJdkType(Class<?> aClass){
        return aClass.getClassLoader()==null;
    }

    public static String convertClassNameToResourcePath(String className) {
        Assert.notNull(className, "Class name must not be null");
        return className.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }

    public static List<Method> getAllMethods(Class<?> aClass){
        List<Method> allMethods = new LinkedList<>();
        //??????beanClass???????????????
        Set<Class<?>> classes = new LinkedHashSet<>(getAllInterfacesForClassAsSet(aClass));
        classes.add(aClass);
        //?????????????????????????????????????????????????????????
        for (Class<?> clazz : classes) {
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
            allMethods.addAll(Arrays.asList(methods));
        }
        return allMethods;
    }

    @Nullable
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    /**
     * Determine whether the {@link Class} identified by the supplied name is present
     * and can be loaded. Will return {@code false} if either the class or
     * one of its dependencies is not present or cannot be loaded.
     * @param className the name of the class to check
     * @param classLoader the class loader to use
     * (may be {@code null} which indicates the default class loader)
     * @return whether the specified class is present (including all of its
     * superclasses and interfaces)
     * @throws IllegalStateException if the corresponding class is resolvable but
     * there was a readability mismatch in the inheritance hierarchy of the class
     * (typically a missing dependency declaration in a Jigsaw module definition
     * for a superclass or interface implemented by the class to be checked here)
     */
    public static boolean isPresent(String className, @Nullable ClassLoader classLoader) {
        try {
            forName(className, classLoader);
            return true;
        }
        catch (IllegalAccessError err) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" +
                    className + "]: " + err.getMessage(), err);
        }
        catch (Throwable ex) {
            // Typically ClassNotFoundException or NoClassDefFoundError...
            return false;
        }
    }

    /**
     * Check whether the given class is cache-safe in the given context,
     * i.e. whether it is loaded by the given ClassLoader or a parent of it.
     * @param clazz the class to analyze
     * @param classLoader the ClassLoader to potentially cache metadata in
     * (may be {@code null} which indicates the system class loader)
     */
    public static boolean isCacheSafe(Class<?> clazz, @Nullable ClassLoader classLoader) {
        Assert.notNull(clazz, "Class must not be null");
        try {
            ClassLoader target = clazz.getClassLoader();
            // Common cases
            if (target == classLoader || target == null) {
                return true;
            }
            if (classLoader == null) {
                return false;
            }
            // Check for match in ancestors -> positive
            ClassLoader current = classLoader;
            while (current != null) {
                current = current.getParent();
                if (current == target) {
                    return true;
                }
            }
            // Check for match in children -> negative
            while (target != null) {
                target = target.getParent();
                if (target == classLoader) {
                    return false;
                }
            }
        }
        catch (SecurityException ex) {
            // Fall through to loadable check below
        }

        // Fallback for ClassLoaders without parent/child relationship:
        // safe if same Class can be loaded from given ClassLoader
        return (classLoader != null && isLoadable(clazz, classLoader));
    }


    /**
     * Determine if the supplied class is an <em>inner class</em>,
     * i.e. a non-static member of an enclosing class.
     * @return {@code true} if the supplied class is an inner class
     * @since 5.0.5
     * @see Class#isMemberClass()
     */
    public static boolean isInnerClass(Class<?> clazz) {
        return (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()));
    }

    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        Assert.notNull(lhsType, "Left-hand side type must not be null");
        Assert.notNull(rhsType, "Right-hand side type must not be null");
        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        }
        if (lhsType.isPrimitive()) {
            Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
            return (lhsType == resolvedPrimitive);
        }
        else {
            Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);
            return (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper));
        }
    }

    public static Class<?> resolvePrimitiveIfNecessary(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return (clazz.isPrimitive() && clazz != void.class ? primitiveTypeToWrapperMap.get(clazz) : clazz);
    }

    public static Class<?> resolveClassName(String className, @Nullable ClassLoader classLoader)
            throws IllegalArgumentException {

        try {
            return forName(className, classLoader);
        }
        catch (IllegalAccessError err) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" +
                    className + "]: " + err.getMessage(), err);
        }
        catch (LinkageError err) {
            throw new IllegalArgumentException("Unresolvable class definition for class [" + className + "]", err);
        }
    }

    public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
        return getAllInterfacesForClass(clazz, null);
    }

    /**
     * Return all interfaces that the given class implements as an array,
     * including ones implemented by superclasses.
     * <p>If the class itself is an interface, it gets returned as sole interface.
     * @param clazz the class to analyze for interfaces
     * @param classLoader the ClassLoader that the interfaces need to be visible in
     * (may be {@code null} when accepting all declared interfaces)
     * @return all interfaces that the given object implements as an array
     */
    public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, @Nullable ClassLoader classLoader) {
        return toClassArray(getAllInterfacesForClassAsSet(clazz, classLoader));
    }

    /**
     * Return all interfaces that the given instance implements as a Set,
     * including ones implemented by superclasses.
     * @param instance the instance to analyze for interfaces
     * @return all interfaces that the given instance implements as a Set
     */
    public static Set<Class<?>> getAllInterfacesAsSet(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        return getAllInterfacesForClassAsSet(instance.getClass());
    }

    /**
     * Return all interfaces that the given class implements as a Set,
     * including ones implemented by superclasses.
     * <p>If the class itself is an interface, it gets returned as sole interface.
     * @param clazz the class to analyze for interfaces
     * @return all interfaces that the given object implements as a Set
     */
    public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz) {
        return getAllInterfacesForClassAsSet(clazz, null);
    }

    /**
     * Return all interfaces that the given class implements as a Set,
     * including ones implemented by superclasses.
     * <p>If the class itself is an interface, it gets returned as sole interface.
     * @param clazz the class to analyze for interfaces
     * @param classLoader the ClassLoader that the interfaces need to be visible in
     * (may be {@code null} when accepting all declared interfaces)
     * @return all interfaces that the given object implements as a Set
     */
    public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz, @Nullable ClassLoader classLoader) {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface() && isVisible(clazz, classLoader)) {
            return Collections.singleton(clazz);
        }
        Set<Class<?>> interfaces = new LinkedHashSet<>();
        Class<?> current = clazz;
        while (current != null) {
            Class<?>[] ifcs = current.getInterfaces();
            for (Class<?> ifc : ifcs) {
                if (isVisible(ifc, classLoader)) {
                    interfaces.add(ifc);
                }
            }
            current = current.getSuperclass();
        }
        return interfaces;
    }

    /**
     * Check whether the given class is visible in the given ClassLoader.
     * @param clazz the class to check (typically an interface)
     * @param classLoader the ClassLoader to check against
     * (may be {@code null} in which case this method will always return {@code true})
     */
    public static boolean isVisible(Class<?> clazz, @Nullable ClassLoader classLoader) {
        if (classLoader == null) {
            return true;
        }
        try {
            if (clazz.getClassLoader() == classLoader) {
                return true;
            }
        }
        catch (SecurityException ex) {
            // Fall through to loadable check below
        }

        // Visible if same Class can be loaded from given ClassLoader
        return isLoadable(clazz, classLoader);
    }

    /**
     * Check whether the given class is loadable in the given ClassLoader.
     * @param clazz the class to check (typically an interface)
     * @param classLoader the ClassLoader to check against
     * @since 5.0.6
     */
    private static boolean isLoadable(Class<?> clazz, ClassLoader classLoader) {
        try {
            return (clazz == classLoader.loadClass(clazz.getName()));
            // Else: different class with same name found
        }
        catch (ClassNotFoundException ex) {
            // No corresponding class found at all
            return false;
        }
    }

    /**
     * Copy the given {@code Collection} into a {@code Class} array.
     * <p>The {@code Collection} must contain {@code Class} elements only.
     * @param collection the {@code Collection} to copy
     * @return the {@code Class} array
     * @since 3.1
     */
    public static Class<?>[] toClassArray(@Nullable Collection<Class<?>> collection) {
        return (!Assert.isEmptyCollection(collection) ? collection.toArray(EMPTY_CLASS_ARRAY) : EMPTY_CLASS_ARRAY);
    }


}
