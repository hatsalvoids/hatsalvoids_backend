package com.example.hatsalvoids.global.management;

import com.example.hatsalvoids.global.error.core.BaseException;
import com.example.hatsalvoids.global.error.handler.GlobalErrorCode;

public class ManagementException extends BaseException {
    public ManagementException(GlobalErrorCode errorCode) {
        super(errorCode);
    }
}
