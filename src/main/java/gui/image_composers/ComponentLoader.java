package gui.image_composers;

import com.fasterxml.jackson.databind.*;

import abstractclasses.ImageBrowser;
import gui.ImageBrowserManager;
import gui.RenderManager;
import gui.TextComponentManager;
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
    protected int baseWidth = 750;
    protected int baseHeight = 1050;

    public void loadComponents(String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(new File(filePath));

            JsonNode textComponents = root.path("components").path("TextComponents");
            Iterator<String> classNames = textComponents.fieldNames();
            while (classNames.hasNext()) {
                String fqcn = classNames.next();
                JsonNode instances = textComponents.get(fqcn);

                for (JsonNode instance : instances) {
                    String id = instance.path("id").asText(null);
                    String labelName = instance.path("labelName").asText(null);
                    int render = instance.path("render").asInt(0);
                    String side = instance.path("side").asText(null);
                    
                    JsonNode boundsNode = instance.path("bounds");
                    int[] bounds = new int[4];
                    for (int i = 0; i < boundsNode.size() && i < 4; i++) {
                        bounds[i] = boundsNode.get(i).asInt();
                    }

                    List<Object> constructorArgs = new ArrayList<>();
                    constructorArgs.add(id); 
                    constructorArgs.add(labelName);   
                    constructorArgs.add(bounds); 

                    Object[] args = constructorArgs.toArray();
                    Class<?>[] paramTypes = Arrays.stream(args)
                            .map(arg -> {
                                if (arg instanceof Integer) return int.class;
                                if (arg instanceof String) return String.class;
                                if (arg instanceof int[]) return int[].class;
                                return arg.getClass();
                            })
                            .toArray(Class<?>[]::new);

                    try {
                        Class<?> clazz = Class.forName(fqcn);
                        Object obj = clazz.getConstructor(paramTypes).newInstance(args);

                        // must be a JComponent
                        if (!(obj instanceof JComponent)) {
                            throw new IllegalArgumentException("Object is not a JComponent: " + fqcn);
                        }

                        RenderableText comp = new RenderableText(id, labelName, render, bounds, (JComponent) obj);
                        RenderManager.addToTextMap(comp);
                        JPanel panel = comp.getInputComponent();
                        if(!TextComponentManager.isComponentRegistered(id)){
                            TextComponentManager.registerComponents(id, panel, side);
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to instantiate " + fqcn + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            JsonNode bufferedImages = root.path("components").path("BufferedImage");
            for (JsonNode img : bufferedImages) {
                String id = img.path("id").asText();
                JsonNode boundsNode = img.path("bounds");
                String path = img.path("path").asText(null);
                int render = img.path("render").asInt(0);
                String type = img.path("type").asText(null);
                String name = img.path("name").asText(null);

                int[] bounds = new int[4];
                for (int i = 0; i < boundsNode.size(); i++) {
                    bounds[i] = boundsNode.get(i).asInt();
                }

                BufferedImage image = null;
                if (path != null) {
                    image = ImageIO.read(new File(path));
                }

                RenderableImage ri = new RenderableImage(id, path, image, bounds, render);  
                RenderManager.addToImageMap(ri);       
            }

            JsonNode imageBrowserTabs = root.path("components").path("ImageBrowserTabs");
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

    protected BufferedImage getImageFromFile(String path){
        if(path == null){
            return null;
        }

        BufferedImage i = null;
         try{
            i = ImageIO.read(new File(path));
        }catch(IOException e){
            System.out.println("Error on ImageComposer::getImageFromFile ("+path+"); File not found");
            return null;
     
        }
        return i;
    }
}
