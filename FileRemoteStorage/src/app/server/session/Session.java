package app.server.session;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Session {
    private final Map<String, Object> map = new ConcurrentHashMap<>();
    public static final String USERNAME = "username";
    private LocalDateTime expiredAt;

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    boolean isExpired(){
        LocalDateTime now = LocalDateTime.now();
        if(expiredAt != null){
            return now.isAfter(expiredAt);
        }
        return false;
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public String getString(String key) {
        return (String) get(key);
    }
}
