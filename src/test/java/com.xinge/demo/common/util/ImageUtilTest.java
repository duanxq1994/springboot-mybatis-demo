package com.xinge.demo.common.util;

import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * 图片压缩测试
 * @author duanxq
 * @date 2018/2/10
 */
public class ImageUtilTest {

    @Test
    public void compress() throws IOException {
        String filePath = "C:\\Users\\jiexing-pc\\Desktop\\257413513315139977.jpg";
        String fileOutPath = "C:\\Users\\jiexing-pc\\Desktop\\1.jpg";
        Thumbnails.of(filePath).scale(0.5).outputQuality(0.25).toFile(fileOutPath);
        File compress = new File(fileOutPath);
        assertNotNull(compress);
        assertTrue(compress.exists());
//        assertTrue(compress.delete());
    }
}