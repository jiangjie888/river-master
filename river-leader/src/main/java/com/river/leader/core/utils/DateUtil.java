package com.river.leader.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 */
public class DateUtil {

    private static DateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 按照指定格式返回日期字符串
     * @param date
     *          需要格式化的日期
     * @param format
     *          格式化字符串，为空则采用默认  yyyy-MM-dd hh:mm:ss。如果格式化字符串不合法会抛出异常
     * @return
     *          格式化后的日期字符串。
     */
    public static String formatDate(Date date , String format){
        if(date == null)
            return null;
        String str_date = null;
        if(format != null){
            DateFormat formater = new SimpleDateFormat(format);
            str_date = formater.format(date);
        }else{
            str_date =datefmt.format(date);
        }

        return str_date;
    }

    public static Date parseToDate(String date , String format) throws ParseException{
        if(date == null)
            return null;
        Date dDate = null;
        if(format != null){
            DateFormat formater = new SimpleDateFormat(format);
            dDate = formater.parse(date);
        }else{
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            dDate =formater.parse(date);
        }

        return dDate;
    }

    /**
     * 比较两个时间是否相等。
     * @param d1  
     *          时间1
     * @param d2
     *          时间2 
     * @return
     *          相等则true。因为数据库中读出的数据为Timestamp类型(Date的子类)，
     * 当它与Date类型进行比较时,总是为false,即使是同一个时间.因此写了这个方法,用于兼容这两种类型的时间比较.
     */
    public static boolean equalsDate(Date d1 , Date d2){
        if(d1 !=null && d2!= null){
            return d1.getTime() == d2.getTime();
        }
        return false;
    }

    /**
     * 判断后面的一天是否是前面一天的下一天
     * @param day   
     *          基准日期
     * @param nextDay
     *          比较日期
     * @return
     *          如果比较日期是基准日期的下一天则返回true，否则为false
     */
    public static boolean isNextDay(Date day,Date nextDay){
        return ( getBetweenDays(day ,nextDay) == -1 );
    }

    /**
     * 判断两个日期是否是同一天
     * @author slx
     * @date 2009-11-10 下午04:32:07
     * @modifyNote
     * @param day
     * @param otherDay
     * @return
     */
    public static boolean isSameDay(Date day,Date otherDay){
        return ( getBetweenDays(day ,otherDay) == 0 );
    }

    /**
     * 计算两个日期相差的天数.不满24小时不算做一天
     * @param fDate     日期1
     * @param oDate     日期2
     * @return
     *      日期1 - 日期2 的差
     */
    public static int getBetweenDays(Date fDate, Date sDate) {
        int day=(int)((fDate.getTime()-sDate.getTime())/86400000L);//(24小时 * 60分 * 60秒 * 1000毫秒 = 1天毫秒数) 
        return day;
    }

    /**
     * 日期相加指定年
     * @param date
     *      日期
     * @param addYears
     *      要添加的年数
     * @return
     *      相加后的日期
     */
    public static Date addYears(Date date , int addYears){
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.YEAR, addYears);
        return calender.getTime();
    }
    /**
     * 加指定月
     * @param date
     *      日期
     * @param addMonths
     *      月数
     * @return
     *      相加后的日期
     */
    public static Date addMonth(Date date , int addMonths){
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, addMonths);
        return calender.getTime();
    }

    /**
     * 加指定天数
     * @param date
     *      日期
     * @param addDays
     *      天数
     * @return
     *      相加后的日期
     */
    public static Date addDay(Date date , int addDays){
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.DAY_OF_YEAR, addDays);
        return calender.getTime();
    }

    /**
     * 得到一年的第一天
     * @param year
     *      年
     * @return
     *      一年的第一天
     */
    public static Date getFirstDateOfYear(int year){
        Calendar calender = Calendar.getInstance();
        calender.set(Calendar.YEAR,year);
        calender.set(Calendar.DAY_OF_YEAR, calender.getActualMinimum(Calendar.DAY_OF_YEAR));
        setStartTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 得到一年的最后一天
     * @param year
     *      年
     * @return
     *      一年的最后一天
     */
    public static Date getLastDateOfYear(int year){
        Calendar calender = Calendar.getInstance();
        calender.set(Calendar.YEAR,year);
        calender.set(Calendar.DAY_OF_YEAR, calender.getActualMaximum(Calendar.DAY_OF_YEAR));
        setEndTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 判断当前日期是否是所在月份的最后一天
     * @param date
     *      日期
     * @return
     *      是最后一天为 true
     */
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int lastDay = calender.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day == lastDay ;
    }

    /**
     * 得到指定月的最后一天
     * @param year
     *      年
     * @param month
     *      月
     * @return
     *      最后一天
     */
    public static Date getLastDayOfMonth(int year , int month){
        Calendar calender = Calendar.getInstance();
        calender.set(year, month-1, 1);
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH));
        setEndTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 得到日期所在月的最后一天
     * @param date
     *      日期
     * @return
     *      所在月的最后一天
     */
    public static Date getLastDayOfMonth(Date date){
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH));
        setEndTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 设置到当前月的最后时刻
     * @param calender
     */
    private static void setEndTimeOfDay(Calendar calender){
        calender.set(Calendar.HOUR_OF_DAY, calender.getActualMaximum(Calendar.HOUR_OF_DAY));
        calender.set(Calendar.MINUTE, calender.getActualMaximum(Calendar.MINUTE));
        calender.set(Calendar.SECOND, calender.getActualMaximum(Calendar.SECOND));
        calender.set(Calendar.MILLISECOND, calender.getActualMaximum(Calendar.MILLISECOND));
    }

    /**
     * 得到指定月的第一天
     * @param year
     *      年
     * @param month
     *      月
     * @return
     *      第一天
     */
    public static Date getFirstDayOfMonth(int year , int month){
        Calendar calender = Calendar.getInstance();
        calender.set(year, month-1, 1);
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMinimum(Calendar.DAY_OF_MONTH));
        setStartTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 得到指定日期所在月的第一天
     * @param date
     *      日期
     * @return
     *      第一天
     */
    public static Date getFirstDayOfMonth(Date date){
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMinimum(Calendar.DAY_OF_MONTH));
        setStartTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 设置到月份开始的时刻
     * @param calender
     */
    private static void setStartTimeOfDay(Calendar calender){
        calender.set(Calendar.HOUR_OF_DAY, calender.getActualMinimum(Calendar.HOUR_OF_DAY));
        calender.set(Calendar.MINUTE, calender.getActualMinimum(Calendar.MINUTE));
        calender.set(Calendar.SECOND, calender.getActualMinimum(Calendar.SECOND));
        calender.set(Calendar.MILLISECOND, calender.getActualMinimum(Calendar.MILLISECOND));
    }

    public static Date getStartTimeOfDay(Date date){
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        setStartTimeOfDay(calender);
        return calender.getTime();
    }

    public static Date getEndTimeOfDay(Date date){
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        setEndTimeOfDay(calender);
        return calender.getTime();

    }

    /**
     * 得到当前年月
     * 
     * @author yongtree
     * @date 2008-11-22 上午11:25:24
     * @return 格式：2008-11
     * @throws ParseException
     */
    public static  String getThisYearMonth() throws ParseException {
        return getYearMonth(new Date());
    }

    /**
     * 得到年月
     * @return 格式：2008-11
     * @throws ParseException
     */
    public static  String  getYearMonth(Date date){
        Calendar today = Calendar.getInstance();
        today.setTime(date);
        return (today.get(Calendar.YEAR)) + "-" + ((today.get(Calendar.MONTH)+1)>=10?(today.get(Calendar.MONTH)+1):("0"+(today.get(Calendar.MONTH) + 1)));
    }

    /**
     * 计算两个日期之间相差的月份数
     * <br> 日期顺序不分先后不会返回负数
     * <br> 不足一个月不算做一个月
     * @param date1
     *      日期1
     * @param date2
     *      日期2
     * @return
     *      月数
     */
    public static int getBetweenMonths(Date date1, Date date2){      
        int iMonth = 0;      
        int flag = 0;      
        Calendar objCalendarDate1 = Calendar.getInstance();      
        objCalendarDate1.setTime(date1);      

        Calendar objCalendarDate2 = Calendar.getInstance();      
        objCalendarDate2.setTime(date2);      

        if (objCalendarDate2.equals(objCalendarDate1))      
            return 0;      
        if (objCalendarDate1.after(objCalendarDate2)){      
            Calendar temp = objCalendarDate1;      
            objCalendarDate1 = objCalendarDate2;      
            objCalendarDate2 = temp;      
        }      
        if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1.get(Calendar.DAY_OF_MONTH))      
            flag = 1;      

        if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR))      
            iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR))      
                    * 12 + objCalendarDate2.get(Calendar.MONTH) - flag)      
                    - objCalendarDate1.get(Calendar.MONTH);      
        else     
            iMonth = objCalendarDate2.get(Calendar.MONTH)      
                    - objCalendarDate1.get(Calendar.MONTH) - flag;      

        return iMonth;      
    }

    /**
     * 计算两个日期之间相差的年份数
     * <br> 日期顺序不分先后不会返回负数
     * <br> 不足一个年不算做一个年
     * @param date1
     *      日期1
     * @param date2
     *      日期2
     * @return
     *      年数
     */
    public static int getBetweenYears(Date date1, Date date2){  
        return getBetweenMonths(date1 ,date2) / 12;
    }

    /*public static void main(String[] args) throws Exception {
//      Date d1 = parseToDate("2009-11-29", null);
//      Date d2 = parseToDate("2007-12-29", null);
        System.out.println(formatDate(getFirstDayOfMonth(2010,10),"yyyy-MM-dd HH:mm:ss.SSS"));

        System.out.println(formatDate(getLastDateOfYear(2009),"yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(formatDate(getFirstDateOfYear(2009),"yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(formatDate(getEndTimeOfDay(new Date()),"yyyy-MM-dd HH:mm:ss.SSS"));
    }*/
}