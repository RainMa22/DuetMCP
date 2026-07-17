package me.rainma22.DuetMCP;

import java.text.MessageFormat;
import java.util.Deque;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import me.rainma22.DuetMCP.event.Event;
import me.rainma22.DuetMCP.event.EventQueue;

/**
 *
 */
public class UserContext {

    public boolean supportsRoots = false;
    public boolean supportsSampling = false;
    public boolean supportsElicitation = false;
    public String version = "";
    public UUID sessionId = null;
    private static final String MSG_FMT = "%s-%d";
    private long event_num = 0;
    public EventQueue eventQueue = new EventQueue();

    public void queueEvent(String msg) {
        event_num++;
        eventQueue.enqueue(new Event(MSG_FMT.formatted(sessionId, event_num), msg));
    }
    
    public UserContext() {
        queueEvent(""); //initial event
    }

}
