package in.jsw.batchservice.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FirstItemWriter implements ItemWriter {

    @Override
    public void write(List items) throws Exception {
        items.stream().forEach(System.out::println);

    }
}
