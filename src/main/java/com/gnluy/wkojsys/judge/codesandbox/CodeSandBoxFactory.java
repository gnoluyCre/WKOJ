package com.gnluy.wkojsys.judge.codesandbox;

import com.gnluy.wkojsys.judge.codesandbox.impl.ExampleCodeSandBox;
import com.gnluy.wkojsys.judge.codesandbox.impl.RemoteCodeSandBox;
import com.gnluy.wkojsys.judge.codesandbox.impl.ThirdPartyCodeSandBox;
import org.checkerframework.common.reflection.qual.NewInstance;

/**
 * @author IGR
 * @version 1.0
 * @description: TODO
 * @date 23/6/2024 下午7:54
 */
public class CodeSandBoxFactory {

    public static CodeSandBox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandBox();
            case "remote":
                return new RemoteCodeSandBox();
            case "thirdParty":
                return new ThirdPartyCodeSandBox();
            default:
                return new ExampleCodeSandBox();
        }
    }
}
