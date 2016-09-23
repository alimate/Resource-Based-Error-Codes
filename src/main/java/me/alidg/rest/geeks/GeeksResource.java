package me.alidg.rest.geeks;

import me.alidg.model.Geek;
import me.alidg.service.geeks.GeekAlreadyExists;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/geeks")
public class GeeksResource {
    @PostMapping
    public void createGeek(@Valid @RequestBody Geek geek) {
        // Normally we save the geek but not this time!
        throw new GeekAlreadyExists();
    }
}