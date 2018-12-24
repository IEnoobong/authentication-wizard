package co.enoobong.authenticationwizard.model;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sign_up_verification_token", uniqueConstraints = {@UniqueConstraint(columnNames = "token")})
public class SignUpVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "token", nullable = false, unique = true, updatable = false)
    private String token;

    @NotNull
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @NotNull
    @Column(name = "expiration_date", nullable = false, updatable = false)
    private LocalDateTime expirationDate;

    public SignUpVerificationToken() {
        //default constructor for JPA
    }

    public SignUpVerificationToken(@NotNull User user, @NotBlank String token, @NotNull LocalDateTime expirationDate) {
        this.token = token;
        this.user = user;
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignUpVerificationToken)) return false;

        SignUpVerificationToken that = (SignUpVerificationToken) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!token.equals(that.token)) return false;
        if (!user.equals(that.user)) return false;
        return expirationDate.equals(that.expirationDate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + token.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + expirationDate.hashCode();
        return result;
    }
}
