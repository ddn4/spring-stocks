package com.vmware.labs.stockservice.stock.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.labs.stockservice.stock.domain.events.DomainEvent;
import com.vmware.labs.stockservice.stock.domain.events.VoidDomainEvent;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

@Slf4j
@Configuration
class PersistenceConfig {

    @WritingConverter
    @RequiredArgsConstructor
    static class DomainEventToJsonConverter implements Converter<DomainEvent, Json> {

        private final ObjectMapper mapper;

        @Override
        public Json convert( DomainEvent source ) {

            try {

                return Json.of( this.mapper.writeValueAsString( source ) );

            } catch( JsonProcessingException e ) {
                log.error( "convert : error converting to json", e );
            }

            return Json.of( "" );
        }

    }

    @ReadingConverter
    @RequiredArgsConstructor
    static class JsonToDomainEventConverter implements Converter<Json, DomainEvent> {

        private final ObjectMapper mapper;

        @Override
        public DomainEvent convert( Json json ) {

            try {

                return this.mapper.readValue( json.asString(), DomainEvent.class );

            } catch( JsonProcessingException e ) {
                log.error( "convert : error converting to domain event", e );
            }

            return new VoidDomainEvent();
        }

    }

    @Bean
    R2dbcCustomConversions r2dbcCustomConversions( final ObjectMapper mapper ) {

        return R2dbcCustomConversions.of( new PostgresDialect(), new JsonToDomainEventConverter( mapper ), new DomainEventToJsonConverter( mapper ) );
    }

}
