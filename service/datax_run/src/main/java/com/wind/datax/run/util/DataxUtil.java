package com.wind.datax.run.util;


import com.wind.datax.run.source.Engine;

public class DataxUtil  {

    public static void runJson(String home, String path) throws Throwable {
        System.setProperty("datax.home",  home);
        String[] datxArgs = {"-job",  path, "-mode", "standalone", "-jobid", "-1"};
        Engine.entry(datxArgs);
    }

}
