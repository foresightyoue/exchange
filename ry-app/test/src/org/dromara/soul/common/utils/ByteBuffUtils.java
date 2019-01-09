package org.dromara.soul.common.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ByteBuffUtils {
    /**
     * ByteBuffer类是在Java NIO中常常使用的一个缓冲区类，使用它可以进行高效的IO操作，但是，如果对常用方法的理解有错误，那么就会出现意想不到的bug。
     * bufferToString.
     *
     * @param buffer buffer
     * @return java.lang.String
     */
    public static String byteBufferToString(final ByteBuffer buffer) {
        CharBuffer charBuffer;
        try {
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer);
            buffer.flip();
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
