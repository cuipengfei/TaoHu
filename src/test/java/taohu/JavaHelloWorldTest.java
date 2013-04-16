package taohu;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class JavaHelloWorldTest {
    @Test
    public void testTest() throws Exception {
        JavaHelloWorld javaHelloWorld = new JavaHelloWorld();
        String text = javaHelloWorld.javaSayHello();

        Assert.assertThat(text, is("Java Says Hello, World!"));
    }
}
