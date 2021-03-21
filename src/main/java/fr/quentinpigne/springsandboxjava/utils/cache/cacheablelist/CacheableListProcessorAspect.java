package fr.quentinpigne.springsandboxjava.utils.cache.cacheablelist;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Component
@RequiredArgsConstructor
public class CacheableListProcessorAspect {

    private final CacheManager cacheManager;

    @Around("@annotation(CacheableList)")
    public List<Object> cacheableList(ProceedingJoinPoint joinPoint) throws Throwable {

        // Getting method, annotation and cache
        Method method = getCurrentMethod(joinPoint);
        CacheableList cacheableList = method.getAnnotation(CacheableList.class);
        Cache<Object, Object> cache = cacheManager.getCache(cacheableList.value());

        // List to spread for caching must be the first argument
        Object[] args = Arrays.copyOf(joinPoint.getArgs(), joinPoint.getArgs().length);
        List<Object> listParameter = (List<Object>) joinPoint.getArgs()[0];
        Object[] otherParameters = joinPoint.getArgs().length != 1 ? Arrays.copyOfRange(joinPoint.getArgs(), 1,
            joinPoint.getArgs().length) : null;

        // Getting all items of the list already stored in the cache
        Map<Object, Object> cachedItemByCacheKey = cache.getAll(
            listParameter.stream().map(item -> getCacheKey(method, item, otherParameters)).collect(Collectors.toSet()));

        // Computing list of parameters not found in cache
        List<Object> remainingParameters = listParameter.stream().filter(
            item -> !cachedItemByCacheKey.keySet().contains(getCacheKey(method, item, otherParameters))).distinct().collect(Collectors.toList());

        // No need to call initial method if everything was found in the cache
        if (remainingParameters.isEmpty()) {
            return listParameter.stream().map(item -> cachedItemByCacheKey.get(getCacheKey(method, item, otherParameters))).collect(Collectors.toList());
        }

        // Getting non cached items by calling initial method
        args[0] = remainingParameters;
        List<Object> nonCachedItems = (List<Object>) joinPoint.proceed(args);

        // Grouping non cached item by corresponding parameter and caching it
        Map<Object, Object> nonCachedItemByParameter = zipToMap(remainingParameters, nonCachedItems);
        cache.putAll(nonCachedItemByParameter.entrySet().stream().distinct().filter(e -> Objects.nonNull(e.getValue()))
            .collect(Collectors.toMap(e -> getCacheKey(method, e.getKey(), otherParameters), Map.Entry::getValue, (prev, next) -> next, HashMap::new)));

        return listParameter.stream().map(item -> cachedItemByCacheKey.containsKey(getCacheKey(method, item, otherParameters)) ? cachedItemByCacheKey
            .get(getCacheKey(method, item, otherParameters)) : nonCachedItemByParameter.get(item)).collect(Collectors.toList());
    }

    private Method getCurrentMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private Object getCacheKey(Method method, Object item, Object[] otherParameters) {
        if (otherParameters != null) {
            Object[] both = Stream.concat(Stream.of(method.getName(), item), Arrays.stream(otherParameters)).toArray(Object[]::new);
            return SimpleKeyGenerator.generateKey(both);
        } else {
            return SimpleKeyGenerator.generateKey(method.getName(), item);
        }
    }

    private Map<Object, Object> zipToMap(List<Object> keys, List<Object> values) {
        assert (keys.size() == values.size());
        Iterator<Object> valuesIterator = values.iterator();
        // We use this form of collect with HashMap constructor because values can contain null
        return keys.stream().collect(HashMap::new, (m, v) -> m.put(v, valuesIterator.next()), HashMap::putAll);
    }
}
