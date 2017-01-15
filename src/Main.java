import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application{

	// controllers
	
		VBox bigBox;
		HBox mainBox;	
		HBox binBox;
		HBox sqlBox;
		HBox testBox;
		HBox dataBox;
		
		Button btnMain;
		Button btnBin;
		Button btnBin_run;
		Button btnBin_edit;
		Button btnData;
		Button btnSql;
		Button btnTest;
		
		TextField txtMain;
		TextField txtBinSearch;
		TextField txtDataSearch;
		TextField txtSqlSearch;
		TextField txtTestSearch;
		
		Stage window;
		
		ChoiceBox<String> cboxBat;
		ChoiceBox<String> cboxSql;
		ChoiceBox<String> cboxData;
		ChoiceBox<String> cboxTest;
		
		// vars
		ArrayList<String> batList;
		//ArrayList<String> bats_temp;
		ArrayList<String> sqlList;
		ArrayList<String> dataList;
		ArrayList<String> testList;
		
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage){
		window = primaryStage;
		window.setTitle("Shorcuts Tool");
		System.out.println("Start");
		//this.init();		
		
		batList = new ArrayList<String>();
		//bats_temp = new ArrayList<String>();
		sqlList = new ArrayList<String>();
		dataList = new ArrayList<String>();
		testList = new ArrayList<String>();
		
		cboxBat = new ChoiceBox<String>();
		cboxSql = new ChoiceBox<String>();
		cboxData = new ChoiceBox<String>();
		cboxTest = new ChoiceBox<String>();
		
		bigBox = new VBox();
		mainBox = new HBox();		
		binBox = new HBox();
		sqlBox = new HBox();
		testBox = new HBox();
		dataBox = new HBox();
		

		//loadBats();
		//loadDatas();
		//loadSqls();
		//loadTests();
		
		// ============== Main ==============
		btnMain = new Button("main");
		txtMain = new TextField("C:\\Users\\yun\\Desktop\\rfolder\\Root\\Feature1");
		txtMain.setPromptText("Enter Main Path");	
		
		btnMain.setOnAction(event -> openFF(txtMain.getText()));
		txtMain.setOnAction(e -> enableButtonsByInput("main"));
		// ============== bin ==============
		btnBin = new Button("bin");
		btnBin.setOnAction(event -> openFF(txtMain.getText() + "\\" + btnBin.getText()));
		
		txtBinSearch = new TextField();		
		txtBinSearch.setPromptText("Search .bat file");
		cboxBat = new ChoiceBox<String>();
		cboxBat.setOnAction(e-> enableButtonsByInput("bat"));
		loadBats();
		txtBinSearch.setOnAction(e-> searchRefreshChoiceBox("bat",txtBinSearch.getText(), batList, cboxBat));
		
		btnBin_edit = new Button("Edit .bat in Notepad++");
		btnBin_edit.setOnAction(e-> openFF(txtMain.getText() + "\\" + btnBin.getText()+ "\\" +cboxBat.getSelectionModel().getSelectedItem()));
		
		btnBin_run = new Button("Execute .bat in cmd");
		btnBin_run.setOnAction(e -> runBatAndWait(txtMain.getText() + "\\" + btnBin.getText()+ "\\" +cboxBat.getSelectionModel().getSelectedItem()));
		disableButton(btnBin_edit, true);
		disableButton(btnBin_run, true);
		
		binBox.getChildren().addAll(btnBin, txtBinSearch, cboxBat,btnBin_edit,btnBin_run);
		
		
		// ============== data ==============
		btnData = new Button("data");		
		btnData.setOnAction(event -> openFF(txtMain.getText() + "\\" + btnData.getText()));	
		
		
		dataBox.getChildren().addAll(btnData);
		// ============== sql ==============

		btnSql = new Button("sql");
		btnSql.setOnAction(event -> openFF(txtMain.getText() + "\\" + btnSql.getText()));
		//loadSqls();
		ChoiceBox<String> sqlsCBox = new ChoiceBox<String>();
		fillChoiceBox(findFiles(txtMain.getText() + "\\" + btnSql.getText(),0),sqlsCBox);
		sqlBox.getChildren().addAll(btnSql, sqlsCBox);
		// ============== test ==============

		btnTest = new Button("test");
		btnTest.setOnAction(event -> openFF(txtMain.getText() + "\\" + btnTest.getText()));
		txtTestSearch = new TextField();
		txtTestSearch.setPromptText("Search test file/folder");
		txtTestSearch.setOnAction(e -> searchRefreshChoiceBox("test",txtTestSearch.getText(), testList, cboxTest));
		loadTests();
		testBox.getChildren().addAll(btnTest,txtTestSearch,cboxTest);	
		// --------------------------------------------------

		
		
		
		mainBox.getChildren().addAll(btnMain, txtMain);
		bigBox.getChildren().addAll(mainBox, binBox, dataBox,sqlBox,testBox);
				
		
		Scene scene = new Scene(bigBox, 640, 480);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	
	// ++++++++++++++++++++++++++ Utility +++++++++++++++++++++++++++++++

	private void enableButtonsByInput(String target){
		switch(target){
			case "bat":
				if(cboxBat.getSelectionModel().getSelectedIndex() < 0){
					disableButton(btnBin_edit, true);
					disableButton(btnBin_run, true);
				}else{
					disableButton(btnBin_edit, false);
					
					if(isBat(cboxBat.getSelectionModel().getSelectedItem())){
						disableButton(btnBin_run, false);
					}else{
						disableButton(btnBin_run, true);
					}
				}
				break;
			case "main":
				File mainDir = new File(txtMain.getText());
				if(txtMain.getText().isEmpty() || !mainDir.isDirectory()){
					disableButton(btnMain, true);
					binBox.setDisable(true);
					sqlBox.setDisable(true);
					dataBox.setDisable(true);
					testBox.setDisable(true);				
					
					//btnMain;btnBin;btnBin_run; btnBin_edit; btnData; btnSql; btnTest;
				}else{					
					disableButton(btnMain, false);
					if(checkFF(txtMain.getText()+"\\"+"bin")== 2){binBox.setDisable(false);}else{binBox.setDisable(true);}
					if(checkFF(txtMain.getText()+"\\"+"sql")== 2){sqlBox.setDisable(false);}else{sqlBox.setDisable(true);}
					if(checkFF(txtMain.getText()+"\\"+"data")== 2){dataBox.setDisable(false);}else{dataBox.setDisable(true);}
					if(checkFF(txtMain.getText()+"\\"+"test")== 2){testBox.setDisable(false);}else{testBox.setDisable(true);}
				}
				break;
			default:
				break;
		}
	}
	public void runBatAndWait(String path){
		if(path.isEmpty()){ return; }
		
		File batFile = new File(path);		
		if(batFile.isFile()){	
			try{
				Runtime.getRuntime().exec("cmd /k start " + path);
			}catch(Exception e){e.printStackTrace();}
		}
	}


	public boolean isBat(String filename){
		if(filename.toLowerCase().endsWith(".bat")) {
			return true;
		}else {
			return false;
		}
	}
	public void loadBats(){
		batList = findFiles(txtMain.getText() + "\\" + "bin",1);
		fillChoiceBox(batList,cboxBat);
	}
	public void loadSqls(){
		sqlList = findFiles(txtMain.getText() + "\\" + "sql",0);
		fillChoiceBox(sqlList,cboxSql);
	}
	public void loadDatas(){
		dataList = findFiles(txtMain.getText() + "\\" + "data",0);
		fillChoiceBox(dataList,cboxData);
	}
	public void loadTests(){
		testList = findFiles(txtMain.getText() + "\\" + "test",0);
		fillChoiceBox(testList,cboxTest);
	}
	public void openFF(String path){		
		try{
			int type = checkFF(path);
			switch(type){
				case 1: // is file, edit in
					Runtime.getRuntime().exec("cmd /c start notepad++ " + path);
					break;
				case 2: // is folder, open
					Runtime.getRuntime().exec("cmd /c start " + path);
					break;
				default:
					break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**Return -1 for path not exists, 1 for file, 2 for directory*/
	public int checkFF(String path){
		int result = -1;
		File file = new File(path);
		if(!file.exists()){
			result = -1;
		}else{
			if(file.isDirectory()){
				result = 2;
			}else{
				result = 1;
			}
		}
		return result;
	}
	
	public void fillChoiceBox(ArrayList<String> list, ChoiceBox box){
		box.getItems().clear();
		for(String c : list){
			box.getItems().add(c);
		}
	}
	/**Type will be : bats, sqls, datas, tests */
	public void searchRefreshChoiceBox(String type,String name, ArrayList<String> fileList, ChoiceBox box){	
		System.out.println("type, name="+ type+","+name);
		if(name.isEmpty() || name.length() == 0 || name == null){
			System.out.println("type, name="+ type+","+name+"," +" for empty");
			switch(type){			
				case "bat":
					loadBats();
					break;
				case "sql":
					loadSqls();
					break;
				case "data": 
					loadDatas();
					break;
				case "test":
					loadTests();
					break;
				default:
					break;
			}
			return;			
		}
		
		//bats_temp.clear();
		ArrayList<String> temp = new ArrayList<String>();
		for(int i=0; i<fileList.size(); i++){
			if(fileList.get(i).contains(name)){		
				temp.add(fileList.get(i));
			}
		}
		box.getItems().clear();
		fillChoiceBox(temp,box);
		box.getSelectionModel().select(0);
	}
	
	public void disableButton(Button button, Boolean isDisabled){
		button.setDisable(isDisabled);
	}

	/**Return ArrayList<String> of files/folders, type: 1 for file only, 2 for folder only, others for all*/
	public ArrayList<String> findFiles(String path, int type){
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> result = new ArrayList<String>();
		switch(type){
			case 1: // file only
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						result.add(listOfFiles[i].getName());
					}
			    }
				break;
			case 2: // folder only
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isDirectory()) {
						result.add(listOfFiles[i].getName());
					}
			    }
				break;
			default: // all
				for (int i = 0; i < listOfFiles.length; i++) {
					result.add(listOfFiles[i].getName());
			    }
				break;
		}
		return result;
	}
	

}
