package controller;

import javafx.application.Platform;
import simu.framework.IMoottori;
import simu.model.Asiakas;
import simu.model.OmaMoottori;
import simu.model.Palvelupiste;
import view.ISimulaattorinUI;

public class Kontrolleri implements IKontrolleri{   // UUSI
	
	private IMoottori moottori; 
	private ISimulaattorinUI ui;
	
	public Kontrolleri(ISimulaattorinUI ui) {
		this.ui = ui;
		moottori = new OmaMoottori(this); // luodaan uusi moottorisäie jokaista simulointia varten
		
	}
		
	@Override
	public void kaynnistaSimulointi() {	
		moottori.luoPalvelupisteet();
		moottori.setSimulointiaika(ui.getAika());
		moottori.setViive(ui.getViive());
		((Thread)moottori).start();
	}
	
	@Override
	public void hidasta() { // hidastetaan moottorisäiettä
		moottori.setViive((long)(moottori.getViive()*1.10));
	}

	@Override
	public void nopeuta() { // nopeutetaan moottorisäiettä
		moottori.setViive((long)(moottori.getViive()*0.9));
	}

	public void varaaPP(int indeksi) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ui.varaaPalvelupiste(indeksi);
			}
		});
	}
	
	public void vapautaPP(int indeksi) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				ui.vapautaPalvelupiste(indeksi);
			}
		});
	}
	
	public void simuloinninJalkeen() {
		ui.simuloinninJalkeen();
	}
	
	public void setAika(double aika) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				ui.setAikaCounter((int) aika);
			}
		});
		
	}
	
	public Palvelupiste[] getPalvelupisteet(){	return moottori.getPalvelupisteet(); }

	@Override
	public void vieAsiakaslistaan(Asiakas asiakas) {
		ui.lisaaAsiakaslistaan(asiakas);
	}

	@Override
	public int getKassat() {
		return ui.getKassat();
	}

	@Override
	public int getIPKassat() {
		return ui.getIPKassat();
	}

	@Override
	public int getRuokalinjastot() {
		return ui.getRuokalinjastot();
	}
	
	public void reset() {
		moottori.reset();
	}
}