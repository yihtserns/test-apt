package com.github.yihtserns.test.apt.annotation.user;

import com.github.yihtserns.test.apt.annotation.MsgTemplate;

/**
 *
 * @author yihtserns
 */
public interface ErrorCode {

    @MsgTemplate("Something really bad happened!")
    String SOMETHING_BAD = "00000";
}
