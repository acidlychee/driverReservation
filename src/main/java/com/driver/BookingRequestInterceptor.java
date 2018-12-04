package com.driver;

import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import feign.RequestTemplate;


@AllArgsConstructor
public class BookingRequestInterceptor implements feign.RequestInterceptor {


  @Override
  public void apply(RequestTemplate template) {
    Map<String, Collection<String>> headers = Maps.newHashMap();
    headers.put("Content-Type", Arrays.asList("application/x-www-form-urlencoded"));
    headers.put("Cookie", Arrays.asList("ASP.NET_SessionId=34eupvmlpxwgp455hp5zpk55; " +
        "Hm_lvt_49ecdefc6bc4a0afbe59cbf51212146c=1507954265,1508558777,1509682030,1510286873; Hm_lpvt_49ecdefc6bc4a0afbe59cbf51212146c=1510373538"));
    template.headers(headers);
  }
}

