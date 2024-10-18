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
F01 | Prijava i registracija | Sustav će omogućiti prijavu korisnika pomoću korisničkog imena i lozinke uz uvjet da podatci verificiraju i omoguće pristup aplikaciji. Registracija se koristi kao forma za slanje upita koji se mora odobriti od admina. | Dario Vučina
F02 | Upravljanje vozilima | Sustav će omogućiti korisniku unos, izmjenu, pregled i brisanje podataka o vozilima. | Paula Pažin
F03 | Upravljanje kontaktima | Sustav će omogućiti unos, izmjenu, pregled i brisanje podataka o kontaktima klijenata i partnera. | Paula Pažin
F04 | Pregled mape lokacija | Sustav će omogućiti prikaz trenutnih lokacija vozila i kontakata na interaktivnoj karti. | Dario Vučina
F05 | Upravljanje aktivnostima | Sustav će omogućiti unos i praćenje aktivnosti vezanih za vozila i kontakte, uključujući transakcije i servise. | Dario Vučina
F06 | Upravljanje mailovima | Sustav će omogućiti slanje, primanje i praćenje e-mail komunikacije s klijentima i partnerima. | Dario Vučina
F07 | Statistika | Sustav će omogućiti generiranje statističkih izvještaja o poslovanju, uključujući analize prodaje, najma i korištenja vozila. | Antonio Vinković
F08 | Pretraga vozila i kontakata | Sustav će omogućiti naprednu pretragu vozila i kontakata putem različitih filtara. | Antonio Vinković
F09 | Objavljivanje na oglasnik| Sustav će omogućiti objavu vozila na vanjske oglasnike s pripadajućim podacima o vozilu. | Antonio Vinković
F10 | Obavijesti i podsjetnici | Sustav će korisnicima slati obavijesti i podsjetnike vezane uz važne događaje (servisi, ugovori). | Paula Pažin
F11 | Vizualno prepoznavanje vozila | Sustav će omogućiti korisnicima prikaz podataka određenog vozila putem slikanja tog vozila i prepoznavanja registracije. | Antonio Vinković 
F12 | Postavke | Sustav će omogućiti korisnicima izmjenu postavki aplikacije, uključujući jezik i notifikacije. | Paula Pažin


## Tehnologije i oprema
Za razvoj aplikacije Carchive koristit ćemo Kotlin kao programski jezik za izradu nativne Android aplikacije. Razvojno okruženje bit će Android Studio za kodiranje, testiranje i debugging.
Za verzioniranje koda koristit ćemo Git, a platforma GitHub bit će korištena za hosting repozitorija, kolaboraciju među članovima tima, te praćenje zadataka pomoću GitHub Projects. Dokumentaciju ćemo voditi kroz GitHub Wiki.
Dizajn korisničkog sučelja izradit ćemo u alatu Figma, koji omogućava jednostavnu izradu i pregled dizajna te suradnju.

## Baza podataka i web server
Tražimo pristup serveru na kojemu ćemo moći imati bazu podataka.
