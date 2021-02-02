/**
* 
* This code creates a process with the location, parameter and name of the executable file as input. Provides and displays information visually. If desired, it can be closed with the kill button on the application.
* 
* @author  Fatih Selim YAKAR
* @version 1.0
* @since   2020-11-04 
*/

package application;
	

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class Main extends Application {
	
	/**
	 * Data structure holding the table in the application.
	 */
	private TableView<TableProcess> table = new TableView<TableProcess>();
	/**
	 * Holds the current row size in table.
	 */
    private int rowSize=0;
    /**
     * It is an arraylist where the values of timers are kept.
     */
    private ArrayList<Integer>timerRows=new ArrayList<Integer>();
    
    /**
     * This method creates a new process by running the commandline command using java's Runtime.getRuntime (). Exec method.
     * @param directory This is the executable's directory.
     * @param executable This is executable name.
     * @return long Returns the created process's PID.
     */
    private long runProgram(String directory,String executable,String arguments) throws IOException {
    	Process process = Runtime.getRuntime().exec("./"+executable+" "+arguments ,null, new File(directory));
    	return process.pid();
    }
    
    /**
     * Converts integer value expressed in seconds to HH: MM: SS.
     * @param inputSec This is the second type integer input.
     * @return String Returns HH:MM:SS type string.
     */
    private String integerToClock(int inputSec) {
    	int hour=inputSec/3600;
    	int minute=(inputSec-hour*3600)/60;
    	int second=(inputSec-hour*3600-minute*60);
    	return String.format("%d:%d:%d", hour,minute,second);
    }
    
    /**
     * Converts HH: MM: SS string to integer value expressed in seconds.
     * @param inputStr This is the String type hour input variable.
     * @return int Returns second type integer input. 
     */
    private int clockToInteger(String inputStr) {
    	String str[]=inputStr.split(":");
    	return Integer.valueOf(str[0])*3600+Integer.valueOf(str[1])*60+Integer.valueOf(str[2]);
    }
    
    /**
     * Creates an application based on the fxml file, but does not fill the created table. When the start process icon is clicked, it creates a new process and adds it to the table. When you click the kill button, it deletes the process that is currently in the table.
     * @param stage This is the stage type input.
     */
    @SuppressWarnings("unchecked")
	@Override
    public void start(Stage stage) throws Exception {
    	
    	/* loads the Scene.fxml designed application */
    	Parent root = FXMLLoader.load(getClass().getResource("Scene.fxml"));
       
    	/* Creates scene by using root */
        Scene scene = new Scene(root, 600, 400);
        
        /* Assigns the table in the fxml file to the private table. */
        table=(TableView<TableProcess>)scene.lookup("#table");
        
        /* Assigns the button in the fxml file's star process button. */
        Button but=(Button)scene.lookup("#start_process_button");
        
        /* When the star process button is pressed, it adds new process blocks to the table and creates that process with the runProgram method.*/
        but.setOnAction(event -> {
            
        	/* Gets the current textfield parameters in the boxes */
            TextField directory=(TextField)scene.lookup("#directory_box");
            TextField executable=(TextField)scene.lookup("#executable_box");
            TextField arguments=(TextField)scene.lookup("#argument_box");
            
            System.out.println(directory.getText()+" "+executable.getText()+" "+arguments.getText());
            
            /* Runs the process and gets the PID of process */
            long pid;
            try {
				pid=runProgram(directory.getText(),executable.getText(),arguments.getText());
			} catch (IOException e) {
				e.printStackTrace();
				pid=-1;
			}
            
            /* Creates a TableProcess by the passed parameters */
            TableProcess process = new TableProcess(String.valueOf(pid), executable.getText(), arguments.getText(),"0:0:0");
        
            /* Adds the process in the table */
            table.getItems().add(process);
            //TableColumn<TableProcess, ?> process_col = table.getColumns().get(3);
            //System.out.println(process_col.getCellData(rowSize));
            
            /* Adds or updates timerRows with row integer value */
            int row=rowSize;
            if(timerRows.size()==row)
            	timerRows.add(row);
            else
            	timerRows.set(row, row);
            
            /* Creates the timeline timer in running duration cell */
            process.setTimer(new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            	TableProcess temp_process=process;
            	temp_process.setRunningDuration(integerToClock(clockToInteger(temp_process.getRunningDuration())+1));
            	table.getItems().set(timerRows.get(row), temp_process);
            	//System.out.println("timer");
            
            })));
            
            /* starts the timer and increases the rowSize*/
            process.startTimer();
            ++rowSize;
            
            /* Removes the parameter boxes */
            directory.clear();
            executable.clear();
            arguments.clear();
            
        });
        
        /* Creates the pid column */
        TableColumn<TableProcess,String> pidCol = new TableColumn<TableProcess,String> ("PID");
        pidCol.setCellValueFactory(new PropertyValueFactory<>("pid"));
        pidCol.setPrefWidth(75);
        
        /* Creates the executable column */
        TableColumn<TableProcess,String>  execCol = new TableColumn<TableProcess,String> ("Exec Name");
        execCol.setCellValueFactory(new PropertyValueFactory<>("execName"));
        execCol.setPrefWidth(96);
        
        /* Creates the arguments column */
        TableColumn<TableProcess,String>  argCol = new TableColumn<TableProcess,String> ("Arguments");
        argCol.setCellValueFactory(new PropertyValueFactory<>("arguments"));
        argCol.setPrefWidth(179);
        
        /* Creates the running duration column */
        TableColumn<TableProcess,String>  runCol = new TableColumn<TableProcess,String> ("Running Duration");
        runCol.setCellValueFactory(new PropertyValueFactory<>("runningDuration"));
        runCol.setPrefWidth(159);
        
        /* Creates the temporary column for kill button */
        TableColumn<TableProcess,String>  actionCol = new TableColumn<TableProcess,String> ("");
        actionCol.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Creates the kill button in actionCol column */
        Callback<TableColumn<TableProcess, String>, TableCell<TableProcess, String>> cellFactory= new Callback<TableColumn<TableProcess, String>, TableCell<TableProcess, String>>() {
		    @Override
		    public TableCell<TableProcess,String>  call(final TableColumn<TableProcess, String> param) {
		        final TableCell<TableProcess, String> cell = new TableCell<TableProcess, String>() {
		
		            final Button btn = new Button("Kill");
		
		            @Override
		            public void updateItem(String item, boolean empty) {
		                super.updateItem(item, empty);
		                if (empty) {
		                    setGraphic(null);
		                    setText(null);
		                } else {
		                    btn.setOnAction(event -> {
		                    	/* gets the current process */
		                        TableProcess process = getTableView().getItems().get(getIndex());
		                        
		                        System.out.println("Killed "+process.getPid() + " " + process.getExecName());
		                        
		                        /* Executes the kill command(SIGTERM signal) */
		                        try {
									runProgram("/usr/bin","./kill","-15 "+process.getPid());
								} catch (IOException e) {
									e.printStackTrace();
								}
		                        
		                        /* removes the current process in the table and updates timers */
		                        table.getItems().remove(process);
		                        for(int i=getIndex();i<timerRows.size();++i) {
		                        	if(timerRows.get(i)>0)
		                        		timerRows.set(i, timerRows.get(i)-1);
		                        }
		                        /* Stops the own timer and decreases the rowSize */
		                        process.stopTimer();
		                        --rowSize;
		                    });
		                    setGraphic(btn);
		                    setText(null);
		                }
		            }
		        };
		        return cell;
		    }
		};
		actionCol.setCellFactory(cellFactory);
		
        /* Adds created columns in the table */
        table.getColumns().addAll(pidCol, execCol,argCol,runCol,actionCol);
        
        /* opens the Application managers */
        stage.setTitle("Application Manager");
        stage.setScene(scene);
        stage.show();
    }
    
	/**
	 * Main method.
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Model class in the ApplicationManager
	 * Includes pid,executable name,arguments,running duration and timer
	 * @author fatihselimyakar
	 */
	public static class TableProcess {
		
        private final SimpleStringProperty pid;
        private final SimpleStringProperty execName;
        private final SimpleStringProperty arguments;
        private final SimpleStringProperty runningDuration;
        private Timeline timer;
        
        /**
         * Constructor method 
         * @param pid Process ID
         * @param execName Executable name
         * @param arguments String arguments
         * @param runningDuration Running duration
         */
        private TableProcess(String pid, String execName, String arguments,String runningDuration) {
            this.pid = new SimpleStringProperty(pid);
            this.execName = new SimpleStringProperty(execName);
            this.arguments = new SimpleStringProperty(arguments);
            this.runningDuration = new SimpleStringProperty(runningDuration);
        }
 
        /**
         * Getter of pid.
         * @return string type pid.
         */
        public String getPid() {
            return this.pid.get();
        }
 
        /**
         * Setter of pid.
         * @param pid Setter pid.
         */
        public void setPid(String pid) {
            this.pid.set(pid);
        }
 
        /**
         * Getter of executable name.
         * @return String type executable name.
         */
        public String getExecName() {
            return this.execName.get();
        }
 
        /**
         * Setter of executable name.
         * @param execName String type executable name.
         */
        public void setExecName(String execName) {
            this.execName.set(execName);
        }
 
        /**
         * Getter of arguments.
         * @return String type arguments.
         */
        public String getArguments() {
            return this.arguments.get();
        }
 
        /**
         * Setter of arguments
         * @param arguments String type arguments.
         */
        public void setArguments(String arguments) {
            this.arguments.set(arguments);
        }
        
        /**
         * Getter of running duration.
         * @return String type running duration.
         */
        public String getRunningDuration() {
            return this.runningDuration.get();
        }
 
        /**
         * Setter of running duration.
         * @param runningDuration String type running duration.
         */
        public void setRunningDuration(String runningDuration) {
            this.runningDuration.set(runningDuration);
        }
        
        /**
         * Getter of timer.
         * @return Timeline type timer.
         */
        public Timeline getTimer() {
            return this.timer;
        }
        
        /**
         * Setter of timer
         * @param timer Timeline type timer.
         */
        public void setTimer(Timeline timer) {
        	this.timer=timer;
        }
 
        /**
         * Starts the timer in the TableProcess.
         */
        public void startTimer() {
        	timer.setCycleCount(Timeline.INDEFINITE);
            timer.play();
        }
        
        /**
         * Stops the timer in the TableProcess.
         */
        public void stopTimer() {
        	timer.stop();
        }
        
    }
	
}
