package org.jahiacommunity.ml.image.services;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import org.drools.core.spi.KnowledgeHelper;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.rules.AddedNodeFact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import static org.jahiacommunity.ml.image.Impl.ComputerVisionServiceImpl.*;
import static org.jahiacommunity.ml.image.utils.Constants.endpoint;
import static org.jahiacommunity.ml.image.utils.Constants.subscriptionKey;

public class ComputerVisionService {

    private static final Logger log = LoggerFactory.getLogger(ComputerVisionService.class);
    // Create an authenticated Computer Vision client.


    /*
    public static void ComputerVisionServiceDescribeImage(){
        ComputerVisionClient compVisClient = authenticate(subscriptionKey, endpoint);
        describeImageTest(compVisClient);
    }
     */

    public void describeImageRuleService(AddedNodeFact node, KnowledgeHelper drools) throws RepositoryException {
        ComputerVisionClient compVisClient = authenticate(subscriptionKey, endpoint);
        JCRNodeWrapper nodeWrapper = node.getNode();
        if (nodeWrapper.hasProperty("j:node")) {
            log.info("*** <<<<<<<< has j:node property $$$ ");
            try {
                PropertyIterator jnodeIter = nodeWrapper.getProperties("j:node");
                if ( jnodeIter.hasNext() ) {
                    log.info("*** *** <<<<<<<< has j:node property has NEXT YES! $$$ ");
                    Property prop = jnodeIter.nextProperty();
                    log.info("*** *** <<<<<<<< has j:node property name="+prop.getName()+" $$$ ");
                    log.info("*** *** <<<<<<<< has j:node property string="+prop.getString()+" $$$ ");
                    String jNodeUUID = prop.getString();
                    JCRSessionWrapper session = nodeWrapper.getSession();
                    JCRNodeWrapper imageNode = session.getNodeByIdentifier(jNodeUUID);
                    InputStream imageStream = imageNode.getFileContent().downloadFile();
                    //Binary imageBin = prop.getBinary();
                    String captionText = describeImage(compVisClient, imageStream);
                    log.warn("*** *** <<<<<<<< captionText = $$$ "+captionText);
                    nodeWrapper.setProperty("j:caption",captionText);
                }
            } catch (NoSuchElementException nselEx){
                nselEx.printStackTrace();
            }
        }
    }
}
