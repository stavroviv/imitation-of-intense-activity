package org.home.stavrov.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtils {

    private ImageUtils() {
    }

    public static String parseImage(BufferedImage bufferedImage, String patternString) throws TesseractException {
        ITesseract tesseract = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        tesseract.setDatapath(tessDataFolder.getAbsolutePath());
        tesseract.setLanguage("eng");
        String parsedImage = tesseract.doOCR(bufferedImage);
        return findAndPrintMatches(parsedImage, patternString);
    }

    private static String findAndPrintMatches(String input, String patternString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group() : "";
    }
}
