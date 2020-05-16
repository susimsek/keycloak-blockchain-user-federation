package org.keycloak.fabric.model.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = "user", createIndex = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserElasticModel implements Serializable {

    @Id
    private String id;

    private String username;

    private String email;

    private String name;

    private String surname;

    private long createdAt;
}
