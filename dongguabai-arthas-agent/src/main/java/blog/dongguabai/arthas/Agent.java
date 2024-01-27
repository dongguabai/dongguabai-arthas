package blog.dongguabai.arthas;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Arrays;

/**
 * @author dongguabai
 * @date 2024-01-14 11:28
 */
public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        executeAgent("premain", agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        executeAgent("agentmain", agentArgs, inst);
    }

    private static void executeAgent(String agentType, String agentArgs, Instrumentation inst) {
        System.out.println("Agent " + agentType + " start");
        try {
            initializeAgent(agentArgs, inst);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("Agent " + agentType + " end");
    }

    private static void initializeAgent(String agentArgs, final Instrumentation inst) throws Exception {
        String[] args = agentArgs.split("#");
        String className = args[0];
        String methodName = args[1];
        int port = Integer.parseInt(args[2]);
        inst.addTransformer(createTransformer(className, methodName, port), true);
        Arrays.stream(inst.getAllLoadedClasses())
                .filter(clazz -> clazz.getName().equals(className))
                .findFirst()
                .ifPresent(clazz -> {
                    try {
                        inst.retransformClasses(clazz);
                    } catch (UnmodifiableClassException e) {
                        System.out.println("Failed to retransform class: " + clazz.getName());
                        e.printStackTrace();
                    }
                });
    }

    private static ClassFileTransformer createTransformer(String className, String methodName, int port) {
        return (loader, className1, classBeingRedefined, protectionDomain, classfileBuffer) -> {
            if (!className1.replace("/", ".").equals(className)) {
                return null;
            }
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(className1.replace("/", "."));
                CtMethod m = cc.getDeclaredMethod(methodName);
                m.insertBefore("{ java.net.Socket socket = new java.net.Socket(\"localhost\", " + port + "); " +
                        "java.io.PrintWriter out = new java.io.PrintWriter(socket.getOutputStream(), true); " +
                        "out.println(\"Arguments: \" + java.util.Arrays.toString($args)); " +
                        "out.close(); socket.close(); }");
                m.insertAfter("{ java.net.Socket socket = new java.net.Socket(\"localhost\", " + port + "); " +
                        "java.io.PrintWriter out = new java.io.PrintWriter(socket.getOutputStream(), true); " +
                        "out.println(\"Return: \" + $_); " +
                        "out.close(); socket.close(); }");
                return cc.toBytecode();
            } catch (Exception e) {
                System.out.println("Failed to transform class: " + className1);
                e.printStackTrace();
                return null;
            }
        };
    }
}