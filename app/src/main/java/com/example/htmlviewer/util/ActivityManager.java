package com.example.htmlviewer.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * ActivityManager
 * <p>
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 2:21 PM
 **/
public class ActivityManager {

    private List<Activity> activities = new ArrayList<>();


    private static final ActivityManager ourInstance = new ActivityManager();

    public static ActivityManager getInstance() {
        return ourInstance;
    }

    private ActivityManager() {
    }

    public void addActivity(Activity activity) {
        if (activities == null)
            return;
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activities == null)
            return;
        activities.remove(activity);
    }

    public void exit() {
        if (activities == null)
            return;
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.gc();
    }

    public List<Activity> getActivities() {
        return activities;
    }

}
