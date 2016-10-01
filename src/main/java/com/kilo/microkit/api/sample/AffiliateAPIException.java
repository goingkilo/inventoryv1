package com.kilo.microkit.api.sample; /***
 * The Exception class.
 * Please refer to the instructions.txt
 *
 *  author vijay.vflipkart.com
 *  version 1.0
 * Copyright (c) Flipkart India Pvt. Ltd.
 */

import java.lang.Exception;

public class AffiliateAPIException extends Exception {
    public AffiliateAPIException(String message) {
        super(message);
    }
}