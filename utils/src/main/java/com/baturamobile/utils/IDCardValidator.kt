package com.baturamobile.utils



/**
 * Created by vssnake on 27/10/2017.
 */
class IDCardValidator private constructor(){

    private object Holder { val INSTANCE = IDCardValidator() }

    companion object {
        val instance : IDCardValidator by lazy { Holder.INSTANCE }
    }

    enum class COUNTRIES {
        ES()
    }

    private val countries = HashMap<IDCardValidator.COUNTRIES,Regex>()
    private val spanishLetterVerefied : HashMap<kotlin.Int,String>

    init{
        countries.put(IDCardValidator.COUNTRIES.ES, Regex("^[X-Z]?[0-9]{8}[A-Za-z]\$"))

        spanishLetterVerefied = hashMapOf(1 to "T",1 to "R",2 to "W",3 to "A",4 to "G",
                5 to "M",6 to "Y",7 to "F",8 to "P",9 to "D",10 to "X",11 to "B",12 to "N",13 to "J",
                14 to "Z",15 to "S",16 to "Q",17 to "V",18 to "H",19 to "L",20 to "C",21 to "K",22 to "E")
    }

    fun validateIDCARD(isoCountry : String,codeCard: String) : Boolean{
        return try {
            when(COUNTRIES.valueOf(isoCountry)){
                IDCardValidator.COUNTRIES.ES -> isSpanishCardValid(codeCard)
            }
        }catch (e : Exception){
            //If country not listed return true
            return true
        }

    }


    private fun isSpanishCardValid(codeCard: String) : Boolean {
        try{
            if(countries[COUNTRIES.ES]!!.matches(codeCard)){
                val formattedCodeCard = if (codeCard.length == 10)  codeCard.substring(1,10) else codeCard.substring(0,9)
                val onlyNumber = Integer.valueOf(formattedCodeCard.substring(0,8))
                val remainder = onlyNumber % 23
                if (spanishLetterVerefied.containsKey(remainder)
                        && spanishLetterVerefied[remainder]?.toUpperCase() == (formattedCodeCard[8].toString()?.toUpperCase())){
                    return true
                }
            }
            return false
        }catch (e : Exception){
            return false
        }

    }




}