package org.activiti.cloud.starter.rb.extension.parser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationConstants {
    public static final String ELEMENT_NAME= "annotation";
    public static final String ATTR_ID= "id";
    public static final String ATTR_NAME= "name";
    public static final String ATTR_TARGET_ELEMENT= "targetElement";
    public static final String ATTR_IMPLEMENTION_TYPE= "implementationType";
    public static final String ATTR_POINTCUT_TYPE= "pointcutType";
    public static final String ATTR_HANDLER= "handler";
    public static final String ATTR_SCRIPT= "script";
    public static final String PRE_PROCESSOR= "PreProcessor";
    public static final String POST_PROCESSOR= "PostProcessor";
    public static final String MSG_PROCESSOR= "MsgProcessor";
    public static final String IMPLEMENTION_TYPE_LOCAL= "localType";
    public static final String IMPLEMENTION_TYPE_GLOBAL= "globalType";
    public static final String HANDLER_EXPRESSION= "${l2LAnnotationListener}";
    public static final List<String> ATTRS = Stream.of(ATTR_ID , ATTR_NAME , ATTR_POINTCUT_TYPE , ATTR_IMPLEMENTION_TYPE, ATTR_HANDLER, ATTR_SCRIPT)
        .collect(Collectors.toList());

}
