package blog.dongguabai.arthas;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * @author dongguabai
 * @date 2024-01-15 11:28
 */
public class Test {

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        VirtualMachine vm = VirtualMachine.attach("64833");
        vm.loadAgent("/Users/dongguabai/IdeaProjects/gitee/blog-dongguabai/dongguabai-arthas/dongguabai-arthas-agent/target/dongguabai-arthas-agent-1.0-SNAPSHOT-jar-with-dependencies.jar", "dongguabai.spring.boot.demo.sb1.TestController#handleTask");
        vm.detach();
    }
}