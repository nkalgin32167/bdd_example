package bdd.example.utils;

import java.util.HashMap;

public class Utils {
    private static final ThreadLocal<HashMap<String, Object>> session = ThreadLocal.withInitial(HashMap::new);

    public static HashMap<String, Object> getCurrentSession() {
        return session.get();
    }

    public static void setCurrentSession(HashMap<String, Object> newState) {
        session.set(newState);
    }
}
