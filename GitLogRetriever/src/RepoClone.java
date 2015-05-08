import java.io.File;
import java.io.IOException;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;


/**
 * @author Sweta
 * 
 */
public class RepoClone {

    private static final String REMOTE_URL = "https://github.com/uberspot/2048-android.git";
    
    static File repoDir=null;

    public static void repoCloner() throws IOException, InvalidRemoteException, TransportException, GitAPIException {
    	final File localCloneDir = new File("C:\\GraduateAssistantship\\Semester_II\\2048Game\\2048-androidTest");
   
    	if (!localCloneDir.exists()) {
    		if (localCloneDir.mkdir()) {
    			System.out.println("Directory is created!");
    		} else {
    			System.out.println("Failed to create directory!");
    		}
    	} 	
    	
        // then clone
        System.out.println("Cloning from " + REMOTE_URL + " to " + localCloneDir);
        Git result = Git.cloneRepository()
        		.setURI(REMOTE_URL)
                .setDirectory(localCloneDir)
                .call();
        
        repoDir=result.getRepository().getDirectory();

        try {
	        System.out.println("Repository Cloned : " + repoDir);
        } finally {
        	result.close();//close connection to repository
        }
    }
}