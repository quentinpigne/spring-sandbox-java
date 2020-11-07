package fr.quentinpigne.springsandboxjava.services;

import fr.quentinpigne.springsandboxjava.utils.cache.cacheablelist.CacheableList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    @CacheableList(value = "longRunningTreatment")
    public List<Integer> longRunningTreatment(List<Integer> param) throws InterruptedException {
        Thread.sleep(1000 * param.size());
        return param;
    }
}
