package fr.free.naoj.svnlab.service.impl;

import static fr.free.naoj.svnlab.utils.Lucene.FIELD_DESCRIPTION;
import static fr.free.naoj.svnlab.utils.Lucene.FIELD_ID;
import static fr.free.naoj.svnlab.utils.Lucene.FIELD_TITLE;
import static fr.free.naoj.svnlab.utils.Lucene.LUCENE_VERSION;
import static org.apache.lucene.document.Field.Index.ANALYZED_NO_NORMS;
import static org.apache.lucene.document.Field.Index.NOT_ANALYZED_NO_NORMS;
import static org.apache.lucene.document.Field.Store.NO;
import static org.apache.lucene.document.Field.Store.YES;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSLockFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.free.naoj.svnlab.entity.Commit;
import fr.free.naoj.svnlab.entity.SearchCriteria;
import fr.free.naoj.svnlab.service.SearchEngine;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
@Service("searchEngine")
public class SearchEngineImpl implements SearchEngine {

	private String indexedDirectoryPath;
	
	@Autowired
	public SearchEngineImpl(@Value("${lucene.directoryIndex}") String indexedDirectoryPath) {
		this.indexedDirectoryPath = indexedDirectoryPath;
	}
	
	@Override
	public void index(Commit commit) {
		if (commit != null) {
			boolean indexDone = false;
			IndexWriter writer = null;
			
			while (!indexDone) {
				try {
					writer = getIndexWriter();
					
					Document document = createDocument(commit);
					
					Term term = new Term("id", Long.toString(commit.getId()));
					
					writer.updateDocument(term, document);
					
					writer.commit();
				} catch (Throwable t) {
					// TODO : handle exception
					t.printStackTrace();
				} finally {
					indexDone = true;
					if (writer != null) {
						try {
							writer.close();
						} catch (Throwable t) {
							// TODO : handle exception
						}
					}
				}
			}
		}
	}
	
	@Override
	public List<Commit> search(SearchCriteria criterias) {
		List<Commit> searchedCommit = null;
		
		Directory directory = null;
		IndexReader reader = null;
		IndexSearcher searcher = null;
		
		boolean searchDone = false;
		
		while (!searchDone) {
			try {
				directory = getDirectory();
				reader = IndexReader.open(directory);
				searcher = new IndexSearcher(reader);
				
				TopDocs documents = searcher.search(createQuery(criterias), 20);
				
				if (areDocumentsFound(documents)) {
					searchedCommit = convertDocuments(searcher, documents);
				}
			} catch (Throwable t) {
				// TODO : handle exception
			} finally {
				searchDone = true;
				try {
					closeOrThrow(searcher, reader, directory);
				} catch (Throwable t) {
					// TODO : handle exception
				}
			}
		}
		
		if (searchedCommit == null) {
			searchedCommit = Collections.<Commit> emptyList();
		}
		
		return searchedCommit;
	}

	private Document createDocument(Commit commit) {
		Document document = new Document();
		
		document.add(new Field(FIELD_ID, Long.toString(commit.getId()), YES, NOT_ANALYZED_NO_NORMS));
		document.add(new Field(FIELD_TITLE, commit.getTitle(), YES, NOT_ANALYZED_NO_NORMS));
		document.add(new Field(FIELD_DESCRIPTION, commit.getDescription(), NO, ANALYZED_NO_NORMS));
		
		return document;
	}
	
	private Query createQuery(SearchCriteria criterias) throws ParseException {
		Analyzer analyzer = new StandardAnalyzer(LUCENE_VERSION);
		QueryParser parser = new QueryParser(LUCENE_VERSION, FIELD_DESCRIPTION, analyzer);
		Query query = parser.parse(criterias.getDescription());
		
		return query;
	}
	
	private boolean areDocumentsFound(TopDocs documents) {
		return documents != null && documents.scoreDocs.length > 0;
	}
	
	private List<Commit> convertDocuments(IndexSearcher searcher, TopDocs documents) throws CorruptIndexException, IOException {
		List<Commit> commits = new ArrayList<Commit>(documents.scoreDocs.length);
		
		for (ScoreDoc document : documents.scoreDocs) {
			commits.add(convertScoreDocToCommit(searcher, document));
		}
		
		return commits;
	}
	
	private Commit convertScoreDocToCommit(IndexSearcher searcher, ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
		Document document = searcher.doc(scoreDoc.doc);
		
		Commit commit = new Commit();
		commit.setId(Long.parseLong(document.get(FIELD_ID)));
		commit.setTitle(document.get(FIELD_TITLE));
		
		return commit;
	}
	
	private IndexWriter getIndexWriter() throws IOException {
		Directory indexDirectory = getDirectory();
		indexDirectory.setLockFactory(new SimpleFSLockFactory());
		
		IndexWriterConfig configuration = new IndexWriterConfig(LUCENE_VERSION, new StandardAnalyzer(LUCENE_VERSION));
		configuration.setOpenMode(OpenMode.CREATE_OR_APPEND);
		
		IndexWriter writer = new IndexWriter(indexDirectory, configuration);
		
		return writer;
	}
	
	private void closeOrThrow(Closeable...closeable) throws Exception {
		for (Closeable c : closeable) {
			if (c != null) {
				c.close();
			}
		}
	}
	
	private Directory getDirectory() throws IOException {
		return FSDirectory.open(new File(indexedDirectoryPath));
	}
}
