package in.jsw.batchservice.processor;

import in.jsw.batchservice.exception.BatchJobException;
import in.jsw.batchservice.model.customer.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer item) throws Exception {

        if(item.getId()==1){
            throw new BatchJobException("Exception is occurred during processing");
        }
        return item;
    }
}
