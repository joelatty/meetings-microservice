package au.com.team2media.model;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 13/02/15
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeetingTest {

    public MeetingBean whatSuburb(Meeting meeting) {

        MeetingBean bean = new MeetingBean();
        if(meeting.getSuburb().equals("Newtown")) {
            bean.setArea("Inner West");
            bean.setType("Speaker");
        }

        return bean;
    }

}
