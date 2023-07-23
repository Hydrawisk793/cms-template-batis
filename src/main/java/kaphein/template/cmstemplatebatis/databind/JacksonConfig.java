package kaphein.template.cmstemplatebatis.databind;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Jackson을 설정하고 `ObjectMapper`의 bean을 등록한다.
 */
@Configuration
public class JacksonConfig
{
    @Bean
    public ObjectMapper objectMapper()
    {
        return new Jackson2ObjectMapperBuilder()
            .modulesToInstall(
                new Jdk8Module(),
                createJavaTimeModule(),
                createJavaLegacyDateModule(),
                createJavaBigNumberModule(),
                createJavaMapModule()
            )
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .serializationInclusion(JsonInclude.Include.ALWAYS).build();
    }

    private com.fasterxml.jackson.databind.Module createJavaTimeModule()
    {
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Instant.class, new JsonSerializer<Instant>()
        {
            @Override
            public void serialize(Instant v, JsonGenerator gen, SerializerProvider serializers) throws IOException
            {
                gen.writeString(DateTimeFormatter.ISO_INSTANT.format(v));
            }
        });
        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>()
        {
            @Override
            public void serialize(LocalDateTime v, JsonGenerator gen, SerializerProvider serializers) throws IOException
            {
                gen.writeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(v));
            }
        });
        javaTimeModule.addSerializer(OffsetDateTime.class, new JsonSerializer<OffsetDateTime>()
        {
            @Override
            public void serialize(OffsetDateTime v, JsonGenerator gen, SerializerProvider serializers)
                throws IOException
            {
                gen.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(v));
            }
        });
        javaTimeModule.addSerializer(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>()
        {
            @Override
            public void serialize(ZonedDateTime v, JsonGenerator gen, SerializerProvider serializers) throws IOException
            {
                gen.writeString(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(v));
            }
        });

        return javaTimeModule;
    }

    private com.fasterxml.jackson.databind.Module createJavaLegacyDateModule()
    {
        final SimpleModule javaLegacyDateModule = new SimpleModule();
        javaLegacyDateModule.addSerializer(java.util.Date.class, new JsonSerializer<java.util.Date>()
        {
            @Override
            public void serialize(java.util.Date v, JsonGenerator gen, SerializerProvider serializers)
                throws IOException
            {
                gen.writeString(new StdDateFormat().withLenient(true).format(v));
            }
        });
        javaLegacyDateModule.addSerializer(Calendar.class, new JsonSerializer<Calendar>()
        {
            @Override
            public void serialize(Calendar v, JsonGenerator gen, SerializerProvider serializers) throws IOException
            {
                gen.writeString(new StdDateFormat().withColonInTimeZone(true).format(v.getTime()));
            }
        });

        return javaLegacyDateModule;
    }

    private com.fasterxml.jackson.databind.Module createJavaBigNumberModule()
    {
        final SimpleModule javaBigNumberModule = new SimpleModule();
        javaBigNumberModule.addSerializer(BigInteger.class, new JsonSerializer<BigInteger>()
        {
            @Override
            public void serialize(BigInteger v, JsonGenerator gen, SerializerProvider serializers) throws IOException
            {
                gen.writeString(v.toString());
            }
        });
        javaBigNumberModule.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>()
        {
            @Override
            public void serialize(BigDecimal v, JsonGenerator gen, SerializerProvider serializers) throws IOException
            {
                gen.writeString(v.toString());
            }
        });

        return javaBigNumberModule;
    }

    @SuppressWarnings("rawtypes")
    private com.fasterxml.jackson.databind.Module createJavaMapModule()
    {
        final SimpleModule javaMapModule = new SimpleModule();
        javaMapModule.addSerializer(Map.class, new JsonSerializer<Map>()
        {
            @Override
            public void serialize(Map v, JsonGenerator gen, SerializerProvider serializers) throws IOException
            {
                final Set keySet = v.keySet();

                final Map<Class<?>, Integer> keyClassHistogram = new HashMap<>();
                for(Object key : keySet)
                {
                    final Class<?> keyClass = key.getClass();
                    if(keyClassHistogram.containsKey(keyClass))
                    {
                        keyClassHistogram.replace(keyClass, keyClassHistogram.get(keyClass) + 1);
                    }
                    else
                    {
                        keyClassHistogram.put(keyClass, 1);
                    }
                }

                if(
                    keySet.isEmpty()
                    || (
                        1 == keyClassHistogram.size()
                        && keyClassHistogram.getOrDefault(keyClassHistogram.keySet().stream().findFirst().orElse(null), 0) >= keySet.size()
                    )
                )
                {
                    gen.writeStartObject();
                    for(Object key : keySet)
                    {
                        Object value = v.get(key);

                        serializers.defaultSerializeField(key.toString(), value, gen);
                    }
                    gen.writeEndObject();
                }
                else
                {
                    gen.writeStartArray();
                    for(Object key : keySet)
                    {
                        Object value = v.get(key);

                        gen.writeStartArray();
                        serializers.defaultSerializeValue(key, gen);
                        serializers.defaultSerializeValue(value, gen);
                        gen.writeEndArray();
                    }
                    gen.writeEndArray();
                }
            }
        });

        return javaMapModule;
    }
}
