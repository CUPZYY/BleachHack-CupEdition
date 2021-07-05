package bleach.hack.util.file;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.io.Resources;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CupGithubReader {

    private static URI url = URI.create("https://raw.githubusercontent.com/CUPZYY/BleachHack-CupEdition-Resources/main/");

    public static List<String> readFileLines(String... path) {
        List<String> st = new ArrayList<>();
        try {
            return Arrays.asList(Resources.toString(stringsToURI(path).toURL(), StandardCharsets.UTF_8).replace("\r", "").split("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return st;
    }

    public static JsonObject readJson(String... path) {
        try {
            String s = Resources.toString(stringsToURI(path).toURL(), StandardCharsets.UTF_8);
            return new JsonParser().parse(s).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URI stringsToURI(String... strings) {
        return url.resolve(StringUtils.join(strings, '/'));
    }
}