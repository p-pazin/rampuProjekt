# Carchive
## Projektni tim

Ime i prezime | E-mail adresa (FOI) | JMBAG | Github korisničko ime | Seminarska grupa
------------  | ------------------- | ----- | --------------------- | ----------------
Dario Vučina | dvucina22@foi.hr | 0016161255 | dvucina22 | G02
Antonio Vinković | avinkovic22@foi.hr | 0016161208 | Avinkovic22 | G02
Paula Pažin | ppazin22@foi.hr | 0016158520 | ppazin | G02

## Opis domene
Aplikacija Carchive je namijenjena vođenju evidencije kod prodaje te iznajmljivanje automobila pojedine auto-kuće. Primarni cilj ove aplikacije je pružanje cjelovitog sustava za praćenje i upravljanje većim brojem vozila, optimizacija resursa te smanjenju operativnih troškova. Sustav će omogućiti potporu auto-kućama u poboljšanju njihove učinkovitosti i održavanju vozila.


## Specifikacija projekta
Aplikacija će se temeljiti na client-server arhitekturi. Aplikacija će sadržavati sučelje za interakciju s korisnikom, dok će backend(server) pružati pozadinske servise kao što su autentifikacija, upravljanje podatcima i korisnicima.

Oznaka | Naziv | Kratki opis | Odgovorni član tima
------ | ----- | ----------- | -------------------
F01 | Prijava korisnika (Login) | Sustav će omogućiti prijavu korisnika pomoću korisničkog imena i lozinke uz uvjet da podatci verificiraju i omoguće pristup aplikaciji. | Dario Vučina
F02 | Pregled aktivnih vozila | Sustav će omogućiti korisniku pregled svih vozila koja su trenutno dostupna, uključujući osnovne informacije poput marke, modela i trenutnog statusa. | Paula Pažin
F03 | Unos novih vozila | Sustav će omogućiti korisniku unos novih vozila u sustav uz potrebne detalje, kao što su marka, model, godina proizvodnje, registarski broj i cijena. | Paula Pažin
F04 | Izmjena podataka vozila | Sustav će omogućiti korisniku izmjenu podataka o postojećim vozilima, uključujući promjenu cijene, statusa ili drugih informacija. | Paula Pažin
F05 | Brisanje vozila | Sustav će omogućiti korisniku brisanje podataka o vozilima koja više nisu u ponudi. | Paula Pažin
F06 | Pregled kontakata | Sustav će omogućiti korisniku pregled svih unesenih kontakata, uključujući osnovne informacije kao što su ime, prezime, kontakt broj i e-mail adresa. | Antonio Vinković
F07 | Unos novih kontakata | Sustav će omogućiti korisniku unos novih kontakata u bazu podataka uz potrebne detalje o klijentima. | Antonio Vinković
F08 | Izmjena podataka kontakata | Sustav će omogućiti korisniku izmjenu podataka o postojećim kontaktima, uključujući promjene broja telefona ili e-mail adrese. | Antonio Vinković
F09 | Brisanje kontakata | Sustav će omogućiti korisniku brisanje podataka o kontaktima koji više nisu potrebni. | Antonio Vinković
F10 | Pregled aktivnosti | Sustav će omogućiti korisniku pregled povijesti aktivnosti vozila, uključujući transakcije prodaje, iznajmljivanja i servisiranja. | Dario Vučina
F11 | Unos novih aktivnosti | Sustav će omogućiti korisniku unos novih aktivnosti, kao što su nove transakcije (najam, prodaja, servis). | Dario Vučina
F12 | Generiranje izvještaja | Sustav će omogućiti korisniku generiranje izvještaja o poslovanju, uključujući statistike vezane za prodaju i najam vozila. | Dario Vučina


## Tehnologije i oprema
Za razvoj aplikacije Carchive koristit ćemo Kotlin kao programski jezik za izradu nativne Android aplikacije. Razvojno okruženje bit će Android Studio za kodiranje, testiranje i debugging.
Za verzioniranje koda koristit ćemo Git, a platforma GitHub bit će korištena za hosting repozitorija, kolaboraciju među članovima tima, te praćenje zadataka pomoću GitHub Projects. Dokumentaciju ćemo voditi kroz GitHub Wiki.
Dizajn korisničkog sučelja izradit ćemo u alatu Figma, koji omogućava jednostavnu izradu i pregled dizajna te suradnju.

## Baza podataka i web server
Tražimo pristup serveru na kojemu ćemo moći imati bazu podataka.
