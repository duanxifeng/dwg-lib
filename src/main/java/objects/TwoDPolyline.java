package objects;

import java.util.ArrayList;
import java.util.List;

import bitstreams.BitBuffer;
import bitstreams.Handle;
import bitstreams.Point3D;
import dwglib.FileVersion;

public class TwoDPolyline extends EntityObject {

    public int flags;
    public int curveType;
    public double startWidth;
    public double endWidth;
    public double thickness;
    public double elevation;
    public Point3D extrusion;
    public List<Handle> ownedObjectHandles;
    public Handle seqEndHandle;

    @Override
    public void readObjectTypeSpecificData(BitBuffer dataStream, BitBuffer stringStream, BitBuffer handleStream, FileVersion fileVersion) {

        flags = dataStream.getBS();
        curveType = dataStream.getBS();
        startWidth = dataStream.getBD();
        endWidth = dataStream.getBD();
        thickness = dataStream.getBT();
        elevation = dataStream.getBD();
        extrusion = dataStream.getBE();
        int ownedObjectCount = dataStream.getBL();

        // The Handles

        ownedObjectHandles = new ArrayList<>();
        for (int i = 0; i< ownedObjectCount; i++) {
            Handle ownedAttributeHandle = handleStream.getHandle();
            ownedObjectHandles.add(ownedAttributeHandle);
        }

        seqEndHandle = handleStream.getHandle();

        handleStream.advanceToByteBoundary();

        dataStream.assertEndOfStream();
        stringStream.assertEndOfStream();
        handleStream.assertEndOfStream();
    }

    public String toString() {
        return "2D POLYLINE";
    }
}
