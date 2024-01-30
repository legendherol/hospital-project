package com.ruoyi.hospital.enu;

import java.util.Calendar;
import java.util.Date;

public enum WorkTimeEnum {
    MONDAY_AM(1), MONDAY_PM(2),
    TUESDAY_AM(3), TUESDAY_PM(4),
    WEDNESDAY_AM(5), WEDNESDAY_PM(6),
    THURSDAY_AM(7), THURSDAY_PM(8),
    FRIDAY_AM(9), FRIDAY_PM(10),
    SATURDAY_AM(11), SATURDAY_PM(12),
    SUNDAY_AM(13), SUNDAY_PM(14);

    private final int value;

    WorkTimeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int getCurrentWorkTimeEnum(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int period = cal.get(Calendar.HOUR_OF_DAY) >= 12 ? 2 : 1; // 上午：1，下午：2
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return period == 1 ? WorkTimeEnum.MONDAY_AM.getValue() : WorkTimeEnum.MONDAY_PM.getValue();
            case Calendar.TUESDAY:
                return period == 1 ? WorkTimeEnum.TUESDAY_AM.getValue() : WorkTimeEnum.TUESDAY_PM.getValue();
            case Calendar.WEDNESDAY:
                return period == 1 ? WorkTimeEnum.WEDNESDAY_AM.getValue() : WorkTimeEnum.WEDNESDAY_PM.getValue();
            case Calendar.THURSDAY:
                return period == 1 ? WorkTimeEnum.THURSDAY_AM.getValue() : WorkTimeEnum.THURSDAY_PM.getValue();
            case Calendar.FRIDAY:
                return period == 1 ? WorkTimeEnum.FRIDAY_AM.getValue() : WorkTimeEnum.FRIDAY_PM.getValue();
            case Calendar.SATURDAY:
                return period == 1 ? WorkTimeEnum.SATURDAY_AM.getValue() : WorkTimeEnum.SATURDAY_PM.getValue();
            case Calendar.SUNDAY:
                return period == 1 ? WorkTimeEnum.SUNDAY_AM.getValue() : WorkTimeEnum.SUNDAY_PM.getValue();
            default:
                throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        }
    }
}