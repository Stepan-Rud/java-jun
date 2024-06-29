package ru.gb.lesson2.anno.lib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

public class RandomDateAnotationProcessor {
    public static void processAnnotation(Object obj) throws NoSuchMethodException, NoSuchFieldException {
        Class<?> objClass = obj.getClass();
         Field[] setDate = Arrays.stream(objClass.getDeclaredFields()).filter(f -> f.isAnnotationPresent(RandomDate.class)).toArray(Field[]::new);
//        if (objClass.isAnnotationPresent(RandomDate.class) && objClass.isAssignableFrom(Date.class)) {
        if (objClass.isAnnotationPresent(RandomDate.class) &&
                Arrays.stream(setDate).iterator().next().isAnnotationPresent(RandomDate.class)) {
            RandomDate annotation = objClass.getAnnotation(RandomDate.class);
            long min = annotation.min();
            long max = annotation.max();
            java.util.Random random = new java.util.Random();
            try {
                Field field = objClass.getDeclaredField("date");
                field.setAccessible(true);
                field.set(obj, new Date(random.nextLong(min, max)));
            } catch (Exception e) {
                System.err.println("Не удалось вставить значение в поле: " + e.getMessage());
            }
        }
    }
}
