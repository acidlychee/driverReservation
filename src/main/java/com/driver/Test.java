package com.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.abs;

public class Test {

  private static int moves = 0;

  private static void calcMoves(int a, int b){
    if (a >= 10)
      calcMoves(a / 10, b / 10);

    int ai = a % 10;
    int mi = b % 10;

    moves += abs(ai - mi);
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    int n = input.nextInt();
    List<Integer> an = new ArrayList<Integer>();
    for (int i = 0; i < n; i++) {
      an.add(input.nextInt());
    }

    int m = input.nextInt();
    List<Integer> bn = new ArrayList<Integer>();
    for (int i = 0; i < m; i++) {
      bn.add(input.nextInt());
    }

    for (int i = 0; i < an.size(); i++) {
      calcMoves(an.get(i), bn.get(i));
    }
    System.out.println(moves);
  }
}