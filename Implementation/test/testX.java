package ift2255;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class testX {

	@Test
	void inscritMembre() {
		Membre m= new Membre("alaadin",909090000,"100 rue eli","montreal","QC","H2B1W2","alaa@gmail.com");
		CentreDonnee c=new CentreDonnee();
		c.addMembre(m);
		assertEquals(c.getListeMembres().get(0).getNom(),"alaadin");
		assertEquals(c.getListeMembres().get(0).getID(),909090000);
		assertEquals(c.getListeMembres().get(0).getVille(),"montreal");
		assertEquals(c.getListeMembres().get(0).getCourriel(),"alaa@gmail.com");
		assertEquals(c.getListeMembres().get(0),m);
	}
	@Test
	void creerComptePro() {
		Professionnel p =new Professionnel("isma",999999999,"2810 1e av","montreal","QC","H3N9O9","ISMA@gmail.com");
		CentreDonnee ca=new CentreDonnee();
		ca.addPro(p);
		assertEquals(ca.getListePros().get(0).getNom(),"isma");
		assertEquals(ca.getListePros().get(0).getCodePostal(),"H3N9O9");
		assertEquals(ca.getListePros().get(0).getAdresse(),"2810 1e av");
		assertEquals(ca.getListePros().get(0),p);
	}
	
	
	
	
	
	
	
}
