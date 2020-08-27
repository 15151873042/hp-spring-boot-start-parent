package hp.boot.web.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String, Date>{
    
    private static final Map<String, String> formartMap = new HashMap<>();
    static {
        formartMap.put("yyyy-MM", "^\\d{4}-\\d{1,2}$");
        formartMap.put("yyyy-MM-dd", "^\\d{4}-\\d{1,2}-\\d{1,2}$");
        formartMap.put("yyyy-MM-dd HH:mm", "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$");
        formartMap.put("yyyy-MM-dd HH:mm:ss", "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$");
    }

    @Override
    public Date convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        
        for (Map.Entry<String, String> entry : formartMap.entrySet()) {
            if (source.matches(entry.getValue())) {
            	try {
					return new SimpleDateFormat(entry.getKey()).parse(source);
				} catch (ParseException e) {
					throw new IllegalArgumentException("日期类型错误！");
				}
            }
        }
        
        throw new IllegalArgumentException("日期类型错误！");
    }

}
