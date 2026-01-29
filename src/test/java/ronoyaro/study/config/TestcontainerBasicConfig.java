package ronoyaro.study.config;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("itest")
@Import(TestcontainersConfiguration.class)
public class TestcontainerBasicConfig {
}
