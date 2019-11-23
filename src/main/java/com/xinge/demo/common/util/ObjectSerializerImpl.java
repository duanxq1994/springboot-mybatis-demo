package com.xinge.demo.common.util;

import java.io.*;

/**
 * @author BG343674
 * created by BG343674 on 2019/11/23
 */
public class ObjectSerializerImpl<T> implements ObjectSerializer<T> {

    @Override
    public byte[] serialize(T object) {
        if (object == null) {
            return null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

            try {
                ObjectOutputStream ex = new ObjectOutputStream(baos);
                ex.writeObject(object);
                ex.flush();
            } catch (IOException var3) {
                throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), var3);
            }

            return baos.toByteArray();
        }
    }

    @Override
    public T deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            try {
                ObjectInputStream ex = new ObjectInputStream(new ByteArrayInputStream(bytes));
                return (T) ex.readObject();
            } catch (IOException var2) {
                throw new IllegalArgumentException("Failed to deserialize object", var2);
            } catch (ClassNotFoundException var3) {
                throw new IllegalStateException("Failed to deserialize object type", var3);
            }
        }
    }


}
