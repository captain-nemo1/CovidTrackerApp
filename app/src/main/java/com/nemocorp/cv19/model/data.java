package com.nemocorp.cv19.model;

public class data {
    private final String confirmed;
    private final String name;
    private final String death;
    private final String active;
    private final String recovered;
    private final String confirmed_inc;
    private final String death_inc;
    private final String recovered_inc;

    public data(String name, String confirmed, String death, String active, String recovered, String confirmed_inc, String death_inc,String recovered_inc)
    {
        this.name=name;
        this.confirmed=confirmed;
        this.death=death;
        this.active=active;
        this.recovered=recovered;
        this.confirmed_inc=confirmed_inc;
        this.death_inc=death_inc;
        this.recovered_inc=recovered_inc;
    }
    public String getName()
    {
        return name;
    }
    public String getConfirmed()
    {
        return confirmed;
    }
    public String getDeath()
    {
        return death;
    }
    public String getActive(){ return active;}
    public String getRecovered()
    {
        return recovered;
    }
    public String getConfirmed_inc()
    {
        return confirmed_inc;
    }
    public String getDeath_inc()
    {
        return death_inc;
    }
    public String getRecovered_inc()
    {
        return recovered_inc;
    }


}
