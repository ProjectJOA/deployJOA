/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.targetServMng.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File; 
import java.io.PrintStream;

import org.apache.maven.cli.MavenCli; 

/**
 * @author Administrator
 *
 */
public class BuildProjectWithMaven {

    public static String createProjectWithMavenCore(String[] args, String outDir) throws Exception{  
 
        class test{
        	
        	protected void main() throws IOException, InterruptedException{
        		
        		String outDir = "";
        		
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                File folder = new File(outDir);

                PrintStream printStream = new PrintStream(output);
                
                ProjectExtractor.extract(folder);

                System.out.println(getOutput(output));    		
                MavenCli cli = new MavenCli();
                int result = cli.doMain(new String[]{"test"}, folder.getAbsolutePath(), printStream, printStream);
        	}
        }
        
        return ""; 
    }
    
    
    private static String getOutput(ByteArrayOutputStream output) { 
        StringBuilder builder = new StringBuilder(); 
        builder.append("\n\n==============================\n"); 
        builder.append("  MAVEN CONSOLE OUTPUT\n"); 
        builder.append("==============================\n"); 
        builder.append(new String(output.toByteArray())); 
        builder.append("\n==============================\n\n"); 
        return builder.toString(); 
    }     
}
