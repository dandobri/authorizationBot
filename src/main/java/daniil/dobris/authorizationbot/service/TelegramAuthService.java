package daniil.dobris.authorizationbot.service;

import daniil.dobris.authorizationbot.dto.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TelegramAuthService {
    @Value("${telegram.bot.token}")
    private String botToken;

    public Optional<TelegramUser> validateAndExtractUser(String initData) {
        Map<String, String> data = parseInitData(initData);

        if (!isDataValid(data)) {
            return Optional.empty();
        }

        return Optional.of(mapToTelegramUser(data));
    }

    private Map<String, String> parseInitData(String initData) {
        Map<String, String> result = new HashMap<>();
        for (String pair : initData.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                result.put(kv[0], URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return result;
    }

    public boolean isDataValid(Map<String, String> data) {
        String hash = data.get("hash");
        String dataCheckString = data.entrySet().stream()
                .filter(e -> !"hash".equals(e.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] secretKey = md.digest(botToken.getBytes(StandardCharsets.UTF_8));

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            byte[] hmac = mac.doFinal(dataCheckString.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hmac) {
                sb.append(String.format("%02x", b));
            }
            String calculatedHash = sb.toString();
            log.info("Checking hash was correctly: {}", calculatedHash);
            return calculatedHash.equals(hash);
        } catch (Exception e) {
            log.error("Error while checking data ", e);
            return false;
        }
    }

    public TelegramUser mapToTelegramUser(Map<String, String> data) {
        String firstName = data.getOrDefault("first_name", "");
        String lastName = data.getOrDefault("last_name", "");
        String userName = data.getOrDefault("username", "");
        String photoUrl = data.getOrDefault("photo_url", "");
        return new TelegramUser(firstName, lastName, userName, photoUrl);
    }
}
