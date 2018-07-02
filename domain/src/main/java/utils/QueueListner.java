package utils;

import model.User;

import org.apache.log4j.Logger;

public class QueueListner {
    private static final Logger LOG = Logger.getLogger(QueueListner.class);

    public void doProcess(User message) throws Exception {
        LOG.info("Message Read From TestQueue : " + message.toString());
    }
}