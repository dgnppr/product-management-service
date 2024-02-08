package me.dgpr.persistence.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MoneyTest {

    @ParameterizedTest(name = "{0} >= {1} = {2}")
    @CsvSource({
            "100, 100, true",
            "100, 101, false",
            "100, 99, true",
    })
    void isEqualOrGreater_withVariousCases(int amount1, int amount2, boolean expected) {
        //Arrange
        var money1 = Money.of(BigDecimal.valueOf(amount1));
        var money2 = Money.of(BigDecimal.valueOf(amount2));

        //Act
        boolean actual = money1.isEqualOrGreater(money2);

        //Assert
        assertThat(actual).isEqualTo(expected);
    }
}