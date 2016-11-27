package com.onespatial.dwglib.objects;

import java.util.AbstractList;
import java.util.List;

import com.onespatial.dwglib.FileVersion;
import com.onespatial.dwglib.bitstreams.BitBuffer;
import com.onespatial.dwglib.bitstreams.Handle;
import com.onespatial.dwglib.bitstreams.Point3D;

public class TwoDPolyline extends EntityObject {

    public int flags;
    public int curveType;
    public double startWidth;
    public double endWidth;
    public double thickness;
    public double elevation;
    public Point3D extrusion;
    private Handle[] ownedObjectHandles;
    private Handle seqEndHandle;

    public TwoDPolyline(ObjectMap objectMap) {
        super(objectMap);
    }

    @Override
    public void readObjectTypeSpecificData(BitBuffer dataStream, BitBuffer stringStream, BitBuffer handleStream, FileVersion fileVersion) {
        // 19.4.16 2D POLYLINE (15) page 115

        flags = dataStream.getBS();
        curveType = dataStream.getBS();
        startWidth = dataStream.getBD();
        endWidth = dataStream.getBD();
        thickness = dataStream.getBT();
        elevation = dataStream.getBD();
        extrusion = dataStream.getBE();
        int ownedObjectCount = dataStream.getBL();

        // The Handles

        ownedObjectHandles = new Handle[ownedObjectCount];
        for (int i = 0; i< ownedObjectCount; i++) {
            ownedObjectHandles[i] = handleStream.getHandle();
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

    public List<EntityObject> getOwnedObjects()
    {
        return new AbstractList<EntityObject>() {

            @Override
            public EntityObject get(int index)
            {
                CadObject result = objectMap.parseObject(ownedObjectHandles[index]);
                return (EntityObject) result;
            }

            @Override
            public int size()
            {
                return ownedObjectHandles.length;
            }
        };
    }

    public SeqEnd getSeqEnd() {
        CadObject result = objectMap.parseObjectPossiblyNull(seqEndHandle);
        return (SeqEnd) result;
    }

}