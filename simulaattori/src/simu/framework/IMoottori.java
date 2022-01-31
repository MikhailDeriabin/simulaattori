package simu.framework;

import simu.model.Palvelupiste;

public interface IMoottori { // UUSI
		
	// Kontrolleri käyttää tätä rajapintaa
	public void luoPalvelupisteet();
	public void setSimulointiaika(double aika);
	public void setViive(long aika);
	public long getViive();
	public Palvelupiste[] getPalvelupisteet();
	public void reset();
}
