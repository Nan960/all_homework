package com.wjy.util.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author wjy
 * @date 2020/9/10
 */
public class AnnotationUtils {

    final static Logger logger = LoggerFactory.getLogger(AnnotationUtils.class);

    @Nullable
    public static <A extends Annotation> A findAnnotation(Field field, @Nullable Class<A> annotationType) {
        Objects.requireNonNull(field, "Class must not be null");
        if (Objects.isNull(annotationType)) {
            return null;
        }
        return field.getDeclaredAnnotation(annotationType);
    }

    @Nullable
    public static <A extends Annotation> A findAnnotation(Method method, @Nullable Class<A> annotationType) {
        Objects.requireNonNull(method, "Class must not be null");
        if (Objects.isNull(annotationType)) {
            return null;
        }
        return method.getAnnotation(annotationType);
    }

    @Nullable
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, @Nullable Class<A> annotationType) {

        Objects.requireNonNull(clazz, "Class must not be null");
        if (Objects.isNull(annotationType)) {
            return null;
        }

        // AnnotationCacheKey cacheKey = new AnnotationCacheKey(clazz, annotationType);
        // A result = (A) findAnnotationCache.get(cacheKey);
        // if (result == null) {
        A result = findAnnotation(clazz, annotationType, new HashSet<>());
        // if (result != null && synthesize) {
        //     result = synthesizeAnnotation(result, clazz);
        // findAnnotationCache.put(cacheKey, result);
        // }
        // }
        return result;
    }

    @Nullable
    private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType, Set<Annotation> visited) {
        try {
            A annotation = clazz.getDeclaredAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            }
            for (Annotation declaredAnn : getDeclaredAnnotations(clazz)) {
                Class<? extends Annotation> declaredType = declaredAnn.annotationType();
                if (!isInJavaLangAnnotationPackage(declaredType) && visited.add(declaredAnn)) {
                    annotation = findAnnotation(declaredType, annotationType, visited);

                    if (annotation != null) {
                        // AliasFor 初始化值
                        final Map<Method, Method> aliasMap = AnnotationUtils.getAttributeAliasMap(declaredType, annotation.annotationType());
                        A finalAnnotation = annotation;
                        aliasMap.forEach((sourceMethod, targetMethod) -> {
                            final InvocationHandler invocationHandler = Proxy.getInvocationHandler(finalAnnotation);
                            try {
                                Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
                                // TODO 更换动态修改值方式  WARNING: All illegal access operations will be denied in a future release
                                memberValues.setAccessible(true);
                                Map o = (Map) memberValues.get(invocationHandler);
                                o.put(targetMethod.getName(), sourceMethod.invoke(declaredAnn));
                            } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                        return annotation;
                    }
                }
            }
        } catch (Throwable ex) {
            logger.warn("获取注解时出错：{}", clazz.getName(), ex);
            return null;
        }

        for (Class<?> ifc : clazz.getInterfaces()) {
            A annotation = findAnnotation(ifc, annotationType, visited);
            if (annotation != null) {
                return annotation;
            }
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return findAnnotation(superclass, annotationType, visited);
    }

    static Annotation[] getDeclaredAnnotations(AnnotatedElement element) {
        // if (element instanceof Class || element instanceof Member) {
        //     // Class/Field/Method/Constructor returns a defensively cloned array from getDeclaredAnnotations.
        //     // Since we use our result for internal iteration purposes only, it's safe to use a shared copy.
        //     return declaredAnnotationsCache.computeIfAbsent(element, AnnotatedElement::getDeclaredAnnotations);
        // }
        return element.getDeclaredAnnotations();
    }


    static boolean isInJavaLangAnnotationPackage(@Nullable Class<? extends Annotation> annotationType) {
        return (annotationType != null && isInJavaLangAnnotationPackage(annotationType.getName()));
    }

    public static boolean isInJavaLangAnnotationPackage(@Nullable String annotationType) {
        return (annotationType != null && annotationType.startsWith("java.lang.annotation"));
    }


    static Map<Method, Method> getAttributeAliasMap(@Nullable Class<? extends Annotation> sourceAnnotationType, @Nullable Class<? extends Annotation> targetAnnotationType) {
        if (sourceAnnotationType == null || targetAnnotationType == null) {
            return Collections.emptyMap();
        }

        // key:source,value:target
        Map<Method, Method> map = new LinkedHashMap<>();
        for (Method attribute : getAttributeMethods(sourceAnnotationType)) {
            AliasFor aliasFor = attribute.getAnnotation(AliasFor.class);
            if (aliasFor == null) {
                continue;
            }
            final Class<? extends Annotation> aliasAnno = aliasFor.annotation();
            if (aliasAnno.equals(Annotation.class)) {
                // 自己指向自己

            } else if (aliasAnno.equals(targetAnnotationType)) {
                // 确定是
                final List<Method> methods = getAttributeMethods(aliasAnno);
                for (Method method : methods) {
                    if (method.getName().equals(aliasFor.attribute())) {
                        map.put(attribute, method);
                    }
                }
            }
        }
        return map;
    }

    static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
        List<Method> methods = new ArrayList<>();
        for (Method method : annotationType.getDeclaredMethods()) {
            if (isAttributeMethod(method)) {
                method.setAccessible(true);
                methods.add(method);
            }
        }
        return methods;
    }

    static boolean isAttributeMethod(@Nullable Method method) {
        return (method != null && method.getParameterCount() == 0 && method.getReturnType() != void.class);
    }

    public static boolean hasAnnotation(@Nonnull Class<?> clazz, @Nonnull Class<? extends Annotation> annotationType) {
        Objects.requireNonNull(clazz, "clazz type must not be null");
        Objects.requireNonNull(annotationType, "annotationType type must not be null");
        return Objects.nonNull(findAnnotation(clazz, annotationType));
    }

    public static boolean isAnnotationMetaPresent(Class<? extends Annotation> annotationType, @Nullable Class<? extends Annotation> metaAnnotationType) {

        Objects.requireNonNull(annotationType, "Annotation type must not be null");
        if (metaAnnotationType == null) {
            return false;
        }

        Boolean metaPresent = Boolean.FALSE;
        if (findAnnotation(annotationType, metaAnnotationType) != null) {
            metaPresent = Boolean.TRUE;
        }
        return metaPresent;
    }

}
