package com.disney.teams.utils;

import com.disney.teams.utils.io.CloseUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/17
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/17       arron.zhou      1.0.0          create
 */
public class CompressUtils {

    public static byte[] gZip(byte[] bytes) throws IOException {
        ByteArrayOutputStream bos = null;
        GZIPOutputStream output = null;
        try {
            //默认容量缩减至1/8，减少数据拷贝
            bos = new ByteArrayOutputStream(bytes.length > 1024 ? (bytes.length >>> 3) : bytes.length);
            output = new GZIPOutputStream(bos);
            output.write(bytes);
            output.close();
            return bos.toByteArray();
        } finally {
            CloseUtils.close(bos, output);
        }
    }

    public static byte[] unGZip(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = null;
        GZIPInputStream input = null;
        ByteArrayOutputStream bos = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            input = new GZIPInputStream(bis);
            //预估解压缩后的空间为原来的4倍
            bos = new ByteArrayOutputStream(bytes.length << 2);
            int len;
            byte[] buf = new byte[1024];
            while ((len = input.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            return bos.toByteArray();
        } finally {
            CloseUtils.close(bis, input, bos);
        }
    }


}
