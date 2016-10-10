import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class XMLParser {
	private File parseFile;
	private File resultFile;
	private static final int baseDp = 360;
	private int resultDp = 0;
	private float ratio = 0; 
	public XMLParser(File file,int resultDp) {
		// TODO Auto-generated constructor stub
		this.parseFile = file;
		this.resultDp = resultDp;
		ratio = (float)resultDp/baseDp;
		System.out.println("ratio is: " + ratio);
	}
	
	public void start(){
		System.out.println("start parse " + parseFile.getName());
		String absolutePath = parseFile.getAbsolutePath();
		String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
		String resultFilename = "dimens.xml";
		createResultFile(filePath,resultFilename);
		translateFile();
	}
	
	private void createResultFile(String filepath, String filename){
		String dirName = "values-sw" + String.valueOf(resultDp) + "dp";
		if(new File(filepath+File.separator+dirName).mkdir()){
			System.out.println("Make dir success");
		}else{
			System.out.println("Make dir fail");
		}
		
		
		String absolutePath = filepath+File.separator+dirName+File.separator+filename;
		resultFile = new File(absolutePath);
        // if file doesnt exists, then create it
        if (!resultFile.exists()) {
        	try {
				resultFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	

	
	private void translateFile(){
		BufferedReader br = null;		
	    StringBuilder sb = new StringBuilder();
	    String line;
	    String result="";
	    
		try {
			br = new BufferedReader(new FileReader(parseFile));
			line = br.readLine();
		    while (line != null) {
		    	String parsedLine = parseLine(line);
		        sb.append(parsedLine);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    result = sb.toString();
		    String everything = sb.toString();
		    System.out.println(everything);
		    br.close();
		    writeResultFile(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	public String parseLine(String line){ 
		Pattern p = Pattern.compile("\\>.*?\\<");
		Matcher m = p.matcher(line);
		String result="";
		if(m.find()){
			result = (String) m.group().subSequence(1, m.group().length()-1);
			System.out.println("result  is: " + result);
		}else{
			System.out.println("parse fail");
			return line;
		}

		String[] tokens = result.split("a|b|c|d|e|f|g|h|i|j|k|m|n|o|p|q|r|s|t|u|v|w|x|y|z");
	
		//System.out.println(tokens[0]);
		int intValue = Integer.valueOf(tokens[0]);
		//System.out.println("value is: " + intValue);
		
		int resultValue = (int) Math.round(intValue*ratio); 
		 		
		return line.replace(tokens[0],  Integer.toString(resultValue));
	}

	
	private void writeResultFile(String result){
        FileWriter fw;
		try {
			fw = new FileWriter(resultFile.getAbsoluteFile());
	        BufferedWriter bw = new BufferedWriter(fw);
	        // write in file
	        bw.write(result);
	        // close connection
	        bw.close();
	        JOptionPane.showMessageDialog(null, "Write file finish :)", "結果", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Write file fail :(", "結果", JOptionPane.INFORMATION_MESSAGE);

		}
	}
}
