package me.dgpr.domains.product.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ProductNameSeparator {

    private static final Pattern KOREAN_PATTERN = Pattern.compile("[가-힣0-9]+");
    private static final Pattern ENGLISH_PATTERN = Pattern.compile("[a-zA-Z0-9]+");
    private static final String[] CHOSUNG = {
            "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
            "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
            "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
            "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    };

    public Set<String> separate(String productName) {
        Set<String> words = new HashSet<>();

        Arrays.stream(productName.split(" "))
                .forEach(it -> {
                    if (KOREAN_PATTERN.matcher(it).matches()) {
                        words.add(it);
                        words.add(extractChosungFromWord(it).toString());
                    } else if (ENGLISH_PATTERN.matcher(it).matches()) {
                        words.add(it);
                    }
                });

        return words;
    }

    private StringBuilder extractChosungFromWord(String it) {
        StringBuilder sb = new StringBuilder();
        for (char ch : it.toCharArray()) {
            if (44032 <= ch && ch <= 55203) {
                sb.append(CHOSUNG[(ch - 44032) / 588]);
            }
        }
        return sb;
    }
}
