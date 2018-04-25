package eu.futuretrust.gtsl.web.repositories;

import eu.futuretrust.gtsl.persistence.entities.SignatureEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SignatureRepository extends MongoRepository<SignatureEntity, String> {
}
