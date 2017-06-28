package com.baturamobile.mvp.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manejador de las SharedPreferences.
 * 
 * Created by jrecio.
 */
public class PreferencesManager
{
	/**
	 * Nombre del archivo de las preferences.
	 */
	public static final String PRIVATE_PREFS_NAME = "BackusPrefs";

	/**
	 * Objeto SharedPreferences.
	 */
	protected static SharedPreferences prefs;

	/***
	 * Inicializar las preferences.
	 */
	public static void init( Context context )
	{
		if ( null == prefs )
		{
			prefs = context.getSharedPreferences( PRIVATE_PREFS_NAME, Context.MODE_PRIVATE );
		}
	}

	/**
	 * Devuelve un valor boolean.
	 * 
	 * @param key - Nombre de la preference.
	 * @param def - Valor por defecto.
	 * @return Valor de la preference en boolean.
	 */
	public static boolean getBoolean( String key, boolean def )
	{
		return ( null != prefs ) ? prefs.getBoolean( key, def ) : def;
	}

	/**
	 * Establece un valor boolean.
	 * 
	 * @param key - Nombre de la preference.
	 * @param value - Valor de la preference.
	 */
	public static void setBoolean( String key, boolean value )
	{
		if ( null != prefs )
		{
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean( key, value );
			editor.apply();
		}
	}

	/**
	 * Devuelve un valor int.
	 * 
	 * @param key - Nombre de la preference.
	 * @param def - Valor por defecto.
	 * @return Valor de la preference en int.
	 */
	public static int getInt( String key, int def )
	{
		return ( null != prefs ) ? prefs.getInt( key, def ) : def;
	}

	/**
	 * Establece un valor int.
	 * 
	 * @param key - Nombre de la preference.
	 * @param value - Valor de la preference.
	 */
	public static void setInt( String key, int value )
	{
		if ( null != prefs )
		{
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt( key, value );
			editor.apply();
		}
	}

	/**
	 * Devuelve un valor String.
	 * 
	 * @param key - Nombre de la preference.
	 * @param def - Valor por defecto.
	 * @return Valor de la preference en String.
	 */
	public static String getString( String key, String def )
	{
		return null != prefs ? prefs.getString( key, def ) : def;
	}



	/**
	 * Establece un valor String.
	 * 
	 * @param key - Nombre de la preference.
	 * @param value - Valor de la preference.
	 */
	public static void setString( String key, String value )
	{
		if ( null != prefs )
		{
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString( key, value ).apply();
		}
	}

}
