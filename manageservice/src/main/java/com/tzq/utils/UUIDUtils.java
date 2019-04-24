package com.tzq.utils;

import java.util.UUID;

/**
 * uuid
 * @author 马庆龙
 *
 */
public class UUIDUtils {

	public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
