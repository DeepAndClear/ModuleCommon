package com.xy.module.base.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Size;

import com.xy.module.base.BaseApplication;
import com.xy.module.base.R;
import com.xy.module.base.bean.MyDateBean;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/12/7.
 */

public class TimeUtil {
    /**
     * 解析 default 类型时间 (@see {@link TimeUtil#analysisStringTime(String, String)}
     */
    public static long analysisStringTime(String time) {
        return analysisStringTime(time, "yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 解析格式化完成的时间
     * @param time 格式化后的时间 默认(yyyy-MM-dd HH:mm:ss)
     * @param pattern 格式
     * @return 源类型
     */
    public static long analysisStringTime(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取 default 类型的时间 (@see {@link TimeUtil#formatLongTime(long, String)}
     */
    public static String formatLongTime(long time) {
        return formatLongTime(time,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 按照给定格式格式化时间 默认(yyyy-MM-dd HH:mm:ss)
     * @param time 带格式化时间
     * @param pattern 格式
     * @return 格式化后的时间
     */
    public static String formatLongTime(long time, String pattern) {
        return new SimpleDateFormat(pattern,Locale.getDefault()).format(new Date(time));
    }

    /**
     * 解析时间 （@see {@link TimeUtil#analysisStringTime(String)} 并转换为时差（@see {@link TimeUtil#transformToFrontTime(long)}
     * @param time 被格式化后的时间
     * @return 转换后的时间
     */
    public static String transformToFrontTime(String time) {
        long longTime = analysisStringTime(time);
        return transformToFrontTime(longTime);
    }

    /**
     * 获取相对时间 （距离现在的时间差）
     * @param longTime 源时间
     * @return 转换后的时间
     */
    public static String transformToFrontTime(long longTime) {
        long subTime = System.currentTimeMillis() - longTime;
        if (subTime / 1000 < 60) {
            //1min 内
            return subTime / 1000
                    + BaseApplication.getApplication()
                    .getString(R.string.base_front_second);
            // 1h 内
        } else if (subTime / 1000 / 60 < 60) {
            return subTime / 1000 / 60 + BaseApplication.getApplication()
                    .getString(R.string.base_front_minute);
            // 1d 内
        } else if (subTime / 1000 / 60 / 60 < 24) {
            return subTime / 1000 / 60 / 60 + BaseApplication.getApplication()
                    .getString(R.string.base_front_hour);
            // 1 月内
        }else if (subTime / 1000 / 60 / 60 / 24 < 30) {
            return subTime / 1000 / 60 / 60 + BaseApplication.getApplication()
                    .getString(R.string.base_front_day);
            // -月-日
        }else if (subTime / 1000 / 60 / 60 / 24 < 365){
            return formatLongTime(longTime,BaseApplication.getApplication()
                    .getString(R.string.format_day_month));
        } else {
            // -年-月-日
            return formatLongTime(longTime, BaseApplication.getApplication()
                    .getString(R.string.format_day_year));
        }
    }

    /**
     * 倒计时 格式化
     * @param time 时间差
     * @return 格式化后的时间
     */
    public static String countDownTime(long time,String overStr) {
        if (time < 60) {
            return BaseApplication.getApplication().getString(R.string.base_time_remaining) + "："
                    + getSecond(time);
        } else if (time / 60 < 60) {
            return BaseApplication.getApplication().getString(R.string.base_time_remaining) + "："
                    + getMinute(time);
        } else if (time / 60 / 60 < 24) {
            return BaseApplication.getApplication().getString(R.string.base_time_remaining) + "："
                    + getHour(time);
        } else if (time / 60 / 60 / 24 < 365) {
            return BaseApplication.getApplication().getString(R.string.base_time_remaining) + "：" + (time / 60 / 60 / 24)
                    + BaseApplication.getApplication().getString(R.string.base_time_day) + getHour(time);
        } else {
            return overStr;
        }
    }

    /**
     *
     * @param time
     * @return
     */
    private static String getSecond(long time) {
        DecimalFormat format = new DecimalFormat("00");
        return format.format(time);
    }

    private static String getMinute(long time) {
        DecimalFormat format = new DecimalFormat("00");
        return format.format(time / 60) + ":" + format.format(time % 60);
    }

    private static String getHour(long time) {
        DecimalFormat format = new DecimalFormat("00");
        return format.format(time / 60 / 60 % 24) + ":"
                + format.format(time / 60 % 60) + ":"
                + format.format(time % 60);
    }


    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;

    }

    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static float getTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        float time = (hour * 60 * 60) + (minute * 60) + second;
        return time;
    }

    public static List<MyDateBean> getSevenDate(Date curDate) {
        String[] months = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        List<MyDateBean> dateList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        for (int i = -7, j = 0; i < 8; i++) {
            int day = c.get(Calendar.DAY_OF_MONTH) + i - j;
            if (day < 0) {
                month--;
                j = i;
                if (month < 1) {
                    month = 12;
                    year--;
                }
                c.clear();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month - 1);
                day = MaxDayFromDay_OF_MONTH(year, month) + day;
                c.set(Calendar.DAY_OF_MONTH, day);
            }
            if (day > MaxDayFromDay_OF_MONTH(year, month)) {
                month++;
                j = i;
                if (month > 12) {
                    month = 1;
                    year++;
                }
                c.clear();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month - 1);
                day = c.get(Calendar.DAY_OF_MONTH);
                c.set(Calendar.DAY_OF_MONTH, day);
            }
//            dateList.add(new Date(year,month,day));
            String week = computeWeek(year + "", month + "", day + "");
            String date = year + "-" + formatDayAndMonth(month) + "-" + formatDayAndMonth(day);

            dateList.add(new MyDateBean(formatDayAndMonth(day) + "", week, months[month - 1], date));
        }
        return dateList;
    }

    public static int MaxDayFromDay_OF_MONTH(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        return day;
    }

    public static String formatDayAndMonth(int value) {
        DecimalFormat format = new DecimalFormat("##00");
        return format.format(value);
    }


    public static String computeWeek(long time) {
        Date date = new Date(time);
        String year = TimeUtil.getYear(date) + "";
        String month = TimeUtil.getMonth(date) + "";
        String day = TimeUtil.getDay(date) + "";
        return computeWeek(year, month, day);
    }

    /**
     * 计算星期
     *
     * @param yearStr
     * @param monthStr
     * @param dayStr
     * @return
     */
    //
    public static String computeWeek(String yearStr, String monthStr, String dayStr) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);
        int day = Integer.parseInt(dayStr);
        Log.e("TAG", "------FORT-----》》" + year + "        " + month +/*"      "+week+*/"     " + day);
        String week = null;

        if (month == 1 || month == 2) {

        }

        int c = year / 100;
        int d = day;
        int y = year % 100;
        int m = month;
        if (m == 1 || m == 2) {
            y--;
            m = month + 12;
        }
        // 运用Zeller公式计算星期
        int w = (y + (y / 4) + (c / 4) - 2 * c + (26 * (m + 1) / 10) + d - 1) % 7;
        while (w < 0) {
            w += 7;
        }
//        if (month == 0 || month == 1) {
//            w += 2;
//        }
        if (w >= 7) {
            w = w % 7;
        }
        return weeks[w];

    }

    public static boolean compareSameDay(long o, long y) {
        String u = formatLongTime(o, "yyyyMMdd");
        String x = formatLongTime(y, "yyyyMMdd");
        if (u.equals(x)) {
            return true;
        }
        return false;
    }


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    public static long getLongDate(long time) {
        // 解析给定的值
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        // 清空结果单位
        Calendar date = Calendar.getInstance();
        date.clear();

        // 设置结果值
        date.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        return date.getTimeInMillis();
    }

    public static String getTimeOfMonth(long time) {
        String curYear = formatLongTime(System.currentTimeMillis(), "yyyy");
        String year = formatLongTime(time, "yyyy");
        if (curYear.equals(year)) {
            return formatLongTime(time, BaseApplication.getApplication().getString(R.string.format_time_month));
        } else {
            return formatLongTime(time, BaseApplication.getApplication().getString(R.string.format_day_year));
        }
    }

    public static String getDayOfMonth(long time) {
        String curYear = formatLongTime(System.currentTimeMillis(), "yyyy");
        String year = formatLongTime(time, "yyyy");
        if (curYear.equals(year)) {
            return formatLongTime(time, BaseApplication.getApplication().getString(R.string.format_day_month));
        } else {
            return formatLongTime(time, BaseApplication.getApplication().getString(R.string.format_day_year));
        }
    }

    /**
     * @param startTime 开启时间
     * @param endTime   结束时间
     * @param desc      0： 尚未开始（开始时间错误）
     *                  1： 等待开始（开始时间正确并小于1月显示倒计时）
     *                  2： 等待结束 （结束时间正确 并小于1月显示倒计时）
     *                  3： 等待开始（开始时间正确 大于1月显示具体时间）
     *                  4： 等待结束 (结束时间正确 大于1月显示具体时间）
     *                  5： 已经结束
     * @return 时间
     */
    public static String getCountDownTime(long startTime, long endTime, @Size(6) String... desc) throws RuntimeException{
        long curTime = System.currentTimeMillis();
        if (startTime > curTime) {
            long time = startTime - curTime;
            if (time < 0) {
                throw new RuntimeException(desc[0]);
            } else if (time < 60) {
                return String.format(getDesc(desc[1]), getSecond(time));
            } else if (time / 60 < 60) {
                return String.format(getDesc(desc[1]), getMinute(time));
            } else if (time / 60 / 60 < 24) {
                return String.format(getDesc(desc[1]), getHour(time));
            } else if (time / 60 / 60 / 24 < 30) {
                return String.format(getDesc(desc[1]), (time / 60 / 60 / 24)
                        + BaseApplication.getApplication().getString(R.string.base_time_day)
                        + getHour(time));
            } else {
                throw new RuntimeException(String.format(desc[3], getDayOfMonth(startTime)));
            }
        }
        if (curTime >= endTime) {
            throw new RuntimeException(desc[5]);
        }
        long time = endTime - curTime;
        if (time < 0) {
            return desc[0];
        } else if (time < 60) {
            return String.format(getDesc(desc[2]), getSecond(time));
        } else if (time / 60 < 60) {
            return String.format(getDesc(desc[2]), getMinute(time));
        } else if (time / 60 / 60 < 24) {
            return String.format(getDesc(desc[2]), getHour(time));
        } else if (time / 60 / 60 / 24 < 30) {
            return String.format(getDesc(desc[2]), (time / 60 / 60 / 24)
                    + BaseApplication.getApplication().getString(R.string.base_time_day)
                    + getHour(time));
        } else {
            throw new RuntimeException(String.format(desc[4], getDayOfMonth(startTime)));
        }
    }

    private static String getDesc(String desc) {
        if (desc == null || desc.isEmpty()) {
            return BaseApplication.getApplication().getString(R.string.base_time_remaining);
        } else {
            return desc;
        }
    }

}
