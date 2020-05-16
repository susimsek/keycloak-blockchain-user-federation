package org.keycloak.fabric.repository.elasticsearch;

import org.keycloak.fabric.model.elasticsearch.UserElasticModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserElasticRepository extends ElasticsearchRepository<UserElasticModel, String> {

    List<UserElasticModel> findByUsernameContainingOrEmailContaining(String username, String email, Pageable pageable);
}
