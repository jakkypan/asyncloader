/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.pan.lean.photowall.util;

import java.util.concurrent.Future;

public class NetworkThreadPool {
    private static LIFOThreadPoolProcessor pool = new LIFOThreadPoolProcessor(3);

    public static Future<?> submitTask(LIFOTask task) {
        return pool.submitTask(task);
    }
}
