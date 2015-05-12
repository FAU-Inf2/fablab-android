package de.fau.cs.mad.fablab.android.navdrawer;

public class NavigationDrawerItem {
    private String title;
    private int icon;
    private Class<?> intent;

    public NavigationDrawerItem(String title, Class<?> intent) {
        this.title = title;
        this.intent = intent;
    }

    public NavigationDrawerItem(String title, Class<?> intent, int icon) {
        this.title = title;
        this.intent = intent;
        this.icon = icon;
    }

    public String getTitle() { return this.title; }

    public int getIcon() {
        return this.icon;
    }

    public Class<?> getIntent() { return this.intent; }
}
