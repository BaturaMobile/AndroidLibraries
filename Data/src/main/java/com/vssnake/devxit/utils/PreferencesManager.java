package com.vssnake.devxit.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.vssnake.devxit.data.JsonConverter;

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
	public String privatePrefName = "";


	JsonConverter jsonConverter;

	public PreferencesManager(String name,JsonConverter jsonConverter,Context context){
		privatePrefName = name;
		this.jsonConverter = jsonConverter;
		init(context);
	}




	/**
	 * Objeto SharedPreferences.
	 */
	protected SharedPreferences prefs;

	/***
	 * Inicializar las preferences.
	 */
	private void init( Context context )
	{
		if ( null == prefs )
		{
			prefs = context.getSharedPreferences( privatePrefName, Context.MODE_PRIVATE );
		}
	}

	/**
	 * Devuelve un valor boolean.
	 * 
	 * @param key - Nombre de la preference.
	 * @param def - Valor por defecto.
	 * @return Valor de la preference en boolean.
	 */
	public boolean getBoolean( String key, boolean def )
	{
		return ( null != prefs ) ? prefs.getBoolean( key, def ) : def;
	}

	/**
	 * Establece un valor boolean.
	 * 
	 * @param key - Nombre de la preference.
	 * @param value - Valor de la preference.
	 */
	public void setBoolean( String key, boolean value )
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
	public int getInt( String key, int def )
	{
		return ( null != prefs ) ? prefs.getInt( key, def ) : def;
	}

	/**
	 * Establece un valor int.
	 * 
	 * @param key - Nombre de la preference.
	 * @param value - Valor de la preference.
	 */
	public void setInt( String key, int value )
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
	public String getString( String key, String def )
	{
		return null != prefs ? prefs.getString( key, def ) : def;
	}

	/**
	 * Establece un valor String.
	 * 
	 * @param key - Nombre de la preference.
	 * @param value - Valor de la preference.
	 */
	public void setString( String key, String value )
	{
		if ( null != prefs )
		{
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString( key, value ).apply();
		}
	}

	public<T> void setObject( String key, T value) throws Throwable {
		String jsonValue = jsonConverter.toJson(value);

		if (null != prefs) {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(key, jsonValue).apply();
		}
	}

	public<T> T getObject( String key, Class<T> tClass) throws Throwable {

		T object = null;
		if (null != prefs) {
			String objectSerialized = prefs.getString(key,null);
			if (objectSerialized != null){
				object = jsonConverter.fromJSon(objectSerialized,tClass);
			}
		}
		return object;


	}




}
