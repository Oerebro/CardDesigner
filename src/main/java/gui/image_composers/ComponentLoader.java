package gui.image_composers;

import com.fasterxml.jackson.databind.*;

import abstractclasses.ImageBrowser;
import gui.ImageBrowserManager;

import javax.imageio.ImageIO;
import javax.swing.JTabbedPane;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ComponentLoader {

    public class RenderableImage {
        public String id, sourcePath;
        public BufferedImage image;
        public int[] bounds;
        public int render;

        public RenderableImage(String id, String sourcePath,BufferedImage image, int[] bounds, int render) {
            this.id = id;
            this.sourcePath = sourcePath;
            this.image = image;
            this.bounds = bounds;
            this.render = render;
        }
    }


    protected List<RenderableImage> imageRenderQueue = new ArrayList<>();
    protected Map<String, RenderableImage> imageMap = new HashMap<>();
    protected List<Object> componentRenderQueue = new ArrayList<>();
    //protected JTabbedPane componentImageBrowser, cardImageBrowser;

//lists to keep track which imagebrowser has registered which tab, to avoid unnecessary loading
/*keeps track of
    *all created tabs (to avoid re-creating them when switching card)
    *currently registered tabs in cardComponents
    *currently registered tabs in cardImages
*/
    //protected Map<String, RenderableImage> imageMap = new HashMap<>();

    protected int baseWidth = 750;
    protected int baseHeight = 1050;

    public void loadComponents(String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(new File(filePath));

            JsonNode jComponents = root.path("components").path("JComponents");
            Iterator<String> classNames = jComponents.fieldNames();
            while (classNames.hasNext()) {
                String fqcn = classNames.next();
                JsonNode instances = jComponents.get(fqcn);

                for (JsonNode instance : instances) {
                    List<Object> constructorArgs = new ArrayList<>();

                    instance.fields().forEachRemaining(entry -> {
                        if (!entry.getKey().equals("bounds")) {
                            JsonNode val = entry.getValue();
                            if (val.isInt()) constructorArgs.add(val.asInt());
                            else if (val.isTextual()) constructorArgs.add(val.asText());
                        }
                    });

                    JsonNode bounds = instance.get("bounds");
                    if (bounds != null && bounds.isArray()) {
                        int[] boundsArray = new int[bounds.size()];
                        for (int i = 0; i < bounds.size(); i++) {
                            boundsArray[i] = bounds.get(i).asInt();
                        }
                        constructorArgs.add(boundsArray);
                    }

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
                        componentRenderQueue.add(obj);
                    } catch (Exception e) {
                        System.err.println("Failed to instantiate " + fqcn + ": " + e.getMessage());
                    }
                }
            }

            JsonNode bufferedImages = root.path("components").path("BufferedImage");
            for (JsonNode img : bufferedImages) {
                String id = img.path("id").asText();
                JsonNode boundsNode = img.path("bounds");
                String sourcePath = img.path("sourcePath").asText(null);
                String path = sourcePath+img.path("path").asText(null);
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

                RenderableImage ri = new RenderableImage(id, sourcePath, image, bounds, render);
                imageRenderQueue.add(ri);
                imageMap.put(id, ri);

//register tabs into ImageBrowserManager 
                ImageBrowser br = new ImageBrowser(name, type);
                if(!ImageBrowserManager.isTabRegistered(name)){
                    br.init();
                }
                ImageBrowserManager.registerTab(name, br, type);
            }

            imageRenderQueue.sort(Comparator.comparingInt(r -> r.render));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RenderableImage> getImageRenderQueue() {
        return imageRenderQueue;
    }

    public List<Object> getComponentRenderQueue() {
        return componentRenderQueue;
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
