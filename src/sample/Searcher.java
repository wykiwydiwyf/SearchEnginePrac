package sample;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Searcher {

    private static final String INDEX_DIR = "data/index";

    public static LinkedList<Document> search(String keyword) throws Exception {
        LinkedList<Document>docList = new LinkedList<>();
        IndexSearcher searcher = createSearcher();
        //Search Content
        String keywords[] = keyword.split(" ");
        String query = keywords[0];
        for (int i =1;i<keywords.length;i++){
            query+=" AND " + keywords[i-1]+" AND "+keywords[i];
        }

        TopDocs foundDocs = searchContent(query, searcher);

        System.out.println("Total Results :: " + foundDocs.totalHits);

        for (ScoreDoc sd : foundDocs.scoreDocs) {
            Document d = searcher.doc(sd.doc);
            docList.add(d);
        }
        return docList;
    }

    private static TopDocs searchContent(String Content, IndexSearcher searcher) throws Exception {
        QueryParser qp = new QueryParser("content", new StandardAnalyzer());
        Query ContentQuery = qp.parse(Content);
        TopDocs hits = searcher.search(ContentQuery, 10);
        return hits;
    }

    private static IndexSearcher createSearcher() throws Exception {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);

        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }


}