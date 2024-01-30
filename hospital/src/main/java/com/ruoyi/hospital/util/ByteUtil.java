package com.ruoyi.hospital.util;

import java.io.*;

public class ByteUtil {
    public static Object byteToObject( byte[] bytes) throws IOException, ClassNotFoundException {
        java.lang.Object obj;
        try {
            //bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();

            bi.close();
            oi.close();
        }
        catch(Exception ae) {
            throw ae;
        }
        return obj;
    }


    public static byte[] objectToByte(Object obj) throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);

        bytes = bo.toByteArray();

        bo.close();
        oo.close();
        return(bytes);
    }
}
