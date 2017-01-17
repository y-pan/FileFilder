import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
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
		
		HBox lineBox1,lineBox2,headerBox;
		
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
		
		// for custom
		int seq;
		ArrayList<ChoiceBox<String>> rowCBoxList;
		ArrayList<TextField> rowPathList;
		ArrayList<HBox> rowBoxList;
		ArrayList<ArrayList<String>> rowPathContentList;
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
		btnBin.setMinWidth(40);
		
		txtBinSearch = new TextField();		
		txtBinSearch.setPromptText("Search .bat file");
		txtBinSearch.setPrefWidth(250);
		
		cboxBat = new ChoiceBox<String>();
		cboxBat.setOnAction(e-> enableButtonsByInput("bat"));
		cboxBat.setMaxWidth(100);
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
		
		
		mainBox.getChildren().addAll(btnMain, txtMain);
		
		// -------Custom-------------------------------------------
		seq = 0;
		rowPathList = new ArrayList<TextField>();
		rowPathContentList = new ArrayList<ArrayList<String>>();
		rowCBoxList = new ArrayList<ChoiceBox<String>>();
		rowBoxList = new ArrayList<HBox>();
		
		lineBox1 = new HBox();		
		Label hl = new Label("-----------------------------------------Custom--------------------------------------------");
		lineBox1.getChildren().add(hl);

		lineBox2 = new HBox();		
		Button btnAdd = new Button(">>>>>>> Click to add a row <<<<<<<");
		btnAdd.setPrefWidth(640);
		btnAdd.setTextFill(Color.RED);
		btnAdd.setOnAction(e-> addRow());
		lineBox2.getChildren().add(btnAdd);
		// header

		headerBox = new HBox();
		Label hTitle = new Label("Title");
		Label hPath = new Label("Path");
		Label hFilter = new Label("Filter");
		Label hContent = new Label("Content");
		Label hOperation = new Label("Operation");

		hTitle.setPrefWidth(200);
		hTitle.setTextFill(Color.BLUE);
		hPath.setMinWidth(170);
		hPath.setTextFill(Color.BLUE);
		hFilter.setPrefWidth(150);
		hFilter.setTextFill(Color.BLUE);
		hContent.setPrefWidth(100);
		hContent.setTextFill(Color.BLUE);
		//hOperation.setTextFill(Color.BLUE);
		headerBox.getChildren().addAll(hTitle,hPath,hFilter,hContent);
		

		// -------Custom end-------------------------------------------
		bigBox.getChildren().addAll(mainBox, binBox, dataBox,sqlBox,testBox,lineBox1,lineBox2,headerBox);
		System.out.println("here");
		
		Scene scene = new Scene(bigBox, 640, 480);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		addRow();
	}

	
	// ++++++++++++++++++++++++++ Utility +++++++++++++++++++++++++++++++
	// Custom >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	private void addRow(){
		
		System.out.println("Add a row");
		
		//1 t  just a title
		TextField rowLabel = new TextField();		
		rowLabel.setMaxWidth(50);
		rowLabel.setPromptText("--title--");
		rowLabel.setId(""+seq);
		//2 p
		TextField rowPath = new TextField();
		rowPath.setPromptText("--Path--");		
		rowPath.setPrefWidth(290);
		rowPath.setId(""+seq);
		rowPath.setOnAction(e->rowPathGetContent( rowPath.getId(), rowPath.getText() ));
		
		//3 f
		TextField rowFilter = new TextField();
		rowFilter.setPromptText("--Filter--");
		rowFilter.setId(""+seq);
		rowFilter.setPrefWidth(110);
		rowFilter.setOnAction(e->rowFilterFilterContent( rowFilter.getId(), rowFilter.getText() ));
		
		//4 c
		ChoiceBox<String> rowContent = new ChoiceBox<String>();
		rowContent.setPrefWidth(150);
		rowContent.setId(""+seq);
		//5 o
		Button rowOpen = new Button(">>");
		rowOpen.setId("" + seq);
		rowOpen.setOnAction(e -> rowOpen(rowOpen.getId()));
		
		
		HBox rowBox = new HBox();
		rowBox.setId(""+seq);
		
		rowBox.getChildren().addAll(rowLabel,rowPath,rowFilter,rowContent,rowOpen);
		bigBox.getChildren().add(rowBox);
		seq++;
		
		rowPathList.add(rowPath);
		rowCBoxList.add(rowContent);
		rowBoxList.add(rowBox);
		rowPathContentList.add(new ArrayList<String>());
		System.out.println("size="+rowPathContentList);
	}
	
	
	
	
	// Custom end <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	
	private void rowPathGetContent(String id, String path){
		System.out.println("rowPathGetContent id="+id + ","+ path);
		
		if(path.isEmpty()){ return; }	
		
		int pathType = checkFF(path);
		switch(pathType){
			case 1: // path=file, only "open" can edit it
				rowCBoxList.get(Integer.parseInt(id)).getItems().clear();
				break;
			case 2: // path=folder
				ArrayList<String> content = findFiles(path,0);
				rowPathContentList.get(Integer.parseInt(id)).clear();
				rowCBoxList.get(Integer.parseInt(id)).getItems().clear(); 
				if(content.size() > 0 ){ 				 
					rowCBoxList.get(Integer.parseInt(id)).getItems().addAll(content);
					rowPathContentList.get(Integer.parseInt(id)).addAll(content);
					System.out.println("rowPathContentList size again="+rowPathContentList.get(Integer.parseInt(id)).size());
				}
				break;
			default:
				break;
				
		}
	}
	private void rowFilterFilterContent(String id, String filter){
		System.out.println("rowFilterFilterContent id="+id + "," + filter);
		
		//bats_temp.clear();
		ArrayList<String> temp = new ArrayList<String>();
		for(int i=0; i<rowPathContentList.get(Integer.parseInt(id)).size(); i++){
			if(rowPathContentList.get(Integer.parseInt(id)).get(i).contains(filter)){		
				temp.add(rowPathContentList.get(Integer.parseInt(id)).get(i));
			}
		}
		rowCBoxList.get(Integer.parseInt(id)).getItems().clear();
		fillChoiceBox(temp,rowCBoxList.get(Integer.parseInt(id)));
		rowCBoxList.get(Integer.parseInt(id)).getSelectionModel().select(0);
	}
	private void rowOpen(String id){
		System.out.println("rowOpen id="+id);
		String path = rowPathList.get(Integer.parseInt(id)).getText();
		String name = rowCBoxList.get(Integer.parseInt(id)).getSelectionModel().getSelectedItem();
		String fp = path+"\\"+name;
		openFF(fp);
	}

	
	
	
	
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
	/**open folder, or open file in notepad++*/
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
		if(path == null || path.length() <=0 || path.isEmpty()){ return null; }		
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
