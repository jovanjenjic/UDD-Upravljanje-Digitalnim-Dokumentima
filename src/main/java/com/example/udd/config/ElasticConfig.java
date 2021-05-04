package com.example.udd.config;

import com.example.udd.dto.BetaReaderDTO;
import com.example.udd.model.BetaReader;
import com.example.udd.model.Book;
import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class ElasticConfig extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200").build();

        return RestClients.create(clientConfiguration).rest();
    }

    @PostConstruct
    public void initIndexes() throws IOException {
        RestHighLevelClient highLevelClient = elasticsearchClient();

        GetIndexRequest getIndexRequest = new GetIndexRequest(Book.INDEX_NAME);

        boolean isBookIndexExists = highLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

        // case when book index is not existing
        if(!isBookIndexExists) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(Book.INDEX_NAME);

            createIndexRequest.settings(Settings.builder()
                    .put("index.number_of_shards", 1)
                    .put("index.number_of_replicas", 0));

            XContentBuilder builder = XContentFactory.jsonBuilder();

            builder.startObject();
            {
                builder.startObject("properties");
                {
                    builder.startObject("id");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();

                    builder.startObject("isbn");
                    {
                        builder.field("type", "text");
                        builder.field("store", "true");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject("title");
                    {
                        builder.field("type", "text");
                        builder.field("store", "true");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject("authorFirstName");
                    {
                        builder.field("type", "text");
                        builder.field("store", "true");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject("authorLastName");
                    {
                        builder.field("type", "text");
                        builder.field("store", "true");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject("authorId");
                    {
                        builder.field("type", "long");
                        builder.field("store", "true");
                    }
                    builder.endObject();

                    builder.startObject("content");
                    {
                        builder.field("type", "text");
                        builder.field("store", "true");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject("genre");
                    {
                        builder.field("type", "text");
                        builder.field("store", "true");
                        builder.field("analyzer", "serbian");
                    }
                    builder.endObject();

                    builder.startObject( "point" );
                    {
                        builder.field( "type", "geo_point" );
                        builder.field( "store", "true" );
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();

            createIndexRequest.mapping(builder);

            highLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        }

        // beta reader
        getIndexRequest = new GetIndexRequest(BetaReader.INDEX_NAME);

        boolean isBetaReaderExist = highLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

        if(!isBetaReaderExist) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(BetaReader.INDEX_NAME);

            createIndexRequest.settings(Settings.builder()
                    .put("index.number_of_shards", 1)
                    .put("index.number_of_replicas", 0));

            XContentBuilder builder = XContentFactory.jsonBuilder();

            builder.startObject();
            {
                builder.startObject("properties");
                {
                    builder.startObject("readerId");
                    {
                        builder.field("type", "long");
                        builder.field("store", "true");
                    }
                    builder.endObject();

                    builder.startObject("username");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();

                    builder.startObject("email");
                    {
                        builder.field("type", "keyword");
                    }
                    builder.endObject();

                    builder.startObject("point");
                    {
                        builder.field("type", "geo_point");
                        builder.field("store", "true");
                    }
                    builder.endObject();

                }
                builder.endObject();
            }
            builder.endObject();

            createIndexRequest.mapping(builder);
            // create index
            highLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

            addBetaReadersToIndex(new BetaReaderDTO(43.3209, 21.8958, 1L, "kilje@kilje.com", "kilje"));
            addBetaReadersToIndex(new BetaReaderDTO(45.267136, 19.833549, 2L, "manda@manda.com", "manda"));
            addBetaReadersToIndex(new BetaReaderDTO(44.7866, 20.4489, 3L, "jenja@jenja.com", "jenja"));
            addBetaReadersToIndex(new BetaReaderDTO(48.2082, 16.3738, 4L, "ziki@ziki.com", "ziki"));
            addBetaReadersToIndex(new BetaReaderDTO(45.7733, 19.1151, 5L, "boom@boom.com", "boom"));
            addBetaReadersToIndex(new BetaReaderDTO(55.7558, 37.6173, 6L, "aaa@boom.com", "boom"));
        }
    }

    private void addBetaReadersToIndex(BetaReaderDTO betaReaderDTO) {
        RestHighLevelClient restHighLevelClient = elasticsearchClient();
        BetaReader betaReader = new BetaReader();
        betaReader.setPoint(new GeoPoint(betaReaderDTO.getLatitude(), betaReaderDTO.getLongitude()));
        betaReader.setReaderId(betaReaderDTO.getReaderId());
        betaReader.setEmail(betaReaderDTO.getEmail());
        betaReader.setUsername(betaReaderDTO.getUsername());

        try {
            IndexRequest indexRequest = new IndexRequest(BetaReader.INDEX_NAME);

            indexRequest.id(String.valueOf(betaReaderDTO.getReaderId()));

            indexRequest.source(new Gson().toJson(betaReader), XContentType.JSON);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
