package com.example.carchive.helpers

import com.example.carchive.entities.Contact
import com.example.carchive.entities.ContactActivity
import com.example.carchive.R
import com.example.carchive.entities.ContactOfferState

object MockDataLoader {
    fun getMockContacts() : List<Contact> = listOf(
        Contact(1, "Ivo", "Ivić", "97626517542", "Hrvatska", "Daruvar",
            "Gradska 21", "-", "0987863451", "iivic@mail.hr",
            "Zainteresiran za obiteljski automobil.",
            "Kupnja",
            ContactActivity("Aktivni kontakt", R.drawable.ic_aktivan_kontakt),
            ContactOfferState("Ponuda nije poslana", R.drawable.ic_x)
        ),
        Contact(2, "Ana", "Anić", "89722475418", "Hrvatska", "Rijeka",
            "Kratka 12", "-", "0998796542", "aanic@mail.hr",
            "-",
            "Kupnja",
            ContactActivity("Neaktivni kontakt", R.drawable.ic_neaktivan_kontakt),
            ContactOfferState("Ponuda poslana", R.drawable.ic_check)
        ),
        Contact(3, "Mirko", "Filipović", "88236556441", "Hrvatska", "Vinkovci",
            "Augusta Šenoe 17", "-", "098786241", "mfilipovic@mail.hr",
            "Treba mu automobil zbog putovanja na posao.",
            "Najam",
            ContactActivity("Aktivni kontakt", R.drawable.ic_aktivan_kontakt),
            ContactOfferState("Ponuda nije poslana", R.drawable.ic_x)
        ),
    )
}