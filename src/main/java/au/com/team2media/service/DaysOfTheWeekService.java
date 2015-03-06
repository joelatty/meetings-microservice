package au.com.team2media.service;

import au.com.team2media.util.DayOfWeekUtil;
import com.google.gson.Gson;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 6/03/15
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DaysOfTheWeekService {

    private static final DayOfWeekUtil dayOfWeekUtil = new DayOfWeekUtil();

    public String  getDaysOfTheWeek() {
        return new Gson().toJson(dayOfWeekUtil.getDaysOfTheWeek());
    }
}
