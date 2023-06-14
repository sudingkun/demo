package top.sudk.script;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.script.ScriptUtil;
import lombok.SneakyThrows;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.script.LuaScriptEngine;

import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;

/**
 * @author sudingkun
 */
public class LuaScript {

    public static void main(String[] args) {
        simple();
        simpleImport();
    }

    @SneakyThrows
    public static void simple() {
        ScriptEngine luaEngine = ScriptUtil.getLuaEngine();
        luaEngine.eval("print(\"hello world\")");
    }

    @SneakyThrows
    public static void simpleImport() {
        String luaStr = "module = {}\n" +
                " \n" +
                "function module.collect(text)\n" +
                "    data = string.match(text,\"生肖:([鼠牛虎兔龙蛇马羊猴鸡狗猪]+)\")\n" +
                "    print(data) \n" +
                "    return data\n" +
                "end\n" +
                " \n" +
                " \n" +
                "return module";

        LuaScriptEngine luaScriptEngine = new LuaScriptEngine();
        luaScriptEngine.eval(luaStr);
        luaScriptEngine.put("text", "12生肖:龙蛇虎兔！！");
        System.out.println(luaScriptEngine.eval("return module.collect(text)"));
    }


}
