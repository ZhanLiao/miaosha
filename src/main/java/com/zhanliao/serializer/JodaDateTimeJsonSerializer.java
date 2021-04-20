package com.zhanliao.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/19 20:05
 * @Version: 1.0
 */
public class JodaDateTimeJsonSerializer extends JsonSerializer<DateTime> {


    @Override
    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.toString("yyyy-MM-dd HH:mm:ss"));
    }
}
