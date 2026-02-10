package com.example.aoi_endka.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ReflectionUtils {


    public static void inspectObject(Object obj) {
        if (obj == null) {
            System.out.println("Object is null");
            return;
        }

        Class<?> clazz = obj.getClass();

        System.out.println("\n========== REFLECTION INSPECTION ==========");
        System.out.println("Class Name: " + clazz.getSimpleName());
        System.out.println("Full Name: " + clazz.getName());
        System.out.println("Package: " + clazz.getPackage().getName());

        // Superclass
        if (clazz.getSuperclass() != null) {
            System.out.println("Superclass: " + clazz.getSuperclass().getSimpleName());
        }

        // Interfaces
        System.out.println("\nInterfaces:");
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) {
            System.out.println("  (none)");
        } else {
            for (Class<?> iface : interfaces) {
                System.out.println("  - " + iface.getSimpleName());
            }
        }

        // Fields
        System.out.println("\nFields:");
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            System.out.println("  (none)");
        } else {
            for (Field field : fields) {
                System.out.println("  - " + field.getType().getSimpleName() + " " + field.getName());
            }
        }

        // Methods
        System.out.println("\nMethods:");
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length == 0) {
            System.out.println("  (none)");
        } else {
            for (Method method : methods) {
                System.out.print("  - " + method.getReturnType().getSimpleName() + " " + method.getName() + "(");
                Class<?>[] params = method.getParameterTypes();
                for (int i = 0; i < params.length; i++) {
                    System.out.print(params[i].getSimpleName());
                    if (i < params.length - 1) System.out.print(", ");
                }
                System.out.println(")");
            }
        }

        System.out.println("==========================================\n");
    }


    public static void printClassHierarchy(Object obj) {
        Class<?> clazz = obj.getClass();
        System.out.println("\n========== CLASS HIERARCHY ==========");

        while (clazz != null) {
            System.out.println("  " + clazz.getSimpleName());
            clazz = clazz.getSuperclass();
            if (clazz != null) {
                System.out.println("    ↑ extends");
            }
        }
        System.out.println("=====================================\n");
    }
}