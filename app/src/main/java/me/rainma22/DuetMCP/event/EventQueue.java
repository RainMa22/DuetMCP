package me.rainma22.DuetMCP.event;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 */
public class EventQueue {

    private final int maintainNSentEvents = 8; //TODO: make this configurable
    private Deque<Event> sent = new ArrayDeque<>(maintainNSentEvents);
    private Deque<Event> eq = new ConcurrentLinkedDeque();
    private Set<String> currentEventIds = new HashSet();
    
    public boolean hasEvent(){
        return !eq.isEmpty();
    }
    public void enqueue(Event e) {
        eq.addLast(e);
        currentEventIds.add(e.id());
    }

    public Event peek() {
        return eq.peekFirst();
    }

    public Event poll() {
        var e = eq.pollFirst();
        currentEventIds.remove(e.id());
        if (sent.size() == maintainNSentEvents) {
            sent.pollFirst();
        }
        sent.addLast(e);
        return e;
    }

    public void regurgitate(String idSince) {
        if (currentEventIds.contains(idSince)) {
            return;
        }
        while (sent.size() != 0 && !sent.peekFirst().equals(idSince)) {
            sent.pollFirst();
        }
        while (sent.size() != 0) {
            eq.addFirst(sent.pollLast());
        }
    }

}
