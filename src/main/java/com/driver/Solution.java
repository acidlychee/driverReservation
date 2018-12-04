package com.driver;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Solution {
  public static int lengthOfLongestSubstring(String s) {
    if (s == null || "".equals(s)) {
      return 0;
    }
    char[] chars = s.toCharArray();
    int current = 0;
    int max = 0;
    Map<Character, Integer> positionMap = new HashMap<>();
    for (int i = 0; i < chars.length; i++) {
      if (positionMap.containsKey(chars[i])) {
        int position = positionMap.get(chars[i]);
        if (current > max) {
          max = current;
        }
        current = i - position;
        positionMap.put(chars[i], i);
      } else {
        positionMap.put(chars[i], i);
        current++;
      }
    }
    return max > current?max: current;
  }
}
