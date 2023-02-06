package in.jsw.batchservice.batch;

public enum BatchJob {

    ADD_CUSTOMERS_JOB(90L),

    IMPORT_CUSTOMERS_CSV_DB(91l);

    private final Long id;
    BatchJob(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

}
