package com.hiekn.plantdata;

/**
 * xialianfu
 */
public class Sushu {

    /**
     * 判断100-200之间有多少素数 并输出所有素数
     *
     * 质数（prime number）又称素数，有无限个。 质数定义为在大于1的自然数中，除了1和它本身以外不再有其他因数，这样的数称为质数。
     * */
        public static void main(String[] args) {
            // 1.循环100-200
            for (int i = 100; i <= 200; i++) {

                // 调用isPrime()方法
                if (isPrime(i)) {
                    System.out.print(i + "\t");// 若isPrime()方法返回true,输出是素数
                }
            }
        }

        /**
         * 有参的方法
         * 1.首先判断数字不能小于2
         * 2.非素数一定可以由两个自然数相乘得到 一个大于或等于它的平方根，一个小于或等于它的平方根。并且成对出现。
         * 3.所以素数可以小于该数的平方根 减少循环的次数
         */
        public static boolean isPrime(int a) {

            boolean flag = true;
            // 素数不小于2
            if (a < 2) {
                return false;
            } else {
                //素数一定不可能由两个自然数相乘 所以i<变量的平方根
                for (int i = 2; i <= Math.sqrt(a); i++) {

                    // 若能被整除，则说明不是素数，返回false
                    if (a % i == 0) {

                        flag = false;
                        break;
                    }
                }
            }
            return flag;
        }
}
