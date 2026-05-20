package com.garagem52.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

/**
 * Remove o campo _class de todos os documentos salvos no MongoDB.
 * Usa InitializingBean para garantir que o converter já foi
 * totalmente inicializado pelo Spring antes de sobrescrever o typeMapper.
 */
@Configuration
public class MongoConfig implements InitializingBean {

    private final MappingMongoConverter mappingMongoConverter;

    public MongoConfig(MappingMongoConverter mappingMongoConverter) {
        this.mappingMongoConverter = mappingMongoConverter;
    }

    @Override
    public void afterPropertiesSet() {
        // null desativa completamente a escrita/leitura do campo _class
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}
