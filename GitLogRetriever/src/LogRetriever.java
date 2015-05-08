import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.io.DisabledOutputStream;

public class LogRetriever {

	public static void  main(String[] s) {

		/*FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository;
		try {
			repository = builder.setGitDir(new File("C:\\GraduateAssistantship\\Semester_II\\2048Game\\2048-android"+"\\.git")).setMustExist(true).build();
			RevWalk rw = new RevWalk(repository);
			ObjectId head = repository.resolve(Constants.HEAD);
			RevCommit commit = rw.parseCommit(head);
			
			Git git = new Git(repository);
			Iterable<RevCommit> log = git.log().add(head).call();
			
			for(@SuppressWarnings("unused") RevCommit commitAgain : log){
				RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
				DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
				df.setRepository(repository);
				df.setDiffComparator(RawTextComparator.DEFAULT);
				df.setDetectRenames(true);
				List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
				for (DiffEntry diff : diffs) {
					System.out.println(MessageFormat.format("({0} {1} {2}", diff.getChangeType().name(), diff.getNewMode().getBits(), diff.getNewPath()));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/

		Calendar cal= Calendar.getInstance();
		System.out.println(cal.get(Calendar.YEAR));

	}
}








//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.eclipse.jgit.api.Git;
//import org.eclipse.jgit.api.errors.GitAPIException;
//import org.eclipse.jgit.api.errors.NoHeadException;
//import org.eclipse.jgit.lib.Constants;
//import org.eclipse.jgit.lib.ObjectId;
//import org.eclipse.jgit.lib.Ref;
//import org.eclipse.jgit.lib.Repository;
//import org.eclipse.jgit.revwalk.DepthWalk.Commit;
//import org.eclipse.jgit.revwalk.RevCommit;
//import org.eclipse.jgit.revwalk.RevWalk;
//import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
//
//
//public class LogRetriever {
//
//	public static void main(String[] s) throws IOException, GitAPIException{
//		FileRepositoryBuilder builder = new FileRepositoryBuilder();
//		Repository repo;
//		try {
//			repo = builder.setGitDir(new File("C:\\GraduateAssistantship\\Semester_II\\2048Game\\2048-android"+"\\.git")).setMustExist(true).build();
//			Git git = new Git(repo);
//			RevWalk walk = new RevWalk(repo);
//			ObjectId head = repo.resolve(Constants.HEAD);
//			int count = 0;
//			List<Ref> branches = git.branchList().call();
//			
//			// retrieves all the branches
//			for (Ref branch : branches) {
//				String branchName = branch.getName();
//				
//				Iterable<RevCommit> log = git.log().add(head).call();
//
//				// iterates through the number of commits
//				//				for (RevCommit rev : log) {
//				//					System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
//				//					count++;
//				//				}
//
//
//				for (RevCommit commit : log) {
//					boolean foundInThisBranch = false;
//
//					RevCommit targetCommit = walk.parseCommit(repo.resolve(commit.getName()));
//					for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
//						if (e.getKey().startsWith(Constants.R_HEADS)) {
//							if (walk.isMergedInto(targetCommit, walk.parseCommit(e.getValue().getObjectId()))) {
//								String foundInBranch = e.getValue().getName();
//								if (branchName.equals(foundInBranch)) {
//									foundInThisBranch = true;
//									break;
//								}
//							}
//						}
//					}
//
//					//displays the commits details
//					if (foundInThisBranch) {
//						count++;
////						System.out.println(commit.getName());
////						System.out.println(commit.getAuthorIdent().getName());
////						System.out.println(new Date(commit.getCommitTime()));
////						System.out.println(commit.getFullMessage());
//					}
//				}
//			}
//
//			System.out.println("Toal number of commits to the project: "+count);
//
//		} catch (NoHeadException e) {
//			e.printStackTrace();
//		}
//	}//PSVM
//}//LogRetriever
//
//
//
//
//
