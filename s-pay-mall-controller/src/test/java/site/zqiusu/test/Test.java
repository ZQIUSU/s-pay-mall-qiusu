package site.zqiusu.test;

public class Test {

    @org.junit.Test
    public void test(){
        int[] nums = new int[]{1,1,1,2,2,3};
        int n = nums.length;
        int slow = 2, fast = 2;
        while (fast < n) {
            if (nums[slow - 2] != nums[fast]) {
                nums[slow] = nums[fast];
                ++slow;
            }
            ++fast;
        }
    }



}
