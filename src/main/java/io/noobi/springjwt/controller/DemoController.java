package io.noobi.springjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @GetMapping("/hello")
  public ResponseEntity<String> hello(){
    return ResponseEntity.ok("Hello World!");
  }

  @GetMapping("/admin")
  public ResponseEntity<String> admin(){
    return ResponseEntity.ok("Hello Admin!");
  }
}
