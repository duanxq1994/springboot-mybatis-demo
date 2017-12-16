package com.xinge.demo.common.util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

public class ImageCompressUtil {

    /**
     * @param im          原始图像
     * @param resizeTimes 倍数,比如0.5就是缩小一半,0.98等等double类型
     * @return 返回处理后的图像
     */
    private static BufferedImage zoomImage(String src) {

        BufferedImage result = null;

        try {
            File srcfile = new File(src);
            if (!srcfile.exists()) {
                System.out.println("文件不存在");

            }
            BufferedImage im = ImageIO.read(srcfile);

            /* 原始图像的宽度和高度 */
            int width = im.getWidth();
            int height = im.getHeight();

            // 压缩计算
            float resizeTimes = 1f; /* 这个参数是要转化成的倍数,如果是1就是转化成1倍 */

            /* 调整后的图片的宽度和高度 */
            int toWidth = (int) (width * resizeTimes);
            int toHeight = (int) (height * resizeTimes);

            /* 新生成结果图片 */
            result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);

            result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

        } catch (Exception e) {
            System.out.println("创建缩略图发生异常" + e.getMessage());
        }

        return result;

    }

    public static BufferedImage zoomImage(InputStream input) throws IOException {

        BufferedImage result = null;
        if (null == input) {
            return null;
        }
        BufferedImage im = ImageIO.read(input);

        /* 原始图像的宽度和高度 */
        int width = im.getWidth();
        int height = im.getHeight();

        // 压缩计算
        float resizeTimes = 1f; /* 这个参数是要转化成的倍数,如果是1就是转化成1倍 */

        /* 调整后的图片的宽度和高度 */
        int toWidth = (int) (width * resizeTimes);
        int toHeight = (int) (height * resizeTimes);

        /* 新生成结果图片 */
        result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);
        result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

        return result;

    }

    public static byte[] getJPEGImageBytes(BufferedImage bufferedImage) throws IOException {
        /* 输出到数据流 */
        ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
        MemoryCacheImageOutputStream ios = new MemoryCacheImageOutputStream(memoryStream);
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        if (imageWriteParam.canWriteCompressed()) {
            // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT 
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            // 这里指定压缩的程度，参数qality是取值0~1范围内， 
            imageWriteParam.setCompressionQuality(0.9f);
        }
        imageWriter.setOutput(ios);
        imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);
        /* 近JPEG编码 */
        memoryStream.flush();
        memoryStream.close();
        return memoryStream.toByteArray();
    }

    /**
     * 图片转储
     *
     * @param input
     * @param file
     * @throws ImageFormatException
     * @throws IOException
     */
    public static void saveImage(byte[] imageBytes, File file) throws IOException {
        ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
        memoryStream.write(imageBytes, 0, imageBytes.length);
        FileOutputStream fs = new FileOutputStream(file);
        memoryStream.writeTo(fs);
        fs.flush();
        fs.close();
    }

    /**
     * 保存文件
     *
     * @param fileBytes
     * @param path
     * @return
     */
    public static Integer saveFile(byte[] fileBytes, String path) {
        FileOutputStream fop = null;
        Integer code = 0;
        try {
            File file = new File(path);
            File fileParent = new File(file.getParent());
            if (!fileParent.exists()) {
                fileParent.mkdir();
            }
            fop = new FileOutputStream(file);
            fop.write(fileBytes);
            fop.flush();
            fop.close();
        } catch (Exception e) {
            code = -1;
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                code = -1;
                e.printStackTrace();
            }
        }
        return code;
    }

    /**
     * 获取文件名称
     *
     * @param pathObj
     * @return
     */
    public static String getFileName(Object pathObj) {
        String fileName = "未上传";
        if (pathObj instanceof String) {
            String path = (String) pathObj;
            File file = new File(path);
            fileName = file.getName();
        }

        return fileName;
    }

    /**
     * 下载文件
     *
     * @param response
     * @param path     文件所在全路径
     * @throws IOException
     */
    public static void download(HttpServletResponse response, String path) throws IOException {
        File file = new File(path);
        byte[] fileBytes = getFileBytes(file);
        String fileName = file.getName();
        // 设置response的Header
        fileName = URLEncoder.encode(fileName, "UTF-8");
        //解决IE 6.0 bug
        if (fileName.length() > 150) {
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setHeader("Content-Length", "" + fileBytes.length);
        //            response.setContentType("application/x-xls");
        response.getOutputStream().write(fileBytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }


    /**
     * 获取文件的byte[]
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] getFileBytes(File file) throws IOException {
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        fi.close();
        // 确保所有数据均被读取  
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        return buffer;
    }

}
