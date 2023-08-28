package io.ioslab.rui.batch.reader.mapper;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import java.util.Objects;

import static io.ioslab.rui.batch.utility.Costants.DELIMITER;

public class CustomLineMapper<T> {

    private final Class<T> genericsClass;
    private FieldSetMapper<T> fieldSetMapper;

    public CustomLineMapper(Class<T> genericsClass) {
        this.genericsClass = genericsClass;
    }

    public CustomLineMapper(Class<T> genericsClass, FieldSetMapper<T> fieldSetMapper) {
        this.genericsClass = genericsClass;
        this.fieldSetMapper = fieldSetMapper;
    }

    public DefaultLineMapper<T> mapper(String... names) {
        DefaultLineMapper<T> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(lineTokenizer(names));
        setCorrectMapper(defaultLineMapper);
        return defaultLineMapper;
    }

    private DelimitedLineTokenizer lineTokenizer(String... names) {
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames(names);
        delimitedLineTokenizer.setDelimiter(DELIMITER);
        return delimitedLineTokenizer;
    }

    private void setCorrectMapper(DefaultLineMapper<T> defaultLineMapper) {
        if (Objects.isNull(fieldSetMapper))
            defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper());
        else
            defaultLineMapper.setFieldSetMapper(fieldSetMapper);
    }

    private BeanWrapperFieldSetMapper<T> beanWrapperFieldSetMapper() {
        BeanWrapperFieldSetMapper<T> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(genericsClass);
        return beanWrapperFieldSetMapper;
    }
}
