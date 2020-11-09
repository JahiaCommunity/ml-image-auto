package org.jahiacommunity.ml.image.Impl;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageCaption;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class ComputerVisionServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(ComputerVisionServiceImpl.class);

    // <snippet_auth>
    public static ComputerVisionClient authenticate(String subscriptionKey, String endpoint){
        return ComputerVisionManager.authenticate(subscriptionKey).withEndpoint(endpoint);
    }
    // </snippet_auth>

    /*
    public static void describeImageTest(ComputerVisionClient compVisCl) {
        String pathToLocalImage = "src/main/resources/biche.jpg";
        try {
            // <snippet_analyzelocal_analyze>
            // Need a byte array for analyzing a local image.
            File rawImage = new File(pathToLocalImage);
            byte[] imageByteArray = Files.readAllBytes(rawImage.toPath());
            ImageDescription imgDescription = compVisCl.computerVision().describeImageInStream().withImage(imageByteArray).execute();
            List<ImageCaption> captions = imgDescription.captions();
            if (!captions.isEmpty()){
                ImageCaption mainCaption = captions.get(0);
                log.info("mainCaption text="+mainCaption.text());
                log.info("mainCaption confidence="+mainCaption.confidence());
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return;
    }
    */

    public static String describeImage(ComputerVisionClient compVisCl, InputStream imageStream) {
        //String pathToLocalImage = "src/main/resources/biche.jpg";
        String returnText = "empty Caption";
        try {
            // <snippet_analyzelocal_analyze>
            // Need a byte array for analyzing a local image.
            //File rawImage = new File(pathToLocalImage);
            //byte[] imageByteArray = Files.readAllBytes(rawImage.toPath());
            //byte[] imageByteArray = new byte[(int) imageBinary.getSize()] ;
            //imageBinary.read(imageByteArray, 0);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = imageStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] imageByteArray = buffer.toByteArray();
            // With Java V9, remplace all this by : byte[] imageByteArray = imageStream.readAllBytes();

            // read all data from input stream to array
            ImageDescription imgDescription = compVisCl.computerVision().describeImageInStream().withImage(imageByteArray).execute();
            List<ImageCaption> captions = imgDescription.captions();
            if (!captions.isEmpty()){
                ImageCaption mainCaption = captions.get(0);
                log.info("mainCaption text="+mainCaption.text());
                log.info("mainCaption confidence="+mainCaption.confidence());
                returnText = mainCaption.text() + " (with confidence ="+mainCaption.confidence()+" )";
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return returnText;
    }


}
