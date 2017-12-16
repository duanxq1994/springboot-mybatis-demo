package com.xinge.demo.common.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图片缩放剪裁
 *
 * @author chunbo
 * @version $Id: CutImageService.java, v 0.1 2016年3月30日 下午7:45:41 chunbo Exp $
 */
public class CutImage {

    public static byte[] cutImageService(BufferedImage image, int cutWide, int cutHigh) {
//        File file = new File(path);
//        String[] imageFormat = path.split("\\.");
        BufferedImage tag = null;
        byte[] imageBytes = null;
        try {
            //读取到图片
//            BufferedImage image = ImageIO.read(file);
            int w = image.getWidth();
            int h = image.getHeight();
            //如果大于1证明是横着的图片

            if (w * 1.0 / h == cutWide * 1.0 / cutHigh) {
                //如果图片原长宽比和要剪裁的比例相等，就等比例缩放
                tag = new BufferedImage(cutWide, cutHigh, BufferedImage.TYPE_INT_RGB);
                //绘制缩小后的图片
                Graphics g = tag.getGraphics();
                g.drawImage(image.getScaledInstance(cutWide, cutHigh, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
                System.out.println("等比例缩放");
            } else if (w * 1.0 / h != cutWide * 1.0 / cutHigh) {
                int difference = 0;
                if (w * 1.0 / cutWide < h * 1.0 / cutHigh) {
                    //先按照缩小的程度相对较少的一边进行等比例缩放
                    int high = (int) ((cutWide * 1.0 / w) * h);
                    tag = new BufferedImage(cutWide, cutHigh, BufferedImage.TYPE_INT_RGB);
                    //算出的等比例高度和要剪切的高度的差值
                    difference = (high - cutHigh) / 2;
                    tag.getGraphics().drawImage(image.getScaledInstance(cutWide, high, java.awt.Image.SCALE_SMOOTH), 0, -difference, null);
                    System.out.println("宽度优先");
                } else {
                    int wide = (int) ((cutHigh * 1.0 / h) * w);
                    tag = new BufferedImage(cutWide, cutHigh, BufferedImage.TYPE_INT_RGB);
                    //算出的等比例宽度和要剪切的宽度的差值
                    difference = (wide - cutWide) / 2;
                    tag.getGraphics().drawImage(image.getScaledInstance(wide, cutHigh, java.awt.Image.SCALE_SMOOTH), -difference, 0, null);
                    System.out.println("长度优先");
                }
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();//新建流
            ImageIO.write(tag, "JPEG", os);//利用ImageIO类提供的write方法，将bi以jpg图片的数据模式写入流
            imageBytes = os.toByteArray();//从流中获取数据数组

        } catch (Exception e) {
            e.printStackTrace();
        }


        return imageBytes;
    }

    /**
     * 转换Image数据为byte数组
     *
     * @param bImage
     * @param format
     * @return
     */
    public static byte[] imageToBytes(BufferedImage bImage, String format) {
//    BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
//    Graphics bg = bImage.getGraphics();
//    bg.drawImage(image, 0, 0, null);
//    bg.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
