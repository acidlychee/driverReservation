package com.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandExecutor implements CommandLineRunner {
  @Autowired
  DataHandler dataHandler;

  @Override
  public void run(String... args) throws Exception {
    dataHandler.query();
  }
}
