import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		Button btnBinEdit;
		Button btnData;
		Button btnSql;
		Button btnTest;
		
		TextField txtMain;
		TextField txtBin;
		TextField txtData;
		TextField txtSql;
		TextField txtTest;
		
		Stage window;
		
		ChoiceBox<String> batCBox;
		ChoiceBox<String> sqlCBox;
		ChoiceBox<String> dataCBox;
		ChoiceBox<String> testCBox;
		
		// vars
		ArrayList<String> bats;
		ArrayList<String> sqls;
		ArrayList<String> datas;
		ArrayList<String> tests;
		
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Shorcuts Tool");
		System.out.println("Start");
		//this.init();		
		
		bats = new ArrayList<String>();
		sqls = new ArrayList<String>();
		datas = new ArrayList<String>();
		tests = new ArrayList<String>();
		
		batCBox = new ChoiceBox<String>();
		sqlCBox = new ChoiceBox<String>();
		dataCBox = new ChoiceBox<String>();
		testCBox = new ChoiceBox<String>();
		
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
		
		// ============== bin ==============
		btnBin = new Button("bin");
		btnBin.setOnAction(event -> openFF(txtMain.getText() + "\\" + btnBin.getText()));
		
		txtBin = new TextField();		
		
		batCBox = new ChoiceBox<String>();
		loadBats();
		txtBin.setOnAction(e-> searchRefresh(txtBin.getText(), bats, batCBox));
		
		btnBinEdit = new Button("Edit .Bat");
		btnBinEdit.setOnAction(e-> openFF(txtMain.getText() + "\\" + btnBin.getText()+ "\\" +batCBox.getSelectionModel().getSelectedItem()));
		binBox.getChildren().addAll(btnBin, txtBin, batCBox,btnBinEdit);
		
		
		// ============== data ==============

		btnData = new Button("data");
		
		btnData.setOnAction(event -> openFF(txtMain.getText() + "\\" + btnData.getText()));
		
		dataBox.getChildren().addAll(btnData);
		// ============== sql ==============

		btnSql = new Button("sql");
		btnSql.setOnAction(event -> openFF(txtMain.getText() + "\\" + btnSql.getText()));
		ChoiceBox<String> sqlsCBox = new ChoiceBox<String>();
		fillChoiceBox(findFiles(txtMain.getText() + "\\" + btnSql.getText(),0),sqlsCBox);
		sqlBox.getChildren().addAll(btnSql, sqlsCBox);
		// ============== test ==============

		btnTest = new Button("test");
		btnTest.setOnAction(event -> openFF(txtMain.getText() + "\\" + btnTest.getText()));
		testBox.getChildren().addAll(btnTest);	
		// --------------------------------------------------

		
		mainBox.getChildren().addAll(btnMain, txtMain);
		bigBox.getChildren().addAll(mainBox, binBox, dataBox,sqlBox,testBox);
				
		
		Scene scene = new Scene(bigBox, 640, 480);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	
	// ++++++++++++++++++++++++++ Utility +++++++++++++++++++++++++++++++
	public void loadBats(){
		bats = findFiles(txtMain.getText() + "\\" + btnBin.getText(),1);
		fillChoiceBox(bats,batCBox);
	}
	public void loadSqls(){
		this.sqls = findFiles(txtMain.getText() + "\\" + this.btnSql.getText(),0);
		fillChoiceBox(this.sqls,this.sqlCBox);
	}
	public void loadDatas(){
		this.datas = findFiles(txtMain.getText() + "\\" + this.btnData.getText(),0);
		fillChoiceBox(this.datas,this.dataCBox);
	}
	public void loadTests(){
		this.tests = findFiles(txtMain.getText() + "\\" + this.btnTest.getText(),1);
		fillChoiceBox(this.tests,this.testCBox);
	}
	public void openFF(String path){
		int type = checkFF(path);
		try{
			switch(type){
				case 0: // not exist
					
					break;
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
	public int checkFF(String path){
		int result = 0;
		File file = new File(path);
		if(!file.exists()){
			result = 0;
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
	
	public void searchRefresh(String name, ArrayList<String> fileList, ChoiceBox box){	
		if(name.isEmpty()){
			return;
		}
		for(int i=0; i<fileList.size(); i++){
			if(fileList.get(i).contains(name)){
				box.getSelectionModel().select(i);
				System.out.println("seach");
				return;
			}
		}
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
