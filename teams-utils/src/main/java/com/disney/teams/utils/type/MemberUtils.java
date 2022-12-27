package com.disney.teams.utils.type;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public class MemberUtils {

    public static <T extends AccessibleObject> void setAccessible(T member) {
        if (member == null) {
            return;
        }
        if (!member.isAccessible()) {
            member.setAccessible(true);
        }
    }

    public static <T extends Member> boolean isPublic(T member) {
        return member != null && Modifier.isPublic(member.getModifiers());
    }

    public static <T extends Member> List<T> filter(List<T> members, Boolean isStatic) {
        if (isStatic == null || CollectionUtils.isEmpty(members)) {
            return members;
        }
        List<T> memberList = new ArrayList<>();
        for (T member : members) {
            //同或
            if (!(isStatic ^ Modifier.isStatic(member.getModifiers()))) {
                memberList.add(member);
            }
        }
        return memberList;
    }

    public static <T extends Member> List<T> filter(List<T> members, int modifier) {
        if (CollectionUtils.isEmpty(members)) {
            return members;
        }
        List<T> memberList = new ArrayList<>();
        for (T member : members) {
            if ((member.getModifiers() & modifier) == modifier) {
                memberList.add(member);
            }
        }
        return memberList;
    }

    public static <T extends Member> List<T> filter(List<T> members, String name) {
        if (CollectionUtils.isEmpty(members)) {
            return null;
        }
        List<T> memberList = new ArrayList<>();
        for (T member : members) {
            if (member.getName().equals(name)) {
                memberList.add(member);
            }
        }
        return memberList;
    }

    public static <T extends Member> T filterFirst(List<T> members, String name) {
        if (CollectionUtils.isEmpty(members)) {
            return null;
        }
        for (T member : members) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        return null;
    }
}
