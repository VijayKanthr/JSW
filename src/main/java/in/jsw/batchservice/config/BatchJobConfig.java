package in.jsw.batchservice.config;


import in.jsw.batchservice.listener.Listener;
import in.jsw.batchservice.model.customer.Customer;
import in.jsw.batchservice.partition.ColumnRangePartitioner;
import in.jsw.batchservice.processor.CustomerProcessor;
import in.jsw.batchservice.processor.FirstItemProcessor;
import in.jsw.batchservice.reader.FirstItemReader;
import in.jsw.batchservice.writer.CustomWriter;
import in.jsw.batchservice.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManager;

@Configuration
public class BatchJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private FirstItemReader firstItemReader;

    @Autowired
    private FirstItemProcessor firstItemProcessor;

    @Autowired
    private FirstItemWriter firstItemWriter;

    @Autowired
    private EntityManager entityManager;

@Autowired
    CustomWriter customWriter;

    @Autowired
    Listener skipListener;
    @Bean
    public CustomerProcessor processor() {
        return new CustomerProcessor();
    }


    @Bean
    public ColumnRangePartitioner partitioner() {
        return new ColumnRangePartitioner();
    }

    @Bean
    public Job launchJob() {
        return jobBuilderFactory.get("importCustomers Job")
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .build();
    }

    private Step firstChunkStep() {
        return stepBuilderFactory.get("First Chunk Step")
                .<Integer, Long>chunk(4)
                .reader(firstItemReader)
                .processor(firstItemProcessor)
                .writer(firstItemWriter)
                .build();
    }

    // Read CSV to DB JOB.

    @Bean
    public Job readCSVJob() {
        return jobBuilderFactory.get("readCSV Job")
                .flow(masterStep())
                .end()
                // .listener()
                .build();
    }

    @Bean
    public Step masterStep() {
        return stepBuilderFactory.get("masterStep").
                partitioner(slaveStep().getName(), partitioner())
                .partitionHandler(partitionHandler())
                .build();
    }
    @Bean
    public Step slaveStep() {
        return stepBuilderFactory.get("slaveStep").<Customer, Customer>chunk(500)
                .reader(flatFileItemReader())
                .processor(processor())
                .writer(customWriter)
                .faultTolerant()
                .skip(Throwable.class)
                .skipLimit(100)
                .listener(skipListener)
                //.skipPolicy(new AlwaysSkipItemSkipPolicy())
                .retryLimit(1)
                .retry(Throwable.class)
                .build();
    }

    @Bean
    public PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
        taskExecutorPartitionHandler.setGridSize(2); //
        taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
        taskExecutorPartitionHandler.setStep(slaveStep());
        return taskExecutorPartitionHandler;
    }
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(2);
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setQueueCapacity(2);
        return taskExecutor;
    }

    /*@Bean
    public FlatFileItemReader<Customer> reader() {
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }*/

    @Bean
    public FlatFileItemReader<Customer> flatFileItemReader() {
        FlatFileItemReader<Customer> flatFileItemReader =
                new FlatFileItemReader<Customer>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        flatFileItemReader.setLineMapper(new DefaultLineMapper<Customer>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {
                    {
                        setTargetType(Customer.class);
                    }
                });
            }
        });
        flatFileItemReader.setLinesToSkip(1);
        return flatFileItemReader;
    }






}
