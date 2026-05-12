package com.garagem52.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Gera IDs numéricos sequenciais (Long) para cada collection do MongoDB,
 * replicando o comportamento do AUTO_INCREMENT do MySQL.
 *
 * Usa a collection "database_sequences" com um documento por entidade.
 * A operação findAndModify é atômica — sem race conditions em concorrência.
 */
@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public long nextId(String sequenceName) {
        DatabaseSequence counter = mongoOperations.findAndModify(
                query(where("_id").is(sequenceName)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                DatabaseSequence.class
        );
        return Objects.requireNonNull(counter).getSeq();
    }

    // Documento interno de sequência
    @org.springframework.data.mongodb.core.mapping.Document(collection = "database_sequences")
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class DatabaseSequence {
        @org.springframework.data.annotation.Id
        private String id;
        private long seq;
    }
}
