package me.dgpr.domains.product.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Stream;
import me.dgpr.domains.product.service.ProductNameSeparator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductNameSeparatorTest {

    private static final ProductNameSeparator sut = new ProductNameSeparator();

    @MethodSource("provideProductNamesForSeparation")
    @ParameterizedTest(name = "상품 이름: {0} -> {1}")
    void 상품_이름을_분할하여_한글은_초성으로_영어는_단어로_분리된_집합을_반환한다(
            final String productName,
            final Set<String> expected
    ) {
        //Act
        Set<String> actual = sut.separate(productName);

        //Assert
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    static Stream<Arguments> provideProductNamesForSeparation() {
        return Stream.of(
                Arguments.of(
                        "아메리카노 라지",
                        Set.of("아메리카노", "ㅇㅁㄹㅋㄴ", "라지", "ㄹㅈ")
                ),
                Arguments.of(
                        "바닐라 라떼",
                        Set.of("바닐라", "ㅂㄴㄹ", "라떼", "ㄹㄸ")
                ),
                Arguments.of(
                        "카페 모카",
                        Set.of("카페", "ㅋㅍ", "모카", "ㅁㅋ")
                ),
                Arguments.of(
                        "그린티 프라푸치노",
                        Set.of("그린티", "ㄱㄹㅌ", "프라푸치노", "ㅍㄹㅍㅊㄴ")
                )
        );
    }
}