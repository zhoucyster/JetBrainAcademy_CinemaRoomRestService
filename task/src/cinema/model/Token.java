package cinema.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Token implements Serializable {
    private final UUID token;

    public Token() {
        this.token = UUID.randomUUID();
    }

    public UUID getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return token.equals(token1.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
