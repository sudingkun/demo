package top.sudk.script;

import cn.hutool.script.ScriptUtil;
import lombok.SneakyThrows;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;

/**
 * @author sudingkun
 */
public class JavaScript {
    public static void main(String[] args) {
        simple();

        simpleImport();
    }

    @SneakyThrows
    private static void simple() {
        ScriptUtil.getGroovyEngine().eval("System.out.println('Groovy test!');");
    }

    @SneakyThrows
    private static void simpleImport() {
        GroovyScriptEngineImpl groovyScriptEngine = new GroovyScriptEngineImpl();
        Object eval = groovyScriptEngine.compile("package top.sudk.thread;\n" +
                "\n" +
                "import cn.hutool.core.map.MapUtil;\n" +
                "\n" +
                "import java.util.Map;\n" +
                "import java.util.concurrent.CompletableFuture;\n" +
                "public class Thread {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        Map<String, String> map = MapUtil.newConcurrentHashMap();\n" +
                "        CompletableFuture.allOf(\n" +
                "                CompletableFuture.runAsync(() -> map.put(\"t1\", \"t1\")),\n" +
                "                CompletableFuture.runAsync(() -> map.put(\"t2\", \"t2\")),\n" +
                "                CompletableFuture.runAsync(() -> map.put(\"t3\", \"t3\"))\n" +
                "        ).join();\n" +
                "        System.out.println(map);\n" +
                "    }\n" +
                "}\n").eval();
        groovyScriptEngine.invokeMethod(eval, "main");
    }


}
