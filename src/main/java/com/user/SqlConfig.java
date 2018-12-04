package com.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlConfig {
  private boolean isParamSQL;
  private boolean saveAsCsv;
  private String fileName;
  private String paramName;
}
