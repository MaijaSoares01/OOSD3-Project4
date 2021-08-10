//Student Name: Maija Soares
//Student Number: C19478224
//Module: OOSD3
//Assignment 4 ~ 30%
//Due Date: 5th May 2020
package assignment;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class Assignment4 {

	public static void main(String[] args) throws IOException{
		try {//try catch exception
			File memory = new File("memory.txt");//a text file called memory 
			File temporary = new File("temporary.txt");//a text file called temporary
			File words = new File("words.txt");//a text file called words
			if(memory.createNewFile()){//if a text file called memory does not exist in the directory it is then created
				System.out.println("File memory.txt has been created.");
			}else{//text file memory already exists
				System.out.println("File memory.txt already exists.");
			}
			if(words.exists()==true) {//if the text file words is in the directory then...... 
				ExecutorService executor = Executors.newFixedThreadPool(5);//fixed amount of Threads-ThreadPool
				FileWriter fw = new FileWriter("memory.txt",true);//FileWriter for memory.txt is set to append and not overwrite
				BufferedWriter bw = new BufferedWriter(fw);//BufferedWriter for memory.txt
				FileWriter fileWriter = new FileWriter("temporary.txt");//FileWriter for temporary.txt
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);//BufferedWriter for temporary.txt
				FileReader fr = new FileReader("words.txt");//FileReader for words.txt
				BufferedReader br = new BufferedReader(fr);//BufferedReader for words.txt
				String s = "";//String s is initiated 
				Runnable wordsThread = new WordThread(s);//Class WordThread is runnable and String s is assigned to it
				Runnable movingThread = new MovingThread(s);//Class MovingThread is runnable and String s is assigned to it 
				fw.flush();//FileWriter for memory.txt is flushed
				bw.flush();//BufferedWriter for memory.txt is flushed
				fileWriter.flush();//FileWriter for temporary.txt is flushed
				bufferedWriter.flush();//BufferedWriter for temporary.txt is flushed
				fw.close();//FileWriter for memory.txt is closed
				bw.close();//BufferedWriter for memory.txt is closed
				fileWriter.close();//FileWriter for temporary.txt is closed
				bufferedWriter.close();//BufferedWriter for temporary.txt is closed
				fr.close();//FileReader for words.txt is closed 
				br.close();//BufferedReader for words.txt is closed
				
				while (!executor.isShutdown()){//keep looping until executor is shutdown
					 fw = new FileWriter("memory.txt",true);//FileWriter for memory.txt is set to append and not overwrite
					 bw = new BufferedWriter(fw);//BufferedWriter for memory.txt
					 fileWriter = new FileWriter("temporary.txt");//FileWriter for temporary.txt
					 bufferedWriter = new BufferedWriter(fileWriter);//BufferedWriter for temporary.txt
					 fr = new FileReader("words.txt");//FileReader for words.txt
					 br = new BufferedReader(fr);//BufferedReader for words.txt
					s = br.readLine();//Sting s is assigned the next line in words.txt
					wordsThread = new WordThread(s);//Class WordThread is runnable and String s is assigned to it
					movingThread = new MovingThread(s);//Class MovingThread is runnable and String s is assigned to it
					bw.append(s);//String s is appended to text file memory 
					bw.newLine();//Go to the next line in text file memory
					executor.execute(wordsThread);//using the ExecutorService run the Threads in the WordThread Class
					s = br.readLine();//Sting s is assigned the next line in words.txt
					while(s != null) {//while String s does not equal null do....
						movingThread = new MovingThread(s);//Class MovingThread is runnable and String s is assigned to it
						bufferedWriter.write(s);//using temporary.txt's BuffereWriter String s is added to the text file
						bufferedWriter.newLine();//Go to the next line in text file temporary
						executor.execute(movingThread);//using the ExecutorService run the Threads in the MovingThread Class
						s = br.readLine();//Sting s is assigned the next line in words.txt
					}
					fw.flush();//FileWriter for memory.txt is flushed
					bw.flush();//BufferedWriter for memory.txt is flushed
					fileWriter.flush();//FileWriter for temporary.txt is flushed
					bufferedWriter.flush();//BufferedWriter for temporary.txt is flushed
					fw.close();//FileWriter for memory.txt is closed
					bw.close();//BufferedWriter for memory.txt is closed
					fileWriter.close();//FileWriter for temporary.txt is closed
					bufferedWriter.close();//BufferedWriter for temporary.txt is closed
					fr.close();//FileReader for words.txt is closed 
					br.close();//BufferedReader for words.txt is closed
					words.delete();//the original words.txt is deleted
			        temporary = new File("temporary.txt");//a text file called temporary
			        words = new File("words.txt");//a text file called words 
			        temporary.renameTo(words);//rename temporary.txt is renamed to words.txt
					if (words.length() == 0) {//if words.txt's length is equal to 0, the file is empty then...
					        executor.shutdown();//executor service can be shutdown!
			        }
		        }
			}else {//else if words.txt is not in the text file then....
				System.out.println("words.txt does not exist.....");
			}
		}catch(IOException e){//catch ioException
			e.printStackTrace();
		}
	}
}

class WordThread implements Runnable {//Class WordThread is runnable
	  
    private String word;//Class WorkThread holds Sting word
    
    public WordThread(String word){
        this.word=word;
    }

    @Override
    public void run() {//Run Thread
    synchronized(word){//synced 
	      System.out.println(Thread.currentThread().getName()+" Start. New Word to learn = " + word);//the current thread is learning word
	      learnWord(word);//learnWord method is called
	      System.out.println(Thread.currentThread().getName()+" End. " + word + " is in text file memory.");//the current thread is finished learning word
      }
    }

    private void learnWord(String word) {//learnWord Method
        try {
        	System.out.println("1 seconds to learn " + word);//thread sleeps for 1 second to learn word
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){//toString method
        return this.word;
    }
}
class MovingThread implements Runnable {//Class MovingThread is runnable
	  
    private String movingWord;//Class MovingThread holds Sting movingWord
    
    public MovingThread(String movingWord){
        this.movingWord=movingWord;
    }

    @Override
    public void run() {//Run Thread
    synchronized(movingWord){//synced 
	      //System.out.println(Thread.currentThread().getName()+" Moving word = " + movingWord);//the current thread is moving word from words.txt to temporary.txt
	      movingWord.notify();
      }
    }

    @Override
    public String toString(){//toString method
        return this.movingWord;
    }
}