package org.example.htuoj.common.utils;


import org.example.htuoj.common.dao.User;

public class UserHolder {
    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    public static void setUser(User user) {
        tl.set(user);
    }

    public static User getUser() {
        return tl.get();
    }

    public static void removeUser() {
        tl.remove();
    }

    public static Long getUserId() {
        if (tl.get() == null) {
            return null;
        }
        return tl.get().getId();
    }
}
