package fr.quentinpigne.springsandboxjava.utils;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class CacheableListProcessorAspect {

    private final CacheManager cacheManager;

    @Around("@annotation(CacheableList)")
    public List<Object> cacheableList(ProceedingJoinPoint joinPoint) throws Throwable {

        Method method = getCurrentMethod(joinPoint);
        List<Object> listParameter = (List<Object>) joinPoint.getArgs()[0];
        CacheableList cacheableList = method.getAnnotation(CacheableList.class);

        Cache cache = cacheManager.getCache(cacheableList.value());

        List<Object> returnList = new ArrayList<>();
        List<Object> remainingParameters = new ArrayList<>();

        listParameter.forEach(item -> {
            Object cachedItem = cache.get(getCacheKey(method, item), cacheableList.itemType());

            if (cachedItem != null) {
                returnList.add(cachedItem);
            } else {
                remainingParameters.add(item);
            }
        });

        if (!remainingParameters.isEmpty()) {
            List<Object> remainingList = (List<Object>) joinPoint.proceed(new List[] { remainingParameters });
            remainingList.forEach(item -> {
                cache.put(getCacheKey(method, item), item);
            });
            returnList.addAll(remainingList);
        }

        return returnList;
    }

    private Method getCurrentMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private Object getCacheKey(Method method, Object item) {
        return SimpleKeyGenerator.generateKey(method.getName(), item);
    }
}
