package com.baturamobile.utils.validators

/**
 * Created by vssnake on 27/10/2017.
 */
class VATValidator private constructor(){

    private object Holder { val INSTANCE = VATValidator() }

    companion object {
        val instance : VATValidator by lazy { Holder.INSTANCE }
    }

    enum class COUNTRIES {
        ES()
    }

    private val countries = HashMap<COUNTRIES,Regex>()
    private val spanishLetterVerefied : HashMap<kotlin.Int,String>

    init{
        countries.put(COUNTRIES.ES, Regex("^[A-Wa-w][0-9]{8}\$"))

        spanishLetterVerefied = hashMapOf(0 to "J", 1 to "A", 2 to "B", 3 to "C",
                4 to "D", 5 to "E", 6 to "F", 7 to "G", 8 to "H", 9 to "I")
    }

    fun validateVAT(isoCountry : String,codeCard: String) : Boolean{
        return try {
            when(COUNTRIES.valueOf(isoCountry)){
                COUNTRIES.ES -> isSpanishCardValid(codeCard)
            }
        }catch (e : Exception){
            //If country not listed return true
            return true
        }

    }


    private fun isSpanishCardValid(codeCard: String) : Boolean {
        if(countries[COUNTRIES.ES]!!.matches(codeCard)){

            try {
                val centralDigists = codeCard.substring(1,8)
                val evenSum = Integer.valueOf(centralDigists.substring(1,2)) +
                        Integer.valueOf(centralDigists.substring(3,4)) +
                        Integer.valueOf(centralDigists.substring(5,6))

                val odd1 = sumDigits(Integer.valueOf(centralDigists.substring(0,1)) * 2)

                val odd2 = sumDigits(Integer.valueOf(centralDigists.substring(2,3)) * 2)

                val odd3 = sumDigits(Integer.valueOf(centralDigists.substring(4,5)) * 2)

                val odd4 = sumDigits(Integer.valueOf(centralDigists.substring(6,7)) * 2)

                val oddSum = odd1 + odd2 + odd3 + odd4

                val partialSum = evenSum + oddSum

                val EDigit = Integer.valueOf(partialSum.toString()[1].toString())

                val letterFinal = 10- EDigit

                return spanishLetterVerefied.containsKey(letterFinal)
                        //&& spanishLetterVerefied.get(letterFinal).equals(codeCard.substring(0,1),true)
            }catch (e :  Exception){
                return false;
            }



        }
        return false
    }



    private fun sumDigits(number: Int) : Int{
       return sumDigits(number.toString())
    }

    private fun sumDigits(number: String): Int {
        var sum = 0
        for (digit in number) {
            sum += Integer.valueOf(digit.toString())
        }
        return sum
    }




}