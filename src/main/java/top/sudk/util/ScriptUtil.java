package top.sudk.util;

import groovy.lang.GroovyClassLoader;

/**
 * @author we
 */
public class ScriptUtil {


    public static Class<?> createScriptClass() {
        String scriptContent = "package top.sudk.script;\n" +
                "\n" +
                "import org.aspectj.lang.annotation.*;\n" +
                "import org.aspectj.lang.JoinPoint;\n" +
                "\n" +
                "\n" +
                "@Aspect\n" +
                "public class DynamicAspect {\n" +
                "\n" +
                "\n" +
                "    @Before(\"execution(* top.sudk.service.impl.BookServiceImpl.list(..))\")\n" +
                "    public void printParam(JoinPoint joinPoint) {\n" +
                "        System.out.println(\"DynamicAspect printParam run !!!!\");\n" +
                "        Object[] args = joinPoint.getArgs();\n" +
                "        for (Object arg : args) {\n" +
                "            System.out.println(arg);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    @After(\"@annotation(org.springframework.scheduling.annotation.Scheduled)\")\n" +
                "    public void addLog() {\n" +
                "        System.out.println(\"DynamicAspect addLog run !!!!\");\n" +
                "    }\n" +
                "\n" +
                "}";
        //编译
        return new GroovyClassLoader().parseClass(scriptContent);
    }


}
