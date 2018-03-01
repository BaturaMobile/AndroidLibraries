package com.baturamobile.utils

import com.baturamobile.utils.validators.VATValidator
import junit.framework.Assert
import org.junit.Test

/**
 * Created by vssnake on 27/10/2017.
 */

class VatValidatorUnitTest {

    @Test
    fun testValidVATSpain(){
        Assert.assertTrue(VATValidator.instance.validateVAT("ES", "B95451092"))
    }



    @Test
    fun testInvalidVATSpain(){
        Assert.assertFalse(VATValidator.instance.validateVAT("ES", "C95451092"))
    }



    @Test
    fun testOtherVATCard(){
        Assert.assertTrue(VATValidator.instance.validateVAT("", "B95451092"))
    }
}