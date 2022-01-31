package controller;

import simu.model.Asiakas;
import simu.model.Palvelupiste;

public interface IKontrolleri {
	
		// Metodit, joita käyttöliittymä käyttää
		public void kaynnistaSimulointi();
		public void nopeuta();
		public void hidasta();
		public Palvelupiste[] getPalvelupisteet();
		// Metodit, joita Moottori + OmaMoottori käyttää
		public int getKassat();
		public int getIPKassat();
		public int getRuokalinjastot();
		public void vieAsiakaslistaan(Asiakas asiakas);
		public void simuloinninJalkeen();
		public void setAika(double aika);
		public void varaaPP(int i);
		public void vapautaPP(int i);
		public void reset();
}
