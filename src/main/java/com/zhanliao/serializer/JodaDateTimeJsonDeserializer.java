package com.zhanliao.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/19 20:04
 * @Version: 1.0
 */
public class JodaDateTimeJsonDeserializer extends JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String dateString = jsonParser.readValueAs(String.class);
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return DateTime.parse(dateString, dateTimeFormat);

    }
}
