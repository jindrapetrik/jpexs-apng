package com.jpexs.images.apng;

/**
 * CRC calculation class.
 * @author JPEXS
 */
public class Crc {

    /* Table of CRCs of all 8-bit messages. */
    private static long[] crcTable = new long[256];

    /* Flag: has the table been computed? Initially false. */
    private static boolean crcTableComputed = false;

    /* Make the table for a fast CRC. */
    private static void makeCrcTable() {
        long c;
        int n;
        int k;

        for (n = 0; n < 256; n++) {
            c = (long) n;
            for (k = 0; k < 8; k++) {
                if ((c & 1) > 0) {
                    c = 0xedb88320L ^ (c >>> 1);
                } else {
                    c = c >>> 1;
                }
            }
            crcTable[n] = c;
        }
        crcTableComputed = true;
    }

    private static long updateCrc(long crc, byte[] buf) {
        long c = crc;
        int n;

        if (!crcTableComputed) {
            makeCrcTable();
        }
        for (n = 0; n < buf.length; n++) {
            c = crcTable[(int) ((c ^ (buf[n] & 0xFF)) & 0xff)] ^ (c >>> 8);
        }
        return c;
    }

    public static long calculate(byte[]... data) {
        long ret = 0xffffffffL;
        for (byte[] dataPart : data) {
            ret = updateCrc(ret, dataPart);
        }
        ret = ret ^ 0xffffffffL;
        ret = ret & 0xffffffffL;
        return ret;
    }        
}
