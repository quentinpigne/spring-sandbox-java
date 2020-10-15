package fr.quentinpigne.springsandboxjava.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    @Cacheable("longRunningTreatment")
    public Integer longRunningTreatment() throws InterruptedException {
        Thread.sleep(1000);
        return 42;
    }
}
