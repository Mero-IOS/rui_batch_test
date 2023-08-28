package io.ioslab.rui.batch.tasklet;

import lombok.Builder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import static io.ioslab.rui.batch.utility.SqlQuery.*;

@Builder
public class DeleteDuplicateTupleTasklet implements Tasklet {

    private String date;
    private JdbcTemplate jdbcTemplate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        jdbcTemplate.update(DELETE_RUI_CARICHE.getQuery(), date);
        jdbcTemplate.update(DELETE_RUI_COLLABORATORI.getQuery(), date);
        jdbcTemplate.update(DELETE_RUI_INTERMEDIARI.getQuery(), date);
        jdbcTemplate.update(DELETE_RUI_MANDATI.getQuery(), date);
        jdbcTemplate.update(DELETE_RUI_SEDI.getQuery(), date);
        return RepeatStatus.FINISHED;
    }
}
