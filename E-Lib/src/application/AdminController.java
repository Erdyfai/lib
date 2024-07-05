package application;


import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.BookDAO;
import dao.BorrowedBookDAO;
import dao.SanctionDAO;
import dao.UserDAO;
import dto.BookDTO;
import dto.BorrowedBookDTO;
import dto.UserDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdminController implements Initializable{

	private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Button studentBtn_form;

    @FXML
    private VBox approveBook_form;

    @FXML
    private VBox returnValidate_form;

    @FXML
    private Button approveBtn_menu;

    @FXML
    private Button approve_btn;

    @FXML
    private Button bookBtn_form;

    @FXML
    private TableView<BookDTO> bookListTable;

    @FXML
    private TableColumn<BookDTO, String> category_column;

    @FXML
    private AnchorPane crudBooks_form;

    @FXML
    private Button homeBtn_form;

    @FXML
    private Button imposeSanctionBtn_menu;

    @FXML
    private Button imposeSanction_Btn;

    @FXML
    private VBox imposeSanction_form;

    @FXML
    private TextField inputViolation;

    @FXML
    private Button returnBtn_menu;

    @FXML
    private TextField searchBook;

    @FXML
    private ComboBox<BookDTO> selectOrderBook;

    @FXML
    private ComboBox<BookDTO> selectReturnBook;

    @FXML
    private ComboBox<String> selectViolation;

    @FXML
    private TableColumn<BookDTO, Integer> stock_column;

    @FXML
    private TableColumn<UserDTO, Integer> studentBookOrder_column;

    @FXML
    private TableColumn<UserDTO, Integer> studentBorrowBooks_column;

    @FXML
    private VBox studentDetails_stdForm;

    @FXML
    private TableColumn<UserDTO, String> studentEmail_column;

    @FXML
    private TableColumn<UserDTO, String> studentFaculty_column;

    @FXML
    private Label studentFaculty_details;

    @FXML
    private VBox studentMenu;

    @FXML
    private TableColumn<UserDTO, String> studentName_column;

    @FXML
    private TableColumn<UserDTO, String> studentNim_column;

    @FXML
    private Label studentNim_details;

    @FXML
    private Label studentProgram_details;

    @FXML
    private TableColumn<UserDTO, String> studentSanction_column;

    @FXML
    private TableColumn<UserDTO, String> studentStudyProgram_nim;

    @FXML
    private TableView<UserDTO> studentTable;

    @FXML
    private AnchorPane student_form;

    @FXML
    private Label studentName_details;

    @FXML
    private TableColumn<BookDTO, String> title_column;

    @FXML
    private Button validate_btn;

    @FXML
    private TableColumn<BookDTO, String> writer_column;
    
    @FXML
    private AnchorPane homeForm;
    
    @FXML
    private Button cancelBtn_iSform;
    
    @FXML
    private Button cancelBtn_approveForm;
    
    @FXML
    private Button cancelBtn_validateForm;
    
    @FXML
    private Button logoutBtn;

//    private ObservableList<UserDTO> studentList = FXCollections.observableArrayList();
//    private ObservableList<BookDTO> bookList = FXCollections.observableArrayList();
    private ObservableList<String> sanctionsList = FXCollections.observableArrayList();
    private UserDTO selectedStudent;
    private UserDAO userDAO = new UserDAO();
    private BookDAO bookDAO = new BookDAO();
    private BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAO();
    private SanctionDAO sanctionDAO = new SanctionDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	setBookTable();
    	setStudentTable();
    	setViolationCbox();
    	
        studentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
            	selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            	setStudentDetails();
            	setOrderBookCbox();
            	setReturnValidationCbox();
            	imposeSanction_form.setVisible(false);
                approveBook_form.setVisible(false);
                returnValidate_form.setVisible(false);
            }
        });
    }
    
    private void setBookTable() {
    	ObservableList<BookDTO> bookList = FXCollections.observableArrayList(bookDAO.selectAllBooks());
        
        title_column.setCellValueFactory(new PropertyValueFactory<>("title"));
        writer_column.setCellValueFactory(new PropertyValueFactory<>("writer"));
        category_column.setCellValueFactory(new PropertyValueFactory<>("category"));
        stock_column.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        bookListTable.setItems(bookList);
    }
    
    private void setStudentTable() {
    	ObservableList<UserDTO> studentList = FXCollections.observableArrayList(userDAO.getAllStudent());
    	
        if(studentTable != null) {
        	studentTable.getItems().clear();
        }
        
    	studentName_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentEmail_column.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentNim_column.setCellValueFactory(new PropertyValueFactory<>("nim"));
        studentFaculty_column.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        studentStudyProgram_nim.setCellValueFactory(new PropertyValueFactory<>("studyProgram"));
        studentBorrowBooks_column.setCellValueFactory(new PropertyValueFactory<>("borrowedBooks"));
        studentBookOrder_column.setCellValueFactory(new PropertyValueFactory<>("countBookOrder"));
        studentSanction_column.setCellValueFactory(new PropertyValueFactory<>("sanctions"));

        studentTable.setItems(studentList);
    }
    
    private void setViolationCbox() {
    	sanctionsList.add("Late Return");
    	sanctionsList.add("Lost Book");
    	sanctionsList.add("Damaged Book");
    	
    	selectViolation.setItems(sanctionsList);
    }
    
    private void setStudentDetails() {
    	studentDetails_stdForm.setVisible(true);
        studentName_details.setText(selectedStudent.getName());
        studentNim_details.setText(selectedStudent.getNim());
        studentFaculty_details.setText(selectedStudent.getFaculty());
        studentProgram_details.setText(selectedStudent.getStudyProgram());
    }
    
    @FXML
    private void handleInsertImages(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show open file dialog
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            try {
                saveImage(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImage(File file) throws IOException {
        // Define the directory where the images will be saved
        Path destinationDirectory = Paths.get("/images");
        // Create the directory if it does not exist
        if (!Files.exists(destinationDirectory)) {
            Files.createDirectories(destinationDirectory);
        }

        // Copy the file to the destination directory
        Path destinationPath = destinationDirectory.resolve(file.getName());
        Files.copy(file.toPath(), destinationPath);
    }
    
    //--Impose Sanction--
    
    @FXML 
    public void imposeSanction(ActionEvent event) {
    	if(selectViolation.getValue() != null) {
    		LocalDate now = LocalDate.now();
	        java.sql.Date sqlDate = java.sql.Date.valueOf(now);
    		sanctionDAO.addMainSanction(selectedStudent.getId(), selectViolation.getValue(), sqlDate);
    		setStudentTable();
    	}
    }
    
    private void setReturnValidationCbox() {
    	List<BorrowedBookDTO> borrowedBook = borrowedBookDAO.getBorrowedBooksByIdAndStatus(selectedStudent.getId(), "borrowed");
    	List<BookDTO> book = new ArrayList<>();
    	
    	for (BorrowedBookDTO bookDTO : borrowedBook) {
			book.add(bookDAO.selectBookById(bookDTO.getBookId()));
		}
    	
    	selectReturnBook.setItems(FXCollections.observableArrayList(book));
    }
    
    //---------------------
    
    //--Approve Book Order--
    
    @FXML
    public void approveBookOrder(ActionEvent event) {
    	if(selectOrderBook.getValue() != null) {
    		borrowedBookDAO.updateBorrowStatus(selectOrderBook.getValue().getBookId(), selectedStudent.getId(), "borrowed");
    		userDAO.setStudentBorrowedBook(selectedStudent.getId(), (selectedStudent.getBorrowedBooks()+1));
    		setStudentTable();
    	}
    }
    
    private void setOrderBookCbox() {
    	List<BorrowedBookDTO> borrowedBook = borrowedBookDAO.getBorrowedBooksByIdAndStatus(selectedStudent.getId(), "booked");
    	List<BookDTO> book = new ArrayList<>();
    	
    	for (BorrowedBookDTO bookDTO : borrowedBook) {
			book.add(bookDAO.selectBookById(bookDTO.getBookId()));
		}
    	
    	selectOrderBook.setItems(FXCollections.observableArrayList(book));
    }
    
    //----------------------
    
    @FXML
    public void switchStudentForm(ActionEvent event) {
    	
    	if (event.getSource()== imposeSanctionBtn_menu) {
    		if(selectedStudent != null) {
    			imposeSanction_form.setVisible(true);
    			approveBook_form.setVisible(false);
    			returnValidate_form.setVisible(false);
    		}
		} else if(event.getSource()== approveBtn_menu) {
			if(selectedStudent.getCountBookOrder() != 0) {
				imposeSanction_form.setVisible(false);
				approveBook_form.setVisible(true);
				returnValidate_form.setVisible(false);
			}
		}else if(event.getSource()== returnBtn_menu) {
			if(selectedStudent.getBorrowedBooks() != 0) {
				imposeSanction_form.setVisible(false);
				approveBook_form.setVisible(false);
				returnValidate_form.setVisible(true);
			}
		}else if(event.getSource()== cancelBtn_iSform) {
			imposeSanction_form.setVisible(false);
		}else if(event.getSource()== cancelBtn_approveForm) {
			approveBook_form.setVisible(false);
		}else if(event.getSource()== cancelBtn_validateForm) {
			returnValidate_form.setVisible(false);
		}
    }
	
	@FXML
	public void switchMainForm(ActionEvent event) {
		if (event.getSource()== homeBtn_form) {
			student_form.setVisible(false);
			crudBooks_form.setVisible(false);
		} else if(event.getSource()== studentBtn_form) {
			student_form.setVisible(true);
			crudBooks_form.setVisible(false);
		}else if(event.getSource()== bookBtn_form) {
			student_form.setVisible(false);
			crudBooks_form.setVisible(true);
		}
	}
	
	@FXML
	private void logout(ActionEvent event) {
        // Create an alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("You are about to logout!");
        alert.setContentText("Are you sure you want to logout?");

        // Capture the user's response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setFullScreen(true);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
