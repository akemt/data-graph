package com.hiekn.plantdata;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.stream.IntStream;

public class test {

    public static void main(String args[]) {
        test(6);

    }

    public static void test(int i) {
        if(i<= 1 || i>=10) {
            return;
        }  else if ( i%2 == 0 ){
            System.out.println("try:" + i);
            test(i-2);
        } else {
            test(i+1);
            System.out.println("try:" + i);
        }
    }

}
