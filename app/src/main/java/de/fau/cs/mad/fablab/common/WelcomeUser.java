package de.fau.cs.mad.fablab.common;

/**
 * This class represents the JSON output of the server.
 */

public class WelcomeUser {
    private long id;
    private String message;
    private long greetings;

    public WelcomeUser(){}

    public long getId(){return this.id;}

    public void setId(long i){this.id = i;}

    public String getMessage(){return this.message;}

    public void setMessage(String m){this.message = m;}

    public long getGreetings(){return this.greetings;}

    public void setGreetings(long g){this.greetings = g;}
}
