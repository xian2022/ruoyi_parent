package com.atguigu.demo;

public class Sum {
    private static Integer sum = 0;
    public static void main(String[] args) {
        int[] nums = {1,2,3,4,5,6,7,8,9,10};
        getNum(nums,0);
        System.out.println(sum);
    }
    public static void getNum(int[] nums,int index) {
        if (nums.length<1) {
            return ;
        }
        if (index < nums.length) {
            sum += nums[index];
            getNum(nums,index+1);
        }
    }
}
