package au.com.team2media.service;


import com.mongodb.DBCursor;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 13/02/15
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeetingsTest {

    @Test
    public void testMeetingsExist() {
        MeetingService service = new MeetingService();
        DBCursor cursor = service.getAllMeetings();
        Assert.assertTrue(cursor.size() > 0);
    }
}
