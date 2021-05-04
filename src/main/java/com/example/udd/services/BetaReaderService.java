package com.example.udd.services;

import com.example.udd.dto.BetaReaderDTO;
import com.example.udd.dto.PointDTO;
import com.example.udd.model.BetaReader;
import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class BetaReaderService {

    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient restHighLevelClient;

    public ResponseEntity<?> addReader(BetaReaderDTO betaReaderDTO) throws IOException {
        BetaReader betaReader = new BetaReader();
        betaReader.setPoint(new GeoPoint(betaReaderDTO.getLatitude(), betaReaderDTO.getLongitude()));
        betaReader.setReaderId(betaReaderDTO.getReaderId());

        IndexRequest indexRequest = new IndexRequest(BetaReader.INDEX_NAME);
        indexRequest.id(String.valueOf(betaReaderDTO.getReaderId()));
        indexRequest.source(new Gson().toJson(betaReader), XContentType.JSON);

        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        return new ResponseEntity<>(indexResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getReadersQuery(PointDTO pointDTO) throws IOException {
        SearchRequest searchRequest = new SearchRequest();

        /* Geo query for looking for in area of specified radius */
        QueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.matchAllQuery())
                .mustNot(
                        QueryBuilders
                            .geoDistanceQuery("point")
                            .point(pointDTO.getLatitude(), pointDTO.getLongitude())
                            .distance(pointDTO.getDistance(), DistanceUnit.KILOMETERS)
                );

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);

        searchRequest.indices(BetaReader.INDEX_NAME);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        ArrayList<BetaReader> betaReaders = new ArrayList<>();

        searchResponse.getHits().forEach(hit -> {
            betaReaders.add(new Gson().fromJson(hit.getSourceAsString(), BetaReader.class));
        });

        return new ResponseEntity<>(betaReaders, HttpStatus.OK);
    }
}
