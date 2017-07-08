package com.driver;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;

import javafx.util.Pair;

@Service
public class BookingService extends BookingEndpoint {

  final String viewState = "/wEPDwUJLTg2MDU2MzQ1D2QWAmYPZBYCAgMPZBYQAgEPDxYEHgRUZXh0BQ/mlK" +
      "/ku5jlrp3nvLTotLkeC05hdmlnYXRlVXJsBUlodHRwOi8vMjIyLjQ0LjMwLjg2OjgwODEvZnAvZnAvY29zdC9zdHVkZW50Q29zdC5kbz9zdHVkZW50Q29kZT1BUjA3MDQ0MDQxZGQCAw8PFgIeCEltYWdlVXJsBTF+L2xvZ2dpbmcvU2hvd0hlYWRQaWN0dXJlLmFzcHg/cHVwaWxObz1BUjA3MDQ0MDQxZGQCBQ8PFgIfAAUG6buO5piOZGQCBw8PFgIfAAUD55S3ZGQCCQ8PFgIfAAUKQVIwNzA0NDA0MWRkAgsPDxYCHwAFAkMxZGQCDQ8PFgIfAAUG5aSn6LevZGQCHw9kFhICBw8PFgIfAAUKMjAxNy0wNy0wNWRkAgsPDxYCHwAFCeiWm+WRqOadpWRkAg8PDxYCHwAFAzE4MGRkAhMPDxYCHwAFBTA3OjAwZGQCFw8PFgIfAAUL5rKqRTQxMzLlraZkZAIZDxYCHgdWaXNpYmxlaBYCAgMPPCsADQBkAhsPZBYGAgMPEA8WAh4HQ2hlY2tlZGdkZGRkAgUPEA8WAh8EaGRkZGQCBw8WAh8DaBYIAgMPEGQPFgdmAgECAgIDAgQCBQIGFgcQBQ8tLS3or7fpgInmi6ktLS0FDy0tLeivt+mAieaLqS0tLWcQBQ3lnLDpk4Ex5Y+357q/BQ3lnLDpk4Ex5Y+357q/ZxAFDeWcsOmTgTflj7fnur8FDeWcsOmTgTflj7fnur9nEAUQ5Zyw6ZOBN+WPt+e6vygxKQUQ5Zyw6ZOBN+WPt+e6vygxKWcQBRDlnLDpk4E35Y+357q/KDIpBRDlnLDpk4E35Y+357q/KDIpZxAFEOWcsOmTgTflj7fnur8oMykFEOWcsOmTgTflj7fnur8oMylnEAUG5p2o5rWmBQbmnajmtaZnFgFmZAIFDw8WAh4HRW5hYmxlZGhkZAIJDxBkZBYBZmQCCw8PFgIfBWhkZAIdDw8WBB4NT25DbGllbnRDbGljawWQAWlmIChDbGllbnRWYWxpZGF0ZSgpKSB7dGhpcy5kaXNhYmxlZD10cnVlO3RoaXMudmFsdWU9J+aCqOivt+eojeWQji4uLic7X19kb1Bvc3RCYWNrKCdjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIyJGJ0blN1Ym1pdCcsJycpfWVsc2V7cmV0dXJuIHRydWU7fR8FZ2RkAikPEGRkFgFmZBgCBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAwUgY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMiRyYWRpbzMFIGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjIkcmFkaW80BSBjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIyJHJhZGlvNAUjY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMiRndkJ1c0xpbmUPZ2TjZRWgismfjW+gKZMMXQPUFwqF7A==";
  final String validation = "/wEWCAKUiO2CDwKjsOvhAwKfi5rBAQLcnt3hDQKhp87FBALstcG6DALstdW6DALPqJWiCr" +
      "+MObqMi1rEbf5Os1De2cbfjkgR";
  final String generator = "5E06A1B7";
  final String button = "radio3";
  final String submit = "预  约";
  final String HU_YE = "9310056854";
  final String LU_ZHEN_YU = "9115049866";

  @PostConstruct
  public void booking() {
    bookingCoach(HU_YE, "2017-07-23");
  }

  private List<Pair<String, String>> pairs = Arrays.asList(
      new Pair<>("0800", "9"),
      new Pair<>("0900", "10"),
      new Pair<>("1000", "11"),
      new Pair<>("1100", "12"),
      new Pair<>("0700", "8"));

  private volatile ThreadPoolTaskExecutor executor;

  public void bookingCoach(String coach, String date) {
    while (true) {
      try {
        pairs.forEach(t -> {
          getAsyncExecutor().execute(new Runnable() {
            @Override
            public void run() {
              String a = buildBookingEndpoint().booking(
                  coach, date, t.getKey(), "场外", t.getValue(),
                  viewState, generator, validation, button, submit);
              if(a.contains("不能预约")){
                System.out.println("It's working!!!!!!!!!!!!!!!!!!!!!");
              } else {
                System.out.println(a);
              }
            }
          });
        });
      } catch (Exception err) {
        //
      }
    }
  }

  private Executor getAsyncExecutor() {
    if (executor != null) {
      return executor;
    }
    synchronized (this) {
      if (executor == null) {
        ThreadPoolTaskExecutor newExecutor = new ThreadPoolTaskExecutor();
        newExecutor.setCorePoolSize(5);
        newExecutor.setMaxPoolSize(10);
        newExecutor.setQueueCapacity(1000);
        newExecutor.setThreadGroupName("PaymentAsyncExecutor");
        newExecutor.setThreadNamePrefix("PaymentAsyncExecutorThread-");
        newExecutor.initialize();
        executor = newExecutor;
      }
      return executor;
    }
  }
}
