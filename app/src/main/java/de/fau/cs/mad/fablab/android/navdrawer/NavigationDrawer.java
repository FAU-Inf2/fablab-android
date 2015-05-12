package de.fau.cs.mad.fablab.android.navdrawer;

import java.util.ArrayList;

public class NavigationDrawer {
    private String name;
    private String email;
    private int icon;
    private ArrayList<NavigationDrawerItem> items = new ArrayList<NavigationDrawerItem>();

    public NavigationDrawer(String name, String email, int icon) {
        this.name = name;
        this.email = email;
        this.icon = icon;
    }

    public NavigationDrawer(String name, String email) {
        this.name = name;
        this.email = email;
        this.icon = -1;
    }

    public NavigationDrawer() {
        this.name = new String("");
        this.email = new String("");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void addItem(NavigationDrawerItem i) {
        items.add(i);
    }

    public ArrayList<NavigationDrawerItem> getItems() {
        return this.items;
    }

}
