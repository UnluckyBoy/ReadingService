package com.cloudstudio.readingservice.tool;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @ClassName TimeUtil
 * @Author Create By matrix
 * @Date 2024/8/28 7:59
 */
public class TimeUtil {
    /**
     *获取当前时间
     * @param add_hour 是否添加小时
     * @return
     */
    public static String GetTime(boolean add_hour){
        //获取当前系统时间
        long time=System.currentTimeMillis();
        //new日期对象
        Date date =new Date(time);
        //转换提日期输出格式
        if(add_hour){
            SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String st = dateFormat.format(date);
            return st;
        }else{
            SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
            String st = dateFormat.format(date);
            return st;
        }
    }

    /**
     * 去掉时间格式
     * yyyy-MM-dd变成YYYYMMDD
     * @param time
     * @return
     */
    public static String timeToString(String time){
        return time.replaceAll("[- :]","");//通过replaceAll将"-"、" "、":"替换为""
    }

    /**
     * 时间格式设置
     * @param time
     * @return
     */
    private static LocalDate setTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(time, formatter);
        return date;
    }

    /**
     * 平，润年计算
     * @param time
     * @return
     */
    public static boolean LeapYearChecker(String time){
        boolean isLeapYear = LocalDate.of(setTime(time).getYear(), 1, 1).isLeapYear(); // 判断是否为闰年
        return isLeapYear;
    }

    /**
     * 大小月计算
     * @param time
     * @return
     */
    public static boolean MonthSizeChecker(String time){
        Month monthEnum = Month.of(setTime(time).getMonthValue()); // 将月份转换为枚举类型
        if (monthEnum.length(false) == 31) {
            //System.out.println(" 大月");
            return true;
        } else {
            //System.out.println(" 小月");
            return false;
        }
    }
}
