package com.driver;

import feign.Headers;
import feign.Param;
import feign.RequestLine;


public interface Booking {

  @RequestLine("POST Web11/logging/BookingCWStudy" +
      ".aspx?coachName={coachName}&date={date}&beginTime={beginTime}&trainType={trainType" +
      "}&timeLine={timeLine}")
  @Headers("Content-Type: application/x-www-form-urlencoded")
  String booking(@Param("coachName") String coachName,
                 @Param("date") String date,
                 @Param("beginTime") String beginTime,
                 @Param("trainType") String trainType,
                 @Param("timeLine") String timeLine,
                 @Param("__VIEWSTATE") String viewState,
                 @Param("__VIEWSTATEGENERATOR") String generator,
                 @Param("__EVENTVALIDATION") String validation,
                 @Param("ctl00$ContentPlaceHolder2$time") String button,
                 @Param("ctl00$ContentPlaceHolder2$btnSubmit") String sumbit);
}
