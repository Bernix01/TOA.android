package toa.toa.web;

/**
 * Creado por Hermosa Programación.
 */

/**
 * Creado por Hermosa Programación.
 * <p/>
 * Clase que representa un cliente HTTP Volley
 */

public final class VolleySingleton {

 /*   // Atributos
    private static VolleySingleton singleton;
    private static Context context;
    private RequestQueue requestQueue;


    private VolleySingleton(Context context) {
        VolleySingleton.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Retorna la instancia unica del singleton
     *
     * @param context contexto donde se ejecutarán las peticiones
     * @return Instancia
     */
  /*  public static synchronized VolleySingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new VolleySingleton(context.getApplicationContext());
        }
        return singleton;
    }*/

    /**
     * Obtiene la instancia de la cola de peticiones
     *
     * @return cola de peticiones
     */
  /*  public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }*/

    /**
     * Añade la petición a la cola
     *
     * @param req petición
     * @param <T> Resultado final de tipo T
     */
   /* public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
*/
}