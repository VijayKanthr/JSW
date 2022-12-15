package in.jsw.batchservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BatchJobException extends RuntimeException{

    public BatchJobException() {
        super();
    }

    public BatchJobException(String message) {
        super(message);
    }

}
