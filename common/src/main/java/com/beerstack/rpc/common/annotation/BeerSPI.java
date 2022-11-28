package com.beerstack.rpc.common.annotation;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BeerSPI {
    /**
     * spi标识的名称
     *
     * @return
     */
    String value();
}
