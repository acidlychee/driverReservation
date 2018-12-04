package com.driver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {
  @RequestMapping("/test")
  public String thing() {
    return "Lin is a pig. Pig is fat";
  }
}
