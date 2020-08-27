package hp.boot.web.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringTrimConverter implements Converter<String, String> {

    @Override
    public String convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        return source;
    }
}
