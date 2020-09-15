package org.debugroom.mynavi.sample.aws.microservice.common.apinfra.util;

import java.sql.Timestamp;
import java.util.Date;

public interface DateStringUtil {

    public static String now(){

        return (new Timestamp(System.currentTimeMillis()).toString());
    }

    public static Date nowDate(){
        return  new Date(System.currentTimeMillis());
    }

}
