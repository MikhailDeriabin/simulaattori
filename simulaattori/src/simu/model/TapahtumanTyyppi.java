package simu.model;

/**
 * <p>Type of the Tapahtuma-object aka event</p>
 * <p>Can be ARR1 = arriving to the system</p>
 * <p>DEP1 = departing from Palvelupiste aka service point with RUOKATISKI type</p>
 * <p>DEP2 = departing from Palvelupiste aka service point with KASSA or IPKASSA type</p>
 * 
 * @see <a href="Palvelupiste.html">Palvelupiste</a> 
 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
 */
public enum TapahtumanTyyppi {
	ARR1,	//Saapuminen järjestelmään
	DEP1,	// Poistuminen ruokatiskiltä
	DEP2,	// Poistuminen kassalta tai IP kassalta
}
