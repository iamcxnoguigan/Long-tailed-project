package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import com.l2l.enterprise.vessel.extension.activiti.parser.AnnotationConstants;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


public class AnnotationUtils {
    private static final String SET_PREFIX = "set";
    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";
    private static final Logger log = LoggerFactory.getLogger(AnnotationUtils.class);

    public static List<Annotation> collectAnnotationsOnElement(FlowElement flowElement){

//        Map<String , List<ExtensionElement>> extensionElements = flowElement.getExtensionElements();

        List<Annotation> annotations = new ArrayList<Annotation>();
        List<ExtensionElement> extensionElements = flowElement.getExtensionElements().entrySet().stream()
            .filter(e -> e.getKey().equals(AnnotationConstants.ELEMENT_NAME))
            .collect(
                () -> new ArrayList<ExtensionElement>() ,
                (r , e) ->{ r.addAll(e.getValue()); },
                (l, r)-> {l.addAll(r);});

        if(extensionElements != null){
            for (ExtensionElement extensionElement : extensionElements){
                Collection<List<ExtensionAttribute>> attrsCollection = extensionElement.getAttributes().values();
                Iterator<List<ExtensionAttribute>> it = attrsCollection.iterator();
                Annotation annotaion = new Annotation();
                annotaion.setTargetElementId(flowElement.getId());
                while (it.hasNext()){
                    ExtensionAttribute tAttr= it.next().get(0);//no duplicate attributes by default
                    if(tAttr != null){
                        if(tAttr.getName().trim().equals("awsVariables") || tAttr.getName().trim().equals("processVariables") ||  tAttr.getName().trim().equals("executionvars")){
                            String[] str1 = tAttr.getValue().trim().split(",");
                            Map<String,Object> map = new HashMap<>();
                            //循环加入map集合
                            for (int i = 0; i < str1.length; i++) {
                                //根据":"截取字符串数组
                                String[] str2 = str1[i].split(":");
                                //str2[0]为KEY,str2[1]为值
                                map.put(str2[0], str2[1]);
                            }
                            reflectFillAnnotationFiled(tAttr.getName().trim(), map, annotaion ,Annotation.class);
                        }else {
                            reflectFillAnnotationFiled(tAttr.getName().trim(), tAttr.getValue().trim(), annotaion ,Annotation.class);
                        }
                    }
                }
                annotations.add(annotaion);
            }
        }
         return annotations;
       }

       public static List<MsgAnnotation> collectMsgAnnoationOnProcess(Process process){
           List<MsgAnnotation> msgAnnotations = new ArrayList<MsgAnnotation>();
           List<ExtensionElement> extensionElements = process.getExtensionElements().entrySet().stream()
               .filter(e -> e.getKey().equals(AnnotationConstants.ELEMENT_NAME))
               .collect(
                   () -> new ArrayList<ExtensionElement>() ,
                   (r , e) ->{ r.addAll(e.getValue()); },
                   (l, r)-> {l.addAll(r);});
           if(extensionElements != null){
               for (ExtensionElement extensionElement : extensionElements){
                   Collection<List<ExtensionAttribute>> attrsCollection = extensionElement.getAttributes().values();
                   Iterator<List<ExtensionAttribute>> it = attrsCollection.iterator();
                   MsgAnnotation msgAnnotation = new MsgAnnotation();
                   msgAnnotation.setTargetElementId(process.getId());
                   while (it.hasNext()){
                       ExtensionAttribute tAttr= it.next().get(0);//no duplicate attributes by default
                       if(tAttr != null){
                           if(tAttr.getName().trim().equals("awsVariables") || tAttr.getName().trim().equals("processVariables") ||  tAttr.getName().trim().equals("executionvars")){
                               String[] str1 = tAttr.getValue().trim().split(",");
                               Map<String,Object> map = new HashMap<>();
                               //循环加入map集合
                               for (int i = 0; i < str1.length; i++) {
                                   //根据":"截取字符串数组
                                   String[] str2 = str1[i].split(":");
                                   //str2[0]为KEY,str2[1]为值
                                   map.put(str2[0], str2[1]);
                               }
                               reflectFillAnnotationFiled(tAttr.getName().trim(), map, msgAnnotation ,Annotation.class);
                           }else {
                                reflectFillAnnotationFiled(tAttr.getName().trim(), tAttr.getValue().trim(), msgAnnotation , MsgAnnotation.class);
                           }
                       }
                   }
                   msgAnnotations.add(msgAnnotation);
               }
           }
            return msgAnnotations;
       }

       public static void reflectFillAnnotationFiled(String attrName , Object attrVal , Annotation annotation ,Class<?> clazz){
           Method setMethod = null;
           Method getMethod = null;
           String methodSuffix=methodSuffix = attrName.substring(0,1).toUpperCase()+attrName.substring(1);
           try {
               Field field = clazz.getField(attrName);
               setMethod = clazz.getMethod(SET_PREFIX+methodSuffix , new Class[]{field.getType()});
               setMethod.invoke(annotation , new Object[]{attrVal});
           } catch (NoSuchFieldException e) {
               e.printStackTrace();
           } catch (NoSuchMethodException e) {
               e.printStackTrace();
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           } catch (InvocationTargetException e) {
               e.printStackTrace();
           }
       }



}
