package com.gnluy.wkojsys.judege.codesandbox;
import com.gnluy.wkojsys.judge.codesandbox.CodeSandBox;
import com.gnluy.wkojsys.judge.codesandbox.CodeSandBoxFactory;
import com.gnluy.wkojsys.judge.codesandbox.CodeSandBoxProxy;
import com.gnluy.wkojsys.judge.codesandbox.model.ExecuteCodeRequest;
import com.gnluy.wkojsys.judge.codesandbox.model.ExecuteCodeResponse;
import com.gnluy.wkojsys.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author IGR
 * @version 1.0
 * @description: TODO
 * @date 22/6/2024 下午9:16
 */

@SpringBootTest
public class ExampleCodeSandBoxTest {
    @Value("${codesandbox.type}")
    private String type;

    @Test
    void CodeSandBoxTest() {
        //CodeSandBox codeSandBox = new RemoteCodeSandBox();
        //创建分两步
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);

        //建造者模式赋值
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().
                code("int main {}").
                language(QuestionSubmitLanguageEnum.JAVA.getValue()).
                inputList(new ArrayList<>(Arrays.asList(new String[]{"a", "b"}))).
                build();
        final ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        //断言
        Assertions.assertNotNull(executeCodeResponse);
    }
}
