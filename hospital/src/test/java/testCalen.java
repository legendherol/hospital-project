import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class testCalen {
    public static void main(String[] args) {
        // 获取今天是星期几
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0); // 小时设置为0
        today.set(Calendar.MINUTE, 0); // 分钟设置为0
        today.set(Calendar.SECOND, 0); // 秒设置为0
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK); // 获取星期几
        System.out.println(dayOfWeek);
        int offset = dayOfWeek - Calendar.MONDAY; // 计算星期一的偏移量
        System.out.println(offset);
        today.add(Calendar.DATE, -offset); // 设置为星期一的日期
        Date monday = today.getTime(); // 获取星期一时间
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(monday));

        today.add(Calendar.DATE, 6); // 下一个周日的日期
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(today.getTime()));
//        today.add(Calendar.DATE,-13);
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(today.getTime()));
//        today.add(Calendar.DATE,-13);
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(today.getTime()));
        Date sunday = today.getTime();
        today.add(Calendar.DATE, 7);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(today.getTime()));
    }
}
