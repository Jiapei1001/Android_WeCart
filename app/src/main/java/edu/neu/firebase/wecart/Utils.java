package edu.neu.firebase.wecart;

import java.util.Calendar;

public class Utils {
    public static String showGreetingWordsByCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay < 12) {
            return "Good Morning";
        } else if (timeOfDay < 17) {
            return "Good Afternoon";
        } else {
            return timeOfDay < 21 ? "Good Evening" : "Good Night";
        }
    }
}
