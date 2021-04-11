package dev.buildtool.scp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SCPObject {
    String number();

    String name();

    Classification classification();

    enum Classification {
        SAFE,
        EUCLID,
        KETER
    }
}
