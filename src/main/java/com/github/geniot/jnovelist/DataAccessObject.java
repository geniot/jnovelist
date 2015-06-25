package com.github.geniot.jnovelist;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class DataAccessObject {


    public static DB open(File f) {
        return DBMaker.fileDB(f)
                .closeOnJvmShutdown()
                .compressionEnable()
                .make();

    }
}
