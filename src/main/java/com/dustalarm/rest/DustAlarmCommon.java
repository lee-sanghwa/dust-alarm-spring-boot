package com.dustalarm.rest;

import com.dustalarm.model.Alarm;
import com.dustalarm.model.User;

import java.util.Collection;

public class DustAlarmCommon {

    public static User setUserAgent(User target, String userAgent) {
        target.setVersion(userAgent);
        return target;
    }

    public static Alarm setUserAgent(Alarm target, String userAgent) {
        target.setVersion(userAgent);
        return target;
    }

    public static Collection<User> setUserAgent2Users(Collection<User> users, String userAgent) {
        for (User user : users) {
            user.setVersion(userAgent);
        }
        return users;
    }

    public static Collection<Alarm> setUserAgent2Alarms(Collection<Alarm> targets, String userAgent) {
        for (Alarm target : targets) {
            target.setVersion(userAgent);
        }
        return targets;
    }
}
