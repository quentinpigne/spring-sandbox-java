package fr.quentinpigne.springsandboxjava.controllers;

import fr.quentinpigne.springsandboxjava.services.TestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping(value = "", params = { "test" })
    public List<Long> test(@NotEmpty(message = "Test parameter cannot be empty.") @RequestParam("test") List<Long> test) {
        return test;
    }

    @GetMapping("cache")
    public Integer testCache() throws InterruptedException {
        logger.trace("Test cache");
        return testService.longRunningTreatment();
    }

    @GetMapping("network/{id}")
    public ResponseEntity<String> testNetwork() {
        logger.trace("Test network");
        RestTemplate restTemplate = new RestTemplate();
        String httpUrl = "http://localhost:8081/test/cache";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        return restTemplate.getForEntity(builder.build().toUri(), String.class);
    }
}
