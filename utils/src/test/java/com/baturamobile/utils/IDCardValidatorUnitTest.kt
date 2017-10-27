package com.baturamobile.utils

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test


/**
 * Created by vssnake on 27/10/2017.
 */

class IDCardValidatorUnitTest{

    @Test
    fun testValidIDCardSpain(){
        assertTrue(IDCardValidator.instance.validateIDCARD("ES","72318689L"))
    }
    @Test
    fun testInvalidIDCardSpain(){
        assertFalse(IDCardValidator.instance.validateIDCARD("ES","45821256L"))
    }

    @Test
    fun testOtherCard(){
        assertTrue(IDCardValidator.instance.validateIDCARD("","458656256L"))
    }
}