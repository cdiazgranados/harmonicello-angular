package rocks.zipcode.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.Presets;

/**
 * Spring Data JPA repository for the Presets entity.
 */
@Repository
public interface PresetsRepository extends JpaRepository<Presets, Long> {
    @Query("select presets from Presets presets where presets.user.login = ?#{principal.username}")
    List<Presets> findByUserIsCurrentUser();

    default Optional<Presets> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Presets> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Presets> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct presets from Presets presets left join fetch presets.user",
        countQuery = "select count(distinct presets) from Presets presets"
    )
    Page<Presets> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct presets from Presets presets left join fetch presets.user")
    List<Presets> findAllWithToOneRelationships();

    @Query("select presets from Presets presets left join fetch presets.user where presets.id =:id")
    Optional<Presets> findOneWithToOneRelationships(@Param("id") Long id);
}
