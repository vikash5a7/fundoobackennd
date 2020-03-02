package com.bridgelabz.fundoonotes.servicesimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.configurations.ElasticSearchConfig;
import com.bridgelabz.fundoonotes.repositoryimpl.NoteRepository;
import com.bridgelabz.fundoonotes.services.ElasticSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@SuppressWarnings("unchecked")
public class ElasticSearchServiceImplementation implements ElasticSearchService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private ElasticSearchConfig config;

	@Autowired
	private ObjectMapper objectmapper;

	private static final String INDEX = "bridgelabz";

	private static final String TYPE = "note";

	@Override
	public String createNote(NoteInformation note) {
		Map<String, Object> notemapper = objectmapper.convertValue(note, Map.class);
		IndexRequest indexrequest = new IndexRequest(INDEX, TYPE, String.valueOf(note.getId())).source(notemapper);
		IndexResponse indexResponse = null;
		try {
			indexResponse = config.client().index(indexrequest, RequestOptions.DEFAULT);
			return indexResponse.getResult().name();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	@Override
	public void updateNote(Long noteId) {
		NoteInformation note = noteRepository.findById(noteId);
		Map<String, Object> notemapper = objectmapper.convertValue(note, Map.class);
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(note.getId())).doc(notemapper);
		UpdateResponse updateResponse = null;
		try {
			updateResponse = config.client().update(updateRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			System.out.println(updateResponse);
			System.out.println(e.getMessage());
		}

	}

	@Override
	public String deleteNote(Long noteId) {
		NoteInformation note = noteRepository.findById(noteId);
		DeleteRequest deleterequest = new DeleteRequest(INDEX, TYPE, String.valueOf(note.getId()));
		DeleteResponse deleteResponse = null;
		try {
			deleteResponse = config.client().delete(deleterequest, RequestOptions.DEFAULT);
			return deleteResponse.getResult().name();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public List<NoteInformation> searchbytitle(String title) {
		SearchRequest searchrequest = new SearchRequest("bridgelabz");
		SearchSourceBuilder searchsource = new SearchSourceBuilder();
		searchsource.query(QueryBuilders.matchQuery("title", title));
		searchrequest.source(searchsource);
		SearchResponse searchresponse = null;
		try {
			searchresponse = config.client().search(searchrequest, RequestOptions.DEFAULT);
			return getResult(searchresponse);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	private List<NoteInformation> getResult(SearchResponse searchresponse) {
		SearchHit[] searchhits = searchresponse.getHits().getHits();
		List<NoteInformation> notes = new ArrayList<>();
		if (searchhits.length > 0) {
			Arrays.stream(searchhits)
					.forEach(hit -> notes.add(objectmapper.convertValue(hit.getSourceAsMap(), NoteInformation.class)));
		}
		return notes;
	}

}
