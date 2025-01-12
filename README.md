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
F02 | Upravljanje katalogom vozila | Sustav će omogućiti korisniku unos, izmjenu, pregled i brisanje podataka o vozilima. | Paula Pažin
F03 | Upravljanje katalogom kontakata | Sustav će omogućiti unos, izmjenu, pregled i brisanje podataka o kontaktima klijenata i partnera. | Antonio Vinković
F04 | Objavljivanje na oglasnik |Sustav će omogućiti objavu vozila na vanjske oglasnike s pripadajućim podacima o vozilu. | Dario Vučina
F05 | Statistika | Sustav će omogućiti generiranje statističkih izvještaja o poslovanju, uključujući analize prodaje, najma i korištenja vozila. | Antonio Vinković
F06 | Prodaja vozila | Sustav će omogućiti unos i obradu podataka potrebnih za provođenje procesa prodaje vozila. | Antonio Vinković
F07 | Iznajmljivanje vozila | Sustav će omogućiti unos i obradu podataka potrebnih za iznajmljivanje vozila. | Dario Vučina
F08 | Pregled mape lokacija | Sustav će omogućiti prikaz trenutnih lokacija vozila i kontakata na interaktivnoj karti. | Paula Pažin
F09 | Upravljanje ponudama | Sustav će omogućiti kreiranje, prilagodbu i slanje ponuda za vozila klijentima. | Paula Pažin


## Tehnologije i oprema
Za razvoj aplikacije Carchive koristit ćemo Kotlin kao programski jezik za izradu nativne Android aplikacije. Razvojno okruženje bit će Android Studio za kodiranje, testiranje i debugging.
Za verzioniranje koda koristit ćemo Git, a platforma GitHub bit će korištena za hosting repozitorija, kolaboraciju među članovima tima, te praćenje zadataka pomoću GitHub Projects. Dokumentaciju ćemo voditi kroz GitHub Wiki.
Dizajn korisničkog sučelja izradit ćemo u alatu Figma, koji omogućava jednostavnu izradu i pregled dizajna te suradnju.

## Baza podataka i web server
Napraviti ćemo zajedničku bazu podataka s RPP-om i backend u .net tehnologiji.
