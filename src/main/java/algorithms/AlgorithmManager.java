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
                if (clazz != null) {
                    var constructor = clazz.getConstructor();
                    var instance = constructor.newInstance();
                    if (instance instanceof Algorithm) {
                        var alg = (Algorithm) instance;
                        var id = alg.getId();
                        var desc = alg.getDescription();
                        if (algorithms.containsKey(id)) {
                            System.out.println("Error: Duplicate algorithm IDs");
                        } else {
                            algorithms.put(id, constructor);
                            descriptions.put(id, desc);
                        }
                    } else {
                        System.out.println("Error: Custom algorithm class is not instanceof Algorithm");
                    }
                } else {
                    System.out.println("Error: Custom algorithm class is null");
                }
            } catch (InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.out.println("Error: Cannot access custom algorithm constructor.");
            } catch (NoSuchMethodException e) {
                System.out.println("Error: Custom algorithm does not have a valid constructor.");
            }
        }
    }

    public Algorithm getAlgorithm(String id) {
        try {
            var constructor = algorithms.get(id);
            if (constructor == null) return null;
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
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
