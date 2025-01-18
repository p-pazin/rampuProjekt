package com.example.carchive.viewmodels

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carchive.data.dto.ContractDetailedRentDto
import com.example.carchive.data.dto.ContractDetailedSaleDto
import com.example.carchive.data.dto.ContractDto
import com.example.carchive.data.network.Result
import com.example.carchive.data.repositories.ContractRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ContractsViewModel : ViewModel() {
    private val contractRepository = ContractRepository()
    private val _contracts = MutableStateFlow<List<ContractDto>>(listOf())
    val contracts = _contracts.asStateFlow()

    private val _contractSale = MutableLiveData<ContractDetailedSaleDto?>()
    val contractSale: LiveData<ContractDetailedSaleDto?> get() = _contractSale

    private val _contractRent = MutableLiveData<ContractDetailedRentDto?>()
    val contractRent: LiveData<ContractDetailedRentDto?> get() = _contractRent

    fun fetchContracts() {
        viewModelScope.launch {
            val contractsFromRepository = when (val result = contractRepository.getContracts()) {
                is Result.Success -> result.data
                is Result.Error -> listOf()
            }
            _contracts.update { contractsFromRepository }
        }
    }

    fun fetchContractById(id: Int, contractType : Int) {
        viewModelScope.launch {
            if (contractType == 1) {
                val contractFromRepository = when (val result = contractRepository.getContractSaleById(id)) {
                    is Result.Success -> result.data
                    is Result.Error -> null
                }
                _contractSale.postValue(contractFromRepository)
            } else {
                val contractFromRepository = when (val result = contractRepository.getContractRentById(id)) {
                    is Result.Success -> result.data
                    is Result.Error -> null
                }
                _contractRent.postValue(contractFromRepository)
            }
        }
    }

    fun createPdfAndOpen(context : Context, contract : ContractDetailedSaleDto) {
        val pdfDocument = PdfDocument()

        if(contract.vehicles != null) {
            for (vehicle in contract.vehicles) {
                createContractForSale(pdfDocument, contract, contract.vehicles.indexOf(vehicle))
            }
        }
        else {
            createContractForSale(pdfDocument, contract, -1)
        }

        var filePath = File(context.getExternalFilesDir(null),
            "ugovor_o_kupoprodaji_${contract.dateOfCreation}_${contract.companyName}_${contract.contactName}.pdf")

        try {
            val fileOutputStream = FileOutputStream(filePath)
            pdfDocument.writeTo(fileOutputStream)
            pdfDocument.close()

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                filePath
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(intent)

        } catch (e: IOException) {
            Toast.makeText(context, "Greška kod generiranja PDF-a!", Toast.LENGTH_SHORT).show()
        }

    }

    fun createPdfAndOpen(context : Context, contract : ContractDetailedRentDto) {
        val pdfDocument = PdfDocument()

        createContractForRent(pdfDocument, contract)

        var filePath = File(context.getExternalFilesDir(null),
            "ugovor_o_najmu_${contract.dateOfCreation}_" +
                    "${contract.firstNameDirector + contract.lastNameDirector}_" +
                    "${contract.firstNameContact + contract.lastNameContact}.pdf")


        try {
            val fileOutputStream = FileOutputStream(filePath)
            pdfDocument.writeTo(fileOutputStream)
            pdfDocument.close()

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                filePath
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(intent)

        } catch (e: IOException) {
            Toast.makeText(context, "Greška kod generiranja PDF-a!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun createContractForRent(pdfDocument: PdfDocument, contract: ContractDetailedRentDto) {
        val boldPaint = Paint().apply {
            textSize = 14f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }
        val normalPaint = Paint().apply {
            textSize = 12f
        }

        val pageInfo = PdfDocument.PageInfo.Builder(600, 900, 1).create()

        var page = pdfDocument.startPage(pageInfo)
        var canvas: Canvas = page.canvas

        var yPosition = 50f
        val lineSpacing = 25f
        val leftMargin = 55f
        val maxLineWidth = 500f

        canvas.drawText("UGOVOR O NAJMU AUTOMOBILA", 200f, yPosition, boldPaint)
        yPosition += lineSpacing * 2

        val introduction = """
        “Iznajmi i vrati auto” d.o.o. iz ${contract.city}, ${contract.address}, OIB: ${contract.pin}
        zastupano po direktoru ${contract.firstNameDirector} ${contract.lastNameDirector}
        i
        ${contract.firstNameContact} ${contract.lastNameContact}, OIB: ${contract.pinContact}, iz ${contract.cityContact}, ${contract.addressContact},
        sklopili su u ${contract.city} ${contract.dateOfCreation}
    """.trimIndent()
        yPosition = writeTextBlock(introduction, canvas, normalPaint, leftMargin, yPosition, lineSpacing, maxLineWidth)

        val articles = listOf(
            "Članak 1.\nIznajmi i vrati auto d.o.o. (u daljnjem tekstu: iznajmljivač) iznajmljuje osobno vozilo ${contract.firstNameContact} ${contract.lastNameContact} (u daljnjem tekstu: korisnik).",
            "Članak 2.\nOsobni automobil iz članka 1 ovog ugovora je:\n${contract.brand} ${contract.model}, ${contract.engine}, registracijskih oznaka ${contract.registration}.",
            "Članak 3.\nOvim ugovorom se ugovara da je korisnik jedina ovlaštena osoba koja će koristiti osobni automobil iz članka 2 ovog ugovora, za vrijeme trajanja najma.\nKorisnik se obvezuje da će osobni automobil koji je predmet najma koristiti sukladno zakonu o sigurnosti prometa na cestama.\nKorisnik se obvezuje da će osobno vozilo koje je predmet najma koristiti samo za vlastite potrebe.",
            "Članak 4.\nKorisnik je stariji od 22 godine i posjeduje važeću vozačku dozvolu najmanje 2 godine.",
            "Članak 5.\nOsobni automobil iz članka 2 se iznajmljuje korisniku na vrijeme od ${ChronoUnit.DAYS.between(LocalDate.parse(contract.startDate), LocalDate.parse(contract.endDate))} dana sa početkom najma na dan sklapanja ovog ugovora.\nOsobni automobil iz članka 2 se predaje korisniku najma u prostorijama tj. parkiralištu iznajmljivača.",
            "Članak 6.\nNajamnina za osobni automobil iz članka 2 iznosi ${contract.price}€ po danu najma.\nKorisnik se obvezuje platiti polog u iznosu od najmanje 20% ukupne cijene najma s obzirom na ukupno vrijeme najma navedeno u članku 5.",
            "Članak 7.\nKorisniku je vozilo predano u voznom tj. ispravnom stanju zajedno sa svom potrebnom opremom i dijelovima.\nKorisniku je predan osobni automobil sa punim rezervoarom goriva.\nKorisnik je prilikom preuzimanja osobnog automobila iz članka 2 ovog ugovora pregledao navedeno vozilo radi utvrđivanja općeg stanja kao i prisutne opreme i dijelova osobnog vozila.\nSve prigovore na stanje automobila kao i na stanje opreme tj. dijelova, korisnik je obvezan odmah uputiti iznajmljivaču o čemu će se napisati izvještaj koji će biti dodatak ovom ugovoru.",
            "Članak 8.\nKorisnik se obvezuje da neće:\n– koristiti predmet najma radi obavljanja bilo kakvih radnji koje bi se mogle okarakterizirati kao radnje u suprotnosti sa zakonima koji su na snazi u zemlji najma.\n– koristiti predmet najma pod utjecajem alkohola i/ili narkotika te bilo kojih drugih tvari koje smanjuju sposobnosti upravljanja vozilom\n– pušiti duhanske i/ili bilo koje druge proizvode u unutrašnjosti automobila\n– koristiti predmet najma za sudjelovanje u natjecanjima ili rally utrkama\n– koristiti predmet najma za prijevoz predmeta, materijala ili drugih stvari koje bi mogle oštetiti osobno vozilo na bilo koji način\n– koristiti predmet najma za vuču drugih vozila\n– koristiti predmet najma za plaćeni prijevoz putnika i robe",
            "Članak 9.\nKorisnik ne može otuđiti osobno vozilo koje je predmet najma ili bilo koji dio navedenog vozila.",
            "Članak 10.\nZa vrijeme trajanja najma korisnik je obvezan se brinuti o vozilu, kao i poduzimati sve potrebne radnje radi osiguranja tehničke ispravnosti samoga osobnog automobila.\nKorisnik je odgovoran za svu štetu koja nastane nepoduzimanjem radnji iz prethodnog stavka.",
            "Članak 11.\nKorisnik snosi sve troškove koji su nastali normalnom upotrebom predmeta najma kao što su: prometni prekršaji, naknade za parkiranje i/ili garažiranje osobnog vozila, naknade za upotrebu autocesta, troškovi goriva i drugi slični troškovi.",
            "Članak 12.\nKorisnik se obvezuje vratiti osobni automobil zajedno sa svom opremom i dijelovima, te u istom stanju u kojem ga je i zaprimio.\nKorisnik mora vratiti osobni automobil sa punim rezervoarom goriva.\nUgovoreno mjesto vraćanja automobila iznajmljivaču su prostorije tj. parkiralište iznajmljivača.\nTočno vrijeme (sat) vračanja automobila na dan prestanka najma će biti kasnije određeno osobnim dogovorom između korisnika i iznajmljivača.",
            "Članak 13.\nSvako oštećenje ili promjena stanja osobnog automobila koje je nastalo tijekom trajanja najma, korisnik je obvezan naznačiti tj. informirati iznajmljivača prilikom vraćanja samog vozila.\nUkoliko je oštećenje takve prirode da utječe na tehničku ispravnost vozila i/ili nemogućnost sigurnog korištenja tj. upravljanja istog, korisnik je obvezan odmah informirati iznajmljivača neposredno nakon nastanka ili saznanja takve štete.\nKorisnik je dužan bez odgode informirati iznajmljivača i u slučaju kada predmetu najma prijeti nastanak štete koja bi utjecala na tehničku ispravnost vozila i/ili nemogućnost sigurnog korištenja tj. upravljanja.\nU slučaju nastanka štete za koju je bilo potrebno informirati nadležne organe, a korisnik je to propustio učiniti, troškove štete snosi sami korisnik.\nKorisnik snosi i troškove prema trećim osobama u slučaju da su nastale nepravilnom upotrebom predmeta najma od strane korisnika.",
            "Članak 14.\nKorisnik se obvezuje da će snositi troškove čišćenja automobila ukoliko se u trenutku vračanja utvrdi da je vanjština i/ili unutrašnjost vozila posebno prljava (prljavština ili neugodni mirisi koji nisu mogli nastati normalnom upotrebom predmeta najma).",
            "Članak 15.\nUkoliko korisnik izgubi ključeve vozila koje je predmet najma, on se obvezuje nadoknaditi štetu u iznosu od 150€.",
            "Članak 16.\nNajam osobnog vozila se može produžiti najkasnije 24 sata prije isteka roka iz članka 5 ovog ugovora.\nUkoliko korisnik prekorači rok vračanja automobila, a da pri tom nije obavijestio i/ili se dogovorio sa iznajmljivačem o produženju najma, osobni automobil iz članka 2. koji je predmet najma, će se smatrati ukradenim pri čemu iznajmljivač ima pravo na sve postupke koje mu omogućuje zakon.",
            "Članak 17.\nIznajmljivač nije odgovoran za štetu nastalu gubitkom, krađom ili oštećenjem imovine korisnika koja se nalazi u vozilu za vrijeme trajanja najma i nakon završetka najma.",
            "Članak 18.\nOsobni automobil iz članka 2. je osigurano za slučaj štete prouzrokovane trećim osobama.\nOsiguravajuće društvo pokriva štetu koju prouzrokuje predmet najma do visine po zakonu osigurane svote.\nKorisnik plaća iznajmljivaču dodatno osiguranje vozila za slučaj prometne nesreće i krađe vozila u iznosu od 30€.",
            "Članak 19.\nIznajmljivač ima pravo uvida u stanje osobnog vozila za vrijeme trajanja samog najma.",
            "Članak 20.\nIznajmljivač pridržava pravo vozilo oduzeti, bez prethodne obavijesti i o trošku korisnika, ukoliko se osobno vozilo koristi protivno ugovorenim uvjetima ovog ugovora.",
            "Članak 21.\nUgovorne strane su suglasne da će eventualne sporove rješavati sporazumom.\nU slučaju da su ugovorne strane u nemogućnosti doći do sporazumnog rješenja ugovara se nadležnost suda u Rijeci.",
            "Članak 22.\nUgovorne strane prihvaćaju sva prava i obveze koje proizlaze iz ovog ugovora stavljanjem vlastoručnih potpisa na isti.",
            "Članak 23.\nOvaj ugovor je napravljen u 2 istovjetna primjeraka. Svaka ugovorna strana zadržava po jedan primjerak."
        )

        for (article in articles) {
            yPosition = writeTextBlock(article, canvas, normalPaint, leftMargin, yPosition, lineSpacing, maxLineWidth)

            if (yPosition > 850f) {
                pdfDocument.finishPage(page)
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                yPosition = 50f
            }
        }

        pdfDocument.finishPage(page)
    }

    private fun createContractForSale(pdfDocument: PdfDocument,
                                      contract: ContractDetailedSaleDto, index : Int) {
        val paint = Paint()
        val boldPaint = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 14f
        }
        val smallTextPaint = Paint().apply {
            textSize = 12f
        }

        val pageInfo = PdfDocument.PageInfo.Builder(600, 900, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        boldPaint.textSize = 18f
        canvas.drawText("UGOVOR O KUPOPRODAJI MOTORNOG VOZILA", 100f, 50f, boldPaint)

        boldPaint.textSize = 13f

        val startX = 55f
        val lineY = floatArrayOf(90f, 120f)
        for (y in lineY) {
            canvas.drawLine(startX, y, 550f, y, paint)
        }

        canvas.drawText("Prodavatelj (Ime i prezime / Pravna osoba): ${contract.companyName}", startX, 85f, smallTextPaint)
        canvas.drawText("OIB: ${contract.companyPin}", 450f, 85f, smallTextPaint)
        canvas.drawText("Adresa: ${contract.companyAddress}", startX, 115f, smallTextPaint)

        for (y in lineY.map { it + 80 }) {
            canvas.drawLine(startX, y, 550f, y, paint)
        }

        canvas.drawText("Kupac (Ime i prezime / Pravna osoba): ${contract.contactName}", startX, 165f, smallTextPaint)
        canvas.drawText("OIB: ${contract.contactPin}", 450f, 165f, smallTextPaint)
        canvas.drawText("Adresa: ${contract.contactAddress}", startX, 195f, smallTextPaint)

        canvas.drawText("Datum:  ${contract.dateOfCreation}", startX, 235f, smallTextPaint)
        canvas.drawLine(102f, 240f, 250f, 240f, paint)
        canvas.drawText("Mjesto:  ${contract.place}", 300f, 235f, smallTextPaint)
        canvas.drawLine(345f, 240f, 550f, 240f, paint)

        boldPaint.textSize = 15f
        canvas.drawText("1. Prodavatelj prodaje kupcu motorno vozilo:", startX, 270f, smallTextPaint)

        paint.style = Paint.Style.STROKE
        val tableTop = 290f
        val rowHeight = 40f
        val colWidth = (550f - startX) / 2

        for (i in 0..2) {
            canvas.drawLine(startX, tableTop + (i * rowHeight), 550f, tableTop + (i * rowHeight), paint)
        }
        for (i in 0..2) {
            canvas.drawLine(startX + (i * colWidth), tableTop, startX + (i * colWidth), tableTop + (rowHeight * 2), paint)
        }

        if(index != -1) {
            canvas.drawText("Marka vozila: ${contract.vehicles!![index].brand} ${contract.vehicles!![index].model}", startX + 10, tableTop + 25f, smallTextPaint)
            canvas.drawText("Zapremnina motora: ${contract.vehicles!![index].cubicCapacity}cc", startX + (colWidth) + 10, tableTop + 25f, smallTextPaint)

            canvas.drawText("Registracijska oznaka: ${contract.vehicles!![index].registration}", startX + 10, tableTop + rowHeight + 25f, smallTextPaint)
            canvas.drawText("Registriran do: ${contract.vehicles!![index].registration}", startX + (colWidth) + 10, tableTop + rowHeight + 25f, smallTextPaint)

            canvas.drawText("2. Prodajna cijena vozila iz članka 1. ugovorena je u iznosu od  ${contract.vehicles!![index].price}", startX, 390f, smallTextPaint)
            canvas.drawText("eura.", 450f, 390f, smallTextPaint)
            canvas.drawLine(395f, 393f, 445f, 393f, paint)
        }
        else {
            canvas.drawText("Marka vozila: ${contract.vehicle?.brand} ${contract.vehicle?.model}", startX + 10, tableTop + 25f, smallTextPaint)
            canvas.drawText("Zapremnina motora: ${contract.vehicle?.cubicCapacity}cc", startX + (colWidth) + 10, tableTop + 25f, smallTextPaint)

            canvas.drawText("Registracijska oznaka: ${contract.vehicle?.registration}", startX + 10, tableTop + rowHeight + 25f, smallTextPaint)
            canvas.drawText("Registriran do: ${contract.vehicle?.registeredTo}", startX + (colWidth) + 10, tableTop + rowHeight + 25f, smallTextPaint)

            canvas.drawText("2. Prodajna cijena vozila iz članka 1. ugovorena je u iznosu od  ${contract.price}", startX, 390f, smallTextPaint)
            canvas.drawText("eura.", 450f, 390f, smallTextPaint)
            canvas.drawLine(395f, 393f, 445f, 393f, paint)
        }

        canvas.drawText("3. Kupac je iznos isplatio prodavatelju u cijelosti.", startX, 420f, smallTextPaint)

        canvas.drawText("4. Prodavatelj jamči da je vozilo njegovo vlasništvo i da nije opterećeno.", startX, 450f, smallTextPaint)

        canvas.drawText("5. Kupac je pregledao vozilo i nema primjedbi.", startX, 480f, smallTextPaint)

        canvas.drawText("6. Kupac preuzima vozilo: Ključevi, prometna dozvola", startX, 510f, smallTextPaint)
        canvas.drawLine(195f, 515f, 550f, 515f, paint)

        canvas.drawText("7. Porez i ostale troškove prijenosa snosi kupac.", startX, 540f, smallTextPaint)

        canvas.drawText("8. U slučaju spora, nadležan je sud u:  ${contract.place}", startX, 570f, smallTextPaint)
        canvas.drawLine(260f, 575f, 550f, 575f, paint)

        canvas.drawText("Prodavatelj:", 100f, 620f, smallTextPaint)
        canvas.drawLine(50f, 640f, 250f, 640f, paint)
        canvas.drawText("Kupac:", 400f, 620f, smallTextPaint)
        canvas.drawLine(350f, 640f, 550f, 640f, paint)

        pdfDocument.finishPage(page)
    }

    private fun writeTextBlock(
        text: String,
        canvas: Canvas,
        paint: Paint,
        leftMargin: Float,
        startY: Float,
        lineSpacing: Float,
        maxLineWidth: Float
    ): Float {
        var yPosition = startY
        val lines = text.split("\n")

        for (line in lines) {
            yPosition = writeLineWithWordWrap(line, canvas, paint, leftMargin, yPosition, lineSpacing, maxLineWidth)
        }

        return yPosition
    }

    private fun writeLineWithWordWrap(
        line: String,
        canvas: Canvas,
        paint: Paint,
        leftMargin: Float,
        startY: Float,
        lineSpacing: Float,
        maxLineWidth: Float
    ): Float {
        var yPosition = startY
        val words = line.split(" ")

        var currentLine = StringBuilder()
        for (word in words) {
            val potentialLine = if (currentLine.isEmpty()) word else "${currentLine} $word"
            if (paint.measureText(potentialLine) > maxLineWidth) {
                canvas.drawText(currentLine.toString(), leftMargin, yPosition, paint)
                yPosition += lineSpacing
                currentLine = StringBuilder(word)
            } else {
                currentLine.append(if (currentLine.isEmpty()) word else " $word")
            }
        }
        if (currentLine.isNotEmpty()) {
            canvas.drawText(currentLine.toString(), leftMargin, yPosition, paint)
            yPosition += lineSpacing
        }

        return yPosition
    }
}