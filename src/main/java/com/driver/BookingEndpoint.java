package com.driver;

import feign.Feign;
import feign.Logger;
import feign.codec.ErrorDecoder;
import feign.codec.StringDecoder;
import feign.form.FormEncoder;


public class BookingEndpoint {

  static final String BASE_URL = "http://t1.ronganjx.com/";

  public static final Object ENDPOINT = buildEndpoint(Booking.class);

  public Booking buildBookingEndpoint() {
    return (Booking) ENDPOINT;
  }

  static Object buildEndpoint(Class<?> clazz) {
    return Feign.builder()
        .encoder(new FormEncoder())
        .decoder(new StringDecoder())
        .errorDecoder(new ErrorDecoder.Default())
        .requestInterceptor(new BookingRequestInterceptor())
        .logLevel(Logger.Level.FULL)
        .target(clazz, BASE_URL);
  }
}
