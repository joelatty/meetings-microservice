<!DOCTYPE html>
<html>
<head>
    <title>Thank you for your input</title>
 </head>
<body>
    <p>Thank you for entering a new meeting named ${headers.meeting.name}</p>
    <p>It will be on at ${headers.meeting.startTime}</p>
    <p>And end at ${headers.meeting.endTime}</p>
    ${body}
</body>
</html>