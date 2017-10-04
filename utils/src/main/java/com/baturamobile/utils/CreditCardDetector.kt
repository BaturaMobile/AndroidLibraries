package com.baturamobile.utils

import java.util.*

/**
 * Created by vssnake on 02/10/2017.
 */
class CreditCardDetector private constructor() {

    val creditCards = HashMap<ENUMCARDS,Regex>()

    private object Holder { val INSTANCE = CreditCardDetector() }

    companion object {
        val instance : CreditCardDetector by lazy { Holder.INSTANCE }
    }

    enum class ENUMCARDS {
        ELECTRON,
        MAESTRO,
        DANKORT,
        INTERPAYMENT,
        UNIONPAY,
        VISA,
        MASTERCARD,
        AMEX,
        DINERS,
        DISCOVER,
        JCB,
        UNKNOWN
    }

    init {
        creditCards.put(ENUMCARDS.ELECTRON, Regex("^(4026|417500|4405|4508|4844|4913|4917)\\d+\$"))
        creditCards.put(ENUMCARDS.MAESTRO, Regex("^(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d+\$"))
        creditCards.put(ENUMCARDS.DANKORT, Regex("^(5019)\\d+\$"))
        creditCards.put(ENUMCARDS.INTERPAYMENT, Regex("^(636)\\d+\$"))
        creditCards.put(ENUMCARDS.UNIONPAY, Regex("^(62|88)\\d+\$"))
        creditCards.put(ENUMCARDS.VISA, Regex("^4[A-Za-z0-9]{10}(?:[0-9]{3})?\$"))
        creditCards.put(ENUMCARDS.MASTERCARD, Regex("^5[1-5][A-Za-z0-9]{12}\$"))
        creditCards.put(ENUMCARDS.AMEX, Regex("^3[74][A-Za-z0-9]{12}\$"))
    }

    fun detectTypeCard( numberCard : String) : ENUMCARDS {
        for (creditCard in creditCards){
            if (creditCard.value.matches(numberCard)){
                return creditCard.key
            }
        }
        return ENUMCARDS.UNKNOWN
    }




}