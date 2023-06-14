package top.sudk.script;

import cn.hutool.script.ScriptUtil;
import lombok.SneakyThrows;
import org.python.jsr223.PyScriptEngine;

import javax.script.ScriptEngine;

/**
 * @author sudingkun
 */
public class PythonScript {

    public static void main(String[] args) {
        simple();
        // python 添加一个库，然后调用其中的一个方法

    }

    @SneakyThrows
    public static void simple() {
        ScriptEngine pythonEngine = ScriptUtil.getPythonEngine();
        pythonEngine.eval("print(\"hello world\")");
    }
}
