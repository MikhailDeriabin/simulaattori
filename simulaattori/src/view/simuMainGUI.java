package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import controller.IKontrolleri;
import controller.Kontrolleri;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.Asiakas;
import simu.model.Palvelupiste;
import simu.model.PalvelupisteenTyyppi;

public class simuMainGUI extends Application implements ISimulaattorinUI {

	private IKontrolleri kontrolleri;
	private List<Rectangle> ppTesti = new ArrayList<Rectangle>();
	private Stage primaryStage;
	private AnchorPane root;
	private ObservableList<Asiakas> asiakaslista = FXCollections.observableArrayList();
	private List<Rectangle> ruokalinjastot = new ArrayList<Rectangle>();
	private List<Rectangle> kassat = new ArrayList<Rectangle>();
	private List<Rectangle> ipKassat = new ArrayList<Rectangle>();

	@FXML
	private Button kaynnistaBtn;
	@FXML
	private Button hidastaBtn;

	@FXML
	private Button nopeutaBtn;
	@FXML
	private Button asiakkaatBtn;

	@FXML
	private TextField aikaTF;
	@FXML
	private Text aikaCount;
	@FXML
	private ProgressBar progressbar;
	@FXML
	private TextField viiveTF;

	@FXML
	private VBox ruokaVBox;
	@FXML
	private VBox kassaVBox;
	@FXML
	private VBox ipKassaVbox;

	@FXML
	private Button ruokaPlus;
	@FXML
	private Button kassaPlus;
	@FXML
	private Button ipKassaPlus;
	@FXML
	private Button tyhjennaBtn;

	@Override
	public void start(Stage primaryStage) {
		Trace.setTraceLevel(Level.INFO);
		try {
			this.ppTesti = new ArrayList<Rectangle>();
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Simulaattori");
			this.kontrolleri = new Kontrolleri(this);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(simuMainGUI.class.getResource("simuView.fxml"));

			root = (AnchorPane) loader.load();
			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void kaynnistaSimulaatio() {
		this.kontrolleri = new Kontrolleri(this);
		// Tarkastetaan onko viiveen ja simulointiajan tekstikentät täytetty
		if (tarkastaSyotteet()) {
			kontrolleri.kaynnistaSimulointi();
			kaynnistaBtn.setDisable(true);
			kassaPlus.setDisable(true);
			ruokaPlus.setDisable(true);
			ipKassaPlus.setDisable(true);
		}
	}

	private boolean tarkastaSyotteet() {
		if (viiveTF.getText() != "" && aikaTF.getText() != "") {
			if (ruokalinjastot.size() == 0 || kassat.size() == 0 || ipKassat.size() == 0) {
				Alert a = new Alert(AlertType.WARNING);
				a.setTitle("Varoitus");
				a.setHeaderText("Palvelupisteitä on liian vähän!");
				a.setContentText("Jokaista palvelupistetyyppiä on oltava vähintään yksi.");
				a.showAndWait();
				return false;
			} else
				return true;
		} else {
			Alert a = new Alert(AlertType.WARNING);
			a.setTitle("Varoitus");
			a.setHeaderText("Antamasi syötteet ovat väärin tai puuttuvat");
			a.setContentText("Tarkasta antamasi syötteet uudelleen.");
			a.showAndWait();
			return false;
		}
	}

	public void lisääPalvelupiste(PalvelupisteenTyyppi ppT) {
		Rectangle pp = new Rectangle(75, 75);
		ppTesti.add(pp);
		pp.setFill(Color.GREEN);

		// Tapahtumankäsittelijän luonti PalvelupisteenTyypin perusteella
		// Palvelupisteet luodaan OmaMoottorissa järjestyksessä
		// Ruokatiski->Kassa->IPKassa, jonka takia pp:n naytaTulos indeksi annetaan
		// tietyssä järjestyksessä

		if (ppT == PalvelupisteenTyyppi.RUOKATISKI) {
			ruokaVBox.getChildren().add(pp);
			ruokalinjastot.add(pp);
			pp.setOnMouseClicked((e) -> {
				naytaTulos(ruokalinjastot.indexOf(pp));
			});
		} else if (ppT == PalvelupisteenTyyppi.KASSA) {
			kassaVBox.getChildren().add(pp);
			kassat.add(pp);
			pp.setOnMouseClicked((e) -> {
				naytaTulos(kassat.indexOf(pp) + ruokalinjastot.size());
			});
		} else if (ppT == PalvelupisteenTyyppi.IPKASSA) {
			ipKassaVbox.getChildren().add(pp);
			ipKassat.add(pp);
			pp.setOnMouseClicked((e) -> {
				naytaTulos(ipKassat.indexOf(pp) + ruokalinjastot.size() + kassat.size());
			});
		}
	}

	public void reset() {
		ruokaVBox.getChildren().clear();
		kassaVBox.getChildren().clear();
		ipKassaVbox.getChildren().clear();

		ppTesti.clear();
		asiakaslista.clear();

		ruokalinjastot.clear();
		kassat.clear();
		ipKassat.clear();

		ruokaPlus.setDisable(false);
		kassaPlus.setDisable(false);
		ipKassaPlus.setDisable(false);

		aikaCount.setText("Käytettu aika: ");
		progressbar.setProgress(0);
		kontrolleri.reset();
		kaynnistaBtn.setDisable(false);
	}

	public void lisaaRuokalinjasto() {
		if (ruokalinjastot.size() < 4) {
			lisääPalvelupiste(PalvelupisteenTyyppi.RUOKATISKI);
			if (ruokalinjastot.size() == 4)
				ruokaPlus.setDisable(true);
		}
	}

	public void lisaaKassa() {
		if (kassat.size() < 3) {
			lisääPalvelupiste(PalvelupisteenTyyppi.KASSA);
			if (kassat.size() == 3)
				kassaPlus.setDisable(true);
		}
	}

	public void lisaaIPKassa() {
		if (ipKassat.size() < 3) {
			lisääPalvelupiste(PalvelupisteenTyyppi.IPKASSA);
			if (ipKassat.size() == 3)
				ipKassaPlus.setDisable(true);
		}
	}

	// Toimii jokaisen palvelupisteen (GUI:ssa Rectangle:t) onClick-metodina.
	// Hakee kontrollerilta moottorin palvelupisteet ja näyttää parametrin tulokset.
	private void naytaTulos(int indeksi) {
		try {
			Palvelupiste[] tulokset = kontrolleri.getPalvelupisteet();
			if (tulokset != null) {
				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("Tulokset");
				a.setHeaderText("Palvelupisteen " + tulokset[indeksi].getPpNimi() + " " + tulokset[indeksi].getPpNum()
						+ " tulokset");
				a.setContentText(tulokset[indeksi].getSimuTulos());
				a.showAndWait();
		}
		} catch (NullPointerException e) {
			throw e;
		}
	}

	// Luo uuden ikkunan, jossa näkyy asiakkaiden simulointitulokset
	public void avaaAsiakasIkkuna() {
		// TableView esittää asiakkaiden simutulokset
		TableView<Asiakas> tv = new TableView<Asiakas>();
		// Luodaan kolumnit ja asetetaan arvojen nimet, jotka lisätään niihin
		TableColumn<Asiakas, Integer> idColumn = new TableColumn<>("Asiakasnumero");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Asiakas, Double> aikaColumn = new TableColumn<>("Käytetty aika");
		aikaColumn.setCellValueFactory(new PropertyValueFactory<>("kokonaisaika"));

		tv.getColumns().add(idColumn);
		tv.getColumns().add(aikaColumn);

		idColumn.setResizable(false);
		aikaColumn.setResizable(false);
		idColumn.prefWidthProperty().bind(tv.widthProperty().multiply(0.5));
		aikaColumn.prefWidthProperty().bind(tv.widthProperty().multiply(0.5));

		// Lisätään asiakkaat TableViewiin
		for (Asiakas a : asiakaslista) {
			tv.getItems().add(a);
		}

		VBox vbox = new VBox(tv);
		Scene asiakasScene = new Scene(vbox, 300, 400);
		Stage asiakasIkkuna = new Stage();

		asiakasIkkuna.setTitle("Asiakkaat");
		asiakasIkkuna.setScene(asiakasScene);
		asiakasIkkuna.showAndWait();
	}

	public void hidasta() {
		kontrolleri.hidasta();
	}

	public void nopeuta() {
		kontrolleri.nopeuta();
	}

	@Override
	public double getAika() {
		Long aika = (long) 0;
		try {
			aika = Long.parseLong(aikaTF.getText());
		} catch (NumberFormatException e) {
			Alert a = new Alert(AlertType.WARNING);
			a.setTitle("Varoitus");
			a.setHeaderText("Antamasi aika on syötettu annettu väärin");
			a.setContentText("Tarkasta antamasi arvo. Muista, että voit syöttää vain numeroita!");
			a.showAndWait();
		}
		return aika;	
	}

	@Override
	public long getViive() {
		Long viive = (long) 0;
		try {
			viive = Long.parseLong(viiveTF.getText());
		} catch (NumberFormatException e) {
			Alert a = new Alert(AlertType.WARNING);
			a.setTitle("Varoitus");
			a.setHeaderText("Antamasi viive on annettu väärin");
			a.setContentText("Tarkasta antamasi arvo. Muista, että voit syöttää vain numeroita!");
			a.showAndWait();
		}
		return viive;
	}

	public void varaaPalvelupiste(int i) {
		ppTesti.get(i).setFill(Color.RED);
	}

	@Override
	public void vapautaPalvelupiste(int i) {
		ppTesti.get(i).setFill(Color.GREEN);
	}

	public void simuloinninJalkeen() {
		asiakkaatBtn.setDisable(false);
	}

	public void setAikaCounter(int aika) {
		aikaCount.setText("Käytettu aika: " + Integer.toString(aika));
		progressbar.setProgress(aika / Double.parseDouble(aikaTF.getText()));
	}

	public void lisaaAsiakaslistaan(Asiakas asiakas) {
		asiakaslista.add(asiakas);
	}

	public int getRuokalinjastot() {
		return ruokalinjastot.size();
	}

	public int getKassat() {
		return kassat.size();
	}

	public int getIPKassat() {
		return ipKassat.size();
	}

	public static void main(String[] args) {
		launch(args);
	}
}