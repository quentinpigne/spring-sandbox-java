package fr.quentinpigne.springsandboxjava.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping(value = "", params = { "test" })
    public List<Long> test(@NotEmpty(message = "Test parameter cannot be empty.") @RequestParam("test") List<Long> test) {
        return test;
    }
}
