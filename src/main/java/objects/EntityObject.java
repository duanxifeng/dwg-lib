package objects;

import java.util.ArrayList;
import java.util.List;

import bitstreams.BitBuffer;
import bitstreams.CmColor;
import bitstreams.Handle;
import dwglib.FileVersion;

public abstract class EntityObject extends CadObject {


    private Handle parentHandle;
    private Handle layerHandle;
    public Handle linetypeHandle;
    public Handle materialHandle;
    public Handle plotstyleHandle;

    public void readPostCommonFields(BitBuffer dataStream, BitBuffer stringStream, BitBuffer handleStream, FileVersion fileVersion) {

        // 19.4.1 Common entity data, page 104

        boolean graphicImageFlag = dataStream.getB();
        if (graphicImageFlag) {
            long sizeOfGraphicImageInBytes = dataStream.getBLL();

            for (int i = 0; i < sizeOfGraphicImageInBytes*8; i++) {
                dataStream.getB();
            }
        }

        int entMode = dataStream.getBB();
        numReactors = dataStream.getBS();
        
        // It appears that the xDicMissingFlag is not included.  As it has been known before for fields in the spec to actually not be there,
        // a process of skipping the reading of each field in turn was tried.  Perhaps the xdic is never there for entities.  
        // TODO This needs more investigation.

//        xDicMissingFlag = dataStream.getB();
        if (fileVersion.is2013OrLater()) {
            boolean hasBinaryData = dataStream.getB();
        }
        boolean areLinkersPresent = dataStream.getB();
        CmColor entityColor = dataStream.getENC();
        double linetypeScale = dataStream.getBD();
        int linetypeFlag = dataStream.getBB();
        int plotstyleFlag = dataStream.getBB();
        int materialFlag = dataStream.getBB();
        int shadowFlags = dataStream.getRC();
        boolean hasFullVisualStyle = dataStream.getB();
        boolean hasFaceVisualStyle = dataStream.getB();
        boolean hasEdgeVisualStyle = dataStream.getB();
        int isInvisible = dataStream.getBS();
        int entityLineweightFlag = dataStream.getRC();

        // 19.4.2 Common Entity Handle Data page 105

        if (this instanceof Attrib || this instanceof Attdef || this instanceof Point) { // not here????
            parentHandle = handleStream.getHandle(handleOfThisObject);
        }
        
        List<Handle> reactorHandles = new ArrayList<>();
        for (int i = 0; i< numReactors; i++) {
            Handle reactorHandle = handleStream.getHandle(handleOfThisObject);
            reactorHandles.add(reactorHandle);
        }

        // These seem to be not present???
//        if (!xDicMissingFlag) {
//            Handle xdicobjhandle = handleStream.getHandle();
//        }
//        
//        Handle colorBookColorHandle = handleStream.getHandle();
        
        layerHandle = handleStream.getHandle();
        if (linetypeFlag == 3) {
            linetypeHandle = handleStream.getHandle();
        }
        if (materialFlag == 3) {
            materialHandle = handleStream.getHandle();
        }
        if (plotstyleFlag == 3) {
            plotstyleHandle = handleStream.getHandle();
        }
        
        readObjectTypeSpecificData(dataStream, stringStream, handleStream, fileVersion);
    }    
}
