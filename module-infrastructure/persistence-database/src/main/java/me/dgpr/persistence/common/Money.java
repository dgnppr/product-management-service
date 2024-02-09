package me.dgpr.persistence.common;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@Embeddable
public class Money {

    public static final String WON = "KRW";
    private final BigDecimal amount;
    private final Currency currency;

    protected Money() {
        this.amount = BigDecimal.ZERO;
        this.currency = Currency.getInstance(WON);
    }

    protected Money(BigDecimal amount) {
        this.amount = amount;
        this.currency = Currency.getInstance(WON);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public boolean isEqualOrGreater(Money other) {
        checkCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    private void checkCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies do not match.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Money other)) {
            return false;
        }

        return this.amount.equals(other.amount) && this.currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return String.format("%s %s", amount, currency.getSymbol());
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

}
