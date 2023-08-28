package io.ioslab.rui.batch.writer;

import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.util.List;

/***
 * @deprecated used only to print for partial test
 */
@Deprecated
public class ConsoleItemWriter extends AbstractItemStreamItemWriter {

    @Override
    public void write(List items) {
        items.forEach(System.out::println);
    }
}
