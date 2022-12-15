package in.jsw.batchservice.writer;


import in.jsw.batchservice.model.customer.Customer;
import in.jsw.batchservice.repository.customer.CustomerRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomWriter implements ItemWriter<Customer> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void write(List<? extends Customer> items) throws Exception {
        customerRepository.saveAll(items);

    }
}
