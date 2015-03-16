package com.example.conor.senan.utils;

/**
 * Created by Conor on 01/02/2015.
 */
public class MathUtils {


    public static String doubleToNiceString(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }


}
