package fr.quentinpigne.springsandboxjava.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    public Integer longRunningTreatment() throws InterruptedException {
        Thread.sleep(1000);
        return 42;
    }
}
