package com.github.yihtserns.test.apt.annotation.user;

import com.github.yihtserns.test.apt.annotation.MsgTemplate;

/**
 *
 * @author yihtserns
 */
public interface ErrorCode {

    @MsgTemplate("Something really bad happened!")
    String SOMETHING_BAD = "00000";
    @MsgTemplate("Nothing")
    String NONE = null;
    @MsgTemplate("Everything's good")
    int OK = 99999;
}
