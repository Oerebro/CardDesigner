package gui.image_composers;

import com.fasterxml.jackson.databind.*;

import abstractclasses.ImageBrowser;
import abstractclasses.TextComponent;
import gui.ImageBrowserManager;
import gui.RenderManager;
import gui.ComponentManager;
import gui.image_composers.components.RenderableImage;
import gui.image_composers.components.RenderableText;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ComponentLoader {
    protected int[] baseResolution;
    protected int[] bleedResolution;

    public void loadComponents(String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(new File(filePath));

            JsonNode components = root.path("components");
            
            //get aspect ratio and base resolution of card
            JsonNode boundsNode = components.path("resolution");
            JsonNode boundsNode2 = components.path("bleed_resolution");
            baseResolution = new int[2];
            bleedResolution = new int[2];
            for (int i = 0; i < boundsNode.size(); i++) {
                baseResolution[i] = boundsNode.get(i).asInt();
                bleedResolution[i] = boundsNode2.get(i).asInt();
            }

            String textComponentsPath = components.path("TextComponents").asText(null);
            String bufferedImagesPath = components.path("BufferedImages").asText(null);
            String imageBrowserTabsPath = components.path("ImageBrowserTabs").asText(null);
            String cardAttributesPath = components.path("CardAttributes").asText(null);
            System.out.println(bufferedImagesPath);
            loadTextComponents(textComponentsPath);
            loadBufferedImages(bufferedImagesPath);
            loadImageBrowserTabs(imageBrowserTabsPath);
            loadAttributeComponent(cardAttributesPath);
        } catch (Exception e) {
            e.printStackTrace();
        }  

            
    }

    


    protected void loadTextComponents(String filePath){
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode root = mapper.readTree(new File(filePath));
            JsonNode textComponents = root;

            Iterator<String> classNames = textComponents.fieldNames();
            while (classNames.hasNext()) {
                String fqcn = classNames.next();
                JsonNode instances = textComponents.get(fqcn);

                for (JsonNode instance : instances) {
                    String id = instance.path("id").asText(null);
                    String labelName = instance.path("labelName").asText(null);
                    int render = instance.path("render").asInt(0);
                    String side = instance.path("side").asText(null);
                    String alignement = instance.path("alignement").asText(null);
                    int minLineCount = instance.path("minLineCount").asInt(1);

                    JsonNode boundsNode = instance.path("bounds");
                    int[] bounds = new int[4];
                    for (int i = 0; i < boundsNode.size() && i < 4; i++) {
                        bounds[i] = boundsNode.get(i).asInt();
                    }

                    List<Object> argsWithMinLine = new ArrayList<>();
                    argsWithMinLine.add(id);
                    argsWithMinLine.add(labelName);
                    argsWithMinLine.add(alignement);
                    argsWithMinLine.add(render);
                    argsWithMinLine.add(bounds);
                    argsWithMinLine.add(minLineCount);

                    List<Object> argsWithoutMinLine = argsWithMinLine.subList(0, 5);

                    try {
                        Class<?> clazz = Class.forName(fqcn);

                        Object obj = null;

                        // local lambda to get param types
                        java.util.function.Function<List<Object>, Class<?>[]> getParamTypes = (argList) -> 
                            argList.stream()
                                .map(arg -> {
                                    if (arg instanceof Integer) return int.class;
                                    if (arg instanceof String) return String.class;
                                    if (arg instanceof int[]) return int[].class;
                                    return arg.getClass();
                                }).toArray(Class<?>[]::new);

                        try {
                            // Try constructor with minLineCount
                            Class<?>[] paramTypesWithMin = getParamTypes.apply(argsWithMinLine);
                            obj = clazz.getConstructor(paramTypesWithMin).newInstance(argsWithMinLine.toArray());
                        } catch (NoSuchMethodException e) {
                            // Fallback: try constructor without minLineCount
                            Class<?>[] paramTypesWithoutMin = getParamTypes.apply(argsWithoutMinLine);
                            obj = clazz.getConstructor(paramTypesWithoutMin).newInstance(argsWithoutMinLine.toArray());
                        }

                        // must be a JComponent
                        if (!(obj instanceof JComponent)) {
                            throw new IllegalArgumentException("Object is not a JComponent: " + fqcn);
                        }

                        RenderableText comp = new RenderableText(id, labelName, render, bounds, (JComponent) obj);
                        RenderManager.addToTextMap(comp);
                        JPanel panel = comp.getInputComponent();
                        if (!ComponentManager.isComponentRegistered(id)) {
                            ComponentManager.registerComponents(id, panel, side);
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to instantiate " + fqcn + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadBufferedImages(String filePath){
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode root = mapper.readTree(new File(filePath));
            JsonNode bufferedImages = root;
            for (JsonNode img : bufferedImages) {
                String id = img.path("id").asText();
                JsonNode boundsNode = img.path("bounds");
                String path = img.path("path").asText(null);
                int render = img.path("render").asInt(0);
                int[] bounds = new int[4];
                for (int i = 0; i < boundsNode.size(); i++) {
                    bounds[i] = boundsNode.get(i).asInt();
                }

                BufferedImage image = null;
                if (path != null) {
                    image = getImageFromFile(path);
                }
                //System.out.println("Loaded image: " + id + ", render: " + render);
                RenderableImage ri = new RenderableImage(id, path, image, bounds, render);  
                RenderManager.addToImageMap(ri);       
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadImageBrowserTabs(String filePath){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(new File(filePath));
            JsonNode imageBrowserTabs = root;
            for (JsonNode tab : imageBrowserTabs) {
                String id = tab.path("id").asText();
                String sourcePath = tab.path("sourcePath").asText(null);
                String type = tab.path("type").asText(null);
                String name = tab.path("name").asText(null);

            //register tabs into ImageBrowserManager 
                ImageBrowser br = new ImageBrowser(name, sourcePath, id);
                if(!ImageBrowserManager.isTabRegistered(name)){
                    br.init();   
                    ImageBrowserManager.registerTab(name, br, type);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadAttributeComponent(String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(new File(filePath));

            Iterator<String> classNames = root.fieldNames();
            while (classNames.hasNext()) {
                String fqcn = classNames.next();
                JsonNode instances = root.get(fqcn);

                if (!instances.isArray()) {
                    System.err.println("Expected an array of instances for class: " + fqcn);
                    continue;
                }

                for (JsonNode instance : instances) {
                    // Create fresh parameter list for each instance
                    List<Object> allParameters = new ArrayList<>();

                    Iterator<String> fieldNames = instance.fieldNames();
                    while (fieldNames.hasNext()) {
                        String fieldName = fieldNames.next();
                        if (fieldName.equals("bounds")) {
                            continue; // handle bounds separately
                        }
                        JsonNode valueNode = instance.get(fieldName);
                        if (valueNode.isTextual()) {
                            allParameters.add(valueNode.asText());
                        } else if (valueNode.isInt()) {
                            allParameters.add(valueNode.asInt());
                        } else if (valueNode.isBoolean()) {
                            allParameters.add(valueNode.asBoolean());
                        } else if (valueNode.isDouble()) {
                            allParameters.add(valueNode.asDouble());
                        } else {
                            allParameters.add(valueNode.toString());
                        }
                    }

                    // Handle bounds if present
                    if (instance.has("bounds")) {
                        JsonNode boundsNode = instance.get("bounds");
                        int[] bounds = new int[4];
                        for (int i = 0; i < boundsNode.size() && i < 4; i++) {
                            bounds[i] = boundsNode.get(i).asInt();
                        }
                        allParameters.add(bounds);
                    }

                    try {
                        Class<?> clazz = Class.forName(fqcn);
                        Object obj = null;

                        // Determine parameter types for constructor
                        Class<?>[] paramTypes = allParameters.stream()
                            .map(arg -> {
                                if (arg instanceof Integer) return int.class;
                                if (arg instanceof String) return String.class;
                                if (arg instanceof int[]) return int[].class;
                                return arg.getClass();
                            })
                            .toArray(Class<?>[]::new);

                        try {
                            // Try to instantiate using constructor with these params
                            obj = clazz.getConstructor(paramTypes).newInstance(allParameters.toArray());
                        } catch (NoSuchMethodException e) {
                            System.out.println("No Constructor for " + fqcn);
                            System.out.println("Parameters: " + allParameters);
                            continue; // skip this instance
                        }

                        // Verify the object is a JComponent
                        if (!(obj instanceof JComponent)) {
                            System.out.println("Object class: " + obj.getClass());
                            System.out.println("Is JComponent: " + (obj instanceof JComponent));
                            throw new IllegalArgumentException("Object is not a JComponent: " + fqcn);
                        }

                        // Cast and register component
                        JPanel panel = ((TextComponent) obj).getInputComponent();
                        String updateID = instance.path("updateID").asText(null);

                        if (!ComponentManager.isComponentRegistered(updateID)) {
                            ComponentManager.registerCardAttribute(updateID, panel);
                        }

                    } catch (Exception e) {
                        System.err.println("Failed to instantiate " + fqcn + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Failed to read or parse file: " + filePath);
            e.printStackTrace();
        }
    }


    protected BufferedImage getImageFromFile(String path){
        if(path == null){
            return null;
        }

        BufferedImage i = null;
         try{
            i = ImageIO.read(new File(path));
        }catch(IOException e){
            System.out.println("Error on ComponentLoader::getImageFromFile ("+path+"); File not found");
            return null;
     
        }
        return i;
    }
}
