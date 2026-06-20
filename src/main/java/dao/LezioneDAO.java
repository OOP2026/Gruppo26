package dao;

import model.Lezione;
import java.util.List;

public interface LezioneDAO {
    List<Lezione> getAllLezioni();
    boolean aggiornaLezione(Lezione lezione);
}
