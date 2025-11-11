package org.delcom.starter.helper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class AppBase64 {

    public String convertPlainToBase64(String text, String plainPath, String plaintobase64Path) throws IOException {
        // Encode ke Base64
        String base64Encoded = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));

        // Simpan file plain dan hasil base64
        Files.write(Path.of(plainPath), text.getBytes(StandardCharsets.UTF_8));
        Files.write(Path.of(plaintobase64Path), base64Encoded.getBytes(StandardCharsets.UTF_8));

        System.out.println("File berhasil dikonversi ke Base64 dan disimpan di " + plaintobase64Path);
        return base64Encoded;

    }

    public String convertBase64ToPlain(String text, String base64Path, String base64toplainPath) throws IOException {
        // Decode dari Base64
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        String decodedText = new String(decodedBytes, StandardCharsets.UTF_8);

        // Simpan file hasil decode
        Files.write(Path.of(base64Path), text.getBytes(StandardCharsets.UTF_8));
        Files.write(Path.of(base64toplainPath), decodedBytes);

        System.out.println("File berhasil dikonversi dari Base64 dan disimpan di " + base64toplainPath);
        return decodedText;

    }

    // Fungsi konversi grade
    public String getGrade(double nilai) {
        if (nilai >= 79.5)
            return "A";
        else if (nilai >= 72)
            return "AB";
        else if (nilai >= 64.5)
            return "B";
        else if (nilai >= 57)
            return "BC";
        else if (nilai >= 49.5)
            return "C";
        else if (nilai >= 34)
            return "D";
        else
            return "E";
    }
}