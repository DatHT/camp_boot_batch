package jp.co.netprotections.batch;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.Delimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jp.co.netprotections.batch.entity.CampMember;
import jp.co.netprotections.batch.repository.CampMemberRepository;
import jp.co.netprotections.batch.service.CampMemberService;
import jp.co.netprotections.batch.springbatch.CampMemberWriter;

/**
 * @author h.dat 入隊基準チェックバッチのコンフェギュレーション
 *
 */
@Configuration // このコンフェギュレーションはspring boot に取り上げられてbeanとdependencyをwire upする
@EnableBatchProcessing // バッチサポートを有効する
public class CampMemberCheckBatch {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	CampMemberService service;

	// consoleにログを書き込む
	private static final Logger logger = LoggerFactory.getLogger(CampMemberCheckBatch.class);

	// 読み込むcsvファイル
	private static final String csvFile = "example.csv";

	/**
	 * csvファイルから読み込む
	 * @return FlatFileItemReader
	 */
	@Bean
	public FlatFileItemReader reader() {
		FlatFileItemReader reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource(csvFile));

		// csvの中で１行ならタイトルなのでスキップする
		reader.setLinesToSkip(1);

		DefaultLineMapper lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

		// CampMemberのmodel classの所属名とマッピングする
		tokenizer.setNames("memberName", "eventPlanning", "cogitation", "coodination", "programmingAbility",
				"infrastructureKnowledge");

		// CsvのデータがObjectにマッピングする
		BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(CampMember.class);

		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(tokenizer);
		reader.setLineMapper(lineMapper);

		return reader;
	}

	/**
	 * chunk()５行を読んで終わったらこの５行をDBに書き込む
	 * @return CampMemberWriter
	 */
	@Bean
	public CampMemberWriter writer() {
		return new CampMemberWriter();
	}

	/**
	 * execメソッド
	 * @return CampMemberWriter
	 */
	@Bean
	public Job importCampMembers(JobExecutionListener listener) {
		return jobBuilderFactory.get("importCampMembers").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step1()).end().build();

	}

	/**
	 * 前提条件設定:Chunk-oriented Processing
	 * https://docs-stage.spring.io/spring-batch/docs/current/reference/html/step.html
	 * @return Step
	 */
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<CampMember, CampMember>chunk(5).reader(reader()).writer(writer())
				.build();
	}

	/**
	 * 実装する前および後のlistener
	 * @return JobExecutionListener
	 */
	@Bean
	public JobExecutionListener listener() {
		return new JobExecutionListener() {

			@Override
			public void beforeJob(JobExecution jobExecution) {
				service.deleteAll();

			}

			@Override
			public void afterJob(JobExecution jobExecution) {
				if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
					logger.info("Job finished!!!!!!");
					service.listAll().
                    forEach(person -> logger.info("----Found <" + person + "> in the database."));
				}

			}
		};
	}

}
