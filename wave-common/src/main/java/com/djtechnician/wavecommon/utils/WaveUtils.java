package com.djtechnician.wavecommon.utils;

public class WaveUtils {
  public static Integer base36ToDecimal(String base36) {
    try {
      if (base36.length() != 2) {
        throw new RuntimeException("base36 should be 두자릿수 : " + base36);
      }
      base36 = base36.toUpperCase();
      char first = base36.charAt(1);
      int firstNum;
      char second = base36.charAt(0);
      int secondNum;
      if (Character.isDigit(first)) {
        firstNum = Character.getNumericValue(first);
      } else if (Character.isAlphabetic(first)) {
        firstNum = first - 'A' + 10;
      } else {
        throw new RuntimeException("first should be numeric or alphabetic : " + first);
      }
      if (Character.isDigit(second)) {
        secondNum = Character.getNumericValue(second);
      } else if (Character.isAlphabetic(second)) {
        secondNum = second - 'A' + 10;
      } else {
        throw new RuntimeException("second should be numeric or alphabetic : " + second);
      }
      return firstNum + secondNum * 36;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error to parse base36toDecimal");
    }
  }
}
