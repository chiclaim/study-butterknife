package com.chiclaim.butterknife.processor;

import com.squareup.javapoet.TypeName;

/**
 * Created by chiclaim on 2016/09/23
 */

class ViewBinding {

    private final String name;
    private final TypeName type;
    private final int value;

    private ViewBinding(String name, TypeName type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    static ViewBinding createViewBind(String name, TypeName type, int value) {
        return new ViewBinding(name, type, value);
    }
}
