package taohu.inject;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;

public class InstanceCreatorTest {

//    public class NestedNoParaCtor {
//        public NestedNoParaCtor() {
//        }
//    }

    @Test
    public void shouldCreateInstanceOfAClassWithAParameterlessConstructor
            () throws Exception {
        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.getInstanceOf("taohu.inject.NoParaCtor");

        assertThat((NoParaCtor) instance, isA(NoParaCtor.class));
    }

//    @Test
//    public void shouldCreateInstanceOfANestedClassWithAParameterlessConstructor
//            () throws Exception {
//        InstanceCreator instanceCreator = new InstanceCreator();
//        Object instance = instanceCreator.getInstanceOf("taohu.inject.InstanceCreatorTest$NestedNoParaCtor");
//
//        assertThat((NestedNoParaCtor) instance, isA(NestedNoParaCtor.class));
//    }
}
