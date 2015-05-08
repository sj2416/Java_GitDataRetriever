import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jcraft.jsch.jce.Random;


/**
 * @author Sweta Jhaveri
 *
 */

public class ExtractLog {
	static final String filePath= "C:\\GraduateAssistantship\\Semester_II\\2048Game\\2048-androidTest\\";
	static FileRepositoryBuilder builder = new FileRepositoryBuilder();
	static Repository repository=null;
	static JSONArray commitDetails = new JSONArray();
	static JSONArray finalCommitDetails= new JSONArray();
	static RevWalk walk=null;
	static JSONArray retainCommitDetails = new JSONArray();
	static JSONObject retainLOC = new JSONObject();
	static HashMap<String, String> lastCommitFileId = new HashMap<String, String>();
	static RevTree tree = null;
	static RevCommit commit = null;
	static Ref head = null;
	static Iterable<RevCommit> logs = null;
	static int count = 0;
	static JSONObject jsonDataset = null;
	static Set<String> countDevelopers = null;
	static String emailIds="";
	static Random random;


	/**
	 * @param args
	 * @throws IOException
	 * @throws GitAPIException
	 */
	/**
	 * @param args
	 * @throws IOException
	 * @throws GitAPIException
	 */
	/**
	 * @param args
	 * @throws IOException
	 * @throws GitAPIException
	 */
	@SuppressWarnings({ "static-access", "deprecation" })
	public static void main(String[] args) throws IOException, GitAPIException {

		random = new Random();
		try {
			RepoClone repoClone=new RepoClone();
			repoClone.repoCloner();
			repository = builder.setGitDir(repoClone.repoDir).setMustExist(true).build();


			HashMap<Integer, Integer> uniqueDates = new HashMap<Integer, Integer>();

			uniqueDates = getUniqueCommitDates();

			commit = walk.parseCommit(head.getObjectId());
			tree = commit.getTree();

			// GET NUMBER OF DEVELOPERS; NUMBER OF COMMITS
			System.out.println("Started Tree walking...");
			int commitSize=0;
			int i=0;

			for (Integer key: uniqueDates.keySet()) {

				TreeWalk treeWalk = new TreeWalk(repository);
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);


				while (treeWalk.next()) {
					if(treeWalk.getPathString().endsWith(".xml") || treeWalk.getPathString().endsWith(".java") ){
						int numDeleted=0;
						int numAdded=0;
						jsonDataset= new JSONObject();
						retainLOC= new JSONObject();
						countDevelopers = new HashSet<String>();

						count = 0;
						int bugCount=0;
						ArrayList<ObjectId> commitIds= new ArrayList<ObjectId>();

						ArrayList<String> ids= new ArrayList<String>();


						int loc= 0;

						logs = new Git(repository).log()
								.addPath(treeWalk.getPathString())
								.call();
						for (RevCommit rev : logs) {
							if (rev.getCommitterIdent().getWhen().getMonth()==key && 
									(1900+rev.getCommitterIdent().getWhen().getYear()) == uniqueDates.get(key)) {

								commitIds.add(rev.getId());
								countDevelopers.add(rev.getAuthorIdent().getEmailAddress());
								count++;	
								if(rev.getFullMessage().contains("bug")) {
									bugCount++;
								}
							}
						}
						for(int commitIdsCount=0;commitIdsCount<commitIds.size();commitIdsCount++)
						{
							ids.add(commitIds.get(commitIdsCount).toString().substring(7, 47));

						}
						int fileCount=0;
						if(commitIds.size()!=0) {
							//FIRST MONTH
							if(i==0)
							{
								for(int idsCount=0;idsCount<ids.size()-1;idsCount++)
								{
									int result[]= diffFiles(ids.get(idsCount+1).toString(), ids.get(idsCount).toString(), treeWalk.getPathString());
									numDeleted+=result[0];
									numAdded+=result[1];
									fileCount++;
								}

							}
							//REST OF THE MONTHS
							else
							{
								for(int idsCount=0;idsCount<ids.size()-1;idsCount++)
								{
									int result[]= diffFiles(ids.get(idsCount+1).toString(), ids.get(idsCount).toString(), treeWalk.getPathString());
									numDeleted+=result[0];
									numAdded+=result[1];
									fileCount++;
								}
								if(fileCount==ids.size()-1)
								{
									if(lastCommitFileId.get(treeWalk.getPathString())!=null)
									{
										int result[]= diffFiles(lastCommitFileId.get(treeWalk.getPathString()),ids.get(ids.size()-1).toString(), treeWalk.getPathString());
										numDeleted+=result[0];
										numAdded+=result[1];
										fileCount++;
									}
								}
							}


							loc= getLOC(commitIds.get(0), treeWalk.getPathString());

							//STORE LAST COMMIT FOR THE MONTH FOR EACH FILE
							if(lastCommitFileId.get(treeWalk.getPathString())!=null)
							{
								lastCommitFileId.remove(treeWalk.getPathString());
								lastCommitFileId.put(treeWalk.getPathString().toString(), ids.get(0).toString());
							}
							else
							{
								lastCommitFileId.put(treeWalk.getPathString().toString(), ids.get(0).toString());
							}

						}
						if(i==0) {
							retainLOC.put("LOC", loc);
							retainCommitDetails.put(retainLOC);
							jsonDataset.put("FileName", treeWalk.getPathString());
							jsonDataset.put("CountDevelopers", countDevelopers.size());
							jsonDataset.put("CountCommits",count);
							jsonDataset.put("LOC", loc);
							jsonDataset.put("CountBugFix", bugCount);
							jsonDataset.put("CountLinesDeleted", numDeleted);
							jsonDataset.put("CountLinesAdded", numAdded);
							commitDetails.put(jsonDataset);
						}
						else {
							JSONObject temp=commitDetails.getJSONObject(commitSize);
							temp.put("FileName", treeWalk.getPathString());
							temp.put("CountDevelopers", countDevelopers.size());
							temp.put("CountCommits",count);
							if(loc!=0){
								temp.put("LOC", loc);
							}
							temp.put("CountBugFix", bugCount);
							temp.put("CountLinesDeleted", numDeleted);
							temp.put("CountLinesAdded", numAdded);
							commitSize++;
						}
					}

				}
				generateCSV(key, uniqueDates.get(key));

				/* SWAPPING BACKUP*/
				if(i==0)
				{
					commitDetails= new JSONArray();
					jsonDataset= new JSONObject();
					for(int locIt=0;locIt<retainCommitDetails.length();locIt++)
					{
						retainLOC=retainCommitDetails.getJSONObject(locIt);
						jsonDataset=retainLOC;
						commitDetails.put(jsonDataset);
					}
					retainCommitDetails= new JSONArray();
					commitSize=0;
				}
				else
				{
					//SWAPPING VALUES FROM CURRENT OBJECT TO BACKUP
					retainCommitDetails= new JSONArray();
					jsonDataset= new JSONObject();
					for(int locIt=0;locIt<commitDetails.length();locIt++)
					{
						retainLOC= new JSONObject();
						retainLOC.put("LOC", commitDetails.getJSONObject(locIt).get("LOC").toString());
						retainCommitDetails.put(retainLOC);
					}

					//CLEARING CURRENT OBJEC TO HOLD ONLY LOC VALUES FOR THE NEXT ROUND
					commitDetails= new JSONArray();
					jsonDataset= new JSONObject();
					for(int locIt=0;locIt<retainCommitDetails.length();locIt++)
					{
						retainLOC=retainCommitDetails.getJSONObject(locIt);
						jsonDataset=retainLOC;
						commitDetails.put(jsonDataset);
					}
					retainCommitDetails= new JSONArray();
					commitSize=0;
				}
				i++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			repository.close();
			System.out.println("EXIT");
		}
	}

	@SuppressWarnings("deprecation")
	private static HashMap<Integer, Integer> getUniqueCommitDates() throws IOException, NoHeadException, GitAPIException {
		head = repository.getRef("HEAD");
		walk = new RevWalk(repository);

		logs = new Git(repository).log().call();
		HashMap<Integer, Integer> tempRevDate = new HashMap<Integer, Integer>();

		for (RevCommit rev : logs) {
			tempRevDate.put(rev.getCommitterIdent().getWhen().getMonth(),1900+rev.getCommitterIdent().getWhen().getYear());
		}		

		return tempRevDate;
	}

	public static void generateCSV(Integer key, Integer value) throws JSONException, IOException {
		System.out.println("Started JSON to CSV conversion");
		String csv = CDL.toString(commitDetails);
		File file = new File(filePath+""+key+"fromJSON.csv");

		FileUtils.writeStringToFile(file, csv);
		System.out.println("File Generated for Month:"+key+" and Year:"+value);
	}

	public static int getLOC(ObjectId lastCommitId, String fileName) throws MissingObjectException, IncorrectObjectTypeException, IOException {

		RevWalk revWalk = new RevWalk(repository);
		RevCommit commit = revWalk.parseCommit(lastCommitId);
		RevTree tree = commit.getTree();

		// now try to find a specific file
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.create(fileName));
		if (!treeWalk.next()) {
			throw new IllegalStateException("Did not find expected file: "+fileName);
		}

		ObjectId objectId = treeWalk.getObjectId(0);
		ObjectLoader loader = repository.open(objectId);

		// and then one can the loader to read the file
		int count= countLines(loader.openStream());


		revWalk.dispose();

		repository.close();
		return count;
	}

	public static int countLines(InputStream is) throws IOException {
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	public static int[] diffFiles(String oldCommit, String newCommit, String fileName) throws IOException, GitAPIException {

		AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, oldCommit);
		AbstractTreeIterator newTreeParser = prepareTreeParser(repository, newCommit);

		List<DiffEntry> diffs= new Git(repository).diff()
				.setNewTree(newTreeParser)
				.setOldTree(oldTreeParser)
				.setPathFilter(PathFilter.create(fileName))
				.call();
		int deletedCount=0;
		int addedCount=0;
		for (DiffEntry entry : diffs) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			DiffFormatter formatter = new DiffFormatter(bos);
			formatter.setRepository(repository);
			formatter.format(entry);
			String out=bos.toString("UTF-8");
			String[] lines = out.split("\n");

			for(int i=0;i<lines.length;i++)
			{
				System.out.println(lines[i]);
				if(lines[i].startsWith("-"))
				{
					deletedCount++;
				}
				if(lines[i].startsWith("+"))
				{
					addedCount++;
				}
			}
			if(deletedCount==1)
			{
				deletedCount=0;
			}
			else
			{
				deletedCount=deletedCount-1;
			}
			if(addedCount==1)
			{
				addedCount=0;
			}
			else
			{
				addedCount=addedCount-1;
			}

			System.out.println("Deleted:"+deletedCount+"Added:"+addedCount);
		}

		return new int[] {deletedCount, addedCount};
	}

	private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException,
	MissingObjectException,
	IncorrectObjectTypeException {
		// from the commit we can build the tree which allows us to construct the TreeParser
		RevWalk walk = new RevWalk(repository);
		RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
		RevTree tree = walk.parseTree(commit.getTree().getId());;

		CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
		ObjectReader oldReader = repository.newObjectReader();
		try {
			oldTreeParser.reset(oldReader, tree.getId());
		} finally {
			oldReader.release();
		}

		walk.dispose();

		return oldTreeParser;
	}

}
