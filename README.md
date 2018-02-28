# Batura Android Library

[![N|Batura](https://pbs.twimg.com/profile_images/935581638179860481/AWlr-ocN_400x400.jpg)


# Arquitectura librería

  La librería tiene 4 modulos diferenciados

  - Bluetooth
  - Design
  - MVP
  - Utils

## Bluetooth
[Pagina Wiki del bluetooth (En construcción)](BLUETOOTH.md)


  ## Design

Sección de librería que se encarga de:
- Dar soporte a los componentes antiguos para dar acceso a los vectores
en la parte derecha e izquierda del componente.
- Fuentes customizadas para versiones compiladas con version 8.0 o menor
- ProgressBar de Leon Cheng que te ayuda a crear un progressBar circular

### Batura ImageText

Es un tipo de compotente que te permite utilizar una imagen y un text como si fueran 1
con sistema de eventos integrado

![selected](resources/on.png)
![no_selected](resources/off.png)


  ## MVP
La librería base mvp se encarga de hacer una manera sencilla y sin mucho codigo BoilerPlate

  - ### Version 1
    - Sistema de mostrar loading Básico
    - Sistema de erorres Básico
    - Se puede hacer finalizar desde el presenter
    - Acceso del contexto desde el presenter
  - ### Versión 2
    - Sistema de injección de dependencias
  - ### Version 3
    - Sistema delegado para injección de vistas
      - Loading
      - Errores
      - Integrado con los lifecycleAnroid
    - Sistema delegado en activities para un posible customización de features modulables
    -Control del presenter y delegados con el lifecycle handle android


  ## Utils
  La librería de utils es uno de los pilares fundamentales de la librería. Está
  subdividida en diferentes modulos encargados de facilitar diferentes casos de usos


  ### Analytics (Desarrollo)
  Modulo que se quiere conseguir una plataforma estandar para integrar analiticas dentro de las apps
  ### Animations
  Modulo que integra unas animaciones básica (Slide desde la zona derecha) en el RecycleView
  ### Callbacks
  Este modulo integra un clase o interfaz Callback con su correspondiente generico

```java
public abstract class CallbackC<T> implements CallbackI<T> {
    @Override
    public void onResponse(@NotNull T dataResponse) {}

    @Override
    public void onError(int codeError, String stringError, Throwable ExceptionError) {}
}
```

#### V2

Una mejora es la integración del modulo de log con el modulo Callback registrando los errores de una manera mas automática

```java
open class CallbackV2<in T> {

    open fun onResponse(dataResponse: T) {}

    @CallSuper
    open fun onError(codeError: Int?, stringError: String?, exceptionError: Throwable?) {
        LogStaticV2.logInterfaceV2?.crashError(stringError,exceptionError)
    }
}
```


  ### Cluster (Falta ejemplo)
  El modulo de cluster está diseñado para la integración con google maps y los clusters
  creando un sistema mas facil para integrar los cluster en los futuros proyectos
  ### Network
  Incluye una serie de excepciones y manejos por los cuales intentamos estandarizar los errores mas comunes en
  los endpoints.

    - NetWorkException
    - NoInternetException -> Tipo de excepción para cuando no tenemos internet

   #### RetrofitStringConverterFactory
   Tambíen tenemos un helper para Retrofit para los casos en los que los endpoints nos devuelven un texto plano
  y saltaba la excepción de retrofit
  #### Modulos  para peticiones https
  No es recomendable utilizar los sistemas marcados con ***unsafe*** a no ser que tenga que ser estritamente necesario ya que perdemos
  totalmente la verificación de los certificados https. Tenemos un modo para **OkHttp y Glide**

  Aparte de estas implementaciones tenemos una lista para utilizar con los certificados comodo
  que antes de la versión 5.0 no funcionan correctamente


  ### List
[Pagina Wiki de la lista](LIST.md)

  ### Log
Un sistema unificado de logs para poder unificar los diferentes tipos de logs en la app y customizar
el canal


Esta es la interfaz que tenemos que implementar para poder trabajar con la verión 2
de los logs
  ```java
  interface LogInterfaceV2{

    fun log(priority: Int, TAG : String,message: String)

    fun crashError(message: String?,exception:Throwable?)

}
```
Esta es la clase singleton en la que tenemos inicializar nuestra implementación de la interfaz anterior
este sistema de logs también es utilizado por la librería por lo que si queremos debugear las librerías seria
recomendable utilizar este sistema
```java
  object  LogStaticV2{
    var logInterfaceV2 : LogInterfaceV2? = null
}
```
  ### Serializer
  En este apartado tenemos los siguientes serializadores y deserializadores

#### Serializador de fecha estandar ISO 8601 (testeado) de string a DateTime (JodaTime)
  - DateTimeSerialize
  - DateTimeDeSerialize (Gson)

### Storage
En este apartado disponemos de:

#### PreferenceManager
Un sistema que abstrae el control de preferencias de la aplicación android de forma única y
no instanciable(No se pueden crear varios preference manager)
 Contiene la adición y estracción de los siguientes elementos.
 - String
 - Booleano
 - Integer


### UI
En este apartado tenemos herramientas de UI que nos harán las funcionalidades mas habituales de forma mas sencilla

#### BitMapHelperV2
En este apartado tenemos una clase con sus extensiones de kotlin que nos permiten varias opciones

    getResizedBitmap
esta función nos permite reducir un bitmap en su anchura en tamaño sin que esté en memoria

    decodeSampledBitmapFromResource
Esta función nos nos permite lo mismo que la anterior funccion pero desde el resource de android

    decodeSampledBitmapFromFile
Esta función nos nos permite lo mismo que la anterior funccion pero desde un File
    Bitmap.rotateBitmap
Esta función nos permite rotar un bitmap desde con un origen de un archivo que tenga exif.
Esto nos permite controlar cuando una imagen tiene que ser rotada

    Bitmap.getEncoded64ImageStringFromBitmap
Esta funcion nos permite convertir un bitmap a un un string en base64 con una compresiobn de 70%

#### Statusbar
    changeStatusbarColor
Esta funcion cambiar la status bar de color
### Validators

#### CreditCardDetector
Te permite saber que tipo de tarjeta de credito estas insertando
Eletron, Maestro, Dankort, Interpayment, UnionPay, Visa, MasterCard, Amex, Diners, Discover, JCB
#### IDCardValidator
validador de Documento de identidad, por el momento solo el de español
#### VatValidator
validador de Documento de empresas, por el momento solo el de español
#### NoSpecialInputFilter
Un inputFilter que admite todos los carácteres menos los numéricos y los especiales
#### NoSpecialNumericInputFilter
Un InputFilter que admite todos los carácteres menos los numéricos y especiales
