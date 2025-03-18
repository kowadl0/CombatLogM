package me.mooneu.kowal.util;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
    private static final String GITHUB_API_URL = "https://api.github.com/repos/{twoje_repo}/releases/latest"; // Zamień na swoje repozytorium

    public static void main(String[] args) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(GITHUB_API_URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String latestVersion = extractVersionFromResponse(response.toString());

            String currentVersion = "1.0";
            if (isUpdateAvailable(currentVersion, latestVersion)) {
                System.out.println("Jest dostępna nowa wersja: " + latestVersion);
            } else {
                System.out.println("Masz najnowszą wersję pluginu.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extractVersionFromResponse(String response) {
        String versionPrefix = "\"tag_name\":\"";
        int versionStart = response.indexOf(versionPrefix) + versionPrefix.length();
        int versionEnd = response.indexOf("\"", versionStart);
        return response.substring(versionStart, versionEnd);
    }

    private static boolean isUpdateAvailable(String currentVersion, String latestVersion) {
        String[] current = currentVersion.split("\\.");
        String[] latest = latestVersion.split("\\.");

        for (int i = 0; i < current.length; i++) {
            int currentPart = Integer.parseInt(current[i]);
            int latestPart = Integer.parseInt(latest[i]);

            if (latestPart > currentPart) {
                return true;
            } else if (latestPart < currentPart) {
                return false;
            }
        }
        return false;
    }
}