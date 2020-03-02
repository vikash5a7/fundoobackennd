package com.bridgelabz.fundoonotes.configurations;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {


	@Bean(destroyMethod = "close")
	public RestHighLevelClient client() {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

	}
}