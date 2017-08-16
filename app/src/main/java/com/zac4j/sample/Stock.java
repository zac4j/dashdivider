package com.zac4j.sample;

/**
 * Stock Model
 * Created by Zaccc on 2017/8/15.
 */

public class Stock {

  private String name;
  private String code;

  public Stock(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
