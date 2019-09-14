package charity.decentralized.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

public class AddressDTO implements Serializable {

    @NotNull
    private String address;

    @NotNull
    private String key;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
            "address='" + address + '\'' +
            ", key='" + key + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDTO that = (AddressDTO) o;
        return Objects.equals(address, that.address) &&
            Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, key);
    }
}
