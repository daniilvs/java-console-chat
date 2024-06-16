package app.server.session;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


// TODO session expiration
public class SessionService {
    private final Map<Token, Session> token2sessionMap = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();

    public TokenSession create() {
        var token = new Token();
        var session = new Session();
        token2sessionMap.put(token, session);
        return new TokenSession(token, session);
    }

    public Session get(Token token) {
        return token2sessionMap.get(token);
    }

    public void remove(Token token) {
        token2sessionMap.remove(token);

    }

    public void hasRequest(Token token) {
        token2sessionMap.get(token).setExpiredAt(LocalDateTime.now().plusMinutes(10));
    }


    public record TokenSession(Token token, Session session) {
    }

    {
        var t = new Thread(() -> {
            while (true) {
                try {
                    if (token2sessionMap != null) {
                        var iter = token2sessionMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            var session = iter.next().getValue();
                            if (session.isExpired()) {
                                System.out.println(STR."Session time out");
                                iter.remove();
                            }
                        }
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
