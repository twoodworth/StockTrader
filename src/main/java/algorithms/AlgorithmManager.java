package algorithms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class AlgorithmManager {
    private static AlgorithmManager instance = null;
    private HashMap<String, Constructor<Algorithm>> algorithms = new HashMap<>();
    private HashMap<String, String> descriptions = new HashMap<>();

    public static AlgorithmManager getInstance() {
        if (instance == null) {
            instance = new AlgorithmManager();
        }
        return instance;
    }

    private AlgorithmManager() {
        registerAlgorithms();
    }

    private void registerAlgorithms() {
        var classes = getClasses();
        for (var clazz : classes) {
            try {
                var constructor = ((Class<Algorithm>) clazz).getConstructor();
                var instance = constructor.newInstance();
                var id = instance.getId();
                var desc = instance.getDescription();
                if (algorithms.containsKey(id)) {
                    System.out.println("Error: Duplicate algorithm IDs");
                } else {
                    algorithms.put(id, constructor);
                    descriptions.put(id, desc);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                System.out.println("");
                System.out.println("Error: Custom algorithm does not have a valid constructor.");
                System.out.println("(Constructor must not have any arguments)");
                System.exit(0);
            }
        }
    }

    public Algorithm getAlgorithm(String id) {
        try {
            var constructor = algorithms.get(id);
            if (constructor == null) return null;
            return constructor.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printAlgorithms() {
        for (var id : descriptions.keySet()) {
            System.out.println("\t[" + id + "] " + descriptions.get(id));
        }
    }


    private Set<Class> getClasses() {
        var name = "algorithms.customAlgorithms";
        var stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(name.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, name))
                .collect(Collectors.toSet());

    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
}
