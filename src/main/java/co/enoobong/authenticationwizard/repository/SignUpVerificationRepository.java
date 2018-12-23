package co.enoobong.authenticationwizard.repository;

import co.enoobong.authenticationwizard.model.SignUpVerificationToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpVerificationRepository extends CrudRepository<SignUpVerificationToken, Long> {

    Optional<SignUpVerificationToken> findByToken(String token);
}
