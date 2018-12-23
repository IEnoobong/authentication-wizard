package co.enoobong.authenticationwizard.repository;

import co.enoobong.authenticationwizard.model.Role;
import co.enoobong.authenticationwizard.model.RoleName;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findRoleByName(RoleName roleName);
}
