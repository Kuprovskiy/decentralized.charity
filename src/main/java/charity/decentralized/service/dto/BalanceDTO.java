package charity.decentralized.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class BalanceDTO implements Serializable {

    private BigDecimal amount;

    public BalanceDTO(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceDTO that = (BalanceDTO) o;
        return Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return "BalanceDTO{" +
            "amount=" + amount +
            '}';
    }
}
