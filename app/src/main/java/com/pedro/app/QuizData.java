package com.pedro.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QuizData {

    public static class Question {
        public String q;
        public String[] opts;
        public int answer;

        public Question(String q, String[] opts, int answer) {
            this.q = q;
            this.opts = opts;
            this.answer = answer;
        }
    }

    private static final Map<String, Map<String, Map<String, List<Question>>>> ENGINE = new HashMap<>();

    static {
        // --- ITALIANO ---
        Map<String, Map<String, List<Question>>> itMap = new HashMap<>();
        ENGINE.put("it", itMap);
        
        // --- SPANISH ---
        Map<String, Map<String, List<Question>>> esMap = new HashMap<>();
        ENGINE.put("es", esMap);
        
        // Logica IT
        Map<String, List<Question>> itLogic = new HashMap<>();
        itMap.put("logic", itLogic);
        List<Question> itLogicMedio = new ArrayList<>();
        itLogicMedio.add(new Question("Quale numero viene dopo il 14?", new String[]{"13", "15", "16", "12"}, 1));
        itLogicMedio.add(new Question("Un contadino ha 17 pecore. Tutte tranne 9 scappano. Quante ne restano?", new String[]{"8", "9", "17", "0"}, 1));
        itLogicMedio.add(new Question("In una corsa superi il secondo. In che posizione sei?", new String[]{"Primo", "Secondo", "Terzo", "Ultimo"}, 1));
        itLogicMedio.add(new Question("Se 5 macchine fanno 5 bulloni in 5 minuti, quante ne servono per farne 100 in 100 minuti?", new String[]{"100", "50", "20", "5"}, 3));
        itLogicMedio.add(new Question("Cosa si rompe quando lo pronunci?", new String[]{"Silenzio", "Vetro", "Promessa", "Cuore"}, 0));
        itLogicMedio.add(new Question("Qual è il prossimo: 2, 4, 8, 16, ...?", new String[]{"20", "24", "32", "64"}, 2));
        itLogicMedio.add(new Question("Una candela dura 1 ora. Se ne accendi 5 insieme, quanto durano?", new String[]{"5 ore", "1 ora", "1/5 di ora", "Dipende"}, 1));
        itLogicMedio.add(new Question("Quante dita hanno 10 mani?", new String[]{"10", "50", "100", "20"}, 1));
        itLogicMedio.add(new Question("Quale parola di 5 lettere diventa più corta se le aggiungi due lettere?", new String[]{"Corta", "Lunga", "Breve", "Piccola"}, 0));
        itLogicMedio.add(new Question("Corro tutto il giorno ma non ho gambe. Ho una bocca ma non parlo. Cosa sono?", new String[]{"Fiume", "Vento", "Treno", "Orologio"}, 0));
        itLogicMedio.add(new Question("Ho una sola serratura ma molte chiavi. Cosa sono?", new String[]{"Scatola", "Diario", "Piano", "Portachiavi"}, 2));
        itLogicMedio.add(new Question("Più mi togli, più divento grande. Cosa sono?", new String[]{"Buco", "Fame", "Debito", "Ombra"}, 0));
        itLogicMedio.add(new Question("Vado su e giù per le scale senza muovermi. Cosa sono?", new String[]{"Tappeto", "Ascensore", "Ringhiera", "Corrimano"}, 0));
        itLogicMedio.add(new Question("Cosa ha un occhio ma non può vedere?", new String[]{"Ago", "Ciclone", "Patata", "Tempesta"}, 0));
        itLogicMedio.add(new Question("Ho molti denti ma non mordo mai. Cosa sono?", new String[]{"Pettine", "Sega", "Cerniera", "Ingranaggio"}, 0));
        itLogicMedio.add(new Question("Più mi usi, più divento piccola. Cosa sono?", new String[]{"Saponetta", "Candela", "Matita", "Gomma"}, 0));
        itLogicMedio.add(new Question("Tutti mi hanno ma nessuno mi può perdere. Cosa sono?", new String[]{"Ombra", "Nome", "Passato", "Sogno"}, 0));
        itLogicMedio.add(new Question("Quale mese ha 28 giorni?", new String[]{"Febbraio", "Tutti", "Agosto", "Aprile"}, 1));
        itLogicMedio.add(new Question("Se un treno elettrico va a sud, dove va il fumo?", new String[]{"Sud", "Nord", "Nessuna parte", "In alto"}, 2));
        itLogicMedio.add(new Question("Cosa pesa di più? 1kg di piume o 1kg di ferro?", new String[]{"Piume", "Ferro", "Uguale", "Dipende"}, 2));
        itLogicMedio.add(new Question("Il nonno di tuo cugino è tuo...?", new String[]{"Zio", "Nonno", "Padre", "Cognato"}, 1));
        itLogicMedio.add(new Question("Cosa puoi vedere una volta in un minuto, due in un momento, ma mai in cent'anni?", new String[]{"La lettera M", "Il tempo", "L'ombra", "Il riflesso"}, 0));
        itLogicMedio.add(new Question("Cosa ha un cuore che non batte?", new String[]{"Carciofo", "Pietra", "Statua", "Albero"}, 0));
        itLogicMedio.add(new Question("Più la lavi, più diventa sporca. Cos'è?", new String[]{"Acqua", "Saponetta", "Spugna", "Asciugamano"}, 0));
        itLogicMedio.add(new Question("Ho le mani ma non posso applaudire. Cosa sono?", new String[]{"Orologio", "Tavolo", "Statua", "Guanto"}, 0));
        itLogicMedio.add(new Question("Sono leggera come una piuma, ma nessuno riesce a tenermi a lungo. Cosa sono?", new String[]{"Respiro", "Piuma", "Bolla", "Aria"}, 0));
        itLogic.put("medio", itLogicMedio);

        List<Question> itLogicDiff = new ArrayList<>();
        itLogicDiff.add(new Question("Il padre di Mario ha 5 figli: Pa, Pe, Pi, Po. Chi è il quinto?", new String[]{"Pu", "Mario", "Pippo", "Palo"}, 1));
        itLogicDiff.add(new Question("Un mattone pesa 1kg più mezzo mattone. Quanto pesa?", new String[]{"1.5kg", "2kg", "3kg", "1kg"}, 1));
        itLogicDiff.add(new Question("Quante volte puoi sottrarre 5 da 25?", new String[]{"5", "4", "1", "Infinite"}, 2));
        itLogicDiff.add(new Question("Se un aereo cade sul confine tra Italia e Francia, dove seppelliscono i superstiti?", new String[]{"Italia", "Francia", "Nessuno dei due", "Svizzera"}, 2));
        itLogicDiff.add(new Question("Quale parola è scritta scorrettamente in tutti i dizionari?", new String[]{"Scorrettamente", "Errore", "Tutte", "Nessuna"}, 0));
        itLogicDiff.add(new Question("Più è asciutto, più si bagna. Cos'è?", new String[]{"Ombrello", "Mare", "Asciugamano", "Spugna"}, 2));
        itLogicDiff.add(new Question("Ha radici ma non si vede, è più alta degli alberi. Cos'è?", new String[]{"Montagna", "Vento", "Cielo", "Nuvola"}, 0));
        itLogicDiff.add(new Question("30 diviso mezzo, più 10. Quanto fa?", new String[]{"25", "70", "40", "15"}, 1));
        itLogicDiff.add(new Question("Se sono le 12 sul quadrante e le lancette si sovrappongono, che ore sono?", new String[]{"12:00", "12:05", "12:30", "13:00"}, 0));
        itLogicDiff.add(new Question("Due padri e due figli vanno a pesca. Pescano 3 pesci, ne hanno uno a testa. Come?", new String[]{"Uno è scappato", "Nonno, padre e figlio", "Hanno mentito", "Magia"}, 1));
        itLogicDiff.add(new Question("Un uomo esce sotto la pioggia senza ombrello e non si bagna un capello. Perché?", new String[]{"È calvo", "È al chiuso", "Indossa un cappello", "Non piove"}, 0));
        itLogicDiff.add(new Question("Cosa appartiene a te ma gli altri lo usano più di te?", new String[]{"Nome", "Denaro", "Cellulare", "Casa"}, 0));
        itLogicDiff.add(new Question("Cosa ha città ma non case, montagne ma non alberi, e acqua ma non pesci?", new String[]{"Globo", "Atlante", "Mappa", "Cartina"}, 2));
        itLogicDiff.add(new Question("Non puoi toccarmi ma posso ferire. Non mi vedi ma sono sempre con te. Cosa sono?", new String[]{"Pensiero", "Vento", "Tempo", "Ombra"}, 0));
        itLogicDiff.add(new Question("Cosa puoi tenere nella mano destra ma non nella sinistra?", new String[]{"Gomito sinistro", "Penna", "Mela", "Niente"}, 0));
        itLogicDiff.add(new Question("Cosa sale ma non scende mai?", new String[]{"Età", "Prezzo", "Palloncino", "Fumo"}, 0));
        itLogicDiff.add(new Question("Più ce n'è, meno vedi. Cosa?", new String[]{"Nebbia", "Buio", "Luce", "Fumo"}, 1));
        itLogicDiff.add(new Question("Ha un collo ma non la testa. Cos'è?", new String[]{"Fiasca", "Maglia", "Chitarra", "Bottiglia"}, 3));
        itLogicDiff.add(new Question("Cosa va in giro per il mondo restando in un angolo?", new String[]{"Francobollo", "Sole", "Vento", "Luna"}, 0));
        itLogicDiff.add(new Question("Porto carichi pesanti ma non ho muscoli. Cosa sono?", new String[]{"Treno", "Nave", "Ponte", "Camion"}, 2));
        itLogicDiff.add(new Question("Un orologio segna le 3:15. Quanto è l'angolo tra le lancette?", new String[]{"0°", "7.5°", "15°", "5°"}, 1));
        itLogicDiff.add(new Question("Ci sono 3 mele e ne prendi 2. Quante mele hai?", new String[]{"1", "2", "3", "0"}, 1));
        itLogicDiff.add(new Question("Quanti mesi hanno 28 giorni?", new String[]{"1", "12", "6", "Nessuno"}, 1));
        itLogicDiff.add(new Question("Cosa ti permette di guardare attraverso un muro?", new String[]{"Binocolo", "Occhiali", "Finestra", "Periscopio"}, 2));
        itLogic.put("difficile", itLogicDiff);

        // Matematica IT
        Map<String, List<Question>> itMath = new HashMap<>();
        itMap.put("math", itMath);
        List<Question> itMathMedio = new ArrayList<>();
        itMathMedio.add(new Question("√(144) = ?", new String[]{"14", "12", "11", "13"}, 1));
        itMathMedio.add(new Question("25% di 80?", new String[]{"25", "20", "15", "40"}, 1));
        itMathMedio.add(new Question("15 x 6?", new String[]{"80", "90", "75", "100"}, 1));
        itMathMedio.add(new Question("3^3 = ?", new String[]{"9", "18", "27", "81"}, 2));
        itMathMedio.add(new Question("0.5 + 1/4 = ?", new String[]{"0.75", "1", "0.6", "0.25"}, 0));
        itMathMedio.add(new Question("18 - 4 x 2 = ?", new String[]{"28", "10", "20", "14"}, 1));
        itMathMedio.add(new Question("50 diviso 0.5 è?", new String[]{"25", "100", "50", "0"}, 1));
        itMathMedio.add(new Question("Triangolo: cateti 3 e 4, ipotenusa?", new String[]{"5", "6", "7", "8"}, 0));
        itMathMedio.add(new Question("Quale è quadrato e cubo perfetto?", new String[]{"64", "16", "27", "81"}, 0));
        itMathMedio.add(new Question("7 x 8?", new String[]{"54", "56", "64", "49"}, 1));
        itMathMedio.add(new Question("100 / 4 + 5?", new String[]{"30", "25", "20", "35"}, 0));
        itMathMedio.add(new Question("13 x 3?", new String[]{"36", "39", "42", "45"}, 1));
        itMathMedio.add(new Question("Il triplo di 33?", new String[]{"66", "99", "77", "88"}, 1));
        itMathMedio.add(new Question("Quanti gradi ha un angolo retto?", new String[]{"45", "180", "90", "360"}, 2));
        itMathMedio.add(new Question("Raggio cerchio = 5, diametro?", new String[]{"10", "25", "5", "15"}, 0));
        itMathMedio.add(new Question("12 x 4?", new String[]{"48", "44", "46", "50"}, 0));
        itMathMedio.add(new Question("81 / 9?", new String[]{"8", "9", "7", "10"}, 1));
        itMathMedio.add(new Question("2 x (3+4)?", new String[]{"10", "14", "9", "12"}, 1));
        itMathMedio.add(new Question("50% di 200?", new String[]{"50", "100", "150", "20"}, 1));
        itMathMedio.add(new Question("11 x 11?", new String[]{"111", "121", "131", "122"}, 1));
        itMathMedio.add(new Question("4^2?", new String[]{"8", "12", "16", "20"}, 2));
        itMathMedio.add(new Question("Quante facce ha un cubo?", new String[]{"4", "6", "8", "12"}, 1));
        itMathMedio.add(new Question("6 x 7?", new String[]{"42", "48", "36", "44"}, 0));
        itMathMedio.add(new Question("9 x 9?", new String[]{"72", "81", "90", "99"}, 1));
        itMathMedio.add(new Question("Radice di 400?", new String[]{"10", "20", "30", "40"}, 1));
        itMathMedio.add(new Question("10% di 500?", new String[]{"10", "50", "100", "5"}, 1));
        itMathMedio.add(new Question("25 x 4?", new String[]{"100", "75", "125", "50"}, 0));
        itMath.put("medio", itMathMedio);

        List<Question> itMathDiff = new ArrayList<>();
        itMathDiff.add(new Question("2^5 = ?", new String[]{"16", "32", "64", "128"}, 1));
        itMathDiff.add(new Question("14 x 14 = ?", new String[]{"186", "196", "176", "206"}, 1));
        itMathDiff.add(new Question("Area cerchio raggio r?", new String[]{"2πr", "πr²", "4πr", "πr"}, 1));
        itMathDiff.add(new Question("√(625) = ?", new String[]{"15", "25", "35", "45"}, 1));
        itMathDiff.add(new Question("Log10(100)?", new String[]{"10", "1", "2", "3"}, 2));
        itMathDiff.add(new Question("Somma angoli interni triangolo?", new String[]{"180°", "360°", "90°", "270°"}, 0));
        itMathDiff.add(new Question("15 x 15?", new String[]{"225", "215", "235", "255"}, 0));
        itMathDiff.add(new Question("√(169)?", new String[]{"12", "13", "14", "15"}, 1));
        itMathDiff.add(new Question("3^4?", new String[]{"27", "81", "64", "12"}, 1));
        itMathDiff.add(new Question("Cos'è il pi greco?", new String[]{"3.14", "2.71", "1.61", "3.41"}, 0));
        itMathDiff.add(new Question("Quanti secondi in un'ora?", new String[]{"3600", "600", "1200", "360"}, 0));
        itMathDiff.add(new Question("2^6?", new String[]{"32", "64", "128", "256"}, 1));
        itMathDiff.add(new Question("5! (5 fattoriale)?", new String[]{"120", "100", "25", "60"}, 0));
        itMathDiff.add(new Question("Somma angoli interni quadrilatero?", new String[]{"180", "360", "90", "540"}, 1));
        itMathDiff.add(new Question("Qual è il numero primo pari?", new String[]{"0", "2", "4", "6"}, 1));
        itMath.put("difficile", itMathDiff);

        // Cultura IT
        Map<String, List<Question>> itKnow = new HashMap<>();
        itMap.put("knowledge", itKnow);
        List<Question> itKnowMedio = new ArrayList<>();
        itKnowMedio.add(new Question("Chi scrisse la Divina Commedia?", new String[]{"Petrarca", "Boccaccio", "Dante", "Leopardi"}, 2));
        itKnowMedio.add(new Question("La capitale del Giappone?", new String[]{"Osaka", "Tokyo", "Kyoto", "Seul"}, 1));
        itKnowMedio.add(new Question("In che anno cadde il Muro di Berlino?", new String[]{"1985", "1989", "1991", "1980"}, 1));
        itKnowMedio.add(new Question("Pianeta più vicino al Sole?", new String[]{"Venere", "Marte", "Mercurio", "Terra"}, 2));
        itKnowMedio.add(new Question("Chi dipinse la Gioconda?", new String[]{"Michelangelo", "Raffaello", "Leonardo", "Donatello"}, 2));
        itKnowMedio.add(new Question("Capitale dell'Australia?", new String[]{"Sydney", "Melbourne", "Canberra", "Perth"}, 2));
        itKnowMedio.add(new Question("Quante nazioni formano il Regno Unito?", new String[]{"3", "4", "5", "2"}, 1));
        itKnowMedio.add(new Question("Qual è l'elemento più abbondante nell'aria?", new String[]{"Ossigeno", "Idrogeno", "Azoto", "Carbonio"}, 2));
        itKnowMedio.add(new Question("Chi scoprì la penicillina?", new String[]{"Fleming", "Pasteur", "Curie", "Einstein"}, 0));
        itKnowMedio.add(new Question("Monte più alto d'Europa?", new String[]{"Monte Bianco", "Elbrus", "Etna", "Everest"}, 1));
        itKnowMedio.add(new Question("Chi dipinse L'Urlo?", new String[]{"Van Gogh", "Munch", "Picasso", "Dalì"}, 1));
        itKnowMedio.add(new Question("Capitale della Germania?", new String[]{"Monaco", "Berlino", "Francoforte", "Amburgo"}, 1));
        itKnowMedio.add(new Question("In che anno scoppiò la Rivoluzione Francese?", new String[]{"1776", "1789", "1848", "1799"}, 1));
        itKnowMedio.add(new Question("Qual è il fiume più lungo d'Italia?", new String[]{"Tevere", "Arno", "Po", "Adige"}, 2));
        itKnowMedio.add(new Question("Capitale della Russia?", new String[]{"Mosca", "San Pietroburgo", "Kiev", "Varsavia"}, 0));
        itKnowMedio.add(new Question("Elemento chimico Fe?", new String[]{"Fluoro", "Ferro", "Fosforo", "Fermio"}, 1));
        itKnowMedio.add(new Question("Chi dipinse la nascita di Venere?", new String[]{"Botticelli", "Leonardo", "Raffaello", "Tiziano"}, 0));
        itKnowMedio.add(new Question("Capitale del Portogallo?", new String[]{"Porto", "Lisbona", "Madrid", "Sintra"}, 1));
        itKnowMedio.add(new Question("Quanti sono i pianeti del sistema solare?", new String[]{"7", "8", "9", "10"}, 1));
        itKnow.put("medio", itKnowMedio);

        List<Question> itKnowDiff = new ArrayList<>();
        itKnowDiff.add(new Question("Chi vinse il Nobel per la letteratura nel 1997?", new String[]{"Dario Fo", "Moravia", "Calvino", "Eco"}, 0));
        itKnowDiff.add(new Question("Anno della scoperta dell'America?", new String[]{"1490", "1492", "1494", "1498"}, 1));
        itKnowDiff.add(new Question("Capitale del Kazakistan?", new String[]{"Almaty", "Astana", "Baku", "Tashkent"}, 1));
        itKnowDiff.add(new Question("Qual è lo stato più piccolo al mondo?", new String[]{"Monaco", "San Marino", "Vaticano", "Liechtenstein"}, 2));
        itKnowDiff.add(new Question("Inventore della pila?", new String[]{"Galvani", "Volta", "Fermi", "Meucci"}, 1));
        itKnowDiff.add(new Question("Primo uomo sulla Luna?", new String[]{"Gagarin", "Armstrong", "Aldrin", "Collins"}, 1));
        itKnowDiff.add(new Question("Lingua più parlata al mondo?", new String[]{"Inglese", "Cinese", "Spagnolo", "Arabo"}, 1));
        itKnowDiff.add(new Question("Chi scrisse Il Gattopardo?", new String[]{"Sciascia", "Lampedusa", "Pirandello", "Verga"}, 1));
        itKnowDiff.add(new Question("Capitale del Brasile?", new String[]{"Rio", "San Paolo", "Brasilia", "Salvador"}, 2));
        itKnowDiff.add(new Question("Chi inventò il telefono?", new String[]{"Bell", "Meucci", "Edison", "Tesla"}, 1));
        itKnowDiff.add(new Question("Guerra dei Cent'anni, chi vinse?", new String[]{"Inghilterra", "Francia", "Spagna", "Italia"}, 1));
        itKnowDiff.add(new Question("Capitale della Svezia?", new String[]{"Oslo", "Stoccolma", "Copenaghen", "Helsinki"}, 1));
        itKnowDiff.add(new Question("Metallo più prezioso?", new String[]{"Oro", "Platino", "Rodio", "Argento"}, 2));
        itKnow.put("difficile", itKnowDiff);

        // --- ENGLISH ---
        Map<String, Map<String, List<Question>>> enMap = new HashMap<>();
        ENGINE.put("en", enMap);
        
        Map<String, List<Question>> enLogic = new HashMap<>();
        enMap.put("logic", enLogic);
        List<Question> enLogicMedio = new ArrayList<>();
        enLogicMedio.add(new Question("Which number comes after 14?", new String[]{"13", "15", "16", "12"}, 1));
        enLogicMedio.add(new Question("A farmer has 17 sheep. All but 9 flee. How many stay?", new String[]{"8", "9", "17", "0"}, 1));
        enLogicMedio.add(new Question("In a race, you overtake the second. What position are you in?", new String[]{"1st", "2nd", "3rd", "Last"}, 1));
        enLogicMedio.add(new Question("If 5 machines make 5 bolts in 5 mins, how many to make 100 in 100 mins?", new String[]{"100", "50", "20", "5"}, 3));
        enLogicMedio.add(new Question("What breaks when you speak its name?", new String[]{"Silence", "Glass", "Promise", "Heart"}, 0));
        enLogicMedio.add(new Question("Next: 2, 4, 8, 16, ...?", new String[]{"20", "24", "32", "64"}, 2));
        enLogicMedio.add(new Question("A candle lasts 1 hour. If you light 5 together, how long do they last?", new String[]{"5 hours", "1 hour", "1/5 hour", "Depends"}, 1));
        enLogicMedio.add(new Question("How many fingers on 10 hands?", new String[]{"10", "50", "100", "20"}, 1));
        enLogicMedio.add(new Question("Which 5-letter word becomes shorter if you add two letters?", new String[]{"Short", "Longer", "Brief", "Small"}, 0));
        enLogicMedio.add(new Question("Run all day but no legs. Mouth but no talk. What?", new String[]{"River", "Wind", "Train", "Clock"}, 0));
        enLogicMedio.add(new Question("I have only one lock but many keys. What am I?", new String[]{"Box", "Diary", "Piano", "Keyring"}, 2));
        enLogicMedio.add(new Question("Drier it gets, wetter it becomes. What?", new String[]{"Umbrella", "Sea", "Towel", "Sponge"}, 2));
        enLogicMedio.add(new Question("Up and down stairs without moving. What?", new String[]{"Carpet", "Elevator", "Railing", "Handrail"}, 0));
        enLogicMedio.add(new Question("What has an eye but can't see?", new String[]{"Needle", "Storm", "Potato", "Hurricane"}, 0));
        enLogicMedio.add(new Question("Many teeth but never bite?", new String[]{"Comb", "Saw", "Zipper", "Gear"}, 0));
        enLogicMedio.add(new Question("More you use it, smaller it becomes?", new String[]{"Soap", "Candle", "Pencil", "Eraser"}, 0));
        enLogicMedio.add(new Question("Everyone has it but no one can lose it?", new String[]{"Shadow", "Name", "Past", "Dream"}, 0));
        enLogicMedio.add(new Question("Which month has 28 days?", new String[]{"February", "All of them", "August", "April"}, 1));
        enLogicMedio.add(new Question("Grandfather of your cousin is your...?", new String[]{"Uncle", "Grandpa", "Father", "Brother"}, 1));
        enLogicMedio.add(new Question("Heart that doesn't beat?", new String[]{"Artichoke", "Stone", "Statue", "Tree"}, 0));
        enLogicMedio.add(new Question("Cleaner it is, dirtier it gets?", new String[]{"Water", "Soap", "Sponge", "Towel"}, 0));
        enLogicMedio.add(new Question("If you break me, I still work?", new String[]{"Heart", "Silence", "News", "Record"}, 3));
        enLogic.put("medio", enLogicMedio);

        List<Question> enLogicDiff = new ArrayList<>();
        enLogicDiff.add(new Question("Mario's father has 5 sons: Pa, Pe, Pi, Po. Who is the 5th?", new String[]{"Pu", "Mario", "Pippo", "Palo"}, 1));
        enLogicDiff.add(new Question("A brick weighs 1kg plus half a brick. Total weight?", new String[]{"1.5kg", "2kg", "3kg", "1kg"}, 1));
        enLogicDiff.add(new Question("How many times can you subtract 5 from 25?", new String[]{"5", "4", "1", "Infinite"}, 2));
        enLogicDiff.add(new Question("If a plane crashes on the border of IT and FR, where are survivors buried?", new String[]{"Italy", "France", "Neither", "Switzerland"}, 2));
        enLogicDiff.add(new Question("Which word is spelled incorrectly in all dictionaries?", new String[]{"Incorrectly", "Error", "All", "None"}, 0));
        enLogicDiff.add(new Question("Roots nobody sees, taller than trees. What?", new String[]{"Mountain", "Wind", "Sky", "Cloud"}, 0));
        enLogicDiff.add(new Question("30 divided by half, plus 10. How much?", new String[]{"25", "70", "40", "15"}, 1));
        enLogicDiff.add(new Question("If it's 12 on the clock and hands overlap, what time is it?", new String[]{"12:00", "12:05", "12:30", "13:00"}, 0));
        enLogicDiff.add(new Question("Two fathers and two sons fish. They catch 3, one each. How?", new String[]{"One escaped", "Grandpa, dad, son", "They lied", "Magic"}, 1));
        enLogicDiff.add(new Question("Comes out in rain no umbrella, hair doesn't get wet. Why?", new String[]{"Bald", "Indoors", "Hat", "No rain"}, 0));
        enLogicDiff.add(new Question("Belongs to you but others use it more?", new String[]{"Name", "Money", "Phone", "House"}, 0));
        enLogicDiff.add(new Question("Has cities no houses, mountains no trees, water no fish?", new String[]{"Globe", "Atlas", "Map", "Chart"}, 2));
        enLogicDiff.add(new Question("Angle of hands at 3:15?", new String[]{"0°", "7.5°", "15°", "5°"}, 1));
        enLogicDiff.add(new Question("You have 3 apples and take 2. How many have you?", new String[]{"1", "2", "3", "0"}, 1));
        enLogicDiff.add(new Question("What lets you look through a wall?", new String[]{"Binoculars", "Glasses", "Window", "Periscope"}, 2));
        enLogic.put("difficile", enLogicDiff);

        Map<String, List<Question>> enMath = new HashMap<>();
        enMap.put("math", enMath);
        List<Question> enMathMedio = new ArrayList<>();
        enMathMedio.add(new Question("√(144) = ?", new String[]{"14", "12", "11", "13"}, 1));
        enMathMedio.add(new Question("25% of 80?", new String[]{"25", "20", "15", "40"}, 1));
        enMathMedio.add(new Question("15 x 6?", new String[]{"80", "90", "75", "100"}, 1));
        enMathMedio.add(new Question("3^3 = ?", new String[]{"9", "18", "27", "81"}, 2));
        enMathMedio.add(new Question("0.5 + 1/4 = ?", new String[]{"0.75", "1", "0.6", "0.25"}, 0));
        enMathMedio.add(new Question("18 - 4 x 2 = ?", new String[]{"28", "10", "20", "14"}, 1));
        enMathMedio.add(new Question("50 divided by 0.5 is?", new String[]{"25", "100", "50", "0"}, 1));
        enMathMedio.add(new Question("Triangle: legs 3 and 4, hypotenuse?", new String[]{"5", "6", "7", "8"}, 0));
        enMathMedio.add(new Question("Perfect square and cube?", new String[]{"64", "16", "27", "81"}, 0));
        enMathMedio.add(new Question("7 x 8?", new String[]{"54", "56", "64", "49"}, 1));
        enMathMedio.add(new Question("12 x 4?", new String[]{"48", "44", "46", "50"}, 0));
        enMathMedio.add(new Question("81 / 9?", new String[]{"8", "9", "7", "10"}, 1));
        enMathMedio.add(new Question("50% of 200?", new String[]{"50", "100", "150", "20"}, 1));
        enMathMedio.add(new Question("Right angle degrees?", new String[]{"45", "180", "90", "360"}, 2));
        enMathMedio.add(new Question("6 x 7?", new String[]{"42", "48", "36", "44"}, 0));
        enMathMedio.add(new Question("√(400) = ?", new String[]{"10", "20", "30", "40"}, 1));
        enMathMedio.add(new Question("10% of 500?", new String[]{"10", "50", "100", "5"}, 1));
        enMath.put("medio", enMathMedio);

        List<Question> enMathDiff = new ArrayList<>();
        enMathDiff.add(new Question("2^5 = ?", new String[]{"16", "32", "64", "128"}, 1));
        enMathDiff.add(new Question("14 x 14 = ?", new String[]{"186", "196", "176", "206"}, 1));
        enMathDiff.add(new Question("√(625) = ?", new String[]{"15", "25", "35", "45"}, 1));
        enMathDiff.add(new Question("Log10(100)?", new String[]{"10", "1", "2", "3"}, 2));
        enMathDiff.add(new Question("Sum of triangle angles?", new String[]{"180°", "360°", "90°", "270°"}, 0));
        enMathDiff.add(new Question("2^6?", new String[]{"32", "64", "128", "256"}, 1));
        enMathDiff.add(new Question("5! (5 factorial)?", new String[]{"120", "100", "25", "60"}, 0));
        enMathDiff.add(new Question("Pi value?", new String[]{"3.14", "2.71", "1.61", "3.12"}, 0));
        enMathDiff.add(new Question("Seconds in one hour?", new String[]{"3600", "600", "1200", "360"}, 0));
        enMathDiff.add(new Question("Prime even number?", new String[]{"0", "2", "4", "6"}, 1));
        enMathDiff.add(new Question("Sum of quad angles?", new String[]{"180", "360", "90", "540"}, 1));
        enMath.put("difficile", enMathDiff);

        Map<String, List<Question>> enKnow = new HashMap<>();
        enMap.put("knowledge", enKnow);
        List<Question> enKnowMedio = new ArrayList<>();
        enKnowMedio.add(new Question("Who wrote Divine Comedy?", new String[]{"Petrarca", "Boccaccio", "Dante", "Shakespeare"}, 2));
        enKnowMedio.add(new Question("Capital of Japan?", new String[]{"Osaka", "Tokyo", "Kyoto", "Seoul"}, 1));
        enKnowMedio.add(new Question("Berlin Wall fell year?", new String[]{"1985", "1989", "1991", "1980"}, 1));
        enKnowMedio.add(new Question("Planet closest to Sun?", new String[]{"Venus", "Mars", "Mercury", "Earth"}, 2));
        enKnowMedio.add(new Question("Who painted Mona Lisa?", new String[]{"Michelangelo", "Raffaello", "Leonardo", "Donatello"}, 2));
        enKnowMedio.add(new Question("Capital of Australia?", new String[]{"Sydney", "Melbourne", "Canberra", "Perth"}, 2));
        enKnowMedio.add(new Question("How many nations in UK?", new String[]{"3", "4", "5", "2"}, 1));
        enKnowMedio.add(new Question("Who discovered penicillin?", new String[]{"Fleming", "Pasteur", "Curie", "Einstein"}, 0));
        enKnowMedio.add(new Question("Capital of Germany?", new String[]{"Munich", "Berlin", "Frankfurt", "Hamburg"}, 1));
        enKnowMedio.add(new Question("River longest in Italy?", new String[]{"Tiber", "Arno", "Po", "Adige"}, 2));
        enKnowMedio.add(new Question("Chemical element Fe?", new String[]{"Fluorine", "Iron", "Phosphorus", "Fermi"}, 1));
        enKnowMedio.add(new Question("Who painted Birth of Venus?", new String[]{"Botticelli", "Leonardo", "Raffaello", "Titian"}, 0));
        enKnowMedio.add(new Question("Capital of Portugal?", new String[]{"Porto", "Lisbon", "Madrid", "Sintra"}, 1));
        enKnowMedio.add(new Question("Planets in solar system?", new String[]{"7", "8", "9", "10"}, 1));
        enKnow.put("medio", enKnowMedio);

        List<Question> enKnowDiff = new ArrayList<>();
        enKnowDiff.add(new Question("Discovery of America year?", new String[]{"1490", "1492", "1494", "1498"}, 1));
        enKnowDiff.add(new Question("Capital of Kazakhstan?", new String[]{"Almaty", "Astana", "Baku", "Tashkent"}, 1));
        enKnowDiff.add(new Question("Smallest state in the world?", new String[]{"Monaco", "San Marino", "Vatican", "Liechtenstein"}, 2));
        enKnowDiff.add(new Question("Inventor of battery?", new String[]{"Galvani", "Volta", "Fermi", "Tesla"}, 1));
        enKnowDiff.add(new Question("First man on Moon?", new String[]{"Gagarin", "Armstrong", "Aldrin", "Collins"}, 1));
        enKnowDiff.add(new Question("Most spoken language in world?", new String[]{"English", "Chinese", "Spanish", "Arabic"}, 1));
        enKnowDiff.add(new Question("Who wrote 'The Leopard'?", new String[]{"Sciascia", "Lampedusa", "Pirandello", "Verga"}, 1));
        enKnowDiff.add(new Question("Capital of Brazil?", new String[]{"Rio", "Sao Paulo", "Brasilia", "Salvador"}, 2));
        enKnowDiff.add(new Question("Who invented telephone?", new String[]{"Bell", "Meucci", "Edison", "Tesla"}, 1));
        enKnowDiff.add(new Question("100 Years War winner?", new String[]{"England", "France", "Spain", "Italy"}, 1));
        enKnowDiff.add(new Question("Capital of Sweden?", new String[]{"Oslo", "Stockholm", "Copenhagen", "Helsinki"}, 1));
        enKnowDiff.add(new Question("Most precious metal?", new String[]{"Gold", "Platinum", "Rhodium", "Silver"}, 2));
        enKnow.put("difficile", enKnowDiff);

        // --- DEUTSCH ---
        Map<String, Map<String, List<Question>>> deMap = new HashMap<>();
        ENGINE.put("de", deMap);
        
        Map<String, List<Question>> deLogic = new HashMap<>();
        deMap.put("logic", deLogic);
        List<Question> deLogicMedio = new ArrayList<>();
        deLogicMedio.add(new Question("Welche Zahl kommt nach 14?", new String[]{"13", "15", "16", "12"}, 1));
        deLogicMedio.add(new Question("Ein Bauer hat 17 Schafe. Alle außer 9 fliehen. Wie viele bleiben?", new String[]{"8", "9", "17", "0"}, 1));
        deLogicMedio.add(new Question("In einem Rennen überholst du den Zweiten. Was bist du?", new String[]{"1.", "2.", "3.", "Letzter"}, 1));
        deLogicMedio.add(new Question("Was bricht, als man seinen Namen sagt?", new String[]{"Stille", "Glas", "Versprechen", "Herz"}, 0));
        deLogicMedio.add(new Question("Nachfolge: 2, 4, 8, 16, ...?", new String[]{"20", "24", "32", "64"}, 2));
        deLogicMedio.add(new Question("Was hat Tasten aber keine Schlösser?", new String[]{"Tür", "Klavier", "Kiste", "Auto"}, 1));
        deLogicMedio.add(new Question("Was wird größer, wenn man wegnimmt?", new String[]{"Loch", "Hunger", "Schulden", "Schatten"}, 0));
        deLogicMedio.add(new Question("Was hat ein Auge aber sieht nicht?", new String[]{"Nadel", "Sturm", "Kartoffel", "Hurrikan"}, 0));
        deLogicMedio.add(new Question("Viele Zähne aber beißt nie?", new String[]{"Kamm", "Säge", "Reißverschluss", "Zahnrad"}, 0));
        deLogicMedio.add(new Question("Je mehr benutzt, desto kleiner?", new String[]{"Seife", "Kerze", "Bleistift", "Radierer"}, 0));
        deLogicMedio.add(new Question("Jeder hat es, keiner verliert es?", new String[]{"Schatten", "Name", "Vergangenheit", "Traum"}, 0));
        deLogicMedio.add(new Question("Welcher Monat hat 28 Tage?", new String[]{"Februar", "Alle", "August", "April"}, 1));
        deLogicMedio.add(new Question("Großvater deines Cousins ist dein...?", new String[]{"Onkel", "Opa", "Vater", "Bruder"}, 1));
        deLogicMedio.add(new Question("Was geht Treppen rauf ohne Bewegung?", new String[]{"Teppich", "Aufzug", "Geländer", "Schiene"}, 0));
        deLogicMedio.add(new Question("Herz das nicht schlägt?", new String[]{"Artischocke", "Stein", "Statue", "Baum"}, 0));
        deLogic.put("medio", deLogicMedio);

        List<Question> deLogicDiff = new ArrayList<>();
        deLogicDiff.add(new Question("Marios Vater hat 5 Söhne: Pa, Pe, Pi, Po. Wer ist der 5.?", new String[]{"Pu", "Mario", "Pippo", "Palo"}, 1));
        deLogicDiff.add(new Question("Ziegel wiegt 1kg plus halber Ziegel. Gesamtgewicht?", new String[]{"1.5kg", "2kg", "3kg", "1kg"}, 1));
        deLogicDiff.add(new Question("Wie oft kann man 5 von 25 abziehen?", new String[]{"5", "4", "1", "Unendlich"}, 2));
        deLogicDiff.add(new Question("Was trocknet, während es nass wird?", new String[]{"Schirm", "Meer", "Handtuch", "Schwamm"}, 2));
        deLogicDiff.add(new Question("Wurzeln keiner sieht, höher als Bäume?", new String[]{"Berg", "Wind", "Himmel", "Wolke"}, 0));
        deLogicDiff.add(new Question("Wenn man bei Regen rausgeht ohne Schirm, Haare nicht nass. Warum?", new String[]{"Glatze", "Drinnen", "Hut", "Kein Regen"}, 0));
        deLogicDiff.add(new Question("Gehört dir, aber andere nutzen es mehr?", new String[]{"Name", "Geld", "Handy", "Haus"}, 0));
        deLogicDiff.add(new Question("Hat Städte keine Häuser, Berge keine Bäume?", new String[]{"Globus", "Atlas", "Karte", "Plan"}, 2));
        deLogicDiff.add(new Question("Winkel der Zeiger um 3:15?", new String[]{"0°", "7.5°", "15°", "5°"}, 1));
        deLogicDiff.add(new Question("Du hast 3 Äpfel und nimmst 2. Wie viele hast du?", new String[]{"1", "2", "3", "0"}, 1));
        deLogic.put("difficile", deLogicDiff);

        Map<String, List<Question>> deMath = new HashMap<>();
        deMap.put("math", deMath);
        List<Question> deMathMedio = new ArrayList<>();
        deMathMedio.add(new Question("√(144) = ?", new String[]{"14", "12", "11", "13"}, 1));
        deMathMedio.add(new Question("25% von 80?", new String[]{"25", "20", "15", "40"}, 1));
        deMathMedio.add(new Question("15 x 6?", new String[]{"80", "90", "75", "100"}, 1));
        deMathMedio.add(new Question("3^3 = ?", new String[]{"9", "18", "27", "81"}, 2));
        deMathMedio.add(new Question("100 / 4 + 5?", new String[]{"30", "25", "20", "35"}, 0));
        deMathMedio.add(new Question("Rechter Winkel Grad?", new String[]{"45", "180", "90", "360"}, 2));
        deMathMedio.add(new Question("12 x 4?", new String[]{"48", "44", "46", "50"}, 0));
        deMathMedio.add(new Question("81 / 9?", new String[]{"8", "9", "7", "10"}, 1));
        deMathMedio.add(new Question("50% von 200?", new String[]{"50", "100", "150", "20"}, 1));
        deMathMedio.add(new Question("11 x 11?", new String[]{"111", "121", "131", "122"}, 1));
        deMathMedio.add(new Question("4^2?", new String[]{"8", "12", "16", "20"}, 2));
        deMath.put("medio", deMathMedio);

        List<Question> deMathDiff = new ArrayList<>();
        deMathDiff.add(new Question("2^5 = ?", new String[]{"16", "32", "64", "128"}, 1));
        deMathDiff.add(new Question("14 x 14 = ?", new String[]{"186", "196", "176", "206"}, 1));
        deMathDiff.add(new Question("√(625) = ?", new String[]{"15", "25", "35", "45"}, 1));
        deMathDiff.add(new Question("Summe der Dreieckswinkel?", new String[]{"180°", "360°", "90°", "270°"}, 0));
        deMathDiff.add(new Question("2^6?", new String[]{"32", "64", "128", "256"}, 1));
        deMathDiff.add(new Question("5! (Fakultät)?", new String[]{"120", "100", "25", "60"}, 0));
        deMathDiff.add(new Question("Pi Wert?", new String[]{"3.14", "2.71", "1.61", "3.12"}, 0));
        deMathDiff.add(new Question("Sekunden in einer Stunde?", new String[]{"3600", "600", "1200", "360"}, 0));
        deMath.put("difficile", deMathDiff);

        Map<String, List<Question>> deKnow = new HashMap<>();
        deMap.put("knowledge", deKnow);
        List<Question> deKnowMedio = new ArrayList<>();
        deKnowMedio.add(new Question("Wer schrieb Göttliche Komödie?", new String[]{"Petrarca", "Boccaccio", "Dante", "Böhl"}, 2));
        deKnowMedio.add(new Question("Hauptstadt von Japan?", new String[]{"Osaka", "Tokio", "Kyoto", "Seoul"}, 1));
        deKnowMedio.add(new Question("Was ist der rote Planet?", new String[]{"Mars", "Venus", "Jupiter", "Saturn"}, 0));
        deKnowMedio.add(new Question("Hauptstadt von Australien?", new String[]{"Sydney", "Melbourne", "Canberra", "Perth"}, 2));
        deKnowMedio.add(new Question("Wer malte Mona Lisa?", new String[]{"Michelangelo", "Raffaello", "Leonardo", "Donatello"}, 2));
        deKnowMedio.add(new Question("Wann fiel die Berliner Mauer?", new String[]{"1985", "1989", "1991", "1980"}, 1));
        deKnowMedio.add(new Question("Häufigstes Gas in der Luft?", new String[]{"Sauerstoff", "Wasserstoff", "Stickstoff", "Kohlenstoff"}, 2));
        deKnowMedio.add(new Question("Wer entdeckte Penicillin?", new String[]{"Fleming", "Pasteur", "Curie", "Einstein"}, 0));
        deKnowMedio.add(new Question("Höchster Berg in Europa?", new String[]{"Mont Blanc", "Elbrus", "Etna", "Everest"}, 1));
        deKnowMedio.add(new Question("Längster Fluss in Italien?", new String[]{"Tiber", "Arno", "Po", "Etsch"}, 2));
        deKnowMedio.add(new Question("Chemisches Element Fe?", new String[]{"Fluor", "Eisen", "Phosphor", "Fermi"}, 1));
        deKnowMedio.add(new Question("Wer malte Geburt der Venus?", new String[]{"Botticelli", "Leonardo", "Raffaello", "Tizian"}, 0));
        deKnow.put("medio", deKnowMedio);

        List<Question> deKnowDiff = new ArrayList<>();
        deKnowDiff.add(new Question("Jahr der Entdeckung Amerikas?", new String[]{"1490", "1492", "1494", "1498"}, 1));
        deKnowDiff.add(new Question("Hauptstadt von Kasachstan?", new String[]{"Almaty", "Astana", "Baku", "Taschkent"}, 1));
        deKnowDiff.add(new Question("Kleinststaat der Welt?", new String[]{"Monaco", "San Marino", "Vatikan", "Liechtenstein"}, 2));
        deKnowDiff.add(new Question("Erfinder der Batterie?", new String[]{"Galvani", "Volta", "Fermi", "Tesla"}, 1));
        deKnowDiff.add(new Question("Erster Mensch auf dem Mond?", new String[]{"Gagarin", "Armstrong", "Aldrin", "Collins"}, 1));
        deKnowDiff.add(new Question("Meistgesprochene Sprache?", new String[]{"Englisch", "Chinesisch", "Spanisch", "Arabisch"}, 1));
        deKnowDiff.add(new Question("Wer schrieb 'Der Leopard'?", new String[]{"Sciascia", "Lampedusa", "Pirandello", "Verga"}, 1));
        deKnowDiff.add(new Question("Hauptstadt von Brasilien?", new String[]{"Rio", "Sao Paulo", "Brasilia", "Salvador"}, 2));
        deKnowDiff.add(new Question("Wer erfand das Telefon?", new String[]{"Bell", "Meucci", "Edison", "Tesla"}, 1));
        deKnowDiff.add(new Question("Hundertjähriger Krieg, wer gewann?", new String[]{"England", "Frankreich", "Spanien", "Italien"}, 1));
        deKnow.put("difficile", deKnowDiff);

        // --- SPANISH (LOGIC & KNOWLEDGE) ---
        Map<String, List<Question>> esLogic = new HashMap<>();
        esMap.put("logic", esLogic);
        List<Question> esLogicMedio = new ArrayList<>();
        esLogicMedio.add(new Question("¿Qué número viene después de 14?", new String[]{"13", "15", "16", "12"}, 1));
        esLogicMedio.add(new Question("Un granjero tiene 17 ovejas. Todas excepto 9 escapan. ¿Cuántas quedan?", new String[]{"8", "9", "17", "0"}, 1));
        esLogicMedio.add(new Question("En una carrera superas al segundo. ¿En qué lugar estás?", new String[]{"Primero", "Segundo", "Tercero", "Último"}, 1));
        esLogicMedio.add(new Question("¿Qué se rompe al decir su nombre?", new String[]{"Silencio", "Cristal", "Promesa", "Corazón"}, 0));
        esLogicMedio.add(new Question("Si 5 máquinas hacen 5 tornillos en 5 minutos, ¿cuántas para 100 en 100 minutos?", new String[]{"100", "50", "20", "5"}, 3));
        esLogicMedio.add(new Question("¿Cuál sigue: 2, 4, 8, 16, ...?", new String[]{"20", "24", "32", "64"}, 2));
        esLogicMedio.add(new Question("Cuanto más lo usas, más pequeño se vuelve. ¿Qué es?", new String[]{"Jabón", "Vela", "Lápiz", "Borrador"}, 0));
        esLogicMedio.add(new Question("¿Qué mes tiene 28 días?", new String[]{"Febrero", "Todos", "Agosto", "Abril"}, 1));
        esLogicMedio.add(new Question("El abuelo de tu primo es tu...", new String[]{"Tío", "Abuelo", "Padre", "Hermano"}, 1));
        esLogicMedio.add(new Question("¿Qué tiene muchas llaves pero ninguna cerradura?", new String[]{"Caja", "Piano", "Diario", "Puerta"}, 1));
        esLogic.put("medio", esLogicMedio);

        List<Question> esLogicDiff = new ArrayList<>();
        esLogicDiff.add(new Question("El padre de Mario tiene 5 hijos: Pa, Pe, Pi, Po. ¿Quién es el quinto?", new String[]{"Pu", "Mario", "Pippo", "Palo"}, 1));
        esLogicDiff.add(new Question("Un ladrillo pesa 1kg más medio ladrillo. ¿Cuánto pesa?", new String[]{"1.5kg", "2kg", "3kg", "1kg"}, 1));
        esLogicDiff.add(new Question("¿Cuántas veces puedes restar 5 a 25?", new String[]{"5", "4", "1", "Infinitas"}, 2));
        esLogicDiff.add(new Question("Si un tren va al sur, ¿hacia dónde va el humo?", new String[]{"Sur", "Norte", "Ningún lado", "Arriba"}, 2));
        esLogicDiff.add(new Question("¿Qué tiene ciudades sin casas y agua sin peces?", new String[]{"Globo", "Atlas", "Mapa", "Planeta"}, 2));
        esLogicDiff.add(new Question("Entre más hay, menos ves. ¿Qué es?", new String[]{"Niebla", "Oscuridad", "Luz", "Humo"}, 1));
        esLogicDiff.add(new Question("Si me rompes, sigo funcionando. ¿Qué soy?", new String[]{"Secreto", "Silencio", "Récord", "Corazón"}, 2));
        esLogicDiff.add(new Question("Tienes 3 manzanas y tomas 2. ¿Cuántas tienes?", new String[]{"1", "2", "3", "0"}, 1));
        esLogic.put("difficile", esLogicDiff);

        Map<String, List<Question>> esKnow = new HashMap<>();
        esMap.put("knowledge", esKnow);
        List<Question> esKnowMedio = new ArrayList<>();
        esKnowMedio.add(new Question("¿Quién escribió La Divina Comedia?", new String[]{"Petrarca", "Boccaccio", "Dante", "Leopardi"}, 2));
        esKnowMedio.add(new Question("¿Capital de Japón?", new String[]{"Osaka", "Tokio", "Kioto", "Seúl"}, 1));
        esKnowMedio.add(new Question("¿Cuál es el planeta más cercano al Sol?", new String[]{"Venus", "Marte", "Mercurio", "Tierra"}, 2));
        esKnowMedio.add(new Question("¿Qué gas es más abundante en el aire?", new String[]{"Oxígeno", "Hidrógeno", "Nitrógeno", "Carbono"}, 2));
        esKnowMedio.add(new Question("¿Capital de Australia?", new String[]{"Sídney", "Melbourne", "Canberra", "Perth"}, 2));
        esKnowMedio.add(new Question("¿Quién descubrió la penicilina?", new String[]{"Fleming", "Pasteur", "Curie", "Einstein"}, 0));
        esKnowMedio.add(new Question("¿Capital de Alemania?", new String[]{"Múnich", "Berlín", "Fráncfort", "Hamburgo"}, 1));
        esKnowMedio.add(new Question("¿Símbolo químico del Hierro?", new String[]{"Fl", "Fe", "P", "Fm"}, 1));
        esKnowMedio.add(new Question("¿Río más largo de Italia?", new String[]{"Tíber", "Arno", "Po", "Adigio"}, 2));
        esKnow.put("medio", esKnowMedio);

        List<Question> esKnowDiff = new ArrayList<>();
        esKnowDiff.add(new Question("¿Año del descubrimiento de América?", new String[]{"1490", "1492", "1494", "1498"}, 1));
        esKnowDiff.add(new Question("¿País más pequeño del mundo?", new String[]{"Mónaco", "San Marino", "Vaticano", "Liechtenstein"}, 2));
        esKnowDiff.add(new Question("¿Inventor de la pila?", new String[]{"Galvani", "Volta", "Fermi", "Meucci"}, 1));
        esKnowDiff.add(new Question("¿Idioma más hablado en el mundo?", new String[]{"Inglés", "Chino", "Español", "Árabe"}, 1));
        esKnowDiff.add(new Question("¿Capital de Brasil?", new String[]{"Río", "San Pablo", "Brasilia", "Salvador"}, 2));
        esKnowDiff.add(new Question("¿Primer hombre en la Luna?", new String[]{"Gagarin", "Armstrong", "Aldrin", "Collins"}, 1));
        esKnowDiff.add(new Question("¿Metal más precioso?", new String[]{"Oro", "Platino", "Rodio", "Plata"}, 2));
        esKnowDiff.add(new Question("¿Capital de Suecia?", new String[]{"Oslo", "Estocolmo", "Copenhague", "Helsinki"}, 1));
        esKnow.put("difficile", esKnowDiff);
    }

    public static List<Question> getQuestions(String lang, String topic, String diff) {
        if ("math".equals(topic)) {
            return generateDynamicMath(diff, 10);
        }
        
        Map<String, Map<String, List<Question>>> langMap = ENGINE.get(lang);
        if (langMap == null) langMap = ENGINE.get("en");
        Map<String, List<Question>> topicMap = langMap.get(topic);
        if (topicMap == null) topicMap = langMap.get("logic");
        List<Question> qs = topicMap.get(diff);
        if (qs == null) qs = topicMap.get("medio");
        return qs != null ? new ArrayList<>(qs) : new ArrayList<>();
    }

    private static List<Question> generateDynamicMath(String diff, int count) {
        List<Question> list = new ArrayList<>();
        Random r = new Random();
        boolean isHard = "difficile".equals(diff);
        
        for (int i = 0; i < count; i++) {
            int type = r.nextInt(isHard ? 5 : 3);
            String q = "";
            int ans = 0;
            
            switch (type) {
                case 0: // Addizione/Sottrazione complicata
                    int a = r.nextInt(isHard ? 200 : 100);
                    int b = r.nextInt(isHard ? 200 : 100);
                    if (r.nextBoolean()) { q = a + " + " + b; ans = a + b; }
                    else { q = a + " - " + b; ans = a - b; }
                    break;
                case 1: // Moltiplicazione
                    int m1 = r.nextInt(isHard ? 20 : 12) + 2;
                    int m2 = r.nextInt(isHard ? 15 : 10) + 2;
                    q = m1 + " x " + m2;
                    ans = m1 * m2;
                    break;
                case 2: // Parentesi (tricky)
                    int p1 = r.nextInt(10) + 2;
                    int p2 = r.nextInt(10) + 2;
                    int p3 = r.nextInt(10) + 2;
                    q = "(" + p1 + " x " + p2 + ") + " + p3;
                    ans = (p1 * p2) + p3;
                    break;
                case 3: // Radici quadrate (solo hard)
                    int[] roots = {4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225};
                    int base = roots[r.nextInt(roots.length)];
                    int rootVal = (int) Math.sqrt(base);
                    int offset = r.nextInt(20);
                    q = "√(" + base + ") + " + offset;
                    ans = rootVal + offset;
                    break;
                case 4: // Divisioni con resto 0 (solo hard)
                    int d2 = r.nextInt(12) + 2;
                    int d1 = d2 * (r.nextInt(15) + 2);
                    q = d1 + " / " + d2;
                    ans = d1 / d2;
                    break;
                default:
                    q = "10 + 10"; ans = 20;
            }
            
            String[] opts = new String[4];
            int correctPos = r.nextInt(4);
            opts[correctPos] = String.valueOf(ans);
            
            List<Integer> used = new ArrayList<>();
            used.add(ans);
            for (int k = 0; k < 4; k++) {
                if (k == correctPos) continue;
                int wrong;
                do {
                    wrong = ans + (r.nextInt(20) - 10);
                    if (wrong == ans) wrong += (r.nextBoolean() ? 1 : -1);
                } while (used.contains(wrong));
                opts[k] = String.valueOf(wrong);
                used.add(wrong);
            }
            list.add(new Question(q + " = ?", opts, correctPos));
        }
        return list;
    }

    public static List<Question> getUnlockQuiz(String lang) {
        List<Question> qs = new ArrayList<>();
        if ("it".equals(lang)) {
            qs.add(new Question("Capitale della Francia?", new String[]{"Londra", "Berlino", "Parigi", "Madrid"}, 2));
            qs.add(new Question("Quanti lati ha un esagono?", new String[]{"5", "6", "7", "8"}, 1));
            qs.add(new Question("Qual è il pianeta rosso?", new String[]{"Marte", "Venere", "Giove", "Saturno"}, 0));
            qs.add(new Question("Cosa bolle a 100 gradi?", new String[]{"Latte", "Olio", "Acqua", "Vino"}, 2));
            qs.add(new Question("Quanti sono i nani di Biancaneve?", new String[]{"5", "6", "7", "8"}, 2));
            qs.add(new Question("Qual è il pianeta più grande?", new String[]{"Saturno", "Giove", "Terra", "Urano"}, 1));
            qs.add(new Question("In che continente si trova l'Egitto?", new String[]{"Africa", "Asia", "Europa", "Sud America"}, 0));
            qs.add(new Question("Simbolo chimico dell'Oro?", new String[]{"Ag", "Au", "Or", "O"}, 1));
            qs.add(new Question("Quanti giorni ha un anno bisestile?", new String[]{"365", "366", "364", "360"}, 1));
            qs.add(new Question("Chi ha scritto l'Odissea?", new String[]{"Virgilio", "Omero", "Dante", "Socrate"}, 1));
            qs.add(new Question("Qual è l'oceano più vasto?", new String[]{"Atlantico", "Pacifico", "Indiano", "Artico"}, 1));
            qs.add(new Question("Capitale della Spagna?", new String[]{"Barcellona", "Madrid", "Siviglia", "Valencia"}, 1));
            qs.add(new Question("Cosa indica il numero atomico?", new String[]{"Neutroni", "Protoni", "Elettroni", "Massa"}, 1));
            qs.add(new Question("In che anno è iniziata la 1^ GM?", new String[]{"1914", "1915", "1918", "1939"}, 0));
            qs.add(new Question("Qual è il metallo più leggero?", new String[]{"Litio", "Alluminio", "Ferro", "Piombo"}, 0));
            qs.add(new Question("Chi dipinse la Cappella Sistina?", new String[]{"Leonardo", "Michelangelo", "Raffaello", "Donato"}, 1));
            qs.add(new Question("Quante corde ha una chitarra standard?", new String[]{"4", "5", "6", "12"}, 2));
            qs.add(new Question("Qual è la capitale del Portogallo?", new String[]{"Lisbona", "Porto", "Madrid", "Sintra"}, 0));
        } else if ("de".equals(lang)) {
            qs.add(new Question("Hauptstadt von Frankreich?", new String[]{"London", "Berlin", "Paris", "Madrid"}, 2));
            qs.add(new Question("Wie viele Seiten hat ein Sechseck?", new String[]{"5", "6", "7", "8"}, 1));
            qs.add(new Question("Was ist der rote Planet?", new String[]{"Mars", "Venus", "Jupiter", "Saturn"}, 0));
            qs.add(new Question("Größtes Tier der Welt?", new String[]{"Elefant", "Blauwal", "Giraffe", "Hai"}, 1));
            qs.add(new Question("Hauptstadt von Spanien?", new String[]{"Barcelona", "Madrid", "Sevilla", "Valencia"}, 1));
            qs.add(new Question("Wie viele Planeten hat unser Sonnensystem?", new String[]{"7", "8", "9", "10"}, 1));
            qs.add(new Question("Woraus besteht Wasser?", new String[]{"H2O", "CO2", "O2", "NaCl"}, 0));
            qs.add(new Question("Hauptstadt von Italien?", new String[]{"Mailand", "Venedig", "Rom", "Neapel"}, 2));
            qs.add(new Question("Wie viele Zähne hat ein Erwachsener?", new String[]{"28", "30", "32", "34"}, 2));
            qs.add(new Question("Was kocht bei 100 Grad?", new String[]{"Milch", "Öl", "Wasser", "Wein"}, 2));
            qs.add(new Question("Wie viele Zwerge hat Schneewittchen?", new String[]{"5", "6", "7", "8"}, 2));
            qs.add(new Question("Symbol für Gold?", new String[]{"Ag", "Au", "Or", "O"}, 1));
            qs.add(new Question("Wer malte die Sixtinische Kapelle?", new String[]{"Leonardo", "Michelangelo", "Raffael", "Donatello"}, 1));
            qs.add(new Question("Auf welchem Kontinent liegt Ägypten?", new String[]{"Afrika", "Asien", "Europa", "Südamerika"}, 0));
            qs.add(new Question("Wie viele Tage hat ein Schaltjahr?", new String[]{"365", "366", "364", "360"}, 1));
        } else if ("es".equals(lang)) {
            qs.add(new Question("¿Capital de Francia?", new String[]{"Londres", "Berlín", "París", "Madrid"}, 2));
            qs.add(new Question("¿Cuántos lados tiene un hexágono?", new String[]{"5", "6", "7", "8"}, 1));
            qs.add(new Question("¿Cuál es el planeta rojo?", new String[]{"Marte", "Venus", "Júpiter", "Saturno"}, 0));
            qs.add(new Question("¿Animal más grande del mundo?", new String[]{"Elefante", "Ballena Azul", "Jirafa", "Tiburón"}, 1));
            qs.add(new Question("¿Capital de España?", new String[]{"Barcelona", "Madrid", "Sevilla", "Valencia"}, 1));
            qs.add(new Question("¿Cuántos planetas hay en el sistema solar?", new String[]{"7", "8", "9", "10"}, 1));
            qs.add(new Question("¿Quién pintó la Mona Lisa?", new String[]{"Miguel Ángel", "Rafael", "Leonardo", "Donatello"}, 2));
            qs.add(new Question("¿De qué está hecha el agua?", new String[]{"H2O", "CO2", "O2", "NaCl"}, 0));
            qs.add(new Question("¿Capital de Italia?", new String[]{"Milán", "Venecia", "Roma", "Nápoles"}, 2));
            qs.add(new Question("¿Cuántos dientes tiene un adulto?", new String[]{"28", "30", "32", "34"}, 2));
        } else {
            qs.add(new Question("Capital of France?", new String[]{"London", "Berlin", "Paris", "Madrid"}, 2));
            qs.add(new Question("How many sides does a hexagon have?", new String[]{"5", "6", "7", "8"}, 1));
            qs.add(new Question("Which is the red planet?", new String[]{"Mars", "Venus", "Jupiter", "Saturn"}, 0));
            qs.add(new Question("Largest animal in the world?", new String[]{"Elephant", "Blue Whale", "Giraffe", "Shark"}, 1));
            qs.add(new Question("Capital of Spain?", new String[]{"Barcelona", "Madrid", "Seville", "Valencia"}, 1));
            qs.add(new Question("How many planets are in our solar system?", new String[]{"7", "8", "9", "10"}, 1));
            qs.add(new Question("Who painted the Mona Lisa?", new String[]{"Michelangelo", "Raffaello", "Leonardo", "Donatello"}, 2));
            qs.add(new Question("What is water made of?", new String[]{"H2O", "CO2", "O2", "NaCl"}, 0));
            qs.add(new Question("Capital of Italy?", new String[]{"Milan", "Venice", "Rome", "Naples"}, 2));
            qs.add(new Question("How many teeth does an adult have?", new String[]{"28", "30", "32", "34"}, 2));
        }
        return qs;
    }

    public static final String[][] APPS = {
        {"ig", "INSTAGRAM", "instagram.com", "com.instagram.android"},
        {"tt", "TIKTOK", "tiktok.com", "com.zhiliaoapp.musically"},
        {"th", "THREADS", "threads.net", "com.instagram.barcelona"},
        {"yt", "YOUTUBE", "youtube.com", "com.google.android.youtube"},
        {"x",  "X / TWITTER", "x.com", "com.twitter.android"},
        {"fb", "FACEBOOK", "facebook.com", "com.facebook.katana"},
        {"sc", "SNAPCHAT", "snapchat.com", "com.snapchat.android"},
        {"rd", "REDDIT", "reddit.com", "com.reddit.frontpage"},
        {"li", "LINKEDIN", "linkedin.com", "com.linkedin.android"},
        {"pt", "PINTEREST", "pinterest.com", "com.pinterest"},
        {"tw", "TWITCH", "twitch.tv", "tv.twitch.android.app"},
        {"wa", "WHATSAPP", "whatsapp.com", "com.whatsapp"},
        {"tg", "TELEGRAM", "telegram.org", "org.telegram.messenger"},
        {"nf", "NETFLIX", "netflix.com", "com.netflix.mediaclient"},
        {"dv", "DISNEY+", "disneyplus.com", "com.disney.disneyplus"},
        {"pv", "PRIME VIDEO", "primevideo.com", "com.amazon.avod.thirdpartyclient"},
        {"dc", "DISCORD", "discord.com", "com.discord"},
        {"az", "AMAZON", "amazon.com", "com.amazon.mShop.android.shopping"},
    };

    public static String[] TOPICS = {"logic", "math", "knowledge"};
    public static String[] DIFFS = {"medio", "difficile"};

    public static String idToPackage(String id) {
        for (String[] app : APPS) if (app[0].equals(id)) return app[3];
        return id;
    }

    public static String packageToId(String pkg) {
        for (String[] app : APPS) {
            // Un package reale dovrebbe avere un nome definito, evito di matchare generici come "com"
            if (pkg.equals(app[3]) || pkg.startsWith(app[3] + ".")) return app[0]; 
        }
        return null;
    }
}
