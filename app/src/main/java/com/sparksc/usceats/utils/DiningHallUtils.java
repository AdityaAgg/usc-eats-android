package com.sparksc.usceats.utils;

import java.util.Calendar;

/**
 * A Utils class to maintain consistency with dining hall items across different activities and
 * fragments.
 */
public class DiningHallUtils {

    public static String DINING_HALL_TYPE = "diningHallType";
    public static String MEAL_TIME = "mealTime";
    public static String EVK_STRING = "EVK";
    public static String PARKSIDE_STRING = "Parkside";
    public static String CAFE_84_STRING = "Cafe 84";

    public enum DiningHallType {
        EVK, PARKSIDE, CAFE_84
    }

    public enum MealTime {
        BREAKFAST, LUNCH, DINNER, BRUNCH
    }

    public static MealTime getCurrentMealTime() {
        if (!isWeekend()) {
            switch (CalendarUtils.getCurrentHour()) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    return MealTime.BREAKFAST;
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                    return MealTime.LUNCH;
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                    return MealTime.DINNER;
                default:
                    return MealTime.DINNER;
            }
        } else {
            switch (CalendarUtils.getCurrentHour()) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    return MealTime.BRUNCH;
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                    return MealTime.DINNER;
                default:
                    return MealTime.DINNER;
            }
        }
    }

    /**
     * Menus will appear in different ways (brunch & dinner as opposed to breakfast, lunch, &
     * dinner) depending on if it is a weekend. Also, Cafe 84 is closed on weekends.
     * @return True, if the current time dictates the date is a weekend. Otherwise, false.
     */
    public static boolean isWeekend() {
        switch (CalendarUtils.getDayOfWeek()) {
            case Calendar.SUNDAY:
            case Calendar.SATURDAY:
                return true;
            default:
                return false;
        }
    }
}
