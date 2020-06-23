package org.activiti.cloud.starter.rb.extension.annotation;


import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.cloud.starter.rb.extension.model.Annotation;
import org.activiti.cloud.starter.rb.extension.parser.AnnotationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


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
                        reflectFillAnnotationFiled(tAttr.getName().trim(), tAttr.getValue().trim(), annotaion);
                    }
                }
                annotations.add(annotaion);
            }
        }
         return annotations;
       }

       public static void reflectFillAnnotationFiled(String attrName , Object attrVal , Annotation annotation){
           Method setMethod = null;
           Method getMethod = null;
           Class<?> clazz = Annotation.class;
           String methodSuffix=methodSuffix = attrName.substring(0,1).toUpperCase()+attrName.substring(1);
           try {
               Field field = clazz.getDeclaredField(attrName);
               setMethod = clazz.getDeclaredMethod(SET_PREFIX+methodSuffix , new Class[]{field.getType()});
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
