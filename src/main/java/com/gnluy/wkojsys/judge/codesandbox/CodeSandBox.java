package com.gnluy.wkojsys.judge.codesandbox;

import com.gnluy.wkojsys.judge.codesandbox.model.ExecuteCodeRequest;
import com.gnluy.wkojsys.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandBox {

    /**
     * 沙箱接口
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
