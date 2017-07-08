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
    headers.put("Cookie", Arrays.asList("ASP.NET_SessionId=kfkp5fa5t3vbkh55iomcbe3i; " +
        "Hm_lvt_49ecdefc6bc4a0afbe59cbf51212146c=1496895734,1497587131,1498795449,1499486104; Hm_lpvt_49ecdefc6bc4a0afbe59cbf51212146c=1499486104"));
    template.headers(headers);
  }
}

