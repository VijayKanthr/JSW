package in.jsw.batchservice.batch;

public enum BatchJob {

    ADD_CUSTOMERS_JOB(90L);
    private final Long id;
    BatchJob(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

}
