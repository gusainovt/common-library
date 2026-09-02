package gusainov.library.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gusainov.library.aspect.LogUserRequestAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
public class LoggingAutoConfiguration {

  @Bean
  public LogUserRequestAspect logUserRequestAspect(ObjectMapper objectMapper) {
    return new LogUserRequestAspect(objectMapper);
  }
}
