package kaphein.template.cmstemplatebatis.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

@Configuration
public class ApiJsonConfig
{
    @PostConstruct
    public void onPostConstruct()
    {
        objectMapper.registerModule(createApiModule());
    }

    @Resource(name = "objectMapper")
    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    private com.fasterxml.jackson.databind.Module createApiModule()
    {
        final SimpleModule apiResponseModule = new SimpleModule();
        apiResponseModule.addSerializer(ApiErrorCode.class, new JsonSerializer<ApiErrorCode>()
        {
            @Override
            public void serialize(ApiErrorCode v, JsonGenerator gen, SerializerProvider serializers)
                throws IOException
            {
                gen.writeObject(v.getCode());
            }
        });
        apiResponseModule.addDeserializer(ApiErrorCode.class, new JsonDeserializer<ApiErrorCode>()
        {
            @Override
            public ApiErrorCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
            {
                return ApiErrorCode.valueOf(p.getIntValue()).orElse(null);
            }
        });

        return apiResponseModule;
    }

    private ObjectMapper objectMapper;
}
