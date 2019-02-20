package com.zjrb.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类
 * Created by wangzhen on 16/8/2.
 */
public class TimeUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat mFormater = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);

    public static String getTime(long time, String format) {
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format, Locale.CHINA);
        try {
            Date dateParam = new Date(time);
            result = formater.format(dateParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 积分明细列表日期格式
     *
     * @param time
     * @param format
     * @return
     */
    public static String getScoreDetailFormat(long time, String format) {
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        Date currDate = new Date();
        Date date = new Date(time);

        //日期之差
        long diff = currDate.getTime() - date.getTime();
        //天
        int day = (int) (diff / (1000 * 60 * 60 * 24));

        String formatCurrDate = formater.format(currDate);
        String formatParamDate = formater.format(date);

        L.e("日期：" + formatCurrDate + " -> " + formatParamDate);

        if (diff < 0) {
            //如果当前日期比指定日期晚，默认显示为当天
            result = "今天";
        } else if (formatCurrDate.equals(formatParamDate)) {
            //判断是否为同一天
            result = "今天";
        } else if (day <= 1) {
            //判断是否为昨天
            result = "昨天";
        } else {
            result = formater.format(date);
        }
        return result;
    }

    /**
     * 带周几
     *
     * @param time
     * @param format
     * @return
     */
    public static String getDateWithWeekend(long time, String format) {
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        Date currDate = new Date();
        Date date = new Date(time);

        //日期之差
        long diff = currDate.getTime() - date.getTime();
        //天
        int day = (int) (diff / (1000 * 60 * 60 * 24));

        if (diff < 0) {
            //如果当前日期比指定日期晚，默认显示为当天
            result = "今天";
        } else if (formater.format(currDate).equals(formater.format(date))) {
            //判断是否为同一天
            result = "今天";
        } else if (day <= 1) {
            //判断是否为昨天
            result = "昨天";
        } else if (day < 7) {
            //判断是否为一周内
            result = getWeekOfDate(date);
        } else if (day >= 7) {
            //判断是否为一周外
            result = formater.format(date);
        }

        return result;
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 追加0
     *
     * @param time
     * @return
     */
    private static String appendZero(int time) {
        String value = String.valueOf(time);
        if (value.length() == 1) {
            return "0" + value;
        } else {
            return value;
        }
    }


    /**
     * 时间标示规则
     * 1.	1分钟内发布的，显示为“刚刚”
     * 2.	1min＜发布时间＜1h，显示为“xx分钟前”
     * 3.	1h＜发布时间＜1d，显示为“xx小时前”
     * 4.	1d＜发布时间＜2d，显示为“昨天”
     * 5.	2d＜发布时间＜3d，显示为“前天”
     * 6.	3d＜发布时间，显示为“x月x日”
     *
     * @param date
     * @return
     */
    public static String getFriendlyTime(long date) {
        Date time;

        if (TimeZoneUtils.isInEasternEightZones())
            time = new Date(date);
        else
            time = TimeZoneUtils.transformTime(new Date(date),
                    TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());

        Date curDate = new Date();
        long diffTime = curDate.getTime() - time.getTime();
        if (diffTime < 1 * 60 * 1000) // 1分钟内发布的
            return "刚刚";

        if (diffTime < 60 * 60 * 1000) // 1min＜发布时间＜1h
            return diffTime / (1 * 60 * 1000) + "分钟前";

        if (diffTime < 24 * 60 * 60 * 1000) {
            return diffTime / (60 * 60 * 1000) + "小时前";
        }

        if (time.getTime() > getYesterdayStartTime()) { // 1d＜发布时间＜2d，显示为“昨天”
            return "昨天";
        }

        if (time.getTime() > getDayBeforeStartTime()) { // 2d＜发布时间＜3d
            return "前天";
        }

        return new SimpleDateFormat("MM月dd日").format(time);
    }

    /**
     * 返回long类型的昨天零点的时间
     *
     * @return Long
     */
    private static long getYesterdayStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            return sdf.parse(sdf.format(cal.getTime())).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal.getTime().getTime();
    }

    /**
     * 返回long类型的前一天零点的时间
     *
     * @return Long
     */
    private static long getDayBeforeStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            return sdf.parse(sdf.format(cal.getTime())).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal.getTime().getTime();
    }

    /**
     * 判断前一天是否打开过APP
     *
     * @param lastLogin
     * @return true 不跳转24小时日报  false 跳转24小时日报
     */
    public static boolean isYesterdayOpened(long lastLogin) {
        //当前日期
        Date currDate = new Date();
        //上次打开日期
        Date lastDate = new Date(lastLogin);
        //如果当前日期小于上次打开日期，存在手机时间被手动修改的可能
        if (lastDate.after(currDate)) return true;
        //
        //日期之差
        long diff = currDate.getTime() - lastDate.getTime();
        //24小时毫秒数
        long h24 = 24 * 60 * 60 * 1000;
        return diff <= h24;
    }

    /**
     * 格式化视频时长
     *
     * @param duration 时长 单位：s
     * @return 格式化
     */
    public static String formateVideoDuration(int duration) {
        if (duration < 0) {
            return "--:--";
        }
        int seconds = duration % 60;
        int minutes = (duration / 60) % 60;
        int hours = duration / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String
                .format("%02d:%02d", minutes, seconds);
    }

}
