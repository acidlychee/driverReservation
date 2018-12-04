package com.driver;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;

import javafx.util.Pair;

//@Service
public class BookingService extends BookingEndpoint {

  private static volatile ThreadPoolTaskExecutor executor;
  private static volatile ThreadPoolTaskExecutor executor2;

  final String viewState = "/wEPDwUJLTg2MDU2MzQ1D2QWAmYPZBYCAgMPZBYQAgEPDxYEHgRUZXh0BQ/mlK" +
      "/ku5jlrp3nvLTotLkeC05hdmlnYXRlVXJsBUlodHRwOi8vMjIyLjQ0LjMwLjg2OjgwODEvZnAvZnAvY29zdC9zdHVkZW50Q29zdC5kbz9zdHVkZW50Q29kZT1BUjA3MDQ0MDQxZGQCAw8PFgIeCEltYWdlVXJsBTF+L2xvZ2dpbmcvU2hvd0hlYWRQaWN0dXJlLmFzcHg/cHVwaWxObz1BUjA3MDQ0MDQxZGQCBQ8PFgIfAAUG6buO5piOZGQCBw8PFgIfAAUD55S3ZGQCCQ8PFgIfAAUKQVIwNzA0NDA0MWRkAgsPDxYCHwAFAkMxZGQCDQ8PFgIfAAUG5aSn6LevZGQCHw9kFhICBw8PFgIfAAUKMjAxNy0wNy0wNWRkAgsPDxYCHwAFCeiWm+WRqOadpWRkAg8PDxYCHwAFAzE4MGRkAhMPDxYCHwAFBTA3OjAwZGQCFw8PFgIfAAUL5rKqRTQxMzLlraZkZAIZDxYCHgdWaXNpYmxlaBYCAgMPPCsADQBkAhsPZBYGAgMPEA8WAh4HQ2hlY2tlZGdkZGRkAgUPEA8WAh8EaGRkZGQCBw8WAh8DaBYIAgMPEGQPFgdmAgECAgIDAgQCBQIGFgcQBQ8tLS3or7fpgInmi6ktLS0FDy0tLeivt+mAieaLqS0tLWcQBQ3lnLDpk4Ex5Y+357q/BQ3lnLDpk4Ex5Y+357q/ZxAFDeWcsOmTgTflj7fnur8FDeWcsOmTgTflj7fnur9nEAUQ5Zyw6ZOBN+WPt+e6vygxKQUQ5Zyw6ZOBN+WPt+e6vygxKWcQBRDlnLDpk4E35Y+357q/KDIpBRDlnLDpk4E35Y+357q/KDIpZxAFEOWcsOmTgTflj7fnur8oMykFEOWcsOmTgTflj7fnur8oMylnEAUG5p2o5rWmBQbmnajmtaZnFgFmZAIFDw8WAh4HRW5hYmxlZGhkZAIJDxBkZBYBZmQCCw8PFgIfBWhkZAIdDw8WBB4NT25DbGllbnRDbGljawWQAWlmIChDbGllbnRWYWxpZGF0ZSgpKSB7dGhpcy5kaXNhYmxlZD10cnVlO3RoaXMudmFsdWU9J+aCqOivt+eojeWQji4uLic7X19kb1Bvc3RCYWNrKCdjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIyJGJ0blN1Ym1pdCcsJycpfWVsc2V7cmV0dXJuIHRydWU7fR8FZ2RkAikPEGRkFgFmZBgCBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAwUgY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMiRyYWRpbzMFIGN0bDAwJENvbnRlbnRQbGFjZUhvbGRlcjIkcmFkaW80BSBjdGwwMCRDb250ZW50UGxhY2VIb2xkZXIyJHJhZGlvNAUjY3RsMDAkQ29udGVudFBsYWNlSG9sZGVyMiRndkJ1c0xpbmUPZ2TjZRWgismfjW+gKZMMXQPUFwqF7A==";
  final String validation = "/wEWCAKUiO2CDwKjsOvhAwKfi5rBAQLcnt3hDQKhp87FBALstcG6DALstdW6DALPqJWiCr" +
      "+MObqMi1rEbf5Os1De2cbfjkgR";
  final String generator = "5E06A1B7";
  final String button = "radio3";
  final String submit = "预  约";




  final String HU_YE = "9310056854";
  final String LU_ZHEN_YU = "9115049866";
  final String GU_JIAN_XIA = "9115055742";
  final String BOOKING_DATE = "2017-11-26";

  private final List<Pair<String, String>> MORNING = Arrays.asList(
      new Pair<>("0800", "9"),
      new Pair<>("0900", "10"),
      new Pair<>("1000", "11"),
      new Pair<>("1100", "12"));

  private final List<Pair<String, String>> AFTERNOON = Arrays.asList(
      new Pair<>("1230", "13"),
      new Pair<>("1330", "14"),
      new Pair<>("1430", "15"),
      new Pair<>("1530", "16"));

  private final List<Pair<String, String>> NIGHT = Arrays.asList(
      new Pair<>("1800", "19"),
      new Pair<>("1900", "20"),
      new Pair<>("2000", "21"),
      new Pair<>("2100", "22"));

  @PostConstruct
  public void booking() {
    bookingCoach(GU_JIAN_XIA, BOOKING_DATE, MORNING);
  }

  public void bookingCoach(String coach, String date, List<Pair<String, String>> pairs) {
    while (true) {

      pairs.forEach(t -> {
        try {
          getAsyncExecutor().execute(() -> {
            try {
              String a = buildBookingEndpoint().booking(
                  coach, date, t.getKey(), "场外", t.getValue(),
                  viewState, generator, validation, button, submit);
              if (a.contains("场外训练必须有2小时夜间训练")) {
                System.out.println("T1, it's working now!!!!!!!!!!!!!!!!!!!!!");
              } else {
                System.out.println("T1, it may  booking success.." + t.getKey());
              }
            } catch (Exception err) {
              System.out.println("Exception T1");
              err.printStackTrace();
            }
          });

          getAsyncExecutor2().execute(() -> {
            try {
              String a = buildBookingEndpointT2().booking(
                  coach, date, t.getKey(), "场外", t.getValue(),
                  viewState, generator, validation, button, submit);
              if (a.contains("场外训练必须有2小时夜间训练")) {
                System.out.println("T2, It's working now!!!!!!!!!!!!!!!!!!!!!");
              } else {
                System.out.println("T2, it may  booking success.." + t.getKey());
              }
            } catch (Exception error) {
              System.out.println("Exception .T2");
              error.printStackTrace();
            }
          });
        } catch (Exception err) {
        }
      });
    }
  }

  private static Executor getAsyncExecutor() {
    if (executor != null) {
      return executor;
    }
    synchronized (BookingService.class) {
      if (executor == null) {
        ThreadPoolTaskExecutor newExecutor = new ThreadPoolTaskExecutor();
        newExecutor.setCorePoolSize(5);
        newExecutor.setMaxPoolSize(15);
        newExecutor.setQueueCapacity(1000);
        newExecutor.setThreadGroupName("PaymentAsyncExecutor");
        newExecutor.setThreadNamePrefix("PaymentAsyncExecutorThread-");
        newExecutor.initialize();
        executor = newExecutor;
      }
      return executor;
    }
  }

  private static Executor getAsyncExecutor2() {
    if (executor2 != null) {
      return executor2;
    }
    synchronized (BookingService.class) {
      if (executor2 == null) {
        ThreadPoolTaskExecutor newExecutor = new ThreadPoolTaskExecutor();
        newExecutor.setCorePoolSize(5);
        newExecutor.setMaxPoolSize(15);
        newExecutor.setQueueCapacity(100000);
        newExecutor.setThreadGroupName("PaymentAsyncExecutor");
        newExecutor.setThreadNamePrefix("PaymentAsyncExecutorThread-2");
        newExecutor.initialize();
        executor2 = newExecutor;
      }
      return executor2;
    }
  }
}
