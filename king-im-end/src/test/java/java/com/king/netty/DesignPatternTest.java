package java.com.king.netty;

import lombok.Data;

public class DesignPatternTest {

    @Data
    public class Inner {
        private String name;
    }


    public Inner getObj() {
        return new Inner();
    }


}
