package ronoyaro.study.utils;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

@Component
public class FileUtils {
    @Autowired
    private ResourceLoader resourceLoader;

    @SneakyThrows
    public String readResourceLoader(String fileName) {
        var file = resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX.concat(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

}
