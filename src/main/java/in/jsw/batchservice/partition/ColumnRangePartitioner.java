package in.jsw.batchservice.partition;

import in.jsw.batchservice.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class ColumnRangePartitioner implements Partitioner {

    private static Logger logger = LoggerFactory.getLogger(ColumnRangePartitioner.class);

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {//4
        int min = 1;
        int max = 1000;
        int targetSize = (max - min) / gridSize + 1;//250
        logger.info("targetSize = " + targetSize);
        Map<String, ExecutionContext> result = new HashMap<>();

        int number = 0;
        int start = min;
        int end = start + targetSize - 1;
        //1 to 500
        // 501 to 1000
        while (start <= max) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + number, value);

            if (end >= max) {
                end = max;
            }
            value.putInt("minValue", start);
            value.putInt("maxValue", end);
            start += targetSize;
            end += targetSize;
            number++;
        }
        logger.info("partition result: = " + result.toString());
        return result;
    }
}
